<template>
  <div class="sign-info">
    <div class="basic">
      <div class="basic-title">健康打卡信息</div>
      <div class="main">
        <div class="basic-out">
          <div class="basic-sign" :key="item.index" v-for="item in dates">
            <div>{{item.date}}</div>
            <div>{{item.isSign ? "已签到" : "未打卡"}}</div>
          </div>
        </div>
        <div class="zhu">注：仅显示近14天的打卡记录</div>
        <div class="basic-submit">
          <div class="slogen">坚持每天健康打卡，助力疫情防控工作</div>
          <van-button @click="sign" type="primary" block>去打卡</van-button>
        </div>
      </div>
    </div>
    <!-- <div class="basic">
      <div class="basic-title">近期情况</div>
      <div style="padding: 10px 20px;">
        <div class="basic-recent">
          <div>近14天内您是否接触新冠肺炎确诊或疑似患者？</div>
          <div>
            <van-radio-group v-model="checked">
              <van-radio name="是">是</van-radio>
              <van-radio name="否">否</van-radio>
              <van-radio name="不确定">不确定</van-radio>
            </van-radio-group>
          </div>
        </div>
        <div class="basic-status">
          <div>当前状况（可多选）</div>
          <div>
            <van-checkbox-group v-model="manychecked">
              <van-checkbox shape="square" name="没有出现症状">没有出现症状</van-checkbox>
              <van-checkbox shape="square" name="感冒症状：乏力、咳嗽、发烧、肌肉痛、头痛">感冒症状：乏力、咳嗽、发烧、肌肉痛、头痛</van-checkbox>
              <van-checkbox shape="square" name="喘憋、呼吸急促">喘憋、呼吸急促</van-checkbox>
              <van-checkbox shape="square" name="恶心呕吐、腹泻">恶心呕吐、腹泻</van-checkbox>
              <van-checkbox shape="square" name="心慌、胸闷">心慌、胸闷</van-checkbox>
              <van-checkbox shape="square" name="结膜炎（红眼病样表现：眼睛涩、红、分泌物）">结膜炎（红眼病样表现：眼睛涩、红、分泌物）</van-checkbox>
              <van-checkbox shape="square" name="其他症状">其他症状</van-checkbox>
            </van-checkbox-group>
          </div>
        </div>
        <div class="basic-temperature">
          <div>当前体温(°C)</div>
          <div style="display: flex;align-items: center;">
            <van-field
              type="number"
              :border="false"
              ref="basicInput"
              style="flex: 1;"
              class="basic-input"
              v-model="temperature"
              placeholder="清输入当前体温"
            />
            <div @click="basicFocus" style="color: #1989fa;">填写</div>
          </div>
        </div>
        <div class="basic-confirm">
          <van-checkbox v-model="ischecked" shape="square">上述信息是我本人填写，本人对信息内容的真实性和完整性负责。</van-checkbox>
        </div>
        <div class="basic-submit">
          <van-button :disabled="!ischecked" type="primary" block>提交</van-button>
        </div>
      </div>
    </div>-->
  </div>
</template>
<script>
import {
  Field,
  RadioGroup,
  Radio,
  Checkbox,
  CheckboxGroup,
  Button,
  Dialog,
} from "vant";
import { get } from "../utils/fetch.js";
export default {
  data() {
    return {
      location: "河北省石家市桥西区",
      checked: "",
      manychecked: [],
      temperature: "",
      ischecked: false,
      dates: [],
    };
  },
  async beforeMount() {
    let date = new Date();
    let oneday = 24 * 60 * 60 * 1000;
    let count = 14;
    for (let i = 0; i < count; i++) {
      let time = new Date(date.valueOf() - oneday * i);
      let d = `${time.getFullYear()}-${
        time.getMonth() + 1 < 10
          ? `0${time.getMonth() + 1}`
          : time.getMonth() + 1
      }-${time.getDate() < 10 ? `0${time.getDate()}` : time.getDate()}`;
      this.dates.push({ date: d, index: i });
    }
    // console.log(this.dates, "this.dates");
    let userInfo = localStorage.getItem("userInfo");
    if (userInfo) {
      userInfo = JSON.parse(userInfo);
      let res = await get({
        path: `findUserSign?id=${userInfo.id}`,
      });
      if (res.code === "404") {
        Dialog.alert({
          title: "提示",
          message: "请重新登录",
          theme: "round-button",
        }).then(() => {
          localStorage.removeItem("userInfo");
          this.$router.push("/account");
        });
      } else if (res.code === "200") {
        this.$datas.setUserInfoAction(res.data);
        let dates = res.data.signs;
        // let signDate = [];
        // for (let i = 0; i < this.dates.length; i++) {
        //   signDate.push({
        //     index: i,
        //     date: new Date(this.dates[i].date).format("yyyy-MM-dd"),
        //   });
        // }
        let signDate = {};
        for (let i = 0; i < this.dates.length; i++) {
          signDate[new Date(this.dates[i].date).format("yyyy-MM-dd")] = i;
        }

        // console.log(signDate);
        if (dates) {
          for (let i = 0; i < dates.length; i++) {
            // let res = signDate.filter((ele) => {
            //   if (ele.date === new Date(dates[i].time).format("yyyy-MM-dd")) {
            //     return true;
            //   }
            // });
            // this.dates[res[0].index].isSign = true;

            let index = signDate[new Date(dates[i].time).format("yyyy-MM-dd")];
            this.dates[index].isSign = true;
          }
        }
        // console.log(this.dates);
        // console.log(this.$datas);
      }
    }
  },
  watch: {
    location(oldVal, newVal) {
      console.log(oldVal, newVal);
    },
  },
  methods: {
    basicFocus() {
      this.$refs.basicInput.focus();
    },
    sign() {
      this.$router.push("/sign");
    },
  },
  components: {
    [Field.name]: Field,
    [RadioGroup.name]: RadioGroup,
    [Radio.name]: Radio,
    [Checkbox.name]: Checkbox,
    [CheckboxGroup.name]: CheckboxGroup,
    [Button.name]: Button,
  },
};
</script>
<style scope>
.basic {
}
.sign-info .main {
  padding: 0 20px;
}
.sign-info .basic-out {
  border-bottom: 1px solid rgb(190, 190, 190);
  padding: 10px 0;
}
.sign-info .slogen {
  text-align: center;
  color: #999;
  padding-bottom: 15px;
  font-size: 10px;
}
.sign-info .zhu {
  color: #999;
  padding-top: 15px;
  font-size: 10px;
}
.sign-info .basic-submit {
  padding: 25px 0;
  font-size: 16px;
}
.sign-info .basic-title {
  background: rgb(190, 190, 190);
  height: 50px;
  line-height: 50px;
  padding-left: 20px;
  font-size: 16px;
}
.sign-info .basic-sign {
  font-size: 16px;
  color: #999;
  /* border-bottom: 1px solid rgb(190, 190, 190); */
  padding-bottom: 5px;
  display: flex;
  justify-content: space-between;
}
.sign-info .basic-sign > div {
  line-height: 25px;
}
</style>