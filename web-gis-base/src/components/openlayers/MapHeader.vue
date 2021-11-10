<template>
  <div id="snb">
    <MapControl/>
    <WeatherLayer :weatherDate="weatherDate"/>
    <MapLayer v-on:setMapVisible="mapVisible"/>

  </div>
</template>

<script>

import MapControl from "@/components/openlayers/control/MapControl";
import MapLayer from "@/components/openlayers/control/MapLayer";
import WeatherLayer from "@/components/openlayers/control/WeatherLayer";

export default {
  name: "MapHeader",
  props: ['weatherDate'],
  components: {
    MapControl,
    MapLayer,
    WeatherLayer
  },
  methods:{
    mapVisible(aerial, topo, osm){
      this.aerial = aerial;
      this.topo = topo;
      this.osm = osm;
      this.$emit("setMapVisible", aerial, topo, osm);
    },
  }
}
</script>

<style scoped>
#snb{float: left; z-index: -1}
</style>