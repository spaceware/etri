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
        <h3> <b>File Name</b> :  {{modalInformation.file_name}} </h3>
        <h3> <b>Width</b> : {{ modalInformation.width}} </h3>
        <h3> <b>height</b> : {{modalInformation.height }} </h3>
        <h3> <b>Origin Left</b> :  {{modalInformation.origin_left}} </h3>
        <h3> <b>Origin Top</b> : {{ modalInformation.origin_top}} </h3>
        <h3> <b>Resolution</b> : {{modalInformation.resolution }} </h3>
        <h3> <b>Band Counts</b> :  {{modalInformation.band_counts}} </h3>
        <h3> <b>Unit Type</b> : {{ modalInformation.unit_type}} </h3>
        <h3> <b>No Data Value</b> : {{modalInformation.no_data_value }} </h3>
        <h3> <b>Description</b> :  {{modalInformation.description}} </h3>
        <h3> <b>Projection</b> : </h3>
        <h3><span v-html="modalInformation.projection" style="white-space: pre-wrap"> </span></h3>
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
        file_name: "",
        width:"",
        height:"",
        origin_left:"",
        origin_top:"",
        resolution:"",
        band_counts:"",
        unit_type:"",
        no_data_value:"",
        description:"",
        projection:"",
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
            this.addLayerList(this.layer_url, this.dataList[i]);
          }
        } else{
          for(let j in this.layerList){
            if (this.layerList[j].satLayername == this.dataList[i].satLayername){
              this.layerList.splice(j,1);
              this.removeLayerList(this.dataList[i]);
            }
          }
        }
      }
    },
    getLayerPath(data) {
      this.layer_url=data.satDate+'/'+data.satType+'/'+data.satLayername
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
      this.axios.get("/api/satelliteInfo/",{
        params: {
          layer : data.satLayername,
          sat : data.satType,
          date : data.satDate,
        },
      }).then((res)=>{
        vm.modalInformation.file_name=res.data["file name"]
        vm.modalInformation.width=res.data.width
        vm.modalInformation.height=res.data.height
        vm.modalInformation.origin_left=res.data.origin_left
        vm.modalInformation.origin_top=res.data.origin_top
        vm.modalInformation.resolution=res.data.resolution
        vm.modalInformation.band_counts=res.data["band counts"]
        vm.modalInformation.unit_type=res.data["unit type"]
        vm.modalInformation.no_data_value=res.data["no data value"]==null?"null":res.data["no data value"]
        vm.modalInformation.description=res.data.description
        vm.modalInformation.projection=res.data.projection.split('\n').join('<br />')
      }).catch((err)=>{
        console.log(err)
      })



      vm.showModal=true
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