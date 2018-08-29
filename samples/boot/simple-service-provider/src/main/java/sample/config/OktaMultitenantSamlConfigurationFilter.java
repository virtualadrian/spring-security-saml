package sample.config;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.saml.provider.SamlServerConfiguration;
import org.springframework.security.saml.provider.config.ThreadLocalSamlConfigurationFilter;
import org.springframework.security.saml.provider.config.ThreadLocalSamlConfigurationRepository;

public class OktaMultitenantSamlConfigurationFilter extends ThreadLocalSamlConfigurationFilter {

	private MultiTenantConfigurationRepository repository = new MultiTenantConfigurationRepository();

	public OktaMultitenantSamlConfigurationFilter(ThreadLocalSamlConfigurationRepository repository) {
		super(repository);
	}

	@Override
	protected SamlServerConfiguration getConfiguration(HttpServletRequest request) {
		SamlServerConfiguration result = super.getConfiguration(request);
		return repository.getServerConfiguration(request, result);
	}
}
