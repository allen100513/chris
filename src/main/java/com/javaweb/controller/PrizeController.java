package com.javaweb.controller;

import com.javaweb.pojo.Prize;
import com.javaweb.pojo.Result;
import com.javaweb.service.PrizeService;
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
    public Result deletePrize(@RequestParam Integer id) {
        prizeService.deletePrize(id);
        return Result.success();
    }

    @PutMapping("/prizes")
    public Result updatePrize(@RequestBody Prize prize) {
        prizeService.updatePrize(prize);
        return Result.success();
    }

    @PostMapping("/prizes")
    public Result addPrize(@RequestBody Prize prize) {
        prizeService.addPrize(prize);
        return Result.success(prize.getId());
    }
}
