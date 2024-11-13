<template>
    <div class="app-container">
      <nav>
        <ul>
          <li><router-link to="/start">開始遊戲</router-link></li>
          <li><router-link to="/user">用戶信息</router-link></li>
          <li><router-link to="/prizes">兌換獎品</router-link></li>
          <li class="logout"><a href="#" @click.prevent="logout">登出</a></li>
          <li class="name">{{ name }},您好</li>
        </ul>
      </nav>
      <router-view />
    </div>
  </template>
  
  <script>
  export default {
    data() {
      return {
        name: ''
      }
    },
    methods: {
      fetchName() {
          this.$axios.get('/user')
          .then(response => {
            if (response.data.success) {
              this.name = response.data.resultData.name;
            }
          })
          .catch(error => {
            console.error('獲取用戶姓名失敗:', error);
          });
      },
      logout() {
        localStorage.removeItem('token');
        this.$router.push({ name: 'LoginView' });
      }
    },
    created() {
      this.fetchName();
    }
  }
  </script>
  
  <style scoped>
  .app-container {
    max-width: 100%;
    margin: 0 auto;
  }
  nav ul {
    display: flex;
    justify-content: space-between;
    list-style: none;
    padding: 0;
    margin: 1rem 0;
    background-color: #42b983;
    border-radius: 5px;
  }
  nav ul li {
    margin: 0 1rem;
  }
  nav ul li a, nav ul li router-link {
    color: white;
    text-decoration: none;
    padding: 0.5rem 1rem;
    display: block;
  }
  nav ul li a:hover {
    background-color: #3a9a76;
  }
  .logout {
    margin-left: auto;
  }
  .name {
  color: white;
  padding: 0.5rem 1rem;
  }
  </style>
  