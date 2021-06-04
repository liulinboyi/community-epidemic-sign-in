const baseUrl = "http://localhost/"
export function get(options) {
    let {path, data} = options;
    let url = baseUrl + path;
    try {
        return await fetch(url, {
            method: 'GET',
            headers: new Headers(
                {'Content-Type': 'application/json'}
            )
        }).then(res => res.json())
    } catch (e) {
        return e.data
    }
}

export async function post(options) {
    let {path, data} = options;
    let url = baseUrl + path;
    try {
        return await fetch(url, {
            method: 'POST', // or 'PUT'
            body: JSON.stringify(data), // data can be `string` or {object}!
            headers: new Headers(
                {'Content-Type': 'application/json'}
            )
        }).then(res => res.json())
    } catch (e) {
        return e.data
    }
}
