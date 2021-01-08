<template>
  <div>
    <!--    展示筛选列表-->
    <el-row type="flex"  justify="end" style="margin-bottom: 30px">
      <el-select v-model="screenResult" filterable placeholder="筛选条件">
        <el-option
          v-for="item in options"
          :key="item.value"
          :label="item.label"
          :value="item.value">
        </el-option>
      </el-select>
    </el-row>

    <!--    展示病人列表-->
    <div v-for="(item,index) in patient_list" :key="index">
<!--      row:  index: {{index}}-->
      <el-row v-if="index%3===0">
        <el-col :span="7" :offset="1" v-for="n in 3" :key="n" style="margin-bottom: 10px;">
          <!--      vue for循环下标从1开始！！！-->
<!--          index: {{index}}  index+n-1: {{index+n-1}} n:{{n}}-->
          <div v-if="index+n-1<patient_list.length">
            <el-card :body-style="{ padding: '0px' }" shadow="hover">
              <div style="padding: 20px;">
                <div style="text-align: left;line-height: 180%;">
                  <div>姓名：{{ patient_list[index+n-1].name }}</div>
                  <div>所在区域：{{ getAreaLevel(patient_list[index+n-1].area_level) }}</div>
                  <div>病情评级：{{ getIllness_level(patient_list[index + n - 1].illness_level) }}</div>
                  <div v-if="patient_list[index+n-1].illness_level!=patient_list[index+n-1].area_level">
                    (等待对应区域有病床)
                  </div>
                  <div>生命状态：
                    <el-tag size="small">{{ getLifeState(patient_list[index+n-1].life_state,patient_list[index+n-1].area_level) }}</el-tag>
                  </div>
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
  name: "PatientCardForEmergency",
  data(){
    return{
      user:this.$store.state.user,
      patient_list: [],
      currPatient: "",//当前的病人
      //筛选结果
      options:[{value:0,label:"全部"},{value:1,label:"隔离区"},
        {value:2,label:"轻症病房"},{value:3,label:"重症病房"},{value:4,label:"危重症病房"},
        {value:5,label:"轻症"},{value:6,label:"重症"},{value:7,label:"危重症"},
        {value:8,label:"可出院"},{value:9,label:"治疗中"},{value:10,label:"病亡"}
      ],
      screenResult:0
    }
  },
  methods:{
    // "area":"1",0：isolation;1:mild;2；severe;3:critical 4:all
    // "illnessLevel":"1", 1:mild;2severe;3:critical 4:all
    // "lifeState":"1" 0:health;1:treating;2:death;3:all
    updatePatientList(screen_result){
      //根据传入的筛选结果构造出发往后端的三个值

      var life_state=3, area=4,illness_level=4
      switch (screen_result){
        case 0:
          //取全部病人，则什么都不要改
          break
        case 1:
          area=0
          break
        case 2:
          area=1
          break
        case 3:
          area=2
          break
        case 4:
          area=3
          break
        case 5:
          illness_level=1
          break
        case 6:
          illness_level=2
          break
        case 7:
          illness_level=3
          break
        case 8:
          life_state=0
          break
        case 9:
          life_state=1
          break
        case 10:
          life_state=2
          break
      }

      this.$axios.post('/emergency/filter', {
        area:area,
        illnessLevel:illness_level,
        lifeState:life_state
      }).then(res=>{
        console.log(res)

        if(res.data.data!=null){
          res.data.data.forEach(item => {
            item.id=parseInt(item.id)
            item.life_state=parseInt(item.lifeState);
            item.illness_level=parseInt(item.illnessLevel)
            item.area_level=parseInt(item.areaLevel)
            delete item.areaLevel
            delete item.lifeState
            delete item.illnessLevel
          })
        }
        this.patient_list=res.data.data
        console.log("初始化 or 更新后的 病人列表为",this.patient_list)
      }).catch(err=>{
        console.log(err)
      })
    },
    getAreaLevel(num){
      return this.$myUtil.myFunc.getAreaLevel(num)
    },
    getIllness_level(num){
      return this.$myUtil.myFunc.getIllness_level(num)
    },
    getLifeState(life_state,area_level){
      return this.$myUtil.myFunc.getLifeState(life_state,area_level)
    }

  },
  watch:{
    screenResult(newVal,oldVal){
      console.log("screenResult 新值为"+newVal)
      this.updatePatientList(newVal)
    }
  },
  created() {
    //初始化病人列表
    this.updatePatientList(0)
  }
}
</script>

<style scoped>

</style>
