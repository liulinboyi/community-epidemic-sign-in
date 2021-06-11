import {createRouter, createWebHashHistory} from 'vue-router'
// eslint-disable-next-line
import {store} from './utils/store.js'
// import Scanf from "./pages/Scanf.vue"
const routes = [
    {
        name: 'Home',
        path: '/',
        component: () => import ('./pages/Home.vue'),
        children: [
            {
                name: "Entry",
                path: "/",
                component: () => import ('./pages/Entry.vue')
            }, {
                name: "account",
                path: "/account",
                component: () => import ('./pages/Account.vue')
            }
        ]
    }, {
        name: 'sign',
        path: '/sign',
        meta: {
            top: 0
        },
        component: () => import ('./pages/Sign.vue')
    }, {
        name: "sign-info",
        path: "/sign-info",
        component: () => import ('./pages/SignInfo.vue')
    }, {
        name: "scanf",
        path: "/scanf",
        component: () => import ("./pages/Scanf.vue")
    }
]

export const router = createRouter({
    history: createWebHashHistory(),
    routes,
    scrollBehavior(to, from, savedPosition) { // console.log(to, from, store.state)
        if (savedPosition) { // eslint-disable-next-line
            return new Promise((resolve, reject) => {
                setTimeout(() => {
                    resolve(savedPosition)
                }, 500)
            })
        } else { // eslint-disable-next-line
            return new Promise((resolve, reject) => {
                setTimeout(() => {
                    if (to.path === "/sign") { // console.log("这个路由是 /sign")
                        resolve({top: 0})
                        // resolve({
                        //     // 也可以这么写
                        //     // el: document.getElementById('main'),
                        //     el: "#app",
                        //     top: -store.state.top
                        // })
                    } else {
                        resolve({top: 0})
                    }
                }, 500)
            })
        }
    }
})

router.beforeResolve((to, from, next) => { // 每次进入路由前，读取用户信息
    let userInfo = localStorage.getItem("userInfo");
    if (userInfo) {
        userInfo = JSON.parse(userInfo);
        store.setUserInfoAction(userInfo);
    }
    next()
})
