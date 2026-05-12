import { useState } from "react";
import { useKeycloak } from "@react-keycloak/web";
import { useNavigate } from "react-router-dom";
import api from '../api/axios';

const Register = () => {
    const { keycloak } = useKeycloak();
    const navigate = useNavigate();
    
    const [formData, setFormData] = useState({
        username: keycloak.tokenParsed?.preferred_username || '',
        fullName: keycloak.tokenParsed?.name || '',
        email: keycloak.tokenParsed?.email || '',
        phoneNumber: '',
        documentNumber: '',
        nationality: ''
    });

    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try{
            await api.post('/api/user/save', formData),
            alert("Registro completado"),
            navigate('/')
        } catch (error){
            console.error("Error al guardar el perfil", error);
            alert("Hubo un error con el servidor");
        }
    };

   return (
        <div style={{ maxWidth: '400px', margin: '50px auto', padding: '20px', border: '1px solid #ccc', borderRadius: '8px' }}>
            <h2>Datos extra de Registro</h2>
            <p>Ingresa estos ultimos datos para completar tu registro en el página.</p>
            
            <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: '15px' }}>
                <input 
                    type="text" name="documentNumber" placeholder="Número de Documento / RUT" 
                    value={formData.documentNumber} onChange={handleChange} required 
                    style={{ padding: '8px' }}
                />
                <input 
                    type="text" name="phoneNumber" placeholder="Teléfono" 
                    value={formData.phoneNumber} onChange={handleChange} required 
                    style={{ padding: '8px' }}
                />
                <input 
                    type="text" name="nationality" placeholder="Nacionalidad" 
                    value={formData.nationality} onChange={handleChange} required 
                    style={{ padding: '8px' }}
                />
                <button type="submit" style={{ padding: '10px', backgroundColor: '#2ecc71', color: 'white', border: 'none', cursor: 'pointer' }}>
                    Finalizar Registro
                </button>
            </form>
        </div>
    );
};

export default Register;

