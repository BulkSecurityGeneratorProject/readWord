const fetch = require('../../utils/fetch');

Page({
    /**
     * 页面的初始数据
     */
    data: {},

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad(options) {
        let message = JSON.parse(options.message);
        fetch.loginAndFetch("/message-contents", {'id.equals': message.contentId}).then(res => {
            Object.assign(message, res.data[0]);
            this.setData({message});
        })
    },

    previewHandle(e) {
        wx.previewImage({
            current: e.target.dataset.src,
            urls: this.data.gridData.images
        })
    }
});
