import React, { useState, useEffect } from 'react';
import { useKeycloak } from '@react-keycloak/web';
import api from '../api/axios';

const ProfileView = () => {
    const { keycloak } = useKeycloak();
    const [loading, setLoading] = useState(true);
    const [formData, setFormData] = useState({
        username: '',
        fullName: '',
        email: '',
        phoneNumber: '',
        documentNumber: '',
        nationality: ''
    });

    useEffect(() => {
        
        const username = keycloak.tokenParsed?.preferred_username;
        if (username) {
            api.get(`/api/user/${username}`)
                .then(response => {
                    setFormData(response.data);
                    setLoading(false);
                })
                .catch(error => {
                    console.error("Error al cargar perfil:", error);
                    alert("No se pudo cargar la información del perfil.");
                    setLoading(false);
                });
        }
    }, [keycloak]);

    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            await api.put('/api/user/update', formData);
            alert("¡Perfil actualizado con éxito!");
        } catch (error) {
            console.error("Error al actualizar:", error);
            alert("Hubo un error al guardar los cambios.");
        }
    };

    if (loading) return <div style={{ textAlign: 'center', marginTop: '50px' }}>Cargando perfil...</div>;

    const inputStyle = { padding: '10px', borderRadius: '4px', border: '1px solid #ccc', width: '100%' };
    const labelStyle = { fontWeight: 'bold', marginBottom: '5px', display: 'block', color: '#34495e' };

    return (
        <div style={{ maxWidth: '600px', margin: '40px auto', padding: '30px', backgroundColor: '#fff', borderRadius: '8px', boxShadow: '0 4px 12px rgba(0,0,0,0.1)' }}>
            <h2 style={{ textAlign: 'center', color: '#2c3e50', marginBottom: '20px' }}>Mi Perfil de Usuario</h2>
            <p style={{ textAlign: 'center', color: '#7f8c8d', marginBottom: '30px' }}>Mantén tu información actualizada para tus próximas reservas.</p>
            
            <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: '20px' }}>
                <div>
                    <label style={labelStyle}>Nombre de Usuario:</label>
                    <input 
                        type="text" name="username" value={formData.username} 
                        disabled style={{ ...inputStyle, backgroundColor: '#f9f9f9', cursor: 'not-allowed' }} 
                    />
                </div>

                <div style={{ display: 'flex', gap: '15px' }}>
                    <div style={{ flex: 1 }}>
                        <label style={labelStyle}>Nombre Completo:</label>
                        <input type="text" name="fullName" value={formData.fullName} onChange={handleChange} required style={inputStyle} />
                    </div>
                    <div style={{ flex: 1 }}>
                        <label style={labelStyle}>Email:</label>
                        <input type="email" name="email" value={formData.email} onChange={handleChange} required style={inputStyle} />
                    </div>
                </div>

                <div style={{ display: 'flex', gap: '15px' }}>
                    <div style={{ flex: 1 }}>
                        <label style={labelStyle}>Teléfono:</label>
                        <input type="text" name="phoneNumber" value={formData.phoneNumber} onChange={handleChange} required style={inputStyle} />
                    </div>
                    <div style={{ flex: 1 }}>
                        <label style={labelStyle}>Nacionalidad:</label>
                        <input type="text" name="nationality" value={formData.nationality} onChange={handleChange} required style={inputStyle} />
                    </div>
                </div>

                <div>
                    <label style={labelStyle}>Número de Documento / RUT:</label>
                    <input type="text" name="documentNumber" value={formData.documentNumber} onChange={handleChange} required style={inputStyle} />
                </div>

                <button 
                    type="submit" 
                    style={{ 
                        marginTop: '10px', padding: '12px', backgroundColor: '#2ecc71', color: 'white', 
                        border: 'none', borderRadius: '4px', cursor: 'pointer', fontSize: '16px', fontWeight: 'bold' 
                    }}
                >
                    Actualizar Datos
                </button>
            </form>
        </div>
    );
};

export default ProfileView;