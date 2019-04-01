import { combineReducers } from 'redux';
import equipmentReducer from './equipment';

const rootReducer = combineReducers({
    equipment: equipmentReducer
})

export default rootReducer