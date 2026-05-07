import React from 'react';
import { CheckCircle, Clock, AlertCircle } from 'lucide-react';

export interface TimelineStep {
  title: string;
  description?: string;
  date?: string;
  status: 'completed' | 'current' | 'pending' | 'overdue';
}

interface TimelineProps {
  steps: TimelineStep[];
}

export function Timeline({ steps }: TimelineProps) {
  const getIcon = (status: TimelineStep['status']) => {
    switch (status) {
      case 'completed':
        return <CheckCircle className="w-5 h-5 text-white" />;
      case 'current':
        return <Clock className="w-5 h-5 text-white" />;
      case 'overdue':
        return <AlertCircle className="w-5 h-5 text-white" />;
      default:
        return <div className="w-2.5 h-2.5 rounded-full bg-slate-300" />;
    }
  };

  const getNodeStyle = (status: TimelineStep['status']) => {
    switch (status) {
      case 'completed':
        return 'bg-emerald-500';
      case 'current':
        return 'bg-brand-blue animate-pulse-dot';
      case 'overdue':
        return 'bg-red-500';
      default:
        return 'bg-slate-200';
    }
  };

  return (
    <div className="space-y-0">
      {steps.map((step, i) => (
        <div key={i} className="flex gap-4">
          {/* Line + Node */}
          <div className="flex flex-col items-center">
            <div className={`w-8 h-8 rounded-full flex items-center justify-center flex-shrink-0 ${getNodeStyle(step.status)}`}>
              {getIcon(step.status)}
            </div>
            {i < steps.length - 1 && (
              <div className={`w-0.5 flex-1 min-h-[32px] ${
                step.status === 'completed' ? 'bg-emerald-300' : 'bg-slate-200'
              }`} />
            )}
          </div>
          {/* Content */}
          <div className="pb-6 pt-1">
            <p className={`text-sm font-semibold ${
              step.status === 'overdue' ? 'text-red-700' :
              step.status === 'current' ? 'text-brand-blue' :
              step.status === 'completed' ? 'text-emerald-700' : 'text-slate-400'
            }`}>
              {step.title}
            </p>
            {step.description && (
              <p className="text-sm text-slate-500 mt-0.5">{step.description}</p>
            )}
            {step.date && (
              <p className="text-xs text-slate-400 mt-1">{step.date}</p>
            )}
          </div>
        </div>
      ))}
    </div>
  );
}
