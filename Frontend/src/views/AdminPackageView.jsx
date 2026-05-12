import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useKeycloak } from "@react-keycloak/web";
import api from '../api/axios';

const CreatePackage = () => {

    const keycloak = useKeycloak();
    const navigate = useNavigate();
    const[formData, setFormData] = useState ({
        name:'',
        destiantion:'',
        description:'',
        startDate:'',
        endDate:'',
        price:'',
        capacity:'',
        tripType:'',
        season:''
    });
    const today = new Date().toISOString().split("T")[0];

    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value});
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            await api.post("/api/tour_packages", formData, {
                headers: {
                    Authorization: `Bearer ${keycloak.token}`
                }
            });
            alert("Paquete creado");
            navigate('/');
        } catch (error) {
            console.error("Error al crear el paquete:", error);
            alert("Ha ocurrido un error al momento de la creación del Paquete Turistico");
        }
    
    };

    const inputStyle = { width: '100%', padding: '10px', marginBottom: '15px', borderRadius: '4px', border: '1px solid #ccc' };

    return (
        <div style={{ maxWidth: '600px', margin: '40px auto', padding: '30px', border: '1px solid #ddd', borderRadius: '8px', backgroundColor: '#f9f9f9' }}>
            <h2 style={{ textAlign: 'center', marginBottom: '20px' }}>Crear Nuevo Paquete Turístico</h2>
            
            <form onSubmit={handleSubmit}>
                <div style={{ display: 'flex', gap: '15px' }}>
                    <div style={{ flex: 1 }}>
                        <label>Nombre del Paquete:</label>
                        <input type="text" name="name" onChange={handleChange} required style={inputStyle} placeholder="Ej: Aventura en la Patagonia" />
                    </div>
                    <div style={{ flex: 1 }}>
                        <label>Destino:</label>
                        <input type="text" name="destination" onChange={handleChange} required style={inputStyle} placeholder="Ej: Torres del Paine" />
                    </div>
                </div>

                <label>Descripción:</label>
                <textarea name="description" onChange={handleChange} required style={{ ...inputStyle, height: '80px' }} placeholder="Detalles del viaje..." />

                <div style={{ display: 'flex', gap: '15px' }}>
                    <div style={{ flex: 1 }}>
                        <label>Fecha de Salida:</label>
                        <input type="date" name="startDate" min={today} onChange={handleChange} required style={inputStyle} />
                    </div>
                    <div style={{ flex: 1 }}>
                        <label>Fecha de Regreso:</label>
                        <input type="date" name="endDate" onChange={handleChange} required style={inputStyle} />
                    </div>
                </div>

                <div style={{ display: 'flex', gap: '15px' }}>
                    <div style={{ flex: 1 }}>
                        <label>Precio (CLP):</label>
                        <input type="number" name="price" onChange={handleChange} required style={inputStyle} min="1" />
                    </div>
                    <div style={{ flex: 1 }}>
                        <label>Capacidad (Cupos):</label>
                        <input type="number" name="capacity" onChange={handleChange} required style={inputStyle} min="1" />
                    </div>
                </div>

                <div style={{ display: 'flex', gap: '15px' }}>
                    <div style={{ flex: 1 }}>
                        <label>Tipo de Viaje:</label>
                        <input type="text" name="tripType" onChange={handleChange} required style={inputStyle} placeholder="Ej: Aventura, Relax..." />
                    </div>
                    <div style={{ flex: 1 }}>
                        <label>Temporada:</label>
                        <select name="season" onChange={handleChange} required style={inputStyle}>
                            <option value="">Seleccione...</option>
                            <option value="Verano">Verano</option>
                            <option value="Otoño">Otoño</option>
                            <option value="Invierno">Invierno</option>
                            <option value="Primavera">Primavera</option>
                        </select>
                    </div>
                </div>

                <button type="submit" style={{ width: '100%', padding: '12px', backgroundColor: '#e67e22', color: 'white', border: 'none', borderRadius: '4px', cursor: 'pointer', fontSize: '16px', fontWeight: 'bold' }}>
                    Crear Paquete 
                </button>
            </form>
        </div>
    );
};

export default CreatePackage;
