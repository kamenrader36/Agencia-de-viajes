import React, { useState } from 'react';
import { useNavigate, useLocation, useParams } from 'react-router-dom';
import api from '../api/axios';

const PaymentView = () => {
    const navigate = useNavigate();
    const location = useLocation();
    const { bookingId } = useParams();
    
    const amount = location.state?.amount || 0;

    const [paymentData, setPaymentData] = useState({
        cardNumber: '',
        expireDate: '',
        cvv: '',
        amountToPay: amount,
        bookingId: bookingId
    });

    const handleChange = (e) => {
        const { name, value } = e.target;

        if ((name === 'cardNumber' || name === 'cvv') && value !== '' && !/^\d+$/.test(value)) return;
        setPaymentData({ ...paymentData, [name]: value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const response = await api.post('/api/payment/process', paymentData);
            const recibo = response.data;
            const montoFinal = recibo?.amountPaid || paymentData.amountToPay;
            alert(`¡Pago procesado con éxito!\n
            Detalle de la transacción:
            - Estado Pago: ${recibo.paymentStatus}
            - Estado Reserva: ${recibo.bookingStatus}
            TOTAL PAGADO: $${Number(montoFinal).toLocaleString('es-CL')}`);  
            navigate('/mis-reservas');
        } catch (error) {
            console.error("Error en el pago:", error);
            alert("Hubo un error al procesar el pago. Por favor, verifica los datos de tu tarjeta.");
        }
    };

    const inputStyle = { width: '100%', padding: '12px', marginBottom: '15px', borderRadius: '4px', border: '1px solid #ccc', fontSize: '16px' };

    return (
        <div style={{ maxWidth: '500px', margin: '50px auto', padding: '30px', boxShadow: '0 4px 15px rgba(0,0,0,0.1)', borderRadius: '10px', backgroundColor: '#fff' }}>
            <h2 style={{ textAlign: 'center', color: '#2c3e50', marginBottom: '20px' }}>Finalizar Pago 💳</h2>
            
            <div style={{ backgroundColor: '#f8f9fa', padding: '15px', borderRadius: '8px', marginBottom: '20px', textAlign: 'center' }}>
                <p style={{ margin: 0, color: '#7f8c8d' }}>Monto a pagar:</p>
                <h3 style={{ margin: '5px 0', color: '#27ae60' }}>${amount.toLocaleString('es-CL')} CLP</h3>
            </div>

            <form onSubmit={handleSubmit}>
                <label>Número de Tarjeta (16 dígitos):</label>
                <input 
                    type="text" name="cardNumber" maxLength="16" required 
                    placeholder="0000 0000 0000 0000"
                    value={paymentData.cardNumber} onChange={handleChange} style={inputStyle} 
                />

                <div style={{ display: 'flex', gap: '15px' }}>
                    <div style={{ flex: 1 }}>
                        <label>Fecha Exp. (MM/AA):</label>
                        <input 
                            type="text" name="expireDate" placeholder="MM/AA" required 
                            value={paymentData.expireDate} onChange={handleChange} style={inputStyle} 
                        />
                    </div>
                    <div style={{ flex: 1 }}>
                        <label>CVV:</label>
                        <input 
                            type="password" name="cvv" maxLength="3" required 
                            placeholder="123"
                            value={paymentData.cvv} onChange={handleChange} style={inputStyle} 
                        />
                    </div>
                </div>

                <button type="submit" style={{ 
                    width: '100%', padding: '15px', backgroundColor: '#2ecc71', color: 'white', 
                    border: 'none', borderRadius: '5px', fontSize: '18px', fontWeight: 'bold', cursor: 'pointer', marginTop: '10px'
                }}>
                    Confirmar Pago
                </button>
                
                <button type="button" onClick={() => navigate('/mis-reservas')} style={{ 
                    width: '100%', padding: '10px', backgroundColor: 'transparent', color: '#e74c3c', 
                    border: 'none', cursor: 'pointer', marginTop: '10px', textDecoration: 'underline'
                }}>
                    Cancelar
                </button>
            </form>
        </div>
    );
};

export default PaymentView;