package com.javaweb.service.impl;

import com.javaweb.mapper.PrizeMapper;
import com.javaweb.pojo.Prize;
import com.javaweb.service.PrizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrizeServiceImpl implements PrizeService {

    @Autowired
    private PrizeMapper prizeMapper;

    @Override
    public List<Prize> getPrizes() {

        List<Prize> list = prizeMapper.selectAll();
        return list;
    }

    @Override
    public void deletePrize(Integer id) {
        prizeMapper.deleteById(id);
    }

    @Override
    public void updatePrize(Prize prize) {
        prizeMapper.update(prize);
    }

    @Override
    public void addPrize(Prize prize) {
        prizeMapper.insert(prize);
    }
}
