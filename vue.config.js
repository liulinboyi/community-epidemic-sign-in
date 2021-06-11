module.exports = (opts) => {
    return {
        devServer: { // contentBase: path.join(__dirname, 'dist'),
            compress: true,
            port: 8081,
            // https: true
        },
        publicPath: "./",
        chainWebpack: config => {
            config.plugin('html').tap(args => { // console.log(args)
                args[0].title = "社区疫情签到"
                // return [ /* 传递给 html-webpack-plugin's 构造函数的新参数 */ ]
                return args
            })
        }
    }
}
