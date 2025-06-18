/*
package icu.lry.ordersystem.utils;

import icu.lry.managesystem.pojo.User;
import icu.lry.managesystem.service.UserService;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
public class RefreshTokenUtils {

    public Result refreshToken(String token, UserService userService, StringRedisTemplate stringRedisTemplate) {
        try {
            JwtUtils.parseJWT(token);
        } catch (Exception e) {
            return deleteNotUseRedis(stringRedisTemplate, token, "NOT_LOGIN");
        }
        Result result = deleteNotUseRedis(stringRedisTemplate, token, "NOT_LOGIN");
        if (result.getMsg().equals("error")) {
            return result;
        }
        Claims claim = JwtUtils.parseJWT(token);
        Object date = claim.get("date");
        Long oldTime = Long.parseLong(date.toString());
        Long newTime = System.currentTimeMillis();
        if (newTime - oldTime > 1000 * 60 * 60 * 24) {
            return deleteNotUseRedis(stringRedisTemplate, token, "长时间未操作");
        }
        if(newTime - oldTime < 1000 * 60) {
            return Result.success("无需更新令牌");
        }
        String idStr = claim.get("id").toString();
        Integer id = Integer.parseInt(idStr);
        List<User> userList = userService.selectUserById(id);
        HashMap<String, Object> claims = new HashMap<>();
        claims.put("id", userList.get(0).getId());
        claims.put("username", userList.get(0).getUsername());
        claims.put("password", userList.get(0).getPassword());
        claims.put("date", new Date().getTime());
        userList.get(0).setToken(JwtUtils.generateJwt(claims));
        stringRedisTemplate.opsForHash().put("user", userList.get(0).getId() + " " + new Date().getTime(), JwtUtils.generateJwt(claims));
        if (stringRedisTemplate.opsForHash().get("RedisPojo", token).toString() != null) {
            stringRedisTemplate.opsForHash().delete("RedisPojo", token);
        }
        stringRedisTemplate.opsForHash().put("RedisPojo", JwtUtils.generateJwt(claims), "1");
        return Result.success(JwtUtils.generateJwt(claims));
    }

    public Result deleteNotUseRedis(StringRedisTemplate stringRedisTemplate, String token, String msg) {
        Set<Object> user = stringRedisTemplate.opsForHash().keys("user");
        log.info(token);
        if (stringRedisTemplate.opsForHash().get("RedisPojo", token).toString().equals("0")) {
            stringRedisTemplate.opsForHash().delete("RedisPojo", token);
            for (Object o : user) {
                if (stringRedisTemplate.opsForHash().get("user", o).toString().equals(token)) {
                    stringRedisTemplate.opsForHash().delete("user", o);
                }
            }
            return Result.error(msg);
        }
        return Result.success("");
    }
}
*/
