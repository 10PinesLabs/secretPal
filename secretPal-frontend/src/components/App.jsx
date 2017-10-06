import React from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import '../stylesheets/main.css';
import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';
import { Link } from 'react-router-dom';
import AppBar from 'material-ui/AppBar';
import Drawer from 'material-ui/Drawer';
import getMuiTheme from 'material-ui/styles/getMuiTheme';
import MenuItem from 'material-ui/MenuItem';
import * as c from 'material-ui/styles/colors';
import {fade} from 'material-ui/utils/colorManipulator';
import spacing from 'material-ui/styles/spacing';

// app component
export class App extends React.Component {
  constructor (props) {
    super(props);
    this.state = {open: false};
  }
  render () {
    const appBar =
      <AppBar
        title={<Link to="/">Secret Pal</Link>}
        iconStyleLeft={this.props.isLoggedIn ? {} : {display: 'none'}}
        onLeftIconButtonTouchTap={() =>
          this.setState((prevState) => ({open: !prevState.open}))}
      />;

    return (
      <MuiThemeProvider>
        <div>
          {appBar}
          <div style={{ maxWidth: '1024px', margin: '0 auto'}}>
            {this.props.children}
          </div>
          <Drawer
            open={this.state.open}
            docked={false}
            onRequestChange={(open) => this.setState({open})}
            >
            {appBar}
            <Link to='/wishlist'>
              <MenuItem onClick={
                () => this.setState({open: !this.state.open})}>
                Agregar empleado
              </MenuItem>
            </Link>
          </Drawer>
        </div>
      </MuiThemeProvider>
    );
  }
}

App.propTypes = {
  children: PropTypes.any,
  isLoggedIn: PropTypes.bool.isRequired
};

const stateToProps = (state) => ({ isLoggedIn: state.auth.isAuthenticated });

export default connect(stateToProps)(App);
