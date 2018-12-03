package com.homework.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.homework.entity.Post;
import com.homework.entity.User;
import com.homework.shiro.AccountProfile;
import com.homework.utils.Constant;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.Account;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
        User user = userService.getById(getProfileId());
        user.setPassword(null);

        req.setAttribute("user", user);
        return "user/setting";
    }

    @ResponseBody
    @PostMapping("/setting")
    public R postSetting(User user) {

        User tempUser = userService.getById(getProfileId());
//        tempUser.setEmail(user.getEmail());
        tempUser.setUsername(user.getUsername());
        tempUser.setGender(user.getGender());
        tempUser.setSign(user.getSign());

        boolean isSucc = userService.updateById(tempUser);
        if(isSucc) {
            //更新shiro的信息
            AccountProfile profile = getProfile();
            profile.setUsername(user.getUsername());
            profile.setGender(user.getGender());
        }

        return isSucc ? R.ok(user): R.failed("更新失败");
    }

    @ResponseBody
    @PostMapping("/upload")
    public R upload(@RequestParam(value = "file") MultipartFile file) {

        if(file.isEmpty()) {
            return R.failed("上传失败");
        }

        // 获取文件名
        String fileName = file.getOriginalFilename();
        log.info("上传的文件名为：" + fileName);
        // 获取文件的后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        log.info("上传的后缀名为：" + suffixName);
        // 文件上传后的路径
        String filePath = Constant.uploadDir;

        fileName = "avatar_" + getProfileId() + suffixName;
        File dest = new File(filePath + fileName);
        // 检测是否存在目录
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            file.transferTo(dest);
            log.info("上传成功后的文件路径未：" + filePath + fileName);

            String path = filePath + fileName;
            String url = "http://localhost:10080/upload/" + fileName;

            log.info("url ---> {}", url);

            User current = userService.getById(getProfileId());
            current.setAvatar(url);
            userService.updateById(current);

            //更新shiro的信息
            AccountProfile profile = getProfile();
            profile.setAvatar(url);

            return R.ok(url);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return R.ok(null);
    }


    @ResponseBody
    @PostMapping("/resetPwd")
    public R resetPwd(String nowpass, String pass) {

        User user = userService.getById(getProfileId());

        String nowPassMd5 = SecureUtil.md5(nowpass);
        if(!nowPassMd5.equals(user.getPassword())) {
            return R.failed("密码不正确");
        }

        user.setPassword(SecureUtil.md5(pass));
        userService.updateById(user);

        return R.ok(null);
    }


    @GetMapping("/message")
    public String message() {
        return "user/message";
    }

}
