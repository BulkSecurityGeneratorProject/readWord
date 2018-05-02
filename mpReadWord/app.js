const apiBaseStr = 'https://pandabear.cn';
App({
    config: {
        apiBase: apiBaseStr,
        apiBaseApi: apiBaseStr + '/api',
        apiLogin: apiBaseStr + '/wechat/user/login',
        apiUserInfo: apiBaseStr + '/wechat/user/info',
        apiObjectWord: '/words-mini',
        apiObjectWordDetail: '/words/',
        jhpsterHeader: "jhpsterHeader",
        profile: "profile",
        favorite: "favorite",
        words: "words",
        gridList: "gridList",
        version: "1.2.4"
    },
    message: {
        error: "错误,请重刷新重试,或者联系工作人员",
        unAuth: "错误,您未同意授权",
        timeOut: "连接超时"
    }
    , onLaunch: function () {
        console.log("onLaunch->removeStorageSync->jhpsterHeader");
        wx.removeStorageSync(this.config.jhpsterHeader);

    }
});
