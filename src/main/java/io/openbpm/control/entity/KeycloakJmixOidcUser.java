package io.openbpm.control.entity;

import io.jmix.oidc.user.DefaultJmixOidcUser;

public class KeycloakJmixOidcUser extends DefaultJmixOidcUser {

    private String locale;

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }
}
