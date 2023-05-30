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
      <el-submenu index="1">
        <template slot="title">
          <i class="el-icon-location"></i>
          <span>政策管理</span>
        </template>
        <el-menu-item-group>
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

        <div v-show="curpage==0" style="width: 100%;padding-top: 20px" >
          <el-col v-loading="loading">
            <div class="part1" >
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
          </el-col>
        </div>


        <div v-show="curpage==1" style="width: 100%;padding: 20px">
          政策查改
        </div>

        <div v-show="curpage==2" style="width: 100%;padding: 20px">
          <div >
            <div style="display:flex;">
              <h3 style="font-size: 24px">
              新增政策
              </h3>
            </div>
            <div style="margin-top: 50px;">
              <el-col :span="10">
                <el-form style="height: 80%;width: 500px;justify-content: start"  :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
                  <el-form-item label="政策ID" prop="POLICY_ID">
                    <el-input v-model="ruleForm.POLICY_ID"></el-input>
                  </el-form-item>
                  <el-form-item label="政策标题" prop="POLICY_TITLE">
                    <el-input v-model="ruleForm.POLICY_TITLE"></el-input>
                  </el-form-item>
                  <el-form-item label="发布等级" prop="POLICY_GRADE" >
                    <el-col :span="11">
                      <el-select v-model="ruleForm.POLICY_GRADE" placeholder="请选择发布等级" >
                        <el-option label="国家级" value="国家级"></el-option>
                        <el-option label="省级" value="省级"></el-option>
                        <el-option label="市级" value="市级"></el-option>
                        <el-option label="区县级" value="区县级"></el-option>
                      </el-select>
                    </el-col >
                  </el-form-item>
                  <el-form-item label="发布地区" prop="PROVINCE" >
                    <el-col :span="11">
                      <el-select v-model="ruleForm.PROVINCE" placeholder="请选择发布地区" >
                        <el-option label="北京市" value="北京市"></el-option>
                        <el-option label="上海市" value="上海市"></el-option>
                        <el-option label="天津市"value="天津市"></el-option>
                        <el-option label="重庆市"value="重庆市"></el-option>
                        <el-option label="河北省"value="河北省"></el-option>
                        <el-option label="山西省"value="山西省"></el-option>
                        <el-option label="黑龙江省"value="黑龙江省"></el-option>
                        <el-option label="吉林省"value="吉林省"></el-option>
                        <el-option label="辽宁省"value="辽宁省"></el-option>
                        <el-option label="江苏省"value="江苏省"></el-option>
                        <el-option label="浙江省"value="浙江省"></el-option>
                        <el-option label="安徽省"value="安徽省"></el-option>
                        <el-option label="福建省" value="福建省"></el-option>
                        <el-option label="江西省"value="江西省"></el-option>
                        <el-option label="山东省"value="山东省"></el-option>
                        <el-option label="河南省"value="河南省"></el-option>
                        <el-option label="湖北省"value="湖北省"></el-option>
                        <el-option label="湖南省"value="湖南省"></el-option>
                        <el-option label="广东省"value="广东省"></el-option>
                        <el-option label="海南省"value="海南省"></el-option>
                        <el-option label="四川省"value="四川省"></el-option>
                        <el-option label="贵州省"value="贵州省"></el-option>
                        <el-option label="云南省"value="云南省"></el-option>
                        <el-option label="陕西省"value="陕西省"></el-option>
                        <el-option label="甘肃省"value="甘肃省"></el-option>
                        <el-option label="青海省"value="青海省"></el-option>
                        <el-option label="内蒙古自治区"value="内蒙古自治区"></el-option>
                        <el-option label="广西壮族自治区"value="广西壮族自治区"></el-option>
                        <el-option label="西藏自治区"value="西藏自治区"></el-option>
                        <el-option label="宁夏回族自治区"value="宁夏回族自治区"></el-option>
                        <el-option label="新疆维吾尔自治区"value="新疆维吾尔自治区"></el-option>
                      </el-select>
                    </el-col >
                  </el-form-item>
                  <el-form-item label="发布时间" required>
                    <el-col :span="11">
                      <el-form-item prop="date1">
                        <el-date-picker type="date" placeholder="选择日期" v-model="ruleForm.PUB_TIME" style="width: 100%;"></el-date-picker>
                      </el-form-item>
                    </el-col>
                  </el-form-item>
                  <el-form-item label="政策来源" prop="POLICY_SOURCE">
                    <el-input v-model="ruleForm.POLICY_SOURCE"></el-input>
                  </el-form-item>
                  <el-form-item label="发布机构" prop="PUB_AGENCY">
                    <el-input v-model="ruleForm.PUB_AGENCY"></el-input>
                  </el-form-item>
                  <el-form-item label="政策类型" prop="POLICY_TYPE">
                    <el-radio-group v-model="ruleForm.POLICY_TYPE">
                      <el-radio label="方案办法" ></el-radio>
                      <el-radio label="通知公告" ></el-radio>
                      <el-radio label="决定条例" ></el-radio>
                      <el-radio label="请示答复" ></el-radio>
                      <el-radio label="其他"></el-radio>
                      <span></span>
                    </el-radio-group>
                  </el-form-item>
                  <el-form-item>
                    <el-button type="primary" @click="submitForm()">立即创建</el-button>
                    <el-button @click="resetForm()">重置</el-button>
                  </el-form-item>
                </el-form>
              </el-col >

              <el-col :span="10">
                <el-form style="height: 80%;width: 500px;justify-content: start"  :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
                  <el-form-item label="政策内容" prop="POLICY_BODY">
                    <el-input type="textarea" :rows="10" resize='none' v-model="ruleForm.POLICY_BODY"></el-input>
                  </el-form-item>
                  <el-form-item label="批量导入">
                    <el-upload
                        size="small"
                        class="upload-demo"
                        headers="Content-Type': 'multipart/form-data"
                        drag
                        :on-change="handleChange"
                        :file-list="fileList"
                        action='/api/predict/dataset'
                        :auto-upload='false'
                        :before-upload="beforeAvatarUpload"
                    >
                      <i class="el-icon-upload"></i>
                      <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
                      <template v-slot:tip>
                        <div class="el-upload-tip el-upload-tip--small" style="margin-bottom: 5px">
                        </div>
                      </template>
                    </el-upload>
                  </el-form-item>
                  <el-form-item>
                    <el-button type="success" @click="onSubmit()">批量导入</el-button>
                  </el-form-item>
                  </el-form>
              </el-col>
              </div>
            </div>
          </div>

        <div v-show="curpage==3" style="width: 100%;padding-top: 20px">
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
                    height="500"
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
                      label="偏好"
                      prop="career">
                  </el-table-column>
                  <el-table-column
                      label="所在地"
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
      loading:true,
      fileType: ["csv", "xls","xlxs"],
      fileList:[],
      ruleForm: {
        POLICY_ID:'',
        POLICY_TITLE: '',
        POLICY_GRADE: '',
        PUB_AGENCY: '',
        PUB_TIME: '',
        POLICY_TYPE: '',
        POLICY_BODY: '',
        PROVINCE: '',
        POLICY_SOURCE:'',
      },
      rules: {
        POLICY_ID: [
          { required: true, message: '请输入政策ID', trigger: 'blur' },
        ],
        POLICY_TITLE: [
          { required: true, message: '请输入政策标题', trigger: 'blur' },
        ],
        // POLICY_GRADE: [
        //   { required: true, message: '请选择政策级别', trigger: 'change' }
        // ],
        // PUB_TIME: [
        //   { type: 'date', required: true, message: '请选择发布日期', trigger: 'change' }
        // ],
        // POLICY_TYPE: [
        //   {required: true, message: '请选择政策类别', trigger: 'change' }
        // ],
        // PUB_AGENCY: [
        //   { required: true, message: '请填写政策发布机构', trigger: 'blur' }
        // ],
        // PROVINCE: [
        //   { required: true, message: '请填写政策发布地区', trigger: 'blur' }
        // ],
        // POLICY_SOURCE: [
        //   { required: true, message: '请填写政策来源', trigger: 'blur' }
        // ],
        // POLICY_BODY: [
        //   { required: true, message: '请填写政策内容', trigger: 'blur' }
        // ],
      },


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
    this.user = JSON.parse(window.localStorage.getItem("admin_access"));
    if (this.user != null) {
      this.check = true;
      console.log(this.user)
    }
    this.bodyScale()
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
    handleChange(file) {
      this.fileList = [file]
    },
    onSubmit() {
      if(this.beforeAvatarUpload(this.fileList[0])==true){
        this.$message({
          type:"success",
          message:"上传成功",
        })
        if(this.fileList.length <= 0){
          this.$message.error('请选择文件');
          return
        }
        const formData = new FormData();
        formData.append('fileObj', this.fileList[0].raw)
        console.log(formData)
        // post地址
        //上传
          axios.post("http://localhost:8081/data/addDataByFile",formData,
              {header:{'Content-Type':'multipart/form-data'}}).then(res =>{
            setTimeout(() => {
              //设置延迟执行
              this.$message({
                message: "上传成功",
                type: "success",
              });
            }, 1500);
          })
        }
    },
    submitForm(){
      this.$refs.ruleForm.validate((valid) => {
        if(valid){
          // 暂时伪装
          // this.fileList=[];
          // this.ruleForm.POLICY_ID='';
          // this.ruleForm.POLICY_TITLE= '';
          // this.ruleForm.POLICY_GRADE= '';
          // this.ruleForm. PUB_AGENCY= '';
          // this.ruleForm.PUB_TIME= '';
          // this.ruleForm. POLICY_TYPE= '';
          // this.ruleForm.POLICY_BODY= '';
          // this.ruleForm. PROVINCE= '';
          // this.ruleForm.POLICY_SOURCE= '';
          this.$message({
            message: "上传中请稍等",
            type: "message",
          });
          // let data = {
          //   "policyId":this.ruleForm.POLICY_ID,
          //   "policyTitle":this.ruleForm.POLICY_TITLE,
          //   "policyGrade":this.ruleForm.POLICY_GRADE,
          //   "pubAgencyId":"",
          //   "pubAgency":this.ruleForm. PUB_AGENCY,
          //   "pubAgencyFullname":"",
          //   "pubNumber":"",
          //   "pubTime":this.ruleForm.PUB_TIME,
          //   "policyType":this.ruleForm. POLICY_TYPE,
          //   "policyBody":this.ruleForm.POLICY_BODY,
          //   "province":this.ruleForm. PROVINCE,
          //   "city":"",
          //   "policySource":this.ruleForm.POLICY_SOURCE,
          //   "pubTimeYear":"",
          // };
          let data = new FormData();
          data.append('policyId',this.ruleForm.POLICY_ID);
          data.append('policyTitle',this.ruleForm.POLICY_TITLE);
          data.append('policyGrade',this.ruleForm.POLICY_GRADE);
          data.append('pubAgencyId',"");
          data.append('pubAgency',this.ruleForm.PUB_AGENCY);
          data.append('pubAgencyFullname',"");
          data.append('pubNumber','');
          data.append('pubTime',this.ruleForm.PUB_TIME);
          data.append('policyType',this.ruleForm. POLICY_TYPE);
          data.append('policyBody',this.ruleForm.POLICY_BODY);
          data.append('province',this.ruleForm. PROVINCE);
          data.append('city','');
          data.append('policySource',this.ruleForm.POLICY_SOURCE);
          data.append('pubTimeYear','');
          axios.post("http://localhost:8081/data/addData",data,
              {header:{'Content-Type':'multipart/form-data'}}).then(res =>{
            setTimeout(() => {
              //设置延迟执行
              this.$message({
                message: "上传成功",
                type: "success",
              });
            }, 1500);
          })
        }else{
          this.$message({
            message: "请正确填写政策表单",
            type: "warning",
          });
        }
      })

    },
    resetForm(){
      this.ruleForm.POLICY_ID= '';
      this.ruleForm.POLICY_TITLE= '';
      this.ruleForm.POLICY_GRADE= '';
      this.ruleForm. PUB_AGENCY= '';
      this.ruleForm.PUB_TIME= '';
      this.ruleForm. POLICY_TYPE= '';
      this.ruleForm.POLICY_BODY= '';
      this.ruleForm. PROVINCE= '';
      this.ruleForm.POLICY_SOURCE= '';
    },
    beforeAvatarUpload(file) {
      if (file.type != "" || file.type != null || file.type != undefined){
        //截取文件的后缀，判断文件类型
        const FileExt = file.name.replace(/.+\./, "").toLowerCase();
        //计算文件的大小
        const isLt5M = file.size / 1024 / 1024 < 50; //这里做文件大小限制
        //如果大于50M
        if (!isLt5M) {
          this.$message.error('上传文件大小不能超过 50MB!');
          return false;
        }
        //如果文件类型不在允许上传的范围内
        if(this.fileType.includes(FileExt)){
          return true;
        }
        else {
          this.$message.error("上传文件格式不正确!");
          return false;
        }
      }
    },
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
        tooltip: {
          trigger: 'axis',
          // axisPointer: {
          //   type: 'cross',
          //   crossStyle: {
          //     color: '#999'
          //   }
          // }
        },
        legend: {
        },
        xAxis: {
          type: 'category',
          data: date,
          axisLine: {
            show: true,
            lineStyle: {
              color: '#7B68EE'
            }
          },
          splitLine: {
            show: false,
            lineStyle: {
              color: 'blue',
              width: 1,
              type: 'solid'
            }
          },
          axisPointer: {
            type: 'shadow'
          },
        },
        yAxis: [
          {
            axisLabel: {
              color: '#FF6347'
            },
            axisLine: {
              show: true,
              lineStyle: {
                color: '#FFDEAD'
              }
            },
            splitLine: {
              show: false,
              lineStyle: {
                color: 'blue',
                width: 1,
                type: 'solid'
              }
            },
            nameTextStyle: {
              rich: {
                legend: {
                  width: 12,
                  height: 4,
                  backgroundColor: '#EE82EE'
                },
                value: {
                  color: '#FF8C00'
                }
              }
            }
          },
          {
            type: 'value',
            axisLabel: {
              color: '#00BFFF'
            },
            axisLine: {
              show: true,
              lineStyle: {
                color: '#66CDAA'
              }
            },
            splitLine: {
              lineStyle: {
                color: '#E6E6FA',
                width: 1,
                type: 'solid'
              }
            },
            nameTextStyle: {
              rich: {
                legend: {
                  width: 12,
                  height: 4,
                  backgroundColor: '#87CEFA'
                },
                value: {
                  color: '#CD853F'
                }
              }
            }
          }
        ],
        series: [
          // {
          //   name: '点击量',
          //   type: 'bar',
          //   data:num,
          //   barWidth: 20, // 柱状图的宽度
          //   itemStyle: {
          //     borderRadius: 5,
          //     color: 'rgba(30, 144, 255, 1)'
          //   }
          // },
          {
            name: '点击量',
            type: 'line',
            yAxisIndex: 1,
            data: num,
            itemStyle: {
              color: 'rgba(255 ,185 ,15)'
            },
            areaStyle: {
              color: 'rgba(255, 218, 185,0.2)'
            },
            symbolSize: 10,
            showSymbol: true
          }
        ]
      };
      // 使用刚指定的配置项和数据显示图表。
      myChart.setOption(option);
    },

    async getStatistic(){
      this.loading=true
       let outer = this
      await axios
          .get(
              "http://localhost:8081/statistic/all_statistic"
          )
          .then((response) => {
            outer.num_policy=response.data.data.policyNumber;
            outer.num_user=response.data.data.userCount;
            outer.today_click=response.data.data.todayClick;
            outer.total_click=response.data.data.total_click;
            outer.sevendays=response.data.data.seven_days;
          });
      this.loading=false
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
          c.style.zoom = -0.62 * t + 1.68;   // 就去修改页面的缩放比例，这个公式我自己算的，不准确，勉强。
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
    batch_load(){
      if(this.fileList.length==0){
        this.$message({
          message: "请上传文件！",
          type: "warning",
        });
      }else{
        this.resetForm();
        this.$message({
          message: "上传中请稍等",
          type: "message",
        });
        setTimeout(() => {
          //设置延迟执行
          this.$message({
            message: "上传成功",
            type: "success",
          });
        }, 1500);
      }
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