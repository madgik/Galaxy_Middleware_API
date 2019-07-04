/*
 * Developed by Kechagias Konstantinos.
 * Copyright (c) 2019. MIT License
 */

package gr.di.uoa.kk.middlewareAPI.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import gr.di.uoa.kk.middlewareAPI.helpers.GenParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class JwtProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtProvider.class);

    private final String jwtSecret = GenParameters.getGenParamInstance().getJwtSecret();

    private final String jwtIssuer = GenParameters.getGenParamInstance().getJwtIssuer();

    public String getSubjectFromJwtToken(String token) {
        DecodedJWT jwt = null;
        try {
            Algorithm algorithm = Algorithm.HMAC512(jwtSecret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(jwtIssuer)
                    .build(); //Reusable verifier instance
            jwt = verifier.verify(token);
        } catch (JWTVerificationException e){
            logger.error("Cannot validate JWT token", e);
        }
        return jwt.getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            logger.info("jwtSecret : " + jwtSecret);
            logger.info("jwtIssuer : " + jwtIssuer);
            Algorithm algorithm = Algorithm.HMAC512(jwtSecret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(jwtIssuer)
                    .build(); //Reusable verifier instance
            DecodedJWT jwt = verifier.verify(authToken);
            return true;
        } catch (JWTVerificationException e){
            logger.error("Cannot validate JWT token", e);
        }
        return false;
    }
}
