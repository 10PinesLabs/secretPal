import React from 'react';
import RaisedButton from 'material-ui/RaisedButton';

export const LoginPage = (props) =>
  <div className="loginContainer">
    <h1>Secret Pal</h1>
    <a href={process.env.BACKOFFICE_URL || 'https://backoffice-10pines-stg.herokuapp.com/auth/sign_in?redirect_url=http://localhost:5000/api/auth/callback&app_id=secret-pal'}>
      <RaisedButton primary={true} label="Login con backoffice"/>
    </a>
  </div>;

export default LoginPage;
