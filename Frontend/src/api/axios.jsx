import axios from "axios";
import keycloak from "../services/keycloak";

const api = axios.create({baseURL: import.meta.env.VITE_API_BASE_URL});

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