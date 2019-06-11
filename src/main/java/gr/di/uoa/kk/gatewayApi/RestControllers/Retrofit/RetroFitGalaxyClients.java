/*
 * Developed by Kechagias Konstantinos.
 * Copyright (c) 2019. MIT License
 */

package gr.di.uoa.kk.gatewayApi.RestControllers.Retrofit;

import com.google.gson.JsonObject;
import gr.di.uoa.kk.gatewayApi.dto.GetWorkflowResultsFromGalaxyDtoResponse;
import gr.di.uoa.kk.gatewayApi.dto.PostWorkflowToGalaxyDtoResponse;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface RetroFitGalaxyClients {

    @POST("workflows/{workflowId}/invocations")
    Call<PostWorkflowToGalaxyDtoResponse> postWorkflowToGalaxy(@Path("workflowId") String workflowId, @Query("key") String key, @Body JsonObject body);

    @GET("histories/{historyId}")
    Call<Object> getWorkflowStatusFromGalaxy(@Path("historyId") String historyId, @Query("key") String key);

    @GET("histories/{historyId}/contents")
    Call<List<GetWorkflowResultsFromGalaxyDtoResponse>> getWorkflowResultsFromGalaxy(@Path("historyId") String historyId, @Query("key") String key);

    @GET("histories/{historyId}/contents/{contentId}/display")
    Call<Object> getWorkflowResultsBodyFromGalaxy(@Path("historyId") String historyId, @Path("contentId") String contentId, @Query("key") String key);

    @GET("histories/{historyId}/contents/{contentId}")
    Call<Object> getWorkflowResultsDetailsFromGalaxy(@Path("historyId") String historyId, @Path("contentId") String contentId, @Query("key") String key);

}