import {app} from '../main.js'
const baseUrl = "http://localhost/"
// const baseUrl = "http://localhost:8080/"

// eslint-disable-next-line
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
        return await fetch(url, {
            method: 'GET',
            headers: new Headers(
                {'Content-Type': 'application/json'}
            ),
            mode: 'cors',
            credentials: 'include'
        }).then(res => {
            console.log(res)
            return res.json()
        })
    } catch (e) {
        app.config.globalProperties.$dialog.alert({title: '提示', message: '出错了！请稍后再试！', theme: 'round-button'});
        return e.data
    }
}

function base(options, methods) {
    let {path, data} = options;
    let url = baseUrl + path;
    return new Promise((resolve, reject) => { // eslint-disable-next-line
        fetch(url, {
            method: methods, // or 'PUT'
            body: JSON.stringify(data), // data can be `string` or {object}!
            headers: new Headers(
                {'Content-Type': 'application/json'}
            ),
            mode: 'cors',
            credentials: 'include'
        }).then(res => {
            if (res.ok) {
                resolve(res.json())
            } else {
                reject(res.json())
            }
        })
    })
}

export async function post(options) {
    try {
        return await base(options, "POST")
    } catch (e) {
        console.log(e)
        app.config.globalProperties.$dialog.alert({title: '提示', message: '出错了！请稍后再试！', theme: 'round-button'});
        return e.data
    }
}
