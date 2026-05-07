## Soberanía Digital: Privacy Hub & IA Compliance
## Empoderando al ciudadano a través del control total de sus datos personales.

## 📌 Visión General
En el marco de la nueva Ley 21.719 de Protección de Datos Personales en Chile, Soberanía Digital nace como la infraestructura necesaria para devolver el poder al titular de la información.
Nuestra plataforma no es solo un repositorio de datos; es un Hub de Soberanía que actúa como intermediario inteligente (Notario Digital) entre los ciudadanos y las organizaciones. A través de nuestra solución, el ciudadano puede monitorear, autorizar y revocar accesos en tiempo real, garantizando que su privacidad no sea una moneda de cambio, sino un derecho inalienable.

## 🚀 Características Principales## 1. Centro de Control Ciudadano (Privacy Dashboard)
* Gestión Centralizada: Un único panel para ver todas las empresas que poseen tus datos.
* Revocación Instantánea: Interruptor de "muerte súbita" para accesos. Al desactivarlo, el sistema dispara automáticamente una orden legal de eliminación vía Webhooks a la organización.
* Gestión ARCO: Seguimiento en tiempo real de solicitudes de Acceso, Rectificación, Cancelación y Oposición.

## 2. Auditoría Agéntica (IA Compliance)
Utilizamos Claude 3.5 Sonnet como un motor de análisis normativo. Cada vez que una organización solicita datos:

* Análisis Proporcional: La IA evalúa si los datos pedidos (ej. biometría) coinciden con la finalidad declarada.
* Semáforo de Riesgo: Traduce contratos legales de 50 páginas en una alerta visual simple (Verde, Amarilla, Roja) para el usuario.
* Detección de Cláusulas Abusivas: Escaneo automático de la "letra chica" contra la jurisprudencia de la BCN.

## 3. Bóveda de Datos de Alta Seguridad (Vault)
* Segmentación Física: Separación de datos generales y sensibles.
* Encriptación de Grado Bancario: Implementación de AES-256 para cualquier dato de categoría sensible o biométrica.
* Identidad Soberana: Integración con ClaveÚnica como el estándar de autenticación del Estado.

## 🛠️ Arquitectura Técnica## Backend (The Core)
* Framework: Spring Boot 3.2+
* Seguridad: Spring Security con OAuth2 y OpenID Connect.
* IA Integration: Spring AI para la orquestación de prompts y herramientas agénticas con Anthropic.
* Base de Datos: PostgreSQL para persistencia de consentimientos y auditoría inmutable.

## Protocolo de Portabilidad Segura
El sistema implementa un flujo de transferencia supervisada:

   1. Validación de Destino: La organización receptora debe pasar por el filtro de la IA.
   2. Token de Transferencia: Se genera un puente temporal encriptado para mover la data del punto A al punto B sin almacenamiento permanente en nuestro Hub (Minimización de riesgo).

## 🛡️ Cumplimiento Legal (Privacy Score)
El sistema asigna un Puntaje de Privacidad a las organizaciones basado en:

* Claridad y brevedad de sus políticas.
* Respeto a los plazos legales de 15 días hábiles.
* Historial de solicitudes rechazadas por la IA por ser abusivas.

## 📦 Instalación y Desarrollo
Próximamente: Guía de despliegue para Antigravity.

# Clonar el repositorio
git clone https://github.com
# Configurar variables de entorno (.env)
ANTHROPIC_API_KEY=tu_key_aqui
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/privacy_hub

------------------------------
Desarrollado para el Hackathon Bendita IA - FinteChile 2026
Transformando la Ley 21.719 de un texto legal a una realidad digital accesible para todos.
