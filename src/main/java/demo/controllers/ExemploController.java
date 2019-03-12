package demo.controllers;

import demo.multitenant.TenantDataSource;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static demo.multitenant.MultiTenantConstants.CURRENT_TENANT_IDENTIFIER;

@RestController
@RequestMapping("/api/exemplo")
public class ExemploController {

    @Autowired
    ApplicationContext context; // fornece a configuração para a app

    @RequestMapping(value="/{nome}", method= RequestMethod.GET)
    // @PreAuthorize("hasAnyRole('ADMIN')")
    public String exemplo(@PathVariable("nome") String nome) {

        MultiTenantConnectionProvider x = context.getBean(MultiTenantConnectionProvider.class);
        try {
            Connection connection =
                    x.getConnection(String.valueOf(RequestContextHolder.getRequestAttributes().getAttribute(CURRENT_TENANT_IDENTIFIER, RequestAttributes.SCOPE_REQUEST)));
            try (Statement statement = connection.createStatement()) {
                ResultSet resultSet = statement.executeQuery("select * from usuario");
                nome += " " + resultSet.getObject(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "Olá " + nome;
    }
}
