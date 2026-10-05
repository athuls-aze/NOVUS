import { useEffect, useState } from 'react';
import { useParams, useSearchParams, useNavigate } from 'react-router-dom';
import { ShieldCheck, Loader2, Sparkles, CheckCircle2 } from 'lucide-react';

export default function SellDevice() {
  const { id } = useParams();
  const [searchParams] = useSearchParams();
  const offerId = searchParams.get('offerId');
  const navigate = useNavigate();

  const [step, setStep] = useState(0);
  const [error, setError] = useState('');

  const steps = [
    "Locking in merchant offer...",
    "Verifying condition assessment...",
    "Generating secure transaction ID...",
    "Finalizing sale..."
  ];

  useEffect(() => {
    if (!offerId) {
      navigate(`/devices/${id}`);
      return;
    }

    let currentStep = 0;
    const interval = setInterval(() => {
      currentStep++;
      if (currentStep < steps.length) {
        setStep(currentStep);
      } else {
        clearInterval(interval);
        executeSale();
      }
    }, 1500);

    return () => clearInterval(interval);
  }, [id, offerId, navigate]);

  const executeSale = async () => {
    try {
      const token = localStorage.getItem('token');
      const res = await fetch(`/api/v1/user-devices/${id}/sale?offerId=${offerId}`, {
        method: 'POST',
        headers: { 'Authorization': `Bearer ${token}` }
      });
      if (res.ok) {
        setStep(steps.length); // Success state
      } else {
        setError('Failed to process the sale. The offer might have expired.');
      }
    } catch (e) {
      setError('Network error occurred during transaction.');
    }
  };

  if (error) {
    return (
      <div className="max-w-md mx-auto mt-20 text-center animate-fade-in-up">
        <div className="w-16 h-16 bg-rose-100 text-rose-600 rounded-full flex items-center justify-center mx-auto mb-6 shadow-sm">
          <ShieldCheck size={32} />
        </div>
        <h2 className="text-2xl font-bold text-apple-primary mb-2">Transaction Failed</h2>
        <p className="text-apple-secondary mb-8">{error}</p>
        <button onClick={() => navigate(`/devices/${id}`)} className="bg-apple-primary text-white px-6 py-3 rounded-full font-medium shadow-sm hover:opacity-90 transition">
          Return to Device
        </button>
      </div>
    );
  }

  if (step === steps.length) {
    return (
      <div className="max-w-md mx-auto mt-20 text-center animate-fade-in-up">
        <div className="w-20 h-20 bg-emerald-100 text-emerald-600 rounded-full flex items-center justify-center mx-auto mb-6 shadow-sm">
          <CheckCircle2 size={40} />
        </div>
        <h2 className="text-3xl font-extrabold text-apple-primary mb-3 tracking-tight">Offer Accepted!</h2>
        <p className="text-apple-secondary mb-8 leading-relaxed">Your device has been locked in for sale. Please drop it off at the selected merchant within 48 hours to receive your payout.</p>
        <button onClick={() => navigate(`/devices/${id}`)} className="bg-apple-blue text-white px-8 py-3 rounded-full font-medium shadow-apple-card hover:shadow-apple-card-hover transition-all">
          View Instructions
        </button>
      </div>
    );
  }

  return (
    <div className="max-w-md mx-auto mt-20 soft-card p-10 text-center animate-fade-in-up">
      <div className="relative w-24 h-24 mx-auto mb-8">
        <div className="absolute inset-0 bg-apple-blue/10 rounded-full animate-ping"></div>
        <div className="relative w-full h-full bg-apple-blue/20 text-apple-blue rounded-full flex items-center justify-center shadow-inner">
          <Loader2 size={40} className="animate-spin" />
        </div>
        <div className="absolute top-0 right-0 w-8 h-8 bg-apple-surface rounded-full flex items-center justify-center shadow-sm">
          <Sparkles size={16} className="text-emerald-500" />
        </div>
      </div>
      <h2 className="text-2xl font-bold text-apple-primary mb-3">Processing Transaction</h2>
      <p className="text-apple-secondary font-medium min-h-[1.5rem] animate-pulse">
        {steps[step]}
      </p>
      
      <div className="mt-8 flex gap-1.5 justify-center">
        {steps.map((_, i) => (
          <div key={i} className={`h-1.5 rounded-full transition-all duration-500 ${i <= step ? 'w-8 bg-apple-blue' : 'w-2 bg-apple-border'}`}></div>
        ))}
      </div>
    </div>
  );
}
