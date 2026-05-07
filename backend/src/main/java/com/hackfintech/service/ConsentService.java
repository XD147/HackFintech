package com.hackfintech.service;

import com.hackfintech.dto.ConsentResponse;
import com.hackfintech.entity.AccesoOrganizacion;
import com.hackfintech.entity.EstadoPermiso;
import com.hackfintech.entity.Usuario;
import com.hackfintech.repository.AccesoOrganizacionRepository;
import com.hackfintech.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConsentService {

    private final AccesoOrganizacionRepository accesoRepository;
    private final UsuarioRepository usuarioRepository;

    public List<ConsentResponse> getConsentsForUser(String userEmail) {
        Usuario usuario = usuarioRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<AccesoOrganizacion> accesos = accesoRepository.findByUsuario(usuario);

        return accesos.stream().map(acceso -> ConsentResponse.builder()
                .id(acceso.getId())
                .nombreOrganizacion(acceso.getOrganizacion().getNombre())
                .industriaOrganizacion(acceso.getOrganizacion().getIndustria())
                .permitirDatosBasicos(acceso.isPermitirDatosBasicos())
                .permitirDatosSensibles(acceso.isPermitirDatosSensibles())
                .permitirGeolocalizacion(acceso.isPermitirGeolocalizacion())
                .estado(acceso.getEstado().name())
                .fechaOtorgamiento(acceso.getFechaOtorgamiento())
                .build()
        ).collect(Collectors.toList());
    }

    public void revokeConsent(UUID consentId, String userEmail) {
        Usuario usuario = usuarioRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        AccesoOrganizacion acceso = accesoRepository.findById(consentId)
                .orElseThrow(() -> new RuntimeException("Consent not found"));

        if (!acceso.getUsuario().getId().equals(usuario.getId())) {
            throw new RuntimeException("No autorizado para revocar este consentimiento");
        }

        acceso.setEstado(EstadoPermiso.REVOCADO);
        accesoRepository.save(acceso);
        
        // Disparar Webhook Asíncrono
        String webhookUrl = acceso.getOrganizacion().getWebhookUrlRevocacion();
        if (webhookUrl != null && !webhookUrl.isEmpty()) {
            java.util.concurrent.CompletableFuture.runAsync(() -> {
                try {
                    // TODO: Reemplazar con RestTemplate o WebClient
                    // log.info("Enviando webhook de revocación a: " + webhookUrl);
                    System.out.println("WEBHOOK NOTIFICATION: Notifying " + webhookUrl + " about revocation for User Hash: " + usuario.getRutHash());
                    
                    // Payload propuesto:
                    // { "event": "CONSENT_REVOKED", "rutHash": "...", "timestamp": "..." }
                } catch (Exception e) {
                    System.err.println("Error al enviar webhook a " + webhookUrl);
                }
            });
        }
    }
}
