import React, { Component } from 'react';
import { connect } from 'react-redux';
import * as actions from '../actions';

const LIMIT = 2;

class EquipmentTable extends Component {

    componentWillMount() {
        this.props.getEquipmentList(LIMIT);
    }

    getMoreEquipment() {
        const last_equipment = this.props.equipment.slice(-1)[0];
        this.props.getEquipmentList(LIMIT, last_equipment.equipmentNumber);
    }

    renderEquipment(equipment) {
        return (
            <tr>
                <th scope="row">{equipment.equipmentNumber}</th>
                <td>{equipment.address}</td>
                <td>{equipment.contractStartDate}</td>
                <td>{equipment.contractEndDate}</td>
                <td>{equipment.status}</td>
            </tr>
        );
    }

    render() {
        return (
            <div>
                <table className="table">
                    <thead>
                    <tr>
                        <th scope="col">#</th>
                        <th scope="col">Address</th>
                        <th scope="col">Contract Start Date</th>
                        <th scope="col">Contract End Date</th>
                        <th scope="col">Status</th>
                    </tr>
                    </thead>
                    <tbody>
                        {this.props.equipment.map(this.renderEquipment)}
                    </tbody>
                </table>
                <div>
                    <button type="button" className="btn btn-outline-secondary" onClick={this.getMoreEquipment.bind(this)}>Load More</button>
                </div>
            </div>
        );
    }

}

function mapStateToProps(state) {
    return { equipment: state.equipment };
}

export default connect(mapStateToProps, actions)(EquipmentTable);