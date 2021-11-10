<template>
  <div class="weather_layer_btn">
    <div class="btn_inner">
      <a href="#" id="btnWeather"><img :src="imgSrc" alt="Map Layer" v-on:click="toggle"></a>
      <div v-if="isActive" class="maplist">
        <ul>
          <li><label><input type="radio" name="weather" value="Temp" :checked="temp" />Temperature</label></li>
          <li><label><input type="radio" name="weather" value="Wind" :checked="wind" />Precipitation</label></li>
          <li><label><input type="radio" name="weather" value="Prcp" :checked="prcp" />Wind</label></li>
        </ul>
        <input type="range" min="-144" max="144" step="1" v-model="value">
        <p> {{weatherDate}} </p>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "MapLayer",
  components: {

  },
  props:["weatherDate"],
  data() {
    return {
      temp: false,
      prcp: false,
      wind: false,
      isActive: false,
      imgSrc: require("../../../assets/images/btnmap_off.png"),
      value: 0
    }
  },
  methods:{
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
/*.maplist {position:fixed; z-index: 20;  width: 138px; background-color: #ffffff;*/
/*  border: 1px solid rgba(101, 104, 111, 0.3); padding: 15px;}*/
.maplist ul li {font-size: 11pt; line-height: 150%; color:#333;}


</style>