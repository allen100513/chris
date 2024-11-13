<template>
  <div class="container">
    <h2>編輯獎品</h2>
    <div v-for="(prize, index) in prizes" :key="index" class="prize-item">
      品名:<input type="text" v-model="prize.name" :readonly="!prize.isEditing" />
      價格:<input type="text" v-model="prize.cost" :readonly="!prize.isEditing" />
      圖片地址:<input type="text" v-model="prize.imgSrc" :readonly="!prize.isEditing" />
      <div class="actions">
        <button v-if="!prize.isEditing" @click="startEditing(index)">編輯</button>
        <button v-if="!prize.isEditing" @click="confirmDelete(index)">刪除</button>
        <button v-if="prize.isEditing" @click="saveEditing(index)">保存</button>
        <button v-if="prize.isEditing" @click="cancelEditing(index)">取消</button>
      </div>
    </div>
    <button @click="showAddForm">新增</button>

    <div v-if="isAddFormVisible" class="add-form">
      <h3>新增獎品</h3>
      <form @submit.prevent="addPrize">
        <label>品名:</label>
        <input type="text" v-model="newPrize.name" required />
        <label>價格:</label>
        <input type="number" v-model="newPrize.cost" required />
        <label>圖片地址:</label>
        <input type="text" v-model="newPrize.imgSrc" required />
        <div class="actions">
          <button type="submit">保存</button>
          <button type="button" @click="hideAddForm">取消</button>
        </div>
      </form>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      prizes: [],
      isAddFormVisible: false,
      newPrize: {
        name: "",
        cost: 0,
        imgSrc: "",
      },
      originalPrizes: {}
    };
  },
  methods: {
    startEditing(index) {
      const prize = this.prizes[index];
      this.originalPrizes[prize.id] = { ...prize };
      this.$set(this.prizes[index], "isEditing", true);
    },
    cancelEditing(index) {
      const prize = this.prizes[index];
      if (this.originalPrizes[prize.id]) {
        this.$set(this.prizes, index, { ...this.originalPrizes[prize.id], isEditing: false });
        delete this.originalPrizes[prize.id];
      }
    },
    saveEditing(index) {
      const prize = this.prizes[index];

      if (!prize.name || prize.name.length > 10) {
        this.$Swal.fire("錯誤", "品名不能為空且不能超過10個字", "error");
        return;
      }
      if (!prize.cost) {
        this.$Swal.fire("錯誤", "價格不能為空", "error");
        return;
      }
      if (!prize.imgSrc) {
        this.$Swal.fire("錯誤", "圖片地址不能為空", "error");
        return;
      }

      this.$axios.put('/prizes', {
        id: prize.id,
        name: prize.name,
        cost: prize.cost,
        imgSrc: prize.imgSrc
      })
      .then((response) => {
        if (response.data.success) {
          this.$set(this.prizes[index], "isEditing", false);
          this.$Swal.fire("更新成功!");
        } else {
          console.error("更新失敗:", response.data.message);
        }
      })
      .catch((error) => {
        console.error("更新失敗:", error);
      });
    },
    confirmDelete(index) {
      this.$Swal
        .fire({
          title: "確定要刪除這個獎品嗎？",
          showCancelButton: true,
          confirmButtonText: "確定",
          cancelButtonText: "取消",
        })
        .then((result) => {
          if (result.isConfirmed) {
            this.deletePrize(index);
          }
        });
    },
    deletePrize(index) {
        const prize = this.prizes[index];
        this.$axios.delete(`/prizes?id=${prize.id}`)
        .then((response) => {
            if (response.data.success) {
            this.prizes.splice(index, 1);
            this.$Swal.fire("刪除成功!");
            } else {
            console.error("刪除失敗:", response.data.message);
            }
        })
        .catch((error) => {
            console.error("刪除失敗:", error);
        });
    },
    showAddForm() {
      this.isAddFormVisible = true;
    },
    hideAddForm() {
      this.isAddFormVisible = false;
    },
    addPrize() {
      if (this.newPrize.name.length > 10) {
        this.$Swal.fire("錯誤", "品名不能超過10個字", "error");
        return;
      }

      this.$axios.post("/prizes", {
        name: this.newPrize.name,
        cost: this.newPrize.cost,
        imgSrc: this.newPrize.imgSrc
      })
      .then((response) => {
        if (response.data.success) {
          this.prizes.push({
            ...this.newPrize,
            id: response.data.resultData,
            isEditing: false,
          });
          this.hideAddForm();
          this.$Swal.fire("新增成功!");
          this.newPrize = {
            name: "",
            cost: 0,
            imgSrc: "",
          };
        } else {
          console.error("新增失敗:", response.data.message);
        }
      })
      .catch((error) => {
        console.error("新增失敗:", error);
      });
    },
  },
  mounted() {
    this.$axios
      .get("/prizes")
      .then((res) => {
        if (res.data.success) {
          const prizesFromBackend = res.data.resultData;
          this.prizes = prizesFromBackend.map((prize) => ({
            id: prize.id,
            name: prize.name,
            cost: prize.cost,
            imgSrc: prize.imgSrc,
            isEditing: false,
          }));
        }
      })
      .catch((error) => {
        console.error("獲取獎品失敗:", error);
      });
  },
};
</script>

<style scoped>
.container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100vh;
}

.prize-item {
  display: flex;
  margin-bottom: 1rem;
}

.prize-item input {
  margin-right: 0.5rem;
}

.actions {
  display: flex;
  gap: 0.5rem;
}

.add-form {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background-color: white;
  padding: 1rem;
  border-radius: 5px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}

.add-form form {
  display: flex;
  flex-direction: column;
}

.add-form form .actions {
  display: flex;
  gap: 0.5rem;
  margin-top: 1rem;
}
</style>


  