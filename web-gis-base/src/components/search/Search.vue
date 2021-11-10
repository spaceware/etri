<template>

  <div id="nav">
    <div class="cont_tmenu">
      <h2 >Search</h2>
    </div>
    <SearchBox  />
    <SearchList v-on:weather_date="weather_date"/>
  </div>
</template>

<script>
import SearchBox from "./SearchBox"
import SearchList from "./SearchList"
export default {
  name: "Search.Vue",
  components: {SearchBox, SearchList},
  methods: {
    weather_date(date){
      this.$emit("weather_date",date)
    }
  }
}
</script>

<style scoped>

</style>