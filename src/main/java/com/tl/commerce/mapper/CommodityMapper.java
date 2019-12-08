package com.tl.commerce.mapper;


import com.tl.commerce.domain.Goods;
import com.tl.commerce.domain.GoodsSpecs;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 商品逻辑
 */
public interface CommodityMapper {
    /**
     * 获取商品
     * @return
     */
    @Select("  SELECT goods.id," +
            "goods.goodsName," +
            "goods.goodsSno," +
            "goods.description," +
            "goods.unit," +
            "goods.goodsImage," +
            "spec.specification," +
            "spec.price," +
            "spec.stock" +
            "FROM tl.goods left join tl.goods_specs  spec on spec.goods_id=goods.id  where isSale=1  ")
    List<Map<String,Object>> getCommodities();

    /**
     * 商品
     * @return
     */
    @Select("select * from goods order by id ")
    List<Goods> getDoods();

    /**
     * 商品
     * @param goods_id
     * @return
     */
    @Select("select * from goods where id=#{goods_id}")
    Goods getGood(int goods_id);

    /**
     * 商品详情
     * @return
     */
    @Select("select * from goods_specs order by id")
    List<GoodsSpecs> getgoods_specs();

    /**
     * 商品详情
     * @return
     */
    @Select("select * from goods_specs where id=#{goods_id}")
    GoodsSpecs getgoods_spec(int goods_id);

    /**
     * 商品详情
     * @return
     */
    @Select("select * from goods_specs where goods_id=#{goods_id}")
    List<GoodsSpecs> get_goods_spec(int goods_id);

}
