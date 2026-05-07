import React from 'react';
import { NavLink } from 'react-router-dom';
import { LayoutDashboard, Shield, Building2, Activity, FileText } from 'lucide-react';

interface SidebarProps {
  isOpen: boolean;
  onClose: () => void;
}

export function Sidebar({ isOpen, onClose }: SidebarProps) {
  const navItems = [
    { to: '/dashboard', icon: LayoutDashboard, label: 'Resumen Ciudadano' },
    { to: '/hub', icon: Shield, label: 'Centro de Control' },
    { to: '/validate', icon: Activity, label: 'Simulador IA' },
    { to: '/arco', icon: FileText, label: 'Tracking ARCO' },
    { to: '/b2b', icon: Building2, label: 'Dashboard Empresas' },
  ];

  return (
    <aside className={`
      fixed left-0 top-16 z-40 h-[calc(100vh-4rem)] w-64 bg-white border-r border-slate-200 flex flex-col
      transition-transform duration-300 ease-in-out
      ${isOpen ? 'translate-x-0' : '-translate-x-full'}
      md:translate-x-0
    `}>
      <nav className="p-4 space-y-1">
        {navItems.map((item) => (
          <NavLink
            key={item.to}
            to={item.to}
            onClick={onClose}
            className={({ isActive }) =>
              `flex items-center gap-3 px-3 py-2.5 rounded-lg font-medium transition-colors ${
                isActive
                  ? 'bg-brand-blue/10 text-brand-blue'
                  : 'text-slate-600 hover:bg-slate-50 hover:text-slate-900'
              }`
            }
          >
            <item.icon className="w-5 h-5" />
            {item.label}
          </NavLink>
        ))}
      </nav>
      
      <div className="mt-auto p-4 border-t border-slate-200">
        <div className="bg-gradient-to-r from-brand-blue/5 to-blue-50 rounded-lg p-3 text-sm border border-brand-blue/10">
          <p className="font-medium text-brand-dark mb-1">🛡️ Ley 21.719</p>
          <p className="text-slate-500 text-xs">Sistema en cumplimiento activo. Protección de datos personales.</p>
        </div>
      </div>
    </aside>
  );
}
