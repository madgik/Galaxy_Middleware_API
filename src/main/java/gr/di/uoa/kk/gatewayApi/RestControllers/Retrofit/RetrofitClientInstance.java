/*
 * Developed by Kechagias Konstantinos.
 * Copyright (c) 2019. MIT License
 */

package gr.di.uoa.kk.gatewayApi.RestControllers.Retrofit;

import gr.di.uoa.kk.gatewayApi.helpers.GenParameters;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientInstance {

    private static final Logger logger = LoggerFactory.getLogger(RetrofitClientInstance.class);

    private static Retrofit retrofit;

    private static final String BASE_URL = GenParameters.getGalaxyURL() + "/api/";
    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {

            logger.info("BaseURL : " + BASE_URL);

            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
