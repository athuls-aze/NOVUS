import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

const API = '/api/v1';

interface Brand { id: number; name: string; }
interface Series { id: number; name: string; }
interface Model  { id: number; name: string; releaseDate: string; }
interface Variant { id: number; storage: string; ram: string; color: string; basePrice: number; }

export default function AddDevice() {
  const navigate = useNavigate();
  const token = localStorage.getItem('token');

  const [brands, setBrands]   = useState<Brand[]>([]);
  const [series, setSeries]   = useState<Series[]>([]);
  const [models, setModels]   = useState<Model[]>([]);
  const [variants, setVariants] = useState<Variant[]>([]);

  const [brandId, setBrandId]   = useState('');
  const [seriesId, setSeriesId] = useState('');
  const [modelId, setModelId]   = useState('');
  const [variantId, setVariantId] = useState('');
  const [purchaseDate, setPurchaseDate] = useState('');
  const [purchasePrice, setPurchasePrice] = useState('');

  const [loading, setLoading] = useState(false);
  const [error, setError]     = useState('');

  useEffect(() => {
    fetch(`${API}/catalog/brands`)
      .then(r => r.json()).then(setBrands).catch(() => setError('Failed to load brands'));
  }, []);

  useEffect(() => {
    if (!brandId) { setSeries([]); setSeriesId(''); return; }
    fetch(`${API}/catalog/brands/${brandId}/series`)
      .then(r => r.json()).then(setSeries).catch(() => setError('Failed to load series'));
  }, [brandId]);

  useEffect(() => {
    if (!seriesId) { setModels([]); setModelId(''); return; }
    fetch(`${API}/catalog/series/${seriesId}/models`)
      .then(r => r.json()).then(setModels).catch(() => setError('Failed to load models'));
  }, [seriesId]);

  useEffect(() => {
    if (!modelId) { setVariants([]); setVariantId(''); return; }
    fetch(`${API}/catalog/models/${modelId}/variants`)
      .then(r => r.json()).then(setVariants).catch(() => setError('Failed to load variants'));
  }, [modelId]);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!variantId) { setError('Please select a variant'); return; }
    if (!token) { navigate('/login'); return; }
    setLoading(true);
    setError('');
    try {
      const res = await fetch(`${API}/user-devices`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json', 'Authorization': `Bearer ${token}` },
        body: JSON.stringify({
          variantId: Number(variantId),
          purchaseDate: purchaseDate || null,
          purchasePrice: purchasePrice || null,
        }),
      });
      if (res.status === 401 || res.status === 403) {
        localStorage.removeItem('token');
        navigate('/login');
        return;
      }
      if (!res.ok) {
        const errData = await res.json().catch(() => ({}));
        let errMsg = errData.error || `Server error: ${res.status}`;
        if (errData.details) {
          errMsg += " - " + Object.values(errData.details).join(', ');
        }
        throw new Error(errMsg);
      }
      navigate('/');
    } catch (err: any) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  const selectClass = "w-full bg-apple-surface border border-apple-border rounded-lg px-4 py-2 text-apple-primary focus:outline-none focus:ring-2 focus:ring-indigo-500 disabled:opacity-40";
  const labelClass  = "block text-sm font-medium text-apple-secondary mb-1";

  return (
    <div className="max-w-xl mx-auto">
      <h2 className="text-3xl font-bold mb-2">Register New Device</h2>
      <p className="text-apple-secondary mb-6">Select your phone step by step.</p>

      {error && <div className="bg-red-900/50 border border-red-700 text-red-300 rounded-lg px-4 py-3 mb-4">{error}</div>}

      <form onSubmit={handleSubmit} className="bg-apple-surface shadow-apple-card border border-apple-border rounded-xl p-6 space-y-4">

        {/* Brand */}
        <div>
          <label className={labelClass}>Brand</label>
          <select className={selectClass} value={brandId} onChange={e => { setBrandId(e.target.value); setSeriesId(''); setModelId(''); setVariantId(''); }}>
            <option value="">-- Select Brand --</option>
            {brands.map(b => <option key={b.id} value={b.id}>{b.name}</option>)}
          </select>
        </div>

        {/* Series */}
        <div>
          <label className={labelClass}>Series</label>
          <select className={selectClass} value={seriesId} onChange={e => { setSeriesId(e.target.value); setModelId(''); setVariantId(''); }} disabled={!brandId}>
            <option value="">-- Select Series --</option>
            {series.map(s => <option key={s.id} value={s.id}>{s.name}</option>)}
          </select>
        </div>

        {/* Model */}
        <div>
          <label className={labelClass}>Model</label>
          <select className={selectClass} value={modelId} onChange={e => { setModelId(e.target.value); setVariantId(''); }} disabled={!seriesId}>
            <option value="">-- Select Model --</option>
            {models.map(m => <option key={m.id} value={m.id}>{m.name}</option>)}
          </select>
        </div>

        {/* Variant */}
        <div>
          <label className={labelClass}>Variant</label>
          <select className={selectClass} value={variantId} onChange={e => setVariantId(e.target.value)} disabled={!modelId}>
            <option value="">-- Select Variant --</option>
            {variants.map(v => <option key={v.id} value={v.id}>{v.storage} / {v.ram} / {v.color}</option>)}
          </select>
        </div>

        {/* Purchase Date */}
        <div>
          <label className={labelClass}>Purchase Date <span className="text-gray-500">(optional)</span></label>
          <input 
            type="date" 
            className={selectClass} 
            value={purchaseDate} 
            max={new Date().toISOString().split('T')[0]}
            onChange={e => setPurchaseDate(e.target.value)} 
          />
        </div>

        {/* Purchase Price */}
        <div>
          <label className={labelClass}>Purchase Price (₹) <span className="text-gray-500">(optional)</span></label>
          <input 
            type="number" 
            min="1"
            placeholder="e.g. 79900" 
            className={selectClass} 
            value={purchasePrice} 
            onChange={e => setPurchasePrice(e.target.value)} 
          />
        </div>

        <div className="flex gap-3 pt-2">
          <button type="button" onClick={() => navigate('/')}
            className="flex-1 bg-apple-surface hover:bg-apple-border text-apple-primary font-semibold py-2 rounded-lg transition">
            Cancel
          </button>
          <button type="submit" disabled={loading || !variantId}
            className="flex-1 bg-apple-blue hover:opacity-90 disabled:opacity-50 text-white font-semibold py-2 rounded-lg transition">
            {loading ? 'Registering...' : 'Register Device'}
          </button>
        </div>
      </form>
    </div>
  );
}


