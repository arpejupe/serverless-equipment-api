import axios from 'axios';
import {
    GET_EQUIPMENTLIST
} from './types';

const EQUIPMENT_API_URL = `https://rxd5rgoryh.execute-api.eu-west-1.amazonaws.com/test/equipment`;

export function getEquipmentList(limit, offset) {
    var endpoint;
    if(limit) {
        endpoint = `${EQUIPMENT_API_URL}?limit=${limit}`
    }
    if(offset) {
        endpoint += `&offset=${offset}`
    }
    const request = axios.get(endpoint);
    return {
        type: GET_EQUIPMENTLIST,
        payload: request
    };
}