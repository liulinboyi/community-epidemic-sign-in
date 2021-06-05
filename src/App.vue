<template>
  <!-- 主router-view -->
  <router-view v-slot="{ Component }">
    <transition name="fade" mode="out-in">
      <keep-alive include="Sign">
        <component :is="Component" />
      </keep-alive>
    </transition>
  </router-view>
</template>

<script>
// import Home from "./pages/Home.vue";
import Sign from "./pages/Sign.vue";
export default {
  name: "App",
  components: {
    // Home: Home,
    Sign: Sign,
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
