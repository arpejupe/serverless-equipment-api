package com.serverless.util;

import com.google.inject.AbstractModule;
import com.serverless.dao.Dao;
import com.serverless.dao.EquipmentDao;

public class EquipmentInjector extends AbstractModule {

    @Override
    protected void configure() {
        bind(Dao.class).to(EquipmentDao.class);
    }

}
