package io.openbpm.control.restsupport;

import feign.RequestInterceptor;

/**
 * A context to create an instance of Feign client interacting with BPM engine REST API:
 * <ol>
 *     <li>Client class: a Java class containing methods to interact with BPM engine. Example of Camunda Version API client class: {@link org.camunda.community.rest.client.api.VersionApiClient}</li>
 *     <li>URL: a target URL to which the requests should be sent</li>
 *     <li>Request interceptor: a request interceptor, e.g. that adds HTTP headers for an authentication.</li>
 *     <li>Connect timeout: a connect timeout in milliseconds. If not specified, the default value will be taken from the properties.</li>
 *     <li>Read timeout: a read timeout in milliseconds. If not specified, the default value will be taken from the properties.</li>
 *     <li>Max retries timeout: a max number of request execution retries.</li>
 *     <li>Retry timeout: a timeout between retries.</li>
 * </ol>
 *
 * @param <V> feign client class
 */
public class FeignClientCreationContext<V> {
    private final Class<V> clientClass;
    private Integer connectTimeout;
    private Integer readTimeout;
    private Integer maxRetries;
    private Integer retryTimeout;
    private RequestInterceptor requestInterceptor;
    private String url;

    public FeignClientCreationContext(Class<V> clientClass) {
        this.clientClass = clientClass;
    }

    public Class<V> getClientClass() {
        return clientClass;
    }

    public Integer getMaxRetries() {
        return maxRetries;
    }

    /**
     * Sets a max number of retries.
     *
     * @param maxRetries max number of retries
     * @return current context
     */
    public FeignClientCreationContext<V> setMaxRetries(Integer maxRetries) {
        this.maxRetries = maxRetries;
        return this;
    }

    public Integer getRetryTimeout() {
        return retryTimeout;
    }

    /**
     * Sets a timeout between retries.
     *
     * @param retryTimeout a timeout between retries
     * @return current context
     */
    public FeignClientCreationContext<V> setRetryTimeout(Integer retryTimeout) {
        this.retryTimeout = retryTimeout;
        return this;
    }

    public Integer getConnectTimeout() {
        return connectTimeout;
    }

    /**
     * Sets a connect timeout.
     *
     * @param connectTimeout a connect timeout in milliseconds
     * @return current context
     */
    public FeignClientCreationContext<V> setConnectTimeout(Integer connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }

    public Integer getReadTimeout() {
        return readTimeout;
    }

    /**
     * Sets a read timeout.
     *
     * @param readTimeout a read timeout in milliseconds
     * @return current context
     */
    public FeignClientCreationContext<V> setReadTimeout(Integer readTimeout) {
        this.readTimeout = readTimeout;
        return this;
    }

    public RequestInterceptor getRequestInterceptor() {
        return requestInterceptor;
    }

    /**
     * Sets a request interceptor.
     *
     * @param requestInterceptor an instance of request interceptor
     * @return current context
     */
    public FeignClientCreationContext<V> setRequestInterceptor(RequestInterceptor requestInterceptor) {
        this.requestInterceptor = requestInterceptor;
        return this;
    }

    public String getUrl() {
        return url;
    }

    /**
     * Sets a target URL.
     *
     * @param url target URL
     * @return current context
     */
    public FeignClientCreationContext<V> setUrl(String url) {
        this.url = url;
        return this;
    }
}
