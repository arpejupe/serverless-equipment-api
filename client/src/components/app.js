import React, { Component } from 'react';
import { connect } from 'react-redux'
import EquipmentTable from './equipment_table';
import '../style/App.css';
import { getEquipmentList } from "../actions";
import SwaggerUI from "./swagger";
import { BrowserRouter as Router, Route, Link } from "react-router-dom";

export default class App extends Component {

    render() {
        return (
            <div className="App">
                <Router>
                    <div>
                        <header className="App-header">
                            <a
                                className="App-link"
                                href="/"
                                target="_blank"
                                rel="noopener noreferrer"
                            >
                                Serverless Equipment API
                            </a>
                        </header>
                        <Route exact path="/" component={EquipmentTable} />
                        <Route path="/app" component={EquipmentTable} />
                        <Route path="/api" component={SwaggerUI} />
                    </div>
                </Router>
            </div>
        );
    }
}
