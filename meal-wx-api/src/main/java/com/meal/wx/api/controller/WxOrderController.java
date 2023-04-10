package com.meal.wx.api.controller;

import com.meal.common.Result;
import com.meal.common.dto.WxOrderVo;
import com.meal.common.utils.SecurityUtils;
import com.meal.wx.api.service.WxOrderService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/wx/order")
public class WxOrderController {

    @Resource
    private WxOrderService wxOrderService;

    @PostMapping("/submit")
    public Result<?> submit(@RequestBody WxOrderVo wxOrder) {
        return this.wxOrderService.submit(wxOrder, SecurityUtils.getUserId());
    }

    @PostMapping("/prepay/{orderSn}")
    public Result<?> prepay(@PathVariable("orderSn") String orderSn) {
        return this.wxOrderService.prepay(SecurityUtils.getUserId(),orderSn);
    }


    /**
     * 微信付款成功或失败回调接口
     * <p>
     *  注意，这里pay-notify是示例地址，建议开发者应该设立一个隐蔽的回调地址
     *
     * @param request  请求内容
     * @param response 响应内容
     * @return 操作结果
     */
    @PostMapping("/pay-notify")
    public Object payNotify(HttpServletRequest request, HttpServletResponse response) {
        return this.wxOrderService.payNotify(request, response);
    }
    @PostMapping("/refund/{orderSn}")
    public Object refund(@PathVariable("orderSn") String orderSn) {
        return this.wxOrderService.refund(SecurityUtils.getUserId(), orderSn);
    }
    @GetMapping("/detail")
    public  Result<?> detail(@RequestParam(required = false) String orderSn) {
        return wxOrderService.detail(SecurityUtils.getUserId(), orderSn);
    }
}
