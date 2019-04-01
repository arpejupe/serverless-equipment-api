package com.serverless.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.serverless.ApiGatewayResponse;
import com.serverless.Response;
import com.serverless.dao.EquipmentDao;
import com.serverless.model.Equipment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class GetEquipmentListHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

    static final Logger logger = LogManager.getLogger(GetEquipmentListHandler.class);

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
        logger.info("Handler received request: " + input);
        List<Equipment> equipmentList;
        try {
            this.validateInputParameters(input);
            Map<String, String> queryStringParameters = (Map<String, String>) input.get("queryStringParameters");
            Integer limit = Integer.parseInt(queryStringParameters.get("limit"));
            String lastEvaluatedKey = queryStringParameters.get("offset");
            EquipmentDao dynamoDB = new EquipmentDao();
            equipmentList = dynamoDB.getEquipmentList(limit, lastEvaluatedKey);
        }
        catch (IllegalArgumentException e) {
            logger.error(e);
            Response responseBody = new Response(e.getMessage(), input);
            return ApiGatewayResponse.builder()
                    .setStatusCode(500)
                    .setObjectBody(responseBody)
                    .setHeaders(createCORSHeaders())
                    .build();
        }
        catch (Exception e) {
            logger.error(e);
            Response responseBody = new Response("API failed fetching the equipment list", input);
            return ApiGatewayResponse.builder()
                    .setStatusCode(500)
                    .setObjectBody(responseBody)
                    .setHeaders(createCORSHeaders())
                    .build();
        }
        return ApiGatewayResponse.builder()
                .setStatusCode(200)
                .setObjectBody(equipmentList)
                .setHeaders(createCORSHeaders())
                .build();
    }

    //TODO: Proper input parameter validation for all handlers
    private void validateInputParameters(Map<String, Object> input) {
        if(input.get("queryStringParameters") == null) {
            throw new IllegalArgumentException("Query string paramters missing. You need to provide at least limit parameter");
        }
    }

    private Map<String, String> createCORSHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Access-Control-Allow-Origin", "*");
        headers.put("Access-Control-Allow-Headers", "Content-Type,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token");
        return headers;
    }



}
