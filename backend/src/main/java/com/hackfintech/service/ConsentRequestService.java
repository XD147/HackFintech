package com.hackfintech.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackfintech.dto.AiAnalysisResultDto;
import com.hackfintech.dto.ConsentRequestProposalDto;
import com.hackfintech.entity.*;
import com.hackfintech.repository.AccesoOrganizacionRepository;
import com.hackfintech.repository.OrganizacionRepository;
import com.hackfintech.repository.SolicitudConsentimientoRepository;
import com.hackfintech.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConsentRequestService {

    private final SolicitudConsentimientoRepository solicitudRepository;
    private final OrganizacionRepository organizacionRepository;
    private final UsuarioRepository usuarioRepository;
    private final AccesoOrganizacionRepository accesoRepository;
    private final AiComplianceService aiComplianceService;
    private final ObjectMapper objectMapper;

    public AiAnalysisResultDto processNewRequest(ConsentRequestProposalDto proposal, String orgEmail) throws JsonProcessingException {
        // Validar Organización
        Organizacion organizacion = organizacionRepository.findAll().stream()
                .filter(o -> o.getEmailContacto().equals(orgEmail))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Organización no encontrada o no autorizada"));

        // Validar que el rut exista (opcional, para no generar spam)
        String rutHash = hashRut(proposal.getRutCiudadano());
        usuarioRepository.findByRutHash(rutHash).orElseThrow(() -> new RuntimeException("Ciudadano no encontrado"));

        // Ejecutar Motor de IA
        AiAnalysisResultDto aiResult = aiComplianceService.evaluateRequest(proposal);

        // Persistir la solicitud PENDIENTE
        SolicitudConsentimiento solicitud = new SolicitudConsentimiento();
        solicitud.setOrganizacion(organizacion);
        solicitud.setRutCiudadanoHash(rutHash);
        solicitud.setEstado(EstadoSolicitud.PENDIENTE);
        solicitud.setAiFlag(aiResult.getFlagColor());
        solicitud.setAiAnalysisResumen(aiResult.getAnalysisMessage());
        
        // Convertir la estructura a JSON
        String jsonStr = objectMapper.writeValueAsString(proposal);
        solicitud.setProposalJson(jsonStr);
        
        if (proposal.getRequestHeader() != null) {
            solicitud.setRequestType(proposal.getRequestHeader().getRequestType() != null ? proposal.getRequestHeader().getRequestType() : "NORMAL");
            solicitud.setSourceOrganizationId(proposal.getRequestHeader().getSourceOrganizationId());
        } else {
            solicitud.setRequestType("NORMAL");
        }

        solicitudRepository.save(solicitud);

        aiResult.setSolicitudId(solicitud.getId());
        return aiResult; // Retorna el semáforo a la organización para feedback inmediato
    }

    public List<SolicitudConsentimiento> getPendingRequestsForUser(String userEmail) {
        Usuario usuario = usuarioRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return solicitudRepository.findByRutCiudadanoHashAndEstado(usuario.getRutHash(), EstadoSolicitud.PENDIENTE);
    }

    public com.hackfintech.dto.ConsentReceiptDto approveRequest(UUID solicitudId, String userEmail) throws JsonProcessingException {
        Usuario usuario = usuarioRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        SolicitudConsentimiento solicitud = solicitudRepository.findById(solicitudId)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));

        if (!solicitud.getRutCiudadanoHash().equals(usuario.getRutHash())) {
            throw new RuntimeException("No autorizado");
        }

        // Deserializar el JSON para inferir qué permisos crear
        ConsentRequestProposalDto proposal = objectMapper.readValue(solicitud.getProposalJson(), ConsentRequestProposalDto.class);

        boolean basicos = false;
        boolean sensibles = false;

        for (ConsentRequestProposalDto.RequestedDataStructureDto data : proposal.getRequestedDataStructure()) {
            if (data.getCategoria().contains("BASICO") || data.getCategoria().contains("SOCIOECONOMICO")) {
                basicos = true;
            }
            if (data.getCategoria().contains("SENSIBLE")) {
                sensibles = true;
            }
        }
        
        String timestamp = java.time.Instant.now().toString();
        String rawData = solicitudId.toString() + usuario.getRutHash() + solicitud.getOrganizacion().getId().toString() + timestamp + "SECRET_MOCK";
        String receiptHash = hashRut(rawData); // Usamos hashRut ya que genera SHA256 base64

        solicitud.setEstado(EstadoSolicitud.APROBADA);
        solicitud.setReceiptHash(receiptHash);
        solicitudRepository.save(solicitud);

        // Crear o actualizar AccesoOrganizacion
        AccesoOrganizacion acceso = new AccesoOrganizacion();
        acceso.setUsuario(usuario);
        acceso.setOrganizacion(solicitud.getOrganizacion());
        acceso.setPermitirDatosBasicos(basicos);
        acceso.setPermitirDatosSensibles(sensibles);
        acceso.setPermitirGeolocalizacion(false);
        acceso.setEstado(EstadoPermiso.ACTIVO);
        acceso.setReceiptHash(receiptHash);
        
        accesoRepository.save(acceso);
        
        return com.hackfintech.dto.ConsentReceiptDto.builder()
                .receiptId(solicitudId.toString())
                .receiptHash(receiptHash)
                .timestamp(timestamp)
                .orgName(solicitud.getOrganizacion().getNombre())
                .message("Consentimiento registrado criptográficamente.")
                .build();
    }

    public void rejectRequest(UUID solicitudId, String userEmail) {
        Usuario usuario = usuarioRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        SolicitudConsentimiento solicitud = solicitudRepository.findById(solicitudId)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));

        if (!solicitud.getRutCiudadanoHash().equals(usuario.getRutHash())) {
            throw new RuntimeException("No autorizado");
        }

        solicitud.setEstado(EstadoSolicitud.RECHAZADA);
        solicitudRepository.save(solicitud);
    }

    private String hashRut(String rut) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(rut.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encodedhash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing RUT", e);
        }
    }
}
