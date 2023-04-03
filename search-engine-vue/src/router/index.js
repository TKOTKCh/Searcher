import Vue from 'vue'
import Router from 'vue-router'
import content from "../components/Content";

Vue.use(Router)

const Home = () => import('../components/HomePage')
const SearchResult = () => import('../components/SearchResult')
const Login = () => import('../components/Login')
const Register = () => import('../components/Register')
const Content = () =>import('../components/Content')

const routes = [
    { path: '/', component: Home },
    { path: '/search', component: SearchResult },
    { path: '/login', component: Login },
    { path: '/register', component: Register },
    { path:'/content',component:Content},
    { path: '/*', redirect: '/' }  // 当匹配不上时重定向的到首页
]

const router = new Router({
    routes,
    mode: "history"
})

export default router