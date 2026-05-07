import { useAuth } from './auth';

const API_BASE = 'http://localhost:8080/api/v1';

function getHeaders(): Record<string, string> {
  const token = useAuth.getState().token;
  return {
    'Content-Type': 'application/json',
    'Authorization': `Bearer ${token}`
  };
}

async function authedFetch(url: string, opts: RequestInit = {}): Promise<Response> {
  const headers = { ...getHeaders(), ...(opts.headers as Record<string, string> || {}) };
  return fetch(url, { ...opts, headers });
}

// Org-specific token for B2B dashboard (demo purposes)
async function getOrgToken(): Promise<string> {
  let token = sessionStorage.getItem('org_token');
  if (token) return token;
  try {
    const res = await fetch(`${API_BASE}/auth/login`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ email: 'org@test.com', password: 'password123' })
    });
    if (res.ok) {
      const data = await res.json();
      sessionStorage.setItem('org_token', data.token);
      return data.token;
    }
  } catch (e) { console.error('Org auth error:', e); }
  return '';
}

async function orgAuthedFetch(url: string, opts: RequestInit = {}): Promise<Response> {
  const token = await getOrgToken();
  const headers = {
    'Content-Type': 'application/json',
    'Authorization': `Bearer ${token}`,
    ...(opts.headers as Record<string, string> || {})
  };
  return fetch(url, { ...opts, headers });
}

export const api = {
  /* ── Citizen endpoints ── */
  getPermissions: async () => {
    const res = await authedFetch(`${API_BASE}/consents`);
    if (!res.ok) return [];
    return res.json();
  },

  revokePermission: async (id: string) => {
    const res = await authedFetch(`${API_BASE}/consents/${id}/revoke`, { method: 'PUT' });
    return res.ok;
  },

  getPendingRequests: async () => {
    const res = await authedFetch(`${API_BASE}/consents/pending`);
    if (!res.ok) return [];
    return res.json();
  },

  getMyArcoRequests: async () => {
    const res = await authedFetch(`${API_BASE}/arco/my-requests`);
    if (!res.ok) return [];
    return res.json();
  },

  /* ── AI Analysis ── */
  analyzePortability: async (payload: any) => {
    const res = await orgAuthedFetch(`${API_BASE}/organization-requests`, {
      method: 'POST',
      body: JSON.stringify(payload)
    });
    if (!res.ok) throw new Error('Error analyzePortability');
    return res.json();
  },

  /* ── Org B2B endpoints ── */
  getOrgDashboard: async () => {
    const res = await orgAuthedFetch(`${API_BASE}/organization/dashboard/metrics`);
    if (!res.ok) return null;
    return res.json();
  },

  getArcoRequests: async () => {
    const res = await orgAuthedFetch(`${API_BASE}/organization/dashboard/arco-requests`);
    if (!res.ok) return [];
    return res.json();
  }
};
