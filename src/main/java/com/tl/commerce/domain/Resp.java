package com.tl.commerce.domain;

import java.io.Serializable;

/**
 * 返回json
 */
public class Resp implements Serializable {

    private static final long serialVersionUID = -3102333513692702549L;

    public static Resp fail() {
        Resp resp = new Resp(RespResult.FAIL);
        return resp;
    }

    public static Resp fail(String msg) {
        return fail().msg(msg);
    }

    public static Resp ok() {
        Resp resp = new Resp(RespResult.OK);
        return resp;
    }

    public static Resp ok(Object data) {
        return ok().data(data);
    }

    /**
     * 结果：成功、失败或未知
     */
    private RespResult result;

    /**
     * 消息，一般用于显示
     */
    private String msg;

    /**
     * 业务数据，譬如分页数据，用户信息数据等
     */
    private Object data;

    /**
     * 业务编码：一般是在失败情况下会用到这个，以便告知用户失败的原因是什么
     */
    private Integer code;


    private Resp(RespResult respCode) {
        this.result = respCode;
    }

    public Resp code(Integer code) {
        this.setCode(code);
        return this;
    }

    public Resp data(Object data) {
        this.setData(data);
        return this;
    }

    public Integer getCode() {
        return code;
    }

    public Object getData() {
        return data;
    }

    public String getMsg() {
        return msg;
    }

    public boolean isOk() {
        return this.result == RespResult.OK;
    }

    public Resp msg(String msg) {
        this.setMsg(msg);
        return this;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
