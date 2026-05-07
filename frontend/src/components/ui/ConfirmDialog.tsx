import React from 'react';
import { AlertTriangle } from 'lucide-react';
import { Button } from './Button';

interface ConfirmDialogProps {
  isOpen: boolean;
  onConfirm: () => void;
  onCancel: () => void;
  title: string;
  description: string;
  legalNote?: string;
  confirmText?: string;
  loading?: boolean;
}

export function ConfirmDialog({ isOpen, onConfirm, onCancel, title, description, legalNote, confirmText = 'Confirmar', loading = false }: ConfirmDialogProps) {
  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 z-50 modal-backdrop animate-fade-in flex items-center justify-center p-4" onClick={onCancel}>
      <div className="bg-white rounded-2xl shadow-2xl w-full max-w-md animate-slide-up" onClick={e => e.stopPropagation()}>
        <div className="p-6 text-center">
          <div className="w-14 h-14 bg-red-100 rounded-full flex items-center justify-center mx-auto mb-4">
            <AlertTriangle className="w-7 h-7 text-red-600" />
          </div>
          <h3 className="text-lg font-semibold text-brand-dark mb-2">{title}</h3>
          <p className="text-sm text-slate-600 mb-4">{description}</p>
          {legalNote && (
            <div className="bg-slate-50 rounded-lg p-3 text-xs text-slate-500 mb-6 text-left border border-slate-200">
              <span className="font-semibold text-slate-700">📋 Base Legal:</span> {legalNote}
            </div>
          )}
          <div className="flex gap-3">
            <Button variant="outline" className="flex-1" onClick={onCancel} disabled={loading}>
              Cancelar
            </Button>
            <Button variant="danger" className="flex-1" onClick={onConfirm} disabled={loading}>
              {loading ? 'Procesando...' : confirmText}
            </Button>
          </div>
        </div>
      </div>
    </div>
  );
}
