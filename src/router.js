import {createRouter, createWebHashHistory} from 'vue-router'

const routes = [
    {
        path: '/',
        component: () => import ('./pages/Entry.vue')
    }, {
        path: '/sign',
        component: () => import ('./pages/Sign.vue')
    },
]

export const router = createRouter({history: createWebHashHistory(), routes})
