<template>
    <div class="login-container">
      <h1>登入頁面</h1>
      <form @submit.prevent="handleLogin">
        <div>
          <label for="username">帳號:</label>
          <input type="text" v-model="username" id="username" required>
        </div>
        <div>
            <label for="password">密碼:</label>
            <input type="password" v-model="password" id="password" required>
        </div>
        <div class="button-group">
            <button type="submit">登入</button>
            <button type="button" @click="goToRegister">註冊</button>
        </div>
      </form>
    </div>
  </template>
  
  <script>
  export default {
    data() {
      return {
        username: '',
        password: ''
      }
    },
    methods: {
      handleLogin() {
        this.$axios.post('/login', {
          username: this.username,
          password: this.password
        })
        .then(response => {
          if (response.data.success) {
            console.log('登入成功:', response.data.message);
            localStorage.setItem('token', response.data.resultData);
            console.log(response.data.resultData);
            this.$router.push({ name: 'StartGameView' });
          } else {
            console.error('登入失敗:', response.data.message);
            this.$Swal.fire({
            icon: 'error',
            title: '登入失敗',
            text: '帳號或密碼錯誤'
            });
          }
        })
        .catch(error => {
          console.error('登入失敗:', error);
          this.$Swal.fire({
          icon: 'error',
          title: '登入失敗',
          text: '伺服器錯誤，請稍後再試'
          });
        });
      },
      goToRegister() {
      // 跳轉到註冊頁面的邏輯
      this.$router.push({ name: 'RegisterView' });
    }
    }
  }
  </script>
  
  <style scoped>
  .login-container {
    max-width: 300px;
    margin: 0 auto;
    padding: 1rem;
    border: 1px solid #ccc;
    border-radius: 5px;
  }
  .login-container h1 {
    text-align: center;
  }
  .login-container div {
    margin-bottom: 1rem;
  }
  .login-container label {
    display: block;
    margin-bottom: 0.5rem;
  }
  .login-container input {
    width: 90%;
    padding: 0.5rem;
    border: 1px solid #ccc;
    border-radius: 5px;
  }
  .button-group {
  display: flex;
  justify-content: space-between;
}
.login-container button {
  width: 48%;
  padding: 0.5rem;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}
.login-container button[type="submit"] {
  background-color: #42b983;
  color: white;
}
.login-container button[type="button"] {
  background-color: #ccc;
  color: #333;
}
.login-container button:hover {
  opacity: 0.9;
}
  </style>
  