import React, { useState } from 'react';
import { Card } from '../components/ui/Card';
import { Badge } from '../components/ui/Badge';
import { Button } from '../components/ui/Button';
import { LottieLoader } from '../components/ui/LottieLoader';
import { api } from '../lib/api';
import { Bot, ShieldAlert, CheckCircle, AlertTriangle, ArrowLeft, ArrowRight, Database, Clock, Globe } from 'lucide-react';

export function AiValidation() {
  const [loading, setLoading] = useState(false);
  const [result, setResult] = useState<any>(null);

  const simulateValidation = async () => {
    setLoading(true);
    const payload = {
      request_header: {
        organization_id: "ORG-002",
        organization_name: "Global Retail S.A.",
        request_timestamp: new Date().toISOString(),
        request_id: "REQ-" + Math.floor(Math.random() * 10000),
        request_type: "PORTABILITY"
      },
      legal_declaration: {
        base_legal: "Consentimiento",
        finalidad_tratamiento: "Marketing y promociones a terceros",
        clausula_privacidad_url: "https://ejemplo.com/privacidad",
        tiempo_retencion_dias: 365,
        transferencia_internacional: false,
        texto_legal_completo: "Usaremos sus datos médicos y financieros para campañas de marketing masivo."
      },
      requested_data_structure: [
        { categoria: "Salud", campos: ["Historial médico"], justificacion: "Para enviar promociones médicas." }
      ],
      rutCiudadano: "12345678-9"
    };
    try {
      const res = await api.analyzePortability(payload);
      setResult(res);
    } catch (e) {
      console.error(e);
    }
    setLoading(false);
  };

  const flagColor = (d: string) => d === 'RED' ? 'red' : d === 'GREEN' ? 'green' : 'yellow';

  return (
    <div className="space-y-6 max-w-5xl mx-auto">
      <div className="text-center space-y-3 animate-fade-in">
        <div className="w-14 h-14 bg-gradient-to-br from-brand-blue to-blue-400 rounded-2xl flex items-center justify-center mx-auto text-white shadow-lg shadow-brand-blue/20">
          <Bot className="w-7 h-7" />
        </div>
        <h2 className="text-2xl font-semibold text-brand-dark">Defensa IA</h2>
        <p className="text-slate-500 max-w-2xl mx-auto text-sm">
          Nuestra IA audita en tiempo real las políticas de privacidad antes de permitir la portabilidad, verificando la Ley 21.719.
        </p>
      </div>

      {!result && !loading && (
        <div className="flex justify-center mt-6 animate-slide-up">
          <Button onClick={simulateValidation} size="lg" className="shadow-lg shadow-brand-blue/20">
            Simular Solicitud de "Global Retail S.A."
          </Button>
        </div>
      )}

      {loading && <LottieLoader text="Analizando cláusulas legales de Global Retail S.A." />}

      {result && (
        <div className="animate-slide-up space-y-6">
          {/* Split view: Request vs Analysis */}
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            {/* Left: What they request */}
            <Card className="border-t-4 border-slate-300">
              <h3 className="font-semibold text-brand-dark mb-4 flex items-center gap-2">
                <ArrowLeft className="w-4 h-4" /> Lo que solicita la empresa
              </h3>
              <div className="space-y-3">
                <div className="flex items-center gap-2 text-sm">
                  <Database className="w-4 h-4 text-slate-400" />
                  <span className="text-slate-600">Categoría: <strong>Salud</strong></span>
                </div>
                <div className="flex items-center gap-2 text-sm">
                  <Globe className="w-4 h-4 text-slate-400" />
                  <span className="text-slate-600">Finalidad: <strong>Marketing</strong></span>
                </div>
                <div className="flex items-center gap-2 text-sm">
                  <Clock className="w-4 h-4 text-slate-400" />
                  <span className="text-slate-600">Retención: <strong>365 días</strong></span>
                </div>
              </div>
            </Card>

            {/* Right: AI Analysis */}
            <Card className={`border-t-4 border-${flagColor(result.decision)}-500`}>
              <h3 className="font-semibold text-brand-dark mb-4 flex items-center gap-2">
                <ArrowRight className="w-4 h-4" /> Análisis de la IA
              </h3>
              <div className="flex items-start gap-3 mb-3">
                <div className={`p-2 rounded-full bg-${flagColor(result.decision)}-100 text-${flagColor(result.decision)}-600`}>
                  {result.decision === 'RED' ? <ShieldAlert className="w-5 h-5" /> :
                   result.decision === 'GREEN' ? <CheckCircle className="w-5 h-5" /> : <AlertTriangle className="w-5 h-5" />}
                </div>
                <div>
                  <Badge variant={result.decision === 'RED' ? 'danger' : result.decision === 'GREEN' ? 'success' : 'warning'}>
                    Bandera {result.decision === 'RED' ? 'Roja' : result.decision === 'GREEN' ? 'Verde' : 'Amarilla'}
                  </Badge>
                  <p className="text-sm text-slate-600 mt-2">{result.explanation}</p>
                </div>
              </div>
              {result.warnings?.length > 0 && (
                <div className="bg-slate-50 rounded-lg p-3 mt-3">
                  <p className="text-xs font-semibold text-slate-700 mb-1">⚠️ Advertencias:</p>
                  <ul className="text-xs text-slate-600 space-y-1 list-disc pl-4">
                    {result.warnings.map((w: string, i: number) => <li key={i}>{w}</li>)}
                  </ul>
                </div>
              )}
            </Card>
          </div>

          {/* Actions */}
          <div className="flex flex-col sm:flex-row justify-center gap-3">
            <Button variant="outline" onClick={() => setResult(null)}>Cancelar</Button>
            <Button variant={result.decision === 'RED' ? 'danger' : 'primary'} disabled={result.decision === 'RED'}>
              {result.decision === 'RED' ? 'Transferencia Bloqueada' : 'Autorizar Portabilidad'}
            </Button>
          </div>
        </div>
      )}
    </div>
  );
}
