module.exports = (opts) => {
    debugger
    // console.log(opts)
    console.log(process.env)
    console.log(process.env.VUE_APP_ViewportWidth)
    return {
        plugins: {
            'postcss-px-to-viewport': {
                viewportWidth: process.env.VUE_APP_ViewportWidth,
                // selectorBlackList: ['validateInner']
            },
        },
    }
}