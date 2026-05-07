import React, { useEffect } from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { Layout } from './components/layout/Layout';
import { Landing } from './pages/Landing';
import { CitizenDashboard } from './pages/CitizenDashboard';
import { PrivacyHub } from './pages/PrivacyHub';
import { AiValidation } from './pages/AiValidation';
import { ArcoTracking } from './pages/ArcoTracking';
import { B2BDashboard } from './pages/B2BDashboard';
import { useAuth } from './lib/auth';

function AuthGuard({ children }: { children: React.ReactNode }) {
  const { isAuthenticated } = useAuth();
  if (!isAuthenticated) return <Navigate to="/login" replace />;
  return <>{children}</>;
}

function App() {
  const { restoreSession } = useAuth();

  useEffect(() => {
    restoreSession();
  }, []);

  return (
    <BrowserRouter>
      <Routes>
        <Route path="/login" element={<Landing />} />
        <Route path="/" element={<AuthGuard><Layout /></AuthGuard>}>
          <Route index element={<Navigate to="/dashboard" replace />} />
          <Route path="dashboard" element={<CitizenDashboard />} />
          <Route path="hub" element={<PrivacyHub />} />
          <Route path="validate" element={<AiValidation />} />
          <Route path="arco" element={<ArcoTracking />} />
          <Route path="b2b" element={<B2BDashboard />} />
        </Route>
      </Routes>
    </BrowserRouter>
  );
}

export default App;
