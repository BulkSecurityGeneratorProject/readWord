Page({
    /**
     * 页面的初始数据
     */
    data: {
        words: {}
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad(options) {
        let word = options.word;
        let words = word.split("");
        words.sort(this.randomSort);
        this.setData({words: words})
    },

    randomSort(a, b) {
        return Math.random() > 0.5 ? -1 : 1;
    },

    previewHandle(e) {

    }
});
