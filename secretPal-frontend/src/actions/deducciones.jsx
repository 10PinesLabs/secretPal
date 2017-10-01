import axios from 'axios';

export const fetchDeduccionesFor = (cuil) => (año, mes) =>
  axios.get(`/empleados/${cuil}/deducciones/${año}/${mes}`)
    .then((response) => response.data)
    .catch((error) => Promise.reject(error.response.data.error));
