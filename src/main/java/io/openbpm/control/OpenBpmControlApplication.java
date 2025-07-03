/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.openbpm.control;

import com.google.common.base.Strings;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import io.jmix.core.MessageTools;
import io.jmix.flowui.UiComponents;
import io.jmix.flowui.menu.MenuConfig;
import io.jmix.flowui.menu.MenuItemCommands;
import io.jmix.flowui.sys.UiAccessChecker;
import io.jmix.flowui.view.ViewRegistry;
import io.openbpm.control.menu.CustomListMenuBuilder;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

@Push
@Theme(value = "openbpm-control")
@PWA(name = "OpenBPM Control", shortName = "OpenBPM Control", iconPath = "icons/logo.png")
@SpringBootApplication
@ConfigurationPropertiesScan
public class OpenBpmControlApplication implements AppShellConfigurator {

    @Autowired
    private Environment environment;

    public static void main(String[] args) {
        SpringApplication.run(OpenBpmControlApplication.class, args);
    }

    @Bean
    @Primary
    @ConfigurationProperties("main.datasource")
    DataSourceProperties dataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @Primary
    @ConfigurationProperties("main.datasource.hikari")
    DataSource dataSource(final DataSourceProperties dataSourceProperties) {
        return dataSourceProperties.initializeDataSourceBuilder().build();
    }

    @Bean
    @Primary
    CustomListMenuBuilder listMenuBuilder(MenuConfig menuConfig,
                                          ViewRegistry viewRegistry,
                                          UiComponents uiComponents,
                                          MessageTools messageTools,
                                          UiAccessChecker uiAccessChecker,
                                          MenuItemCommands menuItemCommands) {
        return new CustomListMenuBuilder(menuConfig, viewRegistry, uiComponents, messageTools, uiAccessChecker, menuItemCommands);
    }

    @EventListener
    public void printApplicationUrl(final ApplicationStartedEvent event) {
        LoggerFactory.getLogger(OpenBpmControlApplication.class).info("Application started at "
                + "http://localhost:"
                + environment.getProperty("local.server.port")
                + Strings.nullToEmpty(environment.getProperty("server.servlet.context-path")));
    }
}
