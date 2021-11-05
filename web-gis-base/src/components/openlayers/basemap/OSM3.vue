<template>
  <div ref="map-root" style="width: 800px; height: 800px">

  </div>
</template>

<script>
// import {
//   ref
// } from 'vue'
import ol from 'openlayers'
import View from 'ol/View'
import Map from 'ol/Map'
import TileLayer from 'ol/layer/Tile'
import OSM from 'ol/source/OSM'

// importing the OpenLayers stylesheet is required for having
// good looking buttons!
import 'ol/ol.css'
// const center = ol.proj.fromLonLat([127.5, 36]);

export default {
  name: 'MapContainer',
  props: ['aerial', "topo", "osm"],

  mounted() {
    const center = [127.5, 36];
    const osmLayer = new TileLayer({
      title : 'OSM',
      source : new OSM(),
      visible: false,
    });

    const vworldBaseLayer = new TileLayer({
      title : 'VWorld Base Map',
      visible : false,
      type: 'base',
      source : new ol.source.XYZ({
        url:
            'https://cors-anywhere.herokuapp.com/'+
            "https://api.vworld.kr/req/wmts/1.0.0/BC8C80D9-1FA8-38A8-BF31-47B2DCFC5982/Base/{z}/{y}/{x}.png",
        minZoom: 6,
        maxZoom: 19,
        attributions:
            '<img src="https://map.vworld.kr/images/maps/logo_openplatform.png"/>',
      }),
      extent: [12523442.714243278, 3130860.6785608195, 15654303.392804097, 6261721],
    });
    const vworldSatelliteLayer = new TileLayer({
      title: "VWorld Satellite Map",
      visible: true,
      type: "base",
      source: new ol.source.XYZ({
        url:
            'https://cors-anywhere.herokuapp.com/'+
            "https://api.vworld.kr/req/wmts/1.0.0/BC8C80D9-1FA8-38A8-BF31-47B2DCFC5982/Satellite/{z}/{y}/{x}.jpeg",
        minZoom: 6,
        maxZoom: 19,
        attributions:
            '<img src="https://map.vworld.kr/images/maps/logo_openplatform.png"/>',
      }),
      extent: [12523442.714243278, 3130860.6785608195, 15654303.392804097, 6261721],

    });
    new Map({
        target: this.$refs['map-root'],
        layers:[
            osmLayer,
            vworldBaseLayer,
            vworldSatelliteLayer,
        ],
        view: new View({
          projection: 'EPSG:4326',
          zoom: 7,
          center: center,
          constrainRotation: true
        })
    });
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