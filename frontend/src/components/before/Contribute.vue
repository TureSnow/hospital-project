<template>
  <div id="base_contribute">
    <el-form class="contribute_container" :model="contributeForm" :rules="rules" label-width="80px" label-position="left">
      <el-form-item label="FileTitle" prop="title">
        <el-input placeholder="Please input the summary."
                  maxlength="50"
                  show-word-limit
                  v-model="contributeForm.title"></el-input>
      </el-form-item>
      <!--<div style="margin: 25px auto;line-height: 1">
        <span style="color: red">*</span>
        <label>Title:</label>
        <input type="text"
               required
               maxlength="50"
               style="width: 82%;border-radius: 5px;border: 2px solid darkgray"
               value="" v-model="title" placeholder="title:50个字符字符以内.">
      </div>-->

      <el-form-item label="Summary" prop="summary">
        <el-input type="textarea"
                  placeholder="Please input the summary."
                  maxlength="800"
                  show-word-limit
                  v-model="contributeForm.summary"></el-input>
      </el-form-item>
      <!--<div style="margin: 25px auto;line-height: 1">
        <span style="color: red">*</span>
        <label>Summary:</label>
        <textarea  v-model="summary" placeholder="summary:800个字符字符以内."
                   maxlength="800"
                   required
                   style="width: 70%;border-radius: 5px;border: 2px solid darkgray">

        </textarea>
        &lt;!&ndash;<input type="text"
               style="width: 70%;border-radius: 5px;border: 2px solid darkgray"
               value="" v-model="summary" placeholder="summary:800个字符字符以内.">&ndash;&gt;
      </div>-->

      <!--topicList show-->
      <el-form-item label="TopicList" prop="topics" >
        <el-checkbox-group v-model="contributeForm.topics" v-for="element in topicList" :key="element.id" style="text-align: left">
          <el-checkbox :label="element" name="type"></el-checkbox>
        </el-checkbox-group>
      </el-form-item>
      <!--Author Information-->
      <el-form-item style="text-align: left">
        <el-button size="small" @click="add()">+ Add An Author</el-button>
        <draggable v-model="contributeForm.authorList" @update="datadragEnd" v-bind= "{animation:1000}">
          <transition-group>
            <el-form-item v-for="element in contributeForm.authorList" :key="element.contributionid" class = "drag-item">
              <el-input placeholder="Author's name" style="width: 82%;margin-bottom: 10px" v-model="element.name"></el-input>
              <el-input placeholder="Author's country" style="width: 82%;margin-bottom: 10px" v-model="element.country"></el-input>
              <el-input placeholder="Author's department" style="width: 82%;margin-bottom: 10px" v-model="element.company"></el-input>
              <el-input placeholder="Author's email" style="width: 82%;margin-bottom: 10px" v-model="element.email"></el-input>
              <el-button type="danger" icon="el-icon-delete" plain @click="sub(element)"></el-button>
            </el-form-item>
          </transition-group>
        </draggable>
      </el-form-item>

      <el-form-item style="text-align: left">
        <span style="color: red">*</span>
        <label style="width: 100%">Attention:you can only upload one PDF file every time.</label>
        <input style="border: 2px solid gainsboro;border-radius: 5px;width: 79%" type="file" accept="application/pdf" @change="getFile($event)">
      </el-form-item>
      <div slot="tip" class="el-upload__tip">Attention:you can only upload PDF files.</div>

      <el-form-item style="width: 100%">
        <el-button type="primary" style="margin: 10px auto 0px auto;height: 40px;width: 100%;background: #afb4db;"
                   v-on:click="submitForm($event)">Submit</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>
<script>
  import draggable from 'vuedraggable'
  export default {
    data() {
      const dataValid = (rule, value, callback) => {
        return callback()
      }
      return {
        //Url:"/contribute/"+localStorage.getItem('meetingID'),


        topicList:this.$route.params.topic,


        contributeForm: {
          title: '',
          summary: '',
          file:'',

          /*topicSet:[],*/
          topics:[],
          authorList: [
            {contributionid:'0',name:'',country:'',company:'',email:''},
          ],
        },
        rules: {
          title: [
            { required: true, message: '请输入论文的标题，长度在50个字符字符以内.', trigger: 'blur' },
            { max: 50, message: '长度在50个字符字符以内.', trigger: 'blur' },
            {validator:dataValid,trigger:'blur'}
          ],
          summary: [
            { required: true, message: '请填写论文摘要，800个字符字符以内.', trigger: 'blur' },
            { max: 800, message: '800个字符字符以内.', trigger: 'blur' },
            {validator:dataValid,trigger:'blur'}
          ],
          topics: [
            { type: 'array', required: true, message: '请至少选择一个topic', trigger: 'change' }
          ],
        }

      };
    },

    components: {
      draggable
    },
    methods: {
      datadragEnd (evt) {
        evt.preventDefault();
        console.log('拖动前的索引 :' + evt.oldIndex)
        console.log('拖动后的索引 :' + evt.newIndex)
        console.log(this.contributeForm.authorList);
      },

      add(){
        this.contributeForm.authorList.push({contributionid:'0',name:'',country:'',company:'',email:''});
      },

      sub(id){
        //测试
        console.log(this.contributeForm.topics);
        console.log(this.topicList[0]);
        //console.log(localStorage.getItem('topicList'));

        if (this.contributeForm.authorList.length == 1) {
          this.$alert('You should fill in at least one author\'s information!', 'AuthorList alert:', {
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
        console.log(id)
        let index = this.contributeForm.authorList.findIndex(element =>{
          if(element==id){
            return true
          }
        })
        console.log(index)
        //console.log(this.constributeForm.authorList.indexOf(index))
        this.contributeForm.authorList.splice(index,1)
        console.log(this.contributeForm.authorList)
      },

      success_contribute() {
        this.$alert('you have committed successfully.', 'Success commit:', {
          confirmButtonText: 'OK',
          callback: action => {
            this.$message({
              type: 'info',
              message: `action: ${ action }`
            });
          }
        });
      },

      getFile(event) {
        this.contributeForm.file = event.target.files[0];
        console.log("获取当前文件",this.contributeForm.file);
      },
      submitForm(event) {
        //checkAuthor's information
        let length=this.contributeForm.authorList.length;
        console.log(length);
        console.log(this.contributeForm.authorList);
        for (let i=0;i<length;i++){
          if ((this.contributeForm.authorList[i].contributionid == '')||(this.contributeForm.authorList[i].name == '')||(this.contributeForm.authorList[i].company == '')||(this.contributeForm.authorList[i].country == '')||(this.contributeForm.authorList[i].email == '')) {
            this.$alert('Attention please,the Author\'s information can not be empty!', 'Author alert:', {
              confirmButtonText: 'OK',
              callback: action => {
                this.$message({
                  type: 'info',
                  message: `action: ${ action }`
                });
              }
            });

            return ;

          } else { //checkEmail
            const username_reg=/\w[-\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\.)+[A-Za-z]{2,14}/;
            for (let i = 0; i < length; i++) {
              if (!username_reg.test(this.contributeForm.authorList[i].email)) {
                this.$alert('Attention please,the Author\'s email is not satisfied with the request! An example:123@qq.com. As：UserIdentifier+@+DomainName.', 'Email alert:', {
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
            //nothing illegal
          }
        }

        //checkTopicList
        let topicLength=this.contributeForm.topics.length;
        if (topicLength==0) {
          this.$alert('Attention please,you need select the topic!', 'Topic alert:', {
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

        //如果不空
        if (!(this.contributeForm.summary==''||this.contributeForm.file.name==null||this.contributeForm.title=='')){
          event.preventDefault();
          let formData = new FormData();
          formData.append('title', this.contributeForm.title);
          formData.append('summary', this.contributeForm.summary);
          formData.append('file', this.contributeForm.file);

          let config = {
            headers: {
              'Content-Type': 'multipart/form-data'
            }
          }
          this.$axios.post('/contribute/'+this.$route.params.id, formData, config)
            .then(resp=>{
              //成功返回
              if (resp.status===200){
                //返回用户界面
                console.log(resp.data);




                //第一次请求成功后发第二次请求
                //change the contributionId
                //let length=this.contributeForm.authorList.length;
                for (let i = 0; i < length; i++) {
                  this.contributeForm.authorList[i].contributionid=resp.data.contributionId;
                }

                /*//change the topicSet
                for (let i = 0; i < topicLength; i++) {
                  this.contributeForm.topicSet.push({contributionid:resp.data.contributionId,topic:this.contributeForm.topics[i]})
                }*/

                this.$axios.post('/contribute2/'+resp.data.contributionId, {
                  authors:this.contributeForm.authorList,
                  topics:this.contributeForm.topics
                  }
                )
                  .then(resp => {
                    // 根据后端的返回数据修改
                    if(resp.status === 200) {
                      //成功返回时跳转

                      //清楚缓存
                      /*localStorage.removeItem('meetingID');
                      localStorage.removeItem('topicList');*/
                      this.success_contribute();
                      this.$router.replace('/')
                    } else{
                      this.failure_contribute();
                      //alert('register error')
                    }
                  })
                  .catch(error => {
                    console.log(error)
                    this.failure_contribute();
                    //alert('register error')
                  })

                //alert('成功！')
              } else {
                this.failure_contribute();
                //alert("失败！")
              }
            }).catch(error=>{
            console.log(error);
          })
        }else {
          //空
          //alert("ai love you")
          this.empty_contribute();
        }

      },

      //得到url
      /*getUrl(){
        return "/contribute/"+localStorage.getItem('meetingID');
      },*/

      handleRemove(file) {
        console.log(file);
      },
      handlePreview(file) {
        console.log(file);
      },
      handleExceed(files) {
        this.$message.warning(`当前限制选择 1 个文件.`);
      },
      beforeRemove(file) {
        return this.$confirm(`确定移除 ${ file.name }？`);
      },

      //上传？？？
      submitUpload() {
        this.$refs.upload.submit();
      },

      failure_contribute() {
        this.$alert('Please ensure the valid information committed and please make sure full all conformation!', 'Commit error:', {
          confirmButtonText: 'OK',
          callback: action => {
            this.$message({
              type: 'info',
              message: `action: ${ action }`
            });
          }
        });
      },

      empty_contribute() {
        this.$alert('Please ensure the valid title and summary of the file , also be sure select the file !', 'FileInformation empty:', {
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

    mounted() {

    }
  }
</script>

<style>
  @import url("https://unpkg.com/element-ui/lib/theme-chalk/index.css");

  #base_contribute{
    background: url("../assets/background/background.jpg") repeat;
    background-position: center;
    height: 100%;
    width: 100%;
    background-size: cover;
    /*position: fixed;*/
    overflow: auto;
  }

  .contribute_container{
    border-radius: 15px;
    background-clip: padding-box;
    margin: 90px auto;
    width: 520px;
    padding: 35px 35px 15px 35px;
    background: #fff;
    border: 1px solid #eaeaea;
    box-shadow: 0 0 25px #cac6c6;
  }

  .drag-item{
    cursor: move;
    margin-bottom: 10px;
  }
</style>
