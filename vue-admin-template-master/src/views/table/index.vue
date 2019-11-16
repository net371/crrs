<template>
  <div class="app-container">
    <div>
      <el-form :inline="true"  class="demo-form-inline">
        <el-form-item label="用户名">
          <el-input v-model="username" placeholder="用户名"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="onSubmit">查询</el-button>
          <el-button type="primary" @click="handleAdd()">添加用户</el-button>
        </el-form-item>
      </el-form>
    </div>
    <el-table
      ref="filterTable"
      v-loading="listLoading"
      :data="slist"
      :default-sort = "{prop: 'id', order: 'descending'}"
      style="width: 100%"
      height="600"
      element-loading-text="Loading"
      border
      fit
      highlight-current-row
    >
      <el-table-column align="center"
        type="selection"
        width="55">
      </el-table-column>
      <el-table-column fixed align="center" label="ID" width="95">
        <template slot-scope="scope">
          {{ scope.$index }}
        </template>
      </el-table-column>
      <el-table-column
        prop="userName"
        sortable
        label="用户名"
        :filters="[{text: 'zmm', value: 'zmm'}, {text: 'wqq', value: 'wqq'}]"
        :filter-method="filterHandler"
      >
        <template slot-scope="scope">
          {{ scope.row.userName }}
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="160">
        <template slot-scope="scope">
          <el-button
            size="mini"
            @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
          <el-button
            size="mini"
            type="danger"
            @click="handleDelete(scope.$index, scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="currentPage"
      :page-sizes="[5, 10, 20, 50, 100]"
      :page-size="pageSize"
      layout="total, sizes, prev, pager, next, jumper"
      :total="totalItems">
    </el-pagination>
    <el-drawer
      :title="dialogTitle"
      :visible.sync="dialog"
      custom-class="demo-drawer"
      ref="drawer"
    >
      <div class="demo-drawer__content">
        <el-form ref="form" :model="form" :rules="loginRules" auto-complete="on">
          <el-form-item label="用户名" :label-width="formLabelWidth" prop="name">
            <el-input ref="name" v-model="form.name" auto-complete="on"></el-input>
          </el-form-item>
          <el-form-item label="密码" :label-width="formLabelWidth" prop="pwd">
            <el-input ref="pwd" v-model="form.pwd" auto-complete="on"></el-input>
          </el-form-item>
        </el-form>
        <div class="demo-drawer__footer">
          <el-button @click="dialog = false">取 消</el-button>
          <el-button type="primary" @click="handleClose" :drawerloading="drawerloading">{{ drawerloading ? '提交中 ...' : '确 定' }}</el-button>
        </div>
      </div>
    </el-drawer>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  data() {
      const validateUsername = (rule, value, callback) => {
          if (value.length < 1) {
              callback(new Error('请输入用户名！'))
          } else {
              callback()
          }
      }
      const validatePassword = (rule, value, callback) => {
          if (value.length < 6) {
              callback(new Error('请输入至少6位数密码！'))
          } else {
              callback()
          }
      }
    return {
      list: null,
      slist : null,
      listLoading: true,
      username: '',

      currentPage: 1,
      pageSize: 5,
      totalItems: 0,

      drawer: false,
      direction: 'rtl',
      dialog: false,
      dialogTitle: '',
      url: '',
      drawerloading: false,
      form: {
          id: '',
          name: '',
          pwd: ''
      },
      loginRules: {
          name: [{ required: true, trigger: 'blur', validator: validateUsername }],
          pwd: [{ required: true, trigger: 'blur', validator: validatePassword }]
      },
      formLabelWidth: '80px'
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    fetchData() {
      this.listLoading = true
      const param = new URLSearchParams();
      param.append("curr", this.currentPage);
      param.append("nums", this.pageSize);
      param.append("userName", this.username);
      axios
          .post(process.env.VUE_APP_BASE_API + '/sys/findlist',param)
          .then(response => {
              this.list = response.data.data
              this.totalItems = response.data.count;
              this.setSlist(this.list)
              this.listLoading = false
          })
          .catch(function (error) { // 请求失败处理
              console.log(error)
          });
    },
    filterHandler(value, row, column) {
        const property = column['property'];
        return row[property] === value;
    },
    handleAdd() {
        this.dialog = true
        this.form.id = ''
        this.form.name = ''
        this.form.pwd = ''
        this.url = process.env.VUE_APP_BASE_API + '/sys/creadUser'
        this.dialogTitle = "添加"
    },
    handleEdit(index, row) {
        this.dialog = true
        this.form.id = row.id
        this.form.name = row.userName
        this.form.pwd = row.passWord
        this.url = process.env.VUE_APP_BASE_API + '/sys/Edit'
        this.dialogTitle = "编辑"
        console.log(index, row);
    },
    handleDelete(index, row) {
        this.url = process.env.VUE_APP_BASE_API + '/sys/DelUser'
        this.$confirm('确定要删除该用户吗？',{
            confirmButtonText: '确定',
            cancelButtonText: '取消'
        })
            .then(_ => {
                this.listLoading = true;
                const param = new URLSearchParams();
                param.append("id", row.id);
                axios
                    .post(this.url,param)
                    .then(response => {
                        this.listLoading = false;
                        this.fetchData()
                        this.$message({
                            showClose: true,
                            message: response.data.msg,
                            type: 'success'
                        });
                    })
                    .catch(function (error) { // 请求失败处理
                        this.$message({
                            showClose: true,
                            message: '请求失败！',
                            type: 'warning'
                        });
                    });
            })
            .catch(_ => {});
    },
    handleSizeChange(val) {
        this.pageSize = parseInt(`${val}`)
        this.fetchData()
    },
    handleCurrentChange(val) {
        this.currentPage = parseInt(`${val}`)
        this.fetchData()
    },
    setSlist(arr) {
        this.slist = JSON.parse(JSON.stringify(arr));
    },
    onSubmit() {
        this.currentPage = 1
        this.fetchData()
        /*
        //前端检索
        this.slist= []
        let arr = []
        this.list.forEach((value, index) => {
            if(value.userName){
                if(value.userName.indexOf(this.username)>=0){
                    arr.push(value)
                }
            }
        });
        this.setSlist(arr);*/
    },
    handleClose(done) {
        this.$refs.form.validate(valid => {
            if (valid) {
                this.$confirm('确定要'+this.dialogTitle+'该用户吗？',{
                    confirmButtonText: '确定',
                    cancelButtonText: '取消'
                })
                    .then(_ => {
                        this.drawerloading = true;
                        const param = new URLSearchParams();
                        param.append("id", this.form.id);
                        param.append("userName", this.form.name.trim());
                        param.append("passWord", this.form.pwd);
                        axios
                            .post(this.url,param)
                            .then(response => {
                                this.drawerloading = false;
                                this.dialog = false;
                                this.fetchData()
                                this.$message({
                                    showClose: true,
                                    message: response.data.msg,
                                    type: 'success'
                                });
                            })
                            .catch(function (error) { // 请求失败处理
                                this.$message({
                                    showClose: true,
                                    message: '请求失败！',
                                    type: 'warning'
                                });
                            });
                    })
                    .catch(_ => {});
            } else {
                return false
            }
        })
    }
  }
}
</script>
<style lang="scss">
  .el-drawer__body{
    padding: 20px;
    .demo-drawer__content{
      display: flex;
      flex-direction: column;
      height: 100%;
    }
    .el-form{
      flex: 1;
    }
    .demo-drawer__footer{
      display: flex;
    }
    .demo-drawer__footer button {
      flex: 1;
    }
  }
</style>
