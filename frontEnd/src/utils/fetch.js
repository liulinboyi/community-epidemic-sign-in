import {app} from '../main.js'
// const baseUrl = "http://localhost/"
const baseUrl = "http://localhost:8080/"
// const baseUrl = "http://127.0.0.1:8080/"
// const baseUrl = "http://192.168.31.102:8080/"


function isPromise(value) {
    return value && Object.prototype.toString.call(value) === "[object Promise]";
}
/**
 * 当接收到一个代表错误的 HTTP 状态码时，从 fetch() 返回的 Promise 不会被标记为 reject， 
 * 即使响应的 HTTP 状态码是 404 或 500。相反，它会将 Promise 状态标记为 resolve （但是
 * 会将 resolve 的返回值的 ok 属性设置为 false ），仅当网络故障时或请求被阻止时，才会标
 * 记为 reject。
 */
/**
 * 
 * @param {*} options 
 * @returns 
 */
export async function get(options) {
    let {path} = options;
    let url = baseUrl + path;
    try {
        let res = await fetch(url, {
            method: 'GET',
            headers: new Headers(
                {'Content-Type': 'application/json'}
            ),
            mode: 'cors',
            credentials: 'include'
        })
        if (! res.ok) {
            return await Promise.reject(res.json())
        }
        console.log(res)
        return res.json(); // res.json()是Promise
    } catch (e) {
        console.log(e)
        if (isPromise(e)) {
            let res = await e;
            console.log(res)
            app.config.globalProperties.$dialog.alert({title: '提示', message: res.msg, theme: 'round-button'});
            return Promise.reject(res)
        }
        console.log(e)
        app.config.globalProperties.$dialog.alert({title: '提示', message: '出错了！请稍后再试！', theme: 'round-button'});
        return e.data
    }
}

export async function post(options) {
    let {path, data} = options;
    let url = baseUrl + path;
    try {
        let res = await fetch(url, {
            method: 'POST', // or 'PUT'
            body: JSON.stringify(data), // data can be `string` or {object}!
            headers: new Headers(
                {'Content-Type': 'application/json'}
            ),
            mode: 'cors',
            credentials: 'include'
        })
        if (! res.ok) {
            // try catch 会捕获await Promise.reject
            // await Promise.reject(res.json())会捕获
            // Promise.reject(await res.json())这里不会捕获
            return await Promise.reject(await res.json())
        }
        console.log(res)
        return res.json(); // res.json()是Promise
    } catch (e) {
        // console.log(e)
        // if (isPromise(e)) {
        //     let res = await e;
        //     console.log(res)
        //     app.config.globalProperties.$dialog.alert({title: '提示', message: res.msg, theme: 'round-button'});
        //     return Promise.reject(res)
        // }
        console.log(e)
        app.config.globalProperties.$dialog.alert({
            title: '提示',
            message: e.msg ? e.msg : '出错了！请稍后再试！',
            theme: 'round-button'
        }).then(() => {
            console.log("ok")
        });
        return Promise.reject(e.data ? e.data : e)
    }
}
