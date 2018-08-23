package sample.config;

import java.util.LinkedList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.saml.SamlException;
import org.springframework.security.saml.provider.SamlServerConfiguration;
import org.springframework.security.saml.provider.config.StaticSamlConfigurationRepository;
import org.springframework.security.saml.provider.service.config.ExternalIdentityProviderConfiguration;
import org.springframework.security.saml.provider.service.config.LocalServiceProviderConfiguration;
import org.springframework.security.saml.saml2.metadata.NameId;

public class MultiTenantConfigurationRepository extends StaticSamlConfigurationRepository {

	public MultiTenantConfigurationRepository(SamlServerConfiguration configuration) {
		super(configuration);
	}

	@Override
	public SamlServerConfiguration getServerConfiguration(HttpServletRequest request) {
		SamlServerConfiguration config = super.getServerConfiguration(request);
		if (request.getServerName().contains("one.localhost")) {
			return addExternalProvider(
				config,
				"https://dev-254964.oktapreview.com/app/exkg0xlsuaOZSlbu40h7/sso/saml/metadata",
				"Okta Tenant One",
				"okta-tenant-one-app"
			);
		}
		else if (request.getServerName().contains("two.localhost")) {
			return addExternalProvider(
				config,
				"https://dev-254964.oktapreview.com/app/exkg0xownnq6nDJva0h7/sso/saml/metadata",
				"Okta Tenant Two",
				"okta-tenant-two-app"
			);
		}
		else {
			return config;
		}
	}

	private SamlServerConfiguration addExternalProvider(SamlServerConfiguration config,
														String metadata,
														String linktext,
														String alias) {
		config = clone(config);
		List<ExternalIdentityProviderConfiguration> providers =
			new LinkedList<>(config.getServiceProvider().getProviders());
		ExternalIdentityProviderConfiguration idp = new ExternalIdentityProviderConfiguration()
			.setAlias(alias)
			.setAssertionConsumerServiceIndex(0)
			.setNameId(NameId.EMAIL)
			.setMetadata(metadata)
			.setLinktext(linktext);
		providers.add(idp);
		config.getServiceProvider().setProviders(providers);
		return config;
	}

	private SamlServerConfiguration clone(SamlServerConfiguration config) {
		try {
			LocalServiceProviderConfiguration provider = config.getServiceProvider();
			LocalServiceProviderConfiguration newProvider = (LocalServiceProviderConfiguration)provider.clone();
			return new SamlServerConfiguration()
				.setServiceProvider(newProvider)
				.setIdentityProvider(config.getIdentityProvider())
				.setNetwork(config.getNetwork());
		} catch (Exception x) {
			throw new SamlException(x);
		}
	}

	}
