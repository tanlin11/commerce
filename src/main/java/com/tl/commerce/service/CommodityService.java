package com.tl.commerce.service;

import com.tl.commerce.domain.Goods;
import com.tl.commerce.domain.GoodsSpecs;

import java.util.List;
import java.util.Map;

public interface CommodityService {
    public List<Map<String,Object>> getCommoditiesWithSpec();
    public Map<String,Object> getCommodityWithSpec(int goods_id);
    List<Map<String,Object>> getCommodities();

    List<Goods> getGoods();

    List<GoodsSpecs> getSpecs();
    public GoodsSpecs getSpec(int goods_id);

}
