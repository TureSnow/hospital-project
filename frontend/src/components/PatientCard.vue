<template>
  <div>
    <!--    展示筛选列表-->
    <el-row type="flex" justify="end" style="margin-bottom: 30px">
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

      <!--      vue for循环下标从1开始！！！-->
      <el-row v-if="index%3===0">
        <el-col :span="7" :offset="1" v-for="n in 3" :key="n" style="margin-bottom: 10px;">

          <div v-if="index+n-1<patient_list.length">
            <el-card :body-style="{ padding: '0px' }" shadow="hover">
              <div style="padding: 20px;">
                <div style="text-align: left;line-height: 180%;">
                  <div>姓名：{{ patient_list[index + n - 1].name }}</div>
                  <div>所在区域：{{ getAreaLevel(patient_list[index + n - 1].area_level) }}</div>
                  <div>病情评级：{{ getIllness_level(patient_list[index + n - 1].illness_level) }}</div>
                  <div v-if="patient_list[index+n-1].illness_level!=patient_list[index+n-1].area_level">
                    (等待对应区域有病床)
                  </div>
                  <div>生命状态：
                    <el-tag size="small">{{
                        getLifeState(patient_list[index + n - 1].life_state, patient_list[index+n-1].area_level)
                      }}
                    </el-tag>
                  </div>
                </div>

                <div class="">
                  <el-dropdown @command="handleCommand" v-if="user.role===0">
                    <!--                    身份为医生时才可以修改-->
                    <span class="el-dropdown-link">更多<i class="el-icon-arrow-down el-icon--right"></i></span>
                    <el-dropdown-menu slot="dropdown">
                      <el-dropdown-item :command="{event:0,patient_index:index+n-1}">
                        修改病情评级
                      </el-dropdown-item>
                      <el-dropdown-item :command="{event:1,patient_index:index+n-1}">
                        修改生命状态
                      </el-dropdown-item>
                      <el-dropdown-item :command="{event:2,patient_index:index+n-1}">
                        进行核酸检测
                      </el-dropdown-item>
                    </el-dropdown-menu>
                  </el-dropdown>

                </div>
              </div>
            </el-card>
          </div>
        </el-col>
      </el-row>
    </div>

    <!--    添加核酸检测单弹出框-->
    <el-dialog
      title="核酸检测单"
      :visible.sync="detectionVisible"
      width="40%">
      <el-form ref="detectionForm" :rules="rules" :model="detectionForm" label-width="80px">
        <el-form-item label="检测时间" prop="detectionDate">
          <el-date-picker
            v-model="detectionForm.detectionDate"
            type="date"
            :picker-options="pickerOptions"
            placeholder="选择日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="病情评级" prop="detectionRate">
          <el-radio-group v-model="detectionForm.detectionRate" size="medium">
            <el-radio-button label="1">轻症</el-radio-button>
            <el-radio-button label="2">重症</el-radio-button>
            <el-radio-button label="3">危重症</el-radio-button>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="检测结果" prop="detectionResult">
          <el-radio-group v-model="detectionForm.detectionResult">
            <el-radio label="0">阴性</el-radio>
            <el-radio label="1">阳性</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item>
          <el-button @click="detectionVisible = false">取消</el-button>
          <el-button type="primary" @click="addDetection('detectionForm')">添加</el-button>
        </el-form-item>
      </el-form>

    </el-dialog>
    <!--    修改病情评级-->
    <el-dialog
      title="修改病情评级"
      :visible.sync="illnessLevelVisible"
      width="40%">
      <div>
        {{ new_illness_level }}
        <el-radio-group v-model="new_illness_level">
          <el-radio :label="1">轻症</el-radio>
          <el-radio :label="2">重症</el-radio>
          <el-radio :label="3">危重症</el-radio>
        </el-radio-group>
      </div>
      <span slot="footer" class="dialog-footer">
    <el-button @click="illnessLevelVisible = false">取 消</el-button>
    <el-button type="primary" @click="modify_illness_level">确 定</el-button>
  </span>
    </el-dialog>
    <!-- 修改生命状态  -->
    <el-dialog
      title="修改生命状态"
      :visible.sync="lifeStateVisible"
      width="40%">
      <div>
        {{ newLifeState }}
        <el-radio-group v-model="newLifeState">
          <!--          只有轻症医生才能允许出院-->
          <el-radio :label="0" v-if="user.area===1">康复出院</el-radio>
          <el-radio :label="1">在院治疗</el-radio>
          <el-radio :label="2">病亡</el-radio>
        </el-radio-group>
      </div>
      <span slot="footer" class="dialog-footer">
    <el-button @click="lifeStateVisible = false">取 消</el-button>
    <el-button type="primary" @click="modifyLifeState">确 定</el-button>
  </span>
    </el-dialog>
  </div>


</template>

<script>
export default {
  name: "patient_card",
  data() {
    //return之后的大括号不能换行
    return {
      user: this.$store.state.user,
      patient_list: [],
      currPatient: "",//当前的病人
      detectionVisible: false,
      detectionForm: {
        detectionDate: new Date(),
        detectionResult: "0",//0:negative; 1:positive
        detectionRate: "1",//1:mild; 2:severe; 3:critical
      },
      //病情评级 0:health; 1:mild; 2:severe; 3:critical
      illnessLevelVisible: false,
      new_illness_level: -1,
      //修改生命状态 0:healthy; 1:treating; 2:dead
      lifeStateVisible: false,
      newLifeState: -1,
      rules: {
        detectionDate: [{required: true, message: "请选择检测时间", trigger: 'change'}],
        detectionResult: [{required: true, message: "请选择检测结果", trigger: "blur"}],
        detectionRate: [{required: true, message: "请选择病情评级", trigger: "blur"}]
      },

      pickerOptions: {
        disabledDate(time) {
          return time.getTime() < Date.now();
        }
      },
      //筛选结果
      options: [{value: 0, label: "全部"}, {value: 1, label: "可出院"},
        {value: 2, label: "未出院"}, {value: 3, label: "待转入对应治疗区域"}, {value: 4, label: "已转入对应治疗区域"},
        {value: 5, label: "康复出院"}, {value: 6, label: "在院治疗"}, {value: 7, label: "病亡"}],
      screenResult: 0

    }

  },
  methods: {
    test(num) {
      console.log("wwwww")
    },
    //根据条件筛选病人
    screenPatients() {
      console.log(" 筛选结果为 " + this.screenResult)
    },
    getRole(num) {
      //0:doctor; 1:head_nurse; 2:ward_nurse; 3:emergency_nurse
      switch (num) {
        case 0:
          return "医生";
        case 1:
          return "护士长"
        case 2:
          return "病房护士"
        case 3:
          return "急诊护士"
      }

    },
    getIllness_level(num) {
      // illness_level -- 0:health; 1:mild; 2:severe; 3:critical
      switch (num) {
        case 0:
          return "健康";
        case 1:
          return "轻症"
        case 2:
          return "重症"
        case 3:
          return "危重症"
      }
    },
    getAreaLevel(num) {
      return this.$myUtil.myFunc.getAreaLevel(num)
    },
    getLifeState(life_state, area_level) {
      return this.$myUtil.myFunc.getLifeState(life_state, area_level)
    },
    //添加核酸检测单
    addDetection(formName) {
      console.log("addDetection 当前病人为", this.currPatient)
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.$notify({
            title: '成功',
            message: '添加成功',
            type: 'success'
          });
          this.$axios.post('/user/doctor/addSheet', {
            illnessLevel: this.detectionForm.detectionRate,
            patientId: this.currPatient.id,
            date: this.detectionForm.detectionDate,
            result: this.detectionForm.detectionResult,
          }).then(res => {
            console.log(res)
            this.detectionVisible = false;
            this.$notify({
              title: 'success',
              message: res.data.data,
              type: 'success'
            });
          }).catch(err => {
            console.log(err)
          })

        } else {
          this.$notify.error({
            title: '错误',
            message: '输入错误'
          });
          return false;
        }
      });
      // this.detectionVisible = false;

    },
    //修改生命状态
    modifyLifeState() {
      console.log("modifyLifeState 当前病人为", this.currPatient)
      console.log("modifyLifeState 新生命状态为 " + this.newLifeState)
      if (this.newLifeState === -1) {
        this.$notify({
          title: '错误',
          message: '尚未选择生命状态',
          type: 'error'
        });
      } else {
        if (this.newLifeState === 0) {//允许病人出院
          this.$axios.post('/user/doctor/discharge', {
            patientId: this.currPatient.id
          }).then(res => {
            console.log("允许病人出院", res)
            this.lifeStateVisible = false
            this.updatePatientList(0)
            this.$notify({
              title: 'info',
              message: res.data.data,
              type: 'info'
            });
          }).catch(err => {
            console.log(err)
          })
        } else {
          this.$axios.post('/user/doctor/updateLife', {
            level: this.newLifeState,
            patientId: this.currPatient.id
          }).then(res => {
            console.log(res)
            this.lifeStateVisible = false
            this.updatePatientList(0)
            // this.currPatient.life_state=this.newLifeState
            this.$notify({
              title: 'success',
              message: res.data.data,
              type: 'success'
            });
          }).catch(err => {
            console.log(err)
          })
        }

      }

    },
    //修改病情评级
    modify_illness_level() {
      console.log("modify_illness_level 当前病人为", this.currPatient)
      console.log(this.new_illness_level)
      if (this.new_illness_level === -1) {
        this.$notify({
          title: '错误',
          message: '尚未选择生命状态',
          type: 'error'
        });
      } else {
        this.$axios.post('/user/doctor/updateLevel', {
          level: this.new_illness_level,
          patientId: this.currPatient.id
        }).then(res => {
          console.log("modifyIllness_level 返回结果为", res)
          this.illnessLevelVisible = false
          //更新病人状态
          this.updatePatientList(0)
          // this.currPatient.illness_level=this.new_illness_level

          this.$notify({
            title: 'success',
            message: res.data.data,
            type: 'success'
          });
        }).catch(err => {
          console.log(err)
        })


      }


    },
    handleCommand(obj) {
      //形如{event:0,patient_index:index+n-2}的obj
      console.log("handleCommand num、index is" + obj.event + " " + obj.patient_index)
      //先将当前被点击的病人信息存下来
      this.currPatient = this.patient_list[obj.patient_index]
      console.log("handleCommand 重新设置当前病人为", this.currPatient)
      switch (obj.event) {
        case 0:
          this.illnessLevelVisible = true
          this.new_illness_level = -1
          console.log("aaa")
          break;
        case 1:
          this.lifeStateVisible = true
          this.newLifeState = -1
          console.log("bbbb")
          break;
        case 2:
          this.detectionVisible = true
          break
        default:
          console.log("handleCommand 事件错误")
      }
    },
    updatePatientList(screen_result) {
      //根据传入的筛选结果构造出发往后端的三个值
      //以下是筛选结果对应的值的含义 0:全部
      //根据是否满足出院条件 1:可出院 2:未出院
      //根据是否待转入其它治疗区域 3:待转入对应治疗区域  4:已转入对应治疗区域
      //根据病人的生命状态  5:康复出院 6:在院治疗 7:病亡

      // * @param lifeState  0:health; 1:treating; 2:dead; 3:all
      //   * @param isMatchWard 0:match 当前病房与病情评级相符，已转入; 1:not match; 2:all ok 是否满足转移条件
      //   * @param IllnessLevel 0:health; 1:mild; 2:severe; 3:critical; 4:all ok
      var life_state = 3, is_match_ward = 2
      switch (screen_result) {
        case 0:
          //取全部病人，则什么都不要改
          break
        case 1:
          // 1:可出院 todo 可出院 可出院是lifeState=0但是arealevel不等于4（home） 康复出院是lifeState=0而且arealevel=4
          life_state = 0

          break
        case 2:
          life_state = 1
          break
        case 3:
          is_match_ward = 1
          break
        case 4:
          is_match_ward = 0
          break
        case 5: //5:康复出院
        {
          life_state = 0
          this.$axios.get('/user/homePatient', {}).then(res => {
            console.log(" ", res)
            if (res.data.data != null) {
              res.data.data.forEach(item => {
                item.id = parseInt(item.id)
                item.life_state = parseInt(item.lifeState);
                item.illness_level = parseInt(item.illnessLevel)
                item.area_level = parseInt(item.areaLevel)
                delete item.areaLevel
                delete item.lifeState
                delete item.illnessLevel
              })
            }
            this.patient_list = res.data.data
            console.log("可出院的 病人列表为", this.patient_list)

          }).catch(err => {
            console.log(err)
          })
          //直接退出
          return
        }

          break
        case 6:
          life_state = 1
          break
        case 7://病亡是lifeState=2&areaLevel=4
        {
          life_state = 2
          this.$axios.get('/user/deathPatient', {
          }).then(res=>{
            console.log(" ",res)
            if (res.data.data != null) {
              res.data.data.forEach(item => {
                item.id = parseInt(item.id)
                item.life_state = parseInt(item.lifeState);
                item.illness_level = parseInt(item.illnessLevel)
                item.area_level = parseInt(item.areaLevel)
                delete item.areaLevel
                delete item.lifeState
                delete item.illnessLevel
              })
            }
            this.patient_list = res.data.data
            console.log("病亡的 病人列表为", this.patient_list)
          }).catch(err=>{
            console.log(err)
          })
        }
          break
      }

      this.$axios.post('/user/filter', {
        lifeState: life_state,
        isMatch: is_match_ward,
        illnessLevel: 4
      }).then(res => {
        console.log(res)

        if (res.data.data != null) {
          res.data.data.forEach(item => {
            item.id = parseInt(item.id)
            item.life_state = parseInt(item.lifeState);
            item.illness_level = parseInt(item.illnessLevel)
            item.area_level = parseInt(item.areaLevel)
            delete item.areaLevel
            delete item.lifeState
            delete item.illnessLevel
          })
        }
        this.patient_list = res.data.data
        console.log("初始化 or 更新后的 病人列表为", this.patient_list)
      }).catch(err => {
        console.log(err)
      })
    }
  },
  mounted() {

  },
  watch: {
    screenResult(newVal, oldVal) {
      console.log("screenResult 新值为" + newVal)
      this.updatePatientList(newVal)
    }
  },


  created() {
    this.updatePatientList(0)

  }
}
</script>

<style scoped>
@import url("https://unpkg.com/element-ui/lib/theme-chalk/index.css");

.el-dropdown-link {
  cursor: pointer;
  color: #409EFF;
}

.el-icon-arrow-down {
  font-size: 12px;
}
</style>
