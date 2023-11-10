package com.weldogomes.api.url.business;

import java.util.List;

import com.weldogomes.api.url.Url;
import com.weldogomes.api.url.UrlEntryVO;

public interface UrlBusiness {

	public void create(Url url) throws UrlBusinessException;
	
	public Url create(UrlEntryVO urlVO) throws UrlBusinessException;
	
	public List<Url> list() throws UrlBusinessException;

	public List<String> listTop10() throws UrlBusinessException;

	public Url findByAlias(String alias) throws UrlBusinessException;
	
	public String findOriginalUrlByAlias(String alias) throws UrlBusinessException;
}
