package io.openbpm.control.restsupport;

import feign.*;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.codec.ErrorDecoder;
import org.springframework.cloud.openfeign.FeignClientProperties;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * Provides an ability create a new instance of the Feign client using configured beans and additional context.
 *
 * @see Feign.Builder
 */
public class FeignClientProvider {

    protected final Encoder encoder;
    protected final Decoder decoder;
    protected final Retryer retryer;
    protected final ErrorDecoder errorDecoder;
    protected final Contract contract;
    protected final Client client;
    protected final FeignClientProperties feignClientProperties;

    public FeignClientProvider(Encoder encoder, Decoder decoder,
                               Retryer retryer, ErrorDecoder errorDecoder, Contract contract,
                               Client client, FeignClientProperties feignClientProperties) {
        this.encoder = encoder;
        this.decoder = decoder;
        this.retryer = retryer;
        this.errorDecoder = errorDecoder;
        this.contract = contract;
        this.client = client;
        this.feignClientProperties = feignClientProperties;
    }

    /**
     * Creates a new instance of Camunda client using specified context and default configuration.
     *
     * @param context a context containing details like URL, request interceptor etc.
     * @param <V>     a class of Feign client
     * @return created instance of client
     */

    public <V> V createCamundaClient(FeignClientCreationContext<V> context) {
        Feign.Builder builder = Feign.builder()
                .contract(contract)
                .encoder(encoder)
                .decoder(decoder)
                .errorDecoder(errorDecoder);

        FeignClientProperties.FeignClientConfiguration defaultConfig =
                feignClientProperties.getConfig().get(feignClientProperties.getDefaultConfig());

        applyIfNotNull(client, builder::client);
        addLoggerLevel(builder, defaultConfig);
        applyIfNotNull(context.getRequestInterceptor(), builder::requestInterceptor);
        setRetryer(context, builder);
        setConnectTimeouts(context, builder, defaultConfig);

        Class<V> clientClass = context.getClientClass();
        String url = context.getUrl();
        return url == null ? builder.target(Target.EmptyTarget.create(clientClass)) : builder.target(clientClass, url);
    }

    protected void addLoggerLevel(Feign.Builder builder, FeignClientProperties.FeignClientConfiguration defaultConfig) {
        if (defaultConfig != null && defaultConfig.getLoggerLevel() != null) {
            builder.logLevel(defaultConfig.getLoggerLevel());
        }
    }

    protected <E> void applyIfNotNull(E value, Function<E, Feign.Builder> feignClientBuilder) {
        if (value != null) {
            feignClientBuilder.apply(value);
        }
    }

    protected <V> void setConnectTimeouts(FeignClientCreationContext<V> context, Feign.Builder builder, FeignClientProperties.FeignClientConfiguration defaultConfig) {
        Integer connectTimeout = context.getConnectTimeout();
        Integer readTimeout = context.getReadTimeout();
        if (connectTimeout == null && readTimeout == null && defaultConfig != null) {
            connectTimeout = defaultConfig.getConnectTimeout();
            readTimeout = defaultConfig.getReadTimeout();
        }
        if (connectTimeout != null && readTimeout != null) {
            builder.options(new Request.Options(connectTimeout, TimeUnit.MILLISECONDS, readTimeout, TimeUnit.MILLISECONDS, false));
        } else {
            builder.options(new Request.Options());
        }
    }

    protected <V> void setRetryer(FeignClientCreationContext<V> context, Feign.Builder builder) {
        Integer retriesTimeout = context.getRetryTimeout();
        Integer maxRetries = context.getMaxRetries();
        if (retriesTimeout != null && maxRetries != null) {
            builder.retryer(new Retryer.Default(retriesTimeout, retriesTimeout, maxRetries));
        } else {
            builder.retryer(retryer);
        }
    }

}
