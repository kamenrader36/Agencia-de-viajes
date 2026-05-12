import { useKeycloak } from '@react-keycloak/web';

const PrivateRoute = ({ children, roleRequired }) => {
    const { keycloak, initialized } = useKeycloak();

    if (!initialized) {
        return <div>Cargando seguridad...</div>;
    }

    if (roleRequired && !keycloak.hasRealmRole(roleRequired)) {
        return (
            <div style={{ padding: '50px', textAlign: 'center' }}>
                <h2>Acceso Denegado 🛑</h2>
                <p>Tu cuenta no tiene los permisos para ver esta página.</p>
            </div>
        );
    }

    
    return children;
};

export default PrivateRoute;