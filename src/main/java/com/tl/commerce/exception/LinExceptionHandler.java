package com.tl.commerce.exception;


import com.tl.commerce.domain.Resp;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 异常处理控制器
 */
@ControllerAdvice
public class LinExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Resp Handler(Exception e){
        e.printStackTrace();
        if(e instanceof WeChatException){
            e.printStackTrace();
            return Resp.fail().code(-1).msg("订单异常");
        }else{
            return Resp.fail().code(-1).msg(e.getMessage());
        }
    }


}
