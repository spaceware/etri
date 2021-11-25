<template>
  <div class="map" ref="map-root" >

  </div>
</template>

<script>
import 'ol/ol.css'
import { useStore } from 'vuex'

let store = null;
let state = null;



export default {

  name: 'MapContainer',
  props: ['aerial', "topo", "osm"],
  mounted(){
    store = useStore()
    let value = '';
    console.log(this.axios.defaults.baseURL)
    this.axios.get("/api/getKey").then((res)=>{
      value = {ref: this.$refs["map-root"], vworldKey: res.data}
      store.commit("setMapTarget", value)
    }).catch((err)=>{
      console.log(err)
    })
    state = store.state;

  },
  watch: {
    osm(newVisible){
      if (newVisible){
        state.map.getLayers().forEach(function(layer) {
          if (layer.get('title') == "OSM"){
            layer.setVisible(newVisible);
          } else if(layer.get('title') == "dataLayer" || layer.get('title') == "VWorld Base Map" || layer.get('title') == 'VWorld Satellite Map'){
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
          } else if(layer.get('title') == "VWorld Base Map" || layer.get('title') == 'OSM'){
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
          } else if(layer.get('title') == "VWorld Satellite Map" || layer.get('title') == 'OSM'){
            layer.setVisible(!newVisible);
          }
        });
      }
    },
  },


}
</script>

<style scoped>

</style>