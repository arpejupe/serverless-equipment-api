import {
    GET_EQUIPMENTLIST
} from '../actions/types';

export default function(state = [], action) {
    switch (action.type) {
        case GET_EQUIPMENTLIST:
            console.log("TEST")
            return [...state, ...action.payload];
    }
    return state;
}