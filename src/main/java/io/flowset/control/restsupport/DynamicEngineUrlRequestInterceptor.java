package io.flowset.control.restsupport;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import io.flowset.control.entity.engine.AuthType;
import io.flowset.control.entity.engine.BpmEngine;
import io.flowset.control.exception.EngineNotSelectedException;
import io.flowset.control.service.engine.EngineService;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.http.HttpHeaders;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class DynamicEngineUrlRequestInterceptor implements RequestInterceptor {
    protected final EngineService engineService;

    public DynamicEngineUrlRequestInterceptor(EngineService engineService) {
        this.engineService = engineService;
    }

    @Override
    public void apply(RequestTemplate template) {
        BpmEngine selectedEngine = engineService.getSelectedEngine();
        if (selectedEngine == null) {
            throw new EngineNotSelectedException("BPM engine not selected");
        }
        template.target(selectedEngine.getBaseUrl());
        if (BooleanUtils.isTrue(selectedEngine.getAuthEnabled())) {
            if (selectedEngine.getAuthType() == AuthType.BASIC) {
                String username = selectedEngine.getBasicAuthUsername();
                String password = selectedEngine.getBasicAuthPassword();
                String encodedHeader = Base64.getEncoder().encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8));
                template.header(HttpHeaders.AUTHORIZATION, "Basic " + encodedHeader);
            } else if (selectedEngine.getAuthType() == AuthType.HTTP_HEADER) {
                template.header(selectedEngine.getHttpHeaderName(), selectedEngine.getHttpHeaderValue());
            }
        }
    }
}
