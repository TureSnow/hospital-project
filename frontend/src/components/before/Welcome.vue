<template>
  <div id="base">
    <!-- 未登录情况下出示主页，目前只有个标题welcome和登录注册 -->
    <div id="hello">
      <h1>Welcome</h1>
      <ul class="container">
        <li>
          <router-link to="register">
            <el-button class="btn_0" type="primary">register</el-button>
          </router-link>
        </li>
        <li>
          <router-link to="login">
            <el-button class="btn_0" type="primary">login</el-button>
          </router-link>
        </li>
      </ul>
    </div>
    <!-- 登录的情况下会显示会议情况 -->
    <div id="user">
      <h1 id="hello_user"></h1>
      <ul class="container">
        <li>
          <router-link to="conference">
            <el-button class="btn_1" type="primary">apply for a conference</el-button>
          </router-link>
        </li>
        <li>
          <router-link to="myConference">
            <el-button class="btn_1" type="primary">my conference</el-button>
          </router-link>
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
  export default {
    name: 'Welcome',
    methods:{
      loginTest(){
        this.$axios.get('/welcome',{
          Authorization: 'Bearer '+localStorage.getItem("token")
        }).then((response) => {
          console.log(response);
          console.log(response.data);
          var k = document.getElementById("user");
          k.style.display = "inline";
          var t = document.getElementById("hello_user");
          t.innerHTML="hello, "+response.data.message;
        }).catch((error) => {
          console.log(error);
          var k = document.getElementById("hello");
          k.style.display = "inline";
        });
      },
    },

    mounted()
    {
      this.loginTest();
    }
  }

</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
  h1{
    margin-top:100px;
    color:#ffffff;
    font-size:280%;
  }
  #hello,#user{
    display:none;
  }
  #base {
    position:fixed;
    width: 100%;
    height: 100%;
    background: url("../assets/background/background.jpg") repeat;
    background-position: center;
    background-size: cover;
  }
  ul {
    width: 350px;
    padding: 35px 35px 35px 35px;
    margin: 90px auto;
    list-style-type: none;
    background: #fff;
    background-clip: padding-box;
    box-shadow: 0 0 25px #cac6c6;
    border: 1px solid #eaeaea;
    border-radius: 15px;
  }
  li {
    margin: 20px;
  }
  .btn_0,.btn_1 {
    width: 60%;
    background: #afb4db;
    border: none;
    border-radius: 10px;
    font-size: 160%
  }
  .btn_1{
    width:100%;
  }
</style>
