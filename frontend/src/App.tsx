import { BrowserRouter as Router, Routes, Route, Link, useNavigate } from 'react-router-dom';
import Dashboard from './pages/Dashboard';
import Login from './pages/Login';
import Register from './pages/Register';
import AddDevice from './pages/AddDevice';
import DeviceDetail from './pages/DeviceDetail';
import ConditionAssessment from './pages/ConditionAssessment';
import SellDevice from './pages/SellDevice';

function NavBar() {
  const navigate = useNavigate();
  const token = localStorage.getItem('token');
  const handleLogout = () => {
    localStorage.removeItem('token');
    navigate('/login');
  };
  return (
    <header className="bg-apple-surface/80 backdrop-blur-md border-b border-apple-border px-4 sm:px-6 py-4 flex items-center justify-between sticky top-0 z-50">
      <Link to="/" className="text-xl sm:text-2xl font-extrabold text-apple-primary tracking-tight hover:text-apple-blue transition flex items-center gap-2 flex-shrink-0">
        <span className="bg-slate-900 text-white w-8 h-8 rounded-lg flex items-center justify-center text-sm font-bold">N</span>
        NOVUS
      </Link>
      <nav className="flex items-center gap-3 sm:gap-6 text-sm font-medium">
        {token ? (
          <>
            <Link to="/" className="hidden sm:block text-apple-secondary hover:text-apple-primary transition">My Devices</Link>
            <Link to="/add-device" className="bg-apple-blue hover:opacity-90 text-white shadow-sm hover:shadow-md px-3 sm:px-5 py-2 rounded-full transition-all text-xs sm:text-sm">+ Add Device</Link>
            <button onClick={handleLogout} className="text-apple-secondary hover:text-red-500 transition text-xs sm:text-sm">Logout</button>
          </>
        ) : (
          <>
            <Link to="/login" className="text-apple-secondary hover:text-apple-primary transition">Login</Link>
            <Link to="/register" className="bg-slate-900 hover:bg-slate-800 text-white px-3 sm:px-5 py-2 rounded-full transition-all text-xs sm:text-sm">Register</Link>
          </>
        )}
      </nav>
    </header>
  );
}

function App() {
  return (
    <Router>
      <div className="min-h-screen bg-apple-bg font-sans selection:bg-indigo-100 selection:text-indigo-900">
        <NavBar />
        <main className="px-4 sm:px-6 py-6 max-w-7xl mx-auto">
          <Routes>
            <Route path="/" element={<Dashboard />} />
            <Route path="/login" element={<Login />} />
            <Route path="/register" element={<Register />} />
            <Route path="/add-device" element={<AddDevice />} />
            <Route path="/devices/:id" element={<DeviceDetail />} />
            <Route path="/devices/:id/condition" element={<ConditionAssessment />} />
            <Route path="/devices/:id/sell" element={<SellDevice />} />
          </Routes>
        </main>
      </div>
    </Router>
  );
}

export default App;
