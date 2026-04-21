import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Navbar from './components/Navbar';
import PrivateRoute from './components/PrivateRoute';

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
      <Navbar />
      <div className="main-content">
        <Routes>
          {}
          <Route path="/" element={<Home />} />

          {}
          <Route 
            path="/reports" 
            element={
              <PrivateRoute roleRequired="ADMIN">
                <Reports />
              </PrivateRoute>
            } 
          />
        </Routes>
      </div>
    </BrowserRouter>
  );
}

export default App;