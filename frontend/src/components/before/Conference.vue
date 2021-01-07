<template>
  <div id="base_conference">
    <el-form :model="conferenceForm" :rules="rules" class="conference_container" label-position="left"
             label-width="0px" v-loading="loading" :ref="conferenceForm">
      <h3 class="conference_title">Conference</h3>
      <el-form-item prop="shortname">
        <el-input type="text" class="conference_input"
                  prefix-icon="el-icon-user"
                  v-model="conferenceForm.shortname"
                  auto-complete="off" placeholder="ShortName"></el-input>
      </el-form-item>
      <el-form-item prop="fullName">
        <el-input type="text" class="conference_input"
                  prefix-icon="el-icon-user-solid"
                  v-model="conferenceForm.fullName"
                  auto-complete="off" placeholder="FullName"></el-input>
      </el-form-item>

      <el-form-item prop="topic" style="text-align: left">
        <el-button size="small" @click="add()">+ Add New Topic</el-button>
        <div v-for="(element,i) of conferenceForm.topic" :key="i">
          <!--:placeholder=element.value-->
          <el-input style="width: 50%" placeholder="Topic" v-model="conferenceForm.topic[i]" required="required"></el-input>
          <el-button size="medium" type="danger" icon="el-icon-delete" plain @click="sub(element)"></el-button>
        </div>
      </el-form-item>

      <el-form-item prop="startTime">
        <el-date-picker
          format="yyyy.MM.dd" value-format="yyyy.MM.dd"
          v-model="conferenceForm.startTime"
          :picker-options="pickerOptions0"
          auto-complete="off"
          type="daterange"
          range-separator="to"
          start-placeholder="startTime"
          end-placeholder="endTime"
          style="width: 100%;margin-top: 10px"></el-date-picker>
      </el-form-item>


      <el-form-item prop="address">
        <el-input type="text" class="conference_input"
                  prefix-icon="el-icon-location"
                  v-model="conferenceForm.address"
                  auto-complete="off" placeholder="Address"></el-input>
      </el-form-item>

      <el-form-item prop="ddl">
        <el-date-picker type="date"
                        format="yyyy.MM.dd" value-format="yyyy.MM.dd"
                        :picker-options="pickerOptions1"
                        placeholder="Deadline for submission" auto-complete="off"
                        v-model="conferenceForm.ddl" style="width: 100%"></el-date-picker>
      </el-form-item>

      <el-form-item prop="releaseTime">
        <el-date-picker type="date"
                        format="yyyy.MM.dd" value-format="yyyy.MM.dd"
                        :picker-options="pickerOptions2"
                        placeholder="ReleaseTime" auto-complete="off"
                        v-model="conferenceForm.releaseTime" style="width: 100%"></el-date-picker>
      </el-form-item>

      <el-form-item style="width: 100%">
        <el-button type="primary" style="margin: 10px auto 0px auto;height: 40px;width: 100%;background: #afb4db;"
                   v-on:click="conference(conferenceForm)">Submit</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
  export default {
    name: "Conference",

    data () {
      const dataValid = (rule, value, callback) => {
        return callback()
      }
      return {
        conferenceForm: {
          shortname: '',
          fullName: '',
          topic:[""],
          startTime: '',
          address:'',
          ddl: '',
          releaseTime:''
        },
        rules: {
          // blur 失去鼠标焦点时触发验证
          topic:[{required: true, message: '请输入会议的topic，如“Environment protection.”.', trigger: 'blur'}, {validator: dataValid, trigger: 'blur'}],
          shortname: [{required: true, message: '请填入会议简称，如“ICS 2020”.', trigger: 'blur'}, {validator: dataValid, trigger: 'blur'}],
          fullName: [{required: true, message: '请填入申请的会议全称，（如“The 42nd International Conference on Software Engineering”）.', trigger: 'blur'}, {validator: dataValid, trigger: 'blur'}],
          startTime: [{required: true, message: '请填入会议举办时间，（如“October 5-11, 2020”）.', trigger: 'blur'}, {validator: dataValid, trigger: 'blur'}],
          address: [{required: true, message: '请填入会议举办地点，（如“ Seoul, South Korea”）.', trigger: 'blur'}, {validator: dataValid, trigger: 'blur'}],
          ddl: [{required: true, message: '请输入截止日期.', trigger: 'blur'}, {validator: dataValid, trigger: 'blur'}],
          releaseTime: [{required: true, message: '请输入发布日期.', trigger: 'blur'}, {validator: dataValid, trigger: 'blur'}]
        },
        loading: false,

        //Control time(time logic)
        //start time
        //Date.now() - 8.64e7为当前时间，包括当天
        //数组【startTime,endTime】
        pickerOptions0: {
          disabledDate: (time) => {
            /*if (this.conferenceForm.ddl != "") {
              return time.getTime() < (Date.now() - 8.64e7) || time.getTime() > this.conferenceForm.ddl;
              console.log("i love",this.conferenceForm.startTime);
            } else {
              return time.getTime() < Date.now() - 8.64e7;
              console.log("i love",this.conferenceForm.startTime);
            }*/

            return time.getTime() < Date.now() - 8.64e7;
          }
        },

        //ddl
        pickerOptions1: {
          disabledDate: (time) => {
            if(this.conferenceForm.startTime==""){
              return true;
            }else {
              //console.log("i love it.",this.conferenceForm.startTime);
              let startDate = new Date(this.conferenceForm.startTime[0]).getTime();
              let endDate = new Date(this.conferenceForm.startTime[1]).getTime();

              return time.getTime() > endDate || time.getTime() < startDate;
            }
          }
        },

        //releaseTime
        pickerOptions2: {
          disabledDate: (time) => {
            if(this.conferenceForm.startTime==""||this.conferenceForm.ddl==""){
              return true;
            }else {
              let startDate = new Date(this.conferenceForm.ddl).getTime();
              let endDate = new Date(this.conferenceForm.startTime[1]).getTime();
              //let endDate = new Date(this.conferenceForm.startTime[1]).getTime() + 8.64e7;

              return time.getTime() > endDate || time.getTime() < startDate;
              //return time.getTime() < endDate ;
            }
          }
        },

      }
    },

    methods: {
      userTypeChange() {
      },
      conference (formName) {
        //judge topic is full of information
        let length=this.conferenceForm.topic.length;
        for (let i=0;i<length;i++){
          if (this.conferenceForm.topic[i]=="") {
            this.$alert('Attention please,the topics can not be empty!', 'Topic alert:', {
              confirmButtonText: 'OK',
              callback: action => {
                this.$message({
                  type: 'info',
                  message: `action: ${ action }`
                });
              }
            });
            return ;
          }
        }

        this.$refs[formName].validate(valid => {
          if(valid){
            this.$axios.post('/conference', {
                shortname: this.conferenceForm.shortname,
                fullName: this.conferenceForm.fullName,
                startTime: this.conferenceForm.startTime,
                topics:this.conferenceForm.topic,
                address:this.conferenceForm.address,
                ddl:this.conferenceForm.ddl,
                releaseTime:this.conferenceForm.releaseTime,
                token:localStorage.getItem("token")
              }
            )
              .then(resp => {
                // 根据后端的返回数据修改
                if(resp.status === 200 && resp.data.hasOwnProperty("shortname")) {
                  // 跳转到login
                  this.success_conference();
                  //alert('successful submit')
                  this.$router.replace('/')
                } else{
                  console.log("jjjjjjjjj")
                  this.failure_conference();
                  //alert('this is  error')
                }
              })
              .catch(error => {
                console.log(error)
                this.failure_conference();
                //alert('conference error')
              })
          } else {
            console.log("hhhhhhhhhhh")
            this.failure_conference();
            //alert('wrong submit')
          }
        })
      },

      add(){
        this.conferenceForm.topic.push("");
      },

      sub(el){
        if (this.conferenceForm.topic.length == 1) {
          this.$alert('You should fill in at least one topic!', 'Topic alert:', {
            confirmButtonText: 'OK',
            callback: action => {
              this.$message({
                type: 'info',
                message: `action: ${ action }`
              });
            }
          });
          return ;
        }
        console.log(el);
        let index = this.conferenceForm.topic.findIndex(element =>{
          if(element==el){
            return true
          }
        })
        this.conferenceForm.topic.splice(index,1);
        console.log(this.conferenceForm.topic)
      },

      failure_conference() {
        this.$alert('Please ensure the valid information committed and please make sure full all conformation!', 'Conference error:', {
          confirmButtonText: 'OK',
          callback: action => {
            this.$message({
              type: 'info',
              message: `action: ${ action }`
            });
          }
        });
      },

      success_conference() {
        this.$alert('you have applied the conference successfully.', 'Conference register:', {
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
  #base_conference{
    background: url("../assets/background/background.jpg") repeat;
    background-position: center;
    height: 100%;
    width: 100%;
    background-size: cover;
    /*position: fixed;*/
    overflow: auto;
  }
  .conference_container{
    border-radius: 15px;
    background-clip: padding-box;
    margin: 65px auto;
    width: 350px;
    padding: 35px 35px 15px 35px;
    background: #fff;
    border: 1px solid #eaeaea;
    box-shadow: 0 0 25px #cac6c6;
  }
  .conference_title{
    margin: 0px auto 30px auto;
    text-align: center;
    color: #505458;
  }
  /deep/ input{
    height:37px;
    margin-top: 10px;
  }
</style>
