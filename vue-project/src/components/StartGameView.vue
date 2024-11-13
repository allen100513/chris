<template>
  <div class="game-container">
    <div class="header">
      <div class="difficulty-buttons">
        <button @click="setDifficulty('easy')" :disabled="isSwitchDisabled">簡單</button>
        <button @click="setDifficulty('medium')" :disabled="isSwitchDisabled">普通</button>
        <button @click="setDifficulty('hard')" :disabled="isSwitchDisabled">困難</button>
      </div>
      <div class="timer">目前遊戲時間為{{ timer }}秒</div>
    </div>
    <div class="left-panel">
      <div class="score-table">
        <h3>積分說明</h3>
        <table>
          <thead>
            <tr>
              <th>完成時間</th>
              <th>積分</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(points, time) in scoring" :key="time">
              <td>{{ time }} 秒內</td>
              <td>{{ points }} 分</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
    <div class="sudoku-grid">
      <div v-for="(row, rowIndex) in sudokuGrid" :key="rowIndex" class="row">
        <input
          v-for="(cell, colIndex) in row"
          :key="colIndex"
          type="text"
          v-model="sudokuGrid[rowIndex][colIndex]"
          :readonly="readonlyGrid[rowIndex][colIndex]"
          :class="{'error-cell': wrongGrid[rowIndex][colIndex], 'filled-cell': readonlyGrid[rowIndex][colIndex], 'empty-cell': !readonlyGrid[rowIndex][colIndex]}"
        />
      </div>
    </div>
    <div class="buttons">
      <button @click="startGame">開始遊戲</button>
      <button @click="submitAnswers" :disabled="isSubmitDisabled">提交答案</button>
      <button @click="shutGame">結束遊戲</button>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      timer: 0,
      interval: null,
      difficulty: 'medium',
      scoring: {},
      sudokuGrid: Array(9).fill('').map(() => Array(9).fill('')),
      readonlyGrid: Array(9).fill('').map(() => Array(9).fill('')),
      wrongGrid: Array(9).fill('').map(() => Array(9).fill('')),
      attempts: 0,
      isSubmitDisabled: true,
      isSwitchDisabled: false,
    };
  },
  methods: {
    setDifficulty(level) {
      this.difficulty = level;
      this.updateScoring();
    },
    updateScoring() {
      if (this.difficulty === 'easy') {
        this.scoring = { 60: 10, 120: 8, 180: 6 };
      } else if (this.difficulty === 'medium') {
        this.scoring = { 60: 15, 120: 12, 180: 9 };
      } else {
        this.scoring = { 60: 20, 120: 16, 180: 12 };
      }
    },
    startGame() {
      // this.resetTimer();
      // this.sudokuGrid = Array(9).fill('').map(() => Array(9).fill(''));
      this.wrongGrid = Array(9).fill('').map(() => Array(9).fill(''));
      this.attempts = 0;
      this.isSubmitDisabled = false;
      this.isSwitchDisabled = true;
      this.resetTimer();
      this.fetchSudokuGrid();
      this.interval = setInterval(() => {
        this.timer++;
      }, 1000);
    },
    resetTimer() {
      this.timer = 0;
      clearInterval(this.interval);
    },
    fetchSudokuGrid() {
      this.$axios.get('/start', {
        params: {
          difficulty: this.difficulty
        }
      })
      .then(response => {
        this.sudokuGrid = response.data.resultData.gameData;
        this.readonlyGrid = response.data.resultData.readonlyData;
      })
      .catch(error => {
        console.error('無法獲取數獨遊戲數據:', error);
      });
    },
    shutGame() {
      this.isSubmitDisabled = true;
      this.isSwitchDisabled = false;
      this.resetTimer();
      this.$Swal.fire("遊戲結束!");
    },
    submitAnswers() {
      for(let i=0;i<this.sudokuGrid.length;i++){
        for(let j=0;j<this.sudokuGrid[i].length;j++){
          if(this.sudokuGrid[i][j] == ""){
            this.$Swal.fire("還有未填的格子!");
            return;
          }
        }
      }
      this.$axios.post('/start', this.sudokuGrid)
        .then(response => {
          if (response.data.success) {
            this.updateUserScore();
            this.isSubmitDisabled = true;
            this.isSwitchDisabled = false;
            this.resetTimer();
            this.wrongGrid = Array(9).fill('').map(() => Array(9).fill(''));
          } else {
            this.attempts++;
            this.wrongGrid = response.data.resultData;
            if (this.attempts >= 3) {
              this.$Swal.fire('輸掉遊戲', '你已經錯誤3次', 'error');
              this.isSubmitDisabled = true;
              this.isSwitchDisabled = false;
              this.resetTimer();
              // this.resetGame();
            } else {
              this.$Swal.fire('答案錯誤', '請再試一次', 'error');
            }
          }
        })
        .catch(error => {
          console.error('無法提交答案:', error);
        });
    },
    updateUserScore() {
      const points = this.calculatePoints();
      this.$Swal.fire('恭喜完成', `你的成績是 ${this.timer} 秒,獲得了${points}分`, 'success');
      this.$axios.put('/start', { score: points, time: this.timer })
        .then(response => {
          console.log('用戶分數已更新:', response.data.message);
        })
        .catch(error => {
          console.error('無法更新用戶分數:', error);
        });
    },
    calculatePoints() {
      for (const [time, points] of Object.entries(this.scoring)) {
        if (this.timer <= time) {
          return points;
        }
      }
      return 0;
    }//,
    // resetGame() {
    //   this.resetTimer();
    //   // this.sudokuGrid = Array(9).fill('').map(() => Array(9).fill(''));
    //   this.wrongGrid = Array(9).fill('').map(() => Array(9).fill(''));
    //   this.attempts = 0;
    // }
  },
  mounted() {
    this.updateScoring();
  },
  beforeDestroy() {
    clearInterval(this.interval);
  }
};
</script>

<style scoped>
.game-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  height: 100vh;
}

.header {
  display: flex;
  justify-content: center;
  width: 100%;
  padding: 1rem;
}

.timer {
  font-size: 2rem;
  margin-left: 2rem;
}

.difficulty-buttons {
  display: flex;
  gap: 1rem;
}

.left-panel {
  position: absolute;
  left: 0;
  top: 50%;
  transform: translateY(-50%);
  padding: 1rem;
}

.score-table {
  text-align: left;
  border: 1px solid #ccc;
  padding: 1rem;
  border-radius: 5px;
}

.score-table table {
  width: 100%;
  border-collapse: collapse;
}

.score-table th,
.score-table td {
  border: 1px solid #ccc;
  padding: 0.5rem;
  text-align: left;
}

.sudoku-grid {
  display: grid;
  grid-template-columns: repeat(9, 1fr);
  gap: 0;
  margin: 2rem 0;
  position: relative;
}

.sudoku-grid .row {
  display: contents;
}

.sudoku-grid input {
  width: 2rem;
  height: 2rem;
  text-align: center;
  border: 1px solid #ccc;
  margin: 0;
  box-sizing: border-box;
}

.buttons {
  display: flex;
  gap: 1rem;
}

button {
  padding: 0.5rem 1rem;
  background-color: #42b983;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

button:hover {
  background-color: #3a9a76;
}

.filled-cell {
  background-color: #e0e0e0;
}

.empty-cell {
  background-color: #fff;
  border: 2px solid #000 !important; /* 加粗外框線，並設置為黑色 */
  box-sizing: border-box; /* 確保邊框包含在元素的總寬度和高度內 */
}

.error-cell {
  background-color: #f8d7da;
}

.sudoku-grid {
  border: 3px solid #000;
}
.sudoku-grid input:nth-child(3n+1) {
  border-left: 3px solid #000;
}
.sudoku-grid input:nth-child(3n) {
  border-right: 3px solid #000;
}
.sudoku-grid .row:nth-child(3n+1) input {
  border-top: 3px solid #000;
}
.sudoku-grid .row:nth-child(3n) input {
  border-bottom: 3px solid #000;
}
</style>

