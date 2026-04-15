import axios from "axios";
import keycloak from "../security/keycloak";

const api = axios.create({baseURL: 'http://localhost:8080/api'});

api.interceptors.request.use(
    (config) => {
        if(keycloak.token){
            config.headers["Authorization"] = `Bearer ${keycloak.token}`;
        }
        
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

export default api;