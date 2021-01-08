<template>
  <div id="base_login">
    <el-form :model="loginForm"
             :rules="rules"
             class="login_container"
             label-position="left"
             label-width="0px"
             v-loading="loading">
      <h3 class="login_title">Login</h3>
      <el-form-item prop="username">
        <el-input type="text"
                  auto-complete="on"
                  class="login_input"
                  prefix-icon="el-icon-user-solid"
                  v-model="loginForm.username"
                  autocomplete="on"
                  placeholder="username"></el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input type="password"
                  class="login_input"
                  prefix-icon="el-icon-lock"
                  v-model="loginForm.password"
                  autocomplete="on"
                  auto-complete="on"
                  placeholder="password"></el-input>
      </el-form-item>
      <el-form-item style="width: 100%">
        <el-button type="primary"
                   style="width: 100%;background: #afb4db;line-height: 0.8"
                   v-on:click="login">login
        </el-button>
      </el-form-item>

    </el-form>
  </div>

</template>

<script>
export default {
  name: 'Login',
  data() {

    return {
      loginForm: {
        username: '',
        password: ''
      },
      rules: {
        username: [{required: true, message: '请输入用户名', trigger: 'blur'}],
        password: [{required: true, message: '请输入密码', trigger: 'blur'}]
      },
      loading: false
    }
  },
  methods: {
    login() {
      console.log(this.loginForm.username + "  " + this.loginForm.password)
      this.$axios.post('/login', {
        username: this.loginForm.username,
        password: this.loginForm.password
      })
        .then(resp => {
          console.log("登录返回结果为 ",resp)

          if (resp.status === 200) {
            console.log("res is :" + resp)
            this.$store.commit('login', resp.data.data)
            // var user={id:1,role:0,password:"password",area:1}
            // resp.data.user=user;
            // 当为管理员时跳转到审核界面
            resp.data.data.role=parseInt(resp.data.data.role)
            console.log("resp.data.data.role "+(resp.data.data.role===0),resp.data.data.role)

            if (resp.data.data.role === 0) {
              // console.log(0)
              // 0:doctor; 1:head_nurse; 2:ward_nurse; 3:emergency_nurse
              this.$router.replace({path: '/doctor'})
            } else if (resp.data.data.role === 1) {
              // console.log(1)
              this.$router.replace({path: '/headNurse'})
            } else if (resp.data.data.role === 2) {
              // console.log(2)
              this.$router.replace({path: '/wardNurse'})
            } else if (resp.data.data.role === 3) {
              // console.log(3)
              this.$router.replace({path: '/emergencyNurse'})
            } else {
              // console.log(4)
              this.$router.replace({path: '/'})
            }
          }else{
            this.check_login()
          }
          console.log("end")
          }
        )
        .catch(error => {
          console.log(error)
          this.$notify({
            title: '登录失败',
            message: "请重新检查账号密码"
          });
        })
    },

    check_login() {
      this.$alert('Please ensure the valid username and password!', 'Login error:', {
        confirmButtonText: 'OK',
        callback: action => {
          this.$message({
            type: 'info',
            message: `action: ${action}`
          });
        }
      });
    }
  }
}
</script>

<style scoped>
@import url("https://unpkg.com/element-ui/lib/theme-chalk/index.css");

#base_login {
  background: url("../assets/background/001.jpg") repeat;
  background-position: center;
  height: 100%;
  width: 100%;
  background-size: cover;
  position: fixed;
}

body {
  margin: 0px;
  padding: 0px;
}

.login_container {
  border-radius: 15px;
  background-clip: padding-box;
  margin: 90px auto;
  width: 350px;
  padding: 35px 35px 15px 35px;
  background: #fff;
  border: 1px solid #eaeaea;
  box-shadow: 0 0 25px #cac6c6;
}

.login_title {
  margin: 0px auto 30px auto;
  text-align: center;
  color: #494e8f;
}

input {
  height: 37px;
  margin-top: 10px;
}
</style>
