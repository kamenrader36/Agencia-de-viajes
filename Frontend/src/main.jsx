import React from 'react'
import ReactDOM from 'react-dom/client'
import App from './App.jsx'
import './index.css'
import keycloak from './security/keycloak'

keycloak.init({ 
    onLoad: 'login-required',
    checkLoginIframe: false 
}).then((authenticated) => {

    if (authenticated) {

        ReactDOM.createRoot(document.getElementById('root')).render(
            <React.StrictMode>
                <App />
            </React.StrictMode>
        )
    }
}).catch((error) => {
    console.error("4. Error detectado:", error);
});
