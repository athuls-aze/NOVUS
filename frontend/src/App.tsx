import { BrowserRouter as Router, Routes, Route, Link, useNavigate } from 'react-router-dom';
import Dashboard from './pages/Dashboard';
import Login from './pages/Login';
import Register from './pages/Register';
import AddDevice from './pages/AddDevice';
import DeviceDetail from './pages/DeviceDetail';
import ConditionAssessment from './pages/ConditionAssessment';

function NavBar() {
  const navigate = useNavigate();
  const token = localStorage.getItem('token');
  const handleLogout = () => {
    localStorage.removeItem('token');
    navigate('/login');
  };
  return (
    <header className="bg-gray-800 border-b border-gray-700 px-6 py-4 flex items-center justify-between shadow-md">
      <Link to="/" className="text-2xl font-bold text-white tracking-widest hover:text-indigo-400 transition">NOVUS</Link>
      <nav className="flex items-center gap-4 text-sm">
        {token ? (
          <>
            <Link to="/" className="text-gray-300 hover:text-white transition">My Devices</Link>
            <Link to="/add-device" className="bg-indigo-600 hover:bg-indigo-500 text-white px-4 py-1.5 rounded-lg font-medium transition">+ Add Device</Link>
            <button onClick={handleLogout} className="text-gray-400 hover:text-red-400 transition">Logout</button>
          </>
        ) : (
          <>
            <Link to="/login" className="text-gray-300 hover:text-white transition">Login</Link>
            <Link to="/register" className="bg-indigo-600 hover:bg-indigo-500 text-white px-4 py-1.5 rounded-lg font-medium transition">Register</Link>
          </>
        )}
      </nav>
    </header>
  );
}

function App() {
  return (
    <Router>
      <div className="min-h-screen bg-gray-900 text-gray-100 font-sans">
        <NavBar />
        <main className="p-6 max-w-5xl mx-auto">
          <Routes>
            <Route path="/" element={<Dashboard />} />
            <Route path="/login" element={<Login />} />
            <Route path="/register" element={<Register />} />
            <Route path="/add-device" element={<AddDevice />} />
            <Route path="/devices/:id" element={<DeviceDetail />} />
            <Route path="/devices/:id/condition" element={<ConditionAssessment />} />
          </Routes>
        </main>
      </div>
    </Router>
  );
}

export default App;
