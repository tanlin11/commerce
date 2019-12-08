package com.tl.commerce.service.impl;

import com.alibaba.fastjson.JSON;
import com.tl.commerce.domain.Goods;
import com.tl.commerce.domain.GoodsSpecs;
import com.tl.commerce.mapper.CommodityMapper;
import com.tl.commerce.mapper.OrderMapper;
import com.tl.commerce.service.CommodityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品
 */
@Service
public class CommodityServiceImpl implements CommodityService {

    @Autowired
    CommodityMapper commodityMapper;

    /**
     * 获取商品
     * @return
     */
    @Override
    public List<Map<String,Object>> getCommoditiesWithSpec(){
        List<Map<String,Object>> resultMap=new ArrayList<>(12);
        List<Goods> goods=commodityMapper.getDoods();

        List<GoodsSpecs> specs=commodityMapper.getgoods_specs();
        goods.forEach(good -> {
            int id=good.getId();
            Map<String ,Object> goodsMap=JSON.parseObject(JSON.toJSONString(good));
            List<GoodsSpecs> detailList=new ArrayList<>(8);
            specs.forEach(goodsSpecs -> {
                if(id==goodsSpecs.getGoodsId()){
                    detailList.add(goodsSpecs);
                }
            });
            goodsMap.put("detail",detailList);
            resultMap.add(goodsMap);

        });
        return resultMap;
    }

    @Override
    public Map<String, Object> getCommodityWithSpec(int goods_id) {
        Goods goods=commodityMapper.getGood(goods_id);
        List<GoodsSpecs> specs=commodityMapper.get_goods_spec(goods_id);
        if(null==goods||null==specs)return null;

        Map<String,Object>resultMap=JSON.parseObject(JSON.toJSONString(goods));

        resultMap.put("detail",specs);
        return resultMap;

    }

    /**
     * 获取商品   先简单设计商品表，不涉及品类
     * @return
     */
    @Override
    public List<Map<String,Object>> getCommodities() {
        return commodityMapper.getCommodities();

    }

    @Override
    public List<Goods> getGoods() {
        return commodityMapper.getDoods();
    }

    @Override
    public List<GoodsSpecs> getSpecs() {
        return commodityMapper.getgoods_specs();
    }
    @Override
    public GoodsSpecs getSpec(int goods_id) {
        return commodityMapper.getgoods_spec(goods_id);
    }
}
