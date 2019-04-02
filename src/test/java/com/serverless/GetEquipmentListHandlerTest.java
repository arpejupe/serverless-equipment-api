package com.serverless;

import com.serverless.api.ApiGatewayResponse;
import com.serverless.dao.EquipmentDao;
import com.serverless.handler.GetEquipmentListHandler;
import com.serverless.model.Equipment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * TODO: Better unit test coverage
 */
@RunWith(MockitoJUnitRunner.class)
public class GetEquipmentListHandlerTest {

    static final Logger logger = LogManager.getLogger(GetEquipmentListHandlerTest.class);

    @InjectMocks
    private GetEquipmentListHandler handlerMock;
    @Mock
    private EquipmentDao daoMock;
    private Equipment equipmentMock;
    private List<Equipment> equipmentListMock;

    @Before
    public void setUp() {
        Integer limit = 1;

        this.equipmentListMock = generateEquipmentListMock();

        //when(this.daoMock.getEquipmentList(limit)).thenReturn(this.equipmentListMock);
    }

    private List<Equipment> generateEquipmentListMock() {
        equipmentMock = new Equipment();
        equipmentMock.setEquipmentNumber("D823");
        equipmentMock.setAddress("Test");
        equipmentMock.setContractStartDate("2010-05-30 22:15:51");
        equipmentMock.setContractEndDate("2010-05-30 22:15:52");
        equipmentMock.setStatus("STOPPED");

        List<Equipment> equipmentListMock = new ArrayList<>();
        equipmentListMock.add(equipmentMock);
        return equipmentListMock;
    }

    @Test
    @Ignore
    public void shouldReturnListOfEquipmentWithOneItem() {
        Map<String, String> queryStringParameter = new HashMap<String, String>();
        queryStringParameter.put("limit", "1");
        Map<String, Object> input = new HashMap<>();
        input.put("queryStringParameters", queryStringParameter);

        ApiGatewayResponse expected = ApiGatewayResponse.builder().setObjectBody(this.equipmentListMock).build();
        ApiGatewayResponse actual = handlerMock.handleRequest(input, null);

        assertEquals(actual.getBody(), expected.getBody());
    }

    @Test
    public void shouldThrowInternalServerErrorOnInvalidParams() {
        Map<String, String> queryStringParameter = new HashMap<String, String>();
        Map<String, Object> input = new HashMap<>();
        input.put("queryStringParameters", queryStringParameter);

        ApiGatewayResponse expected = ApiGatewayResponse.builder().setStatusCode(500).build();
        ApiGatewayResponse actual = handlerMock.handleRequest(input, null);

        assertEquals(actual.getStatusCode(), expected.getStatusCode());
    }


}