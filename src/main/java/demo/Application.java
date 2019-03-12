package demo;

import demo.multitenant.DataSourceBasedMultiTenantConnectionProviderImpl;
import demo.multitenant.MultiTenantConstants;
import demo.multitenant.MultiTenantFilter;
import demo.multitenant.TenantIdentifierResolver;
import demo.security.entities.Usuario;
import demo.security.enums.PerfilEnum;
import demo.security.repositories.UsuarioRepository;
import demo.utils.SenhaUtils;
import org.hibernate.MultiTenancyStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.persistence.PersistenceContext;
import javax.servlet.Filter;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class Application {
    @Autowired
    DataSourceBasedMultiTenantConnectionProviderImpl dsProvider;
    @Autowired
    TenantIdentifierResolver tenantResolver;
    @Autowired
    AutowireCapableBeanFactory beanFactory;

    @Autowired
    UsuarioRepository usuarioRepository;

    /**
     * Creates the default "master" datasource
     * @return
     */
    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    /**
     * Configures the Hibernate JPA service with multi-tenant support enabled.
     * @param builder
     * @return
     */
    @PersistenceContext
    @Primary
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder) {
        Map<String, Object> props = new HashMap<>();
        props.put("hibernate.multiTenancy", MultiTenancyStrategy.DATABASE.name());
        props.put("hibernate.multi_tenant_connection_provider", dsProvider);
        props.put("hibernate.tenant_identifier_resolver", tenantResolver);

        LocalContainerEntityManagerFactoryBean result = builder.dataSource(dataSource())
                .persistenceUnit(MultiTenantConstants.TENANT_KEY)
                .properties(props)
                .packages("demo").build();
        result.setJpaVendorAdapter(jpaVendorAdapter());
        return result;
    }

    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
        hibernateJpaVendorAdapter.setShowSql(true);
        // Generate DDL is not supported in Hibernate to multi-tenancy features
        // https://hibernate.atlassian.net/browse/HHH-7395
        hibernateJpaVendorAdapter.setGenerateDdl(false);
        return hibernateJpaVendorAdapter;
    }

    @Bean
    public FilterRegistrationBean myFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        Filter tenantFilter = new MultiTenantFilter();
        beanFactory.autowireBean(tenantFilter);
        registration.setFilter(tenantFilter);
        registration.addUrlPatterns("/*");
        return registration;
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {

            Usuario usuario = new Usuario();
            usuario.setEmail("usuario@gmail.com");
            usuario.setPerfil(PerfilEnum.ROLE_USUARIO);
            usuario.setSenha(SenhaUtils.gerarBCrypt("123456"));
            usuario.setTenantId("2");
            this.usuarioRepository.save(usuario);

            Usuario admin = new Usuario();
            admin.setEmail("admin@gmail.com");
            admin.setPerfil(PerfilEnum.ROLE_ADMIN);
            admin.setSenha(SenhaUtils.gerarBCrypt("123456"));
            admin.setTenantId("2");
            this.usuarioRepository.save(admin);

        };
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}