import { combineReducers } from 'redux';
import { routerReducer } from 'react-router-redux';
import { reducer as sweetalertReducer } from 'react-redux-sweetalert';
import authReducer from './auth';

// main reducers
const reducer = combineReducers({
  sweetalert: sweetalertReducer,
  routing: routerReducer,
  auth: authReducer
});

export default reducer;
