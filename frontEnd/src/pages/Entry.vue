<template>
  <div class="entry">
    <div class="main">
      <div class="scanf" @click="scanf"></div>
      <div class="title">社区疫情签到</div>
      <div class="slogen">众志成城 共抗疫情</div>
      <div class="validate">
        <ValidateScroll></ValidateScroll>
      </div>
      <div class="info">
        <div style="padding: 10px">
          <div class="info-title">个人信息码</div>
          <div class="info-name">
            <div class="info-name-title">姓名</div>
            <div class="info-name-content">{{nickName}}</div>
          </div>
          <div class="info-id">
            <div>身份证号码</div>
            <div class="info-id-content">{{idcard}}</div>
          </div>
        </div>
      </div>

      <div class="qr">
        <div class="qr-date">{{curDate ? curDate : '日期'}}</div>
        <div class="qr-qr" :style="{background: backgroundQr}"></div>
        <div class="qr-sign">
          今日是否签到:
          <span class="qr-status">{{isSign ? "是" : "否"}}</span>
        </div>
      </div>
      <div class="btn-group">
        <Button color="#43afe6" @click="signInfo" class="btn-top">查看本人签到信息</Button>
        <Button class="btn-bottom" @click="sign">去打卡</Button>
      </div>
    </div>
  </div>
</template>

<script>
import { Button, Dialog } from "vant";
import ValidateScroll from "../components/ValidateScroll.vue";
import { emitter } from "../utils/eventHub.js";
import { post, get } from "../utils/fetch.js";
import QRCode from "../utils/scanf/qrcode.js";
export default {
  name: "Entry",
  data() {
    return {
      curDate: "",
      InterId: 0,
      childPage: null,
      state: this.$datas.state,
      isSign: false,
      backgroundQr: "",
      start: new Date().valueOf(), // 开始时间
    };
  },
  computed: {
    nickName() {
      return this.state.userInfo && this.state.userInfo.nickname;
    },
    idcard() {
      return (
        this.state.userInfo &&
        `${this.state.userInfo.idcard.substring(
          0,
          4
        )}*********${this.state.userInfo.idcard.substring(
          13,
          this.state.userInfo.idcard.length
        )}`
      );
    },
  },
  methods: {
    scanf() {
      this.$router.push("/scanf");
    },
    sleep(time) {
      return new Promise((resolve) => setTimeout(resolve, time));
    },
    emits() {
      // console.log(emitter);
      emitter.emit("sign", { msg: "子页面已经关闭" });
    },
    sign() {
      /**
      let routes = this.$router.resolve({
        name: "sign",
        // params: {},
      });
      console.log(routes);
      this.childPage = window.open(routes.href, "_blank");
      let timeId = setInterval(() => {
        console.log(this.childPage, "childPage");
        if (this.childPage.closed) {
          console.log("子页面已关闭");
          this.emits();
          clearInterval(timeId);
        }
      }, 1000);
      */
      this.$router.push("/sign");
    },
    signInfo() {
      this.$router.push("/sign-info");
    },
    /**
     * 生成日期
     */
    generateDate() {
      let date = new Date();
      let dif = 1000 * 60 * 30; // 30分钟翻
      if (date.valueOf() - this.start >= dif) {
        this.start = date.valueOf();
        this.generateQr();
      }
      let month = date.getMonth() + 1;
      month = month < 10 ? `0${month}` : month;
      let day = date.getDate();
      day = day < 10 ? `0${day}` : day;
      let hour = date.getHours();
      hour = date.getHours() < 10 ? `0${hour}` : hour;
      let minute = date.getMinutes();
      minute = minute < 10 ? `0${minute}` : minute;
      let second = date.getSeconds();
      second = second < 10 ? `0${second}` : second;
      this.curDate = `${date.getFullYear()}-${month}-${day} ${hour}:${minute}:${second}`;
    },
    generateQr() {
      if (this.$datas.state.userInfo) {
        // console.log(QRCode);
        const qrcode = new QRCode.Encoder();
        // console.log(qrcode);

        qrcode.setEncodingHint(true);
        qrcode.setErrorCorrectionLevel(QRCode.ErrorCorrectionLevel.H);

        // (await window.cookieStore.get("time")).value
        let data = {
          id: this.$datas.state.userInfo.id,
          username: this.$datas.state.userInfo.username,
          timestemp: new Date().valueOf(),
        };
        console.log(data);
        qrcode.write(JSON.stringify(data));
        // qrcode.write(new QRCode.QRByte("hello world\n"));
        // qrcode.write(new QRCode.QRKanji("こんにちは世界"));

        qrcode.make();
        this.backgroundQr = `url(${qrcode.toDataURL(
          10,
          0
        )}),linear-gradient(green, green)`;
        console.log("重新生成二维码");
        // console.log(qrcode.toDataURL(10, 0));
      }
    },
  },
  async beforeMount() {
    // await this.sleep(2000); // 不起作用
    // console.log("Entry");
    // console.log(this.$datas);
    this.generateDate();
    let raf = () => {
      this.InterId = requestAnimationFrame(() => {
        this.generateDate();
        raf();
      });
    };
    raf();
    // console.log(emitter);
    emitter.on("sign", (e) => {
      console.log("sign", e);
    });

    // await this.sleep(2000)
    // 请求用户数据
    try {
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
          this.$datas.setUserInfoAction(res.data);
          // console.log(this.$datas);
        }

        // 签到信息
        let signInfo = await get({
          path: `findUserSign?id=${userInfo.id}`,
        });
        if (signInfo.code === "404") {
          Dialog.alert({
            title: "提示",
            message: "请重新登录",
            theme: "round-button",
          }).then(() => {
            localStorage.removeItem("userInfo");
            // this.$router.push("/account");
          });
        } else if (signInfo.code === "200") {
          // console.log(signInfo.data);
          let datas = signInfo.data;
          if (datas.signs.length <= 0) return;
          let date = new Date().format("yyyy-MM-dd");
          if (date === new Date(datas.signs[0].time).format("yyyy-MM-dd")) {
            this.isSign = true;
          }
        }
      }
      if (!this.$datas.state.userInfo) {
        Dialog.alert({
          title: "提示",
          message: "请登录",
          theme: "round-button",
        }).then(() => {
          this.$router.push("/account");
        });
      }
    } catch (error) {
      console.log(error);
    }
  },
  async mounted() {
    this.generateQr();
  },
  unmounted() {
    console.log("unmounted");
    // clearInterval(this.InterId);
    cancelAnimationFrame(this.InterId);
  },
  components: {
    Button,
    ValidateScroll,
  },
  props: {
    msg: String,
  },
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.entry {
  margin-bottom: 50px;
}
.validate {
  margin: 20px 20px 0 20px;
}
.out {
  width: calc(100vw - 20px);
  margin: 0 10px;
}
.scanf {
  background: url(../assets/扫码.png);
  background-size: cover;
  width: 30px;
  height: 30px;
  position: fixed;
  bottom: 125px;
  right: 20px;
  z-index: 1;
}
.main {
  position: relative;
  /* min-height: 100vh; */
  background: linear-gradient(rgb(206 237 245), #fff);
  /* background: linear-gradient(rgba(227 249 170), #fff); */
}
.title {
  text-align: center;
  font-size: 20px;
  /* color: #666; */
  padding: 10px;
}
.slogen {
  font-size: 30px;
  text-align: center;
}
.info {
  margin: 20px;
  background: #fff;
  border-radius: 5px;
}
.info-title {
  font-size: 20px;
  color: #000;
  font-weight: bold;
  padding-bottom: 5px;
}
.info-title {
  border-bottom: 1px solid rgb(211, 211, 211);
}
.info-name {
  display: flex;
  justify-content: space-between;
  padding: 10px 0 10px 0;
  font-size: 15px;
}
.info-name-title {
  margin-right: 30px;
}
.info-name-content {
  color: #999;
  flex: 1;
  word-break: break-all;
  display: flex;
  justify-content: flex-end;
}
.info-id {
  display: flex;
  justify-content: space-between;
  /* padding: 0 0 10px 0; */
  font-size: 15px;
}

.info-id-content {
  color: #999;
}
.qr {
  text-align: center;
}
.qr-qr {
  width: 200px;
  height: 200px;
  /* border: 1px solid red; */
  margin: 10px auto;
  /* background: url(data:image/gif;base64,R0lGODdhewB7AIAAAAAAAP///ywAAAAAewB7AAAC/4yPqcvtD6OctNqLs968+w+G4kiW5omm6sq27gvH8kzX9o3n+gH0/g8MQoQMH693BCSXhqDzyXlKf0Ngw9hEZpVbZmAKjoKlVWpR+0Vj01x2ckzUrMuKtd26nefx7q5EfwF4heZ1Z8YGSObXFyFY4biQqDZ5GPfGZzgBqUkZZyjpVAgXuqjImPGJ2Um4V7k6Snnp2ob6mtlaB1UKq9pmuvlnG7vry4rY69kreysnXPwsStuaOsv8a2xBbT18Ou0sG50sHfi9uMyXC50AzHg9nl3efUwaye01+Cwulq+8HnamDgE7bfYwsLtH7ByvdMzobDhoLqG3hf5mcXr3CBvAdP/heB0CF9EBRJEetwU0Ga0jxWYlT3JDKbFdS4wUZrq8mTImQTg3QFmKuYOET3RAg4oY+rGoUZLVjO1kWJBcUhPiQCmEGrDW1BJVncYDKQ/eVg8/640zBbahTl1UiZrFijSnwJdsubrlWLGsvpAmyx6N+hQgLIVW8941CPirYZ4TC8+ltw+hTMiLxxD2erYuYosbG2NOq5Hp4If93nrOzC9rsJVa1T6+jBo0TXyjWeK7yje07L6JZ6++PVHyQGR0Ob8mTTOwvOFNb7oeSfvfY8fUZIOVC/1B7etrm6vEO/nDdlwVv5OfTD2qVsnCNYcNCfz4+w6u21OePzK9b9vwc9//h5TfZ/2BsJdp8kV3H2ywZSeaev6VgVZlxE0hXmngsSfhWPN0RaFQryCokmXlnQegbshxN11x46Hn4HyRnYdViNKZh5+JjbAVF4nu6aXijjZCaKGOzeUo306OtRbkPLu5xaNzPu53UZPg2dSkciLS9+SUVE7FoZE/ribliBvOFGNwp1WI04AKqmlmih0SWByGbhaIIFIRYpmmi2eyOSZqV+7TJZOHsajagUoxCJczRGpp3IWHftngkIJqWN2A+jkqVaWRlsjlhCCewCGQlE6qaZlteaedeyiGKhik6/0mJKYLkoqZXxlBScxzoZVkKm783ZiafXXOCGObry4noJum/9oKrHLHHukfkcyK6uuzydon7aDUGrsZr2LeaV2rb9rVEqPM0pnhtGSR+a2qsYoLbg7omrWopUnGMG+vxdbY6Az5LqYvv/V169GaBpb6rpIelrvnwR8CnKurt3rbJ6c0hqvwCAEmR2uw6e5K7K8O0Vvrw3MO+ueJsJLsp8kGJ6qhWL56yfGoW9672cyKgmzzzQNLpXNq56Lsc7+/uoNdb8Im/G9N8cAUXruxMY2z00LH2R2j5vZYqMxtXtpdgcxVm/PXyUKNNJ/Olt3n0lETCnV88Vp9UXxRXtvZyTGvPLKsqXa6n53a8v233WDibeDLBLNmNmMZz60uSxQHfWWgNScjejjjbZ+mKeSDLwV66KKPTnrppp+Oeuqqr856666/Dnvsss++VAEAOw==),
    linear-gradient(green, green); */
  background-blend-mode: lighten;
  background-repeat: no-repeat;
  background-size: cover !important;
}
.qr-status {
  color: green;
}
.qr-sign {
  margin-bottom: 10px;
  font-size: 16px;
}
.qr-date {
  font-size: 20px;
}

.btn-group {
  display: flex;
  flex-direction: column;
  padding: 0 20px 0 20px;
}

.btn-top {
  margin-bottom: 10px;
}

.btn {
  width: 100px;
}
</style>
