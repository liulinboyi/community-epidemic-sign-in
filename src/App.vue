<template>
  <Entry />
</template>

<script>
import Entry from "./components/Entry.vue";

export default {
  name: "App",
  components: {
    Entry,
  },
};
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>
