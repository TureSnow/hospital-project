<template>
  <el-container>

    <el-aside  width="200px" style="background-color: rgb(238, 241, 246);text-align: left;height: 100%;">
      <div style="align-content: center;text-align: center;">
        <img src="../assets/background/photo.jpg" style="border-radius: 50%;width: 60%;padding: 20px 20% 10px 20%">
        <div style="margin-bottom: 20px">Name:{{user.username}}</div>
      </div>
      <el-menu :default-openeds="['1']" >
        <el-submenu index="1" >
          <template slot="title"><i class="el-icon-menu"></i>查看</template>
          <el-menu-item-group >
            <router-link to="/emergencyNurse/patients" style="text-decoration: none"><el-menu-item index="1-3" >本病房病人</el-menu-item></router-link>
          </el-menu-item-group>
          <el-menu-item @click="patientVisible=true">登记新病人</el-menu-item>
        </el-submenu>

        <el-menu-item index="2"><template slot="title"><i class="el-icon-message"></i>
          <router-link to="/emergencyNurse/messages" style="text-decoration: none">通知</router-link></template>
        </el-menu-item>
        <el-menu-item index="3"  @click="logout">
          <template slot="title" ><i class="el-icon-setting"></i>登出
          </template>
        </el-menu-item>
      </el-menu>
    </el-aside>

    <el-container>


      <el-main id="page">
        <router-view>

        </router-view>
      </el-main>
      <!--    添加新病人-->
      <el-dialog
        title="添加新病人"
        :visible.sync="patientVisible"
        width="40%">
        <el-form :label-position="position" ref="patientForm" :rules="rules" :model="patientForm" label-width="80px">
          <el-form-item label="姓名" prop="name">
            <el-input v-model="patientForm.name"></el-input>
          </el-form-item>
          <el-form-item label="年龄" prop="age">
            <el-input v-model="patientForm.age"  ></el-input>
          </el-form-item>
          <el-form-item label="性别" prop="gender">
            <el-radio-group v-model="patientForm.gender" size="medium">
              <el-radio-button label="male">男</el-radio-button>
              <el-radio-button label="female">女</el-radio-button>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="病情评级" prop="illness_level">
            <el-radio-group v-model="patientForm.illness_level" size="medium">
              <el-radio-button label="1">轻症</el-radio-button>
              <el-radio-button label="2">重症</el-radio-button>
              <el-radio-button label="3">危重症</el-radio-button>
            </el-radio-group>
          </el-form-item>
          <el-form-item>
            <el-button @click="patientVisible=false">取消</el-button>
            <el-button type="primary" @click="addPatient('patientForm')">添加</el-button>
          </el-form-item>
        </el-form>

      </el-dialog>
    </el-container>
  </el-container>
</template>

<script>
export default {
  name: "EmergencyNurse",
  data() {
    return{
      user:this.$store.state.user,
      patientVisible:false,
      patientForm:{
        name:"",
        age:20,
        gender:"female",
        illness_level: 1
      },
      rules: {
        name: [{required: true, message: "请输入姓名", trigger: 'change'}],
        age: [{required: true, message: "请输入年龄", trigger: "blur"},
          // { type: 'number', message: '年龄必须为数字值',trigger: "blur"}
        ]
      },
      position:"left"
    }
  },
  methods:{
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
          this.$router.replace({path: '/'})
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '尚未退出',
            duration:2000
          });
        });
      }
    },
    addPatient(formName){
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.$axios.post('/emergency/addPatient', {
            name:this.patientForm.name,
            illnessLevel:this.patientForm.illness_level
          }).then(res=>{
            console.log("添加单个病人 ",res)
            this.patientVisible = false;

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

    },
    // get_all_patient(){
    //   this.$axios.get('/emergency/allPatient', {
    //   }).then(res=>{
    //     console.log("添加单个病人 ",res)
    //   }).catch(err=>{
    //     console.log(err)
    //   })
    // }
  }
}
</script>

<style scoped>
@import url("https://unpkg.com/element-ui/lib/theme-chalk/index.css");

</style>
