import axios from 'axios';
import auth from './auth';
import router from '../router';

const axiosInstance = axios.create({
    baseURL: 'http://localhost:8080/api'
});

// Request interceptor to add the token to headers
axiosInstance.interceptors.request.use(
    config => {
        const tokens = auth.getTokens();
        if (tokens) {
            config.headers['Authorization'] = `Bearer ${tokens.accessToken}`;
        }
        return config;
    },
    error => {
        return Promise.reject(error);
    }
);

// Response interceptor to handle 401 errors
axiosInstance.interceptors.response.use(
    response => response,
    error => {
        if (error.response && error.response.status === 401) {
            // Unauthorized, possibly due to expired token
            auth.logout();
            router.push('/login');
        }
        return Promise.reject(error);
    }
);

export default axiosInstance;