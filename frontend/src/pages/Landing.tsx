import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Shield, Eye, Scale, Lock, ChevronRight } from 'lucide-react';
import { useAuth } from '../lib/auth';

export function Landing() {
  const [rut, setRut] = useState('12345678-9');
  const [nombre, setNombre] = useState('Juan Pérez');
  const [loading, setLoading] = useState(false);
  const { login } = useAuth();
  const navigate = useNavigate();

  const handleLogin = async () => {
    setLoading(true);
    const success = await login(rut, nombre);
    if (success) {
      navigate('/dashboard');
    }
    setLoading(false);
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-slate-900 via-brand-blue to-blue-900 animate-hero-gradient">
      {/* Top bar */}
      <nav className="flex items-center justify-between px-6 py-4">
        <div className="flex items-center gap-2">
          <Shield className="w-7 h-7 text-white" />
          <span className="text-white font-bold text-lg">Data Custodian Hub</span>
        </div>
        <span className="text-blue-200 text-sm hidden sm:block">Ley 21.719 · Protección de Datos Personales</span>
      </nav>

      {/* Hero */}
      <div className="max-w-5xl mx-auto px-6 pt-12 md:pt-24 pb-16">
        <div className="grid md:grid-cols-2 gap-12 items-center">
          {/* Left: Message */}
          <div className="space-y-6 animate-slide-up">
            <div className="inline-flex items-center gap-2 bg-white/10 rounded-full px-4 py-1.5 text-sm text-blue-200 border border-white/10">
              <Lock className="w-3.5 h-3.5" />
              Plataforma oficial de control ciudadano
            </div>
            <h1 className="text-4xl md:text-5xl font-bold text-white leading-tight">
              Tus datos,<br />
              <span className="bg-gradient-to-r from-blue-300 to-cyan-300 bg-clip-text text-transparent">tu decisión.</span>
            </h1>
            <p className="text-blue-200 text-lg leading-relaxed max-w-md">
              Controla qué empresas acceden a tu información personal. Nuestra Inteligencia Artificial audita cada solicitud por ti.
            </p>
            {/* Feature pills */}
            <div className="flex flex-wrap gap-3 pt-2">
              <div className="flex items-center gap-2 bg-white/10 rounded-lg px-3 py-2 text-sm text-white border border-white/10">
                <Eye className="w-4 h-4 text-blue-300" />
                Transparencia total
              </div>
              <div className="flex items-center gap-2 bg-white/10 rounded-lg px-3 py-2 text-sm text-white border border-white/10">
                <Shield className="w-4 h-4 text-emerald-300" />
                Control granular
              </div>
              <div className="flex items-center gap-2 bg-white/10 rounded-lg px-3 py-2 text-sm text-white border border-white/10">
                <Scale className="w-4 h-4 text-amber-300" />
                Cumplimiento legal
              </div>
            </div>
          </div>

          {/* Right: Login Card */}
          <div className="animate-slide-up" style={{ animationDelay: '0.15s' }}>
            <div className="glass rounded-2xl p-8 shadow-2xl shadow-black/20 border border-white/20">
              <div className="text-center mb-6">
                <div className="w-16 h-16 bg-brand-blue rounded-2xl flex items-center justify-center mx-auto mb-4 shadow-lg shadow-brand-blue/30">
                  <Lock className="w-8 h-8 text-white" />
                </div>
                <h2 className="text-xl font-semibold text-brand-dark">Acceso Seguro</h2>
                <p className="text-sm text-slate-500 mt-1">Autenticación vía ClaveÚnica del Estado</p>
              </div>

              <div className="space-y-4">
                <div>
                  <label className="block text-sm font-medium text-slate-700 mb-1.5">RUN (ClaveÚnica)</label>
                  <input
                    type="text"
                    value={rut}
                    onChange={e => setRut(e.target.value)}
                    className="w-full px-4 py-2.5 rounded-lg border border-slate-300 focus:ring-2 focus:ring-brand-blue focus:border-brand-blue transition-all text-sm"
                    placeholder="12.345.678-9"
                  />
                </div>
                <div>
                  <label className="block text-sm font-medium text-slate-700 mb-1.5">Nombre Completo</label>
                  <input
                    type="text"
                    value={nombre}
                    onChange={e => setNombre(e.target.value)}
                    className="w-full px-4 py-2.5 rounded-lg border border-slate-300 focus:ring-2 focus:ring-brand-blue focus:border-brand-blue transition-all text-sm"
                    placeholder="Juan Pérez"
                  />
                </div>
                <button
                  onClick={handleLogin}
                  disabled={loading}
                  className="w-full py-3 bg-brand-blue hover:bg-blue-800 text-white font-medium rounded-lg transition-all flex items-center justify-center gap-2 disabled:opacity-50 shadow-lg shadow-brand-blue/20 hover:shadow-brand-blue/40"
                >
                  {loading ? (
                    <div className="w-5 h-5 border-2 border-white/30 border-t-white rounded-full animate-spin" />
                  ) : (
                    <>
                      Entrar con ClaveÚnica
                      <ChevronRight className="w-4 h-4" />
                    </>
                  )}
                </button>
              </div>

              <p className="text-xs text-slate-400 mt-4 text-center">
                🔒 Conexión cifrada. Sus datos no se almacenan en este dispositivo.
              </p>
            </div>
          </div>
        </div>
      </div>

      {/* Footer */}
      <div className="border-t border-white/10 py-6 text-center">
        <p className="text-blue-300/60 text-sm">Data Custodian Hub © 2026 — Ley 21.719 de Protección de Datos Personales</p>
      </div>
    </div>
  );
}
