package com.universdad.wheels.security;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.universdad.wheels.model.Usuario;
import com.universdad.wheels.repository.UsuarioRepository;



@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthController(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public String register(@RequestBody Usuario usuario) {
        usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
        usuarioRepository.save(usuario);
        return "Usuario registrado correctamente";
    }

    @PostMapping("/login")
    public String login(@RequestBody Usuario usuario) {
        Optional<Usuario> userDB = usuarioRepository.findByCorreo(usuario.getCorreo());
        if (userDB.isPresent() && passwordEncoder.matches(usuario.getContrasena(), userDB.get().getContrasena())) {
            return jwtUtil.generateToken(usuario.getCorreo());
        } else {
            return "Credenciales inválidas";
        }
    }
}

