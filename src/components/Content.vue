<template>
  <el-container style="width: 100%">
    <el-menu mode="horizontal"  style="background-color:#cbb486;" >
<!--      <el-menu-item index="1" style="margin-left: 42%;color: white;font-weight: bolder;font-size: larger" >首页</el-menu-item>-->
<!--      <el-menu-item index="2" style="color: white;font-weight: bolder;font-size: larger">要闻动态</el-menu-item>-->
      <el-menu-item index="3" style="color: white;font-weight: bolder;font-size: larger"></el-menu-item>
    </el-menu>
    <!--    头部-->
    <div class="little_button" style="float: right;margin-right: 10px">
      <el-col style="display: flex; float: right;">
        <!--            <el-popover-->
        <!--                placement="left-start"-->
        <!--                title="收藏夹"-->
        <!--                width="300"-->
        <!--                trigger="click"-->
        <!--                content="将来放收藏夹相关内容，实现效果可以参考edge的收藏夹。"-->
        <!--            >-->
        <!--              <el-button-->
        <!--                  slot="reference"-->
        <!--                  icon="el-icon-folder"-->
        <!--                  circle-->
        <!--                  style="margin-top: 25%"-->
        <!--              ></el-button>-->
        <!--              <div v-if="check">-->
        <!--                <favorites-->
        <!--                    :username="user.username"-->
        <!--                    :addToFavorite="0"-->
        <!--                    :key="timer"-->
        <!--                    ref="favorites"-->
        <!--                ></favorites>-->
        <!--              </div>-->
        <!--              <div v-if="!check">-->
        <!--                <a-->
        <!--                    style="-->
        <!--                    color: #55ab41;-->
        <!--                    margin-right: 148px;-->
        <!--                    text-decoration: none;-->
        <!--                  "-->
        <!--                    href="/login"-->
        <!--                >对不起,请前往登录</a-->
        <!--                >-->
        <!--              </div>-->
        <!--            </el-popover>-->
        <span>&nbsp;&nbsp;</span>
        <el-popover
            placement="bottom"
            title="个人信息"
            width="300"
            trigger="click"
            content="将来放关于用户的信息。"
        >
          <el-button v-if="check" slot="reference" icon="el-icon-user-solid" circle
                     style="margin-top: 25%">
          </el-button>
          <el-button v-if="!check" slot="reference" icon="el-icon-user" circle
                     style="margin-top: 25%">
          </el-button>
          <!--用户信息 -->
          <div v-if="check">
            欢迎回来！<span style="color: #55ab41">{{
              user.username
            }}</span>
            <span id="logout" @click="logout">注销</span>
          </div>
          <div v-if="!check">
            <a
                style="
                    color: #55ab41;
                    margin-right: 148px;
                    text-decoration: none;
                  "
                href="/login"
            >对不起,请前往登录</a
            >
            <a
                style="color: #55ab41; text-decoration: none"
                href="/register"
            >注册</a
            >
          </div>
        </el-popover>
      </el-col>
    </div>
    <el-header class="header" height="20%" >
      <el-row  style="height: 200px; width: 100%; display: flex">
        <el-col s style="width: 100px; margin-right: 40px">
          <div>
            <a href="/"><img src="~@/assets/img.png" alt="" width="200%"/></a>
          </div>
        </el-col>
      </el-row>
    </el-header>

      <div class="main">
        <div class="contain">
          <div class="title">
            <h2 class="big">
              {{item.POLICY_TITLE}}
              <div>
                <small id="ivs_date" class="Article-time">{{ item.PUB_TIME }}
                <span>来源：{{ item.PUB_AGENCY }}</span>
              </small>
              </div>

            </h2>


          </div>
          <div class="article">
            {{item.POLICY_BODY}}
          </div>
        </div>
      </div>

    </el-container>
</template>

<script>
export default {
  name: "content.vue",
  // dconst id=this.$route.params.id;
  data(){
    return{
      check:false,
      item:{
        POLICY_TITLE:''
      },
    }
  },
  created() {
    this.bodyScale()
    this.item.id=this.$route.query.id;
    this.item.POLICY_TITLE=this.$route.query.POLICY_TITLE;
    this.item.PUB_AGENCY=this.$route.query.PUB_AGENCY;
    this.item.PUB_TIME=this.$route.query.PUB_TIME;
    this.item.POLICY_TYPE=this.$route.query.POLICY_TYPE;
    this.item.POLICY_BODY=this.$route.query.POLICY_BODY;
    this.item.caption=this.$route.query.caption;

    this.user = JSON.parse(window.localStorage.getItem("access"));
    if (this.user != null) {
      this.check = true;
    }

  },
  mounted() {
  },
  methods:{
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
  },

}

</script>

<style>

.Article-time {
  color: #CBCCCE;
  font-size: 16px;
  margin-bottom: 29px;
}
.article{
  width: 1000px;
  /*word-wrap:break-word;*/
  padding: 30px 0;
  font-size: 22px;
  line-height: 1.8;
  text-align: left;
  font-family: inherit;
}

small{
  line-height: 1;
}
.main{

}
.contain{
  padding-right: 15px;
  padding-left: 15px;
  margin-right: 20%;
  margin-left: 20%;
  margin-top: 3%;
  align-content: center;
  align-items: center;
  text-align: center;
}
.title{}
.big{
  font-weight: 700;
  line-height: 1.5;
  margin-top: 0;
  padding: 0;
  text-align: left;
  font-size: 28px;
  color: #333333;
  margin-bottom: 30px;
  position: relative;
  border-bottom: solid 1px #eee;
}

div {
  /*white-space: nowrap;*/
}
a {
  text-decoration: none;
  color: #55ab41;
}
.input .el-input__inner {
  height: 50px;
}
.el-header {
  /*background-color: #B3C0D1;*/
  display: flex;
  position: absolute;
  width: 100%;
  height: 5px;
}
.el-main {
  /*background-color: #E9EEF3;*/
  margin-top: 0;
  position: absolute;
  left: 0;
  right: 0;
  top: 60px;
  bottom: 0;
  min-width: 1119px;
  /* overflow-y: scroll; */
}
#allrelated {
  margin-left: 100px;
  margin-bottom: 20px;
  width: 700px;
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
}
.related {
  width: 258px;
  text-align: left;
  margin-right: 10px;
  cursor: pointer;
  border: 1px solid #1176db8f;
  padding: 10px;
  margin: 5px;
}
*{
  margin: 0;
}
#logout {
  cursor: pointer;
  float: right;
  text-decoration: none;
}
.selectedOne {
  border-bottom: 1px solid #55ab41;
}
.picToPic {
  display: none;
}
#text {
  width: 50px;
  height: 30px;
  cursor: pointer;
}
#picture {
  width: 50px;
  height: 30px;
  cursor: pointer;
}
.P_item {
  margin-right: 15px;
}
@media (max-width: 1372px) {
  #gen {
    display: none;
  }
}
</style>