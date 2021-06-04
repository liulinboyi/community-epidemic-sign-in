import {createApp} from 'vue'
import App from './App.vue'
import {router} from './router.js'
import {storeConfig} from './utils/store.js'

const app = createApp(App)
app.use(router)
app.use(storeConfig)
app.mount('#app')
