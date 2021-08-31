<template>
  <div>
    <van-popup :show="show" @update:show="show = $event">
      <div class="account">
        <div style="width: 100%;">
          <div>
            <div class="account-title">社区疫情</div>
            <div class="login-title">{{status}}</div>
            <van-form @submit="onSubmit" v-if="status === STATUS.register">
              <van-field
                v-model="registerUsername"
                name="用户名"
                label="用户名"
                placeholder="用户名"
                :rules="[{ required: true, message: '请填写用户名' }]"
              />
              <van-field
                v-model="registerPassword"
                type="password"
                name="密码"
                label="密码"
                placeholder="密码"
                :rules="[{ required: true, message: '请填写密码' }]"
                autocomplete="off"
              />
              <van-field
                v-model="registerPhone"
                name="手机号码"
                label="手机号码"
                placeholder="手机号码"
                type="tel"
                :rules="[{ required: true, message: '请填写手机号码' }]"
              />
              <van-field
                v-model="registerIdCard"
                name="身份证号码"
                label="身份证号码"
                placeholder="身份证号码"
                type="tel"
                :rules="[{ required: true, message: '请填写身份证号码' }]"
              />
              <div style="margin: 16px;">
                <van-button round block type="primary" native-type="submit">注册</van-button>
              </div>
            </van-form>
            <van-form @submit="onLogin" v-if="status === STATUS.login">
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
              <div class="btn-group" style>
                <van-button round block type="primary" native-type="submit">登录</van-button>
                <van-button @click="cancel" round block type="primary">取消</van-button>
              </div>
            </van-form>
            <div class="register" style="padding: 0 16px;">
              <div @click="registerOrLogin($event,status)">{{register}}</div>
            </div>
          </div>
        </div>
      </div>
    </van-popup>

    <div class="userInfo-main">
      <div class="userInfo">
        <div class="user-profile">
          <div class="avatar"></div>
          <div
            class="user-name"
          >{{state.userInfo && state.userInfo.username ? state.userInfo.username : "用户名"}}</div>
          <div
            class="user-phone"
          >{{state.userInfo && state.userInfo.telephone ? state.userInfo.telephone : "联系方式"}}</div>
        </div>
      </div>
      <div v-if="state.userInfo">
        <div class="request-logined">
          <div>个人信息</div>
          <div>
            <van-icon name="arrow" />
          </div>
        </div>
        <div class="request-btned">
          <van-button block @click="quite" type="primary">退出</van-button>
        </div>
      </div>
      <div class="request-login" v-else>
        <div>
          <div class="icon-user"></div>
          <div>请先登录账号</div>
        </div>
        <div class="request-btn">
          <van-button block @click="showPupop" type="primary" native-type="submit">登录</van-button>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import { Form, Field, Button, Dialog, Popup, Icon, Toast } from "vant";
import { post } from "../utils/fetch.js";
const STATUS = {
  login: "登录",
  register: "注册",
};
export default {
  data() {
    return {
      username: "",
      registerUsername: "",
      password: "",
      registerPassword: "",
      registerPhone: "",
      registerIdCard: "",
      status: STATUS.login,
      STATUS: STATUS,
      isLogin: false,
      state: this.$datas.state,
      show: false,
    };
  },
  computed: {
    register() {
      return this.status === this.STATUS.login
        ? this.STATUS.register
        : this.STATUS.login;
    },
  },
  async beforeMount() {
    try {
      // 请求用户数据
      let userInfo = localStorage.getItem("userInfo");
      if (userInfo) {
        userInfo = JSON.parse(userInfo);
        let res = await post({
          path: `findUserById?id=${userInfo.id}`,
          data: {},
        });
        if (res.code === "404") {
          Dialog.alert({
            title: "提示",
            message: "请重新登录",
            theme: "round-button",
          }).then(() => {
            localStorage.removeItem("userInfo");
            // this.$router.push("/account");
          });
        } else if (res.code === "200") {
          this.isLogin = true;
          this.$datas.setUserInfoAction(res.data);
          // console.log(this.$datas);
        }
      } else {
        this.isLogin = false;
      }
    } catch (error) {
      console.log(error);
    }
  },
  methods: {
    clearAllCookie() {
      // eslint-disable-next-line
      let keys = document.cookie.match(/[^ =;]+(?=\=)/g);
      if (keys) {
        for (var i = keys.length; i--; )
          document.cookie = keys[i] + "=0;expires=" + new Date(0).toUTCString();
      }
    },
    async quite() {
      const beforeClose = (action) =>
        new Promise((resolve) => {
          if (action === "confirm") {
            this.clearAllCookie();
            this.$datas.clearUserInfoAction();
            localStorage.removeItem("userInfo");
            this.$router.push("/");
            resolve(true);
          } else {
            // 拦截取消操作
            // resolve(false);
            resolve(true);
          }
        });

      try {
        await Dialog.confirm({
          title: "提示",
          message: "确定要退出？",
          beforeClose,
        });
      } catch (error) {
        console.log(error);
      }
    },
    cancel() {
      this.show = false;
    },
    showPupop() {
      this.show = true;
    },
    async onLogin() {
      let toast = Toast.loading({
        duration: 0,
        message: "登录中...",
        forbidClick: true,
      });
      try {
        let res = await post({
          path: "login",
          data: {
            username: this.username,
            password: this.password,
          },
        });
        // console.log(res);
        // console.log(this.$datas);
        toast.clear();
        if (res.code === "404") {
          Dialog.alert({
            title: "提示",
            message: res.msg,
            theme: "round-button",
          }).then(() => {
            // this.$router.push("/account");
          });
        } else if (res.code === "200") {
          localStorage.setItem("userInfo", JSON.stringify(res.data));
          this.$datas.setUserInfoAction(res.data);
          this.isLogin = true;
          this.show = false;
        }
      } catch (error) {
        toast.clear();
        console.log(error);
      }
    },
    registerOrLogin(e, status) {
      // console.log(e, status);
      if (status === STATUS.register) {
        // 切换到注册
        this.status = this.STATUS.login;
      } else if (status === STATUS.login) {
        this.status = this.STATUS.register;
      }
    },
    async onSubmit(values) {
      console.log("submit", values);
      let toast = Toast.loading({
        duration: 0,
        message: "注册中...",
        forbidClick: true,
      });
      try {
        let res = await post({
          path: "register",
          data: {
            username: this.registerUsername,
            password: this.registerPassword,
            telephone: this.registerPhone,
            idcard: this.registerIdCard,
          },
        });
        localStorage.setItem("userInfo", JSON.stringify(res.data));
        this.$datas.setUserInfoAction(res.data);
        this.isLogin = true;
        this.show = false;
        toast.clear();
        // console.log(res);
      } catch (error) {
        toast.clear();
        console.log(error);
      }
    },
  },
  components: {
    [Form.name]: Form,
    [Field.name]: Field,
    [Button.name]: Button,
    [Popup.name]: Popup,
    [Icon.name]: Icon,
  },
};
</script>
<style scope>
.request-btned {
  padding: 0 20px;
  width: 100%;
  margin-top: 20px;
}
.request-logined {
  height: 40px;
  background: #fff;
  margin-top: 10px;
  box-shadow: 0 0 20px 2px #d6d5d5;

  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: row;
  justify-content: space-between;
  padding: 0 20px;
}
.request-btn {
  padding: 0 20px;
  width: 100%;
}
.icon-user {
  background: url("../assets/账号管理.png");
  background-size: cover;
  background-position-x: 0px;
  background-position-y: -16px;
  background-repeat: no-repeat;
  width: 100px;
  height: 75px;

  margin-bottom: 10px;
}
.request-login {
  height: 210px;
  background: #fff;
  margin-top: 20px;
  box-shadow: 0 0 20px 2px #d6d5d5;

  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
  justify-content: space-between;
  padding: 20px 0;
}
.userInfo-main {
  background: #e8e8e8;
  min-height: 100vh;
}
.userInfo {
  /* display: flex; */
  padding: 20px 20px;
  background: #fff;
  box-shadow: 0 0 20px 2px #d6d5d5;
}
.user-profile {
  display: flex;
  align-items: center;
}
.avatar {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  background: #666;
  background: url(../assets/头像.png);
  background-size: cover;
}
.user-name {
  padding-left: 10px;
  font-size: 20px;
  overflow: hidden;
  text-overflow: ellipsis;
  flex: 1;
  text-align: center;
}
.user-phone {
  padding-left: 10px;
  font-size: 20px;
  overflow: hidden;
  text-overflow: ellipsis;
  flex: 1;
}

.account {
  margin-bottom: 50px;

  width: 100vw;
}
.account .btn-group {
  margin: 16px;
  display: flex;
}

.account .btn-group > button:nth-child(1) {
  margin-right: 5px;
}
.account .btn-group > button:nth-child(2) {
  margin-left: 5px;
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