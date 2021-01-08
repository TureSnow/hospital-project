<template>
  <el-container style="height: 100%;">
    <el-aside width="200px" style="overflow:hidden;background-color: rgb(238, 241, 246);position: fixed;height: 100%">
      <div style="align-content: center">
        <img src="../assets/background/photo.jpg" style="border-radius: 50%;width: 60%;padding: 20px 20% 10px 20%">
        <div style="margin-bottom: 20px">Name: {{ getUserName() }}</div>
      </div>
      <el-menu style="text-align: left; padding-left:10px" :default-openeds="open_list">
        <el-submenu index="1">
          <template slot="title"><i class="el-icon-menu"></i>会议</template>
          <el-menu-item-group>
            <el-menu-item index="1-1" @click="changeContent(1)"><i class="el-icon-s-order"></i>所有会议</el-menu-item>
            <el-menu-item index="1-2" @click="changeContent(2)"><i class="el-icon-s-order"></i>我主持的会议</el-menu-item>
            <el-menu-item index="1-3" @click="changeContent(3)"><i class="el-icon-s-order"></i>我参与的会议</el-menu-item>
            <el-menu-item index="1-4" @click="changeContent(4)"><i class="el-icon-s-order"></i>我参与投稿的会议</el-menu-item>
            <el-menu-item index="1-5">
              <router-link to="conference">
                <i class="el-icon-plus"></i>申请新会议
              </router-link>
            </el-menu-item>
          </el-menu-item-group>

        </el-submenu>
        <el-menu-item index="2" @click="changeContent(5)"><i class="el-icon-message-solid"></i>通知</el-menu-item>
        <el-menu-item index="3" @click="changeContent(6)"><i class="el-icon-user"></i>我的邀请</el-menu-item>
      </el-menu>
    </el-aside>

    <el-container style="margin-left:200px">
      <el-header style="text-align: right; font-size: 12px;background-color: #B3C0D1;
                    color: #333;line-height: 60px;padding:0 50px">
        <el-dropdown>
          <i class="el-icon-setting" style="margin-right: 15px;cursor: pointer;">关于此账户</i>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item>
              <el-button @click="checkLogOut();">
                注销此账户（登出）
              </el-button>
            </el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </el-header>


      <el-main style="padding-bottom:50px">
        <el-row :gutter="12">
          <div v-if="meeting">
            <el-col :span="8" v-for="meeting in currentMeetingList" :key="meeting.meetingId">
              <el-card class="box-card" style="text-align: left;margin-bottom: 10px">
                <div slot="header"
                     style="padding-left:20px;white-space: nowrap;text-overflow: ellipsis;overflow: hidden;">
                  <span>{{ meeting.fullname }}</span>
                </div>
                <div style="margin-left: 18px;font-size: 14px;">
                  ShortName:{{ meeting.shortname }}<br>
                  StartTime:{{ meeting.starttime }}<br>
                  EndTime:{{ meeting.endtime }}<br>
                  Address:{{ meeting.address }}<br>
                  Deadline for submission: {{ meeting.ddl }}<br>
                  ReleaseTime:{{ meeting.releaseTime }}<br>
                  Conference Creator: {{ meeting.creatusername }}<br>
                  The condition of conference：{{ getCondition(meeting.condition) }}<br><br>
                </div>

                <!--投稿button-->
                <div v-if="meeting.condition==2 && toContribute">


                  <!--测试-->
                  <!--提交稿件评价信息的按钮-->
                  <!-- <div style="text-align: right; margin-top: 10px">
                    <el-button type="primary" size="mini" @click="changeShow()">Evaluate the Contribution</el-button>
                  </div> -->


                  <div style="text-align: right; margin-top: 10px">
                    <!--@click.native="getId(meeting.meetingId,meeting.topicList)"-->
                    <!--<router-link :to="{name:'Contribute',params:{id:meeting.meetingId,topic:meeting.topicList}}">
                      <el-button type="primary" size="mini" >投稿</el-button>
                    </router-link>-->
                    <el-button type="primary" size="mini"
                               @click="judgeIdentify(meeting.meetingId,meeting.topics,meeting.creatusername)">Contribute
                    </el-button><!--@click="goToPost(30,rebuttal,dd)"-->
                  </div>
                </div>
                <!--修改稿件-->
                <div v-if="meeting.condition==2 && changeContribution">
                  <div style="text-align: right; margin-top: 10px">
                    <el-button type="primary" size="mini"
                               @click="getContributionList(meeting.meetingId,meeting.topics)">To Change Contribution
                    </el-button>
                  </div>
                </div>

                <!--选择修改的稿件-->
                <el-dialog title="Select the contribution you want to change:" :visible.sync="contributeDialog"
                           width="35%">
                  <div v-for="element in contributionList" :key="element.title">
                    <el-button type="primary" @click="toChangeContribution(element.contributionId)"
                               style="margin-bottom: 10px;overflow: hidden;width:80%">{{ element.title }}
                    </el-button>
                  </div>
                </el-dialog>

                <!-- PCmember查看分配到的稿件    ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ -->
                <div v-if="meeting.condition==3 && isPCmember">
                  <div style="text-align: right; margin-top: 10px">

                    <el-button type="primary" size="mini" @click="getContributionList_PCmember(meeting.meetingId);">
                      查看分配到的稿件
                    </el-button>

                  </div>
                </div>

                <el-dialog title="稿件列表" :visible.sync="PCmemberContributionDialog" width="30%">
                  <div v-for="contribution in contributionList_PCmember" :key="contribution.contributionId">

                    <el-button type="primary"
                               @click="currentContribution=contribution;getContributionCondition(xid,currentContribution.contributionId);getContributionConditionString(xid,currentContribution.contributionId);contributionDetailDialog=true;PCmemberContributionDialog=false;">
                      {{ contribution.title }}
                    </el-button>
                    <br>

                  </div>
                </el-dialog>

                <el-dialog title="详情" :visible.sync="contributionDetailDialog" width="60%">
                  <h3>{{ currentContribution.title }}</h3>
                  <h3>状态：{{ contributionConditionString }}</h3><br>
                  Summary:<br>
                  <div style="overflow-y:scroll; width:100%; height:200px;">
                    {{ currentContribution.summary }}
                  </div>
                  <div style="width:100%;height:30px;padding:10px">
                    <div style="width:100%;">
                      <el-button style="display:inline;" type="primary" size="mini"
                                 @click="toPreview(currentContribution.contributionId);contributionDetailDialog=false;">
                        预览稿件（PDF）
                      </el-button>
                      <br>
                      <el-button style="display:inline;margin-top:2px" type="primary" size="mini"
                                 @click="contributionDownload(currentContribution.contributionId,currentContribution.filename)">
                        下载稿件（PDF）
                      </el-button>
                      <br>
                      <el-button style="float:right;margin-top:-20px" v-if="!contributionCondition" type="primary"
                                 size="mini"
                                 @click="changeShow(currentContribution.contributionId);contributionDetailDialog=false;">
                        评审
                      </el-button>
                      <br>
                    </div>
                  </div>
                </el-dialog>

                <!--提交稿件的评价信息-->
                <el-dialog title="Evaluate the contribution:" :visible.sync="changeContributionDialog" width="40%">
                  <el-form :model="theForm"
                           :rules="rules"
                           label-position="left"
                           label-width="95px">

                    <el-form-item label="Score" prop="score">
                      <el-input type="text"
                                class="login_input"
                                v-model="theForm.score"
                                auto-complete="off"
                                placeholder="Scores"></el-input>
                    </el-form-item>
                    <br>

                    <el-form-item label="Comment" prop="comment" style="margin-top: -5px">
                      <el-input type="textarea"
                                placeholder="Please input the Comment."
                                maxlength="800"
                                auto-complete="off"
                                show-word-limit
                                v-model="theForm.comment"></el-input>
                    </el-form-item>

                    <el-form-item label="Confidence" prop="confidence">
                      <el-checkbox-group
                        :max="1"
                        v-model="theForm.confidence"
                        v-for="element in confidence"
                        :key="element.id"
                        style="text-align: left">

                        <el-checkbox :label="element" name="type"></el-checkbox>
                      </el-checkbox-group>
                    </el-form-item>

                    <el-form-item style="width: 100%">
                      <el-button type="primary"
                                 style="margin:10px auto 0px auto;width: 100%;background: #afb4db;line-height: 0.8"
                                 v-on:click="submitTheInformationOfContribution(theIdOfContribution)">Submit
                      </el-button>
                    </el-form-item>
                  </el-form>
                </el-dialog>
                <!-- PCmember查看分配到的稿件   ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ -->


                <div v-if="isAvailable||isChair||isAuthor||isPCmember">
                  <el-dropdown style="float:right">
                    <span class="el-dropdown-link">
                      <i class="el-icon-more">  &nbsp; <small>更多操作</small></i>
                    </span>
                    <el-dropdown-menu slot="dropdown">

                      <el-dropdown-item v-if="isPCmember&&(meeting.condition==3 ||meeting.condition==4)"
                                        @click.native="getDiscussionAvailableList(meeting.meetingId,false);currentMeetingId=meeting.meetingId;reviewChangeSelectorDialog=true;">
                        修改或确认评审结果
                      </el-dropdown-item>

                      <el-dropdown-item v-if="(isChair||isPCmember)&&(meeting.condition==3 ||meeting.condition==4)"
                                        @click.native="getDiscussionAvailableList(meeting.meetingId,isChair);currentMeetingId=meeting.meetingId;discussionSelectorDialog=true;">
                        参与评审结果讨论
                      </el-dropdown-item>
                      <el-dropdown-item v-if="isChair"
                                        @click.native="changeContent(7);currentMeetingId=meeting.meetingId;currentMeetingName=meeting.fullname">
                        邀请 PC_Member
                      </el-dropdown-item>
                      <el-dropdown-item v-if="isChair&&meeting.condition==1"
                                        @click.native="currentMeetingId=meeting.meetingId;openContribute();changeContent(2);fixContributeTwice();">
                        开启投稿
                      </el-dropdown-item>
                      <el-dropdown-item v-if="isChair&&meeting.condition==2&&operationAvailable"
                                        @click.native="currentMeetingId=meeting.meetingId;reviewDialog=true;">开启审稿
                      </el-dropdown-item>
                      <el-dropdown-item v-if="isChair&&meeting.condition==3"
                                        @click.native="currentMeetingId=meeting.meetingId;finishReview();changeContent(2);">
                        发布第一次评审结果
                      </el-dropdown-item>
                      <el-dropdown-item v-if="isChair&&meeting.condition==4"
                                        @click.native="currentMeetingId=meeting.meetingId;finalFinishReview();changeContent(2);fixContributeTwice();">
                        发布第二次评审结果
                      </el-dropdown-item>
                      <el-dropdown-item v-if="isAuthor&&(meeting.condition==4 || meeting.condition==6)"
                                        @click.native="getContributionSelectList(meeting.meetingId);currentMeetingId=meeting.meetingId;resultSelectDialog=true;">
                        查看评审结果
                      </el-dropdown-item>

                      <!--警告：测试用 <el-dropdown-item  @click.native="getContributionSelectList(meeting.id);currentMeetingId=meeting.meetingId;resultSelectDialog=true;">查看评审结果</el-dropdown-item> -->
                    </el-dropdown-menu>
                  </el-dropdown>
                </div>

                <!-- 查看审稿结果 -->
                <el-dialog title="选择你要查看的稿件" :visible.sync="resultSelectDialog" width="40%">
                  <el-button v-for="contribution in contributionList" :key="contribution.title"
                             style="margin:0 5px 5px 0" type="primary"
                             @click="resultDialog=true;currentContribution=contribution;">{{ contribution.title }}
                  </el-button>
                  <!--警告：测试用 <el-button style="margin:0 5px 5px 0" type="primary" @click="resultDialog=true;">121212</el-button> -->
                </el-dialog>

                <el-dialog title="审稿结果" :visible.sync="resultDialog" width="60%">
                  <div v-for="i in 3" :key="i" style="width:30%;display:inline-block;margin-left:3%">
                    <h3>评审结果&nbsp;{{ i }}</h3><br>
                    <div style="font-size: 17px;">
                      score:{{ getScoreString(getReview(currentContribution, i).score) }}<br>
                      confidence:{{ getReview(currentContribution, i).confidence }}<br>
                      comment:<br>
                      <div style="overflow-y:scroll; width:100%; height:200px;">
                        {{ getReview(currentContribution, i).comment }}
                      </div>
                    </div>
                  </div>

                  <el-button v-if="(!currentContribution.rebuttal)" style="margin:5px;float:right" type="primary"
                             @click="rebuttalDialog=true;">rebuttal
                  </el-button>

                </el-dialog>


                <el-dialog title="The reason of rebuttal:" :visible.sync="rebuttalDialog" width="40%">
                  <el-input
                    type="textarea"
                    placeholder="Please input your reason."
                    v-model="rebuttal">
                  </el-input>
                  <el-button style="margin:0 5px 5px 0" type="primary"
                             @click="sendRebuttal(currentContribution.contributionId);rebuttalDialog=false;resultDialog=false;resultSelectDialog=false;">
                    提交
                  </el-button>
                </el-dialog>


                <!-- 选择稿件分配策略 -->
                <el-dialog title="选择稿件分配策略" :visible.sync="reviewDialog" width="18%">

                  <el-button type="primary"
                             @click="reviewDialog = false;startReview(0);operationAvailable=false;fixContributeTwice()">
                    基于&nbsp;&nbsp;topic&nbsp;&nbsp;相关度
                  </el-button>
                  <br><br>
                  <el-button type="primary"
                             @click="reviewDialog = false;startReview(1);operationAvailable=false;fixContributeTwice()">
                    基于审稿平均负担&nbsp;
                  </el-button>

                </el-dialog>

                <!-- 查看稿件讨论 -->
                <el-dialog title="选择进行讨论的稿件" :visible.sync="discussionSelectorDialog" width="40%">
                  <div><small>注意：仅能对发表过评审的稿件进行讨论</small></div>

                  <el-button v-for="contribution in discussionAvailableList" :key="contribution.title"
                             style="margin:0 5px 5px 0" type="primary"
                             @click="discussionSelectorDialog=false;goToPost(contribution.contributionId,contribution.rebuttal,contribution.title);">
                    {{ contribution.title }}
                  </el-button>

                </el-dialog>

                <!-- 选择修改评审内容 -->
                <el-dialog title="选择重新评审的稿件" :visible.sync="reviewChangeSelectorDialog" width="60%">
                  <div v-for="contribution in discussionAvailableList" :key="contribution.title">
                    <el-button style="margin:0 5px 5px 0" type="primary"
                               @click="reviewChangeDialog=true;reviewChangeSelectorDialog=false;currentContribution=contribution;theIdOfContribution=contribution.contributionId;">
                      修改对于&nbsp;{{ contribution.title }}&nbsp;的评审
                    </el-button>

                    <el-button style="margin:0 5px 5px 0;float:right" type="primary"
                               v-on:click="reviewConfirm(contribution.contributionId,contribution.rebuttal)">确认不修改
                    </el-button>

                  </div>
                </el-dialog>

                <el-dialog title="Re-Evaluate the contribution:" :visible.sync="reviewChangeDialog" width="40%">
                  <el-form :model="theSecondForm"
                           :rules="rules"
                           label-position="left"
                           label-width="95px">

                    <el-form-item label="Score" prop="score">
                      <el-input type="text"
                                class="login_input"
                                v-model="theSecondForm.score"
                                auto-complete="off"
                                placeholder="Scores"></el-input>
                    </el-form-item>
                    <br>

                    <el-form-item label="Comment" prop="comment" style="margin-top: -5px">
                      <el-input type="textarea"
                                placeholder="Please input the Comment."
                                maxlength="800"
                                auto-complete="off"
                                show-word-limit
                                v-model="theSecondForm.comment"></el-input>
                    </el-form-item>

                    <el-form-item label="Confidence" prop="confidence">
                      <el-checkbox-group
                        :max="1"
                        v-model="theSecondForm.confidence"
                        v-for="element in confidence"
                        :key="element.id"
                        style="text-align: left">

                        <el-checkbox :label="element" name="type"></el-checkbox>
                      </el-checkbox-group>
                    </el-form-item>

                    <el-form-item style="width: 100%">
                      <el-button type="primary"
                                 style="margin:10px auto 0px auto;width: 100%;background: #afb4db;line-height: 0.8"
                                 v-on:click="re_submitTheInformationOfContribution(theIdOfContribution,currentContribution.rebuttal)">
                        Submit
                      </el-button>
                    </el-form-item>
                  </el-form>
                </el-dialog>


              </el-card>
            </el-col>
          </div>

          <div v-if="receiveInvitation">
            <el-col :span="24" v-for="invitation in invitationList" :key="invitation.meetingId"
                    style="font-size: 80%;margin:5px 0;background-color: rgb(238, 241, 246);height:40px;border-radius:5px 5px 5px 5px;text-align:left;padding:10px 20px 0 20px;color:#000000a0">
              <p style="display:inline;width:70%">来自 {{ invitation.senderName }} 的邀请，邀请您加入会议
                {{ invitation.meetingName }}</p>
              <el-popover
                placement="right"
                title="Topic 列表"
                width="150"
                trigger="click">
                <p v-for="topic in invitation.topics" :key="topic" style="font-size:80%"> {{ topic }} </p>
                <el-button slot="reference" size="mini" style="margin-top:-5px;opacity:0.7;">查看会议Topic</el-button>
              </el-popover>
              <div v-if="operationAvailable" style="float:right;margin-top:-5px">
                <el-button type="primary" @click="currentMeetingId=invitation.meetingId;topicSelectDialog=true;"
                           icon="el-icon-check" size="mini" style="opacity:0.7;"><small>接受</small></el-button>
                <el-button type="primary"
                           @click="currentMeetingId=invitation.meetingId;selectedTopics=[];operationAvailable=false;answerInvitation('refuse');"
                           icon="el-icon-close" size="mini" style="opacity:0.7;"><small>拒绝</small></el-button>
              </div>

              <el-dialog title="选择你要负责的Topic" :visible.sync="topicSelectDialog" width="30%">
                <el-checkbox-group v-model="selectedTopics">
                  <el-checkbox v-for="topic in invitation.topics" :key="topic" v-bind:label="topic"></el-checkbox>
                </el-checkbox-group>
                <el-button type="primary" v-if="selectedTopics.length!=0"
                           @click="operationAvailable=false;answerInvitation('agree');topicSelectDialog=false;"
                           icon="el-icon-check" size="mini" style="margin-top:20px;opacity:0.7;"><small>确认提交</small>
                </el-button>
              </el-dialog>
            </el-col>

            <!-- 测试用-->
            <!-- <el-button type="primary" @click="topicSelectDialog=true;" icon="el-icon-check" size="mini" style="opacity:0.7;"><small>接受</small></el-button> -->
            <!--测试用 -->


            <!-- <el-col :span="24" v-for="invitation in invitationList_answered" :key="invitation.meetingId" style="font-size: 80%;margin:5px 0;background-color: rgb(238, 241, 246);height:40px;border-radius:5px 5px 5px 5px;text-align:left;padding:10px 20px 0 20px;color:#000000a0">
              <p style="display:inline;width:70%">来自 {{invitation.senderName}} 的邀请，邀请您加入会议 {{invitation.meetingName}}</p>
              <div style="float:right;margin-top:-5px">
              <el-button type="primary" v-if="invitation.state==1" icon="el-icon-check" disabled size="mini" style="opacity:0.7;"><small>接受</small></el-button>
              <el-button type="primary" v-if="invitation.state==2" icon="el-icon-close" disabled size="mini" style="opacity:0.7;"><small>拒绝</small></el-button>
              </div>
            </el-col> --><!--这是显示历史受邀记录的，后端没做，先搁置一下-->
          </div>

          <div v-if="myInvitation">
            <el-col :span="24" v-for="invitation in myInvitationList" :key="invitation.meetingId"
                    style="font-size: 80%;margin:5px 0;background-color: rgb(238, 241, 246);height:40px;border-radius:5px 5px 5px 5px;text-align:left;padding:10px 20px 0 20px;color:#000000a0">
              <p style="display:inline;width:70%">您邀请 {{ invitation.receiverName }} 加入会议
                {{ invitation.meetingName }}</p>
              <div style="float:right;margin-top:-5px">
                <el-button type="primary" v-if="invitation.state==0" icon="el-icon-s-check" size="mini" disabled
                           style="opacity:0.7;"><small>待审核</small></el-button>
                <el-button type="primary" v-if="invitation.state==1" icon="el-icon-check" size="mini" disabled
                           style="opacity:0.7;"><small>接受</small></el-button>
                <el-button type="primary" v-if="invitation.state==2" icon="el-icon-close" size="mini" disabled
                           style="opacity:0.7;"><small>拒绝</small></el-button>
              </div>
            </el-col>
          </div>

          <div v-if="sendInvitation">
            <el-col :span="24"
                    style="margin:5px 0;background-color: rgb(238, 241, 246);border-radius:5px 5px 5px 5px;padding:10px 20px 0 20px;color:#000000a0">
              <h1>在会议：{{ currentMeetingName }} 中邀请PC member</h1><br>
              <div>
                <el-input v-model="search" placeholder="输入对方的真实姓名" style="width:20%"></el-input>
                <el-button type="primary" @click="getUsersFromRealUsername();" icon="el-icon-search"
                           style="margin-left:20px;opacity:0.8">搜索
                </el-button>
                <el-button type="primary" @click="invite();changeContent(7);canInvite=false;" icon="el-icon-user"
                           style="opacity:0.8">邀请
                </el-button>
              </div>
              <br>
            </el-col>

            <el-col :span="24" v-for="user in targetUserList" :key="user.id"
                    style="margin:5px 0;background-color: rgb(238, 241, 246);border-radius:5px 5px 5px 5px;padding:10px 20px 10px 20px;color:#000000a0">
              <el-checkbox-group v-model="checkList" style="width:100%">
                <el-checkbox v-bind:label="user.id" style="width:100%;text-align:left;">{{ user.realUsername }} &nbsp;
                  <i class="el-icon-caret-right"></i>area:{{ user.area }} &nbsp; <i class="el-icon-caret-right"></i>username:{{
                    user.username
                  }}
                  &nbsp; <i class="el-icon-caret-right"></i>email:{{ user.email }}<br></el-checkbox>
              </el-checkbox-group>

            </el-col>
          </div>

        </el-row>

      </el-main>
    </el-container>
  </el-container>
</template>

<script>
export default {
  name: 'User',
  data() {
    const dataValid = (rule, value, callback) => {
      return callback()
    };
    return {
      xid: '',
      /*//测试数据
      rebuttal:false,
      dd:'hjdkkf',*/
      open_list: ["1"],
      name: "",
      currentMeetingName: "",
      currentMeetingId: 0,
      contributionDetailDialog: false,
      PCmemberContributionDialog: false,
      reviewDialog: false,
      reviewChangeSelectorDialog: false,
      reviewChangeDialog: false,
      operationAvailable: true,//为了在后台响应的时候不让前端继续传递信息所设置的开关
      resultSelectDialog: false,
      discussionSelectorDialog: false,
      topicSelectDialog: false,
      rebuttalDialog: false,
      resultDialog: false,
      isAuthor: false,
      isChair: false,
      isPCmember: false,

      //稿件的评价信息
      theForm: {
        score: '',
        comment: '',
        confidence: []
      },

      theSecondForm: {
        score: '',
        comment: '',
        confidence: []
      },

      //4个confidence原始值
      confidence: [
        'very low',
        'low',
        'high',
        'very high'
      ],
      //设置评价的稿子的id
      theIdOfContribution: '',

      //toContribute控制"投稿"button的显示
      toContribute: false,
      //控制"稿件修改"的button显示
      changeContribution: false,
      //控制稿件修改的选择对话界面
      contributeDialog: false,
      //要修改稿件的某一特定会议的topicList
      topicListOfTheContributeConference: [],
      //返回的确定会议的稿件List
      contributionList: [],
      //某个稿件的所有信息
      contributionSet: [],
      //控制稿件信息提交的对话界面
      changeContributionDialog: false,

      //当前需要查看结果的稿件 此处是初始化的值
      currentContribution: {
        "contributionId": 0,
        "meetingid": 0,
        "title": "",
        "summary": "",
        "submitusername": "",
        "filename": "",
        "topics": [],
        "condition": 0,
        "confidence1": 0,
        "confidence2": 0,
        "confidence3": 0,
        "score1": 0,
        "score2": 0,
        "score3": 0,
        "comment1": null,
        "comment2": null,
        "comment3": null,
        "isRebuttal": false,
      },

      isAvailable: false,
      meeting: true,
      sendInvitation: false,
      receiveInvitation: false,
      myInvitation: false,
      search: "",
      rebuttal: "",
      reviewConfirmed: [false, false],
      currentMeetingList: [],
      checkList: [],
      availableList: [],
      chairList: [],
      authorList: [],
      PCMemberList: [],
      myInvitationList: [],
      invitationList: [],
      invitationList_answered: [],
      targetUserList: [],
      discussionAvailableList: [],
      selectedTopics: [], //接受邀请时选择的topic
      contributionList_PCmember: [],//PCmember分配到的稿件列表
      index: 0,
      contributionConditionString: "",
      contributionCondition: false,


      rules: {
        score: [{
          required: true,
          message: 'Score:即PC member对分配给自己的稿件进行评分，-2 到 2 分且不能给0分。4个评分分别对应4个状态 ：-2 -> reject， -1 -> weak reject， 1 -> weak accept， 2 -> accept。',
          trigger: 'blur'
        }],
        comment: [
          {required: true, message: '请填写稿件评语，800个字符字符以内.', trigger: 'blur'},
          {max: 800, message: '800个字符字符以内.', trigger: 'blur'},
          {validator: dataValid, trigger: 'blur'}
        ],
        confidence: [
          {type: 'array', required: true, message: '请至少选择一个topic', trigger: 'change'}
        ]
      },
    }
  },
  methods: {
    //参加帖子讨论传参:第一次rebuttal时将参数rebuttal值设为false;第二次rebuttal时将参数rebuttal设为true.
    goToPost(contributionId, rebuttal, contributionName) {
      //传参、跳转
      this.$router.push({
        path: 'DiscussionPost',
        query: {postId: contributionId, rebuttal: rebuttal, name: contributionName}
      });
    },
    getM(meeting) {
      console.log("打印meeting ", meeting)
    },

    checkForm(form) {
      if (form.confidence.length == 0) {
        this.$alert('You should select at least one confidence level!', 'ConfidenceList alert:', {
          confirmButtonText: 'OK',
          callback: action => {
            this.$message({
              type: 'info',
              message: `action: ${action}`
            });
          }
        });
        return false;
      } else if ((form.score != 1) && (form.score != 2) && (form.score != -1) && (form.score != -2)) {
        this.$alert('The score must be -2 or -1 or 1 or 2,it can not be empty and any other value!', 'Score alert:', {
          confirmButtonText: 'OK',
          callback: action => {
            this.$message({
              type: 'info',
              message: `action: ${action}`
            });
          }
        });
        return false;
      } else if (form.comment.length == 0) {
        this.$alert('You must fill the comment,it can not be empty !', 'Comment alert:', {
          confirmButtonText: 'OK',
          callback: action => {
            this.$message({
              type: 'info',
              message: `action: ${action}`
            });
          }
        });
        return false;
      }
      return true;
    },

    //提交稿子的信息
    submitTheInformationOfContribution(id) {
      //判断表单数据是否符和要求
      // if (this.theForm.confidence.length == 0) {
      //   this.$alert('You should select at least one confidence level!', 'ConfidenceList alert:', {
      //     confirmButtonText: 'OK',
      //     callback: action => {
      //       this.$message({
      //         type: 'info',
      //         message: `action: ${ action }`
      //       });
      //     }
      //   });
      //   return ;
      // }else if ((this.theForm.score != 1) && (this.theForm.score != 2) && (this.theForm.score != -1) && (this.theForm.score != -2)) {
      //   this.$alert('The score must be -2 or -1 or 1 or 2,it can not be empty and any other value!', 'Score alert:', {
      //     confirmButtonText: 'OK',
      //     callback: action => {
      //       this.$message({
      //         type: 'info',
      //         message: `action: ${ action }`
      //       });
      //     }
      //   });
      //   return ;
      // }else if(this.theForm.comment.length==0){
      //   this.$alert('You must fill the comment,it can not be empty !', 'Comment alert:', {
      //     confirmButtonText: 'OK',
      //     callback: action => {
      //       this.$message({
      //         type: 'info',
      //         message: `action: ${ action }`
      //       });
      //     }
      //   });
      //   return ;
      // }
      if (!this.checkForm(this.theForm)) return;
      //发送请求
      this.$axios.post('/startReview/submitReviewResult', {
        contributionId: id,
        score: this.theForm.score,
        comment: this.theForm.comment,
        confidence: this.theForm.confidence[0]
      }).then((response) => {
        console.log(response);
        console.log(response.data);
        console.log("1111111111aaaaaaaaaa");
        //成功上传提示
        this.$alert('You evaluate the contribution successfully !', 'Evaluate successfully:', {
          confirmButtonText: 'OK',
          callback: action => {
            this.$message({
              type: 'info',
              message: `action: ${action}`
            });
          }
        });
        //关闭稿件审核信息界面
        this.changeContributionDialog = false;

      }).catch((error) => {
        console.log(error);
        this.$alert('You evaluate the contribution failed !', 'Evaluate failed:', {
          confirmButtonText: 'OK',
          callback: action => {
            this.$message({
              type: 'info',
              message: `action: ${action}`
            });
          }
        });
        console.log("aaaaaaaaaaxxxxxxx")
      });

    },

    re_submitTheInformationOfContribution(id, rebuttal) {
      if (!this.checkForm(this.theSecondForm)) return;
      this.$axios.post('refactor' + (rebuttal ? 2 : 1) + '/' + id, {
        confidence: this.theSecondForm.confidence[0],
        score: this.theSecondForm.score,
        comment: this.theSecondForm.comment,
      }).then((response) => {
        console.log(response);
        console.log(response.data);
        console.log("22222222222aaaaaaaaaa");

        if (response.data.result) {
          this.$alert('Access deny ', 'Failed alert:', {
            confirmButtonText: 'OK',
            callback: action => {
              this.$message({
                type: 'info',
                message: `action: ${action}`
              });
            }
          });
        } else {
          //成功上传提示
          this.$alert('You re-evaluate the contribution successfully !', 'Re-Evaluate successfully:', {
            confirmButtonText: 'OK',
            callback: action => {
              this.$message({
                type: 'info',
                message: `action: ${action}`
              });
            }
          });
        }

        //关闭稿件审核信息界面
        this.reviewChangeDialog = false;

      }).catch((error) => {
        console.log(error);
        this.$alert('You re-evaluate the contribution failed !', 'Re-Evaluate failed:', {
          confirmButtonText: 'OK',
          callback: action => {
            this.$message({
              type: 'info',
              message: `action: ${action}`
            });
          }
        });
        console.log("aaaaaaaaaaxxxxxxx2")
      });

    },


    reviewConfirm(id, rebuttal) {
      if (rebuttal == true) {

        this.$axios.post('/confirm/' + id + "/" + 2, {}).then((response) => {
          console.log(response);
          console.log(response.data);
          this.reviewChangeSelectorDialog = false;
        }).catch((error) => {
          console.log(error);
        });
      } else {
        this.$axios.post('/confirm/' + id + "/" + 1, {}).then((response) => {
          console.log(response);
          console.log(response.data);
          this.reviewChangeSelectorDialog = false;
        }).catch((error) => {
          console.log(error);
        });
      }
    },

    sendRebuttal(id) {
      this.$axios.post('/submitRebuttal', {
        contributionId: id,//以long类型提交
        message: this.rebuttal//以String类型提交
      }).then((response) => {
        console.log(response);
        console.log(response.data);
      }).catch((error) => {
        console.log(error);
      });
    },

    //改变提交的评语信息界面的显示状态
    changeShow(contributionId) {
      this.changeContributionDialog = true;
      this.theIdOfContribution = contributionId;
    },

    //修改可以二次开启投稿
    fixContributeTwice() {
      window.location.reload();
    },
    //修改可以二次开启审稿
    debugContributeTwice(id) {
      window.location.reload();
      this.currentMeetingId = id;
      this.reviewDialog = true;

    },
    //稿件修改传参
    toChangeContribution(contributionId) {
      this.$axios.post('/getContribution/' + contributionId, {}).then((response) => {
        console.log(response);
        console.log(response.data);
        console.log("aaaaaaaaaa");
        this.contributionSet = response.data;
        //关闭稿件选择列表
        this.contributeDialog = false;
        //传参、跳转
        this.$router.push({
          name: 'Change_contribute',
          params: {informationSet: this.contributionSet, topic: this.topicListOfTheContributeConference}
        });

      }).catch((error) => {
        console.log(error);
        console.log("aaaaaaaaaaxxxxxxx")
      });
    },

    //得到所有稿件列表
    getContributionList(id, topic) {
      this.$axios.post('/getMyContribution/' + id, {}).then((response) => {
        console.log(response);
        console.log(response.data);
        console.log("bbbbb");
        this.topicListOfTheContributeConference = topic;
        //console.log(this.topicListOfTheContributeConference);
        this.contributionList = response.data;
        //显示稿件选择列表
        this.contributeDialog = true;
      }).catch((error) => {
        console.log(error);
        console.log("xxxxxxx")
      });
    },

    //得到所有稿件列表————用于查看结果
    getContributionSelectList(id) {
      this.$axios.post('/getMyContribution/' + id, {}).then((response) => {
        console.log(response);
        console.log(response.data);
        this.contributionList = response.data;
      }).catch((error) => {
        console.log(error);
      });
    },

    //判断会议创建者不能投稿
    judgeIdentify(id, topics, name) {
      if (this.getUserName() == name) {
        this.$alert('Sorry,you have created this conference, so you can not contribute for it!', 'Contribute alert:', {
          confirmButtonText: 'OK',
          callback: action => {
            this.$message({
              type: 'info',
              message: `action: ${action}`
            });
          }
        });

        return;
      }

      //console.log("bbbbbbbbb");
      this.$router.push({name: 'Contribute', params: {id: id, topic: topics}});
      //this.$router.replace('/Contribute')
    },

    getContributionList_PCmember(meetingId) {
      console.log("dsad", meetingId)
      this.$axios.post('/getMyContributionInMeeting/' + meetingId, {}).then((response) => {
        console.log(response);
        console.log(response.data);
        console.log("dsad2", meetingId)
        this.contributionList_PCmember = response.data;
        this.xid = meetingId;
        console.log("xid is", this.xid)
        this.PCmemberContributionDialog = true;
      }).catch((error) => {
        console.log(error);
      });
    },

    //注销账号
    logoutAccount() {
      // 移除token
      localStorage.removeItem('token')
      localStorage.removeItem('userDetails')
      this.$router.replace({path: '/Login'})
    },

    getAllAvailableMeeting() {
      this.$axios.post('/allMeeting', {}).then((response) => {
        console.log(response);
        console.log(response.data);

        var availableList = new Array(0);
        var length = response.data.length;
        for (var i = 0; i <= length - 1; i++)
          if (response.data[i].condition == 1 || response.data[i].condition == 2)
            availableList.push(response.data[i]);
        this.availableList = availableList;

      }).catch((error) => {
        console.log(error);
      });
    },

    getAllMyMeeting() {
      this.$axios.post('/findAllmyMeeting', {}).then((response) => {
        console.log(response);
        console.log(response.data);
        this.chairList = response.data;
      }).catch((error) => {
        console.log(error);
      });
    },

    getDiscussionAvailableList(meetingId, isChair) {
      if (isChair) {
        this.$axios.post('/getContributions/' + meetingId, {}).then((response) => {
          console.log(response);
          console.log(response.data);
          this.discussionAvailableList = response.data;
        }).catch((error) => {
          console.log(error);
        });
      } else {
        this.$axios.post('/getContributeCanCommunicate/' + meetingId, {}).then((response) => {
          console.log(response);
          console.log(response.data);
          this.discussionAvailableList = response.data;
        }).catch((error) => {
          console.log(error);
        });
      }
    },

    getMeetingAsAuthor() {
      this.$axios.post('/getMeetingAsAuthor', {}).then((response) => {
        console.log(response);
        console.log(response.data);
        this.authorList = response.data;
      }).catch((error) => {
        console.log(error);
      });
    },

    getMeetingAsPCmember() {
      this.$axios.post('/getMeetingAsPCmember', {}).then((response) => {
        console.log(response);
        console.log(response.data);
        this.PCMemberList = response.data;
      }).catch((error) => {
        console.log(error);
      });
    },

    getMyInvitationList() {
      this.$axios.get('/invite/1', {}).then((response) => {
        console.log(response);
        console.log(response.data);
        this.myInvitationList = response.data;
      }).catch((error) => {
        console.log(error);
      });
    },

    getInvitationList() {
      this.$axios.get('/invite/2', {}).then((response) => {
        console.log(response);
        console.log(response.data);
        this.invitationList = [];
        this.invitationList_answered = [];
        var length = response.data.length;
        for (var i = 0; i <= length - 1; i++) {
          if (response.data[i].state == 0) this.invitationList.push(response.data[i]);
          else this.invitationList_answered.push(response.data[i]);
        }
      }).catch((error) => {
        console.log(error);
      });
    },

    getUsersFromRealUsername() {
      this.$axios.get('/invite/realUsername', {
        params: {
          realUsername: this.search
        }
      }).then((response) => {
        console.log(response);
        console.log(response.data);
        this.targetUserList = response.data;
      }).catch((error) => {
        console.log(error);
      });
    },

    invite() {
      if (this.generateInvitation().length != 0) {
        this.$axios.post('/invite', this.generateInvitation()
        ).then((response) => {
          console.log(response);
          console.log(response.data);

        }).catch((error) => {
          console.log(error);
        });
      }
    },

    generateInvitation() {
      var list = new Array(0);
      console.log(this.checkList);
      console.log(this.targetUserList);
      for (var id in this.checkList) {
        for (var user in this.targetUserList) {
          if (this.checkList[id] == this.targetUserList[user].id) {
            var temp = {};
            temp["receiver"] = this.targetUserList[user].username;
            temp["receiverName"] = this.targetUserList[user].realUsername;
            temp["meetingId"] = this.currentMeetingId;
            list.push(temp);
          }
        }
      }
      console.log(list);
      return list;
    },

    answerInvitation(myAttitude) {
      this.$axios.post('/invite/agreeOrRefuse', {
        attitude: myAttitude,
        meetingId: this.currentMeetingId,
        topics: this.selectedTopics
      }).then((response) => {
        console.log(response);
        console.log(response.data);
        this.operationAvailable = true;
        this.changeContent(5);
      }).catch((error) => {
        console.log(error);
        this.operationAvailable = true;
        this.changeContent(5);
      });
    },

    getCondition(condition) {
      switch (condition) {
        case 0:
          return "待审核";
          break;
        case 1:
          return "审核通过";
          break;
        case 2:
          return "可投稿";
          break;
        case 3:
          return "审稿中";
          break;

        case 4:
          return "已发布第一次结果";
          break;
        case 5:
          return "未通过审核";
          break;
        case 6:
          return "已发布最终结果";
          break;

      }
    },

    getContributionConditionString(meetingId, contributionId) {
      console.log(" 000dada meeting is ", meetingId)
      this.$axios.post('/isContributionAudited/' + meetingId + '/' + contributionId, {}).then((response) => {
        console.log(response);
        console.log(response.data);
        if (response.data.message == 1) this.contributionConditionString = "已审核";
        else this.contributionConditionString = "未审核";
      }).catch((error) => {
        console.log(error);
      });
      // return "未审核";
    },

    getContributionCondition(meetingId, contributionId) {
      this.$axios.post('/isContributionAudited/' + meetingId + '/' + contributionId, {}).then((response) => {
        console.log(response);
        console.log(response.data);
        if (response.data.message == 1) this.contributionCondition = true;
        else this.contributionCondition = false;
      }).catch((error) => {
        console.log(error);
      });
      // return true;
    },


    toPreview(contributionId) {
      this.$router.push({name: 'PDFPreview', params: {contributionId: contributionId}});
    },

    generateIndex() {
      this.index++
      return this.index - 1;
    },

    changeContent(content) {
      this.isPCmember = this.myInvitation = this.meeting = this.sendInvitation = this.receiveInvitation = this.isChair = this.isAvailable = this.isAuthor = this.toContribute = this.changeContribution = false;
      switch (content) {
        case 1:
          this.getAllAvailableMeeting();
          this.meeting = true;
          this.isAvailable = false;
          this.toContribute = true;
          this.currentMeetingList = this.availableList;
          break;
        case 2:
          this.getAllMyMeeting();
          this.meeting = true;
          this.isChair = true;
          this.currentMeetingList = this.chairList;
          break;
        case 3:
          this.getMeetingAsPCmember();
          this.meeting = true;
          this.currentMeetingList = this.PCMemberList;
          this.isPCmember = true;
          break;
        case 4:
          this.getMeetingAsAuthor();
          this.meeting = true;
          this.isAuthor = true;
          this.currentMeetingList = this.authorList;
          this.changeContribution = true;
          break
        case 5:
          this.getInvitationList();
          this.receiveInvitation = true;
          break
        case 6:
          this.getMyInvitationList();
          this.myInvitation = true;
          break
        case 7:
          this.targetUserList = [];
          this.sendInvitation = true;
          break;
      }
    },

    openContribute() {
      this.$axios.post('/openContribute/' + this.currentMeetingId + '/2', {}).then((response) => {
        console.log(response);
        console.log(response.data);

      }).catch((error) => {
        console.log(error);
      });
    },

    startReview(strategy) {
      this.$axios.post('/startReview/' + this.currentMeetingId + '/' + strategy, {}).then((response) => {
        console.log(response);
        console.log(response.data);
        this.$message(response.data.message);
        this.operationAvailable = true;
        this.changeContent(2);
      }).catch((error) => {
        console.log(error);
      });
    },

    finishReview() {
      this.$axios.post('/startReview/finishReview/' + this.currentMeetingId, {}).then((response) => {
        console.log(response);
        console.log(response.data);
        this.$message(response.data.message);

      }).catch((error) => {
        console.log(error);
      });
    },
    finalFinishReview() {
      this.$axios.post('/releaseFinalResult/' + this.currentMeetingId, {}).then((response) => {
        console.log(response);
        console.log(response.data);
        this.$message(response.data.result);
        console.log("发布最终结果");
      }).catch((error) => {
        console.log(error);
      });
    },

    getReview(contribution, i) {
      switch (i) {
        case 1:
          return {confidence: contribution.confidence1, score: contribution.score1, comment: contribution.comment1};
        case 2:
          return {confidence: contribution.confidence2, score: contribution.score2, comment: contribution.comment2};
        case 3:
          return {confidence: contribution.confidence3, score: contribution.score3, comment: contribution.comment3};
      }
    },

    getScoreString(i) {
      switch (i) {
        case -2:
          return "reject";
        case -1:
          return "weak reject";
        case 1:
          return "weak accept";
        case 2:
          return "accept";
      }
    },

    contributionDownload(contributionId, filename) {
      this.$axios({
        method: 'post',
        url: '/download/' + contributionId,
        responseType: 'blob'  //设置响应的数据类型为一个包含二进制数据的 Blob 对象，必须设置！！！
      }).then(response => {
        const blob = new Blob([response.data]);
        const link = document.createElement('a')//创建a标签
        link.download = filename//a标签添加属性
        link.style.display = 'none'
        link.href = URL.createObjectURL(blob)
        document.body.appendChild(link)
        link.click()//执行下载
        URL.revokeObjectURL(link.href) //释放url
        document.body.removeChild(link)//释放标签
      }).catch(function (error) {
        console.log(error);
      });
    },

    checkLogOut() {
      this.$alert('Are you sure to logout ?', 'Confirm', {
        confirmButtonText: 'OK',
        callback: action => {
          this.$message({
            type: 'info',
            message: `action: ${action}`
          });
          if (action == 'confirm') this.logoutAccount();
        }
      });
    },

    getUserName() {
      if (localStorage.getItem('userDetails') == undefined) return "null";
      else return JSON.parse(localStorage.getItem('userDetails')).username;
    }

  },

  mounted() {
    this.changeContent(1);
    this.changeContent(2);
    this.changeContent(3);
    this.changeContent(4);
  }
}

</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
@import url("https://unpkg.com/element-ui/lib/theme-chalk/index.css");

.router-link-active {
  text-decoration: none;
}

a {
  color: #2c3e50;
  text-decoration: none;
}
</style>
