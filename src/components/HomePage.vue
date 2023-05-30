<template>
  <div style="">
<!--    <el-row  :gutter="20" type="flex" justify="end" style="">-->
      <div style="overflow: hidden;width: 100%;height: 50px;background-color: #cbb486"><!--      <el-image :src="require('~@/assets/tiantan.png')"></el-image>-->
        <img  src="~@/assets/img.png" style="width:180px;float: left">
        <el-popover
            placement="bottom"
            style="margin-right: 0;float: right"
            title="个人信息"
            width="300"
            trigger="click"
            content="将来放关于用户的信息。"
        >

          <el-button v-if="check" slot="reference" icon="el-icon-user-solid" circle style="margin-right:10px;margin-top: 10px;float: right"> </el-button>
          <el-button v-if="!check"slot="reference" icon="el-icon-user" circle style="margin-right:10px;margin-top: 10px;float: right"> </el-button>
          <!--用户信息 -->
          <div v-if="check">
            欢迎回来！<span style="color: #55ab41">{{ user.username }}</span>
            <span id="logout" @click="logout">注销</span>
          </div>
          <div v-if="!check">
            <a
                style="color: #55ab41; margin-right: 148px; text-decoration: none"
                href="/login"
            >对不起,请前往登录</a
            >
            <a style="color: #55ab41; text-decoration: none" href="/register"
            >注册</a
            >
          </div>
        </el-popover>
      </div>
<!--    </el-row>-->
<!--    背景图片底层-->
    <div class="back" style="margin-top: 0%;overflow: hidden;width: 100%">
      <img src="~@/assets/tiantan.png" style="" />
    </div>
    <!--    header -->
    <!--    <el-menu :default-active="activeIndex" class="el-menu-demo" mode="horizontal" @select="handleSelect">-->
    <div class="table-wrapper" style="background-color: transparent;height: 50px">
<!--    <el-menu mode="horizontal"  style="background-color: transparent;margin-top: 1%" >-->
<!--      <el-menu-item index="1" style="margin-left: 42%;color: white;font-weight: bolder;font-size: larger" >首页</el-menu-item>-->
<!--      <el-menu-item index="2" style="color: white;font-weight: bolder;font-size: larger">要闻动态</el-menu-item>-->
<!--      <el-menu-item index="3" style="color: white;font-weight: bolder;font-size: larger">政务公开</el-menu-item>-->
<!--    </el-menu>-->
    </div>



    <el-row id="input">
      <div>
        <img src="~@/assets/guohui.png" alt="" />
      </div>
      <h2 id="top">智能政策检索系统</h2>
      <p id="mid"
      >海纳百川 · 追求卓越 · 开明睿智 · 大气谦和</p>
<!--      :popper-append-to-body='false'-->
        <el-autocomplete v-model="search_word" id="ssk"  style="width: 600px;
    transform: scale(1.3);
    -webkit-tap-highlight-color: transparent;
    -webkit-font-smoothing: antialiased;
    box-sizing: border-box;
    font: inherit;
    font-family: inherit;
    line-height: 2;
    transition: .3s ease-in-out;
    -webkit-appearance: none;
    font-size: 50px;
    outline: none;
    border: none;
    padding: 0 0 0 0px;
    margin: 0;
    width: 100%;
    /*position: relative;*/
    color: #ffffff;
    font-size: 20px;
    background-color: rgba(255,255,255,.6);
    border-radius: 4px 0 0 4px!important;
    box-shadow: 0 0 0px 5px rgba(225,225,225,0.3)!important;"
                         maxlength="50"
                         :fetch-suggestions="querySearchAsync" @select="handleSelect"
                         placeholder="请输入想要了解的政策" prefix-icon="el-icon-search"
                         @keyup.enter.native="search">
          <el-button
              style="margin-right: 0px; width: 80px;font-weight: bolder;font-size: larger"
              slot="suffix"
              type="text"
              @click="search"
          >搜索</el-button>
        </el-autocomplete>
<!--        <div >111</div>-->
    </el-row>

<!--最下面四个东西-->
    <div style="display:flex; flex-direction: row;justify-content:center">
      <div class="bock01" name="Slider-1" style="margin-right: 3%;margin-left: 3%">
        <i><img src="~@/assets/icon1.png" alt="个人政策"></i>
        <h3>个人办事</h3>
        <p>
<!--          <a href="https://zwdt.sh.gov.cn/govPortals/naturalPerson/business?code=117" title="职业资格" target="_blank">职业资格</a>-->
<!--          <a href="https://zwdt.sh.gov.cn/govPortals/naturalPerson/business?code=122" title="证件办理" target="_blank">证件办理</a>-->
<!--          <a href="https://zwdt.sh.gov.cn/govPortals/naturalPerson/business?code=123" title="交通出行" target="_blank">交通出行</a>-->
<!--          <a href="https://zwdt.sh.gov.cn/govPortals/naturalPerson/business?code=133" title="医疗卫生" target="_blank">医疗卫生</a>-->
        </p>
      </div>
      <div class="bock01" name="Slider-1"style="margin-right: 3%;margin-left: 3%">
        <i><img src="~@/assets/icon2.png" alt="最新政策" ></i>
        <h3>最新政策</h3>
        <p>
        </p>
      </div>
      <div class="bock01" name="Slider-1"style="margin-right: 3%;margin-left: 3%">
        <i><img src="~@/assets/icon3.png" alt="企业政策"></i>
        <h3>企业政策</h3>
        <p>
        </p>
      </div>
      <div class="bock01" name="Slider-1"style="margin-right: 3%;margin-left: 3%">
        <i><img src="~@/assets/icon4.png" alt="热点政策"></i>
        <h3>热点政策</h3>
        <p>
        </p>
      </div>

    </div>




  </div>
</template>

<script>
import axios from "axios";
import favorites from "./Favorites";

export default {
  components: { favorites },
  data() {
    return {
      search_word: "",
      user: "",
      check: false,
    };
  },
  created() {
    this.bodyScale()
    this.user = JSON.parse(window.localStorage.getItem("access"));
    if (this.user != null) {
      this.check = true;
    }
  },
  mounted() {
    this.checkToken();
    setInterval(() => {
      this.checkToken();
    }, 1000 * 60 * 10); //每十分钟检查token
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
          c.style.zoom = -0.62 * t + 1.60;   // 就去修改页面的缩放比例，这个公式我自己算的，不准确，勉强。
        }
      }
    },
    async querySearchAsync(queryString, cb) {
      clearTimeout(this.timeout);
      var data = []
      var results = []
      if (queryString == '') {
        cb(results);
      } else {
        await axios
          .get("http://localhost:8081/bm25/completion?query=" + this.search_word)
          .then((response) => {
            data = response.data.data
          })
        if(data){for (let i = 0; i < data.length; i++) {
          results.push({
            value: data[i]
          })
        }
          cb(results)}
      else{
        results=[];
        cb(results)
      }

      }
    },
    //点击出现搜索后点击的每一项
    handleSelect(item) {
      this.id = item.id
      this.name = item.value
      this.search()
    },
    async checkToken() {
      var jwt = JSON.parse(window.localStorage.getItem("access"));
      if (jwt != null) {
        await axios
          .get(
            "http://localhost:9090/survival?token=" +
              jwt.token +
              "&username=" +
              jwt.username
          )
          .then((response) => {
            if (response.data.message != "success") {
              this.$message.error("登录时间到达,请重新登录");
              window.localStorage.removeItem("access");
              setTimeout(() => {
                location.reload();
              }, 3000);
            }
          });
      }
    },
    async logout() {
      var _this = this;
      // var jwt = JSON.parse(window.localStorage.getItem("access"));
      // if (jwt != null) {
      //   await axios
      //     .get(
      //       "http://localhost:9090/user/logout?username=" +
      //         jwt.username +
      //         "&token=" +
      //         jwt.token
      //     )
      //     .then(function (response) {
      //       if (response.data.message == "success") {
      //         _this.$message({
      //           message: "退出成功",
      //           type: "success",
      //         });
      //         window.localStorage.removeItem("access");
      //         _this.check = false;
      //         setTimeout(() => {
      //           location.reload();
      //         }, 3000);
      //       }
      //     });
      // }
      // else{
      //   location.reload();
      // }

      //先简化退出流程
      this.$message({
        message: "退出成功",
        type: "success",
      });
      window.localStorage.removeItem("access");
      setTimeout(() => {
        location.reload();
      }, 1000);
    },
    search() {

      // if(this.check){
      //   axios
      //       .get(
      //           "http://localhost:8081/user/addUserQuery?userid=" + this.user.id
      //           + "&query=" + this.search_word
      //       )
      //       .then((response) => (console.log(response)));
      // }

      // 需要请求数据来显示
      if(this.search_word==''){
        this.$message({
          message: "请输入搜索词",
          type: "warning",
        });
      }
      else{this.$router.push({
        path: "/search",
        query: {
          word: this.search_word,
          recordsNum: this.recordsNum,
        },
      });}
    },
  },
};
</script>

<style lang="scss">

.el-autocomplete_::placeholder{
  font-size: 60px;
}

.bock01 {
  border: 1px solid rgba(0,0,0,0.10);
  width: 50%;
  box-shadow: 0 0 10px 0 rgba(0,0,0,0.05);
  overflow: hidden;
  padding-top: 35px;
  padding-bottom: 27px;
  margin-left: 5px;
  margin-right: 5px;
}

.i {
  font-style: italic;
}

.p {
  display: block;
  margin-block-start: 1em;
  margin-block-end: 1em;
  margin-inline-start: 0px;
  margin-inline-end: 0px;
}

#top{
  -webkit-tap-highlight-color: transparent;
  -webkit-font-smoothing: antialiased;
  box-sizing: border-box;
  font-family: inherit;
  line-height: 1.1;
  color: #fff;
  font-size: 36px;
  font-weight: bold;
  letter-spacing: 5px;
  margin-top: 10px;
  text-align: center;
  margin-bottom: 15px;
}

#mid {
  -webkit-tap-highlight-color: transparent;
  font-family: 'Microsoft YaHei',arial;
  line-height: 1.5;
  -webkit-font-smoothing: antialiased;
  box-sizing: border-box;
  margin: 0 0 10px;
  color: rgba(255,255,255,0.9);
  font-size: 16px;
  text-align: center;
  margin-bottom: 33px;
  letter-spacing: 5px;
}

#input {
  text-align: center; /*让div内部文字居中*/
  border-radius: 40px;
  width: 600px;
  height: 500px;
  margin: auto;
  position: relative;
  margin-top: 2%;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
}
a {
  text-decoration: none;
}
#logout {
  cursor: pointer;
  float: right;
  text-decoration: none;
}

el-menu{
  align-items: center; /* 垂直居中 */
  justify-content: center; /* 水平居中 */
  //background-image: url("../../src/assets/1057.png");
  background-image:url("../assets/back.jpg");
}

.el-textarea__inner,.el-input__inner {
  background: transparent !important;
}

*{
  margin: 0;
}

.back{
  position: absolute;
  top:4%;
  bottom: 0%;
  margin: 0px;
  z-index: -1;
}

/**表格背景透明 */
//透明化整体
.table-wrapper ::v-deep  .el-table,

//hover时样式
.table-wrapper ::v-deep  .el-table tbody tr:hover>td {
  background-color: #367f7f78 !important
}



</style>
