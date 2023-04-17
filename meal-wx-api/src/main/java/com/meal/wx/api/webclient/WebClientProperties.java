package com.meal.wx.api.webclient;

import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Duration;
import java.util.Map;

@ConfigurationProperties(prefix = WebClientProperties.PREFIX)
public class WebClientProperties {

    public final static String PREFIX = "meal.webclient";

    /** 是否启用webclient */
    private Boolean enable = Boolean.FALSE;

    /** 连接目标地址的超时 */
    @NotNull
    private Duration connectionTimeout = Duration.ofSeconds(3);

    /** 等待响应的超时 */
    @NotNull
    private Duration responseTimeout = Duration.ofSeconds(30);

    /** 读取数据的超时 */
    @NotNull
    private Duration readTimeout = Duration.ofSeconds(5);

    /** 写入数据的超时 */
    @NotNull
    private Duration writeTimeout = Duration.ofSeconds(5);

    /** 发送http请求时使用的头部 */
    private Map<@NotNull String, @NotBlank String> defaultHeaders;

    public Boolean getEnable() {
        return enable;
    }

    public WebClientProperties setEnable(Boolean enable) {
        this.enable = enable;
        return this;
    }

    public Duration getConnectionTimeout() {
        return connectionTimeout;
    }

    public WebClientProperties setConnectionTimeout(Duration connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
        return this;
    }

    public Duration getResponseTimeout() {
        return responseTimeout;
    }

    public WebClientProperties setResponseTimeout(Duration responseTimeout) {
        this.responseTimeout = responseTimeout;
        return this;
    }

    public Duration getReadTimeout() {
        return readTimeout;
    }

    public WebClientProperties setReadTimeout(Duration readTimeout) {
        this.readTimeout = readTimeout;
        return this;
    }

    public Duration getWriteTimeout() {
        return writeTimeout;
    }

    public WebClientProperties setWriteTimeout(Duration writeTimeout) {
        this.writeTimeout = writeTimeout;
        return this;
    }

    public Map<String, String> getDefaultHeaders() {
        return defaultHeaders;
    }

    public WebClientProperties setDefaultHeaders(Map<String, String> defaultHeaders) {
        this.defaultHeaders = defaultHeaders;
        return this;
    }
}
