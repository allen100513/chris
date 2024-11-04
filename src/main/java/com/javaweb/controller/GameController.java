package com.javaweb.controller;

import com.javaweb.pojo.GameScore;
import com.javaweb.service.UserService;
import com.javaweb.utils.Sudoku;
import com.javaweb.utils.Sudokudojo;
import com.javaweb.pojo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class GameController {

    @Autowired
    private UserService userService;

    @GetMapping("/start")
    public Result newGame(@RequestParam String difficulty) {
        System.out.println(difficulty);
        int hollowNum = 0;
        switch (difficulty) {
            case "easy":
                hollowNum = 3;
                break;
            case "medium":
                hollowNum = 5;
                break;
            case "hard":
                hollowNum = 55;
                break;
        }

        Sudokudojo sdk = new Sudokudojo();
        Sudoku.generate();
        Sudoku.hollow(hollowNum);
        Sudoku.readonlyjudge();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (Sudoku.problem[i][j] == 0) {
                    sdk.gameData[i][j] = "";
                }else{
                sdk.gameData[i][j] = String.valueOf(Sudoku.problem[i][j]);
                }
            }
        }
        sdk.readonlyData =Sudoku.readonly;
        log.info("遊戲產生成功");
        return Result.success(sdk);
    }

    @PostMapping("/start")
    public Result judgement(@RequestBody String[][] sudokuGrid) {
        Boolean[][] wrongGrid = Sudoku.wrongGridGen(sudokuGrid);
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (wrongGrid[i][j]) {
                    return Result.error("請輸入正確的數字",wrongGrid);
                }
            }
        }
        return Result.success();
    }

    @PutMapping("/start")
    public Result receiveGameData(@RequestHeader("token") String token, @RequestBody GameScore gameScore) {
        userService.saveGameScore(token, gameScore);
        return Result.success();
    }


}
