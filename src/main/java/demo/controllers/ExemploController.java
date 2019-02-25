package demo.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/exemplo")
public class ExemploController {
    @RequestMapping(value="/{nome}", method= RequestMethod.GET)
    @PreAuthorize("hasAnyRole('ADMIN')")
    public String exemplo(@PathVariable("nome") String nome) {
        return "Olá " + nome;
    }
}
