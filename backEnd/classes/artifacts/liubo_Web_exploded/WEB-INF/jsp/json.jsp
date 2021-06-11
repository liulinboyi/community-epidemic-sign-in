<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>客户信息</title>
</head>
<body>
<div>hello Json</div>
<form action="" method="post" id="form">
    <span>用户Id</span><input id="ids" type="number" value=""/>
    <br>
    <input id="submit" type="submit" value="提交"/>
</form>
<script src="${pageContext.request.contextPath}/js/jquery-1.11.3.min.js"></script>

<script>
    submit.addEventListener('click', (e) => {
        console.log(e)
        e.preventDefault()
        getData()
    })

    function getData() {
        let url = 'http://localhost:8080/testJson'
        let data = {
            id: ids.value,
            username: "小明"
        }

        fetch(url, {
            method: "POST",
            body: JSON.stringify(data),
            headers: new Headers({
                'content-type': 'application/json'
            }),

        })
            .then((res) => res.json())
            .catch((error) => console.log(error))
            .then((res) => console.log(res))
    }

</script>
</body>
</html>
