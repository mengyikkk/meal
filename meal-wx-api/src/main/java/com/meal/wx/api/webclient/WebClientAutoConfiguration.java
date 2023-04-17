package com.meal.wx.api.webclient;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.util.concurrent.TimeUnit;

@EnableConfigurationProperties(WebClientProperties.class)
@ConditionalOnClass(WebClient.class)
@ConditionalOnProperty(prefix = WebClientProperties.PREFIX, value = "enable")
@Configuration
public class WebClientAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public WebClient.Builder builder(){
        return WebClient.builder();
    }

    @Bean
    @ConditionalOnMissingBean
    public WebClient webClient(@Validated WebClientProperties properties, WebClient.Builder builder){
        Logger logger = LoggerFactory.getLogger(WebClient.class);
        // http超时相关设置
        HttpClient httpClient = HttpClient
                .create()
                // 连接超时
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, Long.valueOf(properties.getConnectionTimeout().toMillis()).intValue())
                .responseTimeout(properties.getResponseTimeout())
                .doOnConnected(connection -> connection
                        // 读超时
                        .addHandler(new ReadTimeoutHandler(properties.getReadTimeout().getSeconds(), TimeUnit.SECONDS))
                        // 写超时
                        .addHandler(new WriteTimeoutHandler(properties.getWriteTimeout().getSeconds(), TimeUnit.SECONDS)));

        builder
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                // 请求信息日志
                .filter((request, next) -> {
                    if(logger.isDebugEnabled()){
                        logger.debug("Webclient - teleport request {}method: {}, url: {}, headers: {}",
                                request.logPrefix(), request.method(), request.url(), request.headers());
                    }
                    return next.exchange(request);
                })
                // 响应信息日志
                .filter(ExchangeFilterFunction.ofResponseProcessor((response)->{
                    if(logger.isDebugEnabled()){
                        logger.debug("Webclient - teleport response {}status: {}, headers: {}",
                                response.logPrefix(), response.rawStatusCode(), response.headers().asHttpHeaders());
                    }
                    return Mono.just(response);
                }));
        if(! CollectionUtils.isEmpty(properties.getDefaultHeaders())){
            properties.getDefaultHeaders().forEach(builder::defaultHeader);
        }
        return builder.build();
    }
}

