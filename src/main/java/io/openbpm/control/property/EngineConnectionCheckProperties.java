package io.openbpm.control.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.boot.context.properties.bind.DefaultValue;

/**
 * Properties that are used while checking a connection to the BPM engine from the UI.
 */
@ConfigurationProperties(prefix = "openbpm.control.ui.connection-check")
@ConfigurationPropertiesBinding
public class EngineConnectionCheckProperties {

    /**
     * The interval (in seconds) at which the connection to the BPM engine is checked by timer.
     */
    private final Integer intervalSec;
    /**
     * A connection timeout (in milliseconds) that is used while checking BPM engine connection.
     */
    private final Integer connectTimeout;
    /**
     * A timeout (in milliseconds) to read data from the socket that is used while checking BPM engine connection.
     */
    private final Integer readTimeout;
    /**
     * A max number of retries that should be done to check the connection to the BPM engine.
     */
    private final Integer maxRetries;
    /**
     * A timeout (in milliseconds) between retries while checking a connection to the BPM engine.
     */
    private final Integer retryTimeout;

    public EngineConnectionCheckProperties(@DefaultValue("30") Integer intervalSec,
                                           @DefaultValue("10000") Integer connectTimeout,
                                           @DefaultValue("10000") Integer readTimeout,
                                           @DefaultValue("1") Integer maxRetries,
                                           @DefaultValue("100") Integer retryTimeout) {
        this.intervalSec = intervalSec;
        this.connectTimeout = connectTimeout;
        this.readTimeout = readTimeout;
        this.maxRetries = maxRetries;
        this.retryTimeout = retryTimeout;
    }

    /**
     * @return an interval to update a BPM engine connection status on UI by timer
     */
    public Integer getIntervalSec() {
        return intervalSec;
    }

    /**
     * @return a connection timeout that is used while checking a connection to BPM engine by timer
     */
    public Integer getConnectTimeout() {
        return connectTimeout;
    }

    /**
     * @return a read timeout that is used while checking a connection to BPM engine by timer
     */
    public Integer getReadTimeout() {
        return readTimeout;
    }

    /**
     * @return a number of retries that is used while checking a connection to BPM engine by timer
     */
    public Integer getMaxRetries() {
        return maxRetries;
    }

    /**
     * @return a timeout between retries while checking a connection to the BPM engine
     */
    public Integer getRetryTimeout() {
        return retryTimeout;
    }
}
