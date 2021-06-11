/* eslint-disable */
var gCtx = null; // canvas.ctx
var gCanvas = null;
// qr-canvas
// var c = 0;
var stype = 0; // 识别流程 0未开始 1进行中 2已结束
var gUM = false;
var webkit = false;
var moz = false;
var v = null; // 存放视频的变量
var scanCodeStart = false; // 开始扫码
var mediaStreamTrack = null; // mediaStreamTrack 实现关闭摄像头功能 mediaStreamTrack.stop()
var imghtml = '<div id="qrfile"><canvas id="out-canvas" width="320" height="240"></canvas>' + '<div id="imghelp">drag and drop a QRCode here' + "<br>or select a file" + '<input type="file" onchange="handleFiles(this.files)" id="upload-img"/>' + "</div>" + "</div>";

var vidhtml = '<video id="v" autoplay muted></video>';


function qrcodeScanLoad(width, height) {
    if (isCanvasSupported() && window.File && window.FileReader) {
        initCanvas(width, height);
        qrcode.callback = scanCodeCallback;
        document.getElementById("mainbody").style.display = "inline";
        setwebcam();
    } else {
        document.getElementById("mainbody").style.display = "inline";
        document.getElementById("mainbody").innerHTML = '<p id="mp1">QR code scanner for HTML5 capable browsers</p><br>' + '<br><p id="mp2">sorry your browser is not supported</p><br><br>';
    }
}

// 绘制遮罩层canvas
function setMask() {
    var canvas = document.querySelector("#scancode-mask");
    canvas.width = document.body.clientWidth > 1024 ? 1024 : document.body.clientWidth;
    canvas.height = document.body.clientWidth > 1024 ? 1136 : document.body.clientHeight;
    var ctx = canvas.getContext("2d");

    ctx.fillStyle = "rgba(1,1,1,0.2)"
    ctx.fillRect(0, 0, canvas.width, canvas.height);

    ctx.globalCompositeOperation = "destination-out";
    ctx.beginPath();
    let x1,
        y1,
        width = canvas.width * 0.6;
    x1 = (canvas.width - width) / 2;
    y1 = (canvas.height - width) / 2;
    ctx.fillRect(x1, y1, width, width);
    ctx.fill();
    ctx.save();

    ctx.globalCompositeOperation = "source-over";

    // 第二象限点
    ctx.moveTo(x1, y1 + 2);
    ctx.lineTo(x1 + 20, y1 + 2);
    ctx.moveTo(x1 + 2, y1);
    ctx.lineTo(x1 + 2, y1 + 20);

    // 第一象限点
    ctx.moveTo(x1 + width - 20, y1 + 2);
    ctx.lineTo(x1 + width, y1 + 2);
    ctx.moveTo(x1 + width - 2, y1 + 1);
    ctx.lineTo(x1 + width - 2, y1 + 20);

    // 第四象限点
    ctx.moveTo(x1 + width - 20, y1 + width - 2);
    ctx.lineTo(x1 + width, y1 + width - 2);
    ctx.moveTo(x1 + width - 2, y1 + width - 1);
    ctx.lineTo(x1 + width - 2, y1 + width - 20);

    // 第三象限点
    ctx.moveTo(x1 + 20, y1 + width - 2);
    ctx.lineTo(x1, y1 + width - 2);
    ctx.moveTo(x1 + 2, y1 + width - 2);
    ctx.lineTo(x1 + 2, y1 + width - 20);
    ctx.lineWidth = 4;
    ctx.strokeStyle = "green";
    ctx.stroke();
}
function setwebcam() {
    var options = true;
    if (navigator.mediaDevices && navigator.mediaDevices.enumerateDevices) {
        try {
            navigator.mediaDevices.enumerateDevices().then(function (devices) {
                let video = [];
                devices.forEach(function (device) {
                    if (device.kind === "videoinput") {
                        video.push(device);
                    }
                });
                video = video.filter(item => item.label.indexOf("OBS") === -1)
                if (video.length >= 2) {
                    options = {
                        deviceId: {
                            exact: video[1].deviceId
                        },
                        facingMode: {
                            exact: "environment"
                        }
                    };
                }
                video.forEach(item => {
                    if (item.label.toLowerCase().search("back") > -1) 
                        options = {
                            deviceId: {
                                exact: device.deviceId
                            },
                            facingMode: {
                                exact: "environment"
                            }
                        };
                    
                });
                scanCodeStart = true;
                setwebcam2(options);
            });
        } catch (e) {
            console.error(e);
        }
    } else {
        console.log("no navigator.mediaDevices.enumerateDevices");
        setwebcam2(options);
    }
}

function setwebcam2(options) {
    if (stype == 1) {
        setTimeout(captureToCanvas, 500);
        return;
    }

    var n = navigator;
    document.getElementById("outdiv").innerHTML = vidhtml;
    v = document.getElementById("v");
    try {
        if (n.mediaDevices && n.mediaDevices.getUserMedia) {
            n.mediaDevices.getUserMedia({video: options, audio: false}).then(function (stream) {
                success(stream);
            }).catch(function (error) {
                catchError(error);
                // console.log(error)
            });
        } else if (n.getUserMedia) {
            webkit = true;
            n.getUserMedia({
                video: options,
                audio: false
            }, success, catchError);
        } else if (n.webkitGetUserMedia) {
            webkit = true;
            n.webkitGetUserMedia({
                video: options,
                audio: false
            }, success, catchError);
        }
    } catch (err) {
        console.log(err);
    }
    stype = 1;
    if (getSystem() === "ios") {
        alert("您的设备不支持扫码识别，清上传图片！");
        return;
    }
    if ((n.mediaDevices && n.mediaDevices.getUserMedia) || n.getUserMedia || n.webkitGetUserMedia) 
        setTimeout(captureToCanvas, 500);
     else {
        alert("您的设备不支持扫码识别，清上传图片！");
    }
}

// 获取操作系统
function getSystem() {
    var u = navigator.userAgent;
    var isAndroid = u.indexOf("Android") > -1 || u.indexOf("Linux") > -1; // g
    var isIOS = !! u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/); // ios终端
    if (isAndroid) { // 这个是安卓操作系统
        return "android";
    } else if (isIOS) { // 这个是ios操作系统
        return "ios";
    } else {
        return "other";
    }
}

// 选择图片上传
function setimg($event) {
    qrcode.callback = scanCodeCallback;
    $event && $event.preventDefault();
    stype = 2;
    let file = document.getElementById("upload-img");
    file.click();
}

// 上传成功的回调
function scanCodeCallback(a) {
    var html = htmlEntities(a);
    stype = 0;
    console.log("scanCodeCallback " + html)
    // alert(html);
}

function handleFiles(f) {
    var o = [];

    for (var i = 0; i < f.length; i++) {
        var reader = new FileReader();
        reader.onload = (function (theFile) {
            return function (e) {
                gCtx.clearRect(0, 0, gCanvas.width, gCanvas.height);
                qrcode.decode(e.target.result);
            };
        })(f[i]);
        reader.readAsDataURL(f[i]);
    }
}

function initCanvas(w, h) {
    gCanvas = document.getElementById("qr-canvas");
    gCanvas.style.width = w + "px";
    gCanvas.style.height = h + "px";
    gCanvas.width = w;
    gCanvas.height = h;
    gCtx = gCanvas.getContext("2d");
    gCtx.clearRect(0, 0, w, h);
}

function captureToCanvas() {
    if (stype != 1) 
        return;
    
    if (gUM && scanCodeStart) {
        try {
            gCtx.drawImage(v, 0, 0);
            try {
                let canvas = document.querySelector("#qr-canvas")
                var context = canvas.getContext('2d');
                var imageData = context.getImageData(0, 0, canvas.width, canvas.height);
                var result = new QRCode.Decoder().setOptions({canOverwriteImage: false}).decode(imageData.data, imageData.width, imageData.height);
                if (result) {
                    stype = 0;
                    console.log("res " + result)
                    console.log(JSON.stringify(result))
                    alert(result.data)
                } else {
                    throw new Error("null")
                }
                // qrcode.decode(); // 默认为id=qr-canvas的canvas转成图片的base64
            } catch (e) {
                console.log(e);
                setTimeout(captureToCanvas, 500);
            }
        } catch (e) {
            console.log(e);
            setTimeout(captureToCanvas, 500);
        }
    }
}

function htmlEntities(str) {
    return String(str).replace(/&/g, "&amp;").replace(/</g, "&lt;").replace(/>/g, "&gt;").replace(/"/g, "&quot;");
}

// 判断是否支持canvas
function isCanvasSupported() {
    var elem = document.createElement("canvas");
    return !!(elem.getContext && elem.getContext("2d"));
}
function success(stream) { // mediaStreamTrack 实现关闭摄像头功能
    if (stream) 
        mediaStreamTrack = typeof stream.stop === "function" ? stream : stream.getTracks()[0];
    

    v.srcObject = stream;
    if (scanCodeStart) {
        v.play();
        gUM = true;
        setTimeout(captureToCanvas, 500);
    } else {}
}

function catchError(error) {
    gUM = false;
    return;
}


/* eslint-disable */

qrcodeScanLoad(320, 400);
setMask();
if (document.body.clientWidth < 1025) {
    document.getElementById("scancode-tips-group").style.top = (document.body.clientHeight - document.body.clientWidth) / 2 + document.body.clientWidth * 0.9 - 10 + "px";
} else {
    document.getElementById("scancode-tips-group").style.top = "720px";
}
