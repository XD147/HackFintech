import React, { useEffect, useState } from 'react';
import { Card } from '../components/ui/Card';
import { Badge } from '../components/ui/Badge';
import { Timeline } from '../components/ui/Timeline';
import type { TimelineStep } from '../components/ui/Timeline';
import { SkeletonCard } from '../components/ui/Skeleton';
import { api } from '../lib/api';
import { FileText, AlertCircle, Clock, CheckCircle } from 'lucide-react';
import { Button } from '../components/ui/Button';

export function ArcoTracking() {
  const [requests, setRequests] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    api.getMyArcoRequests().then(data => {
      setRequests(data || []);
      setLoading(false);
    }).catch(() => setLoading(false));
  }, []);

  const buildTimeline = (req: any): TimelineStep[] => {
    const dias = req.diasRestantes ?? 0;
    const diaActual = 15 - dias;
    const isOverdue = dias <= 0;

    return [
      { title: 'Solicitud enviada', description: `Tipo: ${req.tipo}`, date: new Date(req.fechaSolicitud).toLocaleDateString(), status: 'completed' },
      { title: 'Recepción confirmada', description: 'La organización recibió tu solicitud', status: 'completed' },
      { title: `Día ${Math.min(diaActual, 15)} de 15 — En proceso`, description: req.detalleSolicitud || 'Esperando respuesta de la organización', status: isOverdue ? 'overdue' : 'current' },
      { title: 'Resolución', description: isOverdue ? 'Plazo vencido — Puede denunciar' : 'Pendiente de respuesta', status: isOverdue ? 'overdue' : 'pending' }
    ];
  };

  return (
    <div className="space-y-6">
      <div className="animate-fade-in">
        <h2 className="text-2xl font-semibold text-brand-dark">Tracking de Derechos ARCO</h2>
        <p className="text-slate-500">Seguimiento en tiempo real de tus solicitudes de Acceso, Rectificación, Cancelación y Oposición.</p>
      </div>

      {loading ? (
        <div className="space-y-4"><SkeletonCard /><SkeletonCard /></div>
      ) : requests.length === 0 ? (
        <Card>
          <div className="text-center py-12 text-slate-400">
            <FileText className="w-12 h-12 mx-auto mb-4 opacity-40" />
            <p className="font-medium text-lg">Sin solicitudes ARCO</p>
            <p className="text-sm mt-2">Cuando envíes una solicitud de acceso, rectificación o cancelación, su progreso aparecerá aquí.</p>
          </div>
        </Card>
      ) : (
        <div className="space-y-4 stagger-children">
          {requests.map((req) => {
            const dias = req.diasRestantes ?? 0;
            const isOverdue = dias <= 0;
            const progress = Math.min(((15 - dias) / 15) * 100, 100);

            return (
              <Card key={req.id} className={`${isOverdue ? 'border-l-4 border-red-500' : ''}`}>
                <div className="flex flex-col md:flex-row gap-6">
                  {/* Left: Info + Progress */}
                  <div className="flex-1">
                    <div className="flex items-center gap-3 mb-4">
                      <div className={`w-10 h-10 rounded-lg flex items-center justify-center ${isOverdue ? 'bg-red-100 text-red-600' : 'bg-brand-blue/10 text-brand-blue'}`}>
                        {isOverdue ? <AlertCircle className="w-5 h-5" /> : <Clock className="w-5 h-5" />}
                      </div>
                      <div>
                        <div className="flex items-center gap-2">
                          <h3 className="font-semibold text-brand-dark">{req.tipo}</h3>
                          <Badge variant={isOverdue ? 'danger' : req.estado === 'EN_PROCESO' ? 'warning' : 'info'}>
                            {req.estado}
                          </Badge>
                        </div>
                        <p className="text-xs text-slate-400">#{req.id?.substring(0, 8)}</p>
                      </div>
                    </div>

                    {/* Progress bar */}
                    <div className="mb-2">
                      <div className="flex justify-between text-xs mb-1">
                        <span className={`font-medium ${isOverdue ? 'text-red-600' : 'text-brand-blue'}`}>
                          {isOverdue ? '⚠️ Plazo vencido' : `Día ${15 - dias} de 15`}
                        </span>
                        <span className="text-slate-400">{dias > 0 ? `${dias} días restantes` : 'Vencido'}</span>
                      </div>
                      <div className="w-full h-2 bg-slate-100 rounded-full overflow-hidden">
                        <div className={`h-full rounded-full transition-all ${isOverdue ? 'bg-red-500' : progress > 70 ? 'bg-amber-500' : 'bg-brand-blue'}`} style={{ width: `${progress}%` }} />
                      </div>
                    </div>

                    {isOverdue && (
                      <Button variant="danger" size="sm" className="mt-3">
                        <AlertCircle className="w-4 h-4 mr-1" /> Denunciar ante la Agencia
                      </Button>
                    )}
                  </div>

                  {/* Right: Timeline */}
                  <div className="md:w-64 md:border-l md:border-slate-100 md:pl-6">
                    <Timeline steps={buildTimeline(req)} />
                  </div>
                </div>
              </Card>
            );
          })}
        </div>
      )}
    </div>
  );
}
