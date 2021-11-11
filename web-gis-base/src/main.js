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
app.use(store);
app.use(OpenLayers)
app.mount('#app')
