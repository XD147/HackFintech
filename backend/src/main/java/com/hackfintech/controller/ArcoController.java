package com.hackfintech.controller;

import com.hackfintech.dto.SolicitudArcoDto;
import com.hackfintech.entity.*;
import com.hackfintech.repository.SolicitudArcoRepository;
import com.hackfintech.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/arco")
@RequiredArgsConstructor
public class ArcoController {

    private final SolicitudArcoRepository arcoRepository;
    private final UsuarioRepository usuarioRepository;

    @GetMapping("/my-requests")
    public ResponseEntity<List<SolicitudArcoDto>> getMyArcoRequests(Authentication authentication) {
        String email = authentication.getName();
        
        // Get user's RUT hash
        var user = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String rutHash = user.getRutHash();

        List<SolicitudArco> solicitudes = arcoRepository.findByRutCiudadanoHash(rutHash);

        List<SolicitudArcoDto> result = solicitudes.stream().map(sol -> {
            long diasRestantes = ChronoUnit.DAYS.between(LocalDateTime.now(), sol.getFechaLimiteRespuesta());
            return SolicitudArcoDto.builder()
                    .id(sol.getId().toString())
                    .tipo(sol.getTipo().name())
                    .estado(sol.getEstado().name())
                    .fechaSolicitud(sol.getFechaSolicitud())
                    .fechaLimiteRespuesta(sol.getFechaLimiteRespuesta())
                    .diasRestantes(diasRestantes)
                    .detalleSolicitud(sol.getDetalleSolicitud())
                    .build();
        }).collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }
}
