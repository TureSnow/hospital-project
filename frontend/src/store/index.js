import Vue from 'vue'
import Vuex from 'vuex'
Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    token: localStorage.getItem('token') || null,
    user: JSON.parse(localStorage.getItem('user')) || null
  },
  mutations: {
    login(state, data){
      console.log("开始保存用户信息",data)

      var user={id:data.id,role:parseInt(data.role),area:parseInt(data.area),username:data.username}
      localStorage.setItem('token', data.token)
      localStorage.setItem('user', JSON.stringify(user))
      state.user = user
      state.token = data.token
      console.log("登录后用户状态为",state.user)
    },
    logout(state) {
      //移除缓存
      localStorage.removeItem('token')
      localStorage.removeItem('user')
      state.user = null
      state.token = null

    }
  },
  actions: {
  }
})
