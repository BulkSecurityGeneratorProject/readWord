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
        pageIndex: 0,
        pageSize: 20,
        totalCount: 0,
        maxSize: 40,
        hasMore: true
    },

    loadMore() {
        let {pageIndex, pageSize, groupId, searchText} = this.data;
        const params = {page: pageIndex++, size: pageSize, 'wordGroupId.equals': groupId, sort: 'rank,asc'};
        if (searchText) params['name.contains'] = searchText;

        return fetch.fetchAvailable(this.data.apiUrl, params)
            .then(res => {
                const totalCount = parseInt(res.header['X-Total-Count']);
                const hasMore = this.data.pageIndex * this.data.pageSize < totalCount;
                let pageData = this.data.pageData;
                if (hasMore) {
                    pageData = this.data.pageData.concat(res.data);
                }
                wx.setStorageSync(app.config.gridList, pageData);
                this.setData({pageData, totalCount, pageIndex, hasMore})
            })
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad(options) {
        let title = options.title;
        let groupId = options.groupId;
        wx.setNavigationBarTitle({title: title});
        this.setData({title, groupId});
        this.loadMore()
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
    }
});
