import React, { useState, useEffect, useRef, useCallback } from 'react';
import { useKeycloak } from '@react-keycloak/web';
import { useNavigate } from 'react-router-dom';
import api from '../api/axios';

const HomePView = () => {
    const [trips, setTrips] = useState([]);
    const [loading, setLoading] = useState(true);
    const [filters, setFilters] = useState({ destination: '', maxPrice: '', startDate: '' });
    const { keycloak, initialized } = useKeycloak();
    const isAuth = initialized && keycloak?.authenticated;
    const isAdmin = isAuth && keycloak?.hasRealmRole('ADMIN');
    const navigate = useNavigate();

    const filtersRef = useRef(filters);

    useEffect(() => {
        filtersRef.current = filters;
    }, [filters]);

    const fetchTrips = useCallback(async (showLoading = false) => {
        if (showLoading) setLoading(true);

        try {
            const { destination, maxPrice, startDate } = filtersRef.current;
            const params = new URLSearchParams({
                destination: destination || '',
                maxPrice: maxPrice || '',
                startDate: startDate || ''
            }).toString();

            const endpoint = isAdmin 
                ? '/api/tour_packages/admin/all' 
                : `/api/tour_packages/search?${params}`;

            const response = await api.get(endpoint);
            setTrips(response.data);
        } catch (error) {
            console.error("Error al cargar paquetes:", error);
            setTrips([]);
        } finally {
            setLoading(false);
        }
    }, [isAdmin]); 

    useEffect(() => {
        if (initialized && keycloak.authenticated) {
            fetchTrips(true);
        }
    }, [initialized, keycloak.authenticated, fetchTrips]);

    const handleFilterChange = (e) => {
        setFilters({ ...filters, [e.target.name]: e.target.value });
    };

    const handleDelete = async (id) => {
        if (window.confirm("¿Estás seguro de que deseas eliminar o cancelar este paquete?")) {
            try {
                await api.delete(`/api/tour_packages/${id}`);
                alert("Paquete eliminado con éxito.");
                setTrips(trips.filter(trip => trip.tourPackageId !== id));
            } catch (error) {
                console.error("Error al eliminar:", error);
                alert("Hubo un error al eliminar el paquete.");
            }
        }
    };

    const handleBooking = async (trip) => {
        const passengersInput = window.prompt(`¿Cuántos pasajeros viajarán a ${trip.name}?`);
        if (!passengersInput) return; 

        const passengers = parseInt(passengersInput);
        if (isNaN(passengers) || passengers <= 0) {
            alert("Por favor, ingresa un número válido de pasajeros.");
            return;
        }

        const bookingDTO = {
            userKeycloak: keycloak.subject,
            tourPackageId: trip.tourPackageId,
            numberOfPassengers: passengers
        };

        try {
            const response = await api.post('/api/booking', bookingDTO, {
                headers: { Authorization: `Bearer ${keycloak.token}` }
            });
            const receipt = response.data;
            alert(`¡Reserva Exitosa!\n\nDestino: ${receipt.packageName}\nPasajeros: ${receipt.numberOfPassengers}\nTotal a Pagar: $${receipt.finalPriceToPay.toLocaleString('es-CL')}`);
            fetchTrips(false);
        } catch (error) {
            alert(error.response?.data?.message || "Hubo un error al procesar tu reserva.");
        }
    };

    if (loading) {
        return <h2 style={{ textAlign: 'center', marginTop: '50px' }}>Cargando destinos...</h2>;
    }

    return (
        <div>
            <main style={{ padding: '20px' }}>
                <h1 style={{ textAlign: 'center', marginBottom: '30px' }}>Nuestros Destinos Disponibles</h1>
                
                {!isAdmin && (
                    <div style={{ 
                        backgroundColor: '#f8f9fa', padding: '20px', borderRadius: '8px', 
                        marginBottom: '40px', display: 'flex', gap: '15px', flexWrap: 'wrap', 
                        alignItems: 'flex-end', boxShadow: '0 2px 4px rgba(0,0,0,0.05)' 
                    }}>
                        <div style={{ flex: 1, minWidth: '200px' }}>
                            <label style={{ display: 'block', marginBottom: '5px', fontWeight: 'bold' }}>Destino:</label>
                            <input type="text" name="destination" placeholder="Ej: Pucón, Paris..." 
                                value={filters.destination} onChange={handleFilterChange} 
                                style={{ width: '100%', padding: '10px', borderRadius: '4px', border: '1px solid #ccc' }} />
                        </div>
                        <div style={{ flex: 1, minWidth: '150px' }}>
                            <label style={{ display: 'block', marginBottom: '5px', fontWeight: 'bold' }}>Precio Máx:</label>
                            <input type="number" name="maxPrice" placeholder="Monto máximo" 
                                value={filters.maxPrice} onChange={handleFilterChange} 
                                style={{ width: '100%', padding: '10px', borderRadius: '4px', border: '1px solid #ccc' }} />
                        </div>
                        <div style={{ flex: 1, minWidth: '150px' }}>
                            <label style={{ display: 'block', marginBottom: '5px', fontWeight: 'bold' }}>Fecha Salida:</label>
                            <input type="date" name="startDate" 
                                value={filters.startDate} onChange={handleFilterChange} 
                                style={{ width: '100%', padding: '10px', borderRadius: '4px', border: '1px solid #ccc' }} />
                        </div>
                        <button onClick={() => fetchTrips(true)} style={{ 
                            padding: '10px 25px', backgroundColor: '#2c3e50', color: 'white', 
                            border: 'none', borderRadius: '4px', cursor: 'pointer', fontWeight: 'bold', height: '42px'
                        }}>
                            Buscar
                        </button>
                    </div>
                )}

                {trips.length === 0 ? (
                    <p style={{ textAlign: 'center', fontSize: '18px' }}>No se encontraron paquetes con esos criterios.</p>
                ) : (
                    <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(300px, 1fr))', gap: '20px' }}>
                        {trips.map(trip => (
                            <div key={trip.tourPackageId} style={{ border: '1px solid #ddd', borderRadius: '8px', padding: '20px', textAlign: 'center', boxShadow: '0 4px 8px rgba(0,0,0,0.1)' }}>
                                <h3>{trip.name}</h3>
                                <p style={{ color: '#7f8c8d' }}> {trip.destination}</p>
                                <div style={{ margin: '15px 0', fontSize: '14px', textAlign: 'left' }}>
                                    <p><strong>Salida:</strong> {trip.startDate}</p>
                                    <p><strong>Regreso:</strong> {trip.endDate}</p>
                                    <p><strong>Cupos Libres:</strong> {trip.capacity}</p>
                                </div>
                                <h4 style={{ color: '#2ecc71', fontSize: '22px' }}>${trip.price.toLocaleString('es-CL')}</h4>

                                {isAuth && !isAdmin && (
                                     <button 
                                        onClick={() => handleBooking(trip)}
                                        style={{ padding: '10px 20px', backgroundColor: '#3498db', color: 'white', border: 'none', borderRadius: '4px', cursor: 'pointer' }}
                                    >
                                        Reservar Paquete
                                    </button>
                                )}

                                {isAdmin && (
                                    <div style={{ display: 'flex', gap: '10px', marginTop: '10px' }}>
                                        <button
                                            onClick={() => navigate(`/admin/editar-paquete/${trip.tourPackageId}`)}
                                            style={{ flex: 1, padding: '8px', backgroundColor: '#f39c12', color: 'white', border: 'none', borderRadius: '4px', cursor: 'pointer' }}
                                        >
                                            Editar
                                        </button>
                                        <button
                                            onClick={() => handleDelete(trip.tourPackageId)}
                                            style={{ flex: 1, padding: '8px', backgroundColor: '#e74c3c', color: 'white', border: 'none', borderRadius: '4px', cursor: 'pointer' }}
                                        >
                                            Eliminar
                                        </button>
                                    </div>
                                )}
                            </div>
                        ))}
                    </div>
                )}
            </main>
        </div>
    );
};

export default HomePView;