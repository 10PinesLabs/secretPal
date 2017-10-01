import React from 'react';
import CircularProgress from 'material-ui/CircularProgress';

export const Loading = () => (
  <div style={{textAlign: 'center'}}>
    <CircularProgress/> <p style={{fontFamily: 'arial'}}>Cargando...</p>
  </div>);

export default Loading;
