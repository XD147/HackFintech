# 📘 Frontend Architecture & Development Guidelines

## 🧠 Contexto
Este documento define estándares obligatorios para el desarrollo de aplicaciones web utilizando:

- React  
- Next.js  
- TypeScript  
- Tailwind CSS  
- React Query  
- React Hook Form  
- Framework: React 18 con Vite (para desarrollo rápido).
- Librería de UI: Material UI (MUI) o Ant Design (componentes robustos para paneles de admin).
- Mapas: React Leaflet o Google Maps React API.
- Recomendación: Leaflet + OpenStreetMap (Gratuito) para reducir costes iniciales, o Mapbox si se requiere un diseño visual muy específico.
- Gestión de Estado: Redux Toolkit o Context API.
- Gráficos: Recharts o Chart.js (para visualizar estadísticas de seguridad).

El objetivo es asegurar consistencia, escalabilidad, calidad de código y eficiencia en equipos colaborativos.

---

# 🏗️ 1. Estructura Profesional de Directorio

```

src/
│
├── app/                    # App Router (Next.js)
│   ├── (routes)/          # Agrupadores de rutas
│   ├── layout.tsx
│   └── page.tsx
│
├── components/            # Componentes reutilizables (UI)
│   ├── ui/                # Componentes base (Button, Input, etc.)
│   ├── shared/            # Componentes compartidos de negocio
│   └── layout/            # Navbar, Sidebar, Footer
│
├── features/              # Módulos por dominio (feature-based)
│   ├── auth/
│   │   ├── components/
│   │   ├── hooks/
│   │   ├── services.ts
│   │   ├── schema.ts
│   │   └── types.ts
│   │
│   └── dashboard/
│
├── hooks/                 # Hooks globales reutilizables
│
├── lib/                   # Configuraciones y utilidades
│   ├── api/               # Cliente HTTP
│   ├── utils/             # Helpers
│   └── constants/
│
├── services/              # Integraciones externas
│
├── store/                 # Estado global (si aplica)
│
├── styles/                # Configuración Tailwind / global styles
│
├── types/                 # Tipos globales
│
└── tests/                 # Testing

````

### 🔑 Principios clave:
- Arquitectura **feature-based** (NO por tipo técnico).
- Componentes UI desacoplados del dominio.
- Separación clara entre:
  - lógica
  - presentación
  - acceso a datos

---

# ⚙️ 2. Reglas Profesionales de Buenas Prácticas

## 📌 Código y Estilo
- TypeScript **estricto obligatorio**
- Prohibido `any`
- Uso de `interface` para contratos públicos
- Uso de `type` para composición

---

## 📌 Componentes
- Máximo 200 líneas por componente
- Separar lógica en hooks
- Props tipadas explícitamente
- Componentes puros siempre que sea posible

```tsx
type Props = {
  title: string;
};

export const Header = ({ title }: Props) => {
  return <h1>{title}</h1>;
};
````

---

## 📌 Hooks

* Prefijo obligatorio: `use`
* Un hook = una responsabilidad

```ts
export const useUser = () => {
  return useQuery({
    queryKey: ['user'],
    queryFn: fetchUser,
  });
};
```

---

## 📌 React Query

* Query keys centralizadas
* Nunca hacer fetch directo en componentes

```ts
const queryKeys = {
  user: ['user'],
};
```

---

## 📌 Formularios (React Hook Form)

* Validación con schema (Zod recomendado)
* Nunca manejar estado manual de inputs

---

## 📌 Tailwind CSS

* Uso de clases utilitarias
* Evitar CSS custom innecesario
* Extraer patrones repetidos a componentes

---

## 📌 Naming Convention

| Tipo        | Convención       |
| ----------- | ---------------- |
| Componentes | PascalCase       |
| Hooks       | camelCase (useX) |
| Variables   | camelCase        |
| Constantes  | UPPER_CASE       |
| Archivos    | kebab-case       |

---

## 📌 Performance

* Lazy loading en rutas grandes
* Memoización cuando sea necesario
* Evitar renders innecesarios

---

## 📌 Testing

* Unit tests para lógica
* Integration tests para features

Naming:

```
Component.test.tsx
```

---

# 🎨 3. Uso de Stitch para XUI/UI Profesional

## 📌 Objetivo

Stitch se utiliza como herramienta de:

* Prototipado UI
* Definición de Design System
* Generación de layouts consistentes

---

## 📌 Flujo de trabajo con Stitch

### 1. Diseño

* Crear vistas completas (no componentes aislados)
* Definir estados:

  * loading
  * error
  * empty
  * success

### 2. Tokenización

* Colores
* Espaciados
* Tipografía
* Breakpoints

### 3. Exportación

* Convertir diseño en:

  * estructura de componentes
  * jerarquía clara

---

## 📌 Reglas obligatorias

* No diseñar directamente en código sin pasar por Stitch
* Todo componente UI debe mapear a:

  * diseño aprobado
  * token definido

---

## 📌 Integración con Tailwind

* Mapear tokens de Stitch → Tailwind config
* Evitar valores hardcodeados

---

# 🔄 4. Proceso: Propuesta → Aceptación → Implementación

## 📌 1. Propuesta

Toda tarea debe iniciar con:

```md
## Proposal

### Problema
Descripción clara

### Solución
Explicación técnica

### Alternativas consideradas

### Impacto
- Performance
- Escalabilidad
```

---

## 📌 2. Aceptación

Antes de implementar:

* Revisión técnica
* Validación de diseño (Stitch)
* Aprobación del equipo

---

## 📌 3. Implementación

### Reglas:

* Crear branch:

```
feature/nombre-feature
```

* Commits semánticos:

```
feat: add login form
fix: correct validation bug
refactor: improve hook structure
```

---

## 📌 4. Pull Request

Debe incluir:

```md
## Summary
Qué se hizo

## Changes
- Lista de cambios

## Screenshots
(UI obligatoria)

## Checklist
- [ ] Tests
- [ ] Responsive
- [ ] Accesibilidad
```

---

## 📌 5. Code Review

Checklist obligatorio:

* Legibilidad
* Reusabilidad
* Performance
* Consistencia arquitectónica

---

## 📌 6. Deploy

* Solo código aprobado
* CI/CD obligatorio
* Feature flags para cambios grandes

---

# 🚫 Anti-Patrones Prohibidos

❌ Lógica de negocio en componentes
❌ Fetch directo en UI
❌ CSS fuera de Tailwind sin justificación
❌ Componentes monolíticos
❌ Hardcode de valores de diseño
❌ Uso de `any`

---

# ✅ Principios Fundamentales

* Separation of Concerns
* Scalability First
* Design-Driven Development
* Consistency over Cleverness
* DX (Developer Experience) importa

```

