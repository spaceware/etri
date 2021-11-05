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
        </tr>
      </thead>
      <tbody v-if="result">
        <tr v-for="(d, index) in dataList" :key="index">
          <td><input type="checkbox" :id="'check_'+index" :value="d.satellite+'_'+d.layer" v-model="d.selected" @change="selected($event)"></td>
          <td>{{ d.satellite }}</td>
          <td>{{ d.layer }}</td>
          <td>{{ d.date }}</td>
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
export default {
  name: "SearchList",
  components:{
    Pagination
  },
  mounted() {
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
    selected(e){
      console.log(e)
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
    getSelected(){
      let selectedList = [];
      for (let i in this.dataList){
        if(this.dataList[i].selected){
          selectedList.push(this.dataList[i]);
          let contained = false;
          for(let j in this.layerList){
            if (this.layerList[j].layer == this.dataList[i].layer){
              contained = true;
              break;
            }
          }
          if (!contained){
            this.layerList.push(this.dataList[i]);
          }
        } else{
          for(let j in this.layerList){
            if (this.layerList[j].layer == this.dataList[i].layer){
              this.layerList.splice(j,1);
            }
          }
        }
      }
      console.log(this.layerList);
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
    }

  }


}
</script>

<style scoped lang="scss">

</style>