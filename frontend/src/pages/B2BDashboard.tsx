import React, { useEffect, useState } from 'react';
import { Card } from '../components/ui/Card';
import { Badge } from '../components/ui/Badge';
import { api } from '../lib/api';
import { Building2, AlertOctagon, Users, FileWarning, RefreshCw } from 'lucide-react';

const DEFAULT_METRICS = {
  privacyScore: 0,
  acceptanceRate: 0,
  rejectionRate: 0,
  consentChurn30Days: 0
};

export function B2BDashboard() {
  const [data, setData] = useState<any>(null);
  const [arcoReqs, setArcoReqs] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const loadData = async () => {
    setLoading(true);
    setError(null);
    try {
      const [metrics, arco] = await Promise.all([
        api.getOrgDashboard(),
        api.getArcoRequests()
      ]);
      setData(metrics || DEFAULT_METRICS);
      setArcoReqs(arco || []);
    } catch (e) {
      console.error('Error cargando dashboard B2B:', e);
      setData(DEFAULT_METRICS);
      setError('No se pudieron cargar las métricas. Mostrando valores por defecto.');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { loadData(); }, []);

  if (loading) return (
    <div className="p-8 text-center">
      <RefreshCw className="w-6 h-6 animate-spin mx-auto mb-2 text-brand-blue" />
      <p className="text-slate-500">Cargando métricas...</p>
    </div>
  );

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <div>
          <h2 className="text-2xl font-semibold text-brand-dark">Dashboard de Cumplimiento (B2B)</h2>
          <p className="text-slate-500">Métricas de salud legal y gestión de derechos ARCO para tu organización.</p>
        </div>
        <button onClick={loadData} className="p-2 rounded-lg hover:bg-slate-100 text-slate-500 hover:text-brand-blue transition-colors" title="Recargar">
          <RefreshCw className="w-5 h-5" />
        </button>
      </div>

      {error && (
        <div className="p-4 rounded-lg bg-yellow-50 border border-yellow-200 text-yellow-800 text-sm">
          ⚠️ {error}
        </div>
      )}

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        <Card className="flex items-center gap-4 border-l-4 border-brand-blue">
          <div className="p-3 bg-blue-100 text-brand-blue rounded-lg">
            <Building2 className="w-6 h-6" />
          </div>
          <div>
            <p className="text-sm font-medium text-slate-500">Privacy Score</p>
            <p className="text-3xl font-bold text-brand-dark">{data.privacyScore}/100</p>
          </div>
        </Card>

        <Card className="flex items-center gap-4">
          <div className="p-3 bg-green-100 text-green-600 rounded-lg">
            <Users className="w-6 h-6" />
          </div>
          <div>
            <p className="text-sm font-medium text-slate-500">Tasa Aceptación</p>
            <p className="text-2xl font-semibold text-brand-dark">{data.acceptanceRate}%</p>
          </div>
        </Card>

        <Card className="flex items-center gap-4">
          <div className="p-3 bg-yellow-100 text-yellow-600 rounded-lg">
            <FileWarning className="w-6 h-6" />
          </div>
          <div>
            <p className="text-sm font-medium text-slate-500">Tasa Rechazo</p>
            <p className="text-2xl font-semibold text-brand-dark">{data.rejectionRate}%</p>
          </div>
        </Card>

        <Card className="flex items-center gap-4 border-l-4 border-red-500">
          <div className="p-3 bg-red-100 text-red-600 rounded-lg">
            <AlertOctagon className="w-6 h-6" />
          </div>
          <div>
            <p className="text-sm font-medium text-slate-500">Churn 30D</p>
            <p className="text-2xl font-semibold text-red-600">{data.consentChurn30Days}</p>
          </div>
        </Card>
      </div>

      <h3 className="text-lg font-semibold text-brand-dark mt-8 mb-4">Bandeja de Derechos ARCO</h3>
      <Card className="p-0 overflow-hidden">
        {arcoReqs.length === 0 ? (
          <div className="p-8 text-center text-slate-500">No hay solicitudes ARCO pendientes.</div>
        ) : (
          <table className="w-full text-left border-collapse">
            <thead>
              <tr className="bg-slate-50 border-b border-slate-200">
                <th className="p-4 font-medium text-slate-600 text-sm">ID Solicitud</th>
                <th className="p-4 font-medium text-slate-600 text-sm">Tipo</th>
                <th className="p-4 font-medium text-slate-600 text-sm">Fecha Recepción</th>
                <th className="p-4 font-medium text-slate-600 text-sm">Plazo Legal (15 días)</th>
                <th className="p-4 font-medium text-slate-600 text-sm">Estado</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-slate-200">
              {arcoReqs.map(req => (
                <tr key={req.id} className="hover:bg-slate-50">
                  <td className="p-4 font-mono text-sm text-brand-blue">#{req.id.substring(0, 8)}</td>
                  <td className="p-4">{req.tipo}</td>
                  <td className="p-4 text-sm">{new Date(req.fechaSolicitud).toLocaleDateString()}</td>
                  <td className="p-4 text-sm font-medium text-red-600">Quedan {req.diasRestantes} días hábiles</td>
                  <td className="p-4">
                    <Badge variant={req.diasRestantes < 3 ? "danger" : "warning"}>{req.estado}</Badge>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </Card>
    </div>
  );
}
