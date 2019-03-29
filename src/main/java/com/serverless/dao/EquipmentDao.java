package com.serverless.dao;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.serverless.model.Equipment;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.regions.Regions;

import java.io.IOException;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EquipmentDao {

    private final String endpoint = System.getenv("ENDPOINT");
    private final String tableName = Optional.ofNullable(System.getenv("TABLE")).orElse("Equipment");
    private final String region = Optional.ofNullable(System.getenv("REGION")).orElse(Regions.EU_WEST_1.toString());

    private final AmazonDynamoDB dynamoDB;
    private final DynamoDBMapperConfig config;
    private final DynamoDBMapper mapper;

    static final Logger logger = LogManager.getLogger(Equipment.class);

    private AwsClientBuilder.EndpointConfiguration getEndpointConfiguration(String endpoint, String region) {
        System.out.println("Created EndpointConfiguration for endpoint: " + endpoint + " in region " + region);
        return new AwsClientBuilder.EndpointConfiguration(endpoint, Regions.EU_WEST_1.getName());
    }

    public EquipmentDao() {
        this.dynamoDB = AmazonDynamoDBClientBuilder
                .standard()
                .withEndpointConfiguration(getEndpointConfiguration(this.endpoint, region))
                .build();
        this.config = new DynamoDBMapperConfig.Builder()
                .build();
        this.mapper = new DynamoDBMapper(this.dynamoDB, this.config);
        logger.info("Created DynamoDB Access");
    }

    public Equipment getEquipment(String equipmentNumber) throws IOException {
        logger.info("Executing load");
        return this.mapper.load(Equipment.class, equipmentNumber);
    }

}
