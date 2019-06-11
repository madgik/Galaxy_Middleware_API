/*
 * Developed by Kechagias Konstantinos.
 * Copyright (c) 2019. MIT License
 */

package gr.di.uoa.kk.gatewayApi.helpers;

import org.springframework.security.core.userdetails.UserDetails;

public class LogHelper {
    public static String logUser(UserDetails userDetails){
        return ("User(subject)->User(" + userDetails.getUsername() + ") : ");
    }
}
