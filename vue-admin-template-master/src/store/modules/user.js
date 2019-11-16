import { login, logout, getInfo } from '@/api/user'
import { getToken, setToken, removeToken } from '@/utils/auth'
import { filterAsyncRouter, resetRouter, constantRoutes } from '@/router'
import axios from 'axios'

const state = {
  token: getToken(),
  name: '',
  avatar: '',
  addRouters: '',
  routers: '',
  roles: ''
}

const mutations = {
  SET_TOKEN: (state, token) => {
    state.token = token
  },
  SET_NAME: (state, name) => {
    state.name = name
  },
  SET_AVATAR: (state, avatar) => {
    state.avatar = avatar
  },
  SET_ADDROUTERS: (state, addRouters) => {
    state.addRouters = addRouters
    state.routers = constantRoutes.concat(addRouters)
    // constantRoutes = constantRoutes.concat(addRouters)
  },
  SET_ROLES: (state, roles) => {
    state.roles = roles
  }
}

const actions = {
  // user login
  login({ commit }, userInfo) {
    const { username, password } = userInfo
    return new Promise((resolve, reject) => {
      const param = new URLSearchParams();
      param.append("username", username.trim());
      param.append("password", password);
      axios
        .post(process.env.VUE_APP_BASE_API + '/sys/loginUser',param)
        .then(response => {
          if(response.data.msg == '登陆成功!'){
            commit('SET_TOKEN', '1')
            setToken('1')
            resolve()
          } else {
            alert('登录失败！')
            reject()
          }
        })
        .catch(function (error) { // 请求失败处理
          alert('请求失败！')
          reject()
        });
    })
  },

  // get user info
  getInfo({ commit, state }) {
    return new Promise((resolve, reject) => {
      axios
        .post(process.env.VUE_APP_BASE_API + '/sys/findsession',{
        })
        .then(response => {
          const { data } = response

          if (!data) {
            reject('Verification failed, please Login again.')
          }

          commit('SET_NAME', data.data.userName)
          commit('SET_AVATAR', 'https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif')
          var json = [
            {
              "path": "/",
              "component": "Layout",
              "redirect": "/dashboard",
              "children": [{
                "name": "Dashboard",
                "path": "dashboard",
                "component": "/dashboard/index",
                "meta": {
                  "icon": "dashboard",
                  "title": "首页"
                },
              }]
            },
            {
              "name": "Example",
              "path": "/example",
              "redirect": '/example/table',
              "component": "Layout",
              "meta": {
                  "icon": "example",
                  "title": "系统管理"
              },
              "children": [
                {
                  "name": "Table",
                  "path": "table",
                  "component": "/table/index",
                  "meta": {
                    "icon": "table",
                    "title": "用户管理"
                  }
                },
                {
                  "name": "Tree",
                  "path": "tree",
                  "component": "/tree/index",
                  "meta": {
                    "icon": "tree",
                    "title": "树形结构"
                  }
                }
              ]
            },
            {
              "path": "/form",
              "component": "Layout",
              "children": [{
                "name": "form",
                "path": "index",
                "component": "/form/index",
                "meta": {
                  "icon": "form",
                  "title": "表单"
                },
              }]
            },
            // 404 page must be placed at the end !!!
            {
              "path": "*",
              "redirect": "/404",
              "hidden": "true",

            }
          ]
          const accessedRouters = filterAsyncRouter(json)
          commit('SET_ADDROUTERS', accessedRouters)
          commit('SET_ROLES', 'zmm')
          resolve(data)
        })
        .catch(function (error) { // 请求失败处理
          reject(error)
        });
    })
  },

  // user logout
  logout({ commit, state }) {
    return new Promise((resolve, reject) => {
      axios
        .post(process.env.VUE_APP_BASE_API + '/sys/logOut',{
          params: {
          }
        })
        .then(response => {
          commit('SET_TOKEN', '')
          removeToken()
          resetRouter()
          state.name = null// 将用户名去掉，如果不去掉，退出登陆后，再重新登陆的情况下，将不再执行getInfo
          resolve()
        })
        .catch(function (error) { // 请求失败处理
          reject(error)
        });
    })
  },

  // remove token
  resetToken({ commit }) {
    return new Promise(resolve => {
      commit('SET_TOKEN', '')
      removeToken()
      resolve()
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}

