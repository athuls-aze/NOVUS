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

  if (loading) return <div className="text-center p-10">Loading devices...</div>;

  return (
    <div className="max-w-4xl mx-auto">
      <div className="flex justify-between items-center mb-6">
        <h2 className="text-3xl font-semibold">Your Devices</h2>
        <Link to="/add-device" className="bg-indigo-600 hover:bg-indigo-700 text-white py-2 px-4 rounded-lg font-medium transition-colors">
          + Add Device
        </Link>
      </div>

      {devices.length === 0 ? (
        <div className="bg-gray-800 rounded-xl p-10 text-center shadow-lg border border-gray-700">
          <p className="text-gray-400 mb-4">You haven't registered any devices yet.</p>
        </div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
          {devices.map((device: any) => (
            <Link key={device.id} to={`/devices/${device.id}`} className="bg-gray-800 rounded-xl p-6 shadow-lg border border-gray-700 hover:border-indigo-500 hover:shadow-indigo-500/20 transition-all group">
              <div className="flex justify-between items-start mb-2">
                <h3 className="text-xl font-bold group-hover:text-indigo-400 transition-colors">
                  {device.variant?.color} {device.variant?.storage}
                </h3>
                <span className="text-xs bg-gray-700 px-2 py-1 rounded text-gray-300">#{device.id}</span>
              </div>
              <p className="text-gray-400 text-sm mb-1">Purchased: <span className="text-gray-300">{device.purchaseDate || 'N/A'}</span></p>
              <p className="text-gray-400 text-sm">Price: <span className="text-gray-300">{device.purchasePrice ? `Rs. ${device.purchasePrice}` : 'N/A'}</span></p>
            </Link>
          ))}
        </div>
      )}
    </div>
  );
}
