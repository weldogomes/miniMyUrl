package com.weldogomes.api.url.business.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import com.weldogomes.api.url.Url;
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
			
			Mockito.verify(urlRepository, Mockito.times(1)).save(url);
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
			Assertions.assertEquals(e.getMessage(), "Erro ao salvar URL.");
		}
	}
	
	public void testSuccessCreateWithUrlVO() {}
}
