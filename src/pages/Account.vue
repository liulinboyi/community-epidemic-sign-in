<template>
  <div class="account">
    <div style="width: 100%;">
      <div class="account-title">社区疫情</div>
      <div class="login-title">{{status}}</div>
      <van-form @submit="onSubmit" v-if="status === STATUS.register">
        <van-field
          v-model="username"
          name="用户名"
          label="用户名"
          placeholder="用户名"
          :rules="[{ required: true, message: '请填写用户名' }]"
        />
        <van-field
          v-model="phone"
          name="手机号码"
          label="手机号码"
          placeholder="手机号码"
          type="tel"
          :rules="[{ required: true, message: '请填写手机号码' }]"
        />
        <van-field
          v-model="password"
          type="password"
          name="密码"
          label="密码"
          placeholder="密码"
          :rules="[{ required: true, message: '请填写密码' }]"
          autocomplete="off"
        />
        <div style="margin: 16px;">
          <van-button round block type="primary" native-type="submit">注册</van-button>
        </div>
      </van-form>
      <van-form @submit="onSubmit" v-if="status === STATUS.login">
        <van-field
          v-model="username"
          name="用户名"
          label="用户名"
          placeholder="用户名"
          :rules="[{ required: true, message: '请填写用户名' }]"
        />
        <van-field
          v-model="password"
          type="password"
          name="密码"
          label="密码"
          placeholder="密码"
          :rules="[{ required: true, message: '请填写密码' }]"
          autocomplete="off"
        />
        <div style="margin: 16px;">
          <van-button round block type="primary" native-type="submit">登录</van-button>
        </div>
      </van-form>
      <div class="register" style="padding: 0 16px;">
        <div @click="registerOrLogin($event,status)">{{register}}</div>
      </div>
    </div>
  </div>
</template>
<script>
import { Form, Field, Button } from "vant";
const STATUS = {
  login: "登录",
  register: "注册",
};
export default {
  data() {
    return {
      username: "",
      password: "",
      phone: "",
      status: STATUS.login,
      STATUS: STATUS,
    };
  },
  computed: {
    register() {
      return this.status === this.STATUS.login
        ? this.STATUS.register
        : this.STATUS.login;
    },
  },
  methods: {
    registerOrLogin(e, status) {
      // console.log(e, status);
      if (status === STATUS.register) {
        // 切换到注册
        this.status = this.STATUS.login;
      } else if (status === STATUS.login) {
        this.status = this.STATUS.register;
      }
    },
    onSubmit(values) {
      console.log("submit", values);
    },
  },
  created() {
    console.log(this.$datas);
  },
  components: {
    [Form.name]: Form,
    [Field.name]: Field,
    [Button.name]: Button,
  },
};
</script>
<style scope>
.account {
  margin-bottom: 50px;
}
.account {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: calc(100vh - 50px);
  background: linear-gradient(rgb(206 237 245), #fff);
}
.account .van-cell {
  background-color: rgba(255, 255, 255, 0.5);
}
.account .login-title {
  font-size: 20px;
  text-align: center;
  padding-bottom: 20px;
}
.account .account-title {
  text-align: center;
  font-size: 25px;
  padding-bottom: 20px;
}
.account .register {
  display: flex;
  justify-content: flex-end;
  font-size: 14px;
  color: #1989fa;
}
</style>