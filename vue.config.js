module.exports = (opts) => {
    return {
        chainWebpack: config => {
            config
                .plugin('html')
                .tap(args => {
                    // console.log(args)
                    args[0].title = "社区疫情签到"
                    // return [ /* 传递给 html-webpack-plugin's 构造函数的新参数 */ ]
                    return args
                })
        }
    }
}