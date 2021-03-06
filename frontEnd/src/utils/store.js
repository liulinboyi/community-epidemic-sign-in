import {reactive} from 'vue'
export const store = {
    debug: true,

    state: reactive(
        {userInfo: null, top: 0}
    ),

    setTop(newValue) {
        if (this.debug) {
            console.log('setuserInfoAction triggered with', newValue)
        }

        this.state.top = newValue
    },

    setUserInfoAction(newValue) {
        if (this.debug) {
            console.log('setuserInfoAction triggered with', newValue)
        }

        this.state.userInfo = newValue
    },

    clearUserInfoAction() {
        if (this.debug) {
            console.log('clearuserInfoAction triggered')
        }

        this.state.userInfo = ''
    }
}
export const storeConfig = {
    install: (app, /*options*/
    ) => { // Plugin code goes here
        app.config.globalProperties.$datas = store
    }
}
