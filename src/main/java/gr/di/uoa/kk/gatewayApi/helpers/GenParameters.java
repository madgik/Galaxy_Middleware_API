/*
 * Developed by Kechagias Konstantinos.
 * Copyright (c) 2019. MIT License
 */

package gr.di.uoa.kk.gatewayApi.helpers;

public class GenParameters {
    public static final String jwtSecret = "hbpSecret";
    public static final String jwtIssuer = "mip.humanbrainproject.eu";
    public static final String galaxyURL = "http://88.197.53.102";
    public static final String galaxyApiKey = "b330573c1fa511187998cb3233647d34";

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