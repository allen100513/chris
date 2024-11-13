package com.javaweb.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Sudoku {
    public static int[][] problem=new int[9][9];
    public static Boolean[][] readonly=new Boolean[9][9];

    public static void generate() {
        int ans=0;
        while (ans==0) {
            out:for (int i = 0; i < 9; i++) {
                    for (int j = 0; j < 9; j++) {
                        List<Integer> nums = new ArrayList<>();
                        for (int o = 1; o <= 9; o++) {
                            if (isSolved(i, j, o)) {
                                nums.add(o);
                            }
                        }
                        if (!nums.isEmpty()) {
                            problem[i][j] = nums.get((int)(Math.random() * nums.size()));
                        }else{
                            problem=new int[9][9];
                           break out;
                        }
                    }
            }
            if(problem[8][8]!=0){
                ans=1;
            }
        }
    }

    public static void hollow(int num) {
        List<Integer> nums = new ArrayList<>();
        for (int i = 0; i <= 80; i++) {
            nums.add(i);
        }
        List<Integer> hollowNums = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            int index = (int)(Math.random() * nums.size());
            int numToRemove = nums.remove(index);
            hollowNums.add(numToRemove);
        }
        for (int i = 0; i < hollowNums.size(); i++) {
            int index = hollowNums.get(i);
            int row = index / 9;
            int col = index % 9;
            problem[row][col] = 0;
        }
    }
    public static void readonlyjudge() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (problem[i][j] != 0) {
                    readonly[i][j] = true;
                } else {
                    readonly[i][j] = false;
                }
            }
        }
    }

    public static void display() {
        for (int i=0; i<9; i++){
            for (int j=0; j<9; j++){
                if(problem[i][j]==0){
                    System.out.print("* ");
                }else {
                    System.out.print(problem[i][j] + " ");
                }
            }
            System.out.println();
        }
    }
    public static boolean isSolved(int m,int n,int num) {
        List<Integer> rowList = new ArrayList<>();
        for (int value : problem[m]) {
            rowList.add(value);
        }
        if (rowList.contains(num)) {
            return false;
        }
        List<Integer> colList = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            colList.add(problem[i][n]);
        }
        if (colList.contains(num)) {
            return false;
        }
        int i = m / 3;
        int j = n / 3;
        List<Integer> subList = new ArrayList<>();
        for (int x = 3 * i; x < 3 * i + 3; x++) {
            for (int y = 3 * j; y < 3 * j + 3; y++) {
                subList.add(problem[x][y]);
            }
        }
        if (subList.contains(num)) {
            return false;
        }

        return true;
    }

    public static Set<String> findDuplicates(List<String> list) {
        Set<String> seen = new HashSet<>();
        return list.stream()
                .filter(n -> !seen.add(n))
                .collect(Collectors.toSet());
    }

    public static Boolean[][] wrongGridGen(String[][] sudokuGrid){
        Boolean[][] wrongGrid=new Boolean[9][9];
        for(int i=0; i<9; i++){
            List<String> list=new ArrayList<>();
            for(int j=0; j<9; j++){
                list.add(sudokuGrid[i][j]);
            }
            Set<String> duplicates = new HashSet<>();
            duplicates = findDuplicates(list);
            for(int j=0; j<9; j++){
                if(duplicates.contains(sudokuGrid[i][j])){
                    wrongGrid[i][j]=true;
                }else{
                    wrongGrid[i][j]=false;
                }
            }
        }
        for(int j=0; j<9; j++){
            List<String> list=new ArrayList<>();
            for(int i=0; i<9; i++){
                list.add(sudokuGrid[i][j]);
            }
            Set<String> duplicates = new HashSet<>();
            duplicates = findDuplicates(list);
            for(int i=0; i<9; i++){
                if(duplicates.contains(sudokuGrid[i][j])){
                    wrongGrid[i][j]=true;
                }else{
                    if (wrongGrid[i][j]==null) {
                        wrongGrid[i][j]=false;
                    }
                }
            }
        }
        for(int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                List<String> list = new ArrayList<>();
                for (int i = 3 * x; i < 3 * x + 3; i++) {
                    for (int j = 3 * y; j < 3 * y + 3; j++) {
                        list.add(sudokuGrid[i][j]);
                    }
                }
                Set<String> duplicates = new HashSet<>();
                duplicates = findDuplicates(list);
                for (int i = 3 * x; i < 3 * x + 3; i++) {
                    for (int j = 3 * y; j < 3 * y + 3; j++) {
                        if (duplicates.contains(sudokuGrid[i][j])) {
                            wrongGrid[i][j] = true;
                        } else {
                            if (wrongGrid[i][j] == null) {
                                wrongGrid[i][j] = false;
                            }
                        }
                    }
                }

            }
        }
        return wrongGrid;
    }
}
