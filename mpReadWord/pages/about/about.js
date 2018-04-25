// pages/settings/settings.js
const app = getApp();
const fetchStorage = require('../../utils/fetchStorage');

Page({

    /**
     * 页面的初始数据
     */
    data: {
        autoPlay: true
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function (options) {
        let profile = wx.getStorageSync(app.config.profile);
        let autoPlay = fetchStorage.obj(app.config.profile, "autoPlay");
        this.setData({autoPlay});

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

    autoPlayChange: function (e) {
        let profile = wx.getStorageSync(app.config.profile);
        if (!profile) {
            profile = {};
        }
        profile['autoPlay'] = e.detail.value;
        wx.setStorageSync(app.config.profile, profile);
    }
});