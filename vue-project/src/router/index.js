import Vue from 'vue'
import VueRouter from 'vue-router'
import RedeemPrizesView from '@/components/RedeemPrizesView.vue'
import CartView from '@/components/CartView.vue'
import StartGameView from '@/components/StartGameView.vue'
import UserInfoView from '@/components/UserInfoView.vue'
import LoginView from '@/views/LoginView.vue'
import RegisterView from '@/views/RegisterView.vue'
import EditPrizesView from '@/components/EditPrizesView.vue'
import AppLayout from '@/components/AppLayout.vue'

Vue.use(VueRouter)

const routes = [
  {
    path: '/login',
    name: 'LoginView',
    component: LoginView
  },
  {
    path: '/regis',
    name: 'RegisterView',
    component: RegisterView
  },
  {
    path: '/',
    component: AppLayout,
    children: [
      {
        path: 'prizes',
        name: 'RedeemPrizesView',
        component: RedeemPrizesView
      },
      {
        path: 'prizes-edit',
        name: 'EditPrizesView',
        component: EditPrizesView
      },
      {
        path: 'cart',
        name: 'CartView',
        component: CartView
      },
      {
        path: 'start',
        name: 'StartGameView',
        component: StartGameView
      },
      {
        path: 'user',
        name: 'UserInfoView',
        component: UserInfoView
      }
    ]
  }
];

const router = new VueRouter({
  routes
});

router.beforeEach((to, from, next) => { 
  if (to.name === 'EditPrizesView') { 
    Vue.prototype.$axios.get('/user')
    .then(response => { 
      if (response.data.success) { 
        const role = response.data.resultData.role;
        if (role !== 1) { 
          next({ name: 'RedeemPrizesView' });
        } else { 
          next();
        } 
      } 
    })
    .catch(error => { 
      console.error('獲取用戶角色失敗:', error); 
      next({ name: 'RedeemPrizesView' }); 
    }); 
  } else { 
    next(); 
  } 
});

export default router
