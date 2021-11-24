<template>
  <div class="row">
    <div id="aside">
      <Search v-on:weather_date="weather_date"/>
    </div>
    <div id="mapWrap">

      <MapHeader v-on:setMapVisible="baseMapVisible" :weatherDate="this.weatherDate"/>

      <OSM :aerial="this.aerial" :topo="this.topo" :osm="this.osm"/>

    </div>
  </div>
</template>

<script>
import OSM from './components/openlayers/basemap/OSM.vue'
import MapHeader from './components/openlayers/MapHeader.vue'
import Search from "@/components/search/Search";


export default {
  name: 'App',
  data(){
    return {
      aerial: true,
      topo: false,
      osm: false,
      weatherDate: ""
    }
  },
  components: {
    MapHeader,
    OSM,
    Search
  },
  methods: {
    weather_date(date){
      this.weatherDate=date
    },
    baseMapVisible(aerial, topo, osm){
      this.aerial = aerial;
      this.topo = topo;
      this.osm = osm;
    },
  }
}
</script>

<style lang="scss">
@import 'node_modules/bootstrap/scss/bootstrap.scss';
@import 'assets/css/common2.css';
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}
</style>
