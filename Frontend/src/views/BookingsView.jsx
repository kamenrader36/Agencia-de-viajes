import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useKeycloak } from '@react-keycloak/web';
import api from '../api/axios';

const BookingsView = () => {
    const { keycloak } = useKeycloak();
    const navigate = useNavigate();
    const [bookings, setBookings] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        api.get(`/api/booking/my-bookings/${keycloak.subject}`)
            .then(response => {
                setBookings(response.data);
                setLoading(false);
            })
            .catch(error => {
                console.error("Error al cargar reservas:", error);
                setLoading(false);
            });
    }, [keycloak.subject]);

    const handleCancel = async (bookingId) => {
        if (window.confirm("¿Estás seguro de que deseas cancelar esta reserva?")) {
            try {
                await api.put(`/api/booking/cancel/${bookingId}`);
                alert("Reserva cancelada con éxito.");
                setBookings(bookings.filter(b => b.bookingId !== bookingId));
            } catch (error) {
                console.error("Error al cancelar:", error);
                alert("No se pudo cancelar la reserva.");
            }
        }
    };

    if (loading) return <h2 style={{ textAlign: 'center', marginTop: '50px' }}>Cargando tus reservas...</h2>;

    const activeBookings = bookings.filter(b => b.bookingStatus !== 'CANCELED');

    return (
        <div style={{ maxWidth: '800px', margin: '40px auto', padding: '20px' }}>
            <h2 style={{ textAlign: 'center', marginBottom: '30px' }}>Mis Reservas</h2>
            
            {activeBookings.length === 0 ? (
                <div style={{ textAlign: 'center', padding: '40px', backgroundColor: '#f9f9f9', borderRadius: '8px' }}>
                    <h3>Aún no tienes reservas activas</h3>
                </div>
            ) : (
                <div style={{ display: 'flex', flexDirection: 'column', gap: '15px' }}>
                    {activeBookings.map(booking => (
                        <div key={booking.bookingId} style={{ 
                            display: 'flex', justifyContent: 'space-between', alignItems: 'center',
                            padding: '20px', border: '1px solid #ddd', borderRadius: '8px',
                            boxShadow: '0 2px 5px rgba(0,0,0,0.05)'
                        }}>
                            <div>
                                <h3 style={{ margin: '0 0 10px 0', color: '#2c3e50' }}>{booking.packageName}</h3>
                                <p style={{ margin: 0, color: '#7f8c8d' }}>Pasajeros: {booking.numberOfPassengers}</p>
                                <p style={{ margin: '5px 0 0 0', fontWeight: 'bold', color: '#27ae60' }}>
                                    Total: ${booking.finalPriceToPay.toLocaleString('es-CL')} CLP
                                </p>
                            </div>
                            
                            <div style={{ textAlign: 'right', minWidth: '250px' }}>
                                <div style={{ marginBottom: '10px' }}>
                                    <span style={{ 
                                        padding: '5px 12px', borderRadius: '15px', fontSize: '13px', fontWeight: 'bold',
                                        backgroundColor: booking.bookingStatus === 'PENDING' ? '#f1c40f' : '#2ecc71',
                                        color: booking.bookingStatus === 'PENDING' ? '#000' : '#fff'
                                    }}>
                                        {booking.bookingStatus === 'PENDING' ? 'PENDIENTE DE PAGO' : 'PAGADO'}
                                    </span>
                                </div>
                                
                                {booking.bookingStatus === 'PENDING' && (
                                    <div style={{ display: 'flex', gap: '10px', justifyContent: 'flex-end' }}>
                                        <button 
                                            onClick={() => navigate(`/pagar/${booking.bookingId}`, { state: { amount: booking.finalPriceToPay } })}
                                            style={{ padding: '8px 15px', backgroundColor: '#3498db', color: 'white', border: 'none', borderRadius: '4px', cursor: 'pointer', fontWeight: 'bold' }}
                                        >
                                            Pagar Ahora
                                        </button>
                                        <button 
                                            onClick={() => handleCancel(booking.bookingId)}
                                            style={{ padding: '8px 15px', backgroundColor: '#e74c3c', color: 'white', border: 'none', borderRadius: '4px', cursor: 'pointer', fontWeight: 'bold' }}
                                        >
                                            Cancelar
                                        </button>
                                    </div>
                                )}
                            </div>
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
};

export default BookingsView;