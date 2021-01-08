<template>
  <div>

    <el-table
      :data="ward_nurse_list"
      style="width: 100%">

      <el-table-column
        label="姓名"
        prop="name">
      </el-table-column>
      <el-table-column
        label="区域"
        prop="area">
      </el-table-column>
      <el-table-column
        align="right">
        <template slot="header" slot-scope="scope">

        </template>

        <template slot-scope="scope">
          <el-button
            size="mini"
            @click="handleMyPatient(scope.$index)">查看负责的病人
          </el-button>
          <el-button v-if="user.role===1"
                     size="mini"
                     type="danger"
                     @click="handleDelete(scope.$index)">删除
          </el-button>
        </template>


      </el-table-column>
    </el-table>
    <div v-if="user.role===1" style="position: fixed;bottom: 30px;right:50px;">
      <!--      el-icon-circle-plus-outline-->
      <el-button type="success" icon="el-icon-plus" circle @click="nurseVisible=true"></el-button>
    </div>
    <!--    查看负责的病人信息-->
    <el-dialog
      title="负责的病人"
      :visible.sync="patientVisible"
      width="60%">
      <div>
        <el-table
          :data="myPatients"
          stripe
          style="width: 100%">
          <el-table-column
            prop="name"
            label="姓名"
            width="180">
          </el-table-column>
          <!--          <el-table-column-->
          <!--            prop="age"-->
          <!--            label="年龄">-->
          <!--          </el-table-column>-->

          <!--          <el-table-column-->
          <!--            prop="gender"-->
          <!--            label="性别">-->
          <!--          </el-table-column>-->
          <el-table-column
            prop="illness_level"
            label="病情评级">
          </el-table-column>
        </el-table>
      </div>

    </el-dialog>

    <!--    添加病房护士-->

    <el-dialog
      title="添加病房护士"
      :visible.sync="nurseVisible"
      width="50%">
      <el-form ref="nurseForm" :rules="rules" :model="nurseForm" label-width="80px">
        <!--        <el-form-item label="工号">-->
        <!--          <el-input v-model="nurseForm.id"></el-input>-->
        <!--        </el-form-item>-->
        <el-form-item label="账号名">
          <el-input v-model="nurseForm.name"></el-input>
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="nurseForm.password"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button @click="nurseVisible = false">取消</el-button>
          <el-button type="primary" @click="addNurse('nurseForm')">添加</el-button>
        </el-form-item>
      </el-form>

    </el-dialog>

  </div>
</template>

<script>
export default {
  name: "WardNurseCard",
  data() {
    return {
      user: this.$store.state.user,
      ward_nurse_list: [],
      //查看负责的病人
      patientVisible: false,
      myPatients: [],
      //添加病床护士
      nurseVisible: false,
      nurseForm: {
        name: "",
        password: "",

        area: this.$store.state.user.area,
        role: 2,

      },
      rules: {
        name: [{required: true, message: "姓名不能为空", trigger: 'blur'}],
        password: [{required: true, message: "密码不能为空", trigger: "blur"}],
      },
    }
  },
  methods: {
    handleDelete(index, row) {
      console.log(index)
      this.$axios.post('/head/deleteNurse', {
        wardNurseId: this.ward_nurse_list[index].id
      }).then(res => {
        console.log(" ", res)
        this.$notify({
          title: 'result',
          message: res.data.data
        });
        this.get_all_ward_nurse()
      }).catch(err => {
        console.log(err)
      })
    },
    //查看护士负责的病人
    handleMyPatient(index, row) {
      console.log("", this.myPatients[index])
      this.$axios.post('/user/doctor/ward2patients', {
        wardNurseId: this.ward_nurse_list[index].id
      }).then(res => {
        console.log(res)
        this.myPatients = res.data.data
        this.myPatients.forEach(item =>
          // console.log(this.getAreaLevel())
          item.illness_level = this.$myUtil.myFunc.getIllness_level(parseInt(item.illnessLevel))
        )
        console.log("初始化护士长为", this.ward_nurse_list)
      }).catch(err => {
        console.log(err)
      })
      this.patientVisible = true
    },
    addNurse(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          // wardNurseId:
          console.log("nurseForm ",this.nurseForm)
          this.$axios.post('/register', {
            id: 0,
            name: this.nurseForm.name,
            password: this.nurseForm.password,
            role: this.nurseForm.role,
            area: this.nurseForm.area
          }).then(res => {
            console.log(" 注册成功", res.data)
            //返回user实例

            if (res.data.message.indexOf("name ")!==0) {
              this.$axios.post('/head/addNurse', {
                wardNurseName: this.nurseForm.name
              }).then(res2 => {
                console.log(" ", res2)
                if (res2.data.data.indexOf("add ok! now ") === 0) {
                  this.$notify({
                    title: '成功',
                    message: '添加成功',
                    type: 'success'
                  });
                  this.nurseVisible=false
                  this.get_all_ward_nurse()
                } else {
                  this.$notify({
                    title: '失败',
                    message: '该员工尚未完全空闲',
                    type: 'error'
                  });
                }
              }).catch(err => {
                console.log(err)
              })
            }else{
              console.log("res.data.data.id ==null ")
                this.$notify({
                  title: '失败',
                  message: '该员工已被注册',
                  type:'error'
                })
            }


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
    },
    get_all_ward_nurse(){
      this.$axios.get('/user/doctor/ward', {}).then(res => {
        console.log(res)
        this.ward_nurse_list = res.data.data
        this.ward_nurse_list.forEach(item =>{
          item.area_num=parseInt(item.area)
          item.area = this.$myUtil.myFunc.getAreaLevel(parseInt(item.area))
        }

        )
        console.log("初始化病房护士为", this.ward_nurse_list)
      }).catch(err => {
        console.log(err)
      })
    }
  },
  created() {
    this.get_all_ward_nurse()

  }
}
</script>

<style scoped>
@import url("https://unpkg.com/element-ui/lib/theme-chalk/index.css");
</style>
