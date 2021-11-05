<template>
  <div id="snb">
    <MapControl/>
    <MapLayer v-on:setMapVisible="mapVisible"/>

  </div>
</template>

<script>

import MapControl from "@/components/openlayers/control/MapControl";
import MapLayer from "@/components/openlayers/control/MapLayer";

export default {
  name: "MapHeader",
  components: {
    MapControl,
    MapLayer
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