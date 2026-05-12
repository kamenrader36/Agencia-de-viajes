import { Link } from 'react-router-dom';
import { useKeycloak } from '@react-keycloak/web';

const Navbar = () => {
    const { keycloak, initialized } = useKeycloak();
    const isAuth = initialized && keycloak.authenticated;
    const isAdmin = isAuth && keycloak.hasRealmRole('ADMIN');
    const isUser = isAuth && !isAdmin;

    return (
        <nav style={{ 
            display: 'flex', justifyContent: 'space-between', alignItems: 'center',
            padding: '1rem 2rem', backgroundColor: '#2c3e50', color: 'white' 
        }}>
            <div>
                <h2 style={{ margin: 0 }}>TravelAgency</h2>
            </div>
            
            <div style={{ display: 'flex', gap: '20px', alignItems: 'center' }}>
                <Link to="/" style={{ color: 'white', textDecoration: 'none' }}>Home</Link>

                {isAuth && isUser && (
                    <><Link to="/perfil" style={{ color: 'white', marginRight: '15px', textDecoration: 'none' }}>
                        Mi Perfil
                    </Link><Link to="/mis-reservas" style={{ color: 'white', marginRight: '15px', textDecoration: 'none' }}>
                        Mis Reservas
                    </Link></>
                )}

                {isAdmin && (
                    <>
                        <Link to="/reports" style={{ color: 'white', textDecoration: 'none' }}>
                            Reports
                        </Link>
                        <Link to="/admin/crear-paquete" style={{ color: '#f39c12', textDecoration: 'none', fontWeight: 'bold' }}>
                            + Crear Paquete
                        </Link>
                    </>
                )}

                {isAuth && (
                    <div style={{ display: 'flex', alignItems: 'center', gap: '15px' }}>
                        <span style={{ fontWeight: 'bold' }}>
                            Welcome, {keycloak.tokenParsed?.preferred_username}
                        </span>
                        <button 
                            onClick={() => keycloak.logout({ redirectUri: 'http://localhost:8070' })}
                            style={{ 
                                padding: '8px 15px', 
                                cursor: 'pointer', 
                                backgroundColor: '#e74c3c', 
                                color: 'white', 
                                border: 'none', 
                                borderRadius: '4px' 
                            }}
                        >
                            Logout
                        </button>
                    </div>
                )}

                {!isAuth && initialized && (
                    <button 
                        onClick={() => keycloak.login()}
                        style={{ 
                            padding: '8px 15px', 
                            cursor: 'pointer', 
                            backgroundColor: '#2ecc71', 
                            color: 'white', 
                            border: 'none', 
                            borderRadius: '4px' 
                        }}
                    >
                        Login
                    </button>
                )}
            </div>
        </nav>
    );
};

export default Navbar;