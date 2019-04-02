package com.serverless.util;

import java.util.HashMap;
import java.util.Map;

public final class HandlerUtil {

    private HandlerUtil() {}

    //TODO: Proper input parameter validation for all handlers
    public static void validateInputParameters(Map<String, Object> input) {
        if(input.get("queryStringParameters") == null) {
            throw new IllegalArgumentException("Query string paramters missing. You need to provide at least limit parameter");
        }
    }

    public static Map<String, String> createCORSHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Access-Control-Allow-Origin", "*");
        headers.put("Access-Control-Allow-Headers", "Content-Type,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token");
        return headers;
    }
}
