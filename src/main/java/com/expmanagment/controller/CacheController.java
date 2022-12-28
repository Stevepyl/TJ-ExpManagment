package com.expmanagment.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "缓存管理")
@RestController
@RequestMapping("cache")
@AllArgsConstructor
@CrossOrigin
@EnableCaching
public class CacheController {

    @ApiOperation("清除所有缓存")
    @DeleteMapping("/delete")
    @CacheEvict(value = "*",allEntries = true)
    public JSONObject deleteAll()
    {
        JSONObject jsonObject =new JSONObject();
        jsonObject.put("code",200);
        jsonObject.put("msg","删除成功");
        return jsonObject;
    }
}
