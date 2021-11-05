<template>

  <div id="nav">
    <div class="cont_tmenu">
      <h2 >Search</h2>
    </div>
    <SearchBox  />
    <SearchList />

  </div>
</template>

<script>
import SearchBox from "./SearchBox"
import SearchList from "./SearchList"
export default {
  name: "Search.Vue",
  components: {SearchBox, SearchList}
}
</script>

<style scoped>

</style>