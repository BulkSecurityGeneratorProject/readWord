Page({
    /**
     * 页面的初始数据
     */
    data: {
        list: []
    },

    loadMore() {

    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad(options) {
        if (options.title) {
            wx.setNavigationBarTitle({title: options.title});
        }

        try {
            let value = wx.getStorageSync('wordGroupKey');
            if (!value || value.length === 0) {
                value = [{content: '我是生字学习的模板'}];
                wx.setStorageSync('wordGroupKey', value);
            }
            this.setData({list: value});
        } catch (e) {
            console.log(e);
        }

    },

    addWordGroup: function () {
        wx.navigateTo({
            url: '../operation/operation',
        })
    },

    deleteWordGroup: function (e) {
        let that = this;
        that.data.list.splice(e.target.dataset.index, 1);
        that.setData({
            list: that.data.list
        });
        wx.setStorageSync('wordGroupKey', that.data.list);

    },

    /**
     * 页面相关事件处理函数--监听用户下拉动作
     */
    onPullDownRefresh() {

    },

    /**
     * 页面上拉触底事件的处理函数
     */
    onReachBottom() {
        // TODO：节流
        this.loadMore()
    },

    searchHandle() {
        // console.log(this.data.searchText)

    },

    showSearchHandle() {
    },
    hideSearchHandle() {
    },
    clearSearchHandle() {
    },
    searchChangeHandle(e) {
    }
});
