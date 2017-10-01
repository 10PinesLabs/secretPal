/* global process */

import { createStore, applyMiddleware, compose } from 'redux';
import { createHashHistory } from 'history';
import { routerMiddleware } from 'react-router-redux';
import thunk from 'redux-thunk';
import reducers from './reducers/index';

// add the middlewares
const middlewares = [];

const history = createHashHistory();

// add the router middleware
middlewares.push(routerMiddleware(history));
middlewares.push(thunk);

// apply the middleware
let middleware = applyMiddleware(...middlewares);

// add the redux dev tools
if (process.env.NODE_ENV !== 'production' && window.devToolsExtension) {
  middleware = compose(middleware, window.devToolsExtension());
}

// create the store
const store = createStore(reducers, middleware);

if (localStorage.getItem('token')) {
  store.dispatch({type: 'TOKEN_FETCHED', token: localStorage.getItem('token')});
}

// export
export { store, history };
