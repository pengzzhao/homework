package com.homework.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.homework.entity.Post;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/user")
public class CenterController extends BaseController{

    @GetMapping("/center")
    public String center( @RequestParam(defaultValue = "1") Integer current,
                          @RequestParam(defaultValue = "10")Integer size, String tab) {

        Page<Post> page = new Page<>();
        page.setCurrent(current);
        page.setSize(size);

        log.info("-------------->进入个人中心");

        QueryWrapper<Post> wrapper = new QueryWrapper<Post>().eq("user_id", getProfileId()).orderByDesc("created");
        if(StrUtil.isNotBlank(tab)) {
//            wrapper.eq();
        }

        IPage<Map<String, Object>> pageData = postService.pageMaps(page, wrapper);
        req.setAttribute("pageData", pageData);

        return "user/center";
    }

    @ResponseBody
    @PostMapping("/message/nums")
    public Object getMessNums() {
        Map<String, Object> result = new HashMap<>();
        result.put("status", 0);
        result.put("count", 2);

        return result;
    }

    @GetMapping("/setting")
    public String setting() {
        return "user/setting";
    }

    @GetMapping("/message")
    public String message() {
        return "user/message";
    }

}
