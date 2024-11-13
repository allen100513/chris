<template>
    <div class="container">
      <h2>購物車</h2>
      <div v-if="carts.length === 0">購物車裡沒有物品</div>
      <div v-else>
        <div v-for="(item, index) in carts" :key="index" class="cart-item">
          <img :src="item.imgSrc" alt="獎品圖片" class="cart-imgSrc" />
          <div class="cart-info">
            <h3>{{ item.name }}</h3>
            <div class="counter">
              <button @click="decrementQuantity(index)">-</button>
              <input type="text" v-model.number="item.quantity" />
              <button @click="incrementQuantity(index)">+</button>
            </div>
          </div>
        </div>
        <dl>
          <dt>總消耗紅利:</dt>
          <dd>{{ totalPoints }} 點</dd>
          <dt>剩餘紅利:</dt>
          <dd>{{ remainingPoints }} 點</dd>
        </dl>
        <div class="button-group">
          <button @click="redeem">兌換</button>
          <button @click="exit">退出</button>
        </div>
      </div>
    </div>
</template>
  
<script>
  export default {
    data() {
      return {
        carts: [],
        remainingPoints: 0 // 假設用戶有1000點紅利
      }
    },
    computed: {
      totalPoints() {
        return this.carts.reduce((total, item) => total + (item.cost * item.quantity), 0);
      }
    },
    methods: {
      incrementQuantity(index) {
        this.carts[index].quantity++;     
      },
      decrementQuantity(index) {
        if (this.carts[index].quantity > 0) this.carts[index].quantity--;
      },
      redeem() {
        if (this.totalPoints > this.remainingPoints) {
          this.$Swal.fire("紅利點數不足!");
          return;
        }
        for(const item of this.carts){
          this.$axios.put('/cart', {
          id: item.id,
          quantity: 0
          })
          .then(response => {
            if (response.data.success) {
              console.log('更改成功');
            }
          })
          .catch(error => {
            console.error('更改失敗:', error);
          });
        }

        this.$axios.put('/user',{
          bonus: this.remainingPoints - this.totalPoints
        })
        .then(res => {
          if (res.data.success){
            this.$Swal.fire("兌換成功!");
          }
        })
        .catch(error => {
            console.error('兌換失敗:', error);
        });
        this.$router.push({ name: 'RedeemPrizesView' });
      },
      exit() {
        for(const item of this.carts){
          this.$axios.put('/cart', {
          id: item.id,
          quantity: item.quantity
          })
          .then(response => {
            if (response.data.success) {
              console.log('更改成功');
            }
          })
          .catch(error => {
            console.error('更改失敗:', error);
          });
        }
        this.$router.push({ name: 'RedeemPrizesView' });
      }
    },
    mounted() {
      this.$axios.get('/cart')
      .then(res => {
        if (res.data.success) {
          const cartsFromBackend = res.data.resultData;
          this.carts = cartsFromBackend.map(cart => ({
            id: cart.id,
            name: cart.name,
            cost: cart.cost,
            imgSrc: cart.imgSrc,
            quantity: cart.quantity
          }));
        }
      })
      .catch(error => {
        console.error('獲取購物車失敗:', error);
      });

      this.$axios.get('/user')
      .then(res => {
        if (res.data.success) {
          if (res.data.resultData.bonus == null){
            this.remainingPoints = 0;
          }else{
            this.remainingPoints = res.data.resultData.bonus;
          }
        }
      })
      .catch(error => {
        console.error('獲取紅利點數失敗:', error);
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
  .cart-item {
    display: flex;
    margin-bottom: 1rem;
  }
  .cart-imgSrc {
    width: 100px;
    height: 100px;
    margin-right: 1rem;
  }
  .cart-info {
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
  dl {
    display: grid;
    grid-template-columns: auto auto;
    gap: 0.5rem;
    width: 200px;
    margin-top: 1rem;
  }
  .button-group {
    display: flex;
    justify-content: space-between;
    width: 200px; /* 調整根據需要 */
    margin-top: 1rem;
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
</style>
  