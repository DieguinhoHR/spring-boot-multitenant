package demo.multitenant;

import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

import static demo.multitenant.MultiTenantConstants.DEFAULT_TENANT_ID;

@Component
public class DataSourceBasedMultiTenantConnectionProviderImpl
		extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl {

	@Autowired
	DataSource defaultDS; // Realiza conexão
	@Autowired
	ApplicationContext context; // fornece a configuração para a app
	// Mapeia todas conexões
	static Map<String, DataSource> map = new HashMap<>();

	boolean init = false;

	/**
	 * Carrega o tenant id e a conexão
	 */
	@PostConstruct
  	public void load() {
		map.put(DEFAULT_TENANT_ID, defaultDS);
	}

	/**
	 * Mapeia qualquer conexão
	 *
	 * @return
	 */
	@Override
	protected DataSource selectAnyDataSource() {
		return map.get(DEFAULT_TENANT_ID);
	}

	/**
	 * Seleciona todas conexões
	 *
	 * @param tenantIdentifier
	 * @return
	 */
	@Override
	protected DataSource selectDataSource(String tenantIdentifier) {
		if (!init) {
			init = true;
			TenantDataSource tenantDataSource = context.getBean(TenantDataSource.class);
			map.putAll(tenantDataSource.getAll());
		}
		return map.get(tenantIdentifier);
	}
}