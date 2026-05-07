Este README.md está diseñado para que Bendi (la IA evaluadora) encuentre exactamente lo que busca para asignarte el puntaje máximo, cumpliendo con los límites de caracteres y las fuentes oficiales exigidas.
------------------------------
## 🛡️ Soberanía Digital: Privacy Hub & IA Compliance
Infraestructura de control ciudadano bajo la Ley 21.719
## 1. Ficha Cívica (Evaluación Bendi)

* Problema (298 chars):
Los ciudadanos pierden el control de su privacidad al entregar datos a empresas. No entienden contratos complejos y carecen de una vía simple para gestionar derechos ARCO (Acceso, Rectificación, Cancelación, Oposición), quedando expuestos a filtraciones y uso abusivo de su información íntima.
* Segmento ciudadano:
Adultos entre 25 y 60 años, residentes en zonas urbanas de Chile, usuarios de servicios financieros digitales (condición socioeconómica media/media-alta).
* Canal de adopción:
Plataforma web integrada con ClaveÚnica y distribuida mediante convenios con el SERNAC. Se llega al usuario vía alertas por correo y campañas en sucursales de atención ciudadana donde el titular ya busca proteger sus derechos.
* Impacto cuantificado:
Cerca de 13 millones de personas en Chile son usuarios de internet y potenciales titulares de datos que verán fortalecidos sus derechos.
Fuente: Subsecretaría de Telecomunicaciones (SUBTEL)
* Fuentes regulatorias:
1. [Ley 21.719 - Modifica Ley 19.628 (BCN)](https://www.bcn.cl/leychile/navegar?idNorma=1209272)
   2. Ley 21.663 - Ley Marco de Ciberseguridad (BCN)

------------------------------
## 2. Arquitectura Agéntica (Claude 3.5 Sonnet)## System Prompt (>200 chars)

"Eres un Agente Experto en Cumplimiento de la Ley 21.719 (Protección de Datos Personales de Chile). Tu función es actuar como un 'Privacy Sandbox' que recibe estructuras JSON de organizaciones y las evalúa frente al Título II y IV de la normativa. Debes identificar la base legal (Consentimiento, Contrato o Ley), verificar el principio de minimización de datos y detectar cláusulas abusivas. Tu salida debe ser un esquema estructurado con flags (GREEN, YELLOW, RED) y justificaciones basadas estrictamente en la Wiki Legal de FinteChile."

## Tools Definition (JSON Schema Valid)

   1. validate_retention_period: Evalúa si el plazo de conservación declarado por la empresa es proporcional a la finalidad.
   2. check_legal_consistency: Contrasta si el tipo de dato solicitado (ej. sensible) permite la base legal declarada según el Art. 4 de la Ley 21.719.

[
  {
    "name": "validate_retention_period",
    "description": "Compara el tiempo de retención contra estándares de la Ley 21.719",
    "input_schema": {
      "type": "object",
      "properties": {
        "days": {"type": "integer"},
        "purpose": {"type": "string"}
      },
      "required": ["days", "purpose"]
    }
  },
  {
    "name": "check_legal_consistency",
    "description": "Valida base legal según categoría de dato",
    "input_schema": {
      "type": "object",
      "properties": {
        "data_category": {"type": "string", "enum": ["BASIC", "SENSITIVE", "BIOMETRIC"]},
        "legal_base": {"type": "string"}
      },
      "required": ["data_category", "legal_base"]
    }
  }
]

------------------------------
## 3. Implementación Técnica (Spring Boot)

* Seguridad: Integración OAuth2 con ClaveÚnica para autenticación soberana.
* Vault: Base de datos PostgreSQL segmentada. Los datos sensibles cuentan con encriptación AES-256.
* Compliance: Gestión de plazos ARCO automatizada (15 días hábiles) con alertas de incumplimiento.

------------------------------
## 4. Impacto Ciudadano Real

* Resolución: Resuelve la asimetría de información. Hoy el ciudadano no puede evaluar la legalidad de un contrato en tiempo real; nuestro agente lo hace por él.
* Alcanzabilidad: La solución es integrable mediante APIs con el ecosistema Fintech actual, permitiendo una adopción progresiva post-hackathon.

------------------------------
Siguiente paso: ¿Deseas que preparemos el guion del video demo de 3 minutos para asegurar que el flujo Input→Output sea visible y cumpla con el 13% de la rúbrica?

