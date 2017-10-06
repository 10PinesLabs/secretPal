import React from 'react';
import { Route, Switch } from 'react-router';
import { ConnectedRouter } from 'react-router-redux';
import { history } from './store';
import App from './components/App';
import LoginPage from './components/LoginPage';
import NotFoundPage from './components/NotFoundPage';
import WishListPage from './components/WishListPage';
import EnsureAuthenticated from './components/EnsureAuthenticated';
import SecretPalPage from './components/SecretPalPage';

// build the router
export const AppRouter = () =>
  <ConnectedRouter history={history}>
    <div>
      <Route path='/' component={() =>
        <App>
          <Switch>
            <Route exact path='/login' component={LoginPage}/>
            <EnsureAuthenticated>
              <Switch>
                <Route exact path='/' component={SecretPalPage}/>
                <Route exact path='/wishlist' component={WishListPage}/>
                <Route component={NotFoundPage}/>
              </Switch>
            </EnsureAuthenticated>
            <Route component={NotFoundPage}/>
          </Switch>
        </App>
      }/>
    </div>
  </ConnectedRouter>
  ;

