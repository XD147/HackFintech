package com.hackfintech.service;

import com.hackfintech.dto.DataRequestDto;
import com.hackfintech.dto.DataResponseDto;
import com.hackfintech.entity.*;
import com.hackfintech.repository.AccesoOrganizacionRepository;
import com.hackfintech.repository.LogAccesoDatosRepository;
import com.hackfintech.repository.OrganizacionRepository;
import com.hackfintech.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DataAccessService {

    private final UsuarioRepository usuarioRepository;
    private final OrganizacionRepository organizacionRepository;
    private final AccesoOrganizacionRepository accesoRepository;
    private final LogAccesoDatosRepository logRepository;
    private final com.hackfintech.security.JwtService jwtService;

    public DataResponseDto requestData(String token) {
        // 1. Extraer Claims del Token Dinámico
        io.jsonwebtoken.Claims claims = jwtService.extractAllClaims(token);
        String type = claims.get("type", String.class);
        if (!"B2B_DATA_ACCESS".equals(type)) {
            throw new RuntimeException("Token inválido para consumo de datos.");
        }

        java.util.UUID orgId = java.util.UUID.fromString(claims.get("orgId", String.class));
        java.util.UUID consentId = java.util.UUID.fromString(claims.get("consentId", String.class));
        String rutCiudadano = claims.get("rutCiudadano", String.class);
        String finalidad = claims.get("finalidad", String.class);
        List<?> scopesRaw = claims.get("scopes", List.class);
        List<String> scopes = (scopesRaw != null) ? scopesRaw.stream().map(Object::toString).toList() : List.of();

        // 2. Identificar Entidades
        Organizacion organizacion = organizacionRepository.findById(orgId)
                .orElseThrow(() -> new RuntimeException("Organización no encontrada."));

        String rutHash = hashRut(rutCiudadano);
        Usuario usuario = usuarioRepository.findByRutHash(rutHash)
                .orElseThrow(() -> new RuntimeException("Ciudadano no encontrado"));

        // 3. INTROSPECCIÓN (Revocación Instantánea)
        AccesoOrganizacion accesoActual = accesoRepository.findById(consentId)
                .orElseThrow(() -> new RuntimeException("Consentimiento no encontrado."));

        if (accesoActual.getEstado() != EstadoPermiso.ACTIVO) {
            throw new RuntimeException("Acceso DENEGADO: El ciudadano revocó este consentimiento.");
        }

        // 4. Evaluar Granularidad desde el Token (Scopes)
        DataResponseDto.DataResponseDtoBuilder responseBuilder = DataResponseDto.builder();
        String tipoAcceso = "Ninguno";

        if (scopes.contains("read:perfil_basico")) {
            responseBuilder.nombreCompleto(usuario.getNombreCompleto())
                           .email(usuario.getEmail());
            tipoAcceso = "BASICOS";
        }

        if (scopes.contains("read:salud_restringido") || scopes.contains("read:geolocalizacion")) {
            DatosSensibles sensibles = usuario.getDatosSensibles();
            if (sensibles != null) {
                if (scopes.contains("read:salud_restringido")) {
                    responseBuilder.informacionSalud(sensibles.getInformacionSalud());
                    tipoAcceso = tipoAcceso.equals("Ninguno") ? "SENSIBLES" : "BASICOS_Y_SENSIBLES";
                }
                // (Geolocalización omitida por simplicidad en la respuesta DTO)
            }
        }

        // 5. REGISTRO INMUTABLE (Auditoría) con la Finalidad del Token
        LogAccesoDatos log = new LogAccesoDatos();
        log.setUsuario(usuario);
        log.setOrganizacion(organizacion);
        log.setTipoDatoConsultado(tipoAcceso);
        log.setJustificacionLegal("Token scopes: " + String.join(",", scopes) + " | Finalidad: " + finalidad);
        logRepository.save(log);

        return responseBuilder.build();
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
