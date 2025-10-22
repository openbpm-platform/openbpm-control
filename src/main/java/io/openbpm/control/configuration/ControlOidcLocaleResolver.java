package io.openbpm.control.configuration;

import com.google.common.base.Strings;
import io.jmix.core.LocaleResolver;
import io.jmix.core.security.AuthenticationLocaleResolver;
import io.openbpm.control.entity.KeycloakJmixOidcUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * Resolves the user interface locale for OIDC-authenticated users.
 */
@Component("control_ControlOidcLocaleResolver")
public class ControlOidcLocaleResolver implements AuthenticationLocaleResolver {

    /**
     * Checks whether this resolver supports the given {@link Authentication} type.
     *
     * @param authentication the current authentication instance
     * @return {@code true} if the authentication is an {@link OAuth2AuthenticationToken}, otherwise {@code false}
     */
    @Override
    public boolean supports(Authentication authentication) {
        return authentication instanceof OAuth2AuthenticationToken;
    }

    /**
     * Resolves the locale for the authenticated OIDC user.
     *
     * @param authentication the authentication object containing the OIDC user principal
     * @return the resolved {@link Locale}, or {@code null} if no locale information is available
     */
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

    /**
     * Returns the order of this resolver. Lower values have higher priority.
     *
     * @return the resolver order
     */
    @Override
    public int getOrder() {
        return 0;
    }
}