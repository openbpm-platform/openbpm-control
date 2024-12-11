/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.openbpm.control.configuration;

import io.openbpm.control.uicomponent.spinner.SpinnerLoader;
import io.openbpm.control.uicomponent.treedatagrid.NoClickTreeDataGridLoader;
import io.openbpm.control.uicomponent.treedatagrid.NoClickTreeGrid;
import io.jmix.flowui.sys.registration.ComponentRegistration;
import io.jmix.flowui.sys.registration.ComponentRegistrationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.vaadin.addons.componentfactory.spinner.Spinner;

@Configuration
public class UiComponentRegistrationConfiguration {

    @Bean
    public ComponentRegistration noClickTreeDataGrid() {
        return ComponentRegistrationBuilder.create(NoClickTreeGrid.class)
                .withComponentLoader("noClickTreeDataGrid", NoClickTreeDataGridLoader.class)
                .build();
    }

    @Bean
    public ComponentRegistration spinner() {
        return ComponentRegistrationBuilder.create(Spinner.class)
                .withComponentLoader("spinner", SpinnerLoader.class)
                .build();
    }
}
