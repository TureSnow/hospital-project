<template>
  <el-container>
    <el-aside width="200px" style="background-color: rgb(238, 241, 246);text-align: left;">
      <div style="align-content: center;text-align: center;">
        <img src="../assets/background/photo.jpg" style="border-radius: 50%;width: 60%;padding: 20px 20% 10px 20%">
        <div style="margin-bottom: 20px">Name:{{user.username}}</div>
      </div>
      <el-menu :default-openeds="['1']" >
        <el-submenu index="1" >
          <template slot="title"><i class="el-icon-menu"></i>查看</template>
          <el-menu-item-group >
            <router-link  to="/wardNurse/patients" style="text-decoration: none"><el-menu-item index="1-3" ><span @click="showPatientList=false">本病房病人</span></el-menu-item></router-link>
          </el-menu-item-group>
          <el-menu-item @click="showPatientList=true">每日登记</el-menu-item>
        </el-submenu>

        <el-menu-item index="2"><template slot="title"><i class="el-icon-message"></i>
          <router-link to="/wardNurse/messages" style="text-decoration: none"><span @click="showPatientList=false">通知</span></router-link></template>
        </el-menu-item>
        <el-menu-item index="3"  @click="logout">
          <template slot="title" ><i class="el-icon-setting"></i>登出
          </template>
        </el-menu-item>
      </el-menu>
    </el-aside>

    <el-container>


      <el-main id="page">
        <div v-if="showPatientList">
          <el-table
            :data="patient_list"
            stripe
            style="width: 100%">
            <el-table-column
              prop="name"
              label="姓名"
              width="180">
            </el-table-column>
            <el-table-column
              prop="age"
              label="年龄">
            </el-table-column>

            <el-table-column
              prop="gender"
              label="性别">
            </el-table-column>
            <el-table-column
              prop="illness_level_str"
              label="病情评级">
            </el-table-column>
            <el-table-column

              label="操作"
              width="100">
              <template slot-scope="scope">
                <el-button @click="handleClick(scope.row)" type="text" size="small">每日登记</el-button>

              </template>
            </el-table-column>
          </el-table>
        </div>
        <div v-else>
          <router-view>

          </router-view>
        </div>

      </el-main>
      <!--    每日登记病人信息-->
      <el-dialog
        title="每日登记病人信息"
        :visible.sync="patientVisible"
        width="40%">
        <el-form :label-position="position" ref="patientForm" :rules="rules" :model="patientForm" label-width="80px">
          <el-form-item label="检测时间" prop="date">
            <el-date-picker
              v-model="patientForm.date"
              type="date"
              :picker-options="pickerOptions"
              placeholder="选择日期">
            </el-date-picker>
          </el-form-item>
          <el-form-item label="体温" prop="temperature">
            <el-input v-model="patientForm.temperature" ></el-input>
          </el-form-item>
          <el-form-item label="症状" prop="symptom">
            <el-input v-model="patientForm.symptom"></el-input>
          </el-form-item>
          <el-form-item label="生命状态" prop="life_state">
            <el-radio-group v-model="patientForm.life_state" size="medium">
              <el-radio-button label="1">住院治疗</el-radio-button>
              <el-radio-button label="2">死亡</el-radio-button>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="检测结果" prop="NA_result">
            <el-radio-group v-model="patientForm.NA_result" size="medium">
              <el-radio-button label="0">阴性</el-radio-button>
              <el-radio-button label="1">阳性</el-radio-button>
            </el-radio-group>
          </el-form-item>
          <el-form-item>
            <el-button @click="patientVisible=false">取消</el-button>
            <el-button type="primary" @click="addDailySheet('patientForm')">添加</el-button>
          </el-form-item>
        </el-form>

      </el-dialog>
    </el-container>
  </el-container>
</template>

<script>
export default {
  name: "wardNurse",
  data() {
    return{
      user:this.$store.state.user,
      patientVisible:false,//是否显示登记病人信息的界面
      showPatientList:false,//是否在主页面显示病人列表
      patientForm:{
        date:new Date(),
        temperature:36.7,
        symptom:"fever",
        life_state:1,
        NA_result:"",

      },
      rules: {
        date: [{required: true, message: "请选择检测时间", trigger: 'change'}],
        symptom: [{required: true, message: "请输入症状", trigger: "blur"}],
        temperature: [{required: true, message: "请输入体温", trigger: "blur"},
          // { type: 'number', message: '体温必须为数字值',trigger: "blur"}
        ],
        NA_result: [{required: true, message: "请输入检测结果", trigger: "blur"}],
        life_state: [{required: true, message: "请输入生命状态", trigger: "blur"}],
      },
      pickerOptions: {
        disabledDate(time) {
          return time.getTime() < Date.now();
        }
      },
      position:"left",
      //我负责的病人
      patient_list:[],
      currPatient:""
    }
  },
  methods:{
    addDailySheet(formName){
      this.$refs[formName].validate((valid) => {
        if (valid) {

          this.$axios.post('/ward/addDailyStatus', {
            date:this.patientForm.date,
            patientId:this.currPatient.id,
            temperature:parseFloat(this.patientForm.temperature),
            lifeState:parseInt(this.patientForm.life_state),
            na_result:parseInt(this.patientForm.NA_result),
            symptom:this.patientForm.symptom
          }).then(res=>{
            console.log(" ",res)

            this.$notify({
              title: '成功',
              message: '添加成功',
              type: 'success'
            });
          }).catch(err=>{
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
      this.patientVisible = false;
    },
    handleClick(patient){
      console.log("handleClick 被点击的病人为")
      console.log(patient)
      this.patientVisible=true
      this.currPatient=patient
      console.log("当前病人是",this.currPatient)
    },
    //用户登出系统
    logout(){
      {
        this.$confirm('确认要退出账户？', 'Confirm', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'info',
          center:true
        }).then(() => {
          this.$store.commit("logout")
          this.$message({
            type: 'success',
            message: '成功退出',
            duration:2000
          });
          this.$router.replace({path:"/"})
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '尚未退出',
            duration:2000
          });
        });
      }
    },
    get_all_patient(){
      this.$axios.get('/ward/allPatient', {
      }).then(res=>{
        console.log(" ",res)
        if(res.data.data!=null){
          res.data.data.forEach(item => {
            item.id=parseInt(item.id)
            item.life_state=parseInt(item.lifeState);
            item.illness_level=parseInt(item.illnessLevel)
            item.illness_level_str=this.$myUtil.myFunc.getIllness_level(parseInt(item.illnessLevel))
            item.area_level=parseInt(item.areaLevel)
            delete item.areaLevel
            delete item.lifeState
            delete item.illnessLevel
          })
        }
        this.patient_list=res.data.data
        console.log("全部病人列表为",this.patient_list)
      }).catch(err=>{
        console.log(err)
      })
    },
  },
  created() {

    // var patient1 = {id: 1, age: 21, name: "patient001", life_state: 1, area_level: 1, gender: "male", illness_level: 1}
    // var patient2 = {id: 2, age: 22, name: "patient002", life_state: 1, area_level: 1, gender: "male", illness_level: 1}
    // var patient3 = {id: 3, age: 23, name: "patient003", life_state: 1, area_level: 2, gender: "female", illness_level: 2}
    // this.myPatients=[patient1,patient2,patient3]
    this.get_all_patient()

  }
}
</script>

<style scoped>
@import url("https://unpkg.com/element-ui/lib/theme-chalk/index.css");

</style>
