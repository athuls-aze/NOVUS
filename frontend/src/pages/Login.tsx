import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';

export default function Login() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const handleLogin = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');
    setLoading(true);
    try {
      const res = await fetch('/api/v1/auth/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email, password })
      });
      if (res.ok) {
        const data = await res.json();
        localStorage.setItem('token', data.token);
        navigate('/');
      } else {
        const errData = await res.json().catch(() => ({}));
        setError(errData.message || errData.error || 'Invalid credentials. Please verify your email and password.');
      }
    } catch {
      setError('Unable to reach server. Please ensure the backend is running.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="max-w-md mx-auto bg-gray-800 p-8 rounded-xl border border-gray-700 shadow-xl mt-10">
      <h2 className="text-3xl font-bold mb-2 text-center text-white">Welcome Back</h2>
      <p className="text-gray-400 text-center mb-6 text-sm">Sign in to manage your devices and valuations</p>

      {error && (
        <div className="bg-red-500/20 border border-red-500/40 text-red-300 px-4 py-3 rounded-lg text-sm mb-4">
          {error}
        </div>
      )}

      <form onSubmit={handleLogin} className="space-y-4">
        <div>
          <label className="block text-sm text-gray-400 mb-1 font-medium">Email Address</label>
          <input 
            type="email" 
            value={email} 
            onChange={e => setEmail(e.target.value)} 
            required
            placeholder="test@example.com"
            className="w-full bg-gray-900 border border-gray-700 rounded-lg p-3 text-white focus:outline-none focus:border-indigo-500 transition-colors" 
          />
        </div>
        <div>
          <label className="block text-sm text-gray-400 mb-1 font-medium">Password</label>
          <input 
            type="password" 
            value={password} 
            onChange={e => setPassword(e.target.value)} 
            required
            placeholder="••••••••"
            className="w-full bg-gray-900 border border-gray-700 rounded-lg p-3 text-white focus:outline-none focus:border-indigo-500 transition-colors" 
          />
        </div>
        <button 
          type="submit" 
          disabled={loading}
          className="w-full bg-indigo-600 hover:bg-indigo-500 disabled:opacity-50 text-white font-bold py-3 rounded-lg transition-colors shadow-md flex items-center justify-center gap-2"
        >
          {loading ? 'Authenticating...' : 'Sign In'}
        </button>
      </form>

      <div className="mt-6 text-center text-sm text-gray-400 border-t border-gray-700/60 pt-4">
        Don't have an account?{' '}
        <Link to="/register" className="text-indigo-400 hover:text-indigo-300 font-semibold transition">
          Create Account
        </Link>
      </div>

      <div className="mt-3 p-3 bg-gray-900/60 rounded-lg border border-gray-700 text-xs text-gray-400 text-center">
        Demo Login: <span className="text-gray-200">test@example.com</span> / <span className="text-gray-200">password123</span>
      </div>
    </div>
  );
}
