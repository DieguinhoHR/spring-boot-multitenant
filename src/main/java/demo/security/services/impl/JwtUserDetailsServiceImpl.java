package demo.security.services.impl;

import demo.security.JwtUserFactory;
import demo.security.entities.Usuario;
import demo.security.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JwtUserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UsuarioService usuarioService;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        Optional<Usuario> usuario = usuarioService.buscaPorEmail(username);

        if (usuario.isPresent()) {
            return JwtUserFactory.create(usuario.get());
        }
        throw new UsernameNotFoundException("Email não encontrado.");
    }
}
