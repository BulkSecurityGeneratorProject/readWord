const favorite = require('../../utils/favorite');
const app = getApp();
const fetchStorage = require('../../utils/fetchStorage');
const fetch = require('../../utils/fetch');
const plugin = requirePlugin("WechatSI");
// 获取**全局唯一**的语音识别管理器**recordRecoManager**
const manager = plugin.getRecordRecognitionManager();


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
        let currentIndex = options.index * 1.0;
        let storageSync = wx.getStorageSync(app.config.gridList);
        let pageData = storageSync;
        let gridData = storageSync[currentIndex];
        if (storageSync.length > 20) {
            if (storageSync.length - currentIndex > 10) {
                if (currentIndex >= 10) {
                    pageData = storageSync.slice(currentIndex - 10, currentIndex + 10);
                    console.log("pageData.length=" + pageData.length);
                    currentIndex = 10;
                } else {
                    pageData = storageSync.slice(0, 20);
                }

            } else {
                pageData = storageSync.slice(storageSync.length - 20, storageSync.length);
                currentIndex = currentIndex - storageSync.length + 20;
                console.log("pageData.length=" + pageData.length);
            }
        }
        that.setData({
            pageData,
            currentIndex
        });
        let isFavorite = favorite.isFavorite("words", gridData);
        this.setData({isFavorite});
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

    fanyi: function (e) {
        console.log("fanyi....");
        let me = this;
        let gridData = me.data['gridData'];
        if (gridData.hasTranslate) {
            return;
        }
        plugin.translate({
            lfrom: app.config.CN,
            lto: app.config.EN,
            content: gridData.name,
            tts: true,
            success: (resTrans) => {

                let passRetcode = [
                    0, // 翻译合成成功
                    -10006, // 翻译成功，合成失败
                    -10007, // 翻译成功，传入了不支持的语音合成语言
                    -10008, // 翻译成功，语音合成达到频率限制
                ];

                if (passRetcode.indexOf(resTrans.retcode) >= 0) {
                    console.log("resTrans->", resTrans);
                    gridData.desctription = resTrans.result;
                    gridData.hasTranslate = true;
                    me.setData({gridData});
                    fetchStorage.setWord(gridData);
                    /*    let tmpDialogList = this.data.dialogList.slice(0);

                        if (!isNaN(index)) {

                            let tmpTranslate = Object.assign({}, item, {
                                autoPlay: true, // 自动播放背景音乐
                                translateText: resTrans.result,
                                translateVoicePath: resTrans.filename || "",
                                translateVoiceExpiredTime: resTrans.expired_time || 0
                            });

                            tmpDialogList[index] = tmpTranslate;


                            this.setData({
                                dialogList: tmpDialogList,
                                bottomButtonDisabled: false,
                                recording: false,
                            });

                            this.scrollToNew();

                        } else {
                            console.error("index error", resTrans, item)
                        }*/
                } else {
                    console.warn("翻译失败", resTrans, item)
                }

            },
            fail: function (resTrans) {
                console.error("调用失败", resTrans, item);
                this.setData({
                    bottomButtonDisabled: false,
                    recording: false,
                })
            },
            complete: resTrans => {
                this.setData({
                    recordStatus: 1,
                });
                wx.hideLoading()
            }
        })

    },
    bindchange: function (e) {

        let gridData = this.data.pageData[e.detail.current];
        fetchStorage.getWordById(gridData.id).then(res => {
            let gridData = res;
            let isFavorite = favorite.isFavorite("words", gridData);
            this.setData({gridData, isFavorite});
            if (this.data.innerAudioContext) {
                this.data.innerAudioContext.setSrc(gridData.audioUrl);
                if (fetchStorage.obj(app.config.profile, "autoPlay")) {
                    this.data.innerAudioContext.play();
                }
            }
        });

    },
    addOrCancelFavorite: function () {
        favorite.addOrCancel("words", this.data.gridData.id).then(res => {
            this.setData({isFavorite: res});
        });
    }

});
