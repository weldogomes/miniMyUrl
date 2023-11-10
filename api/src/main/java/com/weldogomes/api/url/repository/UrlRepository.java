package com.weldogomes.api.url.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.weldogomes.api.url.Url;

@Repository
public interface UrlRepository extends JpaRepository<Url, Long>	{
	
	@Query("SELECT U.originalUrl " +
			"FROM Url U " + 
			"GROUP BY originalUrl " + 
			"ORDER BY COUNT(originalUrl) DESC " + 
			"LIMIT 10")
	public List<String> listTop10();
	
	public Url findByAlias(String alias);
	
	@Query(value = "SELECT CHARACTER_MAXIMUM_LENGTH " +
			"FROM INFORMATION_SCHEMA.COLUMNS " +
			"WHERE TABLE_NAME = 'URL' AND COLUMN_NAME = 'ALIAS'", nativeQuery = true)
	public Integer getAliasSize();
}
