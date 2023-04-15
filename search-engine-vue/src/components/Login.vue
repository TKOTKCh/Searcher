<template>
  <div>
    <el-form ref="loginForm" :model="form" :rules="rules" class="login-box">
      <div>
        <img  src="~@/assets/img.png" style="width:200px">
      </div>
      <h3 class="login-title">欢迎登录</h3>
      <el-form-item label="账号" prop="username">
        <el-input
          type="text"
          placeholder="请输入用户名"
          v-model="form.username"
        ></el-input>
      </el-form-item>
      <el-form-item label="密码" prop="password">
        <el-input
          type="password"
          placeholder="请输入密码"
          v-model="form.password"
        ></el-input>
      </el-form-item>
      <el-form-item>
        <!-- <el-button id="login_button" type="primary" @click="submitForm('form')">登录</el-button> -->
        <el-button id="login_button" type="primary" @click="submitForm()"
          >登录</el-button
        >
        <a href="/register" id="register">立刻注册</a>
        <a href="/home" id="back">返回首页</a>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import axios from "axios";
export default {
  name: "Login",
  data() {
    return {
      form: {
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
          // { validator: validPassword, trigger: 'blur' }
        ],
      },
    };
  },
  created(){
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
    submitForm() {
      this.$refs.loginForm.validate((valid) => {
        if (valid) {
          let _this = this;
          axios
            .post("http://localhost:8081/user/login", this.form, {
              headers: { "Content-Type": "application/json" },
            })
            .then(function (ressponse) {
              if (ressponse.data.data.message == "success") {
                localStorage.setItem("access", JSON.stringify(ressponse.data.data));
                _this.$message({
                  message: "登录成功",
                  type: "success",
                });
                setTimeout(() => {
                  //设置延迟执行
                  _this.$router.replace({ path: "/" });
                }, 1000);
              } else {
                _this.$message.error({
                  message: "登录失败",
                });
              }
            })
            .catch(function (error) {
              console.log("异常信息:" + error);
            });
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
.login-box {
  width: 350px;
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

#back{
  height: 10px;
  float: left;
  text-decoration: none;
  cursor: pointer;
  color: #4e6ef2;
  line-height: 50px;
}
</style>
