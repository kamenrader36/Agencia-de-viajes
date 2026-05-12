import React, { useState } from 'react';
import api from '../api/axios';

const ReportsView = () => {
    const [dates, setDates] = useState({ start: '', end: '' });
    const [salesReport, setSalesReport] = useState([]);
    const [rankingReport, setRankingReport] = useState([]);
    const [loading, setLoading] = useState(false);

    const handleFetchReports = async () => {
        if (!dates.start || !dates.end) {
            alert("Por favor selecciona ambas fechas.");
            return;
        }
        
        setLoading(true);
        try {
            const [salesRes, rankingRes] = await Promise.all([
                api.get(`api/reports/sales?start=${dates.start}&end=${dates.end}`),
                api.get(`api/reports/ranking?start=${dates.start}&end=${dates.end}`)
            ]);
            
            setSalesReport(salesRes.data);
            setRankingReport(rankingRes.data);
        } catch (error) {
            console.error("Error al obtener reportes:", error);
            alert("Hubo un error al generar los reportes.");
        } finally {
            setLoading(false);
        }
    };
    
    const handleCancelBooking = async (bookingId) => {
        if (window.confirm("¿Estás seguro de que deseas cancelar esta reserva de cliente?")) {
            try {
                await api.put(`/api/booking/cancel/${bookingId}`);
                alert("Reserva cancelada con éxito.");
                handleFetchReports(); 
            } catch (error) {
                console.error("Error al cancelar:", error);
                alert("No se pudo cancelar: " + (error.response?.data || "Error del servidor"));
            }
        }
    };

    const tableHeaderStyle = { backgroundColor: '#2c3e50', color: 'white', padding: '12px', textAlign: 'left' };
    const tableCellStyle = { padding: '10px', borderBottom: '1px solid #ddd' };

    return (
        <div style={{ padding: '20px', maxWidth: '1000px', margin: '0 auto' }}>
            <h1 style={{ color: '#2c3e50' }}>Panel de Reportes Administrativos</h1>
            
            {/* Filtros de Fecha */}
            <div style={{ display: 'flex', gap: '20px', alignItems: 'center', marginBottom: '30px', backgroundColor: '#f4f4f4', padding: '20px', borderRadius: '8px' }}>
                <div>
                    <label style={{ marginRight: '10px' }}>Fecha Inicio:</label>
                    <input type="date" value={dates.start} onChange={(e) => setDates({...dates, start: e.target.value})} style={{ padding: '8px' }} />
                </div>
                <div>
                    <label style={{ marginRight: '10px' }}>Fecha Fin:</label>
                    <input type="date" value={dates.end} onChange={(e) => setDates({...dates, end: e.target.value})} style={{ padding: '8px' }} />
                </div>
                <button 
                    onClick={handleFetchReports} 
                    disabled={loading}
                    style={{ padding: '10px 20px', backgroundColor: '#27ae60', color: 'white', border: 'none', borderRadius: '4px', cursor: 'pointer', fontWeight: 'bold' }}
                >
                    {loading ? 'Generando...' : 'Generar Reportes'}
                </button>
            </div>

            {/* Ranking de Paquetes */}
            <section style={{ marginBottom: '40px' }}>
                <h2 style={{ color: '#e67e22' }}>Ranking de Paquetes Más Vendidos</h2>
                <table style={{ width: '100%', borderCollapse: 'collapse', marginTop: '10px', boxShadow: '0 2px 5px rgba(0,0,0,0.1)' }}>
                    <thead>
                        <tr>
                            <th style={tableHeaderStyle}>Paquete Turístico</th>
                            <th style={tableHeaderStyle}>N° Reservas</th>
                            <th style={tableHeaderStyle}>Total Pasajeros</th>
                            <th style={tableHeaderStyle}>Ingresos Totales</th>
                        </tr>
                    </thead>
                    <tbody>
                        {rankingReport.length > 0 ? rankingReport.map((item, index) => (
                            <tr key={index}>
                                <td style={tableCellStyle}>{item.tourPackageName}</td>
                                <td style={tableCellStyle}>{item.bookingNumbers}</td>
                                <td style={tableCellStyle}>{item.totalPassengers}</td>
                                <td style={{ ...tableCellStyle, fontWeight: 'bold', color: '#27ae60' }}>
                                    ${item.profit.toLocaleString('es-CL')}
                                </td>
                            </tr>
                        )) : <tr><td colSpan="4" style={{ textAlign: 'center', padding: '20px' }}>No hay datos para este rango.</td></tr>}
                    </tbody>
                </table>
            </section>

            {/* Detalle de Ventas */}
            <section>
                <h2 style={{ color: '#2980b9' }}>Listado Detallado de Ventas</h2>
                <table style={{ width: '100%', borderCollapse: 'collapse', marginTop: '10px', boxShadow: '0 2px 5px rgba(0,0,0,0.1)' }}>
                    <thead>
                        <tr>
                            <th style={tableHeaderStyle}>Fecha</th>
                            <th style={tableHeaderStyle}>Usuario</th>
                            <th style={tableHeaderStyle}>Paquete</th>
                            <th style={tableHeaderStyle}>Pasajeros</th>
                            <th style={tableHeaderStyle}>Monto Pagado</th>
                            <th style={tableHeaderStyle}>Estado</th>
                            <th style={tableHeaderStyle}>Cancelar</th>
                        </tr>
                    </thead>
                    <tbody>
                        {salesReport.length > 0 ? salesReport.map((sale, index) => (
                            <tr key={index}>
                                <td style={tableCellStyle}>{new Date(sale.saleDate).toLocaleDateString()}</td>
                                <td style={tableCellStyle}>{sale.username}</td>
                                <td style={tableCellStyle}>{sale.tourPackageName}</td>
                                <td style={tableCellStyle}>{sale.numberOfPassengers}</td>
                                <td style={tableCellStyle}>${sale.amountPaid.toLocaleString('es-CL')}</td>
                                <td style={tableCellStyle}>{sale.bookingStatus}</td>
                                <td style={tableCellStyle}>
                                    {sale.bookingStatus !== 'CANCELED' ? (
                                        <button 
                                            onClick={() => handleCancelBooking(sale.bookingId)}
                                            style={{
                                                backgroundColor: '#e74c3c',
                                                color: 'white',
                                                border: 'none',
                                                padding: '5px 10px',
                                                borderRadius: '4px',
                                                cursor: 'pointer',
                                                fontSize: '0.85em'
                                            }}
                                        >
                                        Cancelar
                                        </button>
                                    ) : (
                                    <span style={{ color: '#95a5a6', fontSize: '0.8em' }}>Sin acción</span>
                                    )}
                                </td>
                            </tr>
                        )) : <tr><td colSpan="5" style={{ textAlign: 'center', padding: '20px' }}>Selecciona un rango para ver las ventas.</td></tr>}
                    </tbody>
                </table>
            </section>
        </div>
    );
};

export default ReportsView;