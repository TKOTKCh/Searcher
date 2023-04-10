<template>
  <div id="app">
    <router-view></router-view>
  </div>
</template>

<script>
import DevicePixelRatio from './utils/devicePixelRatio';
export default {
  name: 'App',
  components: { },
  created() {
    // new DevicePixelRatio().init();
  }
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  /*margin-top: 60px; 取消上边界*/
}
</style>
