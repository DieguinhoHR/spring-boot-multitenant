package demo.security.services.impl;

import demo.security.entities.Usuario;
import demo.security.repositories.UsuarioRepository;
import demo.security.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    public Optional<Usuario> buscaPorEmail(String email) {
        return Optional.ofNullable(usuarioRepository.findByEmail(email));
    }
}
