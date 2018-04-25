// pages/settings/settings.js
const app = getApp();
const fetchStorage = require('../../utils/fetchStorage');

Page({

    /**
     * 页面的初始数据
     */
    data: {
        autoPlay: false,
        notObeyMuteSwitch: true
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function (options) {
        let profile = wx.getStorageSync(app.config.profile);
        let autoPlay = fetchStorage.obj(app.config.profile, "autoPlay");
        let notObeyMuteSwitch = fetchStorage.obj(app.config.profile, "notObeyMuteSwitch");
        this.setData({autoPlay, notObeyMuteSwitch});

    },

    /**
     * 生命周期函数--监听页面初次渲染完成
     */
    onReady: function () {

    },

    /**
     * 生命周期函数--监听页面显示
     */
    onShow: function () {

    },

    /**
     * 生命周期函数--监听页面隐藏
     */
    onHide: function () {

    },

    /**
     * 生命周期函数--监听页面卸载
     */
    onUnload: function () {

    },

    /**
     * 页面相关事件处理函数--监听用户下拉动作
     */
    onPullDownRefresh: function () {

    },

    /**
     * 页面上拉触底事件的处理函数
     */
    onReachBottom: function () {

    },

    /**
     * 用户点击右上角分享
     */
    onShareAppMessage: function () {

    },
    getProfile() {
        let profile = wx.getStorageSync(app.config.profile);
        if (!profile) {
            profile = {};
        }
        return profile;
    },

    autoPlayChange: function (e) {
        let profile = this.getProfile();
        profile['autoPlay'] = e.detail.value;
        wx.setStorageSync(app.config.profile, profile);
    },
    notObeyMuteSwitchChange: function (e) {
        let profile = this.getProfile();
        profile['notObeyMuteSwitch'] = !(e.detail.value);
        wx.setStorageSync(app.config.profile, profile);
    }
});
