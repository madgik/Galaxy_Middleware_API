/*
 * Developed by Kechagias Konstantinos.
 * Copyright (c) 2019. MIT License
 */

package gr.di.uoa.kk.gatewayApi.RestControllers;

import com.github.jmchilton.blend4j.galaxy.GalaxyInstance;
import com.github.jmchilton.blend4j.galaxy.GalaxyInstanceFactory;
import com.github.jmchilton.blend4j.galaxy.WorkflowsClient;
import com.github.jmchilton.blend4j.galaxy.beans.Workflow;
import com.github.jmchilton.blend4j.galaxy.beans.WorkflowDetails;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import gr.di.uoa.kk.gatewayApi.RestControllers.Retrofit.RetroFitGalaxyClients;
import gr.di.uoa.kk.gatewayApi.RestControllers.Retrofit.RetrofitClientInstance;
import gr.di.uoa.kk.gatewayApi.dto.GetWorkflowResultsFromGalaxyDtoResponse;
import gr.di.uoa.kk.gatewayApi.dto.PostWorkflowToGalaxyDtoResponse;
import gr.di.uoa.kk.gatewayApi.helpers.LogHelper;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.*;



@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/")
class RestControllerAPIs {

    private static final Logger logger = LoggerFactory.getLogger(RestControllerAPIs.class);

    //The galaxy URL
    @Value("${kk.app.galaxyURL}")
    private String url;

    //The galaxy ApiKey
    @Value("${kk.app.galaxyApiKey}")
    private String apiKey;

    /**
     * Get all the workflows with few details.
     *
     * @return Return a @{@link ResponseEntity}.
     */
    @GetMapping(value = "getWorkflows", produces = "application/json")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity getWorkflows(@AuthenticationPrincipal UserDetails userDetails) {
        logger.info(LogHelper.logUser(userDetails) + "Get workflows called");

        final GalaxyInstance instance = GalaxyInstanceFactory.get(url, apiKey);
        final WorkflowsClient workflowsClient = instance.getWorkflowsClient();

        ArrayList<Workflow> workflowArrayList = new ArrayList<>();
        workflowArrayList.addAll(workflowsClient.getWorkflows());
        logger.info(LogHelper.logUser(userDetails) + "Get workflows completed");

        return ResponseEntity.ok(workflowArrayList);
    }

    /**
     * Get details for a specific workflow.
     *
     * @param id : The id as @{@link String} for the specific workflow.
     * @return Return a @{@link ResponseEntity}.
     */
    @GetMapping(value = "/getDetailWorkflow/{id}", produces = "application/json")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity getDetailWorkflow(@AuthenticationPrincipal UserDetails userDetails, @PathVariable String id) {
        logger.info(LogHelper.logUser(userDetails) + "Get detail workflow called");

        final GalaxyInstance instance = GalaxyInstanceFactory.get(url, apiKey);
        final WorkflowsClient workflowsClient = instance.getWorkflowsClient();

        Workflow matchingWorkflow = null;
        for(Workflow workflow : workflowsClient.getWorkflows()) {
            if(workflow.getId().equals(id)) {
                matchingWorkflow = workflow;
            }
        }
        if(matchingWorkflow == null){
            logger.error(LogHelper.logUser(userDetails) + "Get detail workflow could not find workflow with id : " + id);
            return ResponseEntity.notFound().build();
        }
        final WorkflowDetails workflowDetails = workflowsClient.showWorkflow(matchingWorkflow.getId());
        logger.info(LogHelper.logUser(userDetails) + "Get detail workflow completed");

        return ResponseEntity.ok(workflowDetails);
    }

    /**
     * Get all the workflows with details.
     *
     * @return Return a @{@link ResponseEntity}.
     */
    @GetMapping(value = "getAllWorkflowWithDetails", produces = "application/json")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity getAllWorkflowWithDetails(@AuthenticationPrincipal UserDetails userDetails) {
        logger.info(LogHelper.logUser(userDetails) + "Get all workflow with details called");

        final GalaxyInstance instance = GalaxyInstanceFactory.get(url, apiKey);
        final WorkflowsClient workflowsClient = instance.getWorkflowsClient();

        List<Workflow> workflowList = new ArrayList<Workflow>();
        for(Workflow workflow : workflowsClient.getWorkflows()) {
            workflowList.add(workflow);
        }

        List<WorkflowDetails> workflowDetailsList = new ArrayList<>();
        for(Workflow workflow : workflowList){
            workflowDetailsList.add(workflowsClient.showWorkflow(workflow.getId()));
        }
        logger.info(LogHelper.logUser(userDetails) + "Get all workflow with details completed");

        return ResponseEntity.ok(workflowDetailsList);
    }

    /**
     * Invoke a workflow.
     *
     * @param id : The id as @{@link String} of the workflow.
     * @param httpEntity : The @{@link HttpEntity} to get the body of the request which is the parameter of the workflow.
     * @return Return a @{@link ResponseEntity}.
     */
    @PostMapping(value = "/runWorkflow/{id}", produces = "application/json")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity runWorkflow(@AuthenticationPrincipal UserDetails userDetails, @PathVariable String id, HttpEntity<String> httpEntity) {
        logger.info(LogHelper.logUser(userDetails) + "Run workflow called");

        //In order to parse Json with undefined number of value/key
        String json = httpEntity.getBody();
        JSONObject jObject  = null;
        try {
            jObject = new JSONObject(json);
        } catch (JSONException e) {
            logger.error(LogHelper.logUser(userDetails) + "Cannot parse JSON", e);
        }
        Map<String,String> allJsonParams = new HashMap<String,String>();
        Iterator iter = jObject.keys();
        while(iter.hasNext()){
            String key = (String)iter.next();
            String value = null;
            try {
                value = jObject.getString(key);
            } catch (JSONException e) {
                logger.error(LogHelper.logUser(userDetails) + "Cannot parse JSON", e);
            }
            logger.info(LogHelper.logUser(userDetails) + "Put to map: " + key + " : " + value);
            allJsonParams.put(key,value);
        }

        StringBuffer stringBuffer = new StringBuffer("{\n" +
                "\t\"inputs\": {\n");
        for (Map.Entry<String, String> entry : allJsonParams.entrySet()) {
            stringBuffer.append("\t\t\"" + entry.getKey() + "\" " + " : \"" + entry.getValue() + "\",\n");
            logger.debug(LogHelper.logUser(userDetails) + entry.getKey() + "/" + entry.getValue());
        }
        //Remove Last Comma
        stringBuffer.deleteCharAt(stringBuffer.length() - 2);
        stringBuffer.append("\t}\n");
        stringBuffer.append("}");
        logger.info(LogHelper.logUser(userDetails) + stringBuffer.toString());

        JsonObject jsonObject = new JsonParser().parse(stringBuffer.toString()).getAsJsonObject();

        RetroFitGalaxyClients service = RetrofitClientInstance.getRetrofitInstance().create(RetroFitGalaxyClients.class);
        Call<PostWorkflowToGalaxyDtoResponse> call = service.postWorkflowToGalaxy(id, apiKey, jsonObject);

        PostWorkflowToGalaxyDtoResponse postWorkflowToGalaxyDtoResponse = null;
        try {
            Response<PostWorkflowToGalaxyDtoResponse> response = call.execute();
            if(response.code() >= 400){
                logger.error(LogHelper.logUser(userDetails) + "Resonse code: " + response.code() + "" + " with body: " + response.errorBody().string());
                return ResponseEntity.badRequest().build();
            }
            postWorkflowToGalaxyDtoResponse = response.body();
            logger.info(LogHelper.logUser(userDetails) + "----" + response.body() + "----" + response.code());
        } catch (IOException e) {
            logger.error(LogHelper.logUser(userDetails) + "Cannot make the call to Galaxy API", e);
        }
        logger.info(LogHelper.logUser(userDetails) + "Run workflow completed");

        return ResponseEntity.ok(postWorkflowToGalaxyDtoResponse);
    }

    /**
     * Get the status of a specific workflow.
     *
     * @param id : The id as @{@link String} of the workflow.
     * @return Return a @{@link ResponseEntity}.
     */
    @GetMapping(value = "/getWorkflowStatus/{id}", produces = "application/json")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity getWorkflowStatus(@AuthenticationPrincipal UserDetails userDetails, @PathVariable String id) {
        logger.info(LogHelper.logUser(userDetails) + "Get workflow status called");

        RetroFitGalaxyClients service = RetrofitClientInstance.getRetrofitInstance().create(RetroFitGalaxyClients.class);
        Call<Object> call = service.getWorkflowStatusFromGalaxy(id,apiKey);

        String jsonString = null;
        try {
            Response<Object> response = call.execute();
            if(response.code() >= 400){
                logger.error(LogHelper.logUser(userDetails) + "Resonse code: " + response.code() + "" + " with body: " + response.errorBody().string());
                return ResponseEntity.badRequest().build();
            }
            jsonString = new Gson().toJson(response.body());
            logger.info(LogHelper.logUser(userDetails) + jsonString);

            logger.info(LogHelper.logUser(userDetails) + "----" + response.body() + "----" + response.code());
        } catch (IOException e) {
            logger.error(LogHelper.logUser(userDetails) + "Cannot make the call to Galaxy API", e);
        }
        logger.info(LogHelper.logUser(userDetails) + "Get workflow status completed");

        return ResponseEntity.ok(jsonString);
    }

    /**
     * Get the result of a specific workflow.
     *
     * @param id : The id as @{@link String} of the workflow.
     * @return Return a @{@link ResponseEntity}.
     */
    @GetMapping(value = "/getWorkflowResults/{id}", produces = "application/json")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity getWorkflowResults(@AuthenticationPrincipal UserDetails userDetails, @PathVariable String id) {
        logger.info(LogHelper.logUser(userDetails) + "Get workflow results called");

        RetroFitGalaxyClients service = RetrofitClientInstance.getRetrofitInstance().create(RetroFitGalaxyClients.class);
        Call<List<GetWorkflowResultsFromGalaxyDtoResponse>> call = service.getWorkflowResultsFromGalaxy(id,apiKey);

        List<GetWorkflowResultsFromGalaxyDtoResponse> getWorkflowResultsFromGalaxyDtoResponsesList = null;
        try {
            Response<List<GetWorkflowResultsFromGalaxyDtoResponse>> response = call.execute();
            if(response.code() >= 400){
                logger.error(LogHelper.logUser(userDetails) + "Resonse code: " + response.code() + "" + " with body: " + response.errorBody().string());
                return ResponseEntity.badRequest().build();
            }
            getWorkflowResultsFromGalaxyDtoResponsesList = response.body();
        } catch (IOException e) {
            logger.error(LogHelper.logUser(userDetails) + "Cannot make the call to Galaxy API", e);
        }
        logger.info(LogHelper.logUser(userDetails) + "Get workflow results completed");

        return ResponseEntity.ok(getWorkflowResultsFromGalaxyDtoResponsesList);
    }

    /**
     * Get the result body of a specific workflow.
     *
     * @param id : The id as @{@link String} of the workflow.
     * @param contentId : The content id as @{@link String}.
     * @return Return a @{@link ResponseEntity}.
     */
    @GetMapping(value = "/getWorkflowResultsBody/{id}/contents/{contentId}", produces = "application/json")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity getWorkflowResultsBody(@AuthenticationPrincipal UserDetails userDetails, @PathVariable String id, @PathVariable String contentId) {
        logger.info(LogHelper.logUser(userDetails) + "Get workflow results body called");

        RetroFitGalaxyClients service = RetrofitClientInstance.getRetrofitInstance().create(RetroFitGalaxyClients.class);
        Call<Object> call = service.getWorkflowResultsBodyFromGalaxy(id,contentId,apiKey);

        String jsonString = null;
        try {
            Response<Object> response = call.execute();
            if(response.code() >= 400){
                logger.error(LogHelper.logUser(userDetails) + "Resonse code: " + response.code() + "" + " with body: " + response.errorBody().string());
                return ResponseEntity.badRequest().build();
            }
            jsonString = new Gson().toJson(response.body());
            logger.info(LogHelper.logUser(userDetails) + "----" + response.body() + "----" + response.code());
        } catch (IOException e) {
            logger.error(LogHelper.logUser(userDetails) + "Cannot make the call to Galaxy API", e);
        }
        logger.info(LogHelper.logUser(userDetails) + "Get workflow results body completed");

        return ResponseEntity.ok(jsonString);
    }

    /**
     * Get the result body of a specific workflow with details.
     *
     * @param id : The id as @{@link String} of the workflow.
     * @param contentId : The content id as @{@link String}.
     * @return Return a @{@link ResponseEntity}.
     */
    @GetMapping(value = "/getWorkflowResultsDetails/{id}/contents/{contentId}", produces = "application/json")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity getWorkflowResultsDetails(@AuthenticationPrincipal UserDetails userDetails, @PathVariable String id, @PathVariable String contentId) {
        logger.info(LogHelper.logUser(userDetails) + "Get workflow results details called");

        RetroFitGalaxyClients service = RetrofitClientInstance.getRetrofitInstance().create(RetroFitGalaxyClients.class);
        Call<Object> call = service.getWorkflowResultsDetailsFromGalaxy(id,contentId,apiKey);

        String jsonString = null;
        try {
            Response<Object> response = call.execute();
            if(response.code() >= 400){
                logger.error(LogHelper.logUser(userDetails) + "Resonse code: " + response.code() + "" + " with body: " + response.errorBody().string());
                return ResponseEntity.badRequest().build();
            }
            jsonString = new Gson().toJson(response.body());
            System.out.println(jsonString);
            logger.info(LogHelper.logUser(userDetails) + "----" + response.body() + "----" + response.code());
        } catch (IOException e) {
            logger.error(LogHelper.logUser(userDetails) + "Cannot make the call to Galaxy API", e);
        }
        logger.info(LogHelper.logUser(userDetails) + "Get workflow results details completed");

        return ResponseEntity.ok(jsonString);
    }
}
