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
          <th>기상자료</th>
        </tr>
      </thead>
      <tbody v-if="result">
        <tr v-for="(d, index) in dataList" :key="index">
          <td><input type="checkbox" :id="'check_'+index" :value="d.satellite+'_'+d.layer" v-model="d.selected" @change="[selected($event),]"></td>
          <td>{{ d.satellite }}</td>
          <td>{{ d.layer }}</td>
          <td>{{ moment(d.date).format('YYYY-MM-DD') }}</td>
          <td><input type="radio" name="weather" @change="weather_date(d.date)"></td>
        </tr>
      </tbody>
      <tbody v-else>
        <tr><td colspan="4" class="noresults">No Items</td> </tr>
      </tbody>
    </table>
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

let store = null;
export default {
  name: "SearchList",
  components:{
    Pagination
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
      weather_selected: ""
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
            if (this.layerList[j].layer == this.dataList[i].layer){
              contained = true;
              break;
            }
          }
          if (!contained){
            this.layerList.push(this.dataList[i]);
            // store.state.selectedList.push(this.layer_url);
            this.addLayerList(this.layer_url, this.dataList[i]);
          }
        } else{
          for(let j in this.layerList){
            if (this.layerList[j].layer == this.dataList[i].layer){
              this.layerList.splice(j,1);
              // store.state.selectedList.splice(j, 1);
              this.removeLayerList(this.dataList[i]);
            }
          }
        }
      }
    },
    getLayerPath(data) {
      console.log(data.satellite);
      this.layer_url=''+data.satellite+'/'+data.layer+'/'+data.date
    },
    addLayerList(url,data) {
      let Layer = new TileLayer({
        title: data.layer,
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
      // store.state.map.addLayer(Layer)
    },
    removeLayerList(data) {
      store.state.map.getLayers().forEach(layer => {
        if (layer && layer.get("title") === data.layer) {
          store.state.map.removeLayer(layer);
        }
      });
    },
    checkList(){
      for(let i in this.dataList){
        for (let j in this.layerList){
          if (this.dataList[i].layer == this.layerList[j].layer){
            this.dataList[i].selected = true;
            continue;
          }
        }
      }
    },
  }


}
</script>

<style scoped lang="scss">

</style>