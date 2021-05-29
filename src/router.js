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
    },
]

export const router = createRouter({history: createWebHashHistory(), routes})
