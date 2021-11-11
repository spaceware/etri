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
        <p style="font-size: 6px" v-if="checkSwitche" v-html="checkFalseMsg"></p>
      </div>
    </div>
  </div>
</template>

<script>
import moment from "moment"
import Toggle from '@vueform/toggle'
import "@vueform/toggle/themes/default.css"
import _ from "lodash"
import TileLayer from "ol/layer/Tile";
import XYZ from "ol/source/XYZ";

let tempLayerTitle = "Temp Layer";
let prcpLayerTitle = "Prcp Layer";
let windLayerTitle = "Wind Layer";
export default {
  name: "MapLayer",
  created() {
    this.moment=moment;
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
      imgSrc: require("../../../assets/images/btnmap_off.png"),
      value: 0,
      checkSwitche: false,
      checkFalseMsg: "please, <br/> select weather layer"
    }
  },
  computed:{
    displayWeatherDate(){
      if (_.isEmpty(this.weatherDate)) {
        return "please, <br/> select disaster layer"
      }
      let changeDate = moment(new Date(this.weatherDate)).add(this.value*10,"minutes").format("YYYY-MM-DD HH:mm:ss");
      let interval= this.value*10;
      if (this.value>0){
        interval = "+"+this.value*10
      }
      let intervalDate =  "<br/> ("+interval+" min)"
      return changeDate+intervalDate
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
      console.log(this.enable)
      if ((!this.enable.prop) && (!this.enable.temp) && (!this.enable.wind)) {
        this.checkSwitche=true
      } else {
        this.checkSwitche=false
      }
      if (!this.checkSwitche) {
        if (!this.enable.temp) {
          this.removeTempLayer()
        },
        if (!this.enable.prcp) {
          this.removePrcpLayer()
        },
        if (!this.enable.wind) {
          this.removeWindLayer()
        }

        if (this.enable.temp) {
          this.addTempLayer()
        }
        if (this.enable.prcp){
          this.addPrcpLayer()
        }
        if (this.enable.wind){
          this.addWindLayer()
        }
      }
    },
    addTempLayer(url) {
      let Layer = new TileLayer({
        title: tempLayerTitle,
        visible: true,
        type: "base",
        source: new XYZ({
          url:
              url+"/{z}/{y}/{x}.jpeg",
          minZoom: 6,
          maxZoom: 19,
          attributions:
              '<img src="https://map.vworld.kr/images/maps/logo_openplatform.png"/>',
        }),
        extent: [12523442.714243278, 3130860.6785608195, 15654303.392804097, 6261721],
      });
      store.state.map.addLayer(Layer)
    },
    addPrcpLayer(url) {
      let Layer = new TileLayer({
        title: prcpLayerTitle,
        visible: true,
        type: "base",
        source: new XYZ({
          url:
              url+"/{z}/{y}/{x}.jpeg",
          minZoom: 6,
          maxZoom: 19,
          attributions:
              '<img src="https://map.vworld.kr/images/maps/logo_openplatform.png"/>',
        }),
        extent: [12523442.714243278, 3130860.6785608195, 15654303.392804097, 6261721],
      });
      store.state.map.addLayer(Layer)
    },
    addWindLayer(url) {
      let Layer = new TileLayer({
        title: windLayerTitle,
        visible: true,
        type: "base",
        source: new XYZ({
          url:
              url+"/{z}/{y}/{x}.jpeg",
          minZoom: 6,
          maxZoom: 19,
          attributions:
              '<img src="https://map.vworld.kr/images/maps/logo_openplatform.png"/>',
        }),
        extent: [12523442.714243278, 3130860.6785608195, 15654303.392804097, 6261721],
      });
      store.state.map.addLayer(Layer)
    },
    removeTempLayer() {
      store.state.map.getLayers().forEach(layer => {
        if (layer && layer.get("title") === tempLayerTitle) {
          store.state.map.removeLayer(layer);
        }
      });
    },
    removePrcpLayer() {
      store.state.map.getLayers().forEach(layer => {
        if (layer && layer.get("title") === prcpLayerTitle) {
          store.state.map.removeLayer(layer);
        }
      });
    },.
    removeWindLayer() {
      store.state.map.getLayers().forEach(layer => {
        if (layer && layer.get("title") === windLayerTitle) {
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