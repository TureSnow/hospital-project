<template>
<div style="margin:50px 0;">
  <el-button type="primary" size="mini" @click="prePage()">上一页</el-button>
  <div style="margin:0 200px;overflow:hidden;">
    <pdf 
			:src="src"
      @num-pages="pageCount=$event"
      :page="currentPage"
	  ></pdf>
  </div>
  <el-button type="primary" size="mini" @click="nextPage()">下一页</el-button>
</div>
</template>

<script>
  import pdf from 'vue-pdf'
  export default {
    name: "PDFPreview",
    data(){
      return{
        currentPage:1,
        pageCount:0,
        src:"",
        contributionId:this.$route.params.contributionId,
      }
    },

    components: {
      pdf
    },

    methods: {
      // 初始化获取pdf文件
      getPdfCode () {
        console.log(this.src);
        this.$axios({
          method: 'post',
          url: '/review/'+this.contributionId,
          responseType: 'blob'  //设置响应的数据类型为一个包含二进制数据的 Blob 对象，必须设置！！！
        }).then(response => {
        // this.$axios.post('/review/'+this.contributionId,{
        // }).then((response) => {
          this.src  = this.getObjectURL(response.data);
          console.log(this.src)
        }).catch(function (error) {
          console.log(error);
        });
      },

      // 将返回的流数据转换为url
      getObjectURL(file) {
        let url = null;
        if (window.createObjectURL != undefined) { // basic
          console.log("1");
          url = window.createObjectURL(file);
        } 
        else if (window.webkitURL != undefined) { // webkit or chrome
          try {
            console.log("2");
            url = window.webkitURL.createObjectURL(file);
          } catch (error) {

          }
        } 
        else if (window.URL != undefined) { // mozilla(firefox)
          try {
            console.log("3");
            url = window.URL.createObjectURL(file);
          } catch (error) {
          }
        }
        return url;
      },

      prePage(){
        this.currentPage=this.currentPage==1?1:this.currentPage-1;
      },

      nextPage(){
        this.currentPage=this.currentPage==this.pageCount?this.pageCount:this.currentPage+1;
      },
      
    },
    mounted()
    {

    },

    created()
    {
      this.getPdfCode();
    },
  }

</script>

<style scoped>
  @import url("https://unpkg.com/element-ui/lib/theme-chalk/index.css");
  
</style>
