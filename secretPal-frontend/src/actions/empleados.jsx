import { fetchRemuneraciones } from './remuneraciones';
import {apply} from 'ramda';
import axios from 'axios';

export const fetchEmpleados = () =>
  axios.get('/empleados')
    .then((response) => response.data)
    .catch((error) => Promise.reject(error.response.data.error));

export const fetchShallowEmpleado = (cuil) =>
  axios.get(`/empleados/${cuil}`)
    .then((response) => response.data)
    .catch((error) => Promise.reject(error.response.data.error));

export const fetchEmpleado = (cuil) =>
  Promise.all([fetchShallowEmpleado(cuil), fetchRemuneraciones(cuil)])
    .then(
      apply((empleado, remuneraciones) => ({...empleado, remuneraciones})));
