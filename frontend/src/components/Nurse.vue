<template>
  <div>

  </div>
</template>

<script>
export default {
  name: "Nurse",
  methods:{
    fun(){

      this.$axios.post('/', {
      }).then(res=>{
        console.log(" ",res)

      }).catch(err=>{
        console.log(err)
      })

    }
  }
}
</script>

<style scoped>
@import url("https://unpkg.com/element-ui/lib/theme-chalk/index.css");
</style>
