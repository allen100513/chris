<template>
  <div class="container">
    <div>
      <h2>用戶信息</h2>
      <dl>
        <dt>姓名:</dt>
        <dd>{{ user.name }}</dd>
        <dt>帳號:</dt>
        <dd>{{ user.username }}</dd>
        <dt>生日:</dt>
        <dd>{{ user.birthDate }}</dd>
        <dt>最佳紀錄(秒數):</dt>
        <dd>{{ user.recordSeconds }}</dd>
        <dt>紅利點數:</dt>
        <dd>{{ user.bonus }}</dd>
      </dl>
      <div class="button-container">
        <button @click="showEditNameModal">編輯姓名</button>
        <button @click="showEditPasswordModal">編輯密碼</button>
      </div>

      <div v-if="isEditNameModalVisible" class="modal">
        <div class="modal-content">
          <h3>編輯姓名</h3>
          <form @submit.prevent="handleEditName">
            <div>
              <label for="edit-name">姓名:</label>
              <input type="text" v-model="editUser.name" id="edit-name" required>
            </div>
            <p v-if="nameError" class="error">{{ nameError }}</p>
            <div class="button-group">
              <button type="submit">確認編輯</button>
              <button type="button" @click="hideEditNameModal">取消</button>
            </div>
          </form>
        </div>
      </div>

      <div v-if="isEditPasswordModalVisible" class="modal">
        <div class="modal-content">
          <h3>編輯密碼</h3>
          <form @submit.prevent="handleEditPassword">
            <div>
              <label for="edit-password">密碼:</label>
              <input type="password" v-model="editUser.password" id="edit-password" required>
            </div>
            <p v-if="passwordError" class="error">{{ passwordError }}</p>
            <div class="button-group">
              <button type="submit">確認編輯</button>
              <button type="button" @click="hideEditPasswordModal">取消</button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      user: {
        name: '',
        username: '',
        birthDate: '',
        recordSeconds: '',
        bonus: ''
      },
      isEditNameModalVisible: false,
      isEditPasswordModalVisible: false,
      editUser: {
        name: '',
        password: ''
      },
      passwordError: '',
      nameError: ''
    }
  },
  methods: {
    showEditNameModal() {
      this.editUser.name = this.user.name ; // 複製用戶數據到編輯表單
      this.isEditNameModalVisible = true;
    },
    hideEditNameModal() {
      this.isEditNameModalVisible = false;
      this.nameError = '';
    },
    handleEditName() {
      this.nameError = '';
      if (!this.isNameValid(this.editUser.name)) {
        this.nameError = '姓名不能超過10個字';
        return;
      }

      this.$axios.put('/user',{
        name: this.editUser.name,
      })
      .then(res => {
        if (res.data.success){
          this.$Swal.fire("修改成功!");
          console.log('修改成功:', res.data.message);
        }else {
          console.error('修改失敗:', res.data.message);
          // 顯示失敗訊息或進行其他錯誤處理
        }
      })
      .catch(error => {
          console.error('修改失敗:', error);
      });
      window.location.reload();
      // this.user.name = this.editUser.name;
      // this.hideEditNameModal();
    },
    showEditPasswordModal() {
      this.editUser.password = ''
      this.isEditPasswordModalVisible = true;
    },
    hideEditPasswordModal() {
      this.isEditPasswordModalVisible = false;
      this.passwordError = '';
    },
    handleEditPassword() {
      this.passwordError = '';
      if (!this.isPasswordValid(this.editUser.password)) {
        this.passwordError = '密碼必須包含至少一個字母和一個數字,且介於8-15個字之間';
        return;
      }

      this.$axios.put('/user',{
        password: this.editUser.password,
      })
      .then(res => {
        if (res.data.success){
          this.$Swal.fire("修改成功!");
          console.log('修改成功:', res.data.message);
        }else {
          console.error('修改失敗:', res.data.message);
          // 顯示失敗訊息或進行其他錯誤處理
        }
      })
      .catch(error => {
          console.error('修改失敗:', error);
      });
      this.hideEditPasswordModal();
    },
    isPasswordValid(password) {
      const letter = /[a-zA-Z]/;
      const number = /[0-9]/;
      return password.length <= 15 && password.length >= 8 && letter.test(password) && number.test(password);
    },
    isNameValid(name) {
      return name.length <= 10;
    }
  },
  mounted(){
    this.$axios.get('/user')
    .then(res => {
      if (res.data.success) {
        this.user.name = res.data.resultData.name;
        this.user.username = res.data.resultData.username;
        this.user.birthDate = res.data.resultData.birthDate;
        this.user.recordSeconds = res.data.resultData.recordSeconds;
        this.user.bonus = res.data.resultData.bonus;
      }
    })
    .catch(error => {
      console.error('獲取用戶姓名失敗:', error);
    });
  }
}
</script>

<style scoped>
.container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh; /* 全屏高度 */
}
h2{
  text-align: center;
}
dl {
  display: grid;
  grid-template-columns: auto auto;
  gap: 0.5rem;
  width: 350px ;
  text-align: center;
}
.button-container {
  display: flex;
  justify-content: center; /* 水平置中 */
  gap: 1rem; /* 增加按鈕之間的間距 */
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
.modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}
.modal-content {
  background-color: white;
  padding: 2rem;
  border-radius: 5px;
  width: 300px;
}
.modal-content form {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}
.modal-content form .button-group {
  display: flex;
  justify-content: space-between;
}
.modal-content form .button-group button {
  flex: 1;
  margin-right: 0.5rem; /* 增加按鈕之間的間距 */
}
.modal-content form .button-group button:last-child {
  margin-right: 0; /* 去除最後一個按鈕的右側間距 */
}
.error {
  color: red;
  text-align: center;
  margin-top: 1rem;
}
</style>

  