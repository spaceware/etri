<template>
  <div class="search_tbl">
    <table class="search_01">
      <colgroup>
        <col width="120px"/>
        <col width=""/>
      </colgroup>
      <tbody>
        <tr>
          <th class="search-tag">기간</th>
          <td style="display: inline-flex">
            <datepicker
              v-model="optionList.startDate"
              class="sub_option"
              name="startDatepicker"
              inputFormat="yyyy-MM-dd"
              id="startDatepicker"
              ref="startDatepicker"
              style="width:95px; height: 30px;"></datepicker>
            <a class="ui-datepicker-trigger" @click="startDate" alt="Show Calendar" title="Show Calendar" style="margin-right: 5px;"/> ~
            <datepicker
                v-model="optionList.endDate"
                class="sub_option"
                name="endDatepicker"
                inputFormat="yyyy-MM-dd"
                id="endDatepicker"
                ref="endDatepicker"
                style="width:95px; height: 30px; margin-left: 5px;" />
            <a class="ui-datepicker-trigger" @click="endDate" alt="Show Calendar" title="Show Calendar" />
          </td>
        </tr>
        <tr>
          <th class="search-tag">위성</th>
          <td>
            <label for="Sentinel"><input type="checkbox" id="Sentinel" value="SEN" v-model="optionList.satellite" checked>Sentinel</label>
            <label for="Landsat"><input type="checkbox" id="Landsat" value="LAN" v-model="optionList.satellite" checked>Landsat</label>
            <label for="KOMPSAT"><input type="checkbox" id="KOMPSAT" value="KOM" v-model="optionList.satellite" checked>KOMPSAT</label>
          </td>
        </tr>
      </tbody>
    </table>
    <a href="javascript:;" @click="getList" class="btn_comm_01" style="margin-top:10px;">검색</a>
  </div>

</template>

<script>
import Datepicker from 'vue3-datepicker';
export default {
  name: "SearchBox.vue",
  data(){
    return {
      optionList:{
        startDate: new Date(),
        endDate: new Date(),
        satellite: ["SEN", "LAN", "KOM"],
      }
    }
  },
  components:{
    Datepicker
  },
  methods:{
    getList() {
      const url = "http://192.168.1.90:8080/api/test";
      let params = {};
      params.startDate = this.optionList.startDate;
      params.endDate = this.optionList.endDate;
      params.satellite = this.optionList.satellite;

      console.log("E", params);
      this.axios.post(url)
          .then((res)=>{
            this.emitter.emit("addData", res);
          })
          .catch((err)=>{
            console.log(err);
          })
    },
    startDate(){
      this.$refs.startDatepicker.$el.querySelector('.sub_option').click();
    },
    endDate(){
      this.$refs.endDatepicker.$el.querySelector('.sub_option').click();
    },
  }
}
</script>

<style scoped>

</style>