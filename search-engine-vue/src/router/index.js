import Vue from 'vue'
import Router from 'vue-router'
import content from "../components/Content";

Vue.use(Router)

const Home = () => import('../components/HomePage')
const SearchResult = () => import('../components/SearchResult')
const Login = () => import('../components/Login')
const Register = () => import('../components/Register')
const Content = () =>import('../components/Content')
const Administrator = () =>import('../components/administrator')
const Ad_login = () =>import('../components/admin_login')

const routes = [
    { path: '/', component: Home,meta:{requireAuth:false}},
    { path: '/search', component: SearchResult,meta:{requireAuth:false} },
    { path: '/login', component: Login,meta:{requireAuth:false} },
    { path: '/register', component: Register,meta:{requireAuth:false} },
    { path:'/content',component:Content,meta:{requireAuth:false}},
    { path:'/administrator',component:Administrator,meta:{requireAuth:true}},
    { path:'/ad_login',component:Ad_login,meta:{requireAuth:false}},
    { path: '/*', redirect: '/' }  // 当匹配不上时重定向的到首页
]



const router = new Router({
    routes,
    mode: "history"
})

router.beforeEach((to, from, next) => {
    var _this = this;
    if(to.meta.requireAuth ){
        if(JSON.parse(window.localStorage.getItem("access"))==null){
            console.log('没有登录')
            router.push({path: '/ad_login',query: {}})
            next()
        } else {
            // router.push({path: to.fullPath})
            next()
        }
    }
    else {
        next();
    }
})

export default router
