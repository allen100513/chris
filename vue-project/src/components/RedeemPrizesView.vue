<template>
  <div class="container">
    <h2>兌換獎品</h2>
    <div v-for="(prize, index) in paginatedPrizes" :key="index" class="prize-item">
      <img :src="prize.imgSrc" alt="獎品圖片" class="prize-imgSrc" />
      <div class="prize-info">
        <h3>{{ prize.name }}</h3>
        <p>需要紅利: {{ prize.cost }} 點</p>
        <div class="counter">
          <button @click="decrementCount(index)">-</button>
          <input type="text" v-model.number="prize.quantity" />
          <button @click="incrementCount(index)">+</button>
        </div>
        <button @click="addToCart(index)">加入購物車</button>
      </div>
    </div>
    <div class="button-group">
      <button v-if="isAdmin" @click="goToEdit">編輯獎品</button>
      <button @click="goToCart">結算</button>
    </div>
    <el-pagination
      @current-change="handlePageChange"
      :current-page="currentPage"
      :page-size="pageSize"
      :total="prizes.length"
      layout="total, prev, pager, next, jumper"
    ></el-pagination>
  </div>
</template>

<script>
export default {
  data() {
    return {
      prizes: [],
      role: 0,
      currentPage: 1,
      pageSize: 3
    }
  },
  computed: {
    isAdmin() {
      return this.role === 1;
    },
    paginatedPrizes() {
      const start = (this.currentPage - 1) * this.pageSize;
      const end = start + this.pageSize;
      return this.prizes.slice(start, end);
    }
  },
  methods: {
    incrementCount(index) {
      const actualIndex = (this.currentPage - 1) * this.pageSize + index;
      this.prizes[actualIndex].quantity++;
    },
    decrementCount(index) {
      const actualIndex = (this.currentPage - 1) * this.pageSize + index;
      if (this.prizes[actualIndex].quantity > 0) this.prizes[actualIndex].quantity--;
    },
    addToCart(index) {
      const actualIndex = (this.currentPage - 1) * this.pageSize + index;
      if (this.prizes[actualIndex].quantity == 0) {
        return;
      }
      this.$axios.post('/cart', {
        prizeId: this.prizes[actualIndex].id,
        quantity: this.prizes[actualIndex].quantity
      })
      .then(response => {
        if (response.data.success) {
          this.$Swal.fire("成功加入購物車!");
          this.prizes[actualIndex].quantity = 0;
        } else {
          console.error('加入購物車失敗:', response.data.message);
          // 顯示失敗訊息或進行其他錯誤處理
        }
      })
      .catch(error => {
        console.error('加入購物車失敗:', error);
      });
    },
    goToCart() {
      this.$router.push({ name: 'CartView'});
    },
    goToEdit() {
      this.$router.push({ name: 'EditPrizesView'});
    },
    handlePageChange(page) {
      this.currentPage = page;
    }
  },
  mounted() {
    this.$axios.get('/prizes')
    .then(res => {
      if (res.data.success) {
        const prizesFromBackend = res.data.resultData;
        this.prizes = prizesFromBackend.map(prize => ({
          id: prize.id,
          name: prize.name,
          cost: prize.cost,
          imgSrc: prize.imgSrc,
          quantity: 0
        }));
      }
    })
    .catch(error => {
      console.error('獲取獎品失敗:', error);
    });

    this.$axios.get('/user')
    .then(response => {
      if (response.data.success) {
        this.role = response.data.resultData.role;
      }
    })
    .catch(error => {
      console.error('獲取用戶角色失敗:', error);
    });
  }
}
</script>

<style scoped>
.container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100vh;
}
.button-group { 
  display: flex; 
  gap: 1rem; 
  margin-top: 1rem; 
}
.prize-item {
  display: flex;
  margin-bottom: 1rem;
}
.prize-imgSrc {
  width: 100px;
  height: 100px;
  margin-right: 1rem;
}
.prize-info {
  display: flex;
  flex-direction: column;
  justify-content: center;
}
.counter {
  display: flex;
  align-items: center;
  margin-bottom: 0.5rem;
}
.counter button {
  width: 30px;
  height: 30px;
}
.counter input {
  width: 50px;
  text-align: center;
  margin: 0 0.5rem;
}
</style>
