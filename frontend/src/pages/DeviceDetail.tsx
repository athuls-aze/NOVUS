import { useEffect, useState, useRef, FormEvent } from 'react';
import { useParams, Link, useNavigate } from 'react-router-dom';
import { Trash2, Sparkles, Shield, BarChart3, Store, Send } from 'lucide-react';

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
  
  const [warrantyMonths, setWarrantyMonths] = useState(12);
  const [messages, setMessages] = useState<ChatMessage[]>([]);
  const [assistantQuery, setAssistantQuery] = useState('');
  const [assistantLoading, setAssistantLoading] = useState(false);
  const chatBottomRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    const token = localStorage.getItem('token');
    if (!token) { navigate('/login'); return; }
    
    const headers = { 'Authorization': `Bearer ${token}` };
    
    const fetchAllData = async () => {
      try {
        const [devRes, valRes, warRes, offRes, saleRes] = await Promise.all([
          fetch(`/api/v1/user-devices/${id}`, { headers }),
          fetch(`/api/v1/user-devices/${id}/valuation`, { headers }),
          fetch(`/api/v1/user-devices/${id}/warranty`, { headers }),
          fetch(`/api/v1/user-devices/${id}/offers`, { headers }),
          fetch(`/api/v1/user-devices/${id}/sale`, { headers })
        ]);
        
        if (devRes.status === 401 || devRes.status === 403) { navigate('/login'); return; }
        if (devRes.ok) setDevice(await devRes.json());
        if (valRes.ok) setValuation(await valRes.json());
        if (warRes.ok) setWarranty(await warRes.json());
        if (offRes.ok) {
          const offData = await offRes.json();
          // Backend returns a plain List<OfferDTO>, not a paginated Page
          setOffers(Array.isArray(offData) ? offData : []);
        }
        if (saleRes.ok) {
          // Backend returns a single SaleRequestDTO (not a Page)
          const saleData = await saleRes.json();
          setSaleRequest(saleData || null);
        }
      } catch (e) {
        console.error('Error fetching device data', e);
      } finally {
        setLoading(false);
      }
    };
    
    fetchAllData();
  }, [id, navigate]);

  useEffect(() => {
    chatBottomRef.current?.scrollIntoView({ behavior: 'smooth' });
  }, [messages, assistantLoading]);

  const handleAddWarranty = async () => {
    const token = localStorage.getItem('token');
    const res = await fetch(`/api/v1/user-devices/${id}/warranty`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json', 'Authorization': `Bearer ${token}` },
      body: JSON.stringify({ warrantyMonths })
    });
    if (res.ok) window.location.reload();
  };

  const handleAcceptOffer = (offerId: number) => {
    navigate(`/devices/${id}/sell?offerId=${offerId}`);
  };

  const handleAdvanceSale = async (status: string) => {
    const token = localStorage.getItem('token');
    const res = await fetch(`/api/v1/user-devices/${id}/sale?status=${status}`, {
      method: 'PUT',
      headers: { 'Authorization': `Bearer ${token}` }
    });
    if (res.ok) window.location.reload();
  };

  const sendQuery = async (queryText: string) => {
    if (!queryText.trim()) return;
    
    const userMsg: ChatMessage = {
      id: String(Date.now()), sender: 'user', text: queryText,
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
        setMessages(prev => [...prev, {
          id: String(Date.now() + 1), sender: 'assistant', text: data.reply,
          timestamp: new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })
        }]);
      } else {
        setMessages(prev => [...prev, { id: String(Date.now() + 1), sender: 'assistant', text: 'Error retrieving data.', timestamp: new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }) }]);
      }
    } catch (e) {
      setMessages(prev => [...prev, { id: String(Date.now() + 1), sender: 'assistant', text: 'Network connection error.', timestamp: new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }) }]);
    } finally {
      setAssistantLoading(false);
    }
  };

  const handleChatSubmit = (e: FormEvent) => {
    e.preventDefault();
    sendQuery(assistantQuery);
  };

  const quickPrompts = [
    "What is my phone's current value?",
    "Should I sell now?",
    "What are the full specs?"
  ];

  if (loading) return <div className="text-center p-10 text-apple-secondary">Loading device details...</div>;
  if (!device) return <div className="text-center p-10 text-red-400">Device not found.</div>;

  return (
    <div className="max-w-7xl mx-auto flex flex-col lg:flex-row gap-6 pb-12 items-start animate-fade-in-up">
      {/* Left Column: Device Info & Selling Features */}
      <div className="flex-1 space-y-6 w-full">
        
        {/* Device Header */}
        <div className="soft-card p-8 flex justify-between items-start animate-fade-in-up delay-100">
          <div>
            <span className="text-xs font-semibold text-apple-secondary uppercase tracking-widest flex items-center gap-1.5">
              {device.variant?.ram} RAM • {device.variant?.storage}
            </span>
            <h2 className="text-4xl font-extrabold mt-1 mb-1 text-apple-primary tracking-tight">
              {device.variant?.modelName} {device.variant?.color} {device.variant?.storage}
            </h2>
            <p className="text-apple-secondary text-sm mt-2 font-medium">
              Purchased on <span className="text-apple-primary">{device.purchaseDate || 'N/A'}</span>
              {device.purchasePrice && <> for <span className="text-apple-primary">₹{device.purchasePrice}</span></>}
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
            className="flex items-center gap-2 text-apple-secondary hover:text-red-500 hover:bg-red-50 px-4 py-2 rounded-lg text-sm font-medium transition-colors"
          >
            <Trash2 size={16} strokeWidth={2} />
            Delete Device
          </button>
        </div>

        {/* Main Grid */}
        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
          
          {/* Valuation & Condition */}
          <div className="soft-card p-8 flex flex-col justify-between animate-fade-in-up delay-200">
            <div>
              <div className="flex justify-between items-center mb-6">
                <h3 className="text-xl font-bold text-apple-primary flex items-center gap-2">
                  <BarChart3 className="text-apple-blue" size={20} />
                  Valuation Engine
                </h3>
                {valuation && (
                  <div className="relative flex items-center justify-center w-14 h-14">
                    <svg className="w-full h-full transform -rotate-90">
                      <circle cx="28" cy="28" r="26" stroke="currentColor" strokeWidth="3" fill="transparent" className="text-slate-100" />
                      <circle cx="28" cy="28" r="26" stroke="currentColor" strokeWidth="3" fill="transparent" strokeDasharray="163" strokeDashoffset={163 - (163 * valuation.confidenceScore) / 100} className="text-apple-green transition-all duration-1000 ease-out" />
                    </svg>
                    <div className="absolute flex flex-col items-center">
                      <span className="text-xs font-bold text-apple-green">{valuation.confidenceScore}%</span>
                      <span className="text-[8px] uppercase tracking-wider text-apple-green/70">Confidence</span>
                    </div>
                  </div>
                )}
              </div>
              {valuation ? (
                <div>
                  <p className="text-4xl font-extrabold text-apple-primary mb-1 tracking-tight">
                    ₹{Number(valuation.estimatedValue).toLocaleString('en-IN', { maximumFractionDigits: 2 })}
                  </p>
                  <p className="text-apple-secondary text-sm font-medium mb-5">
                    Fair Market Range: ₹{Number(valuation.lowerRange).toLocaleString('en-IN', { maximumFractionDigits: 0 })} — ₹{Number(valuation.upperRange).toLocaleString('en-IN', { maximumFractionDigits: 0 })}
                  </p>
                  <div className="p-4 bg-apple-bg rounded-xl border border-apple-border text-sm text-apple-secondary leading-relaxed whitespace-pre-wrap">
                    {valuation.explanation}
                  </div>
                </div>
              ) : (
                <div>
                  <p className="text-apple-secondary mb-4 text-sm leading-relaxed">No valuation available yet. Condition assessment required to determine fair market value.</p>
                  <Link to={`/devices/${id}/condition`} className="inline-block bg-slate-900 hover:bg-slate-800 text-white py-2.5 px-5 rounded-xl font-medium transition shadow-sm">
                    Start Condition Assessment
                  </Link>
                </div>
              )}
            </div>
            {valuation && (
              <div className="mt-5 pt-4 border-t border-apple-border flex justify-between text-xs font-medium text-apple-secondary">
                <span className="flex flex-col items-center gap-1">Age: <span className="text-apple-primary">{valuation.ageMonths} mos</span></span>
                <span className="flex flex-col items-center gap-1">Condition: <span className="text-apple-primary">{valuation.conditionScore}/100</span></span>
                <Link to={`/devices/${id}/condition`} className="text-apple-blue hover:text-indigo-700 hover:underline flex flex-col items-center justify-end">Re-assess</Link>
              </div>
            )}
          </div>

          {/* Warranty Section */}
          <div className="soft-card p-8 flex flex-col justify-between animate-fade-in-up delay-300">
            <div>
              <h3 className="text-xl font-bold mb-6 text-apple-primary flex items-center gap-2">
                <Shield className="text-apple-blue" size={20} />
                Warranty Tracking
              </h3>
              {warranty ? (
                <div className="space-y-4">
                  <div className="flex justify-between items-center p-4 bg-apple-bg rounded-xl border border-apple-border">
                    <span className="text-apple-secondary text-sm font-medium">Status</span>
                    <span className={`flex items-center gap-1.5 px-3 py-1 rounded-full text-xs font-bold ${warranty.status === 'ACTIVE' ? 'text-emerald-700 bg-emerald-50' : 'text-rose-600 bg-rose-50'}`}>
                      <span className={`w-1.5 h-1.5 rounded-full ${warranty.status === 'ACTIVE' ? 'bg-emerald-500' : 'bg-rose-500'}`}></span>
                      {warranty.status}
                    </span>
                  </div>
                  <div className="flex justify-between text-sm py-2 border-b border-apple-border">
                    <span className="text-apple-secondary">Coverage Duration:</span>
                    <span className="font-semibold text-apple-primary">{warranty.warrantyMonths} Months</span>
                  </div>
                  <div className="flex justify-between text-sm py-2 border-b border-apple-border">
                    <span className="text-apple-secondary">Expires On:</span>
                    <span className="font-semibold text-apple-primary">{warranty.expiresAt}</span>
                  </div>
                  <div className="flex justify-between text-sm py-2">
                    <span className="text-apple-secondary">Days Remaining:</span>
                    <span className="font-semibold text-apple-blue">{warranty.daysRemaining} days</span>
                  </div>
                </div>
              ) : (
                <div>
                  <p className="text-apple-secondary text-sm mb-5 leading-relaxed">No warranty linked. Adding active warranty documentation significantly boosts your device's market quote.</p>
                  <div className="flex items-center gap-3">
                    <input 
                      type="number" 
                      value={warrantyMonths} 
                      onChange={e => setWarrantyMonths(Number(e.target.value))}
                      className="bg-apple-surface border border-apple-border rounded-xl px-4 py-2.5 w-24 text-apple-primary text-sm font-medium focus:outline-none focus:border-indigo-500 focus:ring-1 focus:ring-indigo-500 shadow-sm"
                      min="1" max="60"
                    />
                    <span className="text-apple-secondary text-sm font-medium">months</span>
                    <button onClick={handleAddWarranty} className="bg-slate-900 hover:bg-slate-800 px-5 py-2.5 rounded-xl text-white font-medium text-sm transition shadow-sm">
                      Save Warranty
                    </button>
                  </div>
                </div>
              )}
            </div>
            <div className="mt-5 pt-4 border-t border-apple-border text-xs text-apple-secondary font-medium leading-relaxed">
              Apple Limited Warranty covers manufacturer defects for 1 year from activation.
            </div>
          </div>
        </div>

        {/* Local Offers & Sell Workflow */}
        <div className="soft-card p-8 animate-fade-in-up delay-400">
          <h3 className="text-xl font-bold mb-6 text-apple-primary flex items-center gap-2">
            <Store className="text-apple-blue" size={20} />
            Local Offers & Instant Liquidation
          </h3>
          
          {saleRequest ? (
            <div className="bg-apple-bg p-6 rounded-2xl border border-indigo-100">
              <div className="flex items-center justify-between mb-4">
                <h4 className="font-bold text-indigo-700">Active Sale Transaction</h4>
                <span className="text-xs bg-indigo-100 text-indigo-800 font-bold px-3 py-1 rounded-full tracking-wide">
                  {saleRequest.status}
                </span>
              </div>
              <p className="text-sm text-apple-secondary mb-5 leading-relaxed">Your device is currently locked in a sale transaction. Deliver the device to the merchant to finalize payment.</p>
              <div className="flex gap-3">
                {saleRequest.status === 'SUBMITTED' && <button onClick={() => handleAdvanceSale('PAYMENT_PENDING')} className="bg-apple-blue hover:opacity-90 text-white text-sm px-5 py-2.5 rounded-xl font-medium transition shadow-sm">Mark Dropped Off</button>}
                {saleRequest.status === 'PAYMENT_PENDING' && <button onClick={() => handleAdvanceSale('COMPLETED')} className="bg-apple-green hover:bg-emerald-700 text-white text-sm px-5 py-2.5 rounded-xl font-medium transition shadow-sm">Confirm Payout Received</button>}
                {saleRequest.status === 'COMPLETED' && <span className="text-sm text-apple-green font-bold flex items-center gap-1.5"><Sparkles size={16} /> Sale completed successfully! Funds transferred.</span>}
              </div>
            </div>
          ) : valuation ? (
            <div>
              {offers.length > 0 ? (
                <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
                  {offers.map((offer: any) => (
                    <div key={offer.id} className="bg-apple-surface p-5 rounded-2xl border border-apple-border shadow-sm flex flex-col justify-between hover:shadow-md transition-shadow">
                      <div>
                        <div className="flex justify-between items-center mb-3">
                           <div className="flex items-center gap-2">
                             <div className="w-6 h-6 bg-slate-900 rounded flex items-center justify-center text-white text-xs font-bold">{offer.shopName.charAt(0)}</div>
                             <p className="font-bold text-apple-primary text-sm tracking-tight">{offer.shopName}</p>
                           </div>
                           <span className="text-xs bg-apple-border text-apple-secondary font-medium px-2 py-0.5 rounded-md">{offer.distance} km</span>
                        </div>
                        <p className="font-extrabold text-2xl text-apple-primary tracking-tight mb-2">₹{Number(offer.price).toLocaleString('en-IN')}</p>
                        <p className="text-xs text-apple-secondary font-medium">Payment: {offer.paymentMethod}</p>
                      </div>
                      <button onClick={() => handleAcceptOffer(offer.id)} className="mt-4 w-full bg-apple-border hover:bg-slate-200 text-apple-primary text-sm py-2.5 rounded-xl font-semibold transition">
                        Accept & Sell
                      </button>
                    </div>
                  ))}
                </div>
              ) : (
                <p className="text-apple-secondary text-sm">No verified local merchant bids active for this specific variant.</p>
              )}
            </div>
          ) : (
            <p className="text-apple-secondary italic text-sm">Complete a condition assessment and valuation to unlock real-time merchant bids.</p>
          )}
        </div>
      </div>

      {/* Right Column: INTERACTIVE AI CHATBOT */}
      <div className="w-full lg:w-[420px] flex-shrink-0 sticky top-6 animate-fade-in-up delay-200">
        <div className="glass-panel overflow-hidden flex flex-col h-[750px]">
          
          {/* Chat Header */}
          <div className="px-6 py-5 border-b border-apple-border/50 flex items-center justify-between bg-apple-surface/50">
            <div className="flex items-center gap-3">
              <div className="w-10 h-10 rounded-2xl bg-gradient-to-tr from-indigo-100 to-sky-100 flex items-center justify-center text-apple-blue shadow-sm">
                <Sparkles size={20} strokeWidth={2.5} />
              </div>
              <div>
                <h3 className="font-bold text-apple-primary text-base flex items-center gap-2 tracking-tight">
                  NOVUS Smart AI
                  <span className="text-apple-green text-xs font-semibold px-2 py-0.5 rounded-full bg-emerald-50 border border-emerald-100">
                    Online
                  </span>
                </h3>
                <p className="text-[11px] text-apple-secondary font-medium">Contextual intelligence engine</p>
              </div>
            </div>
            <button 
              onClick={() => setMessages([])} 
              className="text-[11px] font-semibold text-apple-secondary hover:text-apple-secondary transition px-2 py-1"
            >
              Clear Chat
            </button>
          </div>

          {/* Chat Messages Feed */}
          <div className="flex-1 p-5 overflow-y-auto space-y-4 no-scrollbar bg-apple-bg/30">
            {messages.map(msg => (
              <div key={msg.id} className={`flex gap-2 ${msg.sender === 'user' ? 'justify-end' : 'justify-start'}`}>
                {msg.sender === 'assistant' && (
                  <div className="w-7 h-7 rounded-full bg-apple-surface border border-apple-border shadow-sm flex items-center justify-center text-apple-blue flex-shrink-0 mt-1">
                    <Sparkles size={14} />
                  </div>
                )}
                <div className={`max-w-[85%] rounded-2xl px-4 py-3 shadow-sm text-sm font-medium leading-relaxed ${
                  msg.sender === 'user' 
                    ? 'bg-slate-900 text-white rounded-tr-sm' 
                    : 'bg-apple-surface border border-apple-border text-apple-secondary rounded-tl-sm'
                }`}>
                  <div className="whitespace-pre-wrap">
                    {msg.text}
                  </div>
                  <div className={`text-[9px] mt-2 font-semibold tracking-wider ${msg.sender === 'user' ? 'text-apple-secondary text-right' : 'text-apple-secondary'}`}>
                    {msg.timestamp}
                  </div>
                </div>
              </div>
            ))}

            {assistantLoading && (
              <div className="flex gap-2 justify-start items-start">
                <div className="w-7 h-7 rounded-full bg-apple-surface border border-apple-border shadow-sm flex items-center justify-center text-apple-blue flex-shrink-0 mt-1">
                  <Sparkles size={14} />
                </div>
                <div className="bg-apple-surface border border-apple-border shadow-sm rounded-2xl rounded-tl-sm px-4 py-3 flex items-center gap-1.5 text-xs text-apple-secondary font-medium">
                  <span className="w-1.5 h-1.5 rounded-full bg-indigo-400 animate-pulse"></span>
                  <span className="w-1.5 h-1.5 rounded-full bg-indigo-400 animate-pulse delay-75"></span>
                  <span className="w-1.5 h-1.5 rounded-full bg-indigo-400 animate-pulse delay-150"></span>
                </div>
              </div>
            )}
            <div ref={chatBottomRef} />
          </div>

          {/* Quick Prompts Bar */}
          {messages.length === 0 && (
            <div className="px-5 pb-2 pt-4 flex gap-2 overflow-x-auto no-scrollbar">
              {quickPrompts.map((p, i) => (
                <button
                  key={i}
                  onClick={() => sendQuery(p)}
                  disabled={assistantLoading}
                  className="text-[11px] font-semibold bg-apple-surface hover:bg-apple-bg text-apple-secondary border border-apple-border px-3.5 py-2 rounded-full whitespace-nowrap transition-all flex-shrink-0 disabled:opacity-50 shadow-sm"
                >
                  {p}
                </button>
              ))}
            </div>
          )}

          {/* Chat Input Bar */}
          <div className="p-4 bg-apple-surface/80 border-t border-apple-border/50 backdrop-blur-md">
            <form onSubmit={handleChatSubmit} className="flex gap-2 relative items-center">
              <input 
                type="text" 
                value={assistantQuery} 
                onChange={e => setAssistantQuery(e.target.value)}
                placeholder="Ask anything..."
                className="w-full bg-apple-border/80 border-none rounded-full pl-5 pr-12 py-3.5 text-apple-primary text-sm font-medium focus:outline-none focus:ring-2 focus:ring-indigo-100 transition-all placeholder:text-apple-secondary"
                disabled={assistantLoading}
              />
              <button 
                type="submit" 
                disabled={assistantLoading || !assistantQuery.trim()} 
                className="absolute right-1.5 w-9 h-9 flex items-center justify-center bg-apple-blue hover:opacity-90 text-white rounded-full transition-all disabled:opacity-40"
              >
                <Send size={16} className="ml-0.5" />
              </button>
            </form>
          </div>

        </div>
      </div>

    </div>
  );
}

