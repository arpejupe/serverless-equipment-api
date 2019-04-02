package com.serverless.util;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class DatabaseConnector {

    private final String ENDPOINT = System.getenv("ENDPOINT");
    private final String REGION = Optional.ofNullable(System.getenv("REGION")).orElse(Regions.EU_WEST_1.getName());

    private static AmazonDynamoDB dynamoDB;

    static final Logger logger = LogManager.getLogger(DatabaseConnector.class);

    private AwsClientBuilder.EndpointConfiguration getEndpointConfiguration(String endpoint, String region) {
        logger.info("Created EndpointConfiguration for endpoint: " + endpoint + " in region " + region);
        return new AwsClientBuilder.EndpointConfiguration(endpoint, region);
    }

    public DatabaseConnector() {
        if(ENDPOINT == null || ENDPOINT.isEmpty()) {
            dynamoDB = AmazonDynamoDBClientBuilder
                    .standard()
                    .withRegion(REGION)
                    .build();
        } else {
            dynamoDB = AmazonDynamoDBClientBuilder
                    .standard()
                    .withEndpointConfiguration(getEndpointConfiguration(ENDPOINT, REGION))
                    .build();
        }
        logger.info("DynamoDB Access Created");
    }

    public static DynamoDBMapper createDatabaseMapper(String tableName) {
        logger.info("Configuring DynamoDB for table: " + tableName);
        DynamoDBMapperConfig.TableNameOverride newTableName =
                new DynamoDBMapperConfig.TableNameOverride(tableName);
        DynamoDBMapperConfig mapperConfig = new DynamoDBMapperConfig.Builder()
                .withTableNameOverride(newTableName)
                .build();
        return new DynamoDBMapper(dynamoDB, mapperConfig);

    }

}
