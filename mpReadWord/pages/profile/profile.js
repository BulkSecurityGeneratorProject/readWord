const fetch = require('../../utils/fetch');
const rewardMe = require('../../utils/rewardMe');
const app = getApp();

const login = require('../../utils/login');
Page({
    data: {
        version: '1.0',
        userInfo: {}
    },
    onLoad() {
        let version = app.config.version;
        this.setData({version});
        fetch.loginAndFetch("/account").then(value => {
            this.setData({userInfo: value.data});
            if (!value.data.hasUpdateInfo) {
                console.log("未同步数据!");
                this.updateUserInfo();
            }
        })
    },
    onShow() {

    },
    updateUserInfo: function () {
        let that = this;
        wx.getUserInfo({
            success: function (userInfo) {
                let jhipsterHeader = wx.getStorageSync(app.config.jhpsterHeader);
                wx.request({
                    url: app.config.apiUserInfo,
                    data: {
                        sessionKey: jhipsterHeader.header.WxSessionKey,
                        signature: userInfo.signature,
                        rawData: userInfo.rawData,
                        encryptedData: userInfo.encryptedData,
                        iv: userInfo.iv
                    },
                    success: function (res) {
                        console.log("用户更新:" + jhipsterHeader);
                        that.setData({userInfo: res.data});

                    }, fail: function (data) {
                        console.log(data);
                    }
                })

            },
            fail: function (error) {
                console.log(error);

            }
        })
    },
    profile: function () {
        wx.navigateTo({
            url: '../settings/settings'
        })
    },
    aboutUs: function () {
        wx.showModal({
            title: '关于我们',
            content: '我们是新新的家人',
            showCancel: false
        })
    },


    relogin: function (e) {
        wx.removeStorageSync(app.config.jhpsterHeader);
        login(e.detail.formId).then(res => {
            if (res.Authorization) {
                wx.showModal({
                    title: '登录成功',
                    content: '登录成功',
                    showCancel: false
                })
            }
        });
    },
    rewardMe: function (e) {
        rewardMe();
    }
});