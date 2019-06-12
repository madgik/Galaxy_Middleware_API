/*
 * Developed by Kechagias Konstantinos.
 * Copyright (c) 2019. MIT License
 */

package gr.di.uoa.kk.gatewayApi.helpers;

public class GenParameters {
    public static final String jwtSecret = "";
    public static final String jwtIssuer = "";
    public static final String galaxyURL = "";
    public static final String galaxyApiKey = "";

    public static String getJwtSecret() {
        return jwtSecret;
    }

    public static String getJwtIssuer() {
        return jwtIssuer;
    }

    public static String getGalaxyURL() {
        return galaxyURL;
    }

    public static String getGalaxyApiKey() {
        return galaxyApiKey;
    }
}