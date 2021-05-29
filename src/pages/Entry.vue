<template>
  <div>
    <div class="main">
      <div class="title">社区疫情签到</div>
      <div class="slogen">众志成城 共抗疫情</div>
      <div class="validate">
        <ValidateScroll></ValidateScroll>
      </div>
      <div class="info">
        <div style="padding: 10px">
          <div class="info-title">个人信息码</div>
          <div class="info-name">
            <div>姓名</div>
            <div class="info-name-content">小明</div>
          </div>
          <div class="info-id">
            <div>身份证号码</div>
            <div class="info-id-content">130**********1234</div>
          </div>
        </div>
      </div>

      <div class="qr">
        <div class="qr-date">{{curDate ? curDate : '日期'}}</div>
        <div class="qr-qr"></div>
        <div class="qr-sign">
          是否签到:
          <span class="qr-status">是</span>
        </div>
      </div>
      <div class="btn-group">
        <Button color="#43afe6" class="btn-top">查看本人签到信息</Button>
        <Button class="btn-bottom" @click="sign">去打卡</Button>
      </div>
    </div>
  </div>
</template>

<script>
import { Button } from "vant";
import ValidateScroll from "../components/ValidateScroll.vue";
import { emitter } from "../utils/eventHub.js";
export default {
  name: "Entry",
  data() {
    return {
      curDate: "",
      InterId: 0,
      childPage: null,
    };
  },
  methods: {
    emits() {
      console.log(emitter);
      emitter.emit("sign", { msg: "子页面已经关闭" });
    },
    sign() {
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
      // this.$router.push("/sign");
    },
    /**
     * 生成日期
     */
    generateDate() {
      let date = new Date();
      this.curDate = `${date.getFullYear()}-${
        date.getMonth() + 1
      }-${date.getDate()} ${date.getHours()}:${date.getMinutes()}:${date.getSeconds()}`;
    },
  },
  created() {
    this.generateDate();
    this.InterId = setInterval(() => {
      this.generateDate();
    }, 1000);
    console.log(emitter);
    emitter.on("sign", (e) => {
      console.log("sign", e);
    });
  },
  unmounted() {
    console.log("unmounted");
    clearInterval(this.InterId);
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
.validate {
  margin: 20px 20px 0 20px;
}
.out {
  width: calc(100vw - 20px);
  margin: 0 10px;
}
.main {
  min-height: 100vh;
  background: linear-gradient(rgb(206 237 245), #fff);
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
.info-name-content {
  color: #999;
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
  background: url(data:image/gif;base64,R0lGODdhewB7AIAAAAAAAP///ywAAAAAewB7AAAC/4yPqcvtD6OctNqLs968+w+G4kiW5omm6sq27gvH8kzX9o3n+gH0/g8MQoQMH693BCSXhqDzyXlKf0Ngw9hEZpVbZmAKjoKlVWpR+0Vj01x2ckzUrMuKtd26nefx7q5EfwF4heZ1Z8YGSObXFyFY4biQqDZ5GPfGZzgBqUkZZyjpVAgXuqjImPGJ2Um4V7k6Snnp2ob6mtlaB1UKq9pmuvlnG7vry4rY69kreysnXPwsStuaOsv8a2xBbT18Ou0sG50sHfi9uMyXC50AzHg9nl3efUwaye01+Cwulq+8HnamDgE7bfYwsLtH7ByvdMzobDhoLqG3hf5mcXr3CBvAdP/heB0CF9EBRJEetwU0Ga0jxWYlT3JDKbFdS4wUZrq8mTImQTg3QFmKuYOET3RAg4oY+rGoUZLVjO1kWJBcUhPiQCmEGrDW1BJVncYDKQ/eVg8/640zBbahTl1UiZrFijSnwJdsubrlWLGsvpAmyx6N+hQgLIVW8941CPirYZ4TC8+ltw+hTMiLxxD2erYuYosbG2NOq5Hp4If93nrOzC9rsJVa1T6+jBo0TXyjWeK7yje07L6JZ6++PVHyQGR0Ob8mTTOwvOFNb7oeSfvfY8fUZIOVC/1B7etrm6vEO/nDdlwVv5OfTD2qVsnCNYcNCfz4+w6u21OePzK9b9vwc9//h5TfZ/2BsJdp8kV3H2ywZSeaev6VgVZlxE0hXmngsSfhWPN0RaFQryCokmXlnQegbshxN11x46Hn4HyRnYdViNKZh5+JjbAVF4nu6aXijjZCaKGOzeUo306OtRbkPLu5xaNzPu53UZPg2dSkciLS9+SUVE7FoZE/ribliBvOFGNwp1WI04AKqmlmih0SWByGbhaIIFIRYpmmi2eyOSZqV+7TJZOHsajagUoxCJczRGpp3IWHftngkIJqWN2A+jkqVaWRlsjlhCCewCGQlE6qaZlteaedeyiGKhik6/0mJKYLkoqZXxlBScxzoZVkKm783ZiafXXOCGObry4noJum/9oKrHLHHukfkcyK6uuzydon7aDUGrsZr2LeaV2rb9rVEqPM0pnhtGSR+a2qsYoLbg7omrWopUnGMG+vxdbY6Az5LqYvv/V169GaBpb6rpIelrvnwR8CnKurt3rbJ6c0hqvwCAEmR2uw6e5K7K8O0Vvrw3MO+ueJsJLsp8kGJ6qhWL56yfGoW9672cyKgmzzzQNLpXNq56Lsc7+/uoNdb8Im/G9N8cAUXruxMY2z00LH2R2j5vZYqMxtXtpdgcxVm/PXyUKNNJ/Olt3n0lETCnV88Vp9UXxRXtvZyTGvPLKsqXa6n53a8v233WDibeDLBLNmNmMZz60uSxQHfWWgNScjejjjbZ+mKeSDLwV66KKPTnrppp+Oeuqqr856666/Dnvsss++VAEAOw==),
    linear-gradient(green, green);
  background-blend-mode: lighten;
  background-repeat: no-repeat;
  background-size: cover;
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
