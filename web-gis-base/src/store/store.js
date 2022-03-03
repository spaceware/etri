import { createStore } from "vuex";
import Tooltip from "ol-ext/overlay/Tooltip";
import * as olProj from "ol/proj";
import Draw from "ol/interaction/Draw"
import TileLayer from "ol/layer/Tile";
import OSM from "ol/source/OSM";
import XYZ from "ol/source/XYZ";
import Map from "ol/Map";
import View from "ol/View";

const center = olProj.fromLonLat([127.5, 36]);
const map = {
    osmLayer(){
        let osmLayer = new TileLayer({
            title : 'OSM',
            source : new OSM(),
            zIndex:6,
            visible: false,
        });
        return osmLayer;
    },
    vbaseLayer(vworldKey){
        let vworldBaseLayer = new TileLayer({
            title : 'VWorld Base Map',
            visible : false,
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
    vsatelLayer(vworldKey){
        let vworldSatelliteLayer = new TileLayer({
            title: "VWorld Satellite Map",
            visible: true,
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
    createMap(target, vworldKey){
        return new Map({
            target: target,
            layers:[
                // this.dataLayer(aerial),
                this.vbaseLayer(vworldKey),
                this.vsatelLayer( vworldKey),
                this.osmLayer(),
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
}

const store = createStore({
    state: {
        center: center,
        map: map.createMap(),
        measureLine : interaction.measureLine(),
        measurePoly : interaction.measurePoly(),
        tooltip : interaction.tooltip()
    },
    mutations: {
        setMapTarget(state, value) {
            state.map = map.createMap(value.ref, value.vworldKey)
        },

    },
});
export default store