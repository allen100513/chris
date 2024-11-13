import Vue from 'vue'
import App from './App.vue'
import router from './router'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import axios from 'axios';
import Swal from 'sweetalert2'

Vue.config.productionTip = false
Vue.use(ElementUI);

// 設置 axios 攔截器
axios.interceptors.request.use(config => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.token = token;
  }
  return config;
}, error => {
  return Promise.reject(error);
});


axios.interceptors.response.use(response => {
  if (response.data.message === 'NOT_LOGIN') {
    router.push({ name: 'LoginView' },()=>{},()=>{});
  }
  return response;
}, error => {
  return Promise.reject(error);
});

Vue.prototype.$axios = axios;
Vue.prototype.$Swal = Swal;

new Vue({
  router,
  render: h => h(App)
}).$mount('#app')
