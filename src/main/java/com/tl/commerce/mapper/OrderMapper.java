package com.tl.commerce.mapper;

import com.tl.commerce.domain.Order;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;


/**
 * 用户订单逻辑
 */
public interface OrderMapper {
    /**
     * 支付成功，微信回调里改变订单状态
     * @param openid
     * @param transactionId
     * @param orderId
     * @return
     */
    @Update("UPDATE `tl`.`order` SET state=1 ,openid=#{openid},transactionId=#{transactionId} WHERE orderId =#{orderId}")
    int update(String openid,String transactionId,String orderId);


    /**
     * 订单超时或取消
     * @param orderId
     * @return
     */
    @Delete("update  `tl`.`order` set state =2 where orderId=#{orderId} ")
    int delete(String orderId);

    /**
     * 保存订单
     * @param order
     * @return
     */
    @Insert("INSERT INTO `tl`.`order` ( `totalFee`, `orderId`, " +
            "`userId`, `goodsId`, `specsId`, `address`,`userIp`,`consignee`,`mobile`)" +
            "VALUES" +
            "(#{totalFee}, #{orderId}, #{userId}, #{goodsId}" +
            ",#{specsId},#{address},#{userIp},#{consignee},#{mobile})")
    @Options(useGeneratedKeys=true, keyProperty="id", keyColumn="id")
    int save(Order order);

    /**
     * 获取用户订单
     * @param userId
     * @return
     */
    @Select("SELECT o.id,o.openid,o.totalFee,o.orderId,o.userId,o.address,o.userIp,o.goodsId,o.specsId," +
            "o.transactionId,o.createtime,goods.description,goods.goodsName,goods.goodsImage,goods.unit,goods_specs.price," +
            "goods_specs.specification FROM tl.`order`  o " +
            "left join goods on o.goodsId=goods.id left join goods_specs on o.specsId=goods_specs.id where o.userId=#{userId}")
    List<Map<String,Object>> getOrderByUserId(int userId);

    /**
     * 获取用户订单
     * @param orderId
     * @return
     */
    @Select("select * from `tl`.`order` where orderId=#{orderId}")
    Order getOrderByOrderId(String orderId);

    /**
     * 支付状态查询
     * @param user_id
     * @param goods_id
     * @param spec_id
     * @return
     */
    @Select ("select * from `tl`.`order` where userId=#{user_id} and goodsId=#{goods_id} and specsId=#{spec_id}")
    List<Order> getCurrenrOrderMsg(int user_id,int goods_id,int spec_id);
}
