package com.example.demo.config.exception;

import com.example.demo.util.JsonData;
import com.example.demo.util.exception.CustomException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 这个是给所有的Controller添加了一个Advice
 */
@ControllerAdvice
public class CustomExceptionHandler {

    //所有的Controller如果有异常则直接调用这里的handler方法
    @ExceptionHandler(value= Exception.class)
    @ResponseBody
    public JsonData handler(Exception e){
      if(e instanceof CustomException){
          CustomException customException=(CustomException)e;
          return JsonData.buildError(customException.getMsg(),customException.getCode());
      }else{
          return JsonData.buildError(e.getMessage());
      }
    }
}
