import React from 'react';
import {connect} from 'react-redux';
import PropTypes from 'prop-types';

import Divider from 'material-ui/Divider';
import Paper from 'material-ui/Paper';
import TextField from 'material-ui/TextField';
import Dialog from 'material-ui/Dialog';
import RaisedButton from 'material-ui/RaisedButton';
import {
  Table,
  TableBody,
  TableHeader,
  TableRow,
  TableRowColumn,
  TableHeaderColumn
} from 'material-ui/Table';


import {Loading} from '../Loading';
import axios from 'axios';
import R from 'ramda';
import {swal} from 'react-redux-sweetalert';

class SecretPalPage extends React.Component {
  constructor(props) {
    super(props);
    this.state = {};
  }

  componentDidMount() {
    this.refreshWishes();
  }

  refreshWishes() {
    axios.get('/wishlist/')
      .then((response) => this.setState({wishList: response.data}))
      .catch(error => console.error(error));
  }

  render() {
    if (this.state.wishList) {
      return (
        <div>
          <RaisedButton/>
          <Table
            multiSelectable={false}
            selectable={false}
          >
            <TableHeader
              adjustForCheckbox={false}
              displaySelectAll={false}
              enableSelectAll={false}
            >
              <TableHeaderColumn>Creador</TableHeaderColumn>
              <TableHeaderColumn>Para</TableHeaderColumn>
              <TableHeaderColumn>Que</TableHeaderColumn>
            </TableHeader>
            <TableBody
              stripedRows={true}
              displayRowCheckbox={false}
            >
              {R.map((wish) =>
                <TableRow>
                  <TableRowColumn>{wish.createdBy.fullName}</TableRowColumn>
                  <TableRowColumn>{wish.worker.fullName}</TableRowColumn>
                  <TableRowColumn>{wish.gift}</TableRowColumn>
                </TableRow>, this.state.wishList)}
            </TableBody>
          </Table>
          <Dialog>
            cosa
          </Dialog>
        </div>
      );
    } else if (this.state.error) {
      return <p>Algo salió mal {this.state.error}</p>;
    } else {
      return <Loading/>;
    }
  }
}

SecretPalPage.propTypes = {
  swal: PropTypes.func.isRequired
};

const mapDispatchToProps = (dispatch) => ({swal: (options) => dispatch(swal(options))});


export default connect(null, mapDispatchToProps)(SecretPalPage);
