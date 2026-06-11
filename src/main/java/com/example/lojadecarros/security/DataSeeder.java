package com.example.lojadecarros.security;

import com.example.lojadecarros.model.Role;
import com.example.lojadecarros.model.Usuario;
import com.example.lojadecarros.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        if (usuarioRepository.count() == 0) {
            Usuario admin = new Usuario();
            admin.setUsername("admin");

            admin.setPassword(passwordEncoder.encode("123456"));
            admin.setRole(Role.ROLE_GERENTE);

            usuarioRepository.save(admin);
            System.out.println("✅ Usuário Gerente criado com sucesso! Login: admin / Senha: 123456");
        }
    }
}