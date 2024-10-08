package com.meal.wx.api.util;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaSubscribeMessage;
import com.meal.common.utils.JsonUtils;
import com.meal.wx.api.config.WxProperties;
import com.meal.wx.api.dto.WxSendMessageVo;
import me.chanjar.weixin.common.error.WxErrorException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 微信模版消息通知
 */
@Service
public class WxTemplateSender {
	private final Logger logger =  LoggerFactory.getLogger(WxTemplateSender.class);
	private static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential";

	@Resource
	private WxMaService wxMaService;

	@Resource
	private WxProperties wxProperties;

	@Resource
	private WebClient webClient;

	@Resource
	private RedisTemplate<String, Object> redisTemplate;


	private  void sendSmallMsg(String openId,String templteId,Map<String,String> params) {
		WxMaSubscribeMessage wxMaSubscribeMessage = WxMaSubscribeMessage.builder()
				.toUser(openId)
				.templateId(templteId)
				.build();
		// 设置将推送的消息
		params.forEach((k, v) -> {
			wxMaSubscribeMessage.addData(new WxMaSubscribeMessage.MsgData(k, v));
		});
		try {
			logger.info("开始发送消息！！！！");
			wxMaService.getMsgService().sendSubscribeMsg(wxMaSubscribeMessage);
			logger.info("消息发送成功！！！！");
		} catch (WxErrorException e) {
			e.printStackTrace();
		}
	}

	public boolean sendMessage(WxSendMessageVo vo) {
		this.logger.info("推送消息请求参数：{}", JsonUtils.toJson(vo));
		String url = "https://api.weixin.qq.com/cgi-bin/message/subscribe/send?access_token=" + getAccessToken();
		this.logger.info("推送消息请求地址：{}", url);
		return webClient.post()
				.uri(url)
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(vo)
				.header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
				.retrieve()
				.bodyToMono(new ParameterizedTypeReference<Map<String, String>>() {})
				.map(responseData -> {
					int errorCode = Integer.parseInt(responseData.get("errcode"));
					String errorMessage = responseData.get("errmsg");
					if (errorCode == 0) {
						logger.info("推送消息发送成功");
						return true;
					} else {
						logger.info("推送消息发送失败,errcode：{},errorMessage：{}", errorCode, errorMessage);
						return false;
					}
				})
				.blockOptional()
				.orElse(false);
	}


	/*参数拼接*/



	// 获取访问令牌
	public String getAccessToken() {
		// 从Redis中获取令牌
		Optional<String> redisToken = Optional.ofNullable((String) redisTemplate.opsForValue().get("access_token"));

		// 如果Redis中存在令牌，则返回
		return redisToken.orElseGet(() -> callAccessTokenApi()
				.map(responseMap -> {
					// 从响应中获取访问令牌
					String accessToken = (String) responseMap.get("access_token");
					logger.info("获取 access_token 成功, Send Success");

					// 获取并设置令牌的过期时间
					Optional.ofNullable( responseMap.get("expires_in").toString())
							.ifPresent(timeout -> {
								long timeoutMillis = TimeUnit.SECONDS.toMillis(Long.parseLong(timeout)) - 10L;
								redisTemplate.opsForValue().set("access_token", accessToken, timeoutMillis, TimeUnit.MILLISECONDS);
							});
					return accessToken;
				})
				.orElse(""));

		// 调用API以获取访问令牌，并处理响应
	}

	// 调用访问令牌API
	private Optional<Map<String, Object>> callAccessTokenApi() {
		// 构建API URL
		String url = UriComponentsBuilder.fromHttpUrl(ACCESS_TOKEN_URL)
				.queryParam("appid", wxProperties.getAppId())
				.queryParam("secret", wxProperties.getAppSecret())
				.build().toString();

		// 使用WebClient发起GET请求并处理响应
		return webClient.get().uri(url)
				.retrieve()
				.bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
				.blockOptional();
	}

}
