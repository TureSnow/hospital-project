export function logout() {
  {
    this.$confirm('确认要退出账户？', 'Confirm', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'info',
      center: true
    }).then(() => {
      this.$store.commit("logout")
      // this.$message({
      //   type: 'success',
      //   message: '成功退出',
      //   duration: 2000
      // });
    }).catch((err) => {
      console.log(err)
      console.log("登出失败")
      // this.$message({
      //   type: 'info',
      //   message: '尚未退出',
      //   duration: 2000
      // });
    });
  }
}
export function   getAreaLevel(num) {
  // area_level enum('0','1','2','3','4') default '0',-- 0:isolation; 1:mild; 2:severe; 3:critical; 4:home
  switch (num) {
    case 0:
      return "隔离区";
    case 1:
      return "轻症病房"
    case 2:
      return "重症病房"
    case 3:
      return "危重症病房"
    case 4:
      return "康复出院"
  }
}
export  function getIllness_level(num) {
  // illness_level -- 0:health; 1:mild; 2:severe; 3:critical
  switch (num) {
    case 0:
      return "健康";
    case 1:
      return "轻症"
    case 2:
      return "重症"
    case 3:
      return "危重症"
  }
}
export function getLifeState(life_state,area_level) {
  // life_state enum('0','1','2')default '0', -- 0:healthy; 1:treating; 2:dead
  if(life_state===0&&area_level===4){
    return "康复出院"
  }
  else if(life_state===2&&area_level===4){
    return "病亡"
  }else {
    return "治疗中"
  }
}
