import { useEffect, useState, useRef, FormEvent } from 'react';
import { useParams, Link, useNavigate } from 'react-router-dom';

interface ChatMessage {
  id: string;
  sender: 'user' | 'assistant';
  text: string;
  timestamp: string;
}

export default function DeviceDetail() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [device, setDevice] = useState<any>(null);
  const [valuation, setValuation] = useState<any>(null);
  const [warranty, setWarranty] = useState<any>(null);
  const [offers, setOffers] = useState<any[]>([]);
  const [saleRequest, setSaleRequest] = useState<any>(null);
  
  const [loading, setLoading] = useState(true);
  
  // Interactive Chat State
  const [assistantQuery, setAssistantQuery] = useState('');
  const [assistantLoading, setAssistantLoading] = useState(false);
  const [messages, setMessages] = useState<ChatMessage[]>([]);
  const chatBottomRef = useRef<HTMLDivElement>(null);

  // Warranty state
  const [warrantyMonths, setWarrantyMonths] = useState(12);

  const fetchAllData = async () => {
    const token = localStorage.getItem('token');
    if (!token) { navigate('/login'); return; }
    const headers = { 'Authorization': `Bearer ${token}` };

    try {
      const [devRes, valRes, warRes, offRes, saleRes] = await Promise.all([
        fetch(`/api/v1/user-devices/${id}`, { headers }),
        fetch(`/api/v1/user-devices/${id}/valuation`, { headers }),
        fetch(`/api/v1/user-devices/${id}/warranty`, { headers }),
        fetch(`/api/v1/user-devices/${id}/offers`, { headers }),
        fetch(`/api/v1/user-devices/${id}/sale`, { headers })
      ]);

      if (devRes.ok) {
        const d = await devRes.json();
        setDevice(d);
        // Initialize welcome message for this device
        if (messages.length === 0) {
          const modelTitle = `${d.variant?.modelName || d.variant?.storage + ' ' + d.variant?.color}`;
          setMessages([
            {
              id: 'init-msg',
              sender: 'assistant',
              text: `👋 **Hi there! I'm your NOVUS Smart AI Assistant.**\n\nI have loaded all technical, financial, and condition diagnostics for your **${modelTitle}**.\n\nAsk me anything! For example: *"What is my phone worth?"*, *"Should I sell now?"*, *"What are the full specs?"*, or click one of the suggested prompts below.`,
              timestamp: new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })
            }
          ]);
        }
      }
      if (valRes.ok) setValuation(await valRes.json());
      if (warRes.ok) setWarranty(await warRes.json());
      if (offRes.ok) setOffers(await offRes.json());
      if (saleRes.ok) setSaleRequest(await saleRes.json());
    } catch (e) {
      console.error('Error fetching device data', e);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchAllData();
  }, [id]);

  useEffect(() => {
    chatBottomRef.current?.scrollIntoView({ behavior: 'smooth' });
  }, [messages, assistantLoading]);

  const handleAddWarranty = async () => {
    const headers = { 'Authorization': `Bearer ${localStorage.getItem('token')}`, 'Content-Type': 'application/json' };
    const res = await fetch(`/api/v1/user-devices/${id}/warranty`, {
      method: 'POST', headers, body: JSON.stringify({ warrantyMonths })
    });
    if (res.ok) fetchAllData();
    else alert('Failed to add warranty');
  };

  const handleAcceptOffer = async (offerId: number) => {
    const headers = { 'Authorization': `Bearer ${localStorage.getItem('token')}` };
    const res = await fetch(`/api/v1/user-devices/${id}/sale?offerId=${offerId}`, {
      method: 'POST', headers
    });
    if (res.ok) fetchAllData();
    else {
      const err = await res.json().catch(() => ({}));
      alert(err.error || 'Failed to accept offer');
    }
  };

  const handleAdvanceSale = async (status: string) => {
    const headers = { 'Authorization': `Bearer ${localStorage.getItem('token')}` };
    const res = await fetch(`/api/v1/user-devices/${id}/sale?status=${status}`, {
      method: 'PUT', headers
    });
    if (res.ok) fetchAllData();
    else alert('Failed to update status');
  };

  const sendQuery = async (queryText: string) => {
    if (!queryText.trim() || assistantLoading) return;
    const userMsg: ChatMessage = {
      id: String(Date.now()),
      sender: 'user',
      text: queryText.trim(),
      timestamp: new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })
    };
    setMessages(prev => [...prev, userMsg]);
    setAssistantQuery('');
    setAssistantLoading(true);

    const headers = { 'Authorization': `Bearer ${localStorage.getItem('token')}`, 'Content-Type': 'application/json' };
    try {
      const res = await fetch(`/api/v1/user-devices/${id}/assistant/query`, {
        method: 'POST', headers, body: JSON.stringify({ query: queryText })
      });
      if (res.ok) {
        const data = await res.json();
        const aiMsg: ChatMessage = {
          id: String(Date.now() + 1),
          sender: 'assistant',
          text: data.reply,
          timestamp: new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })
        };
        setMessages(prev => [...prev, aiMsg]);
      } else {
        setMessages(prev => [...prev, {
          id: String(Date.now() + 1),
          sender: 'assistant',
          text: '⚠️ I encountered an error retrieving data. Please try again.',
          timestamp: new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })
        }]);
      }
    } catch (e) {
      setMessages(prev => [...prev, {
        id: String(Date.now() + 1),
        sender: 'assistant',
        text: '⚠️ Network connection error. Unable to reach assistant server.',
        timestamp: new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })
      }]);
    } finally {
      setAssistantLoading(false);
    }
  };

  const handleChatSubmit = (e: FormEvent) => {
    e.preventDefault();
    sendQuery(assistantQuery);
  };

  const quickPrompts = [
    "💰 What is my phone's current value?",
    "📈 Should I sell now?",
    "📱 Technical specifications",
    "🛡️ Warranty status & claims",
    "🔋 How is my battery health?",
    "🔒 How to prepare for sale?"
  ];

  if (loading) return <div className="text-center p-10 text-gray-400">Loading device details...</div>;
  if (!device) return <div className="text-center p-10 text-red-400">Device not found.</div>;

  return (
    <div className="max-w-5xl mx-auto space-y-6 pb-12">
      
      {/* Device Header */}
      <div className="bg-gray-800 p-6 rounded-xl border border-gray-700 shadow flex justify-between items-center relative">
        <div>
          <span className="text-xs font-semibold bg-indigo-500/20 text-indigo-400 px-2.5 py-1 rounded-full uppercase tracking-wider">
            {device.variant?.ram} RAM • {device.variant?.storage}
          </span>
          <h2 className="text-3xl font-bold mt-2 mb-1 text-white">
            {device.variant?.color} {device.variant?.storage}
          </h2>
          <p className="text-gray-400 text-sm">
            Purchased on <span className="text-gray-200 font-medium">{device.purchaseDate || 'N/A'}</span>
            {device.purchasePrice && <> for <span className="text-gray-200 font-medium">₹{device.purchasePrice}</span></>}
          </p>
        </div>
        <button 
          onClick={async () => {
            if (window.confirm("Are you sure you want to delete this device? This action cannot be undone.")) {
              const res = await fetch(`/api/v1/user-devices/${id}`, {
                method: 'DELETE',
                headers: { 'Authorization': `Bearer ${localStorage.getItem('token')}` }
              });
              if (res.ok) navigate('/');
              else alert('Failed to delete device');
            }
          }}
          className="bg-red-600/20 hover:bg-red-600 text-red-400 hover:text-white px-3.5 py-2 rounded-lg text-sm font-semibold transition-all border border-red-500/30 shadow-sm"
        >
          🗑️ Delete Device
        </button>
      </div>

      {/* Main Grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        
        {/* Valuation & Condition */}
        <div className="bg-gray-800 p-6 rounded-xl border border-gray-700 shadow flex flex-col justify-between">
          <div>
            <div className="flex justify-between items-center mb-4">
              <h3 className="text-xl font-bold text-white flex items-center gap-2">
                📊 Valuation Engine
              </h3>
              {valuation && (
                <span className="text-xs bg-green-500/20 text-green-400 font-semibold px-2 py-0.5 rounded">
                  {valuation.confidenceScore}% Confidence
                </span>
              )}
            </div>
            {valuation ? (
              <div>
                <p className="text-4xl font-extrabold text-green-400 mb-1">
                  ₹{Number(valuation.estimatedValue).toLocaleString('en-IN', { maximumFractionDigits: 2 })}
                </p>
                <p className="text-gray-400 text-sm font-medium mb-4">
                  Fair Market Range: ₹{Number(valuation.lowerRange).toLocaleString('en-IN', { maximumFractionDigits: 0 })} — ₹{Number(valuation.upperRange).toLocaleString('en-IN', { maximumFractionDigits: 0 })}
                </p>
                <div className="p-3.5 bg-gray-900 rounded-lg border border-gray-700 text-sm text-gray-300 leading-relaxed whitespace-pre-wrap">
                  {valuation.explanation}
                </div>
              </div>
            ) : (
              <div>
                <p className="text-gray-400 mb-4 text-sm">No valuation available yet. Condition assessment required to determine fair market value.</p>
                <Link to={`/devices/${id}/condition`} className="inline-block bg-indigo-600 hover:bg-indigo-700 text-white py-2.5 px-4 rounded-lg font-medium transition shadow-md">
                  Start Condition Assessment
                </Link>
              </div>
            )}
          </div>
          {valuation && (
            <div className="mt-4 pt-3 border-t border-gray-700 flex justify-between text-xs text-gray-400">
              <span>Age: {valuation.ageMonths} mos</span>
              <span>Condition: {valuation.conditionScore}/100</span>
              <Link to={`/devices/${id}/condition`} className="text-indigo-400 hover:underline">Re-assess</Link>
            </div>
          )}
        </div>

        {/* Warranty Section */}
        <div className="bg-gray-800 p-6 rounded-xl border border-gray-700 shadow flex flex-col justify-between">
          <div>
            <h3 className="text-xl font-bold mb-4 text-white flex items-center gap-2">
              🛡️ Warranty Tracking
            </h3>
            {warranty ? (
              <div className="space-y-3">
                <div className="flex justify-between items-center p-3 bg-gray-900 rounded-lg border border-gray-700">
                  <span className="text-gray-400 text-sm">Status</span>
                  <span className={`px-2.5 py-1 rounded-full text-xs font-bold ${warranty.isActive ? 'bg-green-500/20 text-green-400 border border-green-500/30' : 'bg-red-500/20 text-red-400 border border-red-500/30'}`}>
                    {warranty.isActive ? 'ACTIVE' : 'EXPIRED'}
                  </span>
                </div>
                <div className="flex justify-between text-sm py-1 border-b border-gray-700/60">
                  <span className="text-gray-400">Coverage Duration:</span>
                  <span className="font-medium text-gray-200">{warranty.warrantyMonths} Months</span>
                </div>
                <div className="flex justify-between text-sm py-1 border-b border-gray-700/60">
                  <span className="text-gray-400">Expires On:</span>
                  <span className="font-medium text-gray-200">{warranty.expiresAt}</span>
                </div>
                <div className="flex justify-between text-sm py-1">
                  <span className="text-gray-400">Months Remaining:</span>
                  <span className="font-medium text-indigo-400">{warranty.monthsRemaining} months</span>
                </div>
              </div>
            ) : (
              <div>
                <p className="text-gray-400 text-sm mb-4">No warranty linked. Adding active warranty documentation significantly boosts your device's market quote.</p>
                <div className="flex items-center gap-2">
                  <input 
                    type="number" 
                    value={warrantyMonths} 
                    onChange={e => setWarrantyMonths(Number(e.target.value))}
                    className="bg-gray-700 border border-gray-600 rounded-lg px-3 py-2 w-24 text-white text-sm focus:outline-none focus:border-indigo-500"
                    min="1"
                    max="60"
                  />
                  <span className="text-gray-400 text-sm">months</span>
                  <button onClick={handleAddWarranty} className="bg-indigo-600 hover:bg-indigo-700 px-4 py-2 rounded-lg text-white font-medium text-sm transition">
                    Save Warranty
                  </button>
                </div>
              </div>
            )}
          </div>
          <div className="mt-4 pt-3 border-t border-gray-700 text-xs text-gray-400">
            Apple Limited Warranty covers manufacturer defects for 1 year from activation.
          </div>
        </div>

      </div>

      {/* Local Offers & Sell Workflow */}
      <div className="bg-gray-800 p-6 rounded-xl border border-gray-700 shadow">
        <h3 className="text-xl font-bold mb-4 text-white flex items-center gap-2">
          🏪 Local Offers & Instant Liquidation
        </h3>
        
        {saleRequest ? (
          <div className="bg-gray-900 p-5 rounded-xl border border-indigo-500/30">
            <div className="flex items-center justify-between mb-3">
              <h4 className="font-bold text-indigo-400 text-base">Active Sale Transaction</h4>
              <span className="text-xs bg-indigo-500/20 text-indigo-300 font-mono px-2.5 py-1 rounded border border-indigo-500/40">
                {saleRequest.status}
              </span>
            </div>
            <p className="text-sm text-gray-300 mb-4">
              Your sale request is registered in the state machine. Step through each verification milestone:
            </p>
            <div className="flex flex-wrap gap-2">
              {saleRequest.status === 'DRAFT' && <button onClick={() => handleAdvanceSale('SUBMITTED')} className="bg-blue-600 hover:bg-blue-700 text-xs px-3.5 py-2 rounded-lg font-medium transition">Submit for Review ➔</button>}
              {saleRequest.status === 'SUBMITTED' && <button onClick={() => handleAdvanceSale('VERIFICATION_PENDING')} className="bg-yellow-600 hover:bg-yellow-700 text-xs px-3.5 py-2 rounded-lg font-medium transition">Schedule Verification Inspection ➔</button>}
              {saleRequest.status === 'VERIFICATION_PENDING' && <button onClick={() => handleAdvanceSale('VERIFIED')} className="bg-purple-600 hover:bg-purple-700 text-xs px-3.5 py-2 rounded-lg font-medium transition">Complete Diagnostic Pass ➔</button>}
              {saleRequest.status === 'VERIFIED' && <button onClick={() => handleAdvanceSale('PAYMENT_PENDING')} className="bg-orange-600 hover:bg-orange-700 text-xs px-3.5 py-2 rounded-lg font-medium transition">Request Merchant Payment ➔</button>}
              {saleRequest.status === 'PAYMENT_PENDING' && <button onClick={() => handleAdvanceSale('COMPLETED')} className="bg-green-600 hover:bg-green-700 text-xs px-3.5 py-2 rounded-lg font-medium transition">Confirm Payout Received ✅</button>}
              {saleRequest.status === 'COMPLETED' && <span className="text-sm text-green-400 font-semibold">🎉 Sale completed successfully! Funds transferred.</span>}
            </div>
          </div>
        ) : valuation ? (
          <div>
            {offers.length > 0 ? (
              <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-3">
                {offers.map(offer => (
                  <div key={offer.id} className="bg-gray-900 p-4 rounded-xl border border-gray-700 flex flex-col justify-between hover:border-gray-600 transition">
                    <div>
                      <div className="flex justify-between items-baseline mb-1">
                        <p className="font-extrabold text-xl text-green-400">₹{Number(offer.price).toLocaleString('en-IN')}</p>
                        <span className="text-xs bg-gray-800 text-gray-300 px-2 py-0.5 rounded">{offer.distance} km</span>
                      </div>
                      <p className="font-medium text-gray-200 text-sm">{offer.shopName}</p>
                      <p className="text-xs text-gray-400 mt-1">Payment: {offer.paymentMethod}</p>
                    </div>
                    <button onClick={() => handleAcceptOffer(offer.id)} className="mt-3 w-full bg-indigo-600 hover:bg-indigo-500 text-xs py-2 rounded-lg font-semibold transition">
                      Accept & Sell
                    </button>
                  </div>
                ))}
              </div>
            ) : (
              <p className="text-gray-400 text-sm">No verified local merchant bids active for this specific variant.</p>
            )}
          </div>
        ) : (
          <p className="text-gray-500 italic text-sm">Complete a condition assessment and valuation to unlock real-time merchant bids.</p>
        )}
      </div>

      {/* FULL-WIDTH INTERACTIVE AI CHATBOT (ChatGPT-like) */}
      <div className="bg-gray-800 rounded-2xl border border-gray-700 shadow-2xl overflow-hidden flex flex-col h-[600px]">
        
        {/* Chat Header */}
        <div className="bg-gray-850 px-6 py-4 border-b border-gray-700 flex items-center justify-between">
          <div className="flex items-center gap-3">
            <div className="w-10 h-10 rounded-xl bg-gradient-to-tr from-indigo-600 to-purple-600 flex items-center justify-center text-white text-lg shadow-md">
              ✨
            </div>
            <div>
              <h3 className="font-bold text-white text-lg leading-tight flex items-center gap-2">
                NOVUS Smart AI Assistant
                <span className="bg-green-500/20 text-green-400 text-xs font-semibold px-2 py-0.5 rounded-full border border-green-500/30">
                  Online
                </span>
              </h3>
              <p className="text-xs text-gray-400">Contextual intelligence engine for {device.variant?.storage} {device.variant?.color}</p>
            </div>
          </div>
          <button 
            onClick={() => setMessages([])} 
            className="text-xs text-gray-400 hover:text-gray-200 transition bg-gray-700/50 hover:bg-gray-700 px-3 py-1.5 rounded-lg border border-gray-600/50"
          >
            Clear Chat
          </button>
        </div>

        {/* Chat Messages Feed */}
        <div className="flex-1 p-6 overflow-y-auto space-y-4 bg-gray-900/60">
          {messages.map(msg => (
            <div key={msg.id} className={`flex gap-3 ${msg.sender === 'user' ? 'justify-end' : 'justify-start'}`}>
              {msg.sender === 'assistant' && (
                <div className="w-8 h-8 rounded-lg bg-indigo-600/30 border border-indigo-500/30 flex items-center justify-center text-indigo-300 text-sm flex-shrink-0 mt-1">
                  🤖
                </div>
              )}
              <div className={`max-w-[80%] rounded-2xl px-5 py-3.5 shadow-sm text-sm ${
                msg.sender === 'user' 
                  ? 'bg-indigo-600 text-white rounded-tr-none' 
                  : 'bg-gray-800 border border-gray-700 text-gray-200 rounded-tl-none'
              }`}>
                <div className="whitespace-pre-wrap leading-relaxed">
                  {msg.text}
                </div>
                <div className={`text-[10px] mt-1.5 ${msg.sender === 'user' ? 'text-indigo-200 text-right' : 'text-gray-400'}`}>
                  {msg.timestamp}
                </div>
              </div>
              {msg.sender === 'user' && (
                <div className="w-8 h-8 rounded-lg bg-gray-700 flex items-center justify-center text-gray-300 text-xs font-bold flex-shrink-0 mt-1">
                  YOU
                </div>
              )}
            </div>
          ))}

          {assistantLoading && (
            <div className="flex gap-3 justify-start items-center">
              <div className="w-8 h-8 rounded-lg bg-indigo-600/30 border border-indigo-500/30 flex items-center justify-center text-indigo-300 text-sm flex-shrink-0">
                🤖
              </div>
              <div className="bg-gray-800 border border-gray-700 rounded-2xl px-4 py-3 rounded-tl-none flex items-center gap-1.5 text-xs text-gray-400">
                <span className="w-2 h-2 rounded-full bg-indigo-400 animate-pulse"></span>
                <span className="w-2 h-2 rounded-full bg-indigo-400 animate-pulse delay-75"></span>
                <span className="w-2 h-2 rounded-full bg-indigo-400 animate-pulse delay-150"></span>
                <span className="ml-1">AI Assistant is analyzing device diagnostics...</span>
              </div>
            </div>
          )}
          <div ref={chatBottomRef} />
        </div>

        {/* Quick Prompts Bar */}
        <div className="px-6 py-2.5 bg-gray-850 border-t border-gray-800 flex gap-2 overflow-x-auto no-scrollbar">
          {quickPrompts.map((p, i) => (
            <button
              key={i}
              onClick={() => sendQuery(p)}
              disabled={assistantLoading}
              className="text-xs bg-gray-700/60 hover:bg-indigo-600/30 text-gray-300 hover:text-indigo-300 border border-gray-600/60 hover:border-indigo-500/50 px-3 py-1.5 rounded-full whitespace-nowrap transition-all flex-shrink-0 disabled:opacity-40"
            >
              {p}
            </button>
          ))}
        </div>

        {/* Chat Input Bar */}
        <div className="p-4 bg-gray-800 border-t border-gray-700">
          <form onSubmit={handleChatSubmit} className="flex gap-2">
            <input 
              type="text" 
              value={assistantQuery} 
              onChange={e => setAssistantQuery(e.target.value)}
              placeholder="Ask anything about selling, value, warranty, specs, battery, or trade-in..."
              className="flex-1 bg-gray-900 border border-gray-700 rounded-xl px-4 py-3 text-white text-sm focus:outline-none focus:ring-2 focus:ring-indigo-500 transition-all placeholder:text-gray-500"
              disabled={assistantLoading}
            />
            <button 
              type="submit" 
              disabled={assistantLoading || !assistantQuery.trim()} 
              className="bg-gradient-to-r from-indigo-600 to-purple-600 hover:from-indigo-500 hover:to-purple-500 text-white px-5 py-3 rounded-xl font-semibold text-sm transition-all shadow-md disabled:opacity-40 flex items-center gap-1.5"
            >
              Send ➔
            </button>
          </form>
        </div>

      </div>

    </div>
  );
}
