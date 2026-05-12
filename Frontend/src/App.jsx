import { useEffect } from 'react';
import { BrowserRouter, Routes, Route, useNavigate } from 'react-router-dom';
import { useKeycloak } from '@react-keycloak/web';
import Navbar from './components/Navbar';
import PrivateRoute from './components/PrivateRoute';
import api from './api/axios';
import HomePView from './views/HomePageView';
import RegisterView from './views/RegisterView';
import CreatePackage from './views/AdminPackageView';
import EditPackage from './views/EditPackageView';
import BookingsView from './views/BookingsView';
import PaymentView from './views/PaymentView';
import ReportsView from './views/ReportView';
import ProfileView from './views/ProfileView';

const ProfileCheck = ({ children }) => {
    const { keycloak, initialized } = useKeycloak();
    const navigate = useNavigate();

    useEffect(() => {
        if (initialized && keycloak.authenticated) {
            api.get('/api/user/profile')
                .then(res => console.log("Usuario verificado en BD:", res.data.username))
                .catch(error => {
                    if (error.response && error.response.status === 404) {
                        console.log("¡Usuario nuevo! Mandando al formulario...");
                        navigate('/registro-extra');
                    }
                });
        }
    }, [initialized, keycloak.authenticated, navigate]);

    return children;
};

function App() {
  return (
    <BrowserRouter>
      <Navbar /> 
      
      <ProfileCheck>
          <div className="container" style={{ padding: '20px' }}>
            <Routes>
              <Route path="/" element={<HomePView />} />
              <Route path="/registro-extra" element={<RegisterView />} /> 
              <Route 
                path="/reports" 
                element={
                  <PrivateRoute roleRequired="ADMIN">
                    <ReportsView /> 
                  </PrivateRoute>
                } 
              />
              <Route path="/admin/crear-paquete" element={ 
                <PrivateRoute roleRequired="ADMIN">
                  <CreatePackage />
                </PrivateRoute>
              } />
              <Route path="/admin/editar-paquete/:id" element={
                <PrivateRoute roleRequired="ADMIN">
                  <EditPackage />
                </PrivateRoute>
              } />
              <Route path="/mis-reservas" element={<BookingsView />} />
              <Route path="/pagar/:bookingId" element={<PaymentView />} />
              <Route path="/perfil" element={<ProfileView />} />
            </Routes>
          </div>
      </ProfileCheck>
    </BrowserRouter>
  );
}

export default App;