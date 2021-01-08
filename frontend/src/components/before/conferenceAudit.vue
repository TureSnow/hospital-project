<template>
  <div id="app">
    <el-container style="height: 100%; border: 1px solid #eee">
      <el-aside width="200px" style="background-color: rgb(238, 241, 246)">
        <div style="align-content: center">
          <img src="../assets/background/photo.jpg" style="border-radius: 50%;width: 60%;padding: 20px 20% 10px 20%">
          <!--<span>卡片名称</span>-->
          <div style="margin-bottom: 20px">Name: {{getUserName()}}</div>
        </div>
        <!--实现导航栏的默认开关-->
        <el-menu ><!--:default-openeds="['1','3']"-->
          <el-submenu index="1">
            <template slot="title"><i class="el-icon-menu"></i>待审核会议</template>
            <el-menu-item-group>
              <!--<el-menu-item index="1-1">Conference1</el-menu-item>
              <el-menu-item index="1-2">Conference2</el-menu-item>
              <el-menu-item index="1-3">Conference3</el-menu-item>-->
            </el-menu-item-group>

          </el-submenu>
          <el-submenu index="2">
            <template slot="title"><i class="el-icon-menu"></i>已经审核的会议</template>
            <el-menu-item-group>

              <!--<el-menu-item index="2-1">Conference1</el-menu-item>
              <el-menu-item index="2-2">Conference2</el-menu-item>
              <el-menu-item index="2-3">Conference3</el-menu-item>-->
            </el-menu-item-group>
          </el-submenu>
        </el-menu>
      </el-aside>

      <el-container>
        <el-header style="text-align: right; font-size: 12px;background-color: #B3C0D1;
                    color: #333;line-height: 60px;padding:0 50px">
          <el-dropdown>
            <i class="el-icon-setting" style="margin-right: 15px;cursor: pointer;">关于此账户</i>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item >
                <el-button @click="checkLogOut();">
                  注销此账户（登出）
                </el-button>
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </el-header>

        <el-main>

          <el-row :gutter="12">
            <el-col :span="8" id="conferenceList_needAudit" v-for="cashLists in cashList" :key="cashLists.id">
              <el-card class="box-card" style="text-align: left;margin: 5px 0px">
                <div slot="header" class="clearfix">
                  <span>Conference information:</span>
                </div>
                <div class="text item">
                  <el-card class="box-card" style="text-align: left;margin: 5px 0px">
                    <div class="text item">
                      <p>ShortName:{{cashLists.shortname}}<p>
                      <p>FullName: {{cashLists.fullname}}</p>
                      <p>StartTime:{{cashLists.starttime}}</p>
                      <p>EndTime:{{cashLists.endtime}}</p>
                      <p>Address:{{cashLists.address}}</p>
                      <p>Deadline for submission: {{cashLists.ddl}}</p>
                      <p>ReleaseTime:{{cashLists.releaseTime}}</p>
                    </div>
                  </el-card>
                </div>
                <div  class="clearfix">
                  <div style="text-align: right; margin: 0">
                    <el-button v-if="cashLists.condition==0" type="primary" size="mini" @click="auditFailed(cashLists.condition,cashLists.meetingId)">审核不通过</el-button>
                    <el-button v-if="cashLists.condition==0" type="primary" size="mini" @click="auditPass(cashLists.condition,cashLists.meetingId)">审核通过</el-button>
                    <el-button v-if="cashLists.condition==4" type="primary" size="mini" @click="visible = false">会议已经结束</el-button>
                    <el-button v-if="((cashLists.condition==1)||(cashLists.condition==2)||(cashLists.condition==3))" type="primary" size="mini" @click="visible = false">审核已经通过</el-button>
                    <el-button v-if="cashLists.condition==5" type="primary" size="mini" @click="visible = false" style="color: red">审核没有通过</el-button>
                    </div>
                </div>
              </el-card>
            </el-col>

          </el-row>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>


<script>
  export default {
    name:"conferenceAudit",
    data() {
      return{
        name:"",
        visible:false,


        pageSize:10,//每页条数
        allCount:0,//记录总数
        currentPage:1,//当前页码

        //condition->审核状态：0：待审核，1：已通过，2：可投稿，3、审稿中，4、已结束,5、审核不通过
        //condition:'',

        cashList: [],   //列表数组(现在是准备请求接口，不需要模拟的数据，所以设置一个空数组)
        topicList:[],



      }
    },

    methods:{
      getConferencesInformation(){
        this.$axios.post('/allMeeting',{
          token: localStorage.getItem("token")
        }).then((response) => {
          console.log("dsjkdfkv",response);
          // 添加会议列表

          //console.log("zzzzzzz",response.data);
          //如果请求成功了，这接口code为0代表请求成功。具体怎样判断还需要看接口
          if(response.status==200){
            //设置列表数据
            this.cashList = response.data;
            //console.log("jhsvalskvg",this.cashList);

          }

          //

        }).catch((error) => {
          console.log(error);

          this.conferenceAuditQuitWrong();
          //alert("conferenceList_needAudit return error!");
        });
      },

      auditPass(condition,meetingId){
        condition=1;
        this.$axios.post('/verify/'+meetingId+"/"+condition,{
          token: localStorage.getItem("token")
        }).then((response) => {
          console.log("dsjkdfkv",response);

          //console.log("zzzzzzz",response.data);
          //如果请求成功了，这接口code为0代表请求成功。具体怎样判断还需要看接口
          if(response.status==200){
            //设置列表数据

            this.success_conferenceAudit();
            //alert("Pass successfully!");

          }
          this.getConferencesInformation();
          //window.location.reload();

        }).catch((error) => {
          console.log(error);

          this.conferenceAuditWrong();
          //alert("Pass wrong!!");
        });

      },

      auditFailed(condition,meetingId){
        condition=5;
        this.$axios.post('/verify/'+meetingId+'/'+condition,{
          token: localStorage.getItem("token")
        }).then((response) => {
          console.log(condition)
          console.log("dsjkdfkv",response);

          //console.log("zzzzzzz",response.data);
          //如果请求成功了，这接口code为0代表请求成功。具体怎样判断还需要看接口
          if(response.status==200){
            //设置列表数据

            this.failure_conferenceAudit();
            //alert("Pass Failed!");

          }

          this.getConferencesInformation();
          //window.location.reload();
        }).catch((error) => {
          console.log(error);

          this.conferenceAuditWrong();
          //alert("Pass failed op wrong!!");
        });
      },



      failure_conferenceAudit() {
        this.$alert('You have failed the conference successfully!', 'Audit the conference:', {
          confirmButtonText: 'OK',
          callback: action => {
            this.$message({
              type: 'info',
              message: `action: ${ action }`
            });
          }
        });
      },

      success_conferenceAudit() {
        this.$alert('You have passed the conference successfully.', 'Audit the conference:', {
          confirmButtonText: 'OK',
          callback: action => {
            this.$message({
              type: 'info',
              message: `action: ${ action }`
            });
          }
        });
      },

      conferenceAuditWrong() {
        this.$alert('Sorry,audit the conference failed.', 'Audit the conference error:', {
          confirmButtonText: 'OK',
          callback: action => {
            this.$message({
              type: 'info',
              message: `action: ${ action }`
            });
          }
        });
      },

      conferenceAuditQuitWrong() {
        this.$alert('Sorry,conferenceList_needAudit return error!.', 'Audit the conference return error:', {
          confirmButtonText: 'OK',
          callback: action => {
            this.$message({
              type: 'info',
              message: `action: ${ action }`
            });
          }
        });
      },


      //注销账号 好像没用？？？
      logoutAccount() {
        // 移除token
        localStorage.removeItem('token')
        localStorage.removeItem('userDetails')
        this.$router.replace({path: '/Login'})
      },

      checkLogOut()
      {
        this.$alert('Are you sure to logout ?', 'Confirm', {
          confirmButtonText: 'OK',
          callback: action => {
            this.$message({
              type: 'info',
              message: `action: ${ action }`
            });
            if(action == 'confirm')this.logoutAccount();
          }
        });
      },

      getUserName()
      {
        if(localStorage.getItem('userDetails') == undefined) return "null";
        else return JSON.parse(localStorage.getItem('userDetails')).username;
      }
    },


    //metheds  函数提前定义（ 类似提前声明变量 进入页面内容全部渲染完成后自动引函数）
    mounted()
    {
      this.getConferencesInformation();
    },

  }
</script>
<style>
  @import url("https://unpkg.com/element-ui/lib/theme-chalk/index.css");

  .text {
    font-size: 14px;
  }
  .item {
    margin-bottom: 18px;
  }
  .clearfix:before,
  .clearfix:after {
    display: table;
    content: "";
  }
</style>
