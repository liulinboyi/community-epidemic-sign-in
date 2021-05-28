<template>
  <div id="aa" style="display: flex;">
    <div class="out" ref="out">
      <div class="validateInner"></div>
      <div class="validateInner"></div>
      <div class="validateInner"></div>
      <div class="validateInner"></div>
      <div class="validateInner"></div>
      <div class="validateInner"></div>
      <div class="validateInner"></div>
      <div class="validateInner"></div>
      <div class="validateInner"></div>
      <div class="validateInner"></div>
      <div class="validateInner"></div>
      <div class="validateInner"></div>
      <div class="validateInner"></div>
      <div class="validateInner"></div>
      <div class="validateInner"></div>
      <div class="validateInner"></div>
      <div class="validateInner"></div>
      <div class="validateInner"></div>
      <div class="validateInner"></div>
      <div class="validateInner"></div>
      <div class="validateInner"></div>
      <div class="validateInner"></div>
      <div class="validateInner"></div>
      <div class="validateInner"></div>
      <div class="validateInner"></div>
      <div class="validateInner"></div>
      <div class="validateInner"></div>
      <div class="validateInner"></div>
      <div class="validateInner"></div>
      <div class="validateInner"></div>
      <div class="validateInner"></div>
      <div class="validateInner"></div>
      <div class="validateInner"></div>
      <div class="validateInner"></div>
      <div class="validateInner"></div>
      <div class="validateInner"></div>
    </div>
  </div>
</template>
<script>
export default {
  name: "ValidateScroll",
  created() {},
  mounted() {
    let out = this.$refs.out;
    //
    console.log(process.env);
    console.log(process.env.VUE_APP_ViewportWidth);
    let basic = process.env.VUE_APP_ViewportWidth;
    let vwBasic = 100 / Number(basic); // 1px 代表多少vw
    let start = -vwBasic * 30 * 10; // 长度为validateInner的宽度的倍数即可使动画衔接平稳
    function scroll() {
      requestAnimationFrame(() => {
        // console.log(start)
        if (start >= 0) {
          start = -vwBasic * 30 * 10;
        }
        start += 0.2;
        out.style.transform = `translate3d(${start}vw,0,0)`;
        scroll();
      });
    }
    scroll();
  },
};
</script>
<style scoped>
#aa {
  /* position: absolute; */
  overflow: hidden;
  width: 100%;
  background: #fff;
}

#bb {
  /* position: absolute; */
  overflow: hidden;
  width: 100%;
}

.out {
  display: flex;
  /* animation: scroll 5s linear infinite; */
  /* transform: translate3d(-200px, 0, 0); */
}

.validateInner {
  width: 30px;
  height: 30px;
  transform: skew(-40deg);
  /* background: #666; */
}

.validateInner:nth-child(2n + 1) {
  background: green;
}

.validateInner:nth-child(2n) {
  background: #fff;
}

.two {
  margin-top: 20px;
}
</style>