import React from 'react';
import { Bot } from 'lucide-react';

interface LottieLoaderProps {
  text?: string;
}

export function LottieLoader({ text = 'La IA está analizando...' }: LottieLoaderProps) {
  return (
    <div className="flex flex-col items-center justify-center py-12 animate-fade-in">
      {/* Animated brain/processing visual using CSS */}
      <div className="relative w-24 h-24 mb-6">
        {/* Outer ring */}
        <div className="absolute inset-0 rounded-full border-4 border-brand-blue/20" />
        <div className="absolute inset-0 rounded-full border-4 border-transparent border-t-brand-blue animate-spin" style={{ animationDuration: '1.5s' }} />
        {/* Middle ring */}
        <div className="absolute inset-3 rounded-full border-4 border-transparent border-b-blue-400 animate-spin" style={{ animationDuration: '2s', animationDirection: 'reverse' }} />
        {/* Center icon */}
        <div className="absolute inset-0 flex items-center justify-center">
          <div className="w-12 h-12 bg-gradient-to-br from-brand-blue to-blue-400 rounded-full flex items-center justify-center shadow-lg shadow-brand-blue/30">
            <Bot className="w-6 h-6 text-white" />
          </div>
        </div>
        {/* Pulse dots */}
        <div className="absolute -top-1 left-1/2 -translate-x-1/2 w-2 h-2 bg-brand-blue rounded-full animate-pulse-dot" style={{ animationDelay: '0s' }} />
        <div className="absolute top-1/2 -right-1 -translate-y-1/2 w-2 h-2 bg-blue-400 rounded-full animate-pulse-dot" style={{ animationDelay: '0.5s' }} />
        <div className="absolute -bottom-1 left-1/2 -translate-x-1/2 w-2 h-2 bg-brand-blue rounded-full animate-pulse-dot" style={{ animationDelay: '1s' }} />
        <div className="absolute top-1/2 -left-1 -translate-y-1/2 w-2 h-2 bg-blue-400 rounded-full animate-pulse-dot" style={{ animationDelay: '1.5s' }} />
      </div>
      <p className="text-brand-blue font-medium">{text}</p>
      <p className="text-sm text-slate-400 mt-1">Verificando cumplimiento con Ley 21.719</p>
      {/* Animated progress bar */}
      <div className="w-48 h-1.5 bg-slate-200 rounded-full mt-4 overflow-hidden">
        <div className="h-full bg-gradient-to-r from-brand-blue to-blue-400 rounded-full animate-pulse" style={{ width: '60%' }} />
      </div>
    </div>
  );
}
