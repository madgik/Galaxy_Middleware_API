/*
 * Developed by Kechagias Konstantinos.
 * Copyright (c) 2019. MIT License
 */

package gr.di.uoa.kk.middlewareAPI.RestControllers;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/noAuth/")
public class NoAuthAPIs {
    private static final Logger logger = LoggerFactory.getLogger(NoAuthAPIs.class);
}

