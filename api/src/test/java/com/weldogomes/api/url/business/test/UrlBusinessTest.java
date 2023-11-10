package com.weldogomes.api.url.business.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import com.weldogomes.api.url.Url;
import com.weldogomes.api.url.UrlEntryVO;
import com.weldogomes.api.url.business.UrlBusiness;
import com.weldogomes.api.url.business.UrlBusinessImpl;
import com.weldogomes.api.url.repository.UrlRepository;

@SpringBootTest
public class UrlBusinessTest {

	private static final String EXCEPTION_FAIL = "Exceção deveria ter sido lançada.";
	
	@Mock
	private UrlRepository urlRepository;
	
	@InjectMocks
	private UrlBusiness urlBusiness = new UrlBusinessImpl();
	
	@Test
	public void testSuccessCreate() {
		try {			
			Url url = new Url();
			urlBusiness.create(url);
			
			Mockito.verify(urlRepository).save(url);
		} catch (Exception e) {
			Assertions.fail(e.getMessage());
		}
	}
	
	@Test
	public void testFailCreate() {
		try {
			Url url = new Url();
			Mockito.when(urlRepository.save(url)).thenThrow(new RuntimeException());
			
			urlBusiness.create(url);
			
			Assertions.fail(EXCEPTION_FAIL);
		} catch (Exception e) {
			Assertions.assertEquals("Erro ao salvar URL.", e.getMessage());
		}
	}
	
	@Test
	public void testSuccessCreateWithUrlVOWithoutCustomAlias() {
		try {
			UrlEntryVO urlVO = new UrlEntryVO("http://test.com", null);
			Url url = new Url();
			url.setOriginalUrl(urlVO.originalUrl());
			
			Mockito.when(urlRepository.findByAlias(ArgumentMatchers.anyString())).thenReturn(null);
			
			Assertions.assertEquals(url, urlBusiness.create(urlVO));
		} catch (Exception e) {
			Assertions.fail(e.getMessage());
		}
	}
	
	@Test
	public void testFailCreateWithUrlVOWithoutCustomAliasGenerateAliasMaxAttempts() {
		try {
			UrlEntryVO urlVO = new UrlEntryVO("http://test.com", null);
			Url url = new Url();
			url.setOriginalUrl(urlVO.originalUrl());
			
			Mockito.when(urlRepository.findByAlias(ArgumentMatchers.anyString())).thenReturn(new Url());
			
			urlBusiness.create(urlVO);
			
			Assertions.fail(EXCEPTION_FAIL);
		} catch (Exception e) {
			Assertions.assertEquals("Tentativas de geração de alias esgotadas.", e.getMessage());
		}
	}
	
	@Test
	public void testSuccessCreateWithUrlVOWithCustomAlias() {
		try {
			UrlEntryVO urlVO = new UrlEntryVO("http://test.com", "teste");
			Url url = new Url();
			url.setOriginalUrl(urlVO.originalUrl());
			
			Mockito.when(urlRepository.findByAlias(ArgumentMatchers.anyString())).thenReturn(null);
			
			Assertions.assertEquals(url, urlBusiness.create(urlVO));
		} catch (Exception e) {
			Assertions.fail(e.getMessage());
		}
	}
	
	@Test
	public void testFailCreateWithUrlVOWithCustomAliasExistingAlias() {
		try {
			UrlEntryVO urlVO = new UrlEntryVO("http://test.com", "teste");
			Url url = new Url();
			url.setOriginalUrl(urlVO.originalUrl());
			
			Mockito.when(urlRepository.findByAlias(urlVO.customAlias())).thenReturn(new Url());
			
			urlBusiness.create(urlVO);
			
			Assertions.fail(EXCEPTION_FAIL);
		} catch (Exception e) {
			Assertions.assertEquals("Alias já existe.", e.getMessage());
		}
	}
	
	@Test
	public void testSuccessListTop10() {
		try {
			List<String> list = new ArrayList<String>();
			
			Mockito.when(urlRepository.listTop10()).thenReturn(list);
			
			Assertions.assertEquals(list, urlBusiness.listTop10());
		} catch (Exception e) {
			Assertions.fail(e.getMessage());
		}
	}
	
	@Test
	public void testFailListTop10() {
		try {
			Mockito.when(urlRepository.listTop10()).thenThrow(new RuntimeException());
			
			urlBusiness.listTop10();
			
			Assertions.fail(EXCEPTION_FAIL);
		} catch (Exception e) {
			Assertions.assertEquals("Erro ao listar Top 10 URLs.", e.getMessage());
		}
	}
	
	@Test
	public void testSuccessFindByAlias() {
		try {
			Url url = new Url();
			String param = "teste";

			Mockito.when(urlRepository.findByAlias(param)).thenReturn(url);
			
			Assertions.assertEquals(url, urlBusiness.findByAlias(param));
		} catch (Exception e) {
			Assertions.fail(e.getMessage());
		}
	}
	
	@Test
	public void testFailFindByAlias() {
		try {
			String param = "teste";
			
			Mockito.when(urlRepository.findByAlias(param)).thenThrow(new RuntimeException());
			
			urlBusiness.findByAlias(param);
			
			Assertions.fail(EXCEPTION_FAIL);
		} catch (Exception e) {
			Assertions.assertEquals("Erro ao procurar URL pelo alias.", e.getMessage());
		}
	}
	
	@Test
	public void testSuccessFindOriginalUrlByAlias() {
		try {
			String alias = "teste";
			String originalUrl = "http://test.com";
			
			Url url = new Url();
			url.setOriginalUrl(originalUrl);
			url.setAlias(alias);
			
			Mockito.when(urlRepository.findByAlias(alias)).thenReturn(url);
			
			Assertions.assertEquals(originalUrl, urlBusiness.findOriginalUrlByAlias(alias));
		} catch (Exception e) {
			Assertions.fail(e.getMessage());
		}
	}
	
	@Test
	public void testFailFindOriginalUrlByAliasNotFound() {		
		try {
			String alias = "teste";
			
			Mockito.when(urlRepository.findByAlias(alias)).thenReturn(null);
			
			urlBusiness.findOriginalUrlByAlias(alias);
			
			Assertions.fail(EXCEPTION_FAIL);
		} catch (Exception e) {
			Assertions.assertEquals("Alias não encontrado.", e.getMessage());
		}
	}
}
