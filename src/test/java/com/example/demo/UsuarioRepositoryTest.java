package com.example.demo;

import com.example.demo.entity.Usuario;
import com.example.demo.repository.UsuarioRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    void testSaveAndFind() {
        Usuario user = new Usuario("Yuri", "yuri@email.com",
                "1234", "Aluno");

        Usuario saved = usuarioRepository.save(user);
        Assertions.assertNotNull(saved.getId());

        var all = usuarioRepository.findAll();
        Assertions.assertTrue(all.size() > 0);

        var byId = usuarioRepository.findById(saved.getId());
        Assertions.assertTrue(byId.isPresent());
    }
}
