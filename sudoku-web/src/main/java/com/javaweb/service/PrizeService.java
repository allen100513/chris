package com.javaweb.service;

import com.javaweb.pojo.Prize;

import java.util.List;

public interface PrizeService {
    List<Prize> getPrizes();

    void deletePrize(Integer id);

    void updatePrize(Prize prize);

    void addPrize(Prize prize);
}
