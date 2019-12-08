package com.tl.commerce.domain;


/**
 * 用户收获地址
 */
public class Address {

  private Integer id;
  private Integer idDefault;
  private String provice;
  private String detail;
  private Integer userId;


  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }


  public Integer getIdDefault() {
    return idDefault;
  }

  public void setIdDefault(Integer idDefault) {
    this.idDefault = idDefault;
  }


  public String getProvice() {
    return provice;
  }

  public void setProvice(String provice) {
    this.provice = provice;
  }


  public String getDetail() {
    return detail;
  }

  public void setDetail(String detail) {
    this.detail = detail;
  }


  public Integer getUserId() {
    return userId;
  }

  public void setUserId(Integer userId) {
    this.userId = userId;
  }

}
