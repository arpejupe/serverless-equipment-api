package com.serverless.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.amazonaws.services.dynamodbv2.model.*;
import com.serverless.model.Equipment;

import java.io.IOException;
import java.util.*;

import com.serverless.util.DatabaseConnector;
import com.serverless.util.exception.DuplicateEquipmentException;
import com.serverless.util.exception.EquipmentNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;

/**
 * Database Access Object for Equipment DynamoDB
 * TODO: Better Exception handling for produdction
 */
public class EquipmentDao implements Dao {

    static final Logger logger = LogManager.getLogger(Equipment.class);

    private final String TABLE_NAME = Optional.ofNullable(System.getenv("TABLE")).orElse("Equipment");
    
    private final DynamoDBMapper mapper;
    private DatabaseConnector databaseConnector;

    @Inject
    public EquipmentDao(DatabaseConnector databaseConnector) {
        this.databaseConnector = databaseConnector;
        this.mapper = databaseConnector.createDatabaseMapper(TABLE_NAME);
    }

    /**
     * Returns an order or throws if the order does not exist.
     * @param equipmentNumber id of equipment to load
     * @throws EquipmentNotFoundException if equipment is not found
     *
     */
    @Override
    public Equipment getEquipment(String equipmentNumber) throws IOException, EquipmentNotFoundException {
        logger.info("Executing load of Equipment Number: " + equipmentNumber);
        try {
            return Optional.ofNullable(
                    mapper.load(Equipment.class, equipmentNumber))
                    .orElseThrow(() -> new EquipmentNotFoundException("Equipment of number "
                            + equipmentNumber + " not found"));
        } catch (ResourceNotFoundException e) {
            logger.error(e);
            throw new IOException(e);
        }
    }

    /**
     * Gets a page of orders, at most pageSize long.
     * @param limit the exclusive start id for the next page.
     * @param exclusiveStartKey to start scan from specific equipment
     * @return list of equipment
     */
    @Override
    public List<Equipment> getEquipmentList(Integer limit, String exclusiveStartKey) {
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        scanExpression.setLimit(limit);
        if(exclusiveStartKey != null && !exclusiveStartKey.isEmpty()) {
            Map<String, AttributeValue> startKey = new HashMap<>();
            startKey.put("EquipmentNumber", new AttributeValue().withS(exclusiveStartKey));
            scanExpression.setExclusiveStartKey(startKey);
        }
        List<Equipment> paginatedList = new ArrayList<>();
        Iterator iterator = mapper.scan(Equipment.class, scanExpression).iterator();
        while (iterator.hasNext() && paginatedList.size() <= limit-1) {
            paginatedList.add((Equipment) iterator.next());
        }
        return paginatedList;
    }

    /**
     * Method override for passing only limit as argument
     * @param limit
     * @return
     */
    @Override
    public List<Equipment> getEquipmentList(Integer limit) {
        logger.info("Scanning with only limit parameter");
        return getEquipmentList(limit, null);
    }

    /**
     * Returns an order or throws if the order does not exist.
     * @param equipment object to be saved
     * @throws DuplicateEquipmentException if equipment with same number already exists
     *
     */
    @Override
    public void saveEquipment(Equipment equipment) throws DuplicateEquipmentException {
        String equipmentNumber = equipment.getEquipmentNumber();
        DynamoDBSaveExpression saveExpression = new DynamoDBSaveExpression();
        Map<String, ExpectedAttributeValue> expectedAttributeValueMap = new HashMap<String, ExpectedAttributeValue>();
        expectedAttributeValueMap.put("EquipmentNumber", new ExpectedAttributeValue(false));
        saveExpression.setExpected(expectedAttributeValueMap);
        try {
            logger.info("Saving Equipment: " + equipment.toString());
            mapper.save(equipment, saveExpression);
        } catch (ConditionalCheckFailedException e) {
            logger.info("Duplicate");
            throw new DuplicateEquipmentException("Equipment of number " + equipmentNumber + " already exists");
        }
    }

}
