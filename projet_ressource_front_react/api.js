import axios from 'axios';

const api = axios.create({
  baseURL: 'http://backend:8080', // backend = nom du service Docker
});

export default api;
