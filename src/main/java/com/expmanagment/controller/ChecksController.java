package com.expmanagment.controller;


import cn.dev33.satoken.annotation.SaCheckLogin;
import com.alibaba.fastjson.JSONObject;
import com.expmanagment.entity.ChecksEntity;
import com.expmanagment.service.ChecksService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Api(tags = "教师批改记录管理")
@RestController
@AllArgsConstructor
@RequestMapping("/checks")
@CrossOrigin
public class ChecksController {
    private ChecksService checksService;

    @SaCheckLogin
    @ApiOperation("新增批改记录")
    @PostMapping("/add")
    public JSONObject add(@RequestBody ChecksEntity checks) {
        return checksService.add(checks);
    }
}
