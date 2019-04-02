package com.serverless.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.serverless.api.ApiGatewayResponse;
import com.serverless.api.Response;
import com.serverless.dao.Dao;
import com.serverless.model.Equipment;
import com.serverless.util.EquipmentInjector;
import com.serverless.util.HandlerUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public class GetEquipmentHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

    static final Logger logger = LogManager.getLogger(GetEquipmentHandler.class);

    Injector injector = Guice.createInjector(new EquipmentInjector());
    Dao dao = injector.getInstance(Dao.class);

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
        logger.info("GetEquipmentHandler received request: " + input);
        Equipment equipment;
        try {
            Map<String, String> pathParameters = (Map<String, String>) input.get("pathParameters");
            String equipmentNumber = pathParameters.get("id");
            equipment = dao.getEquipment(equipmentNumber);
            logger.info("Equipment fetched with id " + equipment.getEquipmentNumber());
            return ApiGatewayResponse.builder()
                    .setStatusCode(200)
                    .setObjectBody(equipment)
                    .setHeaders(HandlerUtil.createCORSHeaders())
                    .build();
        } catch (Exception e) {
            logger.error(e);
            Response responseBody = new Response("API failed fetching the equipment", input);
            return ApiGatewayResponse.builder()
                    .setStatusCode(500)
                    .setObjectBody(responseBody)
                    .setHeaders(HandlerUtil.createCORSHeaders())
                    .build();
        }
    }

}