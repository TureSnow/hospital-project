<template>
  <div>
<!--    <div v-for="index in patient_list.length" :key="index">-->

<!--      &lt;!&ndash;      vue for循环下标从1开始！！！&ndash;&gt;-->
<!--      <el-row v-if="index%3===1">-->
<!--        <el-col :span="7" :offset="1" v-for="n in 3" :key="n" style="margin-bottom: 10px;">-->

<!--          <div v-if="index+n-1<=patient_list.length">-->
<!--            <el-card :body-style="{ padding: '0px' }">-->

<!--              <div style="padding: 14px;">-->
<!--                <div style="text-align: left">-->
<!--                  <div>姓名:{{patient_list[index].name}}</div>-->
<!--                  <div>年龄：{{patient_list[index].age}}</div>-->
<!--                  <div>病情评级：{{getIllnessLevel(patient_list[index].illness_level)}}</div>-->
<!--                  <div>生命状态：{{getLifeState(patient_list[index].life_state)}}</div>-->
<!--                  <div>性别：{{patient_list[index].gender}}</div>-->
<!--                </div>-->

<!--                <div class="">-->
<!--                  <el-tag>健康</el-tag>-->
<!--                  <el-dropdown>-->
<!--                    <span class="el-dropdown-link">更多<i class="el-icon-arrow-down el-icon&#45;&#45;right"></i></span>-->
<!--                    <el-dropdown-menu slot="dropdown">-->
<!--                      <el-dropdown-item>修改病情评级</el-dropdown-item>-->
<!--                      <el-dropdown-item>修改生命状态</el-dropdown-item>-->
<!--                      <el-dropdown-item ><span @click="detectionVisible=true">进行核酸检测</span> </el-dropdown-item>-->
<!--                    </el-dropdown-menu>-->
<!--                  </el-dropdown>-->

<!--                </div>-->
<!--              </div>-->
<!--            </el-card>-->
<!--          </div>-->
<!--        </el-col>-->
<!--      </el-row>-->
<!--    </div>-->
  </div>
</template>

<script>
export default {
  name: "user_card",
  data(){
    return{
      user_list:[]
    }
  },
  methods:{
    init(){
      //todo:添加路径
      this.$axios.post('/', {

      })
        .then(resp => {
            console.log(resp)
            if (resp.status === 200 ) {
              this.user_list=resp.data.user_list
            }
          }
        )
        .catch(error => {
          console.log(error)
        })
    }
  },
  created() {
    //todo:初始化
    // this.init();
  }

}
</script>

<style scoped>

</style>
