<template>
    <div class="register-container">
      <h1>註冊頁面</h1>
      <form @submit.prevent="handleRegister">
        <div>
          <label for="username">帳號:</label>
          <input type="text" v-model="username" id="username" required>
        </div>
        <p v-if="usernameError" class="error">{{ usernameError }}</p>
        <div>
          <label for="password">密碼:</label>
          <input type="password" v-model="password" id="password" required>
        </div>
        <p v-if="passwordError" class="error">{{ passwordError }}</p>
        <div>
          <label for="name">姓名:</label>
          <input type="text" v-model="name" id="name" required>
        </div>
        <p v-if="nameError" class="error">{{ nameError }}</p>
        <div>
          <label for="birthDate">生日:</label>
          <input type="date" v-model="birthDate" id="birthDate" required>
        </div>
        <div class="button-group">
          <button type="submit">註冊</button>
          <button type="button" @click="cancelRegister">取消</button>
        </div>
      </form>
    </div>
  </template>
  
  <script>
  export default {
    data() {
      return {
        username: '',
        password: '',
        name: '',
        birthDate: '',
        usernameError: '',
        passwordError: '',
        nameError: ''
      }
    },
    methods: {
      handleRegister() {
        this.usernameError = '';
        this.passwordError = '';
        this.nameError = '';

        let isValid = true;
        if (!this.isUsernameValid(this.username)) {
        this.usernameError = '帳號必須介於8-15個字之間';
        isValid = false;
        }
        if (!this.isPasswordValid(this.password)) {
        this.passwordError = '密碼必須包含至少一個字母和一個數字,且介於8-15個字之間';
        isValid = false;
        }
        if (!this.isNameValid(this.name)) {
        this.nameError = '姓名不能超過10個字';
        isValid = false;
        }
        if (!isValid) {
        return;
        }
        this.$axios.post('/regis', {
          username: this.username,
          password: this.password,
          name: this.name,
          birthDate: this.birthDate
        })
        .then(response => {
          if (response.data.success) {
            this.$Swal.fire("註冊成功!");
            console.log('註冊成功:', response.data.message);
            this.usernameError = '';
            this.passwordError = '';
            this.nameError = '';
            this.$router.push({ name: 'LoginView' });
            // 根據需要進一步處理
            // 例如，導航到登錄頁面或顯示成功訊息
          } else if (response.data.message == "帳號已存在") {
            this.$Swal.fire("帳號已存在!");
          }else {
            console.error('註冊失敗:', response.data.message);
            // 顯示失敗訊息或進行其他錯誤處理
          }
        })
        .catch(error => {
          console.error('註冊失敗:', error);
        });
      },
      isPasswordValid(password) {
      const letter = /[a-zA-Z]/;
      const number = /[0-9]/;
      return password.length <= 15 && password.length >= 8 && letter.test(password) && number.test(password);
      },
      isUsernameValid(username) {
      return username.length <= 15 && username.length >= 8;
      },
      isNameValid(name) {
      return name.length <= 10;
      },
      cancelRegister() {
        // 跳轉回登入頁面
        this.$router.push({ name: 'LoginView' });
      }
    }
  }
  </script>
  
  <style scoped>
  .register-container {
    max-width: 300px;
    margin: 0 auto;
    padding: 1rem;
    border: 1px solid #ccc;
    border-radius: 5px;
  }
  .register-container h1 {
    text-align: center;
  }
  .register-container div {
    margin-bottom: 1rem;
  }
  .register-container label {
    display: block;
    margin-bottom: 0.5rem;
  }
  .register-container input {
    width: 90%;
    padding: 0.5rem;
    border: 1px solid #ccc;
    border-radius: 5px;
  }
  .button-group {
    display: flex;
    justify-content: space-between;
  }
  .register-container button {
    width: 48%;
    padding: 0.5rem;
    border: none;
    border-radius: 5px;
    cursor: pointer;
  }
  .register-container button[type="submit"] {
    background-color: #42b983;
    color: white;
  }
  .register-container button[type="button"] {
    background-color: #ccc;
    color: #333;
  }
  .register-container button:hover {
    opacity: 0.9;
  }
  .error {
  color: red;
  text-align: center;
  margin-top: 1rem;
}
  </style>
  