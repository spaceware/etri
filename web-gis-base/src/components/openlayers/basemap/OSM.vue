<template>
  <div class="map" ref="map-root" >

  </div>
</template>

<script>
import 'ol/ol.css'
import { useStore } from 'vuex'
// import TileLayer from "ol/layer/Tile";
// import XYZ from "ol/source/XYZ";

let store = null;
let state = null;



export default {

  name: 'MapContainer',
  props: ['aerial', "topo", "osm"],
  mounted(){
    store = useStore()
    let value = {ref: this.$refs["map-root"],aerial: this.aerial, topo: this.topo,osm: this.osm}
    store.commit("setMapTarget", value)
    state = store.state;
    state.map.addInteraction(state.measureLine)
    state.map.addInteraction(state.measurePoly)
    state.map.addOverlay(state.tooltip)

    state.measureLine.on("drawstart", state.tooltip.setFeature.bind(state.tooltip))
    state.measureLine.on(["change:active", "drawend"],
        state.tooltip.removeFeature.bind(state.tooltip));
    state.measurePoly.on("drawstart", store.state.tooltip.setFeature.bind(state.tooltip))
    state.measurePoly.on(["change:active", "drawend"],
        state.tooltip.removeFeature.bind(state.tooltip));

    console.log(store.state.map)


  },
  watch: {
    osm(newVisible){
      if (newVisible){
        state.map.getLayers().forEach(function(layer) {
          if (layer.get('title') == "OSM"){
            layer.setVisible(newVisible);
          } else {
            layer.setVisible(!newVisible);
          }
        });
      }
    },
    aerial(newVisible) {
      if (newVisible) {
        state.map.getLayers().forEach(function (layer) {
          if (layer.get('title') == "VWorld Satellite Map") {
            layer.setVisible(newVisible);
          } else {
            layer.setVisible(!newVisible);
          }
        });
      }
    },
    topo(newVisible){
      if (newVisible) {
        state.map.getLayers().forEach(function (layer) {
          if (layer.get('title') == "VWorld Base Map") {
            layer.setVisible(newVisible);
          } else {
            layer.setVisible(!newVisible);
          }
        });
      }
    },
  },
  // setup() {
  //   const center = ref([128, 37])
  //   const projection = ref('EPSG:4326')
  //   const zoom = ref(7)
  //   const rotation = ref(0)
  //
  //   return {
  //     center,
  //     projection,
  //     zoom,
  //     rotation
  //   }
  // },


}
</script>

<style scoped>

</style>