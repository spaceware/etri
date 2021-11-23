import { createStore } from "vuex";
import ol from "openlayers";
import Tooltip from "ol-ext/overlay/Tooltip";
import Draw from "ol/interaction/Draw"
import TileLayer from "ol/layer/Tile";
import OSM from "ol/source/OSM";
import XYZ from "ol/source/XYZ";
import Map from "ol/Map";
import View from "ol/View";
// import TileWMS from "ol/source/TileWMS"

const center = ol.proj.fromLonLat([127.5, 36]);
const map = {
    osmLayer(osm){
        let osmLayer = new TileLayer({
            title : 'OSM',
            source : new OSM(),
            zIndex:6,
            visible: osm,
        });
        return osmLayer;
    },
    vbaseLayer(topo, vworldKey){
        let vworldBaseLayer = new TileLayer({
            title : 'VWorld Base Map',
            visible : topo,
            type: 'base',
            zIndex:6,
            source : new XYZ({
                url:
                    "https://api.vworld.kr/req/wmts/1.0.0/"+vworldKey+"/Base/{z}/{y}/{x}.png",
                minZoom: 6,
                maxZoom: 19,
                attributions:
                    '<img src="https://map.vworld.kr/images/maps/logo_openplatform.png"/>',
            }),
            extent: [12523442.714243278, 3130860.6785608195, 15654303.392804097, 6261721],
        });
        return vworldBaseLayer
    },
    vsatelLayer(aerial, vworldKey){
        let vworldSatelliteLayer = new TileLayer({
            title: "VWorld Satellite Map",
            visible: aerial,
            type: "base",
            zIndex:6,
            source: new XYZ({
                url:
                    "https://api.vworld.kr/req/wmts/1.0.0/"+vworldKey+"/Satellite/{z}/{y}/{x}.jpeg",
                minZoom: 6,
                maxZoom: 19,
                attributions:
                    '<img src="https://map.vworld.kr/images/maps/logo_openplatform.png"/>',
            }),
            extent: [12523442.714243278, 3130860.6785608195, 15654303.392804097, 6261721],

        });
        return vworldSatelliteLayer;
    },
    // dataLayer(aerial, vworldKey){
    //     return new TileLayer({
    //         title: "dataLayer",
    //         visible: aerial,
    //         zIndex:10,
    //         source: new TileWMS({
    //             url : "http://api.vworld.kr/req/wms",
    //             params: {
    //                 "KEY":vworldKey,
    //                 "LAYERS":"lt_c_usfsffb",
    //                 "STYLES":"lt_c_usfsffb",
    //             },
    //             serverType: "geoserver",
    //         })
    //     })
    // },
    createMap(target, aerial, topo, osm, vworldKey){
        return new Map({
            target: target,
            layers:[
                // this.dataLayer(aerial),
                this.vbaseLayer(topo, vworldKey),
                this.vsatelLayer(aerial, vworldKey),
                this.osmLayer(osm),
            ],
            view: new View({
                zoom: 7,
                center: center,
                constrainRotation: true
            })
        });
    },
};

const interaction = {
    measureLine(){
        let ml = new Draw({type: "LineString"});
        ml.setActive(false);
        return ml;
    },
    measurePoly(){
        let mp = new Draw({type: "Polygon"});
        mp.setActive(false);
        return mp;
    },
    tooltip(){
        let tooltip = new Tooltip();
        return tooltip;
    }
    // selectPoly(){
    //     let sp = new Draw({type: "Polygon"});
    //     sp.setActive(false);
    //     return sp;
    // }
}
// export const store = new Vuex.Store({
//     map: {
//         map: map.createMap(),
//         vworldSatelliteLayer: map.vsatelLayer(),
//         vworldBaseLayer: map.vbaseLayer(),
//         osmLayer: map.osmLayer(),
//     }
// })

// export default createStore({
//     state: {
//         map: map.createMap(),
//         vworldSatelliteLayer: map.vsatelLayer(),
//         vworldBaseLayer: map.vbaseLayer(),
//         osmLayer: map.osmLayer(),
//     }
// })

const store = createStore({
    state: {
        center: center,
        map: map.createMap(),
        // vworldSatelliteLayer: map.vsatelLayer(),
        // vworldBaseLayer: map.vbaseLayer(),
        // osmLayer: map.osmLayer(),
        measureLine : interaction.measureLine(),
        measurePoly : interaction.measurePoly(),
        tooltip : interaction.tooltip()
    },
    mutations: {
        setMapTarget(state, value) {
            state.map = map.createMap(value.ref, value.aerial, value.topo, value.osm, value.vworldKey)
        },

    },
});
export default store