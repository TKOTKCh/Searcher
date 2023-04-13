<template>
  <div>
    <el-container style="width: 100%">
      <el-menu mode="horizontal"  style="background-color:#cbb486;" >
<!--        <el-menu-item index="1" style="margin-left: 42%;color: white;font-weight: bolder;font-size: larger" >首页</el-menu-item>-->
<!--        <el-menu-item index="2" style="color: white;font-weight: bolder;font-size: larger">要闻动态</el-menu-item>-->
        <el-menu-item index="3" disabled style="color: white;font-weight: bolder;font-size: larger"></el-menu-item>
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
            <el-button v-if="check" slot="reference"  icon="el-icon-user-solid" circle
                       style="margin-top: 25%;">
            </el-button>

            <el-button v-if="!check" slot="reference"  icon="el-icon-user" circle
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

      <!--el body部分-->
      <el-main>
        <!-- 选择地点的浮窗 -->
        <el-dialog title="地点筛选" :visible.sync="dialogTableVisible" style="height: 800px">
          <div style="margin-bottom: 50px" >
            <div style="display: flex">
              <h3>全部： </h3>
              <el-radio v-model="place" label="全部" border>全部</el-radio>
            </div>
            <div style="display: flex;margin-top: 40px">
              <h3>省份： </h3>
              <el-radio-group v-model="place" >
                <el-radio-button label="北京市"></el-radio-button>
                <el-radio-button label="上海市"></el-radio-button>
                <el-radio-button label="天津市"></el-radio-button>
                <el-radio-button label="重庆市"></el-radio-button>
                <el-radio-button label="河北省"></el-radio-button>
                <el-radio-button label="山西省"></el-radio-button>
                <el-radio-button label="黑龙江省"></el-radio-button>
                <el-radio-button label="吉林省"></el-radio-button>
                <el-radio-button label="辽宁省"></el-radio-button>
              </el-radio-group>
            </div>

            <div style="display: flex;margin-top: 40px" >
              <el-radio-group v-model="place" style="margin-left: 50px">
                <el-radio-button label="江苏省"></el-radio-button>
                <el-radio-button label="浙江省"></el-radio-button>
                <el-radio-button label="安徽省"></el-radio-button>
                <el-radio-button label="福建省"></el-radio-button>
                <el-radio-button label="江西省"></el-radio-button>
                <el-radio-button label="山东省"></el-radio-button>
                <el-radio-button label="河南省"></el-radio-button>
                <el-radio-button label="湖北省"></el-radio-button>
                <el-radio-button label="湖南省"></el-radio-button>
              </el-radio-group>
            </div>
            <div style="display: flex;margin-top: 40px">
              <el-radio-group v-model="place" style="margin-left: 50px">
                <el-radio-button label="广东省"></el-radio-button>
                <el-radio-button label="海南省"></el-radio-button>
                <el-radio-button label="四川省"></el-radio-button>
                <el-radio-button label="贵州省"></el-radio-button>
                <el-radio-button label="云南省"></el-radio-button>
                <el-radio-button label="陕西省"></el-radio-button>
                <el-radio-button label="甘肃省"></el-radio-button>
                <el-radio-button label="青海省"></el-radio-button>
              </el-radio-group>
            </div>
            <div style="display: flex;margin-top: 40px">
              <el-radio-group v-model="place" style="margin-left: 50px;margin-bottom: 50px">
                <el-radio-button label="内蒙古自治区"></el-radio-button>
                <el-radio-button label="广西壮族自治区"></el-radio-button>
                <el-radio-button label="西藏自治区"></el-radio-button>
                <el-radio-button label="宁夏回族自治区"></el-radio-button>
                <el-radio-button label="新疆维吾尔自治区"></el-radio-button>
              </el-radio-group>
            </div>
          </div>
        </el-dialog>


        <el-row style="display: flex;justify-content: center">
          <!--        搜索框列-->
          <div align="center"
               style="
                    display: flex;
                    margin-bottom: 15px;
                    align-items: center;">

            <!--          地区选择按钮-->
            <el-button type="text" @click="dialogTableVisible = true" size="mini" icon="el-icon-location-information" style="height: 50px;margin-right: 30px;font-size: 20px;font-weight: bolder;color: #cbb486">{{place}}</el-button>

            <!--          搜索框-->
            <div class="input" style="text-align: center;margin-top: 2%;margin-bottom: 2%">
              <!--            :popper-append-to-body="false"-->
              <el-autocomplete v-model="search_word" style=""
                               :fetch-suggestions="querySearchAsync" @select="handleSelect"
                               placeholder="请输入搜索内容" prefix-icon="el-icon-search"
                               @keyup.enter.native="search">
                <template v-slot:suffix>
                  <el-button
                      style="margin-right: 10px; margin-top: 5px"
                      slot="suffix"
                      type="text"
                      @click="search"
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
            <!--          <div-->
            <!--            style="margin-right: 15px;height: 60px;font-size: 20px;line-height: 60px;font-weight: bolder;color: black"-->
            <!--            :class="{ selectedOne: picture_text === 2 }"-->
            <!--            @click="tranfer1('picture')"-->
            <!--          >-->
            <!--            要闻动态-->
            <!--          </div>-->
          </div>
          <el-row>
            <el-col :span="2" :class="{ picToPic: picture_text == 3 }">
              <span>&nbsp;</span>
            </el-col>
            <!-- 搜索结果 -->

            <!--          第一类-->
            <el-col :span="10" v-if="picture_text == 1" v-loading="loading">
              <div style="margin-bottom: 25px;display: flex;
                    font-size: 15px;
                    border-bottom: solid 1px #eee;">
                <el-dropdown @command="handle_type" style="font-size: 15px;color: black;margin-bottom: 5px;margin-right: 10px">
                <span class="el-dropdown-link" >
                  {{ q_type }}<i class="el-icon-arrow-down el-icon--right"></i>
                </span>
                  <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item command="a">全部</el-dropdown-item>
                    <el-dropdown-item command="b">方案办法</el-dropdown-item>
                    <el-dropdown-item command="c">请示答复</el-dropdown-item>
                    <el-dropdown-item command="d">通知公告</el-dropdown-item>
                    <el-dropdown-item command="e" >决定条例</el-dropdown-item>
                    <el-dropdown-item command="f" >其他</el-dropdown-item>
                  </el-dropdown-menu>
                </el-dropdown>

                <el-dropdown @command="handle_tableName" style="font-size: 15px;color: black;margin-bottom: 5px;margin-right: 10px">
                <span class="el-dropdown-link" >
                  {{tableName_mask}}<i class="el-icon-arrow-down el-icon--right"></i>
                </span>
                  <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item command="datatitle">标题</el-dropdown-item>
                    <el-dropdown-item command="databody">正文</el-dropdown-item>
                  </el-dropdown-menu>
                </el-dropdown>

                <el-dropdown @command="handle_time" style="font-size: 15px;color: black;margin-bottom: 5px;margin-right: 10px">
                <span class="el-dropdown-link" >
                  {{ time_mask }}<i class="el-icon-arrow-down el-icon--right"></i>
                </span>
                  <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item command="e">不限</el-dropdown-item>
<!--                    <el-dropdown-item command="a">2023</el-dropdown-item>-->
                    <el-dropdown-item command="b">2022</el-dropdown-item>
                    <el-dropdown-item command="c">2021</el-dropdown-item>
                    <el-dropdown-item command="d">2020</el-dropdown-item>
                    <el-dropdown-item command="f">2019</el-dropdown-item>
                  </el-dropdown-menu>
                </el-dropdown>
              </div>



              <dl>
                <div v-if="recordsNum != 0" >
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
                    <div style="display: flex; align-items: center;width: 900px">
                      <!-- 收藏按钮 -->
                      <!--                    <el-button-->
                      <!--                        @click="addToFavorite(item)"-->
                      <!--                        style="margin-right: 10px;"-->
                      <!--                        icon="el-icon-star-off" circle-->
                      <!--                    ></el-button>-->


                      <span class="tag">{{item.POLICY_TYPE}}</span>
                      <!--                    a标签中 作者本身写的 有匹配的挑战Title-->
                      <!--                    <a-->
                      <!--                        :href="item.url"-->
                      <!--                        target="_blank"-->
                      <!--                        style="white-space: nowrap; word-break: break-all"-->
                      <!--                    >-->
                      <!--                      <h3 v-html="lightFn(item.caption, search_word_not_contain_filter)"></h3>-->
                      <!--                    </a>-->
                      <!--                    <h3 @click="seach">{{item.POLICY_TITLE}}</h3>-->
                      <!--                    <el-link style="font-weight: bolder;font-size: 20px" target="_blank" @click="intoContent(item)">{{item.POLICY_TITLE}}</el-link>-->
                      <el-link class="title_caption" :underline='false' v-html="lightFn(item.POLICY_TITLE,search_word_not_contain_filter)" style="font-weight: bolder;font-size: 20px" target="_blank" @click="intoContent(item)"></el-link>
                    </div>
                    <div class="article_caption" style="margin-left: 80px;">
                      <!--                    <p class="article_caption">{{item.POLICY_BODY}}</p>-->
                      {{item.POLICY_BODY}}
                    </div>
                    <div style="margin-left: 80px;margin-top: 10px;margin-bottom: 10px">
                      <small class="small_time">发布机构: {{item.PUB_AGENCY}}</small>
                      <small class="small_time" style="margin-left: 20px">发布时间: {{item.PUB_TIME}}</small>
                    </div>


                  </div>
                </div>
                <div v-if="imgAndCaption.length == 0&&firstload" >
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


            <!--          右侧栏目-->
            <el-col :span="11">
              <div class="search-content-right">
                <div class="recommends-group">
                  <div class="r-g-til" style="position: relative;left: 0px">
                    <span style="display: flex;margin-left: 15px">热搜资源</span>
                  </div>
                  <div class="r-g-con">
                    <div class="r-g-toplist-cell">
                      <span class="r-g-index r-g-index-1">1</span>
                      <el-link class="right_content" v-html="this.hot[0].POLICY_TITLE" target="_blank" @click="intoContent(hot[0])"></el-link>
                      <span class="status keep"></span>
                    </div>
                    <div class="r-g-toplist-cell">
                      <span class="r-g-index r-g-index-2">2</span>
                      <el-link class="right_content" v-html="this.hot[1].POLICY_TITLE" target="_blank" @click="intoContent(hot[1])"></el-link>
                      <span class="status keep"></span>
                    </div>
                    <div class="r-g-toplist-cell">
                      <span class="r-g-index r-g-index-3">3</span>
                      <el-link class="right_content" v-html="this.hot[2].POLICY_TITLE" target="_blank" @click="intoContent(hot[2])"></el-link>
                      <span class="status keep"></span>
                    </div>
                    <div class="r-g-toplist-cell">
                      <span class="r-g-index r-g-index-4">4</span>
                      <el-link class="right_content" v-html="this.hot[3].POLICY_TITLE" target="_blank" @click="intoContent(hot[3])"></el-link>
                      <span class="status keep"></span>
                    </div>
                    <div class="r-g-toplist-cell">
                      <span class="r-g-index r-g-index-5">5</span>
                      <el-link class="right_content" v-html="this.hot[4].POLICY_TITLE" target="_blank" @click="intoContent(hot[4])"></el-link>
                      <span class="status keep"></span>
                    </div>
                    <div class="r-g-toplist-cell">
                      <span class="r-g-index r-g-index-6">6</span>
                      <el-link class="right_content" v-html="this.hot[5].POLICY_TITLE" target="_blank" @click="intoContent(hot[5])"></el-link>
                      <span class="status keep"></span>
                    </div>
                    <div class="r-g-toplist-cell">
                      <span class="r-g-index r-g-index-7">7</span>
                      <el-link class="right_content" v-html="this.hot[6].POLICY_TITLE" target="_blank" @click="intoContent(hot[6])"></el-link>
                      <span class="status keep"></span>
                    </div>
                    <div class="r-g-toplist-cell">
                      <span class="r-g-index r-g-index-8">8</span>
                      <el-link class="right_content" v-html="this.hot[7].POLICY_TITLE" target="_blank" @click="intoContent(hot[7])"></el-link>
                      <span class="status keep"></span>
                    </div>

                  </div>
                </div>
                <div class="recommends-group " style="">
                  <div v-if="check" class="r-g-til" style="position: relative;left: 0px">
                    <span  style="display: flex;margin-left: 15px">最近搜索记录</span>
                  </div>
                  <div class="r-g-con recent-search" id="searchHistoryList">
<!--                    <a class="r-g-link-cell" title="11" href="javascript:void(0)" onclick="searchKeyword(this)">11</a>-->
<!--                    <a class="r-g-link-cell" title="111" href="javascript:void(0)" onclick="searchKeyword(this)">111</a>-->
<!--                    <a class="r-g-link-cell" title="ddd" href="javascript:void(0)" onclick="searchKeyword(this)">ddd</a>-->
<!--                    <a class="r-g-link-cell" title="望月" href="javascript:void(0)" onclick="searchKeyword(this)">望月</a>-->
<!--                    <a class="r-g-clear" href="javascript:void(0);" onclick="removeSearchHistory()">清除所有搜索记录</a>-->
                    <div v-for="(item) in his" style="display: flex">
                      <el-link  v-html="item.keyword" style="font-size: 15px;margin-left: 15px" target="_blank" @click="his_search(item.keyword)"></el-link>
                    </div>
                  </div>
                </div>
              </div>
            </el-col>

            <!--          第二类-->
            <!--          <el-col style="max-width: 1200px" v-if="picture_text == 2">-->
            <!--            <dl>-->
            <!--              <div-->
            <!--                v-if="recordsNum != 0"-->
            <!--                style="-->
            <!--                  display: flex;-->
            <!--                  flex-wrap: wrap;-->
            <!--                  justify-content: flex-start;-->
            <!--                "-->
            <!--              >-->
            <!--                <div v-for="item in imgAndCaption" align="left" class="P_item">-->
            <!--                  <div>-->
            <!--                    <img-->
            <!--                      style="height: 200px; border-radius: 10%"-->
            <!--                      :src="item.url"-->
            <!--                      :alt="item.caption"-->
            <!--                    />-->
            <!--                    <p-->
            <!--                      style="-->
            <!--                        font-size: 10px;-->
            <!--                        overflow: hidden;-->
            <!--                        word-break: keep-all;-->
            <!--                        white-space: nowrap;-->
            <!--                        text-overflow: ellipsis;-->
            <!--                      "-->
            <!--                      :style="'width:' + item.width"-->
            <!--                      v-html="lightFn(item.caption, search_word_not_contain_filter)"-->
            <!--                    ></p>-->
            <!--                  </div>-->
            <!--                </div>-->
            <!--              </div>-->
            <!--              <div v-if="recordsNum == 0">-->
            <!--                <div style="display: flex; margin-bottom: 15px">-->
            <!--                  <div>-->
            <!--                    <h1>-->
            <!--                      抱歉没有找到与<span style="color: #55ab41">{{-->
            <!--                        search_word-->
            <!--                      }}</span-->
            <!--                      >相关的图片。-->
            <!--                    </h1>-->
            <!--                  </div>-->
            <!--                </div>-->
            <!--              </div>-->
            <!--            </dl>-->
            <!--          </el-col>-->

            <!--          第三类-->
            <!--          <el-col :span="11" v-if="picture_text == 3">-->
            <!--            <dl>-->
            <!--              <div-->
            <!--                style="-->
            <!--                  display: flex;-->
            <!--                  flex-wrap: wrap;-->
            <!--                  justify-content: flex-start;-->
            <!--                  min-width: 850px;-->
            <!--                "-->
            <!--              >-->
            <!--                <div v-for="item in imgAndCaption1" align="left">-->
            <!--                  <img-->
            <!--                    style="-->
            <!--                      height: 200px;-->
            <!--                      margin-right: 15px;-->
            <!--                      border-radius: 10%;-->
            <!--                    "-->
            <!--                    :src="item.url"-->
            <!--                  />-->
            <!--                </div>-->
            <!--              </div>-->
            <!--            </dl>-->
            <!--          </el-col>-->
          </el-row>


          <!--        <el-row>-->
          <!--          &lt;!&ndash; 相关搜索 &ndash;&gt;-->
          <!--          <h3 style="width: 100px; margin-left: 100px; color: #248e24">-->
          <!--            相关搜索-->
          <!--          </h3>-->
          <!--          <div id="allrelated">-->
          <!--            <div-->
          <!--              class="related"-->
          <!--              v-for="rw in relatedWord"-->
          <!--              @click="searchRelated({ rw })"-->
          <!--            >-->
          <!--              {{ rw }}-->
          <!--            </div>-->
          <!--          </div>-->
          <!--        </el-row>-->

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
                    :current-page.sync="pageNum"
                    :page-size="10"
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
  </div>

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
      q_type:'事项筛选',
      type:'',
      tableName_mask:'搜索范围',
      time_mask:'时间范围',
      time:'',
      user_historys:[
      ],
      tableName:'datatitle',
      dialogTableVisible:false,
      place:'全部',
      upImage: [],
      hot:[],
      hotbuffer:'',
      his:[],
      hisbuffer:'',
      loading: false,
      user: "",
      check: false,
      search_word: "",
      search_word1: "",
      search_word_not_contain_filter: "",
      info: "",
      imgAndCaption: [],
      imgAndCaption1: [],
      relatedWord: [],
      pageNum: 1,
      recordsNum: '',
      picture_text: 1,
      picTopic1: false,
      addToFavoritedialogVisible: false,
      timer: '',
      firstload:false,
    };
  },
  created() {
    this.bodyScale()
    this.user = JSON.parse(window.localStorage.getItem("access"));
    if (this.user != null) {
      this.check = true;
      console.log(this.user)
    }
  },
  mounted() {
    this.getFirstPage();
    this.getHot();
    this.getHis();
    // this.getRelatedWord();
    // if (window.localStorage.getItem("access") != null) {
    //   this.checkToken();
    // }
    // this.checkLoading();
    // setInterval(() => {
    //   this.checkToken();
    // }, 1000 * 60 * 10); //每十分钟检查token
  },
  watch:{
    place:{
      handler(){
        this.dialogTableVisible=false;
        this.search();
      }
    }
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
                    }, 3000);
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
      axios
          .post(
              "http://localhost:8081/bm25/count?id=" +
              item.id
          )
          .then((response) => (console.log(response)));


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
    handle_type(command){
      if(command=='a'){
        this.q_type='事项筛选';
        this.type='';


      //  请求
      }
      if(command=='b'){
        this.q_type='方案办法';
        this.type='方案办法';
        //  请求
      }
      if(command=='c'){
        this.q_type='请示答复';
        this.type='请示答复';
        //  请求
      }
      if(command=='d'){
        this.q_type='通知公告';
        this.type='通知公告';
        //  请求
      }
      if(command=='e'){
        this.q_type='决定条例';
        this.type='决定条例';
        //  请求
      }
      if(command=='f'){
        this.q_type='其他';
        this.type='其他';
        //  请求
      }
      this.search();
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
      this.loading=true;
      console.log('进入异步搜索');
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

      if(this.check){
        axios
            .get(
                "http://localhost:8081/user/addUserQuery?userid=" + this.user.id
                + "&query=" + this.search_word
            )
            .then((response) => (console.log(response)));
      }

      //开始搜索逻辑
      this.$router.push({
        path: "/search",
        query: {
          word: this.search_word,
          province:this.province,
        },
      });
      // window.location.reload();

      if(this.place!='全部'){
        await axios
            .get(
                "http://localhost:8081/bm25/search_condition?keyword=" +
                this.search_word + "&tableName=" + this.tableName +
                "&pageNum=1" + "&province=" + this.place +"&type=" + this.type+"&year="+this.time
            )
            .then((response) => (outer.info = response.data));
      }else{
        await axios
            .get(
                "http://localhost:8081/bm25/search_condition?keyword=" +
                this.search_word + "&tableName=" + this.tableName +
                "&pageNum=1" + "&province=" +"&type=" + this.type+"&year="+this.time
            )
            .then((response) => (outer.info = response.data));
      }

      if (this.info.data&&this.info.data&&this.info.data.count!=0) {
        this.imgAndCaption = []
        this.recordsNum = this.info.data.count;
        for (let i = 0; i < this.info.data.data.length; i++) {
          this.imgAndCaption.push({
            id:this.info.data.data[i].id,
            POLICY_TITLE: this.info.data.data[i].policyTitle,
            PUB_AGENCY:this.info.data.data[i].pubAgency,
            PUB_TIME:this.info.data.data[i].pubTime,
            POLICY_TYPE:this.info.data.data[i].policyType,
            POLICY_BODY:this.info.data.data[i].policyBody,
          });
        }
      } else {
        this.imgAndCaption = [];
        this.recordsNum = 0;
      }
      this.getHis()
      this.loading=false;
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

    async getFirstPage_b() {
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
      //发送搜索请求
      let outer = this;
      await axios
        .get(
          "http://localhost:8081/bm25/search_condition?keyword=" +
            this.search_word + "&tableName=" + this.tableName +
            "&pageNum=1" + "&province=" +"&type="+"&year="
        )
        .then((response) => (outer.info = response.data));

      // 输出搜索记录
      if(this.check){
        axios
            .get(
                "http://localhost:8081/user/addUserQuery?userid=" + this.user.id
                + "&query=" + this.search_word
            )
            .then((response) => (console.log(response)));
      }

      if (this.info.data&&this.info.data.count!=0) {
        this.imgAndCaption = []
        this.recordsNum = this.info.data.count;
        for (let i = 0; i < this.info.data.data.length; i++) {
          this.imgAndCaption.push({
            id:this.info.data.data[i].id,
            POLICY_TITLE: this.info.data.data[i].policyTitle,
            PUB_AGENCY:this.info.data.data[i].pubAgency,
            PUB_TIME:this.info.data.data[i].pubTime,
            POLICY_TYPE:this.info.data.data[i].policyType,
            POLICY_BODY:this.info.data.data[i].policyBody,
          });
        }
      } else {
        this.imgAndCaption = [];
        this.recordsNum = 0;
      }
    },

    async getFirstPage() {
      this.loading=true;
      this.recordsNum = this.$route.query.recordsNum;
      this.search_word = this.$route.query.word;
      this.search_word1 = this.$route.query.word;
      this.province=this.$route.query.province;

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
      //发送搜索请求
      let outer = this;
      await axios
          .get(
              "http://localhost:8081/bm25/search_condition",{
                params:{
                  keyword:this.search_word,
                  tableName:this.tableName,
                  pageNum:1,
                  province:'',
                  type:'',
                  year:'',
                  user:this.user
                }
              }
          )
          .then((response) => (outer.info = response.data));

      // 输出搜索记录
      if(this.check){
        axios
            .get(
                "http://localhost:8081/user/addUserQuery?userid=" + this.user.id
                + "&query=" + this.search_word
            )
            .then((response) => (console.log(response)));
      }

      if (this.info.data&&this.info.data.count!=0) {
        this.imgAndCaption = []
        this.recordsNum = this.info.data.count;
        for (let i = 0; i < this.info.data.data.length; i++) {
          this.imgAndCaption.push({
            id:this.info.data.data[i].id,
            POLICY_TITLE: this.info.data.data[i].policyTitle,
            PUB_AGENCY:this.info.data.data[i].pubAgency,
            PUB_TIME:this.info.data.data[i].pubTime,
            POLICY_TYPE:this.info.data.data[i].policyType,
            POLICY_BODY:this.info.data.data[i].policyBody,
          });
        }
      } else {
        this.imgAndCaption = [];
        this.recordsNum = 0;
      }
      this.loading=false;
      this.firstload=true;
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

    //处理搜搜范围转变
    handle_tableName(command){
      this.tableName=command;
      this.search();
      if(command=='datatitle'){this.tableName_mask='标题'}
      if(command=='databody'){this.tableName_mask='正文'}
    },

    //时间
    handle_time(command){
      if(command=='e'){
        this.time_mask='时间范围'
        this.time='';
      }
      if(command=='a'){
        this.time_mask='2023年'
        this.time='2023';
      }
      if(command=='b'){
        this.time_mask='2022年'
        this.time='2022';
      }
      if(command=='c'){
        this.time_mask='2021年'
        this.time='2021';
      }
      if(command=='d'){
        this.time_mask='2020年'
        this.time='2020';
      }
      if(command=='f'){
        this.time_mask='2019年'
        this.time='2019';
      }
      this.search();
    },

    async handleCurrentChange(val) {
      this.loading=true;
      let outer = this;
      if(this.place!='全部'){
        await axios
            .get(
                "http://localhost:8081/bm25/search_condition?keyword=" +
                this.search_word + "&tableName=" + this.tableName +
                "&pageNum="+val + "&province=" + this.place +"&type=" + this.type+"&year="+this.time
            )
            .then((response) => (outer.info = response.data));
      }else{
        await axios
            .get(
                "http://localhost:8081/bm25/search_condition?keyword=" +
                this.search_word + "&tableName=" + this.tableName +
                "&pageNum="+val + "&province=" +"&type=" + this.type+"&year="+this.time
            )
            .then((response) => (outer.info = response.data));
      }
      if (this.info.data&&this.info.data.count!=0) {
        this.imgAndCaption = []
        this.recordsNum = this.info.data.count;
        for (let i = 0; i < this.info.data.data.length; i++) {
          this.imgAndCaption.push({
            id:this.info.data.data[i].id,
            POLICY_TITLE: this.info.data.data[i].policyTitle,
            PUB_AGENCY:this.info.data.data[i].pubAgency,
            PUB_TIME:this.info.data.data[i].pubTime,
            POLICY_TYPE:this.info.data.data[i].policyType,
            POLICY_BODY:this.info.data.data[i].policyBody,
          });
        }
      } else {
        this.imgAndCaption = [];
        this.recordsNum = 0;
      }
      this.loading=false;
    },
    // async getRelatedWord() {
    //   let outer = this;
    //   await axios
    //     .get("http://localhost:9090/related_word?word=" + outer.search_word)
    //     .then((response) => (outer.info = response.data));
    //   this.relatedWord = this.info;
    // },
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
    his_search(keyword) {
      // 需要请求数据来显示
      this.$router.push({
        path: "/search",
        query: {
          word:keyword,
        },
      });
      window.location.reload();
    },

    //获取热门
    async getHot(){
      let outer = this;
      await axios
          .get(
              "http://localhost:8081/bm25/hot"
          )
          .then((response) => (outer.hotbuffer = response.data.data));
      if (this.hotbuffer) {
        console.log(this.hotbuffer)
        this.hot = []
        for (let i = 0; i < this.hotbuffer.length; i++) {
          this.hot.push({
            id:this.hotbuffer[i].id,
            POLICY_TITLE: this.hotbuffer[i].policyTitle,
            PUB_AGENCY:this.hotbuffer[i].pubAgency,
            PUB_TIME:this.hotbuffer[i].pubTime,
            POLICY_TYPE:this.hotbuffer[i].policyType,
            POLICY_BODY:this.hotbuffer[i].policyBody,
          });
        }
      }else{

      }
    },

    //获取历史记录
    async getHis(){
      if(this.check){
        let outer = this;
        await axios
            .get(
                "http://localhost:8081/user/getLastQuery?userid=" +this.user.id
            )
            .then((response) => (outer.hisbuffer = response.data.data));
        if (this.hisbuffer) {
          this.his=[]
          for (let i = 0; i < this.hisbuffer.length; i++) {
            this.his.push({
              keyword:this.hisbuffer[i],
            });
          }
        }else{

        }
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

/*热门搜索样式*/
.search-content-right {
  margin-right: 30%;
  float: right;
  width: 334px;
  border-left: 1px solid #eee;
}
.r-g-con {

  overflow: hidden;
}
.recent-search .r-g-link-cell {
  line-height: 32px;
}

.right_content{
  font-size: 15px;
  color: #666;
  width: 268px;
  white-space: nowrap;
  text-overflow: ellipsis;
  display: inline-block;
  overflow: hidden;
  line-height: 20px;
  margin-bottom: 10px;
}

.search-content-right > * {
  margin: 0 0 20px 15px;
}
.r-g-til {
  margin-bottom: 10px;
  overflow: hidden;
}
.r-g-til > span {
  font-weight: bold;
  font-size: 18px;
}

.r-g-toplist-cell + .r-g-toplist-cell {
  margin-top: 10px;
}

.r-g-index {
  width: 20px;
  background: #cccccc;
  color: #fff;
  text-align: center;
}
a {
  color: #666;
}

.r-g-con {
  overflow: hidden;
}
.r-g-index.r-g-index-1 {
  background: #fc7a43;
}
.r-g-toplist-cell > a {
  width: 268px;
  white-space: nowrap;
  text-overflow: ellipsis;
}
.r-g-toplist-cell > * {
  display: inline-block;
  overflow: hidden;
  line-height: 20px;
}
.r-g-index.r-g-index-2 {
  background: #fdab4d;
}
/*end*/



/*筛选样式*/
.el-dropdown-link {
  cursor: pointer;
  color: black;
  font-size: 16px;

}
.el-icon-arrow-down {
  font-size: 12px;
}
/*end*/

.tag {
  position: relative;
  display: inline-block;
  font-size: 12px;
  padding: 4px 10px;
  color: #000;
  line-height: 1;
  background-color: #fff;
  border: 1px solid #cbb486;
  color: #cbb486;
  margin-right: 5px;
}

.el-autocomplete{
  width: 700px;
  background-color: rgba(255, 255, 255, 0.247);

}

.el-radio-button{

  transform: scale(1.1);

}

.little_button{
 position: absolute;
  right: 0;

}

.header {
  /*background-color: #B3C0D1;*/
  display: flex;
  position: absolute;
  width: 30%;
  height: 5px;
}

.r-g-index.r-g-index-3 {
  background: #d5c39e;
}
.title_caption{
  width: 750px;
  overflow: hidden;
  text-overflow: ellipsis;
  -webkit-line-clamp: 1;
  display: -webkit-box;
  -webkit-box-orient: vertical;

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
