package com.hackfintech.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackfintech.dto.AiAnalysisResultDto;
import com.hackfintech.dto.ConsentRequestProposalDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AiComplianceService {

    private final ObjectMapper objectMapper;

    // TODO: Inyectar RestTemplate o WebClient cuando se integre con OpenAI / Gemini
    // private final RestTemplate restTemplate;
    
    private static final String SYSTEM_PROMPT = """
        Eres un auditor experto en la Ley 21.719 de Protección de Datos Personales de Chile.
        Tu trabajo es analizar solicitudes de acceso a datos de ciudadanos por parte de empresas.

        Reglas de Minimización (Infracciones):
        1. Si la empresa solicita datos sensibles (ej: orientación sexual, religión, afiliación política, datos de salud) y la finalidad es puramente comercial (ej: marketing, evaluación de crédito), marca BANDERA ROJA.
        2. Si el tiempo de retención supera los límites legales para la finalidad descrita (ej: retener datos para un crédito temporal por 10 años), marca BANDERA AMARILLA o ROJA.
        3. Si la empresa obliga a "renunciar a derechos ARCO" o usar frases como "venta de datos a terceros" en su texto legal, marca BANDERA ROJA.

        Formato de Respuesta Requerido (JSON Estricto):
        {
          "flagColor": "RED | YELLOW | GREEN",
          "analysisMessage": "Motivo detallado del dictamen...",
          "infractions": ["Lista de infracciones de minimización (si aplica)"]
        }
        """;

    public AiAnalysisResultDto evaluateRequest(ConsentRequestProposalDto proposal) {
        
        try {
            String jsonPayload = objectMapper.writeValueAsString(proposal);
            
            // Construir el Prompt Completo
            String fullPrompt = SYSTEM_PROMPT + "\n\nAnaliza la siguiente solicitud:\n" + jsonPayload;
            log.info("Prompt generado para el LLM: \n{}", fullPrompt);
            
            // =========================================================
            // AQUI IRÍA LA LLAMADA AL API (Ej. OpenAI API)
            // =========================================================
            /*
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth("tu-api-key");
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            Map<String, Object> body = Map.of(
                "model", "gpt-4-turbo",
                "messages", List.of(
                    Map.of("role", "system", "content", SYSTEM_PROMPT),
                    Map.of("role", "user", "content", jsonPayload)
                )
            );
            
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
            ResponseEntity<String> response = restTemplate.postForEntity("https://api.openai.com/v1/chat/completions", entity, String.class);
            // Parsear JSON de respuesta...
            */
            // =========================================================

            // SIMULACIÓN (Mock) BASADA EN HEURÍSTICA TEMPORAL PARA DEMO:
            return simulateLlmResponse(proposal);

        } catch (JsonProcessingException e) {
            log.error("Error al procesar JSON para IA", e);
            throw new RuntimeException("Error en análisis IA");
        }
    }

    private AiAnalysisResultDto simulateLlmResponse(ConsentRequestProposalDto proposal) {
        List<String> infractions = new ArrayList<>();
        ConsentRequestProposalDto.LegalDeclarationDto legal = proposal.getLegalDeclaration();
        
        if (legal.getTiempoRetencionDias() != null && legal.getTiempoRetencionDias() > 365 * 5) {
            infractions.add("Tiempo de retención excesivo (" + legal.getTiempoRetencionDias() + " días).");
        }
        
        if (legal.getTextoLegalCompleto() != null) {
            String texto = legal.getTextoLegalCompleto().toLowerCase();
            if (texto.contains("venta de datos") || texto.contains("renuncia a derechos")) {
                infractions.add("Cláusula abusiva o renuncia de derechos ARCO.");
            }
        }
        
        boolean pideSensibles = false;
        boolean finalidadComercial = legal.getFinalidadTratamiento() != null && 
                                    (legal.getFinalidadTratamiento().toLowerCase().contains("crédito") || 
                                     legal.getFinalidadTratamiento().toLowerCase().contains("marketing"));
                                     
        if (proposal.getRequestedDataStructure() != null) {
            for (var data : proposal.getRequestedDataStructure()) {
                if (data.getCategoria().contains("SENSIBLE")) pideSensibles = true;
                if (finalidadComercial && data.getCampos() != null) {
                    for (String campo : data.getCampos()) {
                        if (campo.toLowerCase().contains("orientacion_sexual") || campo.toLowerCase().contains("orientación sexual")) {
                            infractions.add("Riesgo de Discriminación: Se solicita Orientación Sexual para fines comerciales/financieros.");
                        }
                    }
                }
            }
        }

        if (!infractions.isEmpty()) {
            return AiAnalysisResultDto.builder()
                    .flagColor("RED")
                    .analysisMessage("[Simulación LLM] Riesgo Crítico Detectado por infracción a la Ley 21.719.")
                    .build(); // Nota: En un DTO real podríamos incluir la lista de infractions
        } else {
            return AiAnalysisResultDto.builder()
                    .flagColor("GREEN")
                    .analysisMessage("[Simulación LLM] La solicitud cumple con los estándares de minimización.")
                    .build();
        }
    }
}
