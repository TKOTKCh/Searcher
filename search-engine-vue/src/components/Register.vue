<template>
  <div>
    <el-row>
        <el-form ref="loginForm"
                 :model="form" :rules="rules" class="login-box"
                 :inline="true" >
          <h3 class="login-title">注册</h3>
            <el-form-item label="账号" prop="username" >
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
                  v-model="otherPassword"
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
            <el-form-item label="地址" prop="address">
              <el-input
                  type="text"
                  placeholder="请输入地址"
                  v-model="form.address"
              ></el-input>
            </el-form-item>
            <el-form-item label="职业" prop="career">
              <el-input
                  type="text"
                  placeholder="请输入职业"
                  v-model="form.career"
              ></el-input>
            </el-form-item>
            <el-form-item label="年龄" prop="age">
              <el-input
                  type="text"
                  placeholder="请输入年龄"
                  v-model="form.age"
              ></el-input>
            </el-form-item>
            <el-form-item label="性别" prop="sex">
              <el-input
                  type="text"
                  placeholder="请输入性别"
                  v-model="form.sex"
              ></el-input>
            </el-form-item>
          <el-form-item>
          <!-- <el-button id="login_button" type="primary" @click="submitForm('form')">登录</el-button> -->
          <el-button id="login_button" type="primary" @click="submitForm()"
          >注册</el-button>
        </el-form-item>
          <a href="/login" id="register">立刻登录</a>
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
      otherPassword: "",
      form: {
        username: "",
        password: "",
        email: "",
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
        password: [
          {
            required: true,
            message: "请输入密码",
            trigger: "blur",
          },
          // ,
          { validator: validPassword, trigger: "blur" },
        ],
      },
    };
  },
  methods: {
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
          console.log(this.form.password);
          console.log(this.otherPassword);
          if (this.form.password != this.otherPassword) {
            this.$message({
              message: "密码不一致，请重新输入",
              type: "warning",
            });
            return false;
          } else {
            let _this = this;
            axios
              .post("http://localhost:9090/register", this.form, {
                headers: { "Content-Type": "application/json" },
              })
              .then(function (ressponse) {
                if (ressponse.data != null) {
                  var message = ressponse.data.message;
                  if (message == "userNameExist") {
                    _this.$message({
                      message: "用户名已存在，注册失败",
                    });
                  } else if (message == "success") {
                    _this.$message({
                      message: "注册成功，即将跳转到登录页面",
                      type: "success",
                    });
                    setTimeout(() => {
                      //设置延迟执行
                      _this.$router.replace({ path: "/login" });
                    }, 3000);
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
            message: "用户名或密码错误",
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
  text-align: center;
}
#login_button {
  width: 350px;
  margin-top: 18px;
}
#register {
  height: 10px;
  float: right;
  text-decoration: none;
  cursor: pointer;
  color: #4e6ef2;
  line-height: 50px;
}
</style>
