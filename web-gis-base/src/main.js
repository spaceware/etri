import { createApp } from 'vue'
import App from './App.vue'
import store from './store/store'
import axios from 'axios';



// main.js

import mitt from "mitt"
axios.defaults.baseURL = "/eos-view"
const emitter = mitt()
const app = createApp(App)
app.config.globalProperties.axios = axios;
app.config.globalProperties.emitter = emitter;

app.use(store);
app.mount('#app')
