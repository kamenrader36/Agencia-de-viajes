import { Link } from 'react-router-dom';
import { useKeycloak } from '@react-keycloak/web';

const Navbar = () => {
    const { keycloak, initialized } = useKeycloak();

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
                <Link to="/reports" style={{ color: 'white', textDecoration: 'none' }}>Reports</Link>
                
                {}
                {initialized && keycloak.authenticated && (
                    <>
                        <span style={{ marginLeft: '15px', fontWeight: 'bold' }}>
                            Welcome, {keycloak.tokenParsed?.preferred_username}
                        </span>
                        <button 
                            onClick={() => keycloak.logout({ redirectUri: 'http://localhost:8008' })}
                            style={{ padding: '8px 15px', cursor: 'pointer', backgroundColor: '#e74c3c', color: 'white', border: 'none', borderRadius: '4px' }}
                        >
                            Logout
                        </button>
                    </>
                )}
            </div>
        </nav>
    );
};

export default Navbar;