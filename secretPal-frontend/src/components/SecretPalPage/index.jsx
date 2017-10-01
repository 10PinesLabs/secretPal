import React from 'react';
import {connect} from 'react-redux';
import PropTypes from 'prop-types';

import Divider from 'material-ui/Divider';
import Paper from 'material-ui/Paper';
import TextField from 'material-ui/TextField';

import axios from 'axios';
import {swal} from 'react-redux-sweetalert';

const BloqueAntiPantalleo = ({children}) => (
  <div className="anti-pantalleo"
       style={{
         maxWidth: '50%',
         margin: 'auto',
         textAlign: 'center',
         borderRadius: '0.3em'
       }}>
    {children}
  </div>
);

class SecretPalPage extends React.Component {
  constructor(props) {
    super(props);
    this.state = {};
  }

  componentDidMount() {
    axios.get(`/friendRelation/friend/${this.props.palId}`)
      .then((response) => this.setState({regalado: response.data}))
      .catch(error => console.error(error));
  }

  render() {
    if (this.state.regalado) {
      return (
        <Paper zDepth={2} style={{textAlign: 'center', margin: '2em'}}>
          Apoyá el mouse por encima de los bloques verdes para ver la información
          <h2>
          Este año tu pino cumpleañero es: <br/>
          <BloqueAntiPantalleo> {this.state.regalado.fullName} </BloqueAntiPantalleo>
          </h2>
          <Divider/>

          <h2>
          Tu pino asignado cumple el:<br/>
          <BloqueAntiPantalleo>
            {this.state.regalado.dateOfBirth}
          </BloqueAntiPantalleo>
          </h2>
          <Divider/>

          <h3>El regalo default este año es: {this.state.giftDefault}</h3>
          <Divider/>

          <h3>El monto estimado del regalo es: {this.state.amountDefault}</h3>
          <Divider/>
        </Paper>
      );
    } else if (this.state.error) {
      return <p>Algo salió mal {this.state.error}</p>;
    } else {
      return <p>Cargando empleado...</p>;
    }
  }
}

SecretPalPage.propTypes = {
  palId: PropTypes.string.isRequired,
  swal: PropTypes.func.isRequired
};

const mapDispatchToProps = (dispatch) => ({swal: (options) => dispatch(swal(options))});


export default connect(
  (state) => {
    console.log(state);
    return ({palId: state.auth.user.palId})
  },
  mapDispatchToProps)(SecretPalPage);
