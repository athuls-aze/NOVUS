import React, { useState } from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';

export default function ConditionAssessment() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [formData, setFormData] = useState({
    batteryHealth: 100,
    displayCondition: 'EXCELLENT',
    bodyCondition: 'EXCELLENT',
    cameraCondition: 'EXCELLENT',
    chargingCondition: 'EXCELLENT'
  });

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    const token = localStorage.getItem('token');
    if (!token) {
      navigate('/login');
      return;
    }
    setError('');
    setLoading(true);
    try {
      const res = await fetch(`/api/v1/user-devices/${id}/condition`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify(formData)
      });
      if (res.status === 401 || res.status === 403) {
        localStorage.removeItem('token');
        navigate('/login');
        return;
      }
      if (res.ok) {
        // Automatically calculate and save valuation
        await fetch(`/api/v1/user-devices/${id}/valuation`, {
          method: 'POST',
          headers: { 'Authorization': `Bearer ${token}` }
        });
        navigate(`/devices/${id}`);
      } else {
        const errData = await res.json().catch(() => ({}));
        setError(errData.error || errData.message || 'Failed to submit condition assessment');
      }
    } catch (err: any) {
      setError(err.message || 'Network error occurred. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  const grades = ['EXCELLENT', 'GOOD', 'FAIR', 'POOR'];

  return (
    <div className="max-w-2xl mx-auto bg-gray-800 p-8 rounded-xl border border-gray-700 shadow-xl">
      <div className="flex justify-between items-center mb-6">
        <div>
          <h2 className="text-3xl font-bold text-white">Condition Assessment</h2>
          <p className="text-gray-400 text-sm mt-1">Diagnose your device condition to determine fair resale value.</p>
        </div>
        <Link to={`/devices/${id}`} className="text-xs bg-gray-700 hover:bg-gray-600 text-gray-300 px-3 py-1.5 rounded-lg transition">
          ✕ Cancel
        </Link>
      </div>

      {error && (
        <div className="bg-red-500/20 border border-red-500/40 text-red-300 px-4 py-3 rounded-lg text-sm mb-6">
          {error}
        </div>
      )}

      <form onSubmit={handleSubmit} className="space-y-6">
        <div>
          <div className="flex justify-between items-center mb-1">
            <label className="block text-sm text-gray-400 font-medium">Battery Maximum Capacity (%)</label>
            <span className="text-xs font-semibold px-2 py-0.5 rounded bg-gray-700 text-indigo-300">{formData.batteryHealth}%</span>
          </div>
          <input 
            type="number" 
            min="0" 
            max="100" 
            value={formData.batteryHealth}
            onChange={e => setFormData({...formData, batteryHealth: Math.min(100, Math.max(0, parseInt(e.target.value) || 0))})}
            className="w-full bg-gray-900 border border-gray-700 rounded-lg p-3 text-white focus:outline-none focus:border-indigo-500 transition-colors" 
            required
          />
          <p className="text-xs text-gray-500 mt-1">Check in iPhone Settings &gt; Battery &gt; Battery Health &amp; Charging.</p>
        </div>
        
        {['displayCondition', 'bodyCondition', 'cameraCondition', 'chargingCondition'].map((field) => (
          <div key={field}>
            <label className="block text-sm text-gray-400 mb-1 capitalize font-medium">
              {field.replace('Condition', '')} Condition
            </label>
            <select 
              value={(formData as any)[field]} 
              onChange={e => setFormData({...formData, [field]: e.target.value})}
              className="w-full bg-gray-900 border border-gray-700 rounded-lg p-3 text-white focus:outline-none focus:border-indigo-500 transition-colors"
            >
              {grades.map(g => <option key={g} value={g}>{g}</option>)}
            </select>
          </div>
        ))}
        
        <button 
          type="submit" 
          disabled={loading}
          className="w-full bg-gradient-to-r from-indigo-600 to-purple-600 hover:from-indigo-500 hover:to-purple-500 disabled:opacity-50 text-white font-bold py-3.5 rounded-lg transition-all shadow-md flex items-center justify-center gap-2"
        >
          {loading ? (
            <>
              <span className="w-4 h-4 border-2 border-white/30 border-t-white rounded-full animate-spin"></span>
              Evaluating Valuation Algorithm...
            </>
          ) : (
            'Submit & Calculate Fair Value ➔'
          )}
        </button>
      </form>
    </div>
  );
}
