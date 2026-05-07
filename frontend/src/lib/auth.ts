import { create } from 'zustand';

interface AuthState {
  token: string;
  userName: string;
  isAuthenticated: boolean;
  login: (rut: string, nombre: string) => Promise<boolean>;
  logout: () => void;
  restoreSession: () => void;
}

const API_BASE = 'http://localhost:8080/api/v1';

export const useAuth = create<AuthState>((set) => ({
  token: '',
  userName: '',
  isAuthenticated: false,

  login: async (rut: string, nombre: string) => {
    try {
      const res = await fetch(`${API_BASE}/claveunica/mock-login`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ run: rut, nombreCompleto: nombre })
      });
      if (!res.ok) throw new Error('Login failed');
      const data = await res.json();
      localStorage.setItem('dch_token', data.token);
      localStorage.setItem('dch_userName', nombre);
      set({ token: data.token, userName: nombre, isAuthenticated: true });
      return true;
    } catch (e) {
      console.error('Auth error:', e);
      return false;
    }
  },

  logout: () => {
    localStorage.removeItem('dch_token');
    localStorage.removeItem('dch_userName');
    // Also clear old tokens
    Object.keys(localStorage).filter(k => k.startsWith('jwt_')).forEach(k => localStorage.removeItem(k));
    set({ token: '', userName: '', isAuthenticated: false });
  },

  restoreSession: () => {
    const token = localStorage.getItem('dch_token');
    const userName = localStorage.getItem('dch_userName');
    if (token) {
      set({ token, userName: userName || 'Ciudadano', isAuthenticated: true });
    }
  }
}));
