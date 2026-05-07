import React, { useEffect, useState } from 'react';
import { Card } from '../components/ui/Card';
import { SkeletonCard } from '../components/ui/Skeleton';
import { ShieldAlert, Building, FileText, Activity, ChevronRight, Bell } from 'lucide-react';
import { api } from '../lib/api';
import { useAuth } from '../lib/auth';
import { useNavigate } from 'react-router-dom';

export function CitizenDashboard() {
  const { userName } = useAuth();
  const navigate = useNavigate();
  const [loading, setLoading] = useState(true);
  const [empresas, setEmpresas] = useState(0);
  const [pendingCount, setPendingCount] = useState(0);
  const [permissions, setPermissions] = useState<any[]>([]);

  useEffect(() => {
    Promise.all([
      api.getPermissions(),
      api.getPendingRequests()
    ]).then(([perms, pending]) => {
      setPermissions(perms || []);
      setEmpresas(perms ? perms.length : 0);
      setPendingCount(pending ? pending.length : 0);
      setLoading(false);
    }).catch(() => setLoading(false));
  }, []);

  const firstName = userName?.split(' ')[0] || 'Ciudadano';

  return (
    <div className="space-y-6">
      <div className="animate-fade-in">
        <h2 className="text-2xl font-semibold text-brand-dark">Hola, {firstName} 👋</h2>
        <p className="text-slate-500">Aquí está el resumen de tu huella digital y privacidad.</p>
      </div>

      {/* Notification banner */}
      {pendingCount > 0 && (
        <div
          className="bg-gradient-to-r from-amber-50 to-yellow-50 border border-amber-200 rounded-xl p-4 flex items-center gap-4 cursor-pointer hover:shadow-md transition-shadow animate-slide-up"
          onClick={() => navigate('/validate')}
        >
          <div className="w-10 h-10 bg-amber-100 rounded-full flex items-center justify-center flex-shrink-0">
            <Bell className="w-5 h-5 text-amber-600" />
          </div>
          <div className="flex-1">
            <p className="text-sm font-semibold text-amber-900">
              {pendingCount} solicitud{pendingCount > 1 ? 'es' : ''} pendiente{pendingCount > 1 ? 's' : ''} de revisión
            </p>
            <p className="text-xs text-amber-700">Una empresa requiere acceso a tus datos. Revisa el análisis de la IA.</p>
          </div>
          <ChevronRight className="w-5 h-5 text-amber-400 flex-shrink-0" />
        </div>
      )}

      {/* Metrics cards */}
      <div className="grid grid-cols-2 lg:grid-cols-4 gap-4 md:gap-6 stagger-children">
        {loading ? (
          <>
            <SkeletonCard />
            <SkeletonCard />
            <SkeletonCard />
            <SkeletonCard />
          </>
        ) : (
          <>
            <Card className="flex items-center gap-3 md:gap-4 cursor-pointer hover:shadow-md transition-shadow" onClick={() => navigate('/hub')}>
              <div className="p-2.5 md:p-3 bg-blue-100 text-brand-blue rounded-lg">
                <Building className="w-5 h-5 md:w-6 md:h-6" />
              </div>
              <div>
                <p className="text-xs md:text-sm font-medium text-slate-500">Empresas con acceso</p>
                <p className="text-xl md:text-2xl font-semibold text-brand-dark">{empresas}</p>
              </div>
            </Card>

            <Card className="flex items-center gap-3 md:gap-4">
              <div className="p-2.5 md:p-3 bg-red-100 text-red-600 rounded-lg">
                <ShieldAlert className="w-5 h-5 md:w-6 md:h-6" />
              </div>
              <div>
                <p className="text-xs md:text-sm font-medium text-slate-500">Alertas IA</p>
                <p className="text-xl md:text-2xl font-semibold text-brand-dark">0</p>
              </div>
            </Card>

            <Card className="flex items-center gap-3 md:gap-4 cursor-pointer hover:shadow-md transition-shadow" onClick={() => navigate('/arco')}>
              <div className="p-2.5 md:p-3 bg-yellow-100 text-yellow-600 rounded-lg">
                <FileText className="w-5 h-5 md:w-6 md:h-6" />
              </div>
              <div>
                <p className="text-xs md:text-sm font-medium text-slate-500">Solicitudes ARCO</p>
                <p className="text-xl md:text-2xl font-semibold text-brand-dark">0</p>
              </div>
            </Card>

            <Card className="flex items-center gap-3 md:gap-4">
              <div className="p-2.5 md:p-3 bg-green-100 text-green-600 rounded-lg">
                <Activity className="w-5 h-5 md:w-6 md:h-6" />
              </div>
              <div>
                <p className="text-xs md:text-sm font-medium text-slate-500">Nivel de Seguridad</p>
                <p className="text-xl md:text-2xl font-semibold text-brand-dark">{empresas > 0 ? 'Alto' : 'Pendiente'}</p>
              </div>
            </Card>
          </>
        )}
      </div>

      {/* Recent activity */}
      <h3 className="text-lg font-semibold text-brand-dark mt-8 mb-4">Actividad Reciente</h3>
      <Card>
        {permissions.length === 0 ? (
          <div className="text-center py-8 text-slate-400">
            <Activity className="w-10 h-10 mx-auto mb-3 opacity-40" />
            <p className="font-medium">Sin actividad reciente</p>
            <p className="text-sm mt-1">Los accesos a tus datos aparecerán aquí en tiempo real.</p>
          </div>
        ) : (
          <div className="space-y-4">
            {permissions.slice(0, 3).map((p: any, i: number) => (
              <div key={i} className="flex gap-3 items-start">
                <div className="mt-1.5">
                  <span className={`flex h-2.5 w-2.5 rounded-full ${p.estado === 'ACTIVE' ? 'bg-brand-blue' : 'bg-slate-300'}`}></span>
                </div>
                <div>
                  <p className="text-sm font-medium text-brand-dark">{p.nombreOrganizacion} tiene acceso activo</p>
                  <p className="text-xs text-slate-400 mt-0.5">
                    Otorgado el {new Date(p.fechaOtorgamiento).toLocaleDateString()}
                  </p>
                </div>
              </div>
            ))}
          </div>
        )}
      </Card>
    </div>
  );
}
