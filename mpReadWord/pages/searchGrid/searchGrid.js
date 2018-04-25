const fetch = require('../../utils/fetch');
const fetchStorage = require('../../utils/fetchStorage');
const app = getApp();
Page({
    /**
     * 页面的初始数据
     */
    data: {
        apiUrl: app.config.apiObjectWord,
        title: "",
        pageData: [],
        pageDataJson: "[]",
        pageIndex: 0,
        pageSize: 20,
        totalCount: 0,
        maxSize: 40,
        hasMore: true
    },

    loadMore() {
        let {pageIndex, pageSize, searchText} = this.data;
        const params = {page: pageIndex++, size: pageSize, sort: 'rank,asc'};
        if (searchText) params['name.contains'] = searchText;

        return fetch.fetchAvailable(this.data.apiUrl, params)
            .then(res => {
                const totalCount = parseInt(res.header['X-Total-Count']);
                const hasMore = this.data.pageIndex * this.data.pageSize < totalCount;
                let pageData = this.data.pageData;
                if (hasMore) {
                    pageData = this.data.pageData.concat(res.data);
                }
                // if (pageData.length > this.data.maxSize) {
                //     pageData = pageData.splice(pageData.length - this.data.maxSize)
                // }
                let ids = [];
                for (let data of pageData) {
                    ids.push(data.id);
                    data["json"] = JSON.stringify(data);
                }
                let pageDataJson = JSON.stringify(ids);
                this.setData({pageData, totalCount, pageIndex, hasMore, pageDataJson})
            })
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad(options) {
        let searchText = options.searchText;
        let searchShowed = true;
        this.setData({searchText, searchShowed});
        this.loadMore();
    },

    /**
     * 页面相关事件处理函数--监听用户下拉动作
     */
    onPullDownRefresh() {
        this.setData({pageData: [], pageIndex: 0, hasMore: true});
        this.loadMore().then(() => wx.stopPullDownRefresh())
    },

    /**
     * 页面上拉触底事件的处理函数
     */
    onReachBottom() {
        this.loadMore()
    },

    searchHandle() {
        // console.log(this.data.searchText)
        this.setData({pageData: [], pageIndex: 0, hasMore: true});
        this.loadMore()
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
    favorites: function () {
        let title = this.data.title;
        let index = 0;
        fetchStorage.array(app.config.favorite, title).then(res => {
            if (res.length > 0) {
                let dataJson = JSON.stringify(res[0]);
                let pageDataJson = JSON.stringify(res);
                let url = `/pages/gridDetail/gridDetail?title=${title}&data=${dataJson}&index=${index}&pageData=${pageDataJson}`;
                wx.redirectTo({
                    url
                })
            } else {
                wx.showToast({
                    title: '您还没有收藏',
                    icon: 'success',
                    duration: 2000
                })
            }
        });
    }
});
