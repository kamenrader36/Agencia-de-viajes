import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Navbar from './components/Navbar';

const Home = () => (
  <div>
    <h2>Explore Packages</h2>
    <p>Here we will fetch and display the travel packages...</p>
  </div>
);

const Reports = () => (
  <div>
    <h2>Sales Reports</h2>
    <p>Here we will show the admin report tables...</p>
  </div>
);

function App() {
  return (
    <BrowserRouter>
      {/* El Navbar queda fuera de las Routes para que siempre sea visible */}
      <Navbar />
      
      {/* Contenedor principal para dar un poco de margen al contenido */}
      <div style={{ padding: '20px' }}>
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/reports" element={<Reports />} />
        </Routes>
      </div>
    </BrowserRouter>
  );
}

export default App;