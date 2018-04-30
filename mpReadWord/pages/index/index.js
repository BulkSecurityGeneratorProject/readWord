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
    onShareAppMessage: function (res) {
        if (res.from === 'button') {
            // 来自页面内转发按钮
            console.log(res.target)
        }
        let id = this.data.userInfo.id;
        return {
            title: '新新看图识字',
            path: 'pages/index/index?sharedUserId=' + id,
            success: function (res) {

            },
            fail: function (res) {
                // 转发失败
            }
        }
    },

    onPullDownRefresh() {
        this.onLoad({});
        wx.stopPullDownRefresh();
    },
    /**
     * 生命周期函数--监听页面加载
     */
    onLoad(options) {
        let sharedUserId = options.sharedUserId;
        console.log("####sharedUserId->" + sharedUserId);
        if (sharedUserId) {
            login(sharedUserId).then(res => {
                return fetch.fetchAvailable('/slides').then(res => {
                    this.setData({slides: res.data})
                }).then(res => {
                    return fetch.fetchAvailable('/word-groups-mini', {sort: 'rank,asc'}).then(res => {
                        if (res) this.setData({categories: res.data})
                    });
                }).then(res => {
                    return fetch.loginAndFetch("/account").then(value => {
                        this.setData({userInfo: value.data});
                    });
                });
            });
        } else {
            fetch.fetchAvailable('/slides').then(res => {
                this.setData({slides: res.data})
            }).then(res => {
                return fetch.fetchAvailable('/word-groups-mini', {sort: 'rank,asc'}).then(res => {
                    if (res) this.setData({categories: res.data})
                });
            }).then(res => {
                return fetch.loginAndFetch("/account").then(value => {
                    this.setData({userInfo: value.data});
                });
            });
        }


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
