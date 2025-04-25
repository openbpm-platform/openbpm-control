/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.openbpm.control.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.openbpm.control.dto.DmnDecisionDefinition;
import io.openbpm.control.dto.BpmProcessDefinition;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class BpmParseUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger log = LoggerFactory.getLogger(BpmParseUtil.class);

    public static List<BpmProcessDefinition> parseProcessDefinitionsJson(String processDefinitionsJson) {
        try {
            if (StringUtils.isNotBlank(processDefinitionsJson)) {
                return objectMapper.readValue(processDefinitionsJson, new TypeReference<>() {
                });
            }
        } catch (JsonProcessingException e) {
            log.error("Unable parse definitions JSON {}", processDefinitionsJson);
        }
        return List.of();
    }

    public static List<DmnDecisionDefinition> parseDecisionsDefinitionsJson(String decisionDefinitionsJson) {
        try {
            if (StringUtils.isNotBlank(decisionDefinitionsJson)) {
                return objectMapper.readValue(decisionDefinitionsJson, new TypeReference<>() {
                });
            }
        } catch (JsonProcessingException e) {
            log.error("Unable parse definitions JSON {}", decisionDefinitionsJson);
        }
        return List.of();
    }
}
