import {createApp} from 'vue'
import App from './App.vue'
import {router} from './router.js'
import {storeConfig} from './utils/store.js'
import {DateConfing} from './utils/Date.js'
import {Dialog} from 'vant';

export const app = createApp(App)
app.use(router)
app.use(storeConfig)
app.use(DateConfing)
app.use(Dialog)
app.mount('#app')
