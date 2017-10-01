import axios from 'axios';
import jwtDecode from 'jwt-decode';
import * as actions from '../actions/types';

export const initialState = {
  isAuthenticated: false,
  user: {}
};

const reducer = (state = initialState, { type, token }) => {
  switch (type) {
  case actions.TOKEN_FETCHED:
    axios.defaults.headers.common['Authorization'] = token;
    localStorage.setItem('token', token);
    return {
      isAuthenticated: true,
      user: jwtDecode(token),
      token
    };
  case actions.LOGOUT:
    delete axios.defaults.headers.common['Authorization'];
    localStorage.removeItem('token');
    return initialState;
  default:
    return state;
  }
};

export default reducer;
