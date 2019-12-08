package com.tl.commerce.domain;


/**
 * 用户订单信息
 */
public class Order {

  private Integer id;
  private String openid;
  private Integer totalFee;
  private String orderId;
  private Integer userId;
  /**
   * 0表示未支付 1表示已经支付 2表示支付已取消或过期
   */
  private Integer state;
  private Integer goodsId;
  private Integer specsId;
  private String address;

  private String transactionId;//微信支付成功微信订单

  private String userIp;//用户下单地址

  private String consignee;
  private String mobile;


  public String getConsignee() {
    return consignee;
  }

  public void setConsignee(String consignee) {
    this.consignee = consignee;
  }

  public String getMobile() {
    return mobile;
  }

  public void setMobile(String mobile) {
    this.mobile = mobile;
  }

  public String getUserIp() {

    return userIp;
  }

  public void setUserIp(String userIp) {
    this.userIp = userIp;
  }

  public String getTransactionId() {
    return transactionId;
  }

  public void setTransactionId(String transactionId) {
    this.transactionId = transactionId;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }


  public String getOpenid() {
    return openid;
  }

  public void setOpenid(String openid) {
    this.openid = openid;
  }


  public Integer getTotalFee() {
    return totalFee;
  }

  public void setTotalFee(Integer totalFee) {
    this.totalFee = totalFee;
  }


  public String getOrderId() {
    return orderId;
  }

  public void setOrderId(String orderId) {
    this.orderId = orderId;
  }


  public Integer getUserId() {
    return userId;
  }

  public void setUserId(Integer userId) {
    this.userId = userId;
  }


  public Integer getState() {
    return state;
  }

  public void setState(Integer state) {
    this.state = state;
  }


  public Integer getGoodsId() {
    return goodsId;
  }

  public void setGoodsId(Integer goodsId) {
    this.goodsId = goodsId;
  }


  public Integer getSpecsId() {
    return specsId;
  }

  public void setSpecsId(Integer specsId) {
    this.specsId = specsId;
  }


  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

}
