package com.weldogomes.api.controller;

import java.time.Duration;
import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.weldogomes.api.url.Url;
import com.weldogomes.api.url.UrlEntryVO;
import com.weldogomes.api.url.UrlSuccessResponseVO;
import com.weldogomes.api.url.UrlSuccessResponseWithTimeVO;
import com.weldogomes.api.url.business.UrlBusiness;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/url")
public class UrlController {
	
	@Autowired
	UrlBusiness urlBusiness;
	
	@PostMapping("/create")
	@Transactional
	public ResponseEntity<?> create(@RequestBody @Valid UrlEntryVO urlVO) {
		try {
			Instant start = Instant.now();
			Url url = urlBusiness.create(urlVO);
			Instant finish = Instant.now();
            Long timeTaken = Duration.between(start, finish).toMillis();
			
			return ResponseEntity.ok(new UrlSuccessResponseWithTimeVO(new UrlSuccessResponseVO(url), timeTaken.toString() + "ms"));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/a/{alias}")
	public ResponseEntity<?> acccess(@PathVariable @Valid String alias) {
		try {
			return ResponseEntity.ok(urlBusiness.findByAlias(alias).getOriginalUrl());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/listTop10")
	public ResponseEntity<?> listTop10() {
		try {
			return ResponseEntity.ok(urlBusiness.listTop10());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
}