const fetch = require('../../utils/fetch');
const app = getApp();

Page({
    /**
     * 页面的初始数据
     */
    data: {
        messages: []
    },

    onPullDownRefresh() {
        this.onLoad({});
        wx.stopPullDownRefresh();
    },
    /**
     * 生命周期函数--监听页面加载
     */
    onLoad(options) {
        fetch.loginAndFetch('/object-messages')
            .then(res => {
                if (res) {
                    const messages = res.data;
                    for (let data of messages) {
                        data["json"] = JSON.stringify(data)
                    }
                    this.setData({messages})
                }

            })
    }
});
