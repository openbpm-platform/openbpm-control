package io.openbpm.control.configuration;

import com.vaadin.flow.spring.security.VaadinSavedRequestAwareAuthenticationSuccessHandler;
import io.jmix.core.JmixOrder;
import io.jmix.oidc.userinfo.JmixOidcUserService;
import io.jmix.securityflowui.security.FlowuiVaadinWebSecurity;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;

/**
 * Security configuration for OpenID Connect (OIDC) authentication mode.
 * <p>
 * This configuration is activated automatically when the application property
 * <pre>
 * openbpm.control.security.login-mode=oidc
 * </pre>
 * is set.
 * <p>
 * Extends {@link FlowuiVaadinWebSecurity} to integrate with Jmix FlowUI and configure
 * {@link HttpSecurity} for OIDC login and logout flows.
 * <ul>
 *     <li>Configures {@link JmixOidcUserService} for user information retrieval.</li>
 *     <li>Uses {@link VaadinSavedRequestAwareAuthenticationSuccessHandler} as a success handler.</li>
 *     <li>Sets up {@link OidcClientInitiatedLogoutSuccessHandler} to handle logout via the OIDC provider.</li>
 * </ul>
 */
@Configuration
@EnableWebSecurity
@Order(JmixOrder.HIGHEST_PRECEDENCE + 100)
@ConditionalOnProperty(name = "openbpm.control.security.login-mode", havingValue = "oidc")
public class OidcSecurityConfiguration extends FlowuiVaadinWebSecurity {

    protected final JmixOidcUserService jmixOidcUserService;
    protected final ClientRegistrationRepository clientRegistrationRepository;

    /**
     * Creates a new OIDC security configuration.
     *
     * @param jmixOidcUserService          the service used to load user information from the OIDC provider
     * @param clientRegistrationRepository the client registration repository used for OIDC logout handling
     */
    public OidcSecurityConfiguration(JmixOidcUserService jmixOidcUserService,
                                     ClientRegistrationRepository clientRegistrationRepository) {
        this.jmixOidcUserService = jmixOidcUserService;
        this.clientRegistrationRepository = clientRegistrationRepository;
    }


    /**
     * Configures {@link HttpSecurity} for OIDC authentication and logout.
     *
     * @param http the {@link HttpSecurity} to modify
     * @throws Exception if an error occurs while configuring security
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);

        http.oauth2Login(oauth2 -> oauth2
                .userInfoEndpoint(userInfo -> userInfo.oidcUserService(jmixOidcUserService))
                .successHandler(new VaadinSavedRequestAwareAuthenticationSuccessHandler())
        );

        OidcClientInitiatedLogoutSuccessHandler oidcLogoutHandler =
                new OidcClientInitiatedLogoutSuccessHandler(clientRegistrationRepository);
        oidcLogoutHandler.setPostLogoutRedirectUri("{baseUrl}/login");

        http.logout(logout -> logout.logoutSuccessHandler(oidcLogoutHandler));
    }
}