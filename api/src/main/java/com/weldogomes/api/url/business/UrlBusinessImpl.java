package com.weldogomes.api.url.business;

import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.time.Instant;
import java.util.List;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.weldogomes.api.ApiUtils;
import com.weldogomes.api.url.Url;
import com.weldogomes.api.url.UrlEntryVO;
import com.weldogomes.api.url.repository.UrlRepository;

@Service
public class UrlBusinessImpl implements UrlBusiness{

	private static final int ALIAS_MAX_SIZE = 10;
	private static final int MAX_ATTEMPTS = 100;
	
	@Autowired
	private UrlRepository urlRepository;
	
	@Override
	public void create(Url url) throws UrlBusinessException {
		try {
			urlRepository.save(url);
		} catch (Exception e) {
			throw new UrlBusinessException("Erro ao salvar URL.", e);
		}
	}
	
	@Override
	public Url create(UrlEntryVO urlVO) throws UrlBusinessException {
		try {			
			Url url = new Url();
			url.setOriginalUrl(urlVO.originalUrl());
			
			if(ApiUtils.isNullOrEmpty(urlVO.customAlias())) {
				url.setAlias(generateAlias());
			} else {
				try {
					Url aux = new Url();
					aux = findByAlias(urlVO.customAlias());
					if(aux != null)
						throw new UrlBusinessException("Alias já existe.");
					url.setAlias(urlVO.customAlias());
				} catch (UrlBusinessException e1) {
					throw e1;
				} catch (Exception e) {
					throw new UrlBusinessException("Erro ao verificar existencia de alias.", e);
				}
			}
			
			create(url);

			return url;
		} catch (UrlBusinessException e1) {
			throw e1;
		} catch (Exception e) {
			throw new UrlBusinessException("Erro ao salvar URL.", e);
		}
		
	}
	
	@Override
	public List<String> listTop10() throws UrlBusinessException {
		try {
			return urlRepository.listTop10();
		} catch (Exception e) {
			throw new UrlBusinessException("Erro ao listar Top 10 URLs.", e);
		}
	} 

	public String generateAlias() throws UrlBusinessException {
		try {
			int counter = 0;
			
			String hashAlias = new String();
			Url auxUrl = new Url();
			
			while(counter < MAX_ATTEMPTS) {
				String timestamp = Instant.now().toString();

				SecureRandom random = new SecureRandom();
				byte[] salt = new byte[16];
				random.nextBytes(salt);
				
				KeySpec spec = new PBEKeySpec(timestamp.toCharArray(), salt, 65536, 128);
				SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
				
				byte[] hash = factory.generateSecret(spec).getEncoded();
				
		        hashAlias = hash.toString().substring(0, Math.min(hash.toString().length(), ALIAS_MAX_SIZE));
		        auxUrl = findByAlias(hashAlias);
		        
				if(!ApiUtils.isNullOrEmpty(hashAlias) && auxUrl == null)
					break;
				
				counter++;
			}
			
			if(counter >= MAX_ATTEMPTS)
				throw new UrlBusinessException("Tentativas de geração de alias esgotadas.");
			
			return hashAlias;
		} catch (UrlBusinessException e1) {
			throw e1;
		} catch (Exception e) {
			throw new UrlBusinessException("Erro ao gerar alias.", e);
		}
	}
	
	@Override
	public Url findByAlias(String alias) throws UrlBusinessException {
		try {
			return urlRepository.findByAlias(alias);
		} catch (Exception e) {
			throw new UrlBusinessException("Erro ao procurar URL pelo alias.", e);
		}
	}
	
	@Override
	public String findOriginalUrlByAlias(String alias) throws UrlBusinessException {
		try {
			Url url = new Url();
			url = findByAlias(alias);
			if(url == null)
				throw new UrlBusinessException("Alias não encontrado.");
			return url.getOriginalUrl();
		} catch (UrlBusinessException e1) {
			throw e1;
		} catch (Exception e) {
			throw new UrlBusinessException("Erro ao procurar URL original pelo alias.", e);
		}
	}

}
