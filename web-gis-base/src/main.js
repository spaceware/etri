import { createApp } from 'vue'
import App from './App.vue'
import store from './store/store'
import axios from 'axios';



// main.js

//import router from './routes'
import OpenLayers from 'openlayers'
import mitt from "mitt"

const emitter = mitt()
const app = createApp(App)
app.config.globalProperties.axios = axios;
app.config.globalProperties.emitter = emitter;

// V World Key Property
app.config.globalProperties.vworldKey = "CF1E6214-91ED-342A-BDB4-B54554CACF78";

app.use(store);
app.use(OpenLayers)
app.mount('#app')
