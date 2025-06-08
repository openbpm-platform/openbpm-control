package io.openbpm.control.configuration;

import com.google.common.base.Strings;
import io.jmix.core.LocaleResolver;
import io.jmix.core.security.AuthenticationLocaleResolver;
import io.openbpm.control.entity.KeycloakJmixOidcUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component("control_ControlOidcLocaleResolver")
public class ControlOidcLocaleResolver implements AuthenticationLocaleResolver {

    @Override
    public boolean supports(Authentication authentication) {
        return authentication instanceof OAuth2AuthenticationToken;
    }

    @Override
    public Locale getLocale(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof KeycloakJmixOidcUser user &&
                !Strings.isNullOrEmpty(user.getLocale())) {
            String locale = user.getLocale();
            return LocaleResolver.resolve(locale);
        }
        return null;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
