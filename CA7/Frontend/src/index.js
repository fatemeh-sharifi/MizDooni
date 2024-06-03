import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';
import "bootstrap/dist/css/bootstrap.min.css";
import "bootstrap/dist/js/bootstrap.bundle.min";
import axios from 'axios';
const root = ReactDOM.createRoot(document.getElementById('root'));


axios.interceptors.request.use(
    config => {
        const token = localStorage.getItem('jwtToken');
        console.log("tokennnn : ", token);
        if (token) {
            config.headers['Authorization'] = 'Bearer ' + token;
        }
        config.withCredentials = true;
        return config;
    },
    error => {
        return Promise.reject(error);
    }
);
root.render(
  <React.StrictMode>
    <App />
  </React.StrictMode>
);
