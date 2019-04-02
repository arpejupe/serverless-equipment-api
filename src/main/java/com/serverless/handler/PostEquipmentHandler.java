package com.serverless.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.serverless.api.ApiGatewayResponse;
import com.serverless.api.Response;
import com.serverless.dao.Dao;
import com.serverless.model.Equipment;
import com.serverless.util.EquipmentInjector;
import com.serverless.util.HandlerUtil;
import com.serverless.util.exception.DuplicateEquipmentException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public class PostEquipmentHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

    static final Logger logger = LogManager.getLogger(PostEquipmentHandler.class);

    Injector injector = Guice.createInjector(new EquipmentInjector());
    Dao dao = injector.getInstance(Dao.class);

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
        logger.info("PostEquipmentHandler received request: " + input);
        Equipment equipment;
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        try {
            equipment = mapper.readValue(input.get("body").toString(), Equipment.class);
            this.dao.saveEquipment(equipment);
            return ApiGatewayResponse.builder()
                    .setStatusCode(201)
                    .setObjectBody(equipment)
                    .setHeaders(HandlerUtil.createCORSHeaders())
                    .build();
        } catch (DuplicateEquipmentException e) {
            Response responseBody = new Response(e.getMessage(), input);
            return ApiGatewayResponse.builder()
                    .setStatusCode(403)
                    .setObjectBody(responseBody)
                    .setHeaders(HandlerUtil.createCORSHeaders())
                    .build();
        } catch (Exception e) {
            logger.error(e);
            Response responseBody = new Response("API failed to create Equipment", input);
            return ApiGatewayResponse.builder()
                    .setStatusCode(500)
                    .setObjectBody(responseBody)
                    .setHeaders(HandlerUtil.createCORSHeaders())
                    .build();
        }

    }

}
