<template>
  <div id="base">
    <div class="container">
      <div id="pending">
        <!-- 待审核会议 -->
        <h1>pending</h1>
        <ul id ="pending_list">
        </ul>
      </div>
      <div id="approved">
        <!-- 已审核会议（这次里面什么都没有 -->
        <h1>approved</h1>
        <ul id ="approved_list">
        </ul>
      </div>
    </div>
  </div>
</template>

<script>
  export default {
    name: 'MyConference',
    methods:{
      getConferences(){
        this.$axios.post('/findAllmyMeeting',{
          token: localStorage.getItem("token")
        }).then((response) => {
          console.log(response);
          console.log(response.data);
          // 添加会议内容
          var length = response.data.length;
          var pendingList = document.getElementById("pending_list")
          for(var i = 0 ; i<=length-1 ; i++)
          {
            var content=document.createElement('li')
            content.innerHTML = response.data[i].fullname;
            pendingList.appendChild(content);
          }
        }).catch((error) => {
          console.log(error);
          this.failure_return();
          //alert("error");
        });
      },


      failure_return() {
        this.$alert('Response error!', 'Conference error:', {
          confirmButtonText: 'OK',
          callback: action => {
            this.$message({
              type: 'info',
              message: `action: ${ action }`
            });
          }
        });
      },
    },

    mounted()
    {
      this.getConferences(); 
    }
  }

</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
h1{
    margin-top:70px;
    color:#ffffff;
    font-size:180%;
  }
.container{
  width:70%;
  margin:auto;
}
#pending{
  float:left;
}
#approved{
  float:right;
}
#base {
  position: fixed;
  width: 100%;
  height: 100%;
  padding-top: 0px;
  background: url("../assets/background/background.jpg") repeat;
  background-position: center;
  background-size: cover;
}
ul {
  max-height:300px;
  overflow:auto;
  width: 350px;
  padding: 35px 35px 35px 35px;
  margin: 20px auto;
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
</style>
