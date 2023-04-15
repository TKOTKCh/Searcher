<template>
  <div  >
    <el-row>

        <el-form ref="loginForm"
                 :model="form" :rules="rules" class="login-box"
                 :inline="true" >
          <div>
            <img  src="~@/assets/img.png" style="width:200px">
          </div>
          <h3 class="login-title">注册</h3>
            <el-form-item label="账号" prop="username">
              <el-input
                  type="text"
                  placeholder="请输入用户名"
                  v-model="form.username"
                  @input="change($event)"
              ></el-input>
            </el-form-item>
            <el-form-item label="密码" prop="password">
              <el-input
                  type="password"
                  placeholder="请输入密码"
                  v-model="form.password"
                  @input="change($event)"
              ></el-input>
            </el-form-item>
            <el-form-item label="密码" prop="otherPassword">
              <el-input
                  type="password"
                  placeholder="请再次输入密码"
                  v-model="form.otherPassword"
              ></el-input>
            </el-form-item>
            <el-form-item label="邮箱" prop="email">
              <el-input
                  type="text"
                  placeholder="请再次输入密码"
                  v-model="form.email"
              ></el-input>
            </el-form-item>
            <el-form-item label="电话" prop="phone">
              <el-input
                  type="text"
                  placeholder="请输入电话"
                  v-model="form.phone"
              ></el-input>
            </el-form-item>
          <el-form-item label="年龄" prop="age">
            <el-input
                type="text"
                placeholder="请输入年龄"
                v-model="form.age"
            ></el-input>
          </el-form-item>
            <el-form-item label="领域" prop="career">
<!--              <el-input-->
<!--                  type="text"-->
<!--                  placeholder="请输入职业"-->
<!--                  v-model="form.career"-->
<!--              ></el-input>-->
              <el-select v-model="form.career" placeholder="请选择">
                <el-option
                    v-for="item in options"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          <el-form-item label="所在地" prop="address">
<!--            <el-input-->
<!--                type="text"-->
<!--                placeholder="请输入地址"-->
<!--                v-model="form.address"-->
<!--            ></el-input>-->
            <el-select v-model="form.address" placeholder="请选择">
              <el-option
                  v-for="item in provinces"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>


          <!-- <el-button id="login_button" type="primary" @click="submitForm('form')">登录</el-button> -->
          <el-button id="login_button" type="primary" @click="submitForm()"
          >注册</el-button>
          <div><a href="/login" id="register">立刻登录</a></div>


        </el-form>
    </el-row>



  </div>
</template>

<script>
import axios from "axios";
let validPassword = (rule, value, callback) => {
  let reg = /^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{4,15}$/;
  if (!reg.test(value)) {
    callback(new Error("密码必须是由4-15位字母+数字组合"));
  } else {
    callback();
  }
};
export default {
  name: "Login",
  data() {
    return {
      options: [{
        value: '农业',
        label: '农业'
      }, {
        value: '工商业',
        label: '工商业'
      }, {
        value: '科技',
        label: '科技'
      }, {
        value: '教育',
        label: '教育'
      }, {
        value: '文化',
        label: '文化'
      },
        {
          value: '民生',
          label: '民生'
        }],

      provinces: [{value: '北京市',label: '北京市'},
    {value: '上海市',label: '上海市'},
    {value: '天津市',label: '天津市'},
    {value: '重庆市',label: '重庆市'},
    {value: '河北省',label: '河北省'},
    {value: '山西省',label: '山西省'},
    {value: '黑龙江省',label: '黑龙江省'},
    {value: '吉林省',label: '吉林省'},
    {value: '辽宁省',label: '辽宁省'},
    {value: '江苏省',label: '江苏省'},
    {value: '浙江省',label: '浙江省'},
    {value: '安徽省',label: '安徽省'},
    {value: '福建省',label: '福建省'},
    {value: '江西省',label: '江西省'},
    {value: '山东省',label: '山东省'},
    {value: '河南省',label: '河南省'},
    {value: '湖北省',label: '湖北省'},
    {value: '湖南省',label: '湖南省'},
    {value: '广东省',label: '广东省'},
    {value: '海南省',label: '海南省'},
    {value: '四川省',label: '四川省'},
    {value: '贵州省',label: '贵州省'},
    {value: '云南省',label: '云南省'},
    {value: '陕西省',label: '陕西省'},
    {value: '甘肃省',label: '甘肃省'},
    {value: '青海省',label: '青海省'},
    {value: '内蒙古自治区',label: '内蒙古自治区'},
    {value: '广西壮族自治区',label: '广西壮族自治区'},
    {value: '西藏自治区',label: '西藏自治区'},
    {value: '宁夏回族自治区',label: '宁夏回族自治区'},
    {value: '新疆维吾尔自治区',label: '新疆维吾尔自治区'},],

      form: {
        username: "",
        password: "",
        email: "",
        otherPassword: "",
        phone: "",
        address: "",
        career: "",
        age: "",
        sex: ""
      },
      rules: {
        username: [
          {
            required: true,
            message: "请输入用户名",
            trigger: "blur",
          },
        ],
        address: [
          {
            required: true,
            message: "请输入用户名",
            trigger: "blur",
          },
        ],
        password: [
          {
            required: true,
            message: "请输入密码",
            trigger: "blur",
          },
          // ,
          { validator: validPassword, trigger: "blur" },
        ],
        otherPassword:[
          {
            required: true,
            message: "请确认密码",
            trigger: "blur",
          }
        ],
        career: [
          {
            required: true,
            message: "请选择领域",
            trigger: "blur",
          }
        ]
      },
      address: [
        {
          required: true,
          message: "请选择地区",
          trigger: "blur",
        }
      ],

    };
  },
  created() {
    this.bodyScale()
  },
  methods: {
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
    // submitForm(formName) {
    //   this.$refs[formName].validate((valid) => {
    //     if (valid) {
    //       sessionStorage.setItem("isLogin", "true");
    //       this.$store.dispatch("asyncUpdateUser", { name: this.form.name });
    //       this.$router.push({ name: "Main", params: { name: this.form.name } });
    //     } else {
    //       this.$message({
    //         message: "用户名或密码错误",
    //         type: "warning",
    //       });
    //       return false;
    //     }
    //   });
    // },
    change(){
      this.$forceUpdate()
    },
    submitForm() {
      this.$refs.loginForm.validate((valid) => {
        if (valid) {
          if (this.form.password != this.form.otherPassword) {
            this.$message({
              message: "密码不一致，请重新输入",
              type: "warning",
            });
            return false;
          } else {
            let _this = this;
            axios
              .post("http://localhost:8081/user/register", this.form, {
                headers: { "Content-Type": "application/json" },
              })
              .then(function (ressponse) {
                if (ressponse.data != null) {
                  var message = ressponse.data.message;
                  if (message == "userNameExist") {
                    _this.$message({
                      message: "用户名已存在，注册失败",
                    });
                  } else if (message == "") {
                    _this.$message({
                      message: "注册成功，即将跳转到登录页面",
                      type: "success",
                    });
                    setTimeout(() => {
                      //设置延迟执行
                      _this.$router.replace({ path: "/login" });
                    }, 1000);
                  } else if (message == "failure") {
                    _this.$message({
                      message: "异常错误，请稍后再试",
                    });
                  }
                }
              })
              .catch(function (error) {
                console.log("异常信息:" + error);
              });
          }
        } else {
          this.$message({
            message: "请检查注册信息",
            type: "warning",
          });
          return false;
        }
      });
    },
  },
};
</script>

<style lang="scss" scoped>
.el-form-item{
  margin-right: 70px;
}

.login-box {
  width: 700px;
  margin: 120px auto;
  border: 1px solid #dcdfe6;
  padding: 20px;
  border-radius: 5px;
  box-shadow: 0 0 30px #dcdfe6;
}

.login-title {
  text-align: left;
  margin-left: 5%;
  margin-bottom: 5%;
}
#login_button {
  width: 100px;
  margin-top: 18px;
}
#register {
  height: 10px;
  //float: center;
  text-decoration: none;
  cursor: pointer;
  color: #4e6ef2;
  line-height: 50px;
}
</style>
