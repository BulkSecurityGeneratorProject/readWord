const favorite = require('../../utils/favorite');
const app = getApp();
const fetchStorage = require('../../utils/fetchStorage');
const fetch = require('../../utils/fetch');


Page({
    /**
     * 页面的初始数据
     */
    data: {
        title: "",
        pageData: [],
        gridData: {},
        currentIndex: 0,
        isFavorite: false,
        innerAudioContext: null
    },
    /**
     * 生命周期函数--监听页面加载
     */
    onLoad(options) {
        let title = options.title;
        this.setData({title});
        wx.setNavigationBarTitle({title: title});
        let that = this;
        let pageData = JSON.parse(options.pageData);
        const params = {page: 0, size: 100, 'id.in': pageData.toString(), sort: 'rank,asc'};
        fetch.fetchAvailable(app.config.apiObjectWord, params).then(res => {
            let currentIndex = options.index;
            pageData = res.data;
            let gridData = pageData[currentIndex];
            let isFavorite = favorite.isFavorite("words", gridData);
            that.setData({
                gridData,
                pageData,
                currentIndex,
                isFavorite
            });
            const innerAudioContext = wx.createInnerAudioContext();
            innerAudioContext.autoplay = fetchStorage.obj(app.config.profile, "autoPlay");
            innerAudioContext.src = gridData.audioUrl;
            // console.log(res.data.url);
            innerAudioContext.onPlay(() => {
                // console.log('开始播放')
            });
            innerAudioContext.onError((res) => {
                // console.log(res.errMsg);
                // console.log(res.errCode)
            });
            that.setData({innerAudioContext: innerAudioContext});

        });


    },
    onUnload: function () {
        if (this.data.innerAudioContext) {
            this.data.innerAudioContext.destroy();
        }

    },

    audioPlay: function () {
        if (this.data.innerAudioContext) {
            this.data.innerAudioContext.play();
        }
    },

    previewHandle(e) {
        wx.previewImage({
            current: e.target.dataset.src,
            urls: this.data.gridData.sinaUrl
        })
    },
    bindchange: function (e) {
        if (this.data.innerAudioContext) {
            let gridData = this.data.pageData[e.detail.current];
            let isFavorite = favorite.isFavorite(this.data.title, gridData);
            this.setData({gridData, isFavorite});
            this.data.innerAudioContext.src = gridData.audioUrl;
            if (fetchStorage.obj(app.config.profile, "autoPlay")) {
                this.data.innerAudioContext.play();
            }

        }
    },
    addOrCancelFavorite: function () {
        favorite.addOrCancel("words", this.data.gridData.id).then(res => {
            this.setData({isFavorite: res});
        });
    }

});
