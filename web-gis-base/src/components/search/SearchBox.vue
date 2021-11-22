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
            <label for="KOMPSAT"><input type="checkbox" id="KOMPSAT" value="K3A" v-model="optionList.satellite" checked>KOMPSAT</label>
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
        startDate: new Date(2019,3,3),
        endDate: new Date(2019,3,20),
        satellite: ["SEN", "LAN", "K3A"],
      }
    }
  },
  components:{
    Datepicker
  },
  methods:{
    getDateFormat(newDate){
      const year = newDate.getFullYear();
      const month = newDate.getMonth() + 1;
      const date = newDate.getDate();
      return `${year}${month >= 10 ? month : '0' + month}${date >= 10 ? date : '0' + date}`
    },
    getList() {
      const url = "/api/search";
      let formData = new FormData();

      let startDt = this.getDateFormat(this.optionList.startDate)
      let endDt = this.getDateFormat(this.optionList.endDate)
      formData.append("startDate", startDt);
      formData.append("endDate",endDt);
      formData.append("satellite",this.optionList.satellite);

      console.log("E", formData);
      this.axios.post(url, formData)
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