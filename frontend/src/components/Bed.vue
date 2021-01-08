<template>
  <div>
<!--    {{bed_list}}-->
    <!--    展示病床列表-->
    <div v-for="(item,index) in bed_list" :key="index">

      <!--      vue for循环下标从1开始！！！-->
      <el-row v-if="index%3===0">
        <el-col :span="7" :offset="1" v-for="n in 3" :key="n" style="margin-bottom: 10px;">

          <div v-if="index+n-1<bed_list.length">
            <el-card :body-style="{ padding: '0px' }" shadow="hover">

              <div style="padding: 20px;">
                <div @click="showPatient(index + n - 1)" style="text-align: left;line-height: 180%;">
                  <div >病房号：{{ bed_list[index + n - 1].wardId }}号</div>
                  <div >病床号：{{ bed_list[index + n - 1].bedId }}号</div>
                </div>

              </div>
            </el-card>
          </div>
        </el-col>
      </el-row>
    </div>

  </div>
</template>

<script>
export default {
  name: "bed",
  data() {
    return {
      bed_list: [],
    }

  },
  methods: {
    showPatient(num) {
      console.log("showPatient ", num)
      const h = this.$createElement;
      console.log("bed list", this.bed_list[num])
      let patient = this.bed_list[num].patient
      console.log("patient ", patient)
      let msg=""
      if(!patient){
        msg="病床空闲"
      }else{
        msg = " " + patient.name + "  "
          +this.getIllnessLevel(patient.illness_level)+"   "
          +this.getLifeState(patient.life_state)
      }
      this.$notify({
        title: '病人信息',
        message: h('div', {style: 'color: teal'}, msg)
      });
    },
  getLifeState(num) {
    return this.$myUtil.myFunc.getLifeState(num)
  },
  getIllnessLevel(num) {
    return this.$myUtil.myFunc.getIllness_level(num)
  },
  // get_all_bed_and_patient() {
  //
  //   this.$axios.get('/head/allBed', {}).then(res => {
  //     console.log("get_all_bed_and_patient  ", res)
  //     this.bed_list = res.data.data
  //     // this.get_all_patient()
  //     console.log("get_all_bed_and_patient bed_list is",this.bed_list)
  //
  //   }).catch(err => {
  //     console.log("error ", err)
  //   })
  // },
  get_all_patient() {
    var that = this
    //请求多个病床上的病人
    console.log("获取到的所有病床为 ", this.bed_list)

    const promise_arr = this.bed_list.map(item => {
      return this.get_patient(item)
    })
    Promise.all(promise_arr).then((res) => {
      console.log("输入参数promise_arr为", promise_arr)
      console.log("Promise.all 返回结果为", res.data)
    })
    console.log("bbbbbbbbbbbbbbbb")

    for (let i = 0; i < this.bed_list.length; i++) {

      promise_arr[i].then(function (result) {
        console.log("promise_arr[i] ", result)
        if (result != null) {
          result.id = parseInt(result.id)
          result.life_state = parseInt(result.lifeState);
          result.illness_level = parseInt(result.illnessLevel)
          result.area_level = parseInt(result.areaLevel)
          delete result.areaLevel
          delete result.lifeState
          delete result.illnessLevel
        }
        that.bed_list[i].patient = result
        console.log("promise_arr[i] 更新后的病人病床列表", that.bed_list)

      }).catch(err => {
        console.log("promise_arr[i] error ", err)
      })
    }

    console.log("aaaaaaaaaaaaaaaaaaaaaaaaa")
    console.log("更新后的病人、病床为 ", this.bed_list)
  }
  ,
  get_patient(item) {
    console.log("get_patient 传入的参数为 ", item)
    return new Promise((resolve, reject) => {
      this.$axios.post('/head/bed2patient', {
        bedId: item.id
      }).then(res => {
        console.log("get_patient promise 返回结果为 ", res.data)
        resolve(res.data.data)
      }).catch(err => {
        console.log(err)
        reject(err.data)
      })
    })

  },
},

mounted() {

},

  created() {

    this.$axios.get('/head/allBed', {}).then(res => {
      console.log("get_all_bed_and_patient  ", res)
      this.bed_list = res.data.data
      console.log("before get_all_patient")
      this.get_all_patient()
      console.log("after get_all_patient")
      console.log("get_all_bed_and_patient bed_list is", this.bed_list)
    }).catch(err => {
      console.log("error ", err)
    })
  }
}
</script>

<style scoped>

</style>
