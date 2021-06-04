import {createRouter, createWebHashHistory} from 'vue-router'

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
        component: () => import ('./pages/Sign.vue')
    }, {
        name: "sign-info",
        path: "/sign-info",
        component: () => import ('./pages/SignInfo.vue')
    },
]

export const router = createRouter({history: createWebHashHistory(), routes})
