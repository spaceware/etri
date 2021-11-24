<template>
  <div class="map_layer_btn">
    <div class="btn_inner">
      <a href="#" id="btnMap"><img :src="imgSrc" alt="Map Layer" v-on:click="toggle"></a>
      <div v-if="isActive" class="maplist">
        <ul>
          <li><label><input type="radio" name="basemap" value="Aerial" :checked="aerial" @change="mapVisible(true,false,false)" />v-world Aerial</label></li>
          <li><label><input type="radio" name="basemap" value="Topo" :checked="topo" @change="mapVisible(false,true,false)" />v-world Topo</label></li>
          <li><label><input type="radio" name="basemap" value="OSM" :checked="osm" @change="mapVisible(false,false,true)" />Open StreetMap</label></li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "MapLayer",
  data() {
    return {
      aerial:true,
      topo: false,
      osm: false,
      isActive: false,
      imgSrc: require("../../../assets/images/btnmap_off.png")
    }
  },
  methods:{
    mapVisible(aerial, topo, osm){
      this.aerial = aerial;
      this.topo = topo;
      this.osm = osm;
      this.$emit("setMapVisible", aerial, topo, osm);
    },
    toggle(){
      this.isActive = !this.isActive;
      if (this.isActive){
        this.imgSrc = require("../../../assets/images/btnmap_on.png")
      } else {
        this.imgSrc = require("../../../assets/images/btnmap_off.png")
      }
    }
  }
}
</script>

<style scoped>

ul, li{margin:0; padding:0;}
ul {list-style:none;}
.maplist ul li {font-size: 11pt; line-height: 150%; color:#333;}


</style>