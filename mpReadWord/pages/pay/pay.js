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
        fetch.loginAndFetch("/products").then(res => {
            this.setData({items: res.data});
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
        if(!formValues.productId){
            wx.showToast({
                title: '请选择会员!',
                icon: 'success',
                duration: 2000
            });
            return
        }
        let method = 'GET';
        wx.login({
            success(res) {
                wx.request({
                    url: app.config.apiPay + "/" + formValues.productId,
                    method: method,
                    data: {
                        code: res.code,
                    },
                    success: function (res) {
                        console.log(res);
                        if (res && res.data && res.data.success) {
                            wx.requestPayment(
                                {
                                    timeStamp: res.data.timeStamp,
                                    nonceStr: res.data.nonceStr,
                                    package: res.data.package,
                                    signType: res.data.signType,
                                    paySign: res.data.paySign,
                                    success: function (res) {
                                        console.log("success->", res)
                                    },
                                    fail: function (res) {
                                        console.log("fail->", res)
                                    },
                                    complete: function (res) {
                                        console.log("complete->", res)
                                    }
                                });
                        }
                    }, fail: function (data) {
                        console.log(data);
                    }
                })
            }
        });


    }
});
