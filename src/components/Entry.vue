<template>
  <div>
    <Button class="btn">hello vant</Button>
  </div>
</template>

<script>
import { Button } from "vant";
export default {
  name: "Entry",
  components: {
    Button,
  },
  props: {
    msg: String,
  },
  setup() {},
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.btn {
  width: 100px;
}
</style>
