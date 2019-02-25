package demo.security.services;

import demo.security.entities.Usuario;

import java.util.Optional;

public interface UsuarioService {
    /**
     * Busca e retorna um usuário dado em email
     *
     * @param email
     * @return
     */
    Optional<Usuario> buscaPorEmail(String email);
}
