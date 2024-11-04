package com.javaweb.controller;

import com.javaweb.pojo.Prize;
import com.javaweb.pojo.Result;
import com.javaweb.service.PrizeService;
import com.javaweb.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class PrizeController {

    @Autowired
    private PrizeService prizeService;

    @GetMapping("/prizes")
    public Result getPrizes() {
        List<Prize> list = prizeService.getPrizes();
        return Result.success(list);
    }

    @DeleteMapping("/prizes")
    public Result deletePrize(@RequestHeader("token") String token, @RequestParam Integer id) {
        Claims claims = JwtUtils.parseToken(token);
        Integer role = claims.get("role", Integer.class);
        if (role == 1) {
            prizeService.deletePrize(id);
            return Result.success();
        } else {
            return Result.error("權限不足");
        }
    }

    @PutMapping("/prizes")
    public Result updatePrize(@RequestHeader("token") String token, @RequestBody Prize prize) {
        Claims claims = JwtUtils.parseToken(token);
        Integer role = claims.get("role", Integer.class);
        if (role == 1) {
            prizeService.updatePrize(prize);
            return Result.success();
        } else {
            return Result.error("權限不足");
        }
    }

    @PostMapping("/prizes")
    public Result addPrize(@RequestHeader("token") String token, @RequestBody Prize prize) {
        Claims claims = JwtUtils.parseToken(token);
        Integer role = claims.get("role", Integer.class);
        if (role == 1) {
            prizeService.addPrize(prize);
            return Result.success(prize.getId());
        } else {
            return Result.error("權限不足");
        }
    }
}
