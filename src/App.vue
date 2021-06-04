<template>
  <!-- 主router-view -->
  <router-view></router-view>
</template>

<script>
// import Home from "./pages/Home.vue";
export default {
  name: "App",
  components: {
    // Home: Home,
  },
};
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}
#app {
  /* margin-bottom: 50px; */
}
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.5s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>
