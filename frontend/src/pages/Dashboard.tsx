import { useEffect, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';

export default function Dashboard() {
  const [devices, setDevices] = useState([]);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    const token = localStorage.getItem('token');
    if (!token) { navigate('/login'); return; }
    fetch('/api/v1/user-devices', {
      headers: { 'Authorization': `Bearer ${token}` }
    })
      .then(res => {
        if (res.status === 401 || res.status === 403) { navigate('/login'); return null; }
        return res.json();
      })
      .then(data => {
        if (data) setDevices(data.content || []);
        setLoading(false);
      })
      .catch(err => {
        console.error(err);
        setLoading(false);
      });
  }, [navigate]);

  if (loading) return <div className="text-center p-10 text-apple-secondary font-medium">Loading devices...</div>;

  return (
    <div className="max-w-4xl mx-auto animate-fade-in-up">
      <div className="flex justify-between items-center mb-8">
        <h2 className="text-3xl font-bold text-apple-primary tracking-tight">Your Devices</h2>
      </div>

      {devices.length === 0 ? (
        <div className="soft-card p-12 text-center">
          <p className="text-apple-secondary mb-6 font-medium text-base">You haven't registered any devices yet.</p>
          <Link
            to="/add-device"
            className="inline-block bg-apple-blue hover:opacity-90 text-white font-semibold px-6 py-3 rounded-full transition-all shadow-sm"
          >
            + Register Your First Device
          </Link>
        </div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
          {devices.map((device: any, i: number) => (
            <div key={device.id} className={`soft-card p-6 transition-all hover:-translate-y-1 hover:shadow-lg flex flex-col justify-between animate-fade-in-up delay-${(i % 4) * 100}`}>
              <div>
                <div className="flex justify-between items-start mb-4">
                  <h3 className="text-xl font-bold text-apple-primary">
                    {device.variant?.modelName || 'Device'} {device.variant?.storage}
                  </h3>
                  <span className="text-xs bg-apple-border px-2 py-1 rounded-full font-medium text-apple-secondary border border-apple-border">#{i + 1}</span>
                </div>
                <p className="text-apple-secondary text-sm mb-1">Purchased: <span className="text-apple-primary font-medium">{device.purchaseDate || 'N/A'}</span></p>
                <p className="text-apple-secondary text-sm mb-6">Price: <span className="text-apple-primary font-medium">{device.purchasePrice ? `₹${Number(device.purchasePrice).toLocaleString('en-IN')}` : 'N/A'}</span></p>
              </div>
              <Link 
                to={`/devices/${device.id}`}
                className="w-full text-center bg-apple-border hover:bg-slate-200 text-apple-primary font-semibold py-2.5 px-4 rounded-xl transition-colors shadow-sm"
              >
                View {device.variant?.modelName} {device.variant?.color} Details
              </Link>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}

