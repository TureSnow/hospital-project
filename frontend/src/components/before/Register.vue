<template>
  <div id="base_register">
    <el-form :model="registerForm" :rules="rules" class="register_container" label-position="left"
             label-width="0px" v-loading="loading" :ref="registerForm">
      <h3 class="register_title">Register</h3>
      <el-form-item prop="realUsername">
        <el-input type="text" class="register_input"
                  prefix-icon="el-icon-user"
                  v-model="registerForm.realUsername"
                  auto-complete="off" placeholder="realUsername"></el-input>
      </el-form-item>

      <el-form-item prop="username">
        <el-input type="text" class="register_input"
                  prefix-icon="el-icon-user-solid"
                  v-model="registerForm.username"
                  auto-complete="off" placeholder="username"></el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input type="password" class="register_input"
                  prefix-icon="el-icon-lock"
                  v-model="registerForm.password"
                  auto-complete="off" placeholder="password"></el-input>
      </el-form-item>
      <el-form-item prop="email">
        <el-input type="email" class="register_input"
                  prefix-icon="el-icon-message"
                  v-model="registerForm.email"
                  auto-complete="off" placeholder="email"></el-input>
      </el-form-item>
      <el-form-item prop="department">
        <el-input type="text" class="register_input"
                  prefix-icon="el-icon-office-building"
                  v-model="registerForm.department"
                  auto-complete="off" placeholder="department"></el-input>
      </el-form-item>
      <el-form-item prop="area">
        <el-input type="text" class="register_input"
                  prefix-icon="el-icon-location"
                  v-model="registerForm.area"
                  auto-complete="off" placeholder="area"></el-input>
      </el-form-item>
      <!--<el-form-item prop="usertype">
        <el-radio-group v-model="registerForm.usertype" @change="userTypeChange">
          <el-radio label="Admin" border>Admin</el-radio>
          <el-radio label="Contributor" border>Contributor</el-radio>
          <el-radio label="Reviewer" border>Reviewer</el-radio>
        </el-radio-group>
      </el-form-item>-->
      <el-form-item style="width: 100%">
        <el-button type="primary" style="margin: 10px auto 0px auto;height: 40px;width: 100%;background: #afb4db;"
                   v-on:click="register(registerForm)">register</el-button>
      </el-form-item>

      <el-form-item style="width: 100%;margin: 0px auto">
        <router-link to="Login">
          Already have an account? Login now!
        </router-link>
      </el-form-item>

    </el-form>

  </div>
</template>

<script>
  export default {
    name: 'Register',
    data () {

      const dataValid = (rule, value, callback) => {

        //always false,so don't use it

        /*if(!value || value === '') {
          return callback(new Error('Can\'t be empty'))
        }*/

        return callback()
      }

      //验证username
      const checkusername = (rule, value, callback) => {
        const username_reg=/^[a-zA-Z\-][a-zA-Z0-9_\-]{4,32}$/;

        //always false,so don't use it

        /*if(!value || value === '') {
          return callback(new Error('用户名不能为空！'))
        }else {
          if (!username_reg.test(value)) {
            return callback(new Error('请正确输入用户名！用户名为：5-32个字符，只能包含字母数字或两种特殊字符（_、-）且只能以字母或-开头.'))
          }
          return callback()
        }*/

        if (!username_reg.test(value)) {
          return callback(new Error('请正确输入用户名！用户名为：5-32个字符，只能包含字母数字或两种特殊字符（_、-）且只能以字母或-开头.'))
        }
        return callback()
      }

      //验证password
      const checkpassword = (rule, value, callback) => {
        const password_reg=/(?!^(\d+|[a-zA-Z]+|[_\-]+)$)^[\w_\-]{5,32}$/;

        //always false,so don't use it

        /*if (!value || value === '') {
          return callback(new Error("请输入密码,不能为空!"))
        } else {
          let s=value.toString();
          let e=this.registerForm.username.toString();
          if(s.indexOf(e)!=-1){
            console.log('密码不能包含用户名！');
            return callback(new Error("密码不能包含用户名！密码为：6-32个字符，字母、数字或特殊字符（-、_）至少包含两种,且不能包含账号."))
          }
          if (!password_reg.test(value)) {
            return callback(new Error("请正确输入密码！6-32个字符，字母、数字或特殊字符（-、_）至少包含两种,且不能包含账号."))
          }
          return callback()
        }*/

        let s=value.toString();
        let e=this.registerForm.username.toString();
        if(s.indexOf(e)!=-1){
          console.log('密码不能包含用户名！');
          return callback(new Error("密码不能包含用户名！密码为：6-32个字符，字母、数字或特殊字符（-、_）至少包含两种,且不能包含账号."))
        }
        if (!password_reg.test(value)) {
          return callback(new Error("请正确输入密码！6-32个字符，字母、数字或特殊字符（-、_）至少包含两种,且不能包含账号."))
        }
        return callback()
      }

      //验证email
      const checkemail = (rule, value, callback) => {
        const username_reg=/\w[-\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\.)+[A-Za-z]{2,14}/;

        //always false,so don't use it

        /*if(!value || value === '') {
          return callback(new Error('邮箱不能为空！'))
        }else {
          if (!username_reg.test(value)) {
            return callback(new Error('请正确输入邮箱！email格式应符和雷.汤姆林森创立的标准E-mail格式：用户标识符+@+域名.'))
          }
          return callback()
        }*/

        if (!username_reg.test(value)) {
          return callback(new Error('请正确输入邮箱！email格式应符和雷.汤姆林森创立的标准E-mail格式：用户标识符+@+域名.'))
        }
        return callback()
      }
      return {
        registerForm: {
          realUsername:'',
          username: '',
          password: '',
          email: '',
          department:'',
          area: ''
        },
        rules: {
          // blur 失去鼠标焦点时触发验证
          realUsername: [{required: true, message: '请输入您的真实姓名，其不能为空,如：张三.', trigger: 'blur'}, {validator: dataValid, trigger: 'blur'}],
          username: [{required: true, message: '请设置规范的用户名，5-32个字符，只能包含字母数字或两种特殊字符（_、-）且只能以字母或-开头.', trigger: 'blur'}, {validator: checkusername, trigger: 'blur'}],
          password: [{required: true, message: '请设置安全的密码，6-32个字符，字母、数字或特殊字符（-、_）至少包含两种,且不能包含账号.', trigger: 'blur'}, {validator: checkpassword, trigger: 'blur'}],
          email: [{required: true, message: 'email格式应符和雷.汤姆林森创立的标准E-mail格式：用户标识符+@+域名.', trigger: 'blur'}, {validator: checkemail, trigger: 'blur'}],
          department: [{required: true, message: '请正确输入您的所属单位，如：Fudan University.', trigger: 'blur'}, {validator: dataValid, trigger: 'blur'}],
          area: [{required: true, message: '请正确输入您的所属区域，如：China.', trigger: 'blur'}, {validator: dataValid, trigger: 'blur'}]
        },
        loading: false,
      }
    },


    methods: {
      userTypeChange() {
      },
      register (formName) {
        this.$refs[formName].validate(valid => {
          if(valid){
            this.$axios.post('/register', {
                realUsername:this.registerForm.realUsername,
                username: this.registerForm.username,
                password: this.registerForm.password,
                email: this.registerForm.email,
                department:this.registerForm.department,
                area:this.registerForm.area
              }
            )
              .then(resp => {
                // 根据后端的返回数据修改
                if(resp.status === 200 && resp.data.hasOwnProperty("id")) {
                  // 跳转到login
                  this.success_register();
                  //alert('successful registration')
                  this.$router.replace('/login')
                } else{
                  this.failure_register();
                  //alert('register error')
                }
              })
              .catch(error => {
                console.log(error)
                this.failure_register();
                //alert('register error')
              })
          } else {
            //几种情况
            this.failure_register();
            //alert('wrong submit,please make sure full all conformation.')
          }
        })
      },

      failure_register() {
        this.$alert('Please ensure the valid information committed and please make sure full all conformation!', 'Register error:', {
          confirmButtonText: 'OK',
          callback: action => {
            this.$message({
              type: 'info',
              message: `action: ${ action }`
            });
          }
        });
      },

      success_register() {
        this.$alert('you have registered successfully.', 'Success register:', {
          confirmButtonText: 'OK',
          callback: action => {
            this.$message({
              type: 'info',
              message: `action: ${ action }`
            });
          }
        });
      }
    }
  }
</script>

<style scoped>
  @import url("https://unpkg.com/element-ui/lib/theme-chalk/index.css");
  #base_register{
    background: url("../assets/background/background.jpg") repeat;
    background-position: center;
    height: 100%;
    width: 100%;
    background-size: cover;
    /*position: fixed;*/
    overflow: auto;
  }
  .register_container{
    border-radius: 15px;
    background-clip: padding-box;
    margin: 65px auto;
    width: 350px;
    padding: 35px 35px 15px 35px;
    background: #fff;
    border: 1px solid #eaeaea;
    box-shadow: 0 0 25px #cac6c6;
  }
  .register_title{
    margin: 0px auto 30px auto;
    text-align: center;
    color: #505458;
  }

  /deep/ input{
    height:37px;
    margin-top: 10px;
  }

</style>
