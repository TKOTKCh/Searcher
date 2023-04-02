<template>
  <el-container style="width: 100%">
    <el-menu mode="horizontal"  style="background-color:#cbb486;" >
      <el-menu-item index="1" style="margin-left: 42%;color: white;font-weight: bolder;font-size: larger" >首页</el-menu-item>
      <el-menu-item index="2" style="color: white;font-weight: bolder;font-size: larger">要闻动态</el-menu-item>
      <el-menu-item index="3" style="color: white;font-weight: bolder;font-size: larger">政务公开</el-menu-item>
    </el-menu>
<!--    头部-->
    <el-header class="header" height="20%" >
      <el-row  style="height: 200px; width: 100%; display: flex">
        <el-col s style="width: 100px; margin-right: 40px">
          <div>
            <a href="/"><img src="~@/assets/img.png" alt="" width="200%"/></a>
          </div>
        </el-col>
      </el-row>
      <div class="little_button">
        <el-col style="display: flex; float: left;">
          <el-popover
              placement="left-start"
              title="收藏夹"
              width="300"
              trigger="click"
              content="将来放收藏夹相关内容，实现效果可以参考edge的收藏夹。"
          >
            <el-button
                slot="reference"
                icon="el-icon-folder"
                circle
                style="margin-top: 25%"
            ></el-button>
            <div v-if="check">
              <favorites
                  :username="user.username"
                  :addToFavorite="0"
                  :key="timer"
                  ref="favorites"
              ></favorites>
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
            </div>
          </el-popover>
          <span>&nbsp;&nbsp;</span>
          <el-popover
              placement="bottom"
              title="个人信息"
              width="300"
              trigger="click"
              content="将来放关于用户的信息。"
          >
            <el-button slot="reference" icon="el-icon-user" circle
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
    </el-header>
<!--el body部分-->
    <el-main>
      <el-row style="display: flex;justify-content: center">
        <!--        搜索框列-->
        <div align="center"
             style="
                    display: flex;
                    margin-bottom: 15px;
                    align-items: center;">
          <el-button>1</el-button>
          <div class="input" style="text-align: center;margin-top: 2%;margin-bottom: 2%">
            <el-autocomplete v-model="search_word" style="width: 700px;"
                             :fetch-suggestions="querySearchAsync" @select="handleSelect"
                             placeholder="请输入搜索内容" prefix-icon="el-icon-search"
                             @keyup.enter.native="search">
              <template v-slot:suffix>
                <el-button
                    style="margin-right: 10px; margin-top: 5px"
                    slot="suffix"
                    type="text"
                    @click="searcher"
                >搜索</el-button
                >
              </template>
            </el-autocomplete>
          </div>
        </div>
      </el-row>
      <el-col
        v-loading="loading"
        element-loading-text="拼命加载中"
        element-loading-spinner="el-icon-loading"
        element-loading-background="rgb(255 255 255)"
      >
        <div style="display: flex;background-color:rgba(203,180,134,.2);height: 60px;margin-bottom: 1%" >
          <el-col :span="2" id="gen">
            <span>&nbsp;</span>
          </el-col>
          <div
            style="margin-right: 15px;height: 60px;font-size: 20px;line-height: 60px;font-weight: bolder;color: black"
            id="text"
            :class="{ selectedOne: picture_text === 1 }"
            @click="tranfer1('text')"
          >
            全部
          </div>
          <div
            style="margin-right: 15px;height: 60px;font-size: 20px;line-height: 60px;font-weight: bolder;color: black"
            :class="{ selectedOne: picture_text === 2 }"
            @click="tranfer1('picture')"
          >
            要闻动态
          </div>
          <div

            :class="{
              selectedOne: picture_text === 3,
              picToPic: picTopic1 === false,
            }"
            @click="tranfer1('picToPic')"
          >
            政务公开
          </div>
        </div>
        <el-row>
          <el-col :span="2" :class="{ picToPic: picture_text == 3 }">
            <span>&nbsp;</span>
          </el-col>
          <!-- 搜索结果 -->

<!--          第一类-->
          <el-col :span="10" v-if="picture_text == 1">
            <dl>
              <div v-if="recordsNum != 0">
                <div
                  v-for="(item) in imgAndCaption"
                  align="left"
                  style="
                    /*height: 100px;*/
                    display: flex;
                    margin-bottom: 15px;
                    align-items: center;
                    border-bottom: solid 1px #eee;
                  "
                >
                  <div style="display: flex; align-items: center"> <!-- 收藏按钮 -->
                    <el-button
                        @click="addToFavorite(item)"
                        style="margin-right: 10px;"
                        icon="el-icon-star-off" circle
                    ></el-button
                    >
<!--                    a标签中 作者本身写的 有匹配的挑战Title-->
<!--                    <a-->
<!--                        :href="item.url"-->
<!--                        target="_blank"-->
<!--                        style="white-space: nowrap; word-break: break-all"-->
<!--                    >-->
<!--                      <h3 v-html="lightFn(item.caption, search_word_not_contain_filter)"></h3>-->
<!--                    </a>-->
<!--                    <h3 @click="seach">{{item.POLICY_TITLE}}</h3>-->
                    <el-link style="font-weight: bolder;font-size: 20px" target="_blank" @click="intoContent(item)">{{item.POLICY_TITLE}}</el-link>
                  </div>
                  <div class="article_caption" style="margin-left: 50px;">
<!--                    <p class="article_caption">{{item.POLICY_BODY}}</p>-->
                    {{item.POLICY_BODY}}
                  </div>
                  <div style="margin-left: 50px;margin-top: 10px;margin-bottom: 10px">
                    <small class="small_time">发布机构: {{item.PUB_AGENCY}}</small>
                    <small class="small_time" style="margin-left: 20px">发布时间: {{item.PUB_TIME}}</small>
                  </div>


                </div>
              </div>
              <div v-if="recordsNum == 0">
                <div style="display: flex; margin-bottom: 15px">
                  <div>
                    <h1>
                      抱歉没有找到与<span style="color: #55ab41">{{
                        search_word
                      }}</span
                      >相关的文本。
                    </h1>
                  </div>
                </div>
              </div>
            </dl>
          </el-col>
          <el-col :span="11">
            <h3>热点政策预留</h3>
            <h3>推荐位置预留</h3>
          </el-col>

<!--          第二类-->
          <el-col style="max-width: 1200px" v-if="picture_text == 2">
            <dl>
              <div
                v-if="recordsNum != 0"
                style="
                  display: flex;
                  flex-wrap: wrap;
                  justify-content: flex-start;
                "
              >
                <div v-for="item in imgAndCaption" align="left" class="P_item">
                  <div>
                    <img
                      style="height: 200px; border-radius: 10%"
                      :src="item.url"
                      :alt="item.caption"
                    />
                    <p
                      style="
                        font-size: 10px;
                        overflow: hidden;
                        word-break: keep-all;
                        white-space: nowrap;
                        text-overflow: ellipsis;
                      "
                      :style="'width:' + item.width"
                      v-html="lightFn(item.caption, search_word_not_contain_filter)"
                    ></p>
                  </div>
                </div>
              </div>
              <div v-if="recordsNum == 0">
                <div style="display: flex; margin-bottom: 15px">
                  <div>
                    <h1>
                      抱歉没有找到与<span style="color: #55ab41">{{
                        search_word
                      }}</span
                      >相关的图片。
                    </h1>
                  </div>
                </div>
              </div>
            </dl>
          </el-col>

<!--          第三类-->
          <el-col :span="11" v-if="picture_text == 3">
            <dl>
              <div
                style="
                  display: flex;
                  flex-wrap: wrap;
                  justify-content: flex-start;
                  min-width: 850px;
                "
              >
                <div v-for="item in imgAndCaption1" align="left">
                  <img
                    style="
                      height: 200px;
                      margin-right: 15px;
                      border-radius: 10%;
                    "
                    :src="item.url"
                  />
                </div>
              </div>
            </dl>
          </el-col>
        </el-row>


        <el-row>
          <!-- 相关搜索 -->
          <h3 style="width: 100px; margin-left: 100px; color: #248e24">
            相关搜索
          </h3>
          <div id="allrelated">
            <div
              class="related"
              v-for="rw in relatedWord"
              @click="searchRelated({ rw })"
            >
              {{ rw }}
            </div>
          </div>
        </el-row>

<!--        换页-->
        <el-row style="bottom: 0">
          <el-col :span="2">
            <span>&nbsp;</span>
          </el-col>
          <el-col :span="11">
            <div align="left">
              <el-pagination
                background
                @current-change="handleCurrentChange"
                :current-page="pageNum"
                :page-size="15"
                layout="prev, pager, next, jumper"
                :total="recordsNum"
              >
              </el-pagination>
            </div>
          </el-col>
          <el-col :span="12"> </el-col>
        </el-row>

      </el-col>
    </el-main>

    <div v-if="check">
      <div v-show="false">
        <favorites
            :username="user.username"
            :addToFavorite="1"
            ref="favorites"
            @notShowDialog="change"
        ></favorites>
      </div>
      <el-dialog
        title="请选择添加位置"
        :visible.sync="addToFavoritedialogVisible"
        width="20%"
      >
        <favorites
          :username="user.username"
          :addToFavorite="1"
          ref="favorites"
          @notShowDialog="change"
        ></favorites>
        <span>
          <br />
        </span>
      </el-dialog>
    </div>
  </el-container>
</template>

<script>
import axios from "axios";
import favorites from "./Favorites";

export default {
  components: { favorites },
  data() {
    const item = {
      message: "123",
    };
    return {
      upImage: [],
      loading: true,
      user: "",
      check: false,
      search_word: "",
      search_word1: "",
      search_word_not_contain_filter: "",
      info: "",
      imgAndCaption: [
          {url:'http://localhost:8080/content',
            id:'1',
            POLICY_TITLE:'房地产估价师注册证书遗失作废声明',
            PUB_AGENCY:'住房和城乡建设部房地产市场监管司',
            PUB_TIME:'2022/8/19',
            POLICY_TYPE:'其他',
            POLICY_BODY:'根据《住房和城多建设部关于取消部分部门规壹和规范性文体没定的证明李项的决定》 (法规219) 6号)的规定，现将等2名房地产价师注册证书遗失信息予,房地产估价师注册证书遗失作废声明' +
                '房地产估价师注册证书遗失作废声明根据《住房和城多建设部关于取消部分部门规壹和规范性文体没定的证明李项的决定》 (法规219) 6号)的规定，现将等2名房地产价师注册证书遗失信息予,房地产估价师注册证书遗失作废声明' +
                '房地产估价师注册证书遗失作废声明根据《住房和城多建设部关于取消部分部门规壹和规范性文体没定的证明李项的决定》 (法规219) 6号)的规定，现将等2名房地产价师注册证书遗失信息予,房地产估价师注册证书遗失作废声明' +
                '房地产估价师注册证书遗失作废声明根据《住房和城多建设部关于取消部分部门规壹和规范性文体没定的证明李项的决定》 (法规219) 6号)的规定，现将等2名房地产价师注册证书遗失信息予,房地产估价师注册证书遗失作废声明' +
                '房地产估价师注册证书遗失作废声明根据《住房和城多建设部关于取消部分部门规壹和规范性文体没定的证明李项的决定》 (法规219) 6号)的规定，现将等2名房地产价师注册证书遗失信息予,房地产估价师注册证书遗失作废声明' +
                '房地产估价师注册证书遗失作废声明根据《住房和城多建设部关于取消部分部门规壹和规范性文体没定的证明李项的决定》 (法规219) 6号)的规定，现将等2名房地产价师注册证书遗失信息予,房地产估价师注册证书遗失作废声明' +
                '房地产估价师注册证书遗失作废声明根据《住房和城多建设部关于取消部分部门规壹和规范性文体没定的证明李项的决定》 (法规219) 6号)的规定，现将等2名房地产价师注册证书遗失信息予,房地产估价师注册证书遗失作废声明' +
                '房地产估价师注册证书遗失作废声明根据《住房和城多建设部关于取消部分部门规壹和规范性文体没定的证明李项的决定》 (法规219) 6号)的规定，现将等2名房地产价师注册证书遗失信息予,房地产估价师注册证书遗失作废声明' +
                '房地产估价师注册证书遗失作废声明根据《住房和城多建设部关于取消部分部门规壹和规范性文体没定的证明李项的决定》 (法规219) 6号)的规定，现将等2名房地产价师注册证书遗失信息予,房地产估价师注册证书遗失作废声明' +
                '房地产估价师注册证书遗失作废声明',
            caption:'房地产估价师注册证书遗失作废声明',
      },
        {
          url:'http://localhost:8080/content',
          id:'1',
          POLICY_TITLE:'房地产估价师注册证书遗失作废声明',
          PUB_AGENCY:'住房和城乡建设部房地产市场监管司',
          PUB_TIME:'2022/8/19',
          POLICY_TYPE:'其他',
          POLICY_BODY:'根据《住房和城多建设部关于取消部分部门规壹和规范性文体没定的证明李项的决定》 (法规219) 6号)的规定，现将等2名房地产价师注册证书遗失信息予',
          caption:'房地产估价师注册证书遗失作废声明',
        }
      ],
      imgAndCaption1: [],
      relatedWord: [],
      pageNum: 1,
      recordsNum: 2,
      picture_text: 1,
      picTopic1: false,
      addToFavoritedialogVisible: false,
      timer: ''
    };
  },
  created() {
    this.user = JSON.parse(window.localStorage.getItem("access"));
    if (this.user != null) {
      this.check = true;
    }
  },
  mounted() {
    // 不知道为什么只有这样才能获取第一次的值
    this.addToFavoritedialogVisible = true;
    setTimeout(() => {
      this.addToFavoritedialogVisible = false;
    }, 1);
    this.getFirstPage();
    this.getRelatedWord();
    if (window.localStorage.getItem("access") != null) {
      this.checkToken();
    }
    this.checkLoading();
    setInterval(() => {
      this.checkToken();
    }, 1000 * 60 * 10); //每十分钟检查token
  },
  methods: {
    // =========收藏夹========
    addToFavorite(item) {
      if (localStorage.getItem("access") == null) {
        this.$message.error("请先登录");
      }else{
        var url = item.url;
        var caption = item.caption;
        this.$refs.favorites.addFavorite(url, caption);
        this.addToFavoritedialogVisible = true;
      }
    },
    change() {
      this.addToFavoritedialogVisible = false;
    },
    // =======收藏夹结束======
    checkLoading() {
      const _this = this;
      setInterval(() => {
        if (
          this.search_word != null &&
          this.imgAndCaption != null &&
          this.relatedWord != null
        ) {
          _this.loading = false;
          clearInterval();
        }
      }, 20);
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
              this.$message.error("登录已失效，请重新登录");
            }
          });
      }
    },
    async logout() {
      var _this = this;
      var jwt = JSON.parse(window.localStorage.getItem("access"));
      if (jwt != null) {
        await axios
          .get(
            "http://localhost:9090/user/logout?username=" +
              jwt.username +
              "&token=" +
              jwt.token
          )
          .then(function (response) {
            if (response.data.message == "success") {
              _this.$message({
                message: "退出成功",
                type: "success",
              });
              window.localStorage.removeItem("access");
              _this.check = false;
              setTimeout(() => {
                location.reload();
              }, 3000);
            }
          });
      }else{
        location.reload();
      }
    },
    lightFn(originStr, target) {
      return originStr.replace(
        target,
        '<span style="color:red;">' + target + "</span>"
      );
    },
    //以图搜图的展示
    //上传图片后的回调函数
    handleAvatarSuccess(response) {
      if (response[0] == "图片上传失败!") {
        this.$message.error("图片上传失败!");
      } else {
        this.picTopic1 = true;
        this.imgAndCaption1 = []; //<========================赋值到此
        for (let i = 0; i < response.length; i++) {
          this.imgAndCaption1.push({
            url: response[i],
          });
        }
      }
    },
    beforeAvatarUpload(file) {
      const isJPG = file.type === "image/jpeg";
      const isLt2M = file.size / 1024 / 1024 < 2;

      if (!isJPG) {
        this.$message.error("上传头像图片只能是 JPG 格式!");
      }
      if (!isLt2M) {
        this.$message.error("上传头像图片大小不能超过 2MB!");
      }
      return isJPG && isLt2M;
    },
    intoContent(item){
      this.$router.push({
        path: "/content",
        query: {
          id:item.id,
          POLICY_TITLE:item.POLICY_TITLE,
          PUB_AGENCY:item.PUB_AGENCY,
          PUB_TIME:item.PUB_TIME,
          POLICY_TYPE:item.POLICY_TYPE,
          POLICY_BODY:item.POLICY_BODY,
          caption:item.caption,
        },
      });
    },

    //文本栏，图片栏,搜图栏切换
    tranfer1(val) {
      if (val == "text") {
        this.picture_text = 1;
      }
      if (val == "picture") {
        this.picture_text = 2;
      }
      if (val == "picToPic") {
        this.picture_text = 3;
      }
    },
    searcher() {
      this.$router.push({
        path: "/search",
        query: {
          word: this.search_word,
        },
      });
      location.reload();
    },
    async search() {
      this.pageNum = 1;
      let outer = this;

      this.search_word1 = this.search_word;
      this.search_word_not_contain_filter = this.search_word
      var filter = /^-.*?$/
      var strs = this.search_word_not_contain_filter.trim().split(/\s+/)
      var idx =  -1;
      for (let i = 0; i < strs.length; i++) {
        if (filter.test(strs[i])) {
          idx = this.search_word_not_contain_filter.indexOf(strs[i])
          break;
        }
      }
      if (idx != -1) {
        this.search_word_not_contain_filter = this.search_word_not_contain_filter
            .substring(0, idx).replace(/(^\s*)|(\s*$)/g, "")
      }

      await axios
        .get(
          "http://localhost:8081/bm25/search?keyword=" +
            outer.search_word +
            "&pageNum=" +
            outer.pageNum
        )
        .then((response) => (outer.info = response.data));
      if (this.info != "") {
        this.imgAndCaption = []
        this.recordsNum = this.info.length;
        for (let i = 0; i < this.info.length; i++) {
          this.imgAndCaption.push({
            id:this.info[i].id,
            POLICY_TITLE: this.info[i].policyTitle,
            PUB_AGENCY:this.info[i].pubAgency,
            PUB_TIME:this.info[i].pubTime,
            POLICY_TYPE:this.info[i].policyType,
            POLICY_BODY:this.info[i].policyBody,
          });
        }
      } else {
        this.imgAndCaption = [];
        this.recordsNum = 0;
      }
      this.getRelatedWord();
    },
    async searchRelated(word) {
      this.$router.push({
        path: "/search",
        query: {
          word: word.rw,
        },
      });
      location.reload();
    },
    async getFirstPage() {
      this.recordsNum = this.$route.query.recordsNum;
      this.search_word = this.$route.query.word;
      this.search_word1 = this.$route.query.word;

      // 以下代码用于查询结果中过滤词不变红
      this.search_word_not_contain_filter = this.search_word
      var filter = /^-.*?$/
      var strs = this.search_word_not_contain_filter.trim().split(/\s+/)
      var idx =  -1;
      for (let i = 0; i < strs.length; i++) {
        if (filter.test(strs[i])) {
          idx = this.search_word_not_contain_filter.indexOf(strs[i])
          break;
        }
      }
      if (idx != -1) {
        this.search_word_not_contain_filter = this.search_word_not_contain_filter
            .substring(0, idx).replace(/(^\s*)|(\s*$)/g, "")
      }

      let outer = this;
      await axios
        .get(
          "http://localhost:8081/bm25/search?keyword=" +
            this.search_word +
            "&pageNum=1"
        )
        .then((response) => (outer.info = response.data.result));
      if (this.info != "") {
        this.imgAndCaption = []
        this.recordsNum = this.info.length;
        for (let i = 0; i < this.info.length; i++) {
          this.imgAndCaption.push({
            id:this.info[i].id,
            POLICY_TITLE: this.info[i].policyTitle,
            PUB_AGENCY:this.info[i].pubAgency,
            PUB_TIME:this.info[i].pubTime,
            POLICY_TYPE:this.info[i].policyType,
            POLICY_BODY:this.info[i].policyBody,
          });
        }
      } else {
        this.imgAndCaption = [];
        this.recordsNum = 0;
      }
    },
    async handleCurrentChange(val) {
      // console.log(`当前页: ${val}`);
      let outer = this;
      await axios
        .get(
          "http://localhost:8081/bm25/search?keyword=" +
            this.search_word +
            "&pageNum=" +
            val
        )
        .then((response) => (outer.info = response.data));
      this.imgAndCaption = []
      this.recordsNum = this.info.length;
      for (let i = 0; i < this.info.length; i++) {
        this.imgAndCaption.push({
          id:this.info[i].id,
          POLICY_TITLE: this.info[i].policyTitle,
          PUB_AGENCY:this.info[i].pubAgency,
          PUB_TIME:this.info[i].pubTime,
          POLICY_TYPE:this.info[i].policyType,
          POLICY_BODY:this.info[i].policyBody,
        });
      }
    },
    async getRelatedWord() {
      let outer = this;
      await axios
        .get("http://localhost:9090/related_word?word=" + outer.search_word)
        .then((response) => (outer.info = response.data));
      this.relatedWord = this.info;
    },
    async querySearchAsync(queryString, cb) {
      clearTimeout(this.timeout);
      var data = []
      var results = []
      if (queryString == '') {
        cb(results);
      } else {
        await axios
            .get("http://localhost:9090/prefix_word?word=" + this.search_word)
            .then((response) => {
              data = response.data
            })
        for (let i = 0; i < data.length; i++) {
          results.push({
            value: data[i]
          })
        }
        cb(results)
      }
    },
    //点击出现搜索后点击的每一项
    handleSelect(item) {
      this.id = item.id
      this.name = item.value
      this.search()
    },
  },
};
</script>

<style>
.little_button{
 position: absolute;
  left: 35%;

}

.header {
  /*background-color: #B3C0D1;*/
  display: flex;
  position: absolute;
  width: 30%;
  height: 5px;
}

.article_caption{
  width: 750px;
  overflow: hidden;
  text-overflow: ellipsis;

  -webkit-line-clamp: 2;
  display: -webkit-box;
  -webkit-box-orient: vertical;

}


.small_time{
  color: #98999B;
  font-size: 16px;

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
