const app = getApp();
const fetch = require('fetch');

module.exports.array = (key, childKey) => {
    return new Promise((resolve) => {
        let storageSync = wx.getStorageSync(key) || {};
        let childData = storageSync[childKey] || [];
        resolve(childData);

    })
};

module.exports.obj = (key, childKey) => {
    let storageSync = wx.getStorageSync(key) || {};
    let childData = storageSync[childKey];
    if (childData === undefined) {
        return false;
    } else {
        return childData;
    }
};


module.exports.getWordById = (id) => {
    let retWord = {};
    let wordsStorage = wx.getStorageSync(app.config.words);
    if (!wordsStorage) {
        wordsStorage = [];
        wx.setStorageSync(app.config.words, wordsStorage);
    }
    for (let word of wordsStorage) {
        if (id === word.id) {
            retWord = word;
            return new Promise((resolve) => {
                resolve(retWord);
            })
        }
    }
    return new Promise((resolve) => {
        fetch.fetchAvailable(app.config.apiObjectWordDetail + id).then(res => {
            if (res && res.data && res.data.id === id) {
                wordsStorage.push(res.data);
                retWord = res.data;
                wx.setStorageSync(app.config.words, wordsStorage);
            }
            resolve(retWord);
        })
    })
        ;


};

