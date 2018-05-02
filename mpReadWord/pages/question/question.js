// pages/settings/settings.js
const app = getApp();
const fetch = require('../../utils/fetch');

Page({

    /**
     * 页面的初始数据
     */
    data: {},

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function (options) {
        let that = this;
        fetch.loginAndFetch("/questions/me").then(res => {
            let {id, userId, contact, desctription} = res.data;
            that.setData({id, userId, contact, desctription})
        })
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
    formSubmit: function (e) {
        let formValues = e.detail.value;
        console.log('form发生了submit事件，携带数据为：', formValues);
        let method = 'POST';
        if (formValues.id) {
            method = "PUT";
        }
        fetch.loginAndFetch("/questions", formValues, method).then(res => {
            if (res.statusCode === 200) {
                wx.navigateBack();
                wx.showToast({
                    title: '提交成功,谢谢!',
                    icon: 'success',
                    duration: 2000
                });

            }
        })
    }
});
