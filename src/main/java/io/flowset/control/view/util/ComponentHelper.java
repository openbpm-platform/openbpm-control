/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.flowset.control.view.util;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.theme.lumo.LumoUtility;
import io.jmix.core.Messages;
import io.jmix.core.metamodel.datatype.DatatypeFormatter;
import io.jmix.core.security.CurrentAuthentication;
import io.jmix.flowui.UiComponents;
import io.flowset.control.entity.processinstance.ProcessInstanceState;
import lombok.AllArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.TimeZone;

@Component
@AllArgsConstructor
public class ComponentHelper {
    protected final UiComponents uiComponents;
    protected final Messages messages;
    protected final DatatypeFormatter datatypeFormatter;
    protected final CurrentAuthentication currentAuthentication;

    public Span createProcessInstanceStateBadge(ProcessInstanceState state) {
        Icon icon;
        String stateTheme;

        switch (state) {
            case COMPLETED -> {
                icon = VaadinIcon.CHECK.create();
                icon.addClassNames(LumoUtility.Padding.XSMALL);
                stateTheme = "contrast";
            }
            case SUSPENDED -> {
                icon = VaadinIcon.PAUSE.create();
                icon.addClassNames("suspended-state-small-icon");
                stateTheme = "warning";
            }
            default -> {
                icon = VaadinIcon.HOURGLASS.create();
                icon.addClassNames(LumoUtility.Padding.XSMALL);
                stateTheme = "success";
            }
        }

        Span span = uiComponents.create(Span.class);
        span.getElement().getThemeList().add("badge pill " + stateTheme);

        Span text = uiComponents.create(Span.class);
        text.setText(messages.getMessage(state));
        span.add(icon, text);
        return span;
    }

    public Span createDateSpan(@Nullable OffsetDateTime date) {
        Span span = uiComponents.create(Span.class);

        TimeZone timeZone = currentAuthentication.getTimeZone();
        if (date != null) {
            LocalDateTime timestamp = date
                    .atZoneSameInstant(timeZone.toZoneId())
                    .toLocalDateTime();
            String formattedDate = datatypeFormatter.formatLocalDateTime(timestamp);
            span.setText(formattedDate);
        }

        return span;
    }
}