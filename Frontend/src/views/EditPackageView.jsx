import React, { useState, useEffect} from 'react';
import {useNavigate, useParams} from 'react-router-dom';
import api from '../api/axios';

const EditPackage = () => {
    const navigate = useNavigate();
    const { id } = useParams();

    const [formData, setFormData] = useState({
        name: '', destination: '', description: '', startDate: '', 
        endDate: '', price: '', capacity: '', tripType: '', season: '', tourStatus: ''
    });
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        api.get(`/api/tour_packages/${id}`)
            .then(response => {
                setFormData(response.data);
                setLoading(false);
            })
            .catch(error => {
                console.error("Error al cargar el paquete:", error);
                alert("No se pudo cargar la información del paquete.");
                navigate('/');
            });
    }, [id, navigate]);

    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            await api.put(`/api/tour_packages/${id}`, formData);
            alert("Paquete actualizado");
            navigate('/');
        } catch (error) {
            console.error("Error al actualizar:", error);
            alert(error.response?.data?.message || "Hubo un error al actualizar el paquete.");
        }
    };

    const inputStyle = { width: '100%', padding: '10px', marginBottom: '15px', borderRadius: '4px', border: '1px solid #ccc' };

    if (loading) return <h2 style={{ textAlign: 'center', marginTop: '50px' }}>Cargando datos...</h2>;

    return (
        <div style={{ maxWidth: '600px', margin: '40px auto', padding: '30px', border: '1px solid #ddd', borderRadius: '8px', backgroundColor: '#f9f9f9' }}>
            <h2 style={{ textAlign: 'center', marginBottom: '20px' }}>Editar Paquete Turístico</h2>
            
            <form onSubmit={handleSubmit}>
                <div style={{ display: 'flex', gap: '15px' }}>
                    <div style={{ flex: 1 }}>
                        <label>Nombre del Paquete:</label>
                        <input type="text" name="name" value={formData.name} onChange={handleChange} required style={inputStyle} />
                    </div>
                    <div style={{ flex: 1 }}>
                        <label>Destino:</label>
                        {}
                        <input type="text" name="destination" value={formData.destination} onChange={handleChange} required style={inputStyle} />
                    </div>
                </div>

                <label>Descripción:</label>
                <textarea name="description" value={formData.description} onChange={handleChange} required style={{ ...inputStyle, height: '80px' }} />

                <div style={{ display: 'flex', gap: '15px' }}>
                    <div style={{ flex: 1 }}>
                        <label>Fecha de Salida:</label>
                        <input type="date" name="startDate" value={formData.startDate} onChange={handleChange} required style={inputStyle} />
                    </div>
                    <div style={{ flex: 1 }}>
                        <label>Fecha de Regreso:</label>
                        <input type="date" name="endDate" value={formData.endDate} onChange={handleChange} required style={inputStyle} />
                    </div>
                </div>

                <div style={{ display: 'flex', gap: '15px' }}>
                    <div style={{ flex: 1 }}>
                        <label>Precio (CLP):</label>
                        <input type="number" name="price" value={formData.price} onChange={handleChange} required style={inputStyle} min="1" />
                    </div>
                    <div style={{ flex: 1 }}>
                        <label>Capacidad (Cupos):</label>
                        <input type="number" name="capacity" value={formData.capacity} onChange={handleChange} required style={inputStyle} min="1" />
                    </div>
                </div>

                <div style={{ display: 'flex', gap: '15px' }}>
                    <div style={{ flex: 1 }}>
                        <label>Tipo de Viaje:</label>
                        <input type="text" name="tripType" value={formData.tripType} onChange={handleChange} required style={inputStyle} />
                    </div>
                    <div style={{ flex: 1 }}>
                        <label>Temporada:</label>
                        <select name="season" value={formData.season} onChange={handleChange} required style={inputStyle}>
                            <option value="Verano">Verano</option>
                            <option value="Otoño">Otoño</option>
                            <option value="Invierno">Invierno</option>
                            <option value="Primavera">Primavera</option>
                        </select>
                    </div>
                </div>

                <label>Estado del Paquete:</label>
                <select name="tourStatus" value={formData.tourStatus} onChange={handleChange} required style={inputStyle}>
                    <option value="PENDING">Pendiente</option>
                    <option value="AVAILABLE">Disponible</option>
                    <option value="SOLD_OUT">Vendido</option>
                    <option value="DISABLED">No Disponible</option>
                    <option value="CANCELED">Cancelado</option>
                </select>

                <button type="submit" style={{ width: '100%', padding: '12px', backgroundColor: '#3498db', color: 'white', border: 'none', borderRadius: '4px', cursor: 'pointer', fontSize: '16px', fontWeight: 'bold' }}>
                    Guardar Cambios
                </button>
            </form>
        </div>
    );
};

export default EditPackage;
