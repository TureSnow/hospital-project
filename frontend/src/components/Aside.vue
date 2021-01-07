<template>
  <el-aside width="200px" style="overflow:hidden;background-color: rgb(238, 241, 246);position: fixed;height: 100%">
    <div style="align-content: center">
      <img src="../assets/background/photo.jpg" style="border-radius: 50%;width: 60%;padding: 20px 20% 10px 20%">
      <div style="margin-bottom: 20px">Name: {{getUserName()}}</div>
    </div>
    <el-menu style="text-align: left; padding-left:10px" :default-openeds="open_list">
      <el-submenu index="1">
        <template slot="title"><i class="el-icon-menu"></i>会议</template>
        <el-menu-item-group>
          <el-menu-item index="1-1"  @click="changeContent(1)"><i class="el-icon-s-order"></i>所有会议</el-menu-item>
          <el-menu-item index="1-2"  @click="changeContent(2)"><i class="el-icon-s-order"></i>我主持的会议</el-menu-item>
          <el-menu-item index="1-3"  @click="changeContent(3)"><i class="el-icon-s-order"></i>我参与的会议</el-menu-item>
          <el-menu-item index="1-4"  @click="changeContent(4)"><i class="el-icon-s-order"></i>我参与投稿的会议</el-menu-item>
          <el-menu-item index="1-5" >
            <router-link to="conference">
              <i class="el-icon-plus"></i>申请新会议
            </router-link>
          </el-menu-item>
        </el-menu-item-group>

      </el-submenu>
      <el-menu-item index="2"  @click="changeContent(5)"><i class="el-icon-message-solid"></i>通知</el-menu-item>
      <el-menu-item index="3"  @click="changeContent(6)"><i class="el-icon-user"></i>我的邀请</el-menu-item>
    </el-menu>
  </el-aside>
</template>

<script>
export default {
  name: "Aside"
}
</script>

<style scoped>
@import url("https://unpkg.com/element-ui/lib/theme-chalk/index.css");
.router-link-active {
  text-decoration: none;
}
a {
  color:#2c3e50;
  text-decoration: none;
}
</style>
