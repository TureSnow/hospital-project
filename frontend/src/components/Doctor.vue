<template>
  <el-container  style="height: 100%; border: 1px solid #eeeeee">
    <el-aside width="200px" style="background-color: rgb(238, 241, 246);text-align: left;">
      <div style="align-content: center;text-align: center;">
        <img src="../assets/background/photo.jpg" style="border-radius: 50%;width: 60%;padding: 20px 20% 10px 20%">
        <div style="margin-bottom: 20px">Name:{{user.username}}</div>
      </div>
      <el-menu :default-openeds="['1']" >
        <el-submenu index="1" >
          <template slot="title"><i class="el-icon-menu"></i>查看</template>
          <el-menu-item-group >
            <router-link to="/doctor/headNurses" style="text-decoration: none"><el-menu-item index="1-1">本病房护士长 </el-menu-item></router-link>
            <router-link to="/doctor/wardNurses" style="text-decoration: none"><el-menu-item index="1-2" >本病房护士</el-menu-item></router-link>
            <router-link to="/doctor/patients" style="text-decoration: none"><el-menu-item index="1-3" >本病房病人</el-menu-item></router-link>


          </el-menu-item-group>
        </el-submenu>

        <el-menu-item index="2"><template slot="title"><i class="el-icon-message"></i>
          <router-link to="/doctor/messages" style="text-decoration: none">通知</router-link></template>
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
    </el-container>
  </el-container>

</template>

<script>

export default {
  name: "Doctor",
  data:function (){
    return {
      user:this.$store.state.user
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
          this.$router.replace({path:"/"})
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '尚未退出',
            duration:2000
          });
        });
      }
    }
  },

  created() {
  }
}
</script>

<style scoped>
@import url("https://unpkg.com/element-ui/lib/theme-chalk/index.css");
.el-header {
  background-color: #B3C0D1;
  color: #333;
  line-height: 60px;
}
#page{
  /*background: url("../assets/background/001.jpg") no-repeat center center;*/
  /*background-size:100% 100%;*/
}
.el-aside {
  color: #333;
}
</style>
