const fetch = require('../../utils/fetch');
const login = require('../../utils/login');
const rewardMe = require('../../utils/rewardMe');
const app = getApp();
Page({
    /**
     * 页面的初始数据
     */
    data: {
        slides: [],
        categories: []
    },
    onShareAppMessage: function (options) {
        let id = this.data.userInfo.id;
        return fetch.onShare(options, 'pages/index/index', id);
    },

    onPullDownRefresh() {
        this.onLoad({});
        wx.stopPullDownRefresh();
    },
    /**
     * 生命周期函数--监听页面加载
     */
    onLoad(options) {
        fetch.fromShare(options).then(() => {
            return fetch.fetchAvailable('/slides').then(res => {
                this.setData({slides: res.data});
            }).then(res => {
                return fetch.fetchAvailable('/word-groups-mini', {sort: 'rank,asc'}).then(res => {
                    if (res) this.setData({categories: res.data})
                });
            })
        })

    },

    onShow() {
        fetch.loginAndFetch("/account").then(value => {
            this.setData({userInfo: value.data});
            if (value.data.vipExpired) {
                wx.navigateTo({
                    url: '/pages/pay/pay?vipExpired=true'
                });
            }
        });
    },

    loadMore() {
    },
    searchHandle() {
        wx.navigateTo({
            url: '/pages/searchGrid/searchGrid?searchText=' + this.data.searchText
        })
    },

    showSearchHandle() {
        this.setData({searchShowed: true})
    },
    hideSearchHandle() {
        this.setData({searchText: '', searchShowed: false})
    },
    clearSearchHandle() {
        this.setData({searchText: ''})
    },
    searchChangeHandle(e) {
        this.setData({searchText: e.detail.value})
    },
    rewardMe: function (e) {
        rewardMe();
    }

});
