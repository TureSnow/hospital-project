<template>
  <el-container>


        <el-table
          :data="unread_info_list"
          stripe
          style="width: 100%">
          <el-table-column
            prop="content"
            label="未读信息"
          >
          </el-table-column>
          <el-table-column

            label="操作">
            <template slot-scope="scope">
              <el-button @click="hasRead(scope.$index)" type="text" size="small">设为已读</el-button>

            </template>
          </el-table-column>
        </el-table>

        <el-table
          :data="has_read_info_list"
          stripe
          style="width: 100%">
          <el-table-column
            prop="content"
            label="历史信息"
          >
          </el-table-column>
          <el-table-column

            label=" ">
            <template slot-scope="scope">
              <el-button type="text" size="small">已读</el-button>

            </template>
          </el-table-column>
        </el-table>



  </el-container>
</template>

<script>
export default {
  name: "MessageCard",
  data() {
    return {
      unread_info_list: [],
      has_read_info_list: []
    }

  },
  methods: {
    hasRead(num) {
      console.log("hasRead " + num)
      console.log('当前已被读取的消息为')
      console.log(this.unread_info_list[num])
      this.$axios.post('/user/unread2read', {
        notifyId: this.unread_info_list[num].id
      }).then(res => {
        console.log(res)
        this.get_unread_message()
        this.get_has_read_message()
      }).catch(err => {
        console.log(err)
      })
    },
    get_unread_message() {
      this.$axios.get('/user/unreadNotify', {}).then(res => {
        console.log(res)
        this.unread_info_list = res.data.data
      }).catch(err => {
        console.log(err)
      })
    },
    get_has_read_message() {
      this.$axios.get('/user/readNotify', {}).then(res => {
        console.log(res)
        this.has_read_info_list = res.data.data
      }).catch(err => {
        console.log(err)
      })
    }
  },
  created() {
    // var info1={content:"aaaaaa",id:1}
    // var info2={content:"bbbbbb",id:2}
    // var info3={content:"cccccc",id:3}
    // this.infoList=[info1,info2,info3]
    console.log("进入通知界面")
    this.get_unread_message()
    this.get_has_read_message()
  }
}
</script>

<style scoped>
@import url("https://unpkg.com/element-ui/lib/theme-chalk/index.css");

</style>
