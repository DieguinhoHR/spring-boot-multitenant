package demo.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class SenhaUtils {
    /**
     * Gera um hash utilizando o bcrypt
     *
     * @param senha
     * @return
     */
    public static String gerarBCrypt(String senha) {
        if (senha == null) {
            return senha;
        }
        BCryptPasswordEncoder bCryptPasswordEncoder =
                new BCryptPasswordEncoder();
        return bCryptPasswordEncoder.encode(senha);
    }

    /**
     * Verifica se a senha é válida
     *
     * @param senha
     * @param senhaEncoded
     * @return
     */
    public static boolean senhaValida(String senha, String senhaEncoded) {
        BCryptPasswordEncoder bCryptEncoder = new BCryptPasswordEncoder();

        return bCryptEncoder.matches(senha, senhaEncoded);
    }
}
