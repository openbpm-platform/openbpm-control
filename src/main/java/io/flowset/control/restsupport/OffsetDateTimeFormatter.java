/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.flowset.control.restsupport;

import org.springframework.format.Formatter;

import java.text.ParseException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class OffsetDateTimeFormatter implements Formatter<OffsetDateTime> {
    private static final DateTimeFormatter CAMUNDA_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

    @Override
    public OffsetDateTime parse(String text, Locale locale) throws ParseException {
        return OffsetDateTime.parse(text, CAMUNDA_DATE_TIME_FORMATTER);
    }

    @Override
    public String print(OffsetDateTime object, Locale locale) {
        return CAMUNDA_DATE_TIME_FORMATTER.format(object);
    }
}
