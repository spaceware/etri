<template>

  <div class="search_result_wrap">
    <p class="result_title">검색결과</p>
    <table class="search_results">
      <thead>
        <tr>
          <th><input type="checkbox" v-model="allChecked" @click="checkedAll($event.target.checked)"> </th>
          <th>위성</th>
          <th>레이어명</th>
          <th>촬영시기</th>
          <th><img class="weatherImage" :src="weatherImage"/></th>
        </tr>
      </thead>
      <tbody v-if="result">
        <tr v-for="(d, index) in dataList" :key="index">
          <td><input type="checkbox" :id="'check_'+index" :value="d.satType+'_'+d.satLayername" v-model="d.selected" @change="selected()"></td>
          <td>{{ d.satType }}</td>
          <td><a style="cursor:pointer" @click="getLayerDetails(d)">{{ d.satLayername }}</a></td>
          <td>{{ moment(d.satDate).format('YYYY-MM-DD') }}</td>
          <td><input type="radio" name="weather" @change="weather_date(d.satDate)"></td>
        </tr>
      </tbody>
      <tbody v-else>
        <tr><td colspan="4" class="noresults">No Items</td> </tr>
      </tbody>
    </table>
    <modal v-if="showModal" @close="showModal = false">
      <template class="custom-modal-body"  v-slot:body>
        <h3> <b>레이어명</b> :  {{modalInformation.layer}} </h3>
        <h3> <b>일   시</b> : {{ modalInformation.date}} </h3>
        <h3> <b>위 성 명</b> : {{modalInformation.sat }} </h3>
      </template>
    </modal>
    <Pagination
        v-model="currentPage"
        :records="total"
        :per-page="perPage"
        :options="option"
        @paginate="onPageClick"
      />
  </div>
</template>

<script>
import _ from 'lodash'
import Pagination from 'v-pagination-3'
import { useStore } from "vuex";
import TileLayer from "ol/layer/Tile";
import XYZ from "ol/source/XYZ";
import moment from "moment";
import weatherImage from "@/assets/images/weather.png"

import Modal from '@/components/Modal'

let store = null;
export default {
  name: "SearchList",
  components:{
    Pagination,
    Modal
  },
  created() {
    this.moment=moment;
  },
  mounted() {
    store = useStore()
    this.emitter.on('addData', this.renderList)
  },
  data(){
    return {
      option:{
        texts:{
          count:''
        }
      },
      allChecked: false,
      result: false,
      currentPage: 0,
      perPage: 10,
      total: 0,
      rawData:[],
      dataList:[],
      layerList:[],
      selectedList:[],
      weather_selected: "",
      showModal: false,
      modalInformation: {
        layer: "",
        date:"",
        sat:"",
      },
      weatherImage: weatherImage
    };
  },
  methods:{
    onPageClick(event){
      this.currentPage = event;
      this.getList(event);
      for(let i in this.dataList){
        if(! this.dataList[i].selected){
          this.allChecked = false;
          break;
        } else {
          this.allChecked = true;
        }
      }
    },
    getList(currentPage){
      if (_.isEmpty(this.rawData)){
        this.result = false;
      } else {
        this.result = true;
        this.dataList = this.rawData.slice((currentPage-1) * this.perPage, (currentPage-1) * this.perPage + this.perPage)
      }
      this.checkList();
    },
    renderList(res){
      this.rawData = res.data;
      console.log(this.rawData)
      this.total = this.rawData.length;
      this.currentPage = 1
      this.getList(this.currentPage);
      this.allChecked = false;
    },
    checkedAll(checked){
      this.allChecked = checked;
      for(let i in this.dataList){
        this.dataList[i].selected = this.allChecked;
      }
      this.getSelected()
    },
    selected(){
      for(let i in this.dataList){
        if(! this.dataList[i].selected){
          this.allChecked = false;
          break;
        } else {
          this.allChecked = true;
        }
      }
      this.getSelected()
    },
    weather_date(date){
      this.$emit("weather_date",date)
    },
    getSelected(){
      for (let i in this.dataList){
        if(this.dataList[i].selected){
          this.getLayerPath(this.dataList[i])
          let contained = false;
          for(let j in this.layerList){
            if (this.layerList[j].satLayername == this.dataList[i].satLayername){
              contained = true;
              break;
            }
          }
          if (!contained){
            this.layerList.push(this.dataList[i]);
            // this.selectedList.push(this.layer_url);
            this.addLayerList(this.layer_url, this.dataList[i]);
          }
        } else{
          for(let j in this.layerList){
            if (this.layerList[j].satLayername == this.dataList[i].satLayername){
              this.layerList.splice(j,1);
              // this.selectedList.splice(j, 1);
              this.removeLayerList(this.dataList[i]);
            }
          }
        }
      }
    },
    getLayerPath(data) {
      this.layer_url='http://192.168.1.77:8081/'+data.satDate+'/'+data.satType+'/'+data.satLayername

      // this.layer_url = 'http://192.168.1.77:8081/20190405/K3A/K3_20190405042527_36717_09411281_L1O'
    },
    addLayerList(url,data) {
      let Layer = new TileLayer({
        title: data.satLayername,
        visible: true,
        type: "base",
        zIndex:15,
        source: new XYZ({
          url:
              url+"/{z}/{x}/{y}.png",
          minZoom: 6,
          maxZoom: 19,
        }),
        extent: [12523442.714243278, 3130860.6785608195, 15654303.392804097, 6261721],
      });
      store.state.map.addLayer(Layer)
    },
    removeLayerList(data) {
      store.state.map.getLayers().forEach(layer => {
        if (layer && layer.get("title") === data.satLayername) {
          store.state.map.removeLayer(layer);
        }
      });
    },
    checkList(){
      for(let i in this.dataList){
        for (let j in this.layerList){
          if (this.dataList[i].satLayername == this.layerList[j].satLayername){
            this.dataList[i].selected = true;
            continue;
          }
        }
      }
    },
    getLayerDetails(data){
      const vm=this;

      this.axios.get("/api/modal/",{
        params: {
          layer : data.satLayername,
          sat : data.satType,
          date : data.satDate,
        },
      }).then((res)=>{
        console.log(res.data)
      }).catch((err)=>{
        console.log(err)
      })
      vm.modalInformation.layer=data.satLayername
      vm.modalInformation.sat=data.satType
      vm.modalInformation.date=data.satDate

      vm.showModal=true
      // let url = ''
      // let params = {};
      // params.layer = layer;
      // this.axios.post(url, params)
      //     .then((res)=>{
      //       // need Modal
      //     })
      //     .catch((err)=>{
      //       console.log(err);
      //     })
    }
  }


}
</script>

<style scoped lang="scss">

.modal-body h3{
  font-size: 0.9rem; text-align: left; line-height: 1.0rem
}
.modal-header h2 b{
  margin-top: 0;
  color: #42b983;
  font-size: 1.1rem;
}

.weatherImage {
  width: auto; height: 40px;
}

</style>