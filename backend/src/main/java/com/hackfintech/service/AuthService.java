package com.hackfintech.service;

import com.hackfintech.dto.AuthRequest;
import com.hackfintech.dto.AuthResponse;
import com.hackfintech.dto.RegisterRequest;
import com.hackfintech.entity.Usuario;
import com.hackfintech.repository.UsuarioRepository;
import com.hackfintech.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        String rutHash = hashRut(request.getRut());
        
        if (repository.findByRutHash(rutHash).isPresent() || repository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("El usuario ya existe");
        }

        Usuario user = new Usuario();
        user.setNombreCompleto(request.getNombreCompleto());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRutHash(rutHash);
        user.setRutEncriptado(request.getRut()); // CryptoConverter will encrypt it
        user.setRol("ROLE_USER");
        
        repository.save(user);
        
        User securityUser = new User(user.getEmail(), user.getPassword(), Collections.singletonList(new SimpleGrantedAuthority(user.getRol())));
        var jwtToken = jwtService.generateToken(securityUser);
        return AuthResponse.builder().token(jwtToken).build();
    }

    public AuthResponse login(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = repository.findByEmail(request.getEmail())
                .orElseThrow();
        User securityUser = new User(user.getEmail(), user.getPassword(), Collections.singletonList(new SimpleGrantedAuthority(user.getRol())));
        var jwtToken = jwtService.generateToken(securityUser);
        return AuthResponse.builder().token(jwtToken).build();
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

    @org.springframework.transaction.annotation.Transactional
    public AuthResponse mockLoginWithClaveUnica(String rutReal, String nombreCompleto) {
        String rutHash = hashRut(rutReal);
        
        Usuario user = repository.findByRutHash(rutHash).orElseGet(() -> {
            // Auto-registro si no existe en nuestra DB (Simulando obtener datos de /userinfo de ClaveÚnica)
            Usuario newUser = new Usuario();
            newUser.setNombreCompleto(nombreCompleto);
            // Email ficticio ya que CU no siempre lo da
            newUser.setEmail("cu_" + rutHash.substring(0, 8) + "@mock.claveunica.cl");
            newUser.setPassword(passwordEncoder.encode(java.util.UUID.randomUUID().toString())); // Password dummy
            newUser.setRutHash(rutHash);
            newUser.setRutEncriptado(rutReal);
            newUser.setRol("ROLE_USER");
            return repository.save(newUser);
        });

        User securityUser = new User(user.getEmail(), user.getPassword(), Collections.singletonList(new SimpleGrantedAuthority(user.getRol())));
        var jwtToken = jwtService.generateToken(securityUser);
        
        return AuthResponse.builder().token(jwtToken).build();
    }
}
