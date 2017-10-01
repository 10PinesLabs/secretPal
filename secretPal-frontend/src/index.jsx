/* global module */
import React from 'react';
import ReactDOM from 'react-dom';
import { Provider } from 'react-redux';
import { store } from './store';
import { AppRouter } from './router';
import { AppContainer } from 'react-hot-loader';
import ReduxSweetAlert from 'react-redux-sweetalert';
import 'sweetalert/dist/sweetalert.css';
import axios from 'axios';


axios.defaults.baseURL = '/api/';

// render the main component
const load = () => {
  ReactDOM.render(
    <AppContainer>
      <Provider store={store}>
        <div>
          <AppRouter/>
          <ReduxSweetAlert />
        </div>
      </Provider>
    </AppContainer>,
    document.getElementById('root')
  );
};

if (module.hot) {
  module.hot.accept('./router', load);
}

load();
