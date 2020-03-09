package com.github.com.jorgdz.app.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.com.jorgdz.app.util.AppHelper;

@RestController
@CrossOrigin(AppHelper.CROSS_ORIGIN)
public class MainController {
	
	@GetMapping(value = {"/", "/index"}, produces = AppHelper.JSON)
	public ResponseEntity<?> index ()
	{
		Map<String, Object> map = new HashMap<>();
		map.put("gretting", "Hello user");
		return new ResponseEntity<>(map, HttpStatus.OK);
	}
}
