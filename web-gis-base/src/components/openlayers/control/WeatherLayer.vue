<template>
  <div class="weather_layer_btn">
    <div class="btn_inner">
      <a href="#" id="btnWeather"><img :src="imgSrc" alt="Map Layer" v-on:click="toggle"></a>
      <div v-if="isActive" class="maplist">
        <ul>
          <li style="margin-bottom: 0.1rem"><Toggle class="custom_toggle" v-model="enable.temp" @change="tempSwitche()"></Toggle>Temperature</li>
          <li style="margin-bottom: 0.1rem"><Toggle class="custom_toggle" v-model="enable.prcp" @change="prcpSwitche()"></Toggle>Precipitation</li>
          <li style="margin-bottom: 0.1rem"><Toggle class="custom_toggle" v-model="enable.wind" @change="windSwitche()"></Toggle>Wind</li>
<!--          <li><label><input type="radio" name="weather" value="Temp" :checked="temp" />Temperature</label></li>-->
<!--          <li><label><input type="radio" name="weather" value="Wind" :checked="wind" />Precipitation</label></li>-->
<!--          <li><label><input type="radio" name="weather" value="Prcp" :checked="prcp" />Wind</label></li>-->
        </ul>
        <input type="range" min="-144" max="144" step="1" v-model="value">
        <p style="font-size: 6px" v-html="displayWeatherDate">  </p>
        <button class="btn_comm_05" v-on:click="addWeatherLayer">Done</button>
      </div>
    </div>
  </div>
</template>

<script>
import moment from "moment"
import Toggle from '@vueform/toggle'
import "@vueform/toggle/themes/default.css"
import { useStore } from "vuex";
import _ from "lodash"
import TileLayer from "ol/layer/Tile";
import XYZ from "ol/source/XYZ";

let store = null;
export default {
  name: "MapLayer",
  created() {
    this.moment=moment;
  },
  mounted() {
    store = useStore()
  },
  components: {
    Toggle
  },
  props:["weatherDate"],
  data() {
    return {
      enable : {temp: false,
      prcp: false,
      wind: false},
      isActive: false,
      imgSrc: require("../../../assets/images/btnweather_off.png"),
      value: 0,
    }
  },
  computed:{
    displayWeatherDate(){
      if (_.isEmpty(this.weatherDate)) {
        return "please, <br/> select disaster layer"
      }
      let dateString = this.weatherDate.substr(0,4)+"-"+this.weatherDate.substr(4,2)+"-"+this.weatherDate.substr(6,2)
      let changeDate = moment(Date.parse(dateString)).add((this.value*10), "minutes").add(-9,"hours").format("YYYY-MM-DD HH:mm")
      // let changeDate = moment(new Date(this.weatherDate)).add(this.value*10,"minutes").format("YYYY-MM-DD HH:mm");
      let interval= this.value*10;
      if (this.value>0){
        interval = "+"+this.value*10
      }
      let intervalDate =  "<br/> ("+interval+" min)"
      return changeDate+intervalDate
    },
    layerDate(){
      let dateString = this.weatherDate.substr(0,4)+"-"+this.weatherDate.substr(4,2)+"-"+this.weatherDate.substr(6,2)
      let changeDate = moment(Date.parse(dateString)).add((this.value*10), "minutes").add(-9,"hours").format("YYYYMMDD");
      return changeDate;
    },
    layerDateTime(){
      let dateString = this.weatherDate.substr(0,4)+"-"+this.weatherDate.substr(4,2)+"-"+this.weatherDate.substr(6,2)
      let changeDate = moment(Date.parse(dateString)).add((this.value*10), "minutes").add(-9,"hours").format("YYYYMMDDHHmm");
      return changeDate;
    }
  },
  watch:{
    weatherDate: function(){
      this.isActive = true;
      this.imgSrc = require("../../../assets/images/btnweather_on.png")
    }
  },
  methods:{
    toggle(){
      this.isActive = !this.isActive;
      if (this.isActive){
        this.imgSrc = require("../../../assets/images/btnweather_on.png")
      } else {
        this.imgSrc = require("../../../assets/images/btnweather_off.png")
      }
    },
    tempSwitche() {
      this.enable.prcp=false
      this.enable.wind=false
    },
    prcpSwitche() {
      this.enable.temp=false
      this.enable.wind=false
    },
    windSwitche() {
      this.enable.prcp=false
      this.enable.temp=false
    },
    addWeatherLayer(){
      if ((!this.enable.prcp) && (!this.enable.temp) && (!this.enable.wind)) {
        this.checkSwitche=true
      } else {
        this.checkSwitche=false
      }
      this.removeLayer()
      if (!this.checkSwitche) {

        if (this.enable.temp) {
          this.addLayer("TA")
        }
        if (this.enable.prcp){
          this.addLayer("RN60M")
        }
        if (this.enable.wind){
          this.addLayer("WIND10M")
        }
      }
    },
    addLayer(variable) {

      let url = this.layerDate+"/AWSDB/AWSM_"+this.layerDateTime+"_"+variable
      let Layer = new TileLayer({
        title: "weatherLayer",
        visible: true,
        type: "base",
        zIndex: 20,
        source: new XYZ({
          url:
              url+"/{z}/{x}/{y}.png",
          minZoom: 1,
          maxZoom: 7,
        }),
        extent: [12523442.714243278, 3130860.6785608195, 15654303.392804097, 6261721],
      });
      store.state.map.addLayer(Layer)
    },
    removeLayer() {
      store.state.map.getLayers().forEach(layer => {
        if (layer && layer.get("title") === "weatherLayer") {
          store.state.map.removeLayer(layer);
        }
      });
    },
  }
}
</script>

<style scoped>

.custom_toggle {
  --toggle-width:2.2rem;
  margin-right:0.2rem
}

ul, li{margin:0; padding:0;}
ul {list-style:none;}
/*.maplist {position:fixed; z-index: 20;  width: 138px; background-color: #ffffff;*/
/*  border: 1px solid rgba(101, 104, 111, 0.3); padding: 15px;}*/
.maplist ul li {font-size: 11pt; line-height: 150%; color:#333;}


</style>