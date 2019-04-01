import React from 'react';
import ReactDOM from 'react-dom';
import { Provider } from 'react-redux';
import { createStore, applyMiddleware } from 'redux';
import './style/index.css';
import App from './components/app';
import 'bootstrap/dist/css/bootstrap.css'
import reducers from './reducers';
import Async from './middlewares/async';

const createStoreWithMiddleWare = applyMiddleware(Async)(createStore);

ReactDOM.render(
    <Provider store={createStoreWithMiddleWare(reducers)}>
        <App />
    </Provider>
    , document.getElementById('root'));

