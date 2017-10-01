import axios from 'axios';

export const fetchRemuneracionesFor = (cuil) => (año, mes) =>
  axios.get(`/empleados/${cuil}/remuneraciones/${año}/${mes}`)
    .then((response) => response.data)
    .catch((error) => Promise.reject(error.response.data.error));

export const fetchRemuneraciones = (cuil) =>
  axios.get(`/empleados/${cuil}/remuneraciones`)
    .then((response) => response.data)
    .catch((error) => Promise.reject(error.response.data.error));

export const createRemuneracionFor = (cuil, año, remuneracion) =>
  axios.post(`/empleados/${cuil}/remuneraciones/${año}`, remuneracion)
    .then((response) => response.data)
    .catch((error) => Promise.reject(error.response.data.error));
