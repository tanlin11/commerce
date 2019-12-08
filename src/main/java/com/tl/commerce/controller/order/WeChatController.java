package com.tl.commerce.controller.order;


import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.tl.commerce.config.WeChatConfig;
import com.tl.commerce.domain.GoodsSpecs;
import com.tl.commerce.domain.Order;
import com.tl.commerce.domain.Resp;
import com.tl.commerce.service.CommodityService;
import com.tl.commerce.service.OrderService;
import com.tl.commerce.service.UserService;
import com.tl.commerce.utils.CommonUtils;
import com.tl.commerce.utils.IpUtils;
import com.tl.commerce.utils.JwtUtils;
import com.tl.commerce.utils.WXPayUtil;
import io.jsonwebtoken.Claims;
import org.apache.ibatis.annotations.Insert;
import org.mybatis.logging.Logger;
import org.mybatis.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.*;

/**
 * 统一下单 支付回调
 */
@Controller
@RequestMapping("/order")
public class WeChatController {

    @Autowired
    WeChatConfig weChatConfig;


    @Autowired
    UserService userService;

    @Autowired
    OrderService orderService;

    @Autowired
    CommodityService commodityService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    /**
     * 微信统一下单   校验token  生成签名  拼装下单参数，获取codeUrl 生成下单二维码返回
     *
     * @param GoodId
     * @param SpecsId
     * @param address
     * @param consignee
     * @param mobile
     * @param request
     * @param response
     */
    @GetMapping("/unify/order")
    public void unify_order(@RequestParam(value = "good_id", required = true) Integer GoodId,
                            @RequestParam(value = "specs_id", required = true) Integer SpecsId,
                            @RequestParam(value = "address", required = true) String address,
                            @RequestParam(value = "consignee", required = true) String consignee,
                            @RequestParam(value = "mobile", required = true) String mobile, HttpServletRequest request, HttpServletResponse response) {
        int userId = (Integer) request.getAttribute("user_id");
        String userIp= IpUtils.getIpAddr(request);//生产环境
//        String userIp = "127.0.0.1";

        try {
            //保存订单
            String code_url = orderService.unifiedOrder(GoodId, SpecsId, userIp, userId, address, consignee, mobile);
            if (null == code_url) throw new NullPointerException("下单url生成失败");
            //生成二维码配置
            Map<EncodeHintType, Object> hints = new HashMap<>();

            //设置纠错等级
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
            //编码类型
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

            BitMatrix bitMatrix = new MultiFormatWriter().encode(code_url, BarcodeFormat.QR_CODE, 400, 400, hints);
            OutputStream out = response.getOutputStream();

            MatrixToImageWriter.writeToStream(bitMatrix, "png", out);
        } catch (Exception e) {
            logger.error(()->"weChatPay",e);
            e.printStackTrace();
        }


        /**
         * 统一下单，生成支付参数
         */


    }

    /**
     * 微信支付成功回调接口，校验签名，MD5加密方式，保持接口幂等性
     *
     * @param request
     * @param response
     */
    @PostMapping("/weChat/callback")
    public void weChatCallback(HttpServletRequest request, HttpServletResponse response) throws Exception {
        InputStream inputStream = request.getInputStream();

        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        StringBuffer sb = new StringBuffer();
        String line;
        while ((line = in.readLine()) != null) {
            sb.append(line);
        }
        in.close();
        inputStream.close();
        Map<String, String> callbackMap = WXPayUtil.xmlToMap(sb.toString());
        System.out.println(callbackMap.toString());

        SortedMap<String, String> sortedMap = WXPayUtil.getSortedMap(callbackMap);
        final Gson gson = new Gson();
        System.out.println("weChat callback body:" + gson.toJson(sortedMap));
        /**
         * 校验签名
         * 统一下单的时候生成的签名 sign 是所需的下单参数按字母排序，再与我们的商户号进行md5进行加密的
         * 同样的微信回调签名sign字段也是以字母排序加上我们的商户key进行md5加密，
         * 我们获取之后以同样的方式拼接加密 再与sign进行equals 防止微信回调接口被他人伪造调用
         */
        if (WXPayUtil.isCorrectSign(sortedMap, weChatConfig.getKey())) {

            if ("SUCCESS".equals(sortedMap.get("result_code"))) {

                String outTradeNo = sortedMap.get("out_trade_no");
                String openid = sortedMap.get("openid");
                String transactionId = sortedMap.get("transaction_id");
                Order order = orderService.getOrderByOrderId(outTradeNo);
                if (order.getState() == 0) {
                    int influence = orderService.updateOrderState(openid, transactionId, outTradeNo);
                    if (influence == 1) {
                        response.setContentType("text/xml");
                        response.getWriter().println("success");
                    }
                }
            }
        }
        //都处理失败
        response.setContentType("text/xml");
        response.getWriter().println("fail");

    }


    /**
     * 获取商品
     *
     * @param token
     * @return
     */
    @GetMapping("/getCommodities")
    @ResponseBody
    public Resp getCommodities(@RequestParam(value = "token", required = true) String token) {

        List<Map<String, Object>> commodities = commodityService.getCommoditiesWithSpec();
        if (null == commodities || commodities.size() == 0)
            return Resp.fail().code(-1).msg("暂无商品");
        return Resp.ok().code(0).data(commodities).msg("查询成功");

    }

    /**
     * 获取单个商品
     *
     * @param GoodsId
     * @return
     */
    @GetMapping("/getCommodity")
    @ResponseBody
    public Resp getCommodity(@RequestParam(value = "goods_id", required = true) int GoodsId) {

        Map<String, Object> commodities = commodityService.getCommodityWithSpec(GoodsId);
        if (null == commodities || commodities.size() == 0)
            return Resp.fail().code(-1).msg("暂无商品");
        return Resp.ok().code(0).data(commodities).msg("查询成功");

    }

    /**
     * 获取个人订单
     *
     * @param userId
     * @param openid
     * @return
     */
    @GetMapping("/get/myOrder")
    @ResponseBody
    public Resp getMyOrderMsg(@RequestParam(value = "user_id", required = true) Integer userId,
                              @RequestParam(value = "openid", required = false) String openid) {
        List<Map<String, Object>> orderList = orderService.getOrderByUserId(userId);
        if (null == orderList || orderList.size() == 0)
            return Resp.fail().code(-1).msg("暂无订单");
        return Resp.ok().code(0).data(orderList).msg("查询成功");
    }

    /**
     * 轮询 用户支付状态
     *
     * @param userId
     * @param GoodId
     * @param specId
     * @return
     */
    @GetMapping("/get/current_order_msg")
    @ResponseBody
    public Resp getCurrentOrderMsg(@RequestParam(value = "user_id", required = true) Integer userId,
                                   @RequestParam(value = "good_id", required = true) Integer GoodId, @RequestParam(value = "spec_id", required = true) Integer specId) {
        List<Order> orderList = orderService.getCurrentOrder(userId, GoodId, specId);
        List<Map<String, Object>> resultList = new ArrayList<>(6);
        orderList.forEach(order -> {
            int state = order.getState();

            Map<String, Object> tempMap = JSON.parseObject(JSON.toJSONString(order));
            if (state == 0)
                tempMap.put("state", "未支付");
            else if (state == 1)
                tempMap.put("state", "已支付");
            resultList.add(tempMap);
        });
        if (resultList.size() == 0)
            return Resp.fail().code(-1).msg("暂无订单");
        return Resp.ok().code(0).data(resultList).msg("查询成功");
    }


}
