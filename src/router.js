import {createRouter, createWebHashHistory} from 'vue-router'

const routes = [
    {
        name: 'entry',
        path: '/',
        component: () => import ('./pages/Entry.vue')
    }, {
        name: 'sign',
        path: '/sign',
        component: () => import ('./pages/Sign.vue')
    }, {
        name: "sign-info",
        path: "/sign-info",
        component: () => import ('./pages/SignInfo.vue')
    }, {
        name: "account",
        path: "/account",
        component: () => import ('./pages/Account.vue')
    }
]

export const router = createRouter({history: createWebHashHistory(), routes})
