<template>
  <div>
    <!--    展示护士长列表-->
    <el-table
      :data="head_nurse_list"
      stripe
      style="width: 90%">

      <el-table-column
        prop="name"
        label="姓名">
      </el-table-column>
      <el-table-column
        prop="area"
        label="负责区域">
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
export default {
  name: "head_nurse_card",
  data(){
    return{
      head_nurse_list:[]
    }
  },
  methods:{

  },
  created() {
    // var head1={name:"head001",area:1}
    // var head2={name:"head001",area:2}
    // var head3={name:"head001",area:3}
    // var head4={name:"head001",area:0}
    // this.head_nurse_list=[head1,head2,head3,head4]
    this.$axios.get('/user/doctor/head', {
    }).then(res=>{
      console.log(res)

      this.head_nurse_list.push(res.data.data)
      this.head_nurse_list.forEach(item =>
        // console.log(this.getAreaLevel())
        item.area=this.$myUtil.myFunc.getAreaLevel(parseInt(item.area))
      )
      console.log("初始化护士长为",this.head_nurse_list)
    }).catch(err=>{
      console.log(err)
    })
  }
}

</script>

<style scoped>

</style>
