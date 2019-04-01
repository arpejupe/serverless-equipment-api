package com.serverless.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.serverless.ApiGatewayResponse;
import com.serverless.Response;
import com.serverless.dao.EquipmentDao;
import com.serverless.model.Equipment;
import com.serverless.util.exception.DuplicateEquipmentException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

public class PostEquipmentHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

    static final Logger logger = LogManager.getLogger(PostEquipmentHandler.class);
    static final EquipmentDao dynamoDB = new EquipmentDao();

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
        logger.info("Handler received request: " + input);
        Equipment equipment;
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        try {
            equipment = mapper.readValue(input.get("body").toString(), Equipment.class);
            logger.error("Body", equipment);
            this.dynamoDB.saveEquipment(equipment);
            return ApiGatewayResponse.builder()
                    .setStatusCode(201)
                    .setObjectBody(equipment)
                    .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & serverless"))
                    .build();
        } catch (DuplicateEquipmentException e) {
            Response responseBody = new Response(e.getMessage(), input);
            return ApiGatewayResponse.builder()
                    .setStatusCode(403)
                    .setObjectBody(responseBody)
                    .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & serverless"))
                    .build();
        } catch (Exception e) {
           logger.error(e);
        }
        Response responseBody = new Response("API failed to create Equipment", input);
        return ApiGatewayResponse.builder()
                .setStatusCode(500)
                .setObjectBody(responseBody)
                .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & serverless"))
                .build();
    }

}
