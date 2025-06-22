package icu.lry.ordersystem.controller;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import icu.lry.ordersystem.pojo.WxUserInfo;
import icu.lry.ordersystem.service.WxUserInfoServicePlus;
import icu.lry.ordersystem.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/wxUserInfo")
@Slf4j
public class WxUserInfoController {

    @Autowired
    private WxUserInfoServicePlus wxUserInfoServicePlus;

    @Value("${wx.appid}")
    private String appid;

    @Value("${wx.secret}")
    private String secret;

    @PostMapping("/login")
    public Result login(@RequestBody WxUserInfo wxUserInfo) {
        List<WxUserInfo> users = wxUserInfoServicePlus.list();
        LambdaUpdateWrapper<WxUserInfo> luw = new LambdaUpdateWrapper<>();
        for (WxUserInfo user : users) {
            if(user.getOpenid().equals(wxUserInfo.getOpenid())) {
                luw.set(WxUserInfo::getLastLoginDate, LocalDateTime.now());
                luw.eq(WxUserInfo::getId, user.getId());
                boolean flag = wxUserInfoServicePlus.update(luw);
                if(flag) {
                    LambdaQueryWrapper<WxUserInfo> lqw = new LambdaQueryWrapper<>();
                    lqw.eq(WxUserInfo::getId, user.getId());
                    WxUserInfo user1 = wxUserInfoServicePlus.getOne(lqw);
                    return Result.success(user1);
                }
            }
        }
        LocalDateTime ldt = LocalDateTime.now();
        wxUserInfo.setRegisterDate(ldt);
        wxUserInfo.setLastLoginDate(ldt);
        wxUserInfoServicePlus.save(wxUserInfo);
        return Result.success(wxUserInfo);
    }

    @RequestMapping("/updateNickName")
    public Result updateNickName(@RequestBody WxUserInfo wxUserInfo) {
        LambdaUpdateWrapper<WxUserInfo> luw = new LambdaUpdateWrapper<>();
        luw.eq(WxUserInfo::getId, wxUserInfo.getId());
        luw.set(WxUserInfo::getNickName, wxUserInfo.getNickName());
        boolean flag = wxUserInfoServicePlus.update(luw);
        if(!flag) {
            return Result.error("更新失败");
        }
        return Result.success("更新成功");
    }

    @RequestMapping("/updateAvatar")
    public Result updateAvatar(@RequestBody WxUserInfo wxUserInfo) {
        LambdaUpdateWrapper<WxUserInfo> luw = new LambdaUpdateWrapper<>();
        luw.eq(WxUserInfo::getId, wxUserInfo.getId());
        luw.set(WxUserInfo::getAvatarUrl, wxUserInfo.getAvatarUrl());
        boolean flag = wxUserInfoServicePlus.update(luw);
        if(!flag) {
            return Result.error("更新失败");
        }
        return Result.success("更新成功");
    }

    @GetMapping("/getLoginInfo/{code}")
    public Result test(@PathVariable String code) {
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid="+ appid +"&secret="+ secret +"&js_code="+ code +"&grant_type=authorization_code";
        try{
            URL urlObj = new URL(url);
            //开连接
            URLConnection connection = urlObj.openConnection();
            InputStream is = connection.getInputStream();
            byte[] b = new byte[1024];
            int len;
            StringBuilder stb = new StringBuilder();
            while((len=is.read(b)) != -1) {
                stb.append(new String(b, 0, len));
            }
            JSONObject parse = JSONObject.parse(stb.toString());
            return Result.success(parse);
        } catch (Exception e){
            e.printStackTrace();
        }
        return Result.error("获取失败");
    }
}
