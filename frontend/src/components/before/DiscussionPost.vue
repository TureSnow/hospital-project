<template>
  <div id="base_discussion">
    <el-container id="discussion_container">
      <el-header style="background-color: #B3C0D1;color: #333;line-height: 60px;text-align: left;font-size: 20px;overflow: auto;">
        <div>
          Discussion draft: {{contributionName}}
        </div>
      </el-header>
      <el-header style="color: #333;line-height: 60px;text-align: left;">
        <span>You can click the button to create a new topic.</span>
        <el-button type="primary" size="mini" plain @click="createDialog()">+New discussion topic</el-button>
      </el-header>
      <el-header style="color: #333;line-height: 60px;text-align: left;margin-top: -30px">
        <span>You can click the button to get the latest information about this discussion post.</span>
        <el-button type="primary" size="mini" plain @click="refresh()">Refresh</el-button>
      </el-header>


      <el-main>

        <!--create dialog-->
        <el-dialog
          title="Create discussion topic :"
          :visible.sync="createDialogVisible"
          width="36%"
          :before-close="handleClose"
          style="text-align: left"
        >
          <el-input
            type="textarea"
            :autosize="{ minRows: 4 }"
            placeholder="Input your reply here."
            v-model="textarea"
            maxlength="300"
            show-word-limit
          ></el-input>
          <span slot="footer" class="dialog-footer">
            <el-button size="mini" @click="createDialogVisible = false">Cancel</el-button>
            <el-button size="mini" type="primary" @click="createTopicDiscussion()">Confirm</el-button>
          </span>
        </el-dialog>

        <!--reply dialog-->
        <el-dialog
          title="Discussion reply :"
          :visible.sync="dialogVisible"
          width="36%"
          :before-close="handleClose"
          style="text-align: left"
        >
          <el-input
            type="textarea"
            :autosize="{ minRows: 4 }"
            placeholder="Input your reply here."
            v-model="textarea"
            maxlength="300"
            show-word-limit
          ></el-input>
          <span slot="footer" class="dialog-footer">
            <el-button size="mini" @click="dialogVisible = false">Cancel</el-button>
            <el-button size="mini" type="primary" @click="replyDiscussion()">Confirm</el-button>
          </span>
        </el-dialog>


        <el-card v-if="postList.length==0" style="margin-bottom: 10px;height: 320px">
          <div>
            No comments yet , be the first one to eat crabs ?
          </div>
          <el-header style="color: #333;line-height: 60px;">
            <el-button type="primary" size="middle" plain @click="createDialog()">+Create discussion topic</el-button>
          </el-header>
        </el-card>
        <!--discussion card-->
        <el-card v-else style="margin-bottom: 10px"
                 v-for="(card, i) in postList"
                 :key="i"
        >
          <el-row :gutter="12">
            <div style="text-align: left;">
              <el-avatar class="creator_img" :size="50"> {{card.comment.releaseUserName.substring(card.comment.releaseUserName.length-3,card.comment.releaseUserName.length)}} </el-avatar>
              <div class="creator-info" style="display: inline-block;">
                <div class="creator-name" style="font-weight: 600;font-size: 20px;font-family: Consolas ">Name:{{card.comment.releaseUserName}}</div>
                <div class="creator-time" style="color:#B3C0D1;font-style: italic">Time:{{card.comment.time}}</div>
              </div>
            </div>
            <div style="background-color: aliceblue">
              <el-col :span="21" style="margin-left:7%;text-align: left">
                {{card.comment.content}}
                <el-divider content-position="right">discussion</el-divider>
                <el-col :span="24" style="background-color: #eeeeee;text-align: left;margin-right: 4%">

                  <!--讨论回复-->
                  <div v-if="card.communications.length==0">
                    <div>
                      No comments yet , be the first one to eat crabs ?
                    </div>
                    <el-header style="color: #333;line-height: 60px;">
                      <el-button type="info" size="mini" icon="el-icon-edit" plain  @click="replyFunction(card.comment.commentId,card.comment.releaseUserName)">reply</el-button>
                    </el-header>
                  </div>
                  <div v-else class="reply"
                       v-for="(reply, j) in card.communications"
                       :key="j"
                  >
                    <el-avatar class="creator_img" :size="50"> {{reply.fromUserName.substring(reply.fromUserName.length-3,reply.fromUserName.length)}} </el-avatar>
                    <div class="creator-info" style="display: inline-block;">
                      <div class="creator-name" style="font-weight: 400;font-size: 18px;font-family: Consolas">Name:{{reply.fromUserName}}</div>
                      <div class="creator-time" style="color:#B3C0D1;font-style: italic">Time:{{reply.time}}</div>
                    </div>
                    <el-col :span="21" style="margin-left:8%;text-align: left;margin-bottom: 10px">
                      <span style="color: #B3C0D1;">Reply {{reply.toUserName}} :</span>
                      {{reply.content}}
                    </el-col>
                    <el-col :span="21" style="margin-left:8%;text-align: left;margin-bottom: 10px">
                      <el-button type="info" size="mini" icon="el-icon-edit" plain  @click="replyFunction(card.comment.commentId,reply.fromUserName)">reply</el-button>
                    </el-col>
                    <hr style="width: 100%;opacity: 0.5;">
                  </div>
                </el-col>

              </el-col>
            </div>
          </el-row>
        </el-card>

      </el-main>
    </el-container>
  </div>
</template>

<script>
  export default {
    name: 'DiscussionPost',
    data () {

      return {
        //稿子的id，数据请求时使用
        contributionId:this.$route.query.postId,
        //稿子的name
        contributionName:this.$route.query.name,
        //是否是rebuttal
        rebuttal:this.$route.query.rebuttal,

        //新建讨论帖子时的显示控制
        createDialogVisible:false,
        //输入框界面的显示控制
        dialogVisible: false,
        textarea:'',


        //reply的回复数据暂存区
        replyForm:{
          commentId:'',
          toUserName:'',
          content:''
        },

        //讨论的数据
        postList:[
          /*{
            comment: {
              commentId: 34,
              contributeId: 30,
              releaseUserName: "user001",
              content: "但是公司的更好的妇女解放韩国",
              time: "2020-06-06 22:55:08",
              rebuttal: false
            },
            communications: [
              {
                communicationId: 37,
                commentId: 34,
                fromUserName: "user000",
                toUserName: "User001",
                content: "善良的看能否v金克拉撒旦",
                time: "2020-06-06 22:55:58"
              },
              {
                communicationId: 38,
                commentId: 34,
                fromUserName: "user000",
                toUserName: "User001",
                content: "；你；但 但十分",
                time: "2020-06-06 22:56:13"
              },
              {
                communicationId: 39,
                commentId: 34,
                fromUserName: "user000",
                toUserName: "User001",
                content: "尖酸刻薄大局考虑的方式比较",
                time: "2020-06-06 22:56:24"
              }
            ]
          },
          {
            comment: {
              commentId: 35,
              contributeId: 30,
              releaseUserName: "user001",
              content: "收到v表达方式v艰苦大师傅艰苦了",
              time: "2020-06-06 22:55:22",
              rebuttal: false
            },
            communications: [
              {
                communicationId: 40,
                commentId: 35,
                fromUserName: "user000",
                toUserName: "User002",
                content: "尖酸刻薄大局考虑的方式比较",
                time: "2020-06-06 22:56:32"
              },
              {
                communicationId: 41,
                commentId: 35,
                fromUserName: "user000",
                toUserName: "User002",
                content: "尖酸刻薄发的各部分定格比较,尖酸刻薄发的各部分定尖酸刻薄发的各部分定尖酸刻薄发的各部分定",
                time: "2020-06-06 22:56:36"
              },
              {
                communicationId: 42,
                commentId: 35,
                fromUserName: "user000",
                toUserName: "User001",
                content: "尖酸刻薄发的各部分的比分定格比较",
                time: "2020-06-06 22:56:41"
              },
              {
                communicationId: 43,
                commentId: 35,
                fromUserName: "user000",
                toUserName: "User001",
                content: "尖酸刻多亏了你的尽快发吧那就梵蒂冈",
                time: "2020-06-06 22:56:46"
              }
            ]
          },*/

        ],
      }
    },

    methods: {
      //数据交互

      //新建一个帖子时信息提交
      createTopicDiscussion(){
        this.createDialogVisible = false;
        //判断textarea是否空
        if(this.textarea==null){
          this.$alert('Attention please,the content can not be empty!', 'Empty content alert:', {
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

        //判断是否rebuttal
        if (!this.rebuttal){
          console.log(this.textarea);
          let formData = new FormData();
          formData.append('content', this.textarea);
          let config = {
            headers: {
              'Content-Type': 'multipart/form-data'
            }
          };
          this.$axios.post('/comment/'+this.contributionId,formData, config)
            .then(resp=>{
              //成功返回
              if (resp.status===200){
                //打印返回的数据
                console.log(resp.data);


                //第一次请求成功后发第二次请求总的信息
                this.getAllInformationOfPost();
              }
            }).catch(error=>{
            console.log(error);
          })
        }else { //rebuttal时
          console.log(this.textarea);
          let formData = new FormData();
          formData.append('content', this.textarea);
          let config = {
            headers: {
              'Content-Type': 'multipart/form-data'
            }
          };
          this.$axios.post('/comment/rebuttal/'+this.contributionId,formData, config)
            .then(resp=>{
              //成功返回
              if (resp.status===200){
                //打印返回的数据
                console.log(resp.data);


                //第一次请求成功后发第二次请求总的信息
                this.getAllInformationOfPost();
              }
            }).catch(error=>{
            console.log(error);
          })
        }

      },


      //得到所有的帖子信息
      getAllInformationOfPost(){
        if(!this.rebuttal){
          this.$axios.post('/getNormalComment/'+this.contributionId,{
          }).then((response) => {
            console.log("Success...");
            console.log(response);
            console.log(response.data);
            //数据渲染
            this.postList=response.data;
            console.log("ppppp")
            console.log(this.postList);

          }).catch((error) => {
            console.log(error);
            this.$alert('You get the information failed !', 'GetInformation failed:', {
              confirmButtonText: 'OK',
              callback: action => {
                this.$message({
                  type: 'info',
                  message: `action: ${ action }`
                });
              }
            });
            console.log("Test error...")
          });
        }else {//rebuttal时的数据请求
          this.$axios.post('/getRebuttalComment/'+this.contributionId,{
          }).then((response) => {
            console.log("Success...");
            console.log(response);
            console.log(response.data);
            //数据渲染
            this.postList=response.data;

          }).catch((error) => {
            console.log(error);
            this.$alert('You get the information failed !', 'GetInformation failed:', {
              confirmButtonText: 'OK',
              callback: action => {
                this.$message({
                  type: 'info',
                  message: `action: ${ action }`
                });
              }
            });
            console.log("Test error...")
          });
        }
      },


      //回复时信息提交
      replyDiscussion(){

        this.dialogVisible = false;
        //设置content
        this.replyForm.content=this.textarea;
        this.$axios.post('/communicate', {
          commentId:this.replyForm.commentId,
          toUserName:this.replyForm.toUserName,
          content:this.replyForm.content
          }
        )
          .then(resp => {
            // 根据后端的返回数据修改
            if(resp.status === 200 ) {
              console.log(resp.data);
              console.log("Success_____");

              //再次请求返回所有数据
              this.getAllInformationOfPost();

            }
          })
          .catch(error => {
            console.log("?????????");
            console.log(error)
            //alert('conference error')
          })

      },

      //刷新时数据渲染
      refresh(){
        this.getAllInformationOfPost();
      },

      //新建card dialog的显示控制
      createDialog(){
        //清空textarea的内容
        this.textarea=null;
        this.createDialogVisible=true;
      },

      //回复(reply)dialog的显示控制
      replyFunction(id,name){
        //清空textarea的内容
        this.textarea=null;
        this.dialogVisible=true;

        //设置值
        this.replyForm.commentId=id;
        this.replyForm.toUserName=name;
      },

      handleClose(done) {
        this.$confirm('Confirm to close the dialog ？')
          .then(_=> {
            done();
          })
          .catch(_ => {});
      }
    },

    mounted()
    {
      this.getAllInformationOfPost();
    }
  }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
  @import url("https://unpkg.com/element-ui/lib/theme-chalk/index.css");
  #base_discussion{
    background: url("../assets/background/background_qsy.jpg") repeat;
    background-position: center;
    height: 100%;
    width: 100%;
    background-size: cover;
    /*position: fixed;*/
    overflow: auto;
  }
  body{
    margin: 0px;
    padding: 0px;
  }
  #discussion_container{
    border-radius: 15px;
    background-clip: padding-box;
    margin: 90px auto;
    width: 800px;
    padding: 35px 35px 15px 35px;
    background: #fff;
    border: 1px solid #eaeaea;
    box-shadow: 0 0 25px #cac6c6;
  }
  .creator_img{
    display: inline-block;
    vertical-align: top;
  }
</style>
