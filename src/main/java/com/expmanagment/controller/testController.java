package com.expmanagment.controller;

import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Api("用户注册登录接口")
@RestController
@RequestMapping("/test")
public class testController {

    @ApiOperation("校验登录是否成功")
    @PostMapping("/show1")
    public String test(
                       Integer denglu) {
        return denglu.toString();
    }

}
