package com.hackfintech.config;

import com.hackfintech.entity.Organizacion;
import com.hackfintech.entity.Usuario;
import com.hackfintech.repository.OrganizacionRepository;
import com.hackfintech.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DemoDataLoader {

    private final UsuarioRepository usuarioRepository;
    private final OrganizacionRepository organizacionRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner loadDemoData() {
        return args -> {
            // 1. Crear usuario ciudadano demo si no existe
            String citizenEmail = "citizen@test.com";
            if (usuarioRepository.findByEmail(citizenEmail).isEmpty()) {
                String rut = "12345678-9";
                String rutHash = hashRut(rut);
                
                Usuario citizen = new Usuario();
                citizen.setNombreCompleto("Juan Pérez");
                citizen.setEmail(citizenEmail);
                citizen.setPassword(passwordEncoder.encode("password123"));
                citizen.setRutHash(rutHash);
                citizen.setRutEncriptado(rut);
                citizen.setRol("ROLE_USER");
                usuarioRepository.save(citizen);
                log.info("✅ Usuario ciudadano demo creado: {}", citizenEmail);
            }

            // 2. Crear usuario organización demo si no existe
            String orgEmail = "org@test.com";
            if (usuarioRepository.findByEmail(orgEmail).isEmpty()) {
                String orgRut = "76000000-1";
                String orgRutHash = hashRut(orgRut);

                Usuario orgUser = new Usuario();
                orgUser.setNombreCompleto("Admin Global Retail");
                orgUser.setEmail(orgEmail);
                orgUser.setPassword(passwordEncoder.encode("password123"));
                orgUser.setRutHash(orgRutHash);
                orgUser.setRutEncriptado(orgRut);
                orgUser.setRol("ROLE_ORG");
                usuarioRepository.save(orgUser);
                log.info("✅ Usuario organización demo creado: {}", orgEmail);
            }

            // 3. Crear organización demo si no existe
            String orgRut = "76000000-1";
            if (organizacionRepository.findByRut(orgRut).isEmpty()) {
                Organizacion org = new Organizacion();
                org.setRut(orgRut);
                org.setNombre("Global Retail S.A.");
                org.setIndustria("Retail");
                org.setEmailContacto(orgEmail);
                organizacionRepository.save(org);
                log.info("✅ Organización demo creada: Global Retail S.A.");
            }

            log.info("🚀 Datos demo cargados correctamente.");
        };
    }

    private String hashRut(String rut) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encoded = digest.digest(rut.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encoded);
        } catch (Exception e) {
            throw new RuntimeException("Error hashing RUT", e);
        }
    }
}
