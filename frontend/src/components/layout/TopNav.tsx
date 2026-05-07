import React from 'react';
import { Bell, User, Menu, LogOut } from 'lucide-react';
import { Badge } from '../ui/Badge';
import { useAuth } from '../../lib/auth';
import { useNavigate } from 'react-router-dom';

interface TopNavProps {
  onMenuToggle: () => void;
}

export function TopNav({ onMenuToggle }: TopNavProps) {
  const { userName, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <header className="bg-white border-b border-slate-200 h-16 flex items-center justify-between px-4 md:px-6 sticky top-0 z-20">
      <div className="flex items-center gap-3">
        <button
          onClick={onMenuToggle}
          className="p-2 text-slate-500 hover:text-brand-blue rounded-lg hover:bg-slate-50 transition-colors md:hidden"
        >
          <Menu className="w-5 h-5" />
        </button>
        <h1 className="text-lg md:text-xl font-semibold text-brand-dark">Data Custodian Hub</h1>
        <Badge variant="info" className="hidden sm:inline-flex">Mi Huella Digital</Badge>
      </div>
      
      <div className="flex items-center gap-2 md:gap-4">
        {userName && (
          <span className="text-sm text-slate-600 hidden md:inline">
            Hola, <span className="font-medium text-brand-dark">{userName.split(' ')[0]}</span>
          </span>
        )}
        <button className="p-2 text-slate-500 hover:text-brand-blue rounded-full hover:bg-slate-50 transition-colors relative">
          <Bell className="w-5 h-5" />
          <span className="absolute top-1.5 right-1.5 w-2 h-2 bg-red-500 rounded-full"></span>
        </button>
        <div className="h-8 w-8 rounded-full bg-brand-blue/10 flex items-center justify-center text-brand-blue border border-brand-blue/20">
          <User className="w-4 h-4" />
        </div>
        <button
          onClick={handleLogout}
          className="p-2 text-slate-400 hover:text-red-500 rounded-lg hover:bg-red-50 transition-colors"
          title="Cerrar sesión"
        >
          <LogOut className="w-4 h-4" />
        </button>
      </div>
    </header>
  );
}
