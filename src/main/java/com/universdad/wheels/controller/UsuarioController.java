package com.universdad.wheels.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.universdad.wheels.model.Usuario;
import com.universdad.wheels.repository.UsuarioRepository;
import com.universdad.wheels.security.JwtUtil;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin("*")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    // 🔹 Registro de usuario
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Usuario usuario) {
        if (usuarioRepository.findByCorreo(usuario.getCorreo()).isPresent()) {
            return ResponseEntity.badRequest().body("El email ya está registrado");
        }

        // Encriptar contraseña
        usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));

        // Asignar rol por defecto si no tiene
        if (usuario.getRol() == null) {
            usuario.setRol(Usuario.Rol.CONDUCTOR);
        }

        Usuario saved = usuarioRepository.save(usuario);
        return ResponseEntity.ok(saved);
    }

    // 🔹 Login de usuario
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Usuario request) {
        Optional<Usuario> userOpt = usuarioRepository.findByCorreo(request.getCorreo());

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(401).body("Credenciales inválidas");
        }

        Usuario usuario = userOpt.get();

        if (!passwordEncoder.matches(request.getContrasena(), usuario.getContrasena())) {
            return ResponseEntity.status(401).body("Credenciales inválidas");
        }

        // Convertir rol a String para el token
        String rol = usuario.getRol() != null ? usuario.getRol().toString() : "USER";

        // Generar token con ID, correo y rol
        String token = jwtUtil.generateToken(usuario.getCorreo(), usuario.getId(), rol);

        return ResponseEntity.ok(token);
    }

    // 🔹 Obtener información del usuario desde el token
    @GetMapping("/me")
    public ResponseEntity<?> getProfile(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String email = jwtUtil.extractUsername(token);

        Optional<Usuario> userOpt = usuarioRepository.findByCorreo(email);

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Usuario no encontrado");
        }

        return ResponseEntity.ok(userOpt.get());
    }
}
