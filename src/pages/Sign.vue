<template>
  <div>
    <div class="basic">
      <div class="basic-title">基本信息</div>
      <div style="padding: 10px 20px;">
        <div class="basic-name">
          <div>姓名</div>
          <div>小明</div>
        </div>
        <div class="basic-id">
          <div>证件号码</div>
          <div>130**********1234</div>
        </div>
        <div class="basic-phone">
          <div>联系电话</div>
          <div>12345678900</div>
        </div>
        <div class="basic-addresss">
          <div>当前所在地址</div>
          <div style="display: flex;align-items: center;">
            <van-field :border="false" ref="basicInput" style="flex: 1;" class="basic-input" v-model="location" />
            <div @click="basicFocus" style="color: #1989fa;">填写</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import { Field } from "vant";
export default {
  data() {
    return {
      location: "河北省石家市桥西区",
    };
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
  },
  components: {
    [Field.name]: Field,
  },
};
</script>
<style scope>
.basic {
}
.basic-title {
  background: rgb(190, 190, 190);
  height: 50px;
  line-height: 50px;
  padding-left: 20px;
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