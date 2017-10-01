import axios from 'axios';

export const fetchImpuesto = (cuil) => (año) => (mes) =>
  axios.get(`empleados/${cuil}/impuesto/${año}/${mes}`)
    .then((response) => response.data)
    .catch((error) => Promise.reject(error.response.data.error));
