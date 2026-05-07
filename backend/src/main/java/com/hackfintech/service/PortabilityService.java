package com.hackfintech.service;

import com.hackfintech.dto.PortabilityRequestDto;
import com.hackfintech.dto.OrgTokenResponse;
import com.hackfintech.entity.Organizacion;
import com.hackfintech.entity.Usuario;
import com.hackfintech.repository.OrganizacionRepository;
import com.hackfintech.repository.UsuarioRepository;
import com.hackfintech.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PortabilityService {

    private final UsuarioRepository usuarioRepository;
    private final OrganizacionRepository organizacionRepository;
    private final JwtService jwtService;

    private final com.hackfintech.repository.SolicitudConsentimientoRepository solicitudRepository;

    // 5 minutos de validez para el token de portabilidad
    private static final long PORTABILITY_TOKEN_EXPIRATION = 5 * 60 * 1000;

    public OrgTokenResponse generatePortabilityToken(java.util.UUID solicitudId, String orgEmail) {
        com.hackfintech.entity.SolicitudConsentimiento solicitud = solicitudRepository.findById(solicitudId)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));

        if (!solicitud.getOrganizacion().getEmailContacto().equals(orgEmail)) {
            throw new RuntimeException("No autorizado. Esta solicitud no pertenece a su organización.");
        }

        if (!"PORTABILITY".equals(solicitud.getRequestType())) {
            throw new RuntimeException("La solicitud no es de tipo PORTABILIDAD.");
        }

        if (solicitud.getEstado() != com.hackfintech.entity.EstadoSolicitud.APROBADA) {
            throw new RuntimeException("La solicitud de portabilidad aún no ha sido aprobada por el usuario.");
        }

        Organizacion targetOrg = solicitud.getOrganizacion();
        Organizacion sourceOrg = organizacionRepository.findById(solicitud.getSourceOrganizationId())
                .orElseThrow(() -> new RuntimeException("Organización origen no encontrada"));

        Map<String, Object> claims = new HashMap<>();
        claims.put("type", "PORTABILITY_TRANSFER");
        claims.put("sourceOrgId", sourceOrg.getId().toString());
        claims.put("targetOrgId", targetOrg.getId().toString());
        claims.put("rutHash", solicitud.getRutCiudadanoHash());
        claims.put("receiptHash", solicitud.getReceiptHash());

        String token = jwtService.generateGranularToken(claims, targetOrg.getEmailContacto(), PORTABILITY_TOKEN_EXPIRATION);

        return OrgTokenResponse.builder()
                .accessToken(token)
                .expiresIn(PORTABILITY_TOKEN_EXPIRATION / 1000)
                .tokenType("Bearer")
                .build();
    }
}
