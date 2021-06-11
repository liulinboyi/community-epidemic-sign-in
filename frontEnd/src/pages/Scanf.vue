<template>
  <div>
    <div id="mainbody" style="display: inline;">
      <div id="outdiv" autoplay muted></div>
    </div>
    <canvas id="qr-canvas" width="800" height="600"></canvas>
    <canvas id="scancode-mask"></canvas>
    <div class="scancode-tips-group" id="scancode-tips-group">
      <span class="tips">为了精准扫描，请將QR Code 放入框内</span>
      <div class="upload-my-code" onclick="setimg()">我的QR Code</div>
    </div>

    <div id="img-upload-container" style="display: none">
      <div id="qrfile">
        <canvas id="out-canvas" width="320" height="240"></canvas>
        <div id="imghelp">
          drag and drop a QRCode here
          <br />or select a file
          <input type="file" onchange="handleFiles(this.files)" id="upload-img" />
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import QRCode from "../utils/scanf/qrcode.js";
import { post } from "../utils/fetch.js";
import { Dialog } from "vant";
// import "../utils/scanf/methods.js";
// import "../utils/scanf/index.js";
export default {
  data() {
    return {
      gCtx: null, // canvas.ctx
      gCanvas: null,
      // qr-canvas,
      // c:0,
      stype: 0, // 识别流程 0未开始 1进行中 2已结束
      gUM: false,
      webkit: false,
      moz: false,
      v: null, // 存放视频的变量
      scanCodeStart: false, // 开始扫码
      mediaStreamTrack: null, // mediaStreamTrack 实现关闭摄像头功能 mediaStreamTrack.stop()
      imghtml:
        '<div id="qrfile"><canvas id="out-canvas" width="320" height="240"></canvas>' +
        '<div id="imghelp">drag and drop a QRCode here' +
        "<br>or select a file" +
        '<input type="file" onchange="handleFiles(this.files)" id="upload-img"/>' +
        "</div>" +
        "</div>",

      vidhtml: '<video id="v" autoplay muted></video>',
      timeIds: [0, 0, 0, 0],
      state: this.$datas.state,
    };
  },
  beforeMount() {},
  beforeUnmount() {
    this.mediaStreamTrack && this.mediaStreamTrack.stop();
    for (let timeId of this.timeIds) {
      clearTimeout(timeId);
    }
  },
  unmounted() {
    console.log("unmounted");
  },
  mounted() {
    console.log(QRCode);
    /* eslint-disable */
    this.qrcodeScanLoad(320, 400);
    this.setMask();
    if (document.body.clientWidth < 1025) {
      document.getElementById("scancode-tips-group").style.top =
        (document.body.clientHeight - document.body.clientWidth) / 2 +
        document.body.clientWidth * 0.9 -
        10 +
        "px";
    } else {
      document.getElementById("scancode-tips-group").style.top = "720px";
    }
  },
  methods: {
    catchError: function catchError(error) {
      this.gUM = false;
      return;
    },
    success: function success(stream) {
      // mediaStreamTrack 实现关闭摄像头功能
      if (stream)
        this.mediaStreamTrack =
          typeof stream.stop === "function" ? stream : stream.getTracks()[0];

      this.v.srcObject = stream;
      if (this.scanCodeStart) {
        this.v.play();
        this.gUM = true;
        let timeId = setTimeout(this.captureToCanvas, 500);
        this.timeIds[0] = timeId;
      } else {
      }
    },
    // 判断是否支持canvas
    isCanvasSupported: function isCanvasSupported() {
      var elem = document.createElement("canvas");
      return !!(elem.getContext && elem.getContext("2d"));
    },
    htmlEntities: function htmlEntities(str) {
      return String(str)
        .replace(/&/g, "&amp;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;")
        .replace(/"/g, "&quot;");
    },
    async captureToCanvas() {
      if (this.stype != 1) return;

      if (this.gUM && this.scanCodeStart) {
        // try {
        //   this.gCtx.drawImage(this.v, 0, 0);
        try {
          this.gCtx.drawImage(this.v, 0, 0);
          let canvas = document.querySelector("#qr-canvas");
          var context = canvas.getContext("2d");
          var imageData = context.getImageData(
            0,
            0,
            canvas.width,
            canvas.height
          );
          var result = new QRCode.Decoder()
            .setOptions({ canOverwriteImage: false })
            .decode(imageData.data, imageData.width, imageData.height);
          if (result) {
            console.log("res ", result);
            console.log(JSON.stringify(result));
            let userdata;
            try {
              userdata = JSON.parse(result.data);
            } catch (error) {
              Dialog.alert({
                title: "提示",
                message: "二维码无效",
                theme: "round-button",
              }).then(() => {
                // this.$router.push("/");
                this.stype = 1;
              });
              throw new Error("null");
            }

            console.log(userdata);
            let res = await post({
              path: `findUserById?id=${userdata.id}`,
              data: {},
            });
            console.log(res);
            if (res.data.username === userdata.username) {
              this.stype = 0; // 二维码有效才会停止扫描
              Dialog.alert({
                title: "提示",
                message: "二维码有效",
                theme: "round-button",
              }).then(() => {
                this.$router.replace("/");
              });
            } else {
              Dialog.alert({
                title: "提示",
                message: "二维码无效",
                theme: "round-button",
              }).then(() => {
                // this.$router.push("/");
                this.stype = 1;
              });
            }
            // alert(result.data);
          } else {
            throw new Error("null");
          }
          // qrcode.decode(); // 默认为id=qr-canvas的canvas转成图片的base64
        } catch (e) {
          console.log(e);
          let timeId = setTimeout(this.captureToCanvas, 500);
          this.timeIds[1] = timeId;
        }
        // } catch (e) {
        //   console.log(e);
        //   setTimeout(this.captureToCanvas, 500);
        // }
      }
    },
    initCanvas: function initCanvas(w, h) {
      this.gCanvas = document.getElementById("qr-canvas");
      this.gCanvas.style.width = w + "px";
      this.gCanvas.style.height = h + "px";
      this.gCanvas.width = w;
      this.gCanvas.height = h;
      this.gCtx = this.gCanvas.getContext("2d");
      this.gCtx.clearRect(0, 0, w, h);
    },
    handleFiles: function handleFiles(f) {
      var o = [];

      for (var i = 0; i < f.length; i++) {
        var reader = new FileReader();
        reader.onload = (function (theFile) {
          return function (e) {
            this.gCtx.clearRect(0, 0, this.gCanvas.width, this.gCanvas.height);
            qrcode.decode(e.target.result);
          };
        })(f[i]);
        reader.readAsDataURL(f[i]);
      }
    },
    // 上传成功的回调
    scanCodeCallback: function scanCodeCallback(a) {
      var html = htmlEntities(a);
      this.stype = 0;
      console.log("scanCodeCallback " + html);
      // alert(html);
    },
    // 获取操作系统
    getSystem: function getSystem() {
      var u = navigator.userAgent;
      var isAndroid = u.indexOf("Android") > -1 || u.indexOf("Linux") > -1; // g
      var isIOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/); // ios终端
      if (isAndroid) {
        // 这个是安卓操作系统
        return "android";
      } else if (isIOS) {
        // 这个是ios操作系统
        return "ios";
      } else {
        return "other";
      }
    },
    setwebcam2: function setwebcam2(options) {
      if (this.stype == 1) {
        let timeId = setTimeout(this.captureToCanvas, 500);
        this.timeIds[2] = timeId;
        return;
      }

      var n = navigator;
      document.getElementById("outdiv").innerHTML = this.vidhtml;
      this.v = document.getElementById("v");
      try {
        if (n.mediaDevices && n.mediaDevices.getUserMedia) {
          n.mediaDevices
            .getUserMedia({ video: options, audio: false })
            .then((stream) => {
              this.success(stream);
            })
            .catch((error) => {
              this.catchError(error);
              // console.log(error)
            });
        } else if (n.getUserMedia) {
          webkit = true;
          n.getUserMedia(
            {
              video: options,
              audio: false,
            },
            this.success,
            this.catchError
          );
        } else if (n.webkitGetUserMedia) {
          webkit = true;
          n.webkitGetUserMedia(
            {
              video: options,
              audio: false,
            },
            this.success,
            this.catchError
          );
        }
      } catch (err) {
        console.log(err);
      }
      this.stype = 1;
      if (this.getSystem() === "ios") {
        alert("您的设备不支持扫码识别，清上传图片！");
        return;
      }
      if (
        (n.mediaDevices && n.mediaDevices.getUserMedia) ||
        n.getUserMedia ||
        n.webkitGetUserMedia
      ) {
        let timeId = setTimeout(this.captureToCanvas, 500);
        this.timeIds[3] = timeId;
      } else {
        alert("您的设备不支持扫码识别，清上传图片！");
      }
    },
    setwebcam() {
      var options = true;
      if (navigator.mediaDevices && navigator.mediaDevices.enumerateDevices) {
        try {
          navigator.mediaDevices.enumerateDevices().then((devices) => {
            let video = [];
            devices.forEach(function (device) {
              if (device.kind === "videoinput") {
                video.push(device);
              }
            });
            video = video.filter((item) => item.label.indexOf("OBS") === -1);
            if (video.length >= 2) {
              options = {
                deviceId: {
                  exact: video[1].deviceId,
                },
                facingMode: {
                  exact: "environment",
                },
              };
            }
            video.forEach((item) => {
              if (item.label.toLowerCase().search("back") > -1)
                options = {
                  deviceId: {
                    exact: device.deviceId,
                  },
                  facingMode: {
                    exact: "environment",
                  },
                };
            });
            this.scanCodeStart = true;
            this.setwebcam2(options);
          });
        } catch (e) {
          console.error(e);
        }
      } else {
        console.log("no navigator.mediaDevices.enumerateDevices");
        this.setwebcam2(options);
      }
    },
    // 绘制遮罩层canvas
    setMask: function setMask() {
      var canvas = document.querySelector("#scancode-mask");
      canvas.width =
        document.body.clientWidth > 1024 ? 1024 : document.body.clientWidth;
      canvas.height =
        document.body.clientWidth > 1024 ? 1136 : document.body.clientHeight;
      var ctx = canvas.getContext("2d");

      ctx.fillStyle = "rgba(1,1,1,0.2)";
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
    },
    qrcodeScanLoad: function qrcodeScanLoad(width, height) {
      if (this.isCanvasSupported() && window.File && window.FileReader) {
        this.initCanvas(width, height);
        // qrcode.callback = scanCodeCallback;
        document.getElementById("mainbody").style.display = "inline";
        this.setwebcam();
      } else {
        document.getElementById("mainbody").style.display = "inline";
        document.getElementById("mainbody").innerHTML =
          '<p id="mp1">QR code scanner for HTML5 capable browsers</p><br>' +
          '<br><p id="mp2">sorry your browser is not supported</p><br><br>';
      }
    },
    // 选择图片上传
    setimg: function setimg($event) {
      //   qrcode.callback = scanCodeCallback;
      $event && $event.preventDefault();
      this.stype = 2;
      let file = document.getElementById("upload-img");
      file.click();
    },
  },
};
</script>
<style>
@media screen and (min-width: 1024px) {
  .body {
    width: 1200px;
    margin: 0 auto;
  }
}

#outdiv {
  width: 100vw;
  height: 100vh;
  overflow: hidden;
}

#qr-canvas {
  display: none;
}

#scancode-mask {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
}

.scancode-tips-group {
  position: fixed;
  width: 300px;
  left: 50%;
  transform: translateX(-50%);
  z-index: 200;
  color: #fff;
  font-size: 16px;
  text-align: center;
}

.tips {
  color: rgba(255, 255, 255, 0.4);
}

.upload-my-code {
  color: green;
  margin-top: 10px;
}

#v {
  height: 100vh;
}
</style>