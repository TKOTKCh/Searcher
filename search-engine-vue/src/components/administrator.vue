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
        background-color="#545c64"
        text-color="#fff"
        active-text-color="#ffd04b">
      <el-menu-item index="0" @click="curpage=0">
        <i class="el-icon-menu"></i>
        <span slot="title">首页</span>
      </el-menu-item>
<!--      <el-submenu index="1">-->
<!--        <template slot="title">-->
<!--          <i class="el-icon-location"></i>-->
<!--          <span>政策管理</span>-->
<!--        </template>-->
<!--        <el-menu-item-group>-->
<!--          <el-menu-item @click="curpage=1" index="2">政策查改</el-menu-item>-->
<!--          <el-menu-item @click="curpage=2" index="3">新增政策</el-menu-item>-->
<!--        </el-menu-item-group>-->
<!--      </el-submenu>-->
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

        <div v-show="curpage==0" style="width: 100%;padding-top: 20px" >
          <div class="part1">
            <div  style="display: flex;margin-top: 20px;margin-bottom: 40px">
              <h3 style="font-size: 24px">
                欢迎回来！ 管理员
              </h3>
            </div>
            <div class="line2" style="border-top: solid 2px #eee;border-bottom:solid 2px #eee;height: 100px;align-content: center">
              <el-row style="margin: 20px">
                <el-col :span="5" style="display: flex;border-right: solid 2px #eee;margin-left: 10px;margin-right: 10px">
                    <div style="width: 50px; height: 52px" >
                      <img class="img" src="~@/assets/bob_1.png" draggable="false" style="display: block; width: 50px; height: 52px;">
                    </div>
                    <div style="margin-left: 20px">
                      <h4  style="margin-bottom: 10px">收录政策数：</h4>
                      <h2 style="float: left">
                        {{ num_policy }}
                      </h2>
                    </div>
                </el-col>
                <el-col :span="5"style="display: flex;border-right: solid 2px #eee;margin-left: 10px;margin-right: 10px">
                  <div style="width: 50px; height: 52px" >
                    <img class="img" src="~@/assets/bob_2.png" draggable="false" style="display: block; width: 50px; height: 52px;">
                  </div>
                  <div style="margin-left: 20px">
                    <h4  style="margin-bottom: 10px">注册用户数：</h4>
                    <h2 style="float: left">
                      {{num_user}}
                    </h2>
                  </div>

                </el-col>
                <el-col :span="5" style="display: flex;border-right: solid 2px #eee;margin-left: 10px;margin-right: 10px">
                  <div style="width: 50px; height: 52px" >
                    <img class="img" src="~@/assets/bob_3.png" draggable="false" style="display: block; width: 50px; height: 52px;">
                  </div>
                  <div style="margin-left: 20px">
                    <h4  style="margin-bottom: 10px">站点今日点击量：</h4>
                    <h2 style="float: left">
                      {{today_click}}
                    </h2>
                  </div>
                </el-col>
                <el-col :span="5"style="display: flex;" >
                  <div style="width: 50px; height: 52px" >
                    <img class="img" src="~@/assets/bob_4.png" draggable="false" style="display: block; width: 50px; height: 52px;">
                  </div>
                  <div style="margin-left: 20px">
                    <h4  style="margin-bottom: 10px">政策总点击量：</h4>
                    <h2 style="float: left">
                      {{total_click}}
                    </h2>
                  </div>
                </el-col>
              </el-row>
            </div>

            <div  style="display: flex;margin-top: 20px;margin-bottom: 40px">
              <h3 style="font-size: 24px">
                近七日站点流量图
              </h3>
            </div>
            <div id="main" style="margin-top:50px;width: 80%;height: 400px;"></div>
<!--            <span>这里有图吗</span>-->
          </div>
        </div>


        <div v-show="curpage==1" style="width: 100%;padding: 20px">
          政策查改
        </div>

        <div v-show="curpage==2" style="width: 100%;padding: 20px">
          新增政策
        </div>

        <div v-show="curpage==3" style="width: 100%;padding: 20px">
          <div class="part1">
            <div  style="display: flex;margin-top: 20px;margin-bottom: 40px">
              <h3 style="font-size: 24px">
                用户管理
              </h3>
            </div>
            <div class="line2" style="border-top: solid 2px #eee;">
              <div>
                <el-table
                    :key="Math.random()"

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
                      width="200px"
                      align="right">
                    <template slot="header" slot-scope="scope">
                      <el-input
                          v-model="search"
                          size="mini"
                          placeholder="输入关键字搜索"/>
                    </template>
                    <template slot-scope="scope">
<!--                      <el-button-->
<!--                          size="mini"-->
<!--                          @click="handleEdit(scope.$index, scope.row)">编辑/查看</el-button>-->
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
      tableKey:0,
      like: true,

      tableData: [],
      search: '',
      curpage:'',
      num_policy:'',
      num_user:'',
      today_click:'',
      total_click:'',
      sevendays:[],

    }
  },
  created() {
    this.user = JSON.parse(window.localStorage.getItem("access"));
    if (this.user != null) {
      this.check = true;
      console.log(this.user)
    }
    // this.bodyScale()
    this.getUser()
    this.getStatistic()
  },
  mounted() {
    // this.drawChart()
    window.onbeforeunload = function (e) {
      const storage = window.localStorage;
      storage.clear()}
  },
  watch:{

    sevendays:{
      handler(){
        this.drawChart()
      }
    }
  },
  methods: {
    drawChart() {
      let date=[];
      let num=[];
      console.log(this.sevendays)
      for(var i=0;i<7;i++){
        date.push(this.sevendays[i].key)
        num.push(this.sevendays[i].value)
      }

      // 基于准备好的dom，初始化echarts实例  这个和上面的main对应
      let myChart = this.$echarts.init(document.getElementById("main"));
      // 指定图表的配置项和数据
      let option = {
        title: {
          text: "政策点击量",
        },
        tooltip: {},
        legend: {
          // data: ["销量"],
        },
        xAxis: {
          data: date,
        },
        yAxis: {},
        series: [
          {
            name: "点击量",
            type: "bar",
            data: num,
          },
        ],
      };
      // 使用刚指定的配置项和数据显示图表。
      myChart.setOption(option);
    },

    async getStatistic(){
       let outer = this
      await axios
          .get(
              "http://localhost:8081/statistic/all_statistic"
          )
          .then((response) => {
            outer.num_policy=response.data.data.policyNumber;
            outer.num_user=response.data.data.userCount.value;
            outer.today_click=response.data.data.todayClick.value;
            outer.total_click=response.data.data.total_click;
            outer.sevendays=response.data.data.seven_days;

          });

    },

    async getUser(){
      let outer = this;
      await axios
          .get(
              "http://localhost:8081/user/list"
          )
          .then((response) => (outer.tableData = response.data));
      this.tableKey = Math.random()

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
          c.style.zoom = -0.62 * t + 1.60;   // 就去修改页面的缩放比例，这个公式我自己算的，不准确，勉强。
        }
      }
    },
    handleEdit(index, row) {
      axios
          .get(
              "http://localhost:8081/user/getByUid/" + row.id
          )
          .then((response) => (console.log(response)));
    },
    handleDelete(index, row) {
      this.$confirm('此操作将永久删除该用户及其记录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        axios
            .delete(
                "http://localhost:8081/user/delete/" + row.id
            )
            .then((response) => (console.log(response)));
        this.tableData.splice(index, 1)
        this.$message({
          type: 'success',
          message: '删除成功!'
        });
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });

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