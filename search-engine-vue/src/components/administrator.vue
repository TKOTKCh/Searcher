<template>
  <div style="width: 100%;height: 100%" >
    <div style="background-color: #263238;height: 5%;padding: 20px">
      <h3 style="text-align: left;align-content: center;color: white;font-size: 30px">后台管理系统</h3>
    </div>
    <div style="height: 100%;display:flex">
      <el-col :span="3" >
        <el-menu
        style="height: 100vh"
        default-active="0"
        class="el-menu-vertical-demo"
        @open="handleOpen"
        @close="handleClose"
        background-color="#545c64"
        text-color="#fff"
        active-text-color="#ffd04b">
      <el-submenu index="1">
        <template slot="title">
          <i class="el-icon-location"></i>
          <span>政策管理</span>
        </template>
        <el-menu-item-group>
          <el-menu-item @click="curpage=1" index="0">政策查改</el-menu-item>
          <el-menu-item @click="curpage=2" index="1">新增政策</el-menu-item>
        </el-menu-item-group>
      </el-submenu>
      <el-menu-item index="2" @click="curpage=3">
        <i class="el-icon-menu"></i>
        <span slot="title">用户管理</span>
      </el-menu-item>
<!--      <el-menu-item index="3" >-->
<!--        <i class="el-icon-document"></i>-->
<!--        <span slot="title">导航三</span>-->
<!--      </el-menu-item>-->
    </el-menu>
      </el-col>
      <el-col :span="21" style="margin-left: 40px">

        <div v-if="curpage==1">
          政策查改
        </div>

        <div v-if="curpage==2">
          新增政策
        </div>

        <div v-if="curpage==3" style="width: 100%">
          <el-row>
            <h3 style="float: left; margin-top: 20px">用户管理</h3>
          </el-row>

          <div>
            <el-table
              :data="tableData.filter(data => !search || data.name.toLowerCase().includes(search.toLowerCase()))"
              style="width: 80% ">
            <el-table-column
                label="Date"
                prop="date">
            </el-table-column>
            <el-table-column
                label="Name"
                prop="name">
            </el-table-column>
            <el-table-column
                align="right">
              <template slot="header" slot-scope="scope">
                <el-input
                    v-model="search"
                    size="mini"
                    placeholder="输入关键字搜索"/>
              </template>
              <template slot-scope="scope">
                <el-button
                    size="mini"
                    @click="handleEdit(scope.$index, scope.row)">Edit</el-button>
                <el-button
                    size="mini"
                    type="danger"
                    @click="handleDelete(scope.$index, scope.row)">Delete</el-button>
              </template>
            </el-table-column>
          </el-table></div>

        </div>

      </el-col>
    </div>
  </div >



</template>

<script>
export default {
  name: "vue",

  data(){
    return{
      tableData: [{
        date: '2016-05-02',
        name: '王小虎',
        address: '上海市普陀区金沙江路 1518 弄'
      }, {
        date: '2016-05-04',
        name: '王小虎',
        address: '上海市普陀区金沙江路 1517 弄'
      }, {
        date: '2016-05-01',
        name: '王小虎',
        address: '上海市普陀区金沙江路 1519 弄'
      }, {
        date: '2016-05-03',
        name: '王小虎',
        address: '上海市普陀区金沙江路 1516 弄'
      }],
      search: '',
      curpage:1,
    }
  },
  methods: {
    handleEdit(index, row) {
      console.log(index, row);
    },
    handleDelete(index, row) {
      console.log(index, row);
    }
  },
}



</script>

<style scoped>
.header {
  /*background-color: #B3C0D1;*/
  display: flex;
  position: absolute;
  width: 30%;
  height: 5px;
}

.el-table{
  border: 2px solid #4e6ef2;
  margin-top: 20px;

}


</style>