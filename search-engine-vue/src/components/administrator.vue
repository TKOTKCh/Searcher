<template>
  <div style="width: 100%;height: 100%" >
<!--    <div class="backsite">  <img   src="~@/assets/admin_back.png" ></div>-->
    <div style="background-color: #263238;height: 5%;padding: 20px">
      <img  src="~@/assets/dark_title.png" style="width:150px;float: left;margin-left: 30px">
      <h3 style="text-align: right;align-content: center;color: white;font-size: 30px">后台管理系统</h3>
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
      <el-menu-item index="0" @click="curpage=0">
        <i class="el-icon-menu"></i>
        <span slot="title">首页</span>
      </el-menu-item>
      <el-submenu index="1">
        <template slot="title">
          <i class="el-icon-location"></i>
          <span>政策管理</span>
        </template>
        <el-menu-item-group>
          <el-menu-item @click="curpage=1" index="2">政策查改</el-menu-item>
          <el-menu-item @click="curpage=2" index="3">新增政策</el-menu-item>
        </el-menu-item-group>
      </el-submenu>
      <el-menu-item index="4" @click="curpage=3">
        <i class="el-icon-menu"></i>
        <span slot="title">用户管理</span>
      </el-menu-item>
<!--      <el-menu-item index="3" >-->
<!--        <i class="el-icon-document"></i>-->
<!--        <span slot="title">导航三</span>-->
<!--      </el-menu-item>-->
    </el-menu>
      </el-col>
      <el-col :span="21" style="margin-left: 10px">

        <div v-if="curpage==0" style="width: 100%;padding: 20px" >
          <div class="part1">
            <div  style="display: flex;margin-top: 20px;margin-bottom: 40px">
              <h3 style="font-size: 24px">
                欢迎回来！ 管理员
              </h3>
            </div>
            <div class="line2" style="border-top: solid 2px #eee;border-bottom:solid 2px #eee;height: 100px;align-content: center">
              <el-row style="margin: 20px">
                <el-col span="5" style="display: flex;border-right: solid 2px #eee;margin-left: 10px;margin-right: 10px">
                    <div style="width: 50px; height: 52px" >
                      <img class="img" src="~@/assets/bob_1.png" draggable="false" style="display: block; width: 50px; height: 52px;">
                    </div>
                    <div style="margin-left: 20px">
                      <h4  style="margin-bottom: 10px">收录政策数：</h4>
                      <h2 style="float: left">
                        157902
                      </h2>
                    </div>
                </el-col>
                <el-col span="5"style="display: flex;border-right: solid 2px #eee;margin-left: 10px;margin-right: 10px">
                  <div style="width: 50px; height: 52px" >
                    <img class="img" src="~@/assets/bob_2.png" draggable="false" style="display: block; width: 50px; height: 52px;">
                  </div>
                  <div style="margin-left: 20px">
                    <h4  style="margin-bottom: 10px">注册用户数：</h4>
                    <h2 style="float: left">
                      201
                    </h2>
                  </div>

                </el-col>
                <el-col span="5" style="display: flex;border-right: solid 2px #eee;margin-left: 10px;margin-right: 10px">
                  <div style="width: 50px; height: 52px" >
                    <img class="img" src="~@/assets/bob_3.png" draggable="false" style="display: block; width: 50px; height: 52px;">
                  </div>
                  <div style="margin-left: 20px">
                    <h4  style="margin-bottom: 10px">站点今日点击量：</h4>
                    <h2 style="float: left">
                      114
                    </h2>
                  </div>
                </el-col>
                <el-col span="5"style="display: flex;" >
                  <div style="width: 50px; height: 52px" >
                    <img class="img" src="~@/assets/bob_4.png" draggable="false" style="display: block; width: 50px; height: 52px;">
                  </div>
                  <div style="margin-left: 20px">
                    <h4  style="margin-bottom: 10px">政策总点击量：</h4>
                    <h2 style="float: left">
                      114514
                    </h2>
                  </div>
                </el-col>
              </el-row>
            </div>
            <div  style="display: flex;margin-top: 20px;margin-bottom: 40px">
              <h3 style="font-size: 24px">
                这里可以有一个流量折线
              </h3>
            </div>
            <span>这里有图吗</span>
          </div>
        </div>


        <div v-if="curpage==1" style="width: 100%;padding: 20px">
          政策查改
        </div>

        <div v-if="curpage==2" style="width: 100%;padding: 20px">
          新增政策
        </div>

        <div v-if="curpage==3" style="width: 100%;padding: 20px">
          <div class="part1">
            <div  style="display: flex;margin-top: 20px;margin-bottom: 40px">
              <h3 style="font-size: 24px">
                用户管理
              </h3>
            </div>
            <div class="line2" style="border-top: solid 2px #eee;">
              <div>
                <el-table
                    :data="tableData.filter(data => !search || data.username.toLowerCase().includes(search.toLowerCase()))"
                    style="width: 80% ">
                  <el-table-column
                      label="ID"
                      prop="id">
                  </el-table-column>
                  <el-table-column
                      label="姓名"
                      prop="username">
                  </el-table-column>
                  <el-table-column
                      label="性别"
                      prop="sex">
                  </el-table-column>
                  <el-table-column
                      label="职业"
                      prop="career">
                  </el-table-column>
                  <el-table-column
                      label="地区"
                      prop="address">
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
                          @click="handleEdit(scope.$index, scope.row)">编辑/查看</el-button>
                      <el-button
                          size="mini"
                          type="danger"
                          @click="handleDelete(scope.$index, scope.row)">删除</el-button>
                    </template>
                  </el-table-column>
                </el-table>
              </div>
            </div>

          </div>




        </div>

      </el-col>
    </div>
  </div >



</template>

<script>
import axios from "axios";

export default {
  name: "vue",

  data(){
    return{
      like: true,
      value1: 4154.564,
      value2: 2222,
      title: '今年的增长',

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
      curpage:0,
    }
  },
  created() {
    this.bodyScale()
    this.getUser()
  },
  methods: {
    getUser(){
      let outer = this;
      axios
          .get(
              "http://localhost:8081/user/list"
          )
          .then((response) => (outer.tableData = response.data));

    },

    bodyScale() {
      let t = window.devicePixelRatio   // 获取下载的缩放 125% -> 1.25    150% -> 1.5
      if (!!window.ActiveXObject || "ActiveXObject" in window) {
        if (t != 1) {
          // 如果在笔记本中用IE浏览器打开 则弹出提示
          alert('您的设备对显示进行放大导致页面显示不完全,请调整后打开/或用其他浏览器')
        }
      } else {
        if (t != 1) {   // 如果进行了缩放，也就是不是1
          let c = document.querySelector('body')
          c.style.zoom = -0.62 * t + 1.55;   // 就去修改页面的缩放比例，这个公式我自己算的，不准确，勉强。
        }
      }
    },
    handleEdit(index, row) {
      console.log(index, row);
    },
    handleDelete(index, row) {
      console.log(index);
      console.log(row);
      axios
          .delete(
              "http://localhost:8081/user/delete/" + row.id
          )
          .then((response) => (console.log(response)));
      window.location.reload();
    }
  },
}



</script>

<style scoped>

.like {
  cursor: pointer;
  font-size: 25px;
  display: inline-block;
}

.part1{
  text-align:center;
  border:solid 2px #eee;
padding:10px 40px;
/*background:#dddddd;*/
border-radius:6px;
  width: 80%;
  height: 700px;
}


.backsite{
  position: absolute;
  bottom: 0%;
  margin: 0px;
  z-index: -1;
  overflow: hidden;
  width: 100%

}

.header {
  /*background-color: #B3C0D1;*/
  display: flex;
  position: absolute;
  width: 30%;
  height: 5px;
}

.el-table{
  /*border: 2px solid #4e6ef2;*/
  margin-top: 20px;

}



</style>