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

@Configuration
@EnableWebSecurity
@Order(JmixOrder.HIGHEST_PRECEDENCE + 100)
@ConditionalOnProperty(name = "app.security.login-mode", havingValue = "oidc")
public class OidcSecurityConfiguration extends FlowuiVaadinWebSecurity {


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);

        JmixOidcUserService jmixOidcUserService =
                applicationContext.getBean(JmixOidcUserService.class);

        ClientRegistrationRepository clientRegistrationRepository =
                applicationContext.getBean(ClientRegistrationRepository.class);

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
