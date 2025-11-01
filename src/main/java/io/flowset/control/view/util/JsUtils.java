/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.flowset.control.view.util;

public class JsUtils {
    public static final String COPY_SCRIPT_TEXT = """
                  const textarea = document.createElement("textarea");
                  textarea.value = $0;
                  
                  textarea.style.position = "absolute";
                  textarea.style.opacity = "0";
                  
                  document.body.appendChild(textarea);
                  textarea.select();
                  document.execCommand("copy");
                  document.body.removeChild(textarea);
                """;

    public static final String SET_DEFAULT_TIME_SCRIPT = """
            this.__datePicker.addEventListener('change', function(){this.__timePicker.value='00:00';}.bind(this));""";
}
