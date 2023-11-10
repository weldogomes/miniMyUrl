package com.weldogomes.api.url.business;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.weldogomes.api.ApiUtils;
import com.weldogomes.api.url.Url;
import com.weldogomes.api.url.UrlEntryVO;
import com.weldogomes.api.url.repository.UrlRepository;

@Service
public class UrlBusinessImpl implements UrlBusiness{

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
				} catch (Exception e) {
					throw new UrlBusinessException("Erro ao verificar existencia de alias.", e);
				}
			}
			
			create(url);

			return url;
		} catch (Exception e) {
			throw new UrlBusinessException("Erro ao salvar URL.", e);
		}
		
	}

	@Override
	public List<Url> list() throws UrlBusinessException {
		try {
			return urlRepository.findAll();
		} catch (Exception e) {
			throw new UrlBusinessException("Erro ao listar URLs.", e);
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
			
			Integer aliasMaxSize = 0;
			aliasMaxSize = urlRepository.getAliasSize();
			
			String hashAlias = new String();
			Url auxUrl = new Url();
			
			for (; counter < MAX_ATTEMPTS; counter++) {
				String uuid = UUID.randomUUID().toString().replaceAll("-", "");
		        String timestamp = Instant.now().toString();
		        
		        String uniqueString = uuid + timestamp;
		        
		        hashAlias = uniqueString.substring(0, Math.min(uniqueString.length(), aliasMaxSize));
		        auxUrl = findByAlias(hashAlias);
		        
				if(!ApiUtils.isNullOrEmpty(hashAlias) && auxUrl == null)
					break;
			}
			
			if(!(counter < MAX_ATTEMPTS))
				throw new UrlBusinessException("Tentativas de geração de alias esgotadas.");
			
			return hashAlias;
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
				throw new UrlBusinessException("Alias invalido.");
			return url.getOriginalUrl();
		} catch (Exception e) {
			throw new UrlBusinessException("Erro ao procurar URL original pelo alias.", e);
		}
	}

}
