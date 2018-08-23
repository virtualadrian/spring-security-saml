/*
 * Copyright 2002-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package sample.config;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.saml.provider.config.SamlConfigurationRepository;
import org.springframework.security.saml.provider.service.config.SamlServiceProviderSecurityConfiguration;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends SamlServiceProviderSecurityConfiguration {

	public SecurityConfiguration(AppConfig hostConfiguration) {
		super(hostConfiguration);
	}

	@Override
	public SamlConfigurationRepository<HttpServletRequest> samlConfigurationRepository() {
		return new MultiTenantConfigurationRepository(getHostConfiguration());
	}
}
