<template>
  <div class="map_control" style="">
    <a href="#" @click="zoomAll()" class="map_control_btn ico_all" title="All">All</a>
    <a href="#" @click="interaction('move')" class="map_control_btn ico_move" title="Move">Move</a>
  </div>
</template>

<script>
import { useStore } from 'vuex'

let store = null;

export default {
  name: "MapControl",
  mounted() {
    store = useStore()
  },
  methods: {
    zoomAll(){
      let view = store.state.map.getView()
      let center = store.state.center
      view.setCenter(center)
      view.setZoom(7)
    },
    interaction(val){
      switch(val){
        case "move":
          store.state.measureLine.setActive(false);
          store.state.measurePoly.setActive(false);
          break;
        default:
          throw new Error("NotImplemented Control: " + val);
      }
    }
  }
}
</script>

<style scoped>
.map_control {width:68px;border:1px solid #65686f; border-radius: 3px; padding:0 3px; margin-right: 3px; height:30px; background-color: #fff; overflow: hidden; display: inline-block;}
.map_control .map_control_btn {display:inline-block; width:30px; height:30px; border-right:1px solid #ced0d7; text-indent:-9999in;}
.map_control .map_control_btn:last-child {border:none;}
.ico_all {background: url(../../../assets/images/ico_all.png) no-repeat; }
.ico_move {background: url(../../../assets/images/ico_move.png) no-repeat; }
</style>