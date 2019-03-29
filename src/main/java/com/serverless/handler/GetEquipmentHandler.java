package com.serverless.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.serverless.ApiGatewayResponse;
import com.serverless.Response;
import com.serverless.model.Equipment;
import com.serverless.dao.EquipmentDao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.Map;

public class GetEquipmentHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

    static final Logger logger = LogManager.getLogger(Equipment.class);

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
        logger.info("Handler received request: " + input);
        Equipment equipment;
        try {
            Map<String, String> pathParameters = (Map<String, String>) input.get("pathParameters");
            String equipmentNumber = pathParameters.get("id");
            EquipmentDao dynamoDB = new EquipmentDao();
            equipment = dynamoDB.getEquipment(equipmentNumber);
            logger.info("Equipment fetched with id " + equipment.getEquipmentNumber());
        } catch (Exception e) {
            logger.error(e, e);
            Response responseBody = new Response("Failure getting equipment", input);
            return ApiGatewayResponse.builder()
                    .setStatusCode(500)
                    .setObjectBody(responseBody)
                    .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & serverless"))
                    .build();
        }
        return ApiGatewayResponse.builder()
                .setStatusCode(200)
                .setObjectBody(equipment)
                .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & serverless"))
                .build();
    }

}