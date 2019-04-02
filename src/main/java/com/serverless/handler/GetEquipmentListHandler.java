package com.serverless.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.serverless.dao.Dao;
import com.serverless.util.EquipmentInjector;
import com.serverless.util.HandlerUtil;
import com.serverless.api.ApiGatewayResponse;
import com.serverless.api.Response;
import com.serverless.model.Equipment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.List;

public class GetEquipmentListHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

    static final Logger logger = LogManager.getLogger(GetEquipmentListHandler.class);

    Injector injector = Guice.createInjector(new EquipmentInjector());
    Dao dao = injector.getInstance(Dao.class);

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
        logger.info("GetEquipmentListHandler received request: " + input);
        List<Equipment> equipmentList;
        try {
            HandlerUtil.validateInputParameters(input);
            Map<String, String> queryStringParameters = (Map<String, String>) input.get("queryStringParameters");
            Integer limit = Integer.parseInt(queryStringParameters.get("limit"));
            String lastEvaluatedKey = queryStringParameters.get("offset");
            equipmentList = dao.getEquipmentList(limit, lastEvaluatedKey);
            return ApiGatewayResponse.builder()
                    .setStatusCode(200)
                    .setObjectBody(equipmentList)
                    .setHeaders(HandlerUtil.createCORSHeaders())
                    .build();
        }
        catch (IllegalArgumentException e) {
            logger.error(e);
            Response responseBody = new Response(e.getMessage(), input);
            return ApiGatewayResponse.builder()
                    .setStatusCode(500)
                    .setObjectBody(responseBody)
                    .setHeaders(HandlerUtil.createCORSHeaders())
                    .build();
        }
        catch (Exception e) {
            logger.error(e);
            Response responseBody = new Response("API failed fetching the equipment list", input);
            return ApiGatewayResponse.builder()
                    .setStatusCode(500)
                    .setObjectBody(responseBody)
                    .setHeaders(HandlerUtil.createCORSHeaders())
                    .build();
        }
    }

}
