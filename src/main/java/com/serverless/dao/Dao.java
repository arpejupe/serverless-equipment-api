package com.serverless.dao;

import com.serverless.model.Equipment;
import com.serverless.util.exception.DuplicateEquipmentException;
import com.serverless.util.exception.EquipmentNotFoundException;

import java.io.IOException;
import java.util.List;

public interface Dao<T> {

    Equipment getEquipment(String EquipmentNumber) throws IOException, EquipmentNotFoundException;
    List<Equipment> getEquipmentList(Integer limit);
    List<Equipment> getEquipmentList(Integer limit, String exclusiveStartKey);
    void saveEquipment(Equipment equipment) throws DuplicateEquipmentException;

}
