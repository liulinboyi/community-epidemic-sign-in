<template>
  <div ref="scrollView">
    <div class="basic">
      <div class="basic-title">基本信息</div>
      <div style="padding: 10px 20px;">
        <div class="basic-name">
          <div>姓名</div>
          <div>{{nickName}}</div>
        </div>
        <div class="basic-id">
          <div>证件号码</div>
          <div>{{idcard}}</div>
        </div>
        <div class="basic-phone">
          <div>联系电话</div>
          <div>{{phone}}</div>
        </div>
        <div class="basic-addresss">
          <div>当前所在地址</div>
          <div style="display: flex;align-items: center;">
            <van-field
              :border="false"
              ref="basicInput"
              style="flex: 1;"
              class="basic-input"
              v-model="location"
            />
            <div @click="basicFocus" style="color: #1989fa;">填写</div>
          </div>
        </div>
      </div>
    </div>
    <div class="basic">
      <div class="basic-title">近期情况</div>
      <div style="padding: 10px 20px;">
        <div class="basic-recent">
          <div>近14天内您是否接触新冠肺炎确诊或疑似患者？</div>
          <div>
            <van-radio-group v-model="checked">
              <van-radio name="1">是</van-radio>
              <van-radio name="0">否</van-radio>
              <van-radio name="2">不确定</van-radio>
            </van-radio-group>
          </div>
        </div>
        <div class="basic-status">
          <div>当前状况（可多选）</div>
          <div>
            <van-checkbox-group v-model="manychecked">
              <van-checkbox shape="square" name="0">没有出现症状</van-checkbox>
              <van-checkbox shape="square" name="1">感冒症状：乏力、咳嗽、发烧、肌肉痛、头痛</van-checkbox>
              <van-checkbox shape="square" name="2">喘憋、呼吸急促</van-checkbox>
              <van-checkbox shape="square" name="3">恶心呕吐、腹泻</van-checkbox>
              <van-checkbox shape="square" name="4">心慌、胸闷</van-checkbox>
              <van-checkbox shape="square" name="5">结膜炎（红眼病样表现：眼睛涩、红、分泌物）</van-checkbox>
              <van-checkbox shape="square" name="6">其他症状</van-checkbox>
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
          <van-button @click="uploadSign" :disabled="!ischecked" type="primary" block>提交</van-button>
        </div>
      </div>
    </div>
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
import { post } from "../utils/fetch.js";
export default {
  name: "Sign",
  data() {
    return {
      location: "",
      checked: "",
      manychecked: [],
      temperature: "",
      ischecked: false,
      curRouter: this.$route,
      state: this.$datas.state,
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
    phone() {
      return this.state.userInfo && this.state.userInfo.telephone;
    },
  },
  deactivated() {
    console.log("deactivated");
    // this.curRouter.top = this.$refs.scrollView.getClientRects()[0].top;
    // console.log(this.curRouter.top);
    // console.log(this.curRouter);
    // console.log(this.$refs.scrollView);
    this.$datas.setTop(-this.$refs.scrollView.getBoundingClientRect().top);
    console.log(this.$datas);
  },
  activated() {
    console.log("activated");
    window.scrollTo(0, this.$datas.state.top);
  },
  watch: {
    location(oldVal, newVal) {
      console.log(oldVal, newVal);
    },
  },
  methods: {
    async uploadSign() {
      console.log("上传签到");
      if (
        this.location &&
        this.checked &&
        this.manychecked.length > 0 &&
        this.temperature
      ) {
        let userInfo = localStorage.getItem("userInfo");
        if (userInfo) {
          userInfo = JSON.parse(userInfo);
          let upload = {
            address: this.location,
            contactTag: this.checked,
            statusTag: this.manychecked.join(","),
            temperature: this.temperature,
            userId: userInfo.id,
            time: new Date().toISOString(),
          };
          console.log(upload);
          try {
            let res = await post({
              path: "sign",
              data: upload,
            });
            console.log(res);
            Dialog.alert({
              title: "提示",
              message: res.msg,
              theme: "round-button",
            }).then(() => {
              this.location = "";
              this.checked = "";
              this.manychecked = [];
              this.temperature = "";
              this.ischecked = false;
              this.$router.replace("/sign-info");
            });
          } catch (error) {
            console.log(error);
          }
        }
      } else {
        Dialog({
          title: "提示",
          message: "请填写完整！",
          theme: "round-button",
        });
      }
    },
    basicFocus() {
      this.$refs.basicInput.focus();
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
.basic-submit {
  padding: 25px 0;
  font-size: 16px;
}
.basic-confirm {
  padding: 20px 0;
  border-bottom: 1px solid rgb(190, 190, 190);
  font-size: 16px;
}
.basic-temperature {
  font-size: 16px;
  /* color: #999; */
  border-bottom: 1px solid rgb(190, 190, 190);
  padding: 10px 0;
}
.basic-temperature > div {
  line-height: 25px;
}
.basic-status .van-checkbox-group {
  padding: 10px 0;
}
.basic-status .van-checkbox {
  padding: 15px 0;
  border-bottom: 1px solid rgb(190, 190, 190);
}
.basic-status {
  font-size: 16px;
}
.basic-recent {
  font-size: 16px;
}
.basic-recent .van-radio-group {
  padding: 10px 0;
}
/* .basic-recent .van-radio:not(:last-of-type) {
  padding: 7px 0;
} */
.basic-recent .van-radio {
  padding: 15px 0;
  border-bottom: 1px solid rgb(190, 190, 190);
}
.basic-title {
  background: rgb(190, 190, 190);
  height: 50px;
  line-height: 50px;
  padding-left: 20px;
  font-size: 16px;
}
.basic-name {
  font-size: 16px;
  color: #999;
  border-bottom: 1px solid rgb(190, 190, 190);
  padding-bottom: 10px;
}
.basic-name > div {
  line-height: 25px;
}
.basic-id {
  font-size: 16px;
  color: #999;
  border-bottom: 1px solid rgb(190, 190, 190);
  padding: 10px 0;
}
.basic-id > div {
  line-height: 25px;
}
.basic-phone {
  font-size: 16px;
  color: #999;
  border-bottom: 1px solid rgb(190, 190, 190);
  padding: 10px 0;
}
.basic-phone > div {
  line-height: 25px;
}
.basic-addresss {
  font-size: 16px;
  /* color: #999; */
  border-bottom: 1px solid rgb(190, 190, 190);
  padding: 10px 0;
}
.basic-addresss > div {
  line-height: 25px;
}
.basic-input {
  padding: 0;
}
</style>