import React from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { push } from 'react-router-redux';

class AuthContainer extends React.Component {
  componentDidMount () {
    if (!this.props.isLoggedIn) {
      this.props.goToLogin();
    }
  }

  render () {
    if (this.props.isLoggedIn) {
      return this.props.children;
    }
    return <p>Redirecting you to login</p>;
  }
}

AuthContainer.propTypes = {
  isLoggedIn: PropTypes.bool,
  goToLogin: PropTypes.func.isRequired,
  children: PropTypes.element.isRequired
};

const stateToProps = (state) => ({ isLoggedIn: state.auth.isAuthenticated });

const dispatchToProps = (dispatch) => ({goToLogin: () => dispatch(push('/login'))});

export default connect(stateToProps, dispatchToProps)(AuthContainer);
