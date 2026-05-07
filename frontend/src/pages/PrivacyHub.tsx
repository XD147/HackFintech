import React, { useEffect, useState } from 'react';
import { Card } from '../components/ui/Card';
import { Badge } from '../components/ui/Badge';
import { ToggleSwitch } from '../components/ui/ToggleSwitch';
import { ConfirmDialog } from '../components/ui/ConfirmDialog';
import { SkeletonCard } from '../components/ui/Skeleton';
import { api } from '../lib/api';
import { Shield, Globe, Database, MapPin } from 'lucide-react';

export function PrivacyHub() {
  const [permissions, setPermissions] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);
  const [revokeTarget, setRevokeTarget] = useState<any>(null);
  const [revoking, setRevoking] = useState(false);

  useEffect(() => {
    api.getPermissions().then(data => {
      setPermissions(data || []);
      setLoading(false);
    });
  }, []);

  const handleToggle = (perm: any) => {
    if (perm.estado === 'ACTIVE') setRevokeTarget(perm);
  };

  const confirmRevoke = async () => {
    if (!revokeTarget) return;
    setRevoking(true);
    const ok = await api.revokePermission(revokeTarget.id);
    if (ok) {
      setPermissions(prev => prev.map(p => p.id === revokeTarget.id ? { ...p, estado: 'REVOKED' } : p));
    }
    setRevoking(false);
    setRevokeTarget(null);
  };

  return (
    <div className="space-y-6">
      <div className="animate-fade-in">
        <h2 className="text-2xl font-semibold text-brand-dark">Centro de Control</h2>
        <p className="text-slate-500">Administra qué empresas tienen acceso a tus datos y revócalo con un clic.</p>
      </div>

      {loading ? (
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4"><SkeletonCard /><SkeletonCard /><SkeletonCard /></div>
      ) : permissions.length === 0 ? (
        <Card><div className="text-center py-12 text-slate-400"><Shield className="w-12 h-12 mx-auto mb-4 opacity-40" /><p className="font-medium text-lg">Sin permisos activos</p><p className="text-sm mt-2">Cuando una empresa solicite acceso a tus datos, aparecerá aquí.</p></div></Card>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4 stagger-children">
          {permissions.map((p) => {
            const isActive = p.estado === 'ACTIVE';
            return (
              <Card key={p.id} className={`transition-all duration-300 ${!isActive ? 'opacity-50 grayscale' : 'hover:shadow-md'}`}>
                <div className="flex items-start justify-between mb-4">
                  <div className="flex items-center gap-3">
                    <div className={`w-10 h-10 rounded-lg flex items-center justify-center ${isActive ? 'bg-brand-blue/10 text-brand-blue' : 'bg-slate-100 text-slate-400'}`}>
                      <Shield className="w-5 h-5" />
                    </div>
                    <div>
                      <h3 className="font-semibold text-brand-dark">{p.nombreOrganizacion}</h3>
                      <p className="text-xs text-slate-400">Desde {new Date(p.fechaOtorgamiento).toLocaleDateString()}</p>
                    </div>
                  </div>
                  <ToggleSwitch checked={isActive} onChange={() => handleToggle(p)} disabled={!isActive} />
                </div>
                <div className="flex flex-wrap gap-2">
                  {p.permitirDatosBasicos && <Badge variant="gray"><Database className="w-3 h-3 mr-1" />Básicos</Badge>}
                  {p.permitirDatosSensibles && <Badge variant="warning"><Shield className="w-3 h-3 mr-1" />Sensibles</Badge>}
                  {p.permitirGeolocalizacion && <Badge variant="info"><MapPin className="w-3 h-3 mr-1" />Ubicación</Badge>}
                </div>
                {!isActive && <div className="mt-3 pt-3 border-t border-slate-100"><p className="text-xs text-slate-400">✅ Acceso revocado</p></div>}
              </Card>
            );
          })}
        </div>
      )}

      <ConfirmDialog
        isOpen={!!revokeTarget}
        onCancel={() => setRevokeTarget(null)}
        onConfirm={confirmRevoke}
        loading={revoking}
        title="Revocar acceso"
        description={`Se enviará una orden de revocación inmediata a "${revokeTarget?.nombreOrganizacion}".`}
        legalNote="Art. 9 Ley 21.719: El consentimiento puede ser revocado en cualquier momento sin expresar causa."
        confirmText="Revocar Acceso"
      />
    </div>
  );
}
