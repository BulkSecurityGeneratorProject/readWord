const apiBaseStr = 'http://ectest.nipponpaint.com.cn';
// const apiBaseStr = 'http://ectest.nipponpaint.com.cn/wx-mp';

// const login = require('./utils/login');

App({
    config: {
        apiBase: apiBaseStr,
        apiBaseApi: apiBaseStr + '/api',
        apiLogin: apiBaseStr + '/wechat/user/login',
        apiUserInfo: apiBaseStr + '/wechat/user/info',
        apiPay: apiBaseStr + '/wechat/pay/unifiedOrder',
        apiObjectWord: '/words-mini',
        apiObjectWordDetail: '/words/',
        jhpsterHeader: "jhpsterHeader",
        profile: "profile",
        favorite: "favorite",
        words: "words",
        gridList: "gridList",
        version: "1.2.4",
        CN: 'zh_CN',
        EN: 'en_US'
    },
    message: {
        error: "错误,请重刷新重试,或者联系工作人员",
        unAuth: "错误,您未同意授权",
        timeOut: "连接超时"
    }
    , onLaunch: function () {
        console.log("onLaunch->removeStorageSync->jhpsterHeader");
        wx.removeStorageSync(this.config.jhpsterHeader);
        wx.getStorage({
            key: 'history',
            success: (res) => {
                this.globalData.history = res.data
            },
            fail: (res) => {
                console.log("get storage failed");
                console.log(res);
                this.globalData.history = []
            }
        })

    },
    // 权限询问
    getRecordAuth: function () {
        wx.getSetting({
            success(res) {
                console.log("succ");
                console.log(res);
                if (!res.authSetting['scope.record']) {
                    wx.authorize({
                        scope: 'scope.record',
                        success() {
                            // 用户已经同意小程序使用录音功能，后续调用 wx.startRecord 接口不会弹窗询问
                            console.log("succ auth")
                        }, fail() {
                            console.log("fail auth")
                        }
                    })
                } else {
                    console.log("record has been authed")
                }
            }, fail(res) {
                console.log("fail");
                console.log(res)
            }
        })
    },

    onHide: function () {
        wx.stopBackgroundAudio()
    },
    globalData: {

        history: [],
    }
});
