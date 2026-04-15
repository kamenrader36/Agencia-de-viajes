import { Link } from "react-router-dom";
import keycloak from "../security/keycloak";

const navbar = () => {
  const username = keycloak.tokenParsed?.preferred_username || "User";

  return (
    <nav style={{ 
            display: 'flex', 
            justifyContent: 'space-between', 
            alignItems: 'center',
            padding: '1rem 2rem', 
            backgroundColor: '#2c3e50', 
            color: 'white' 
        }}>
            <div>
                <h2 style={{ margin: 0 }}>TravelAgency</h2>
            </div>
            
            <div style={{ display: 'flex', gap: '20px', alignItems: 'center' }}>
                <Link to="/" style={{ color: 'white', textDecoration: 'none' }}>Home</Link>
                <Link to="/reports" style={{ color: 'white', textDecoration: 'none' }}>Reports</Link>
                
                <span style={{ marginLeft: '15px', fontWeight: 'bold' }}>
                    Welcome, {username}
                </span>
                
                <button 
                    onClick={() => keycloak.logout()}
                    style={{ 
                        padding: '8px 15px', 
                        cursor: 'pointer', 
                        backgroundColor: '#e74c3c', 
                        color: 'white', 
                        border: 'none', 
                        borderRadius: '4px',
                        marginLeft: '10px'
                    }}
                >
                    Logout
                </button>
            </div>
        </nav>
    );
};

export default navbar;
