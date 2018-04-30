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

    onReady(options) {
        console.log(options);
    },
    /**
     * 生命周期函数--监听页面加载
     */
    onLoad(options) {
        let title = options.title;
        this.setData({title});
        wx.setNavigationBarTitle({title: title});
        let that = this;
        let currentIndex = options.index;
        let pageData = wx.getStorageSync(app.config.gridList);
        let gridData = pageData[currentIndex];
        let isFavorite = favorite.isFavorite("words", gridData);
        that.setData({
            pageData,
            currentIndex,
            isFavorite
        });
        fetchStorage.getWordById(gridData.id).then(res => {
            let gridData = res;
            const innerAudioContext = wx.createAudioContext('myAudio');
            innerAudioContext.setSrc(gridData.audioUrl);
            let autoplay = fetchStorage.obj(app.config.profile, "autoPlay");
            if (autoplay) {
                innerAudioContext.play();
            }
            that.setData({innerAudioContext, gridData});

        });
        /*
*/

    },
    onUnload: function () {
        /*if (this.data.innerAudioContext) {
            this.data.innerAudioContext.destroy();
        }
*/
    },

    audioPlay: function (e) {
        console.log("audioPlay....");
        console.log("this.data.innerAudioContext:" + this.data.innerAudioContext);
        if (this.data.innerAudioContext) {
            let gridData = this.data['gridData'];
            if (e.target.id === "1") {
                this.data.innerAudioContext.setSrc(gridData.audio1Url);
            } else if (e.target.id === "5") {
                this.data.innerAudioContext.setSrc(gridData.audioUrl);
            }
            this.data.innerAudioContext.play();
        }
    },


    bindchange: function (e) {
        if (this.data.innerAudioContext) {
            let gridData = this.data.pageData[e.detail.current];
            fetchStorage.getWordById(gridData.id).then(res => {
                let gridData = res;
                let isFavorite = favorite.isFavorite(this.data.title, gridData);
                this.setData({gridData, isFavorite});
                this.data.innerAudioContext.setSrc(gridData.audioUrl);
                if (fetchStorage.obj(app.config.profile, "autoPlay")) {
                    this.data.innerAudioContext.play();
                }
            });


        }
    },
    addOrCancelFavorite: function () {
        favorite.addOrCancel("words", this.data.gridData.id).then(res => {
            this.setData({isFavorite: res});
        });
    }

});
