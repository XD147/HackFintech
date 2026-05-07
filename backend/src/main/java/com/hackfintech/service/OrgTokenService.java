package com.hackfintech.service;

import com.hackfintech.dto.OrgTokenRequest;
import com.hackfintech.dto.OrgTokenResponse;
import com.hackfintech.entity.AccesoOrganizacion;
import com.hackfintech.entity.EstadoPermiso;
import com.hackfintech.entity.Organizacion;
import com.hackfintech.entity.Usuario;
import com.hackfintech.repository.AccesoOrganizacionRepository;
import com.hackfintech.repository.OrganizacionRepository;
import com.hackfintech.repository.UsuarioRepository;
import com.hackfintech.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrgTokenService {

    private final UsuarioRepository usuarioRepository;
    private final OrganizacionRepository organizacionRepository;
    private final AccesoOrganizacionRepository accesoRepository;
    private final JwtService jwtService;

    // 15 minutos en milisegundos para tokens de consumo
    private static final long SHORT_LIVED_TOKEN_EXPIRATION = 15 * 60 * 1000;

    public OrgTokenResponse generateToken(OrgTokenRequest request, String orgEmail) {
        // 1. Validar Organización
        Organizacion organizacion = organizacionRepository.findAll().stream()
                .filter(o -> o.getEmailContacto().equals(orgEmail))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Organización no autorizada"));

        // 2. Validar Ciudadano
        String rutHash = hashRut(request.getRutCiudadano());
        Usuario usuario = usuarioRepository.findByRutHash(rutHash)
                .orElseThrow(() -> new RuntimeException("Ciudadano no encontrado"));

        // 3. Verificar Consentimiento ACTIVO
        AccesoOrganizacion accesoActivo = accesoRepository.findByUsuario(usuario).stream()
                .filter(a -> a.getOrganizacion().getId().equals(organizacion.getId()) 
                        && a.getEstado() == EstadoPermiso.ACTIVO)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Acceso DENEGADO: No existe consentimiento activo."));

        // 4. Preparar Scopes dinámicos
        List<String> scopes = new ArrayList<>();
        if (accesoActivo.isPermitirDatosBasicos()) scopes.add("read:perfil_basico");
        if (accesoActivo.isPermitirDatosSensibles()) scopes.add("read:salud_restringido");
        if (accesoActivo.isPermitirGeolocalizacion()) scopes.add("read:geolocalizacion");

        if (scopes.isEmpty()) {
            throw new RuntimeException("El consentimiento está activo pero no tiene permisos asignados.");
        }

        // 5. Generar JWT
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", "B2B_DATA_ACCESS");
        claims.put("orgId", organizacion.getId().toString());
        claims.put("rutCiudadano", request.getRutCiudadano());
        claims.put("consentId", accesoActivo.getId().toString());
        claims.put("finalidad", request.getFinalidad());
        claims.put("scopes", scopes);

        String token = jwtService.generateGranularToken(claims, orgEmail, SHORT_LIVED_TOKEN_EXPIRATION);

        return OrgTokenResponse.builder()
                .accessToken(token)
                .expiresIn(SHORT_LIVED_TOKEN_EXPIRATION / 1000)
                .tokenType("Bearer")
                .build();
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
