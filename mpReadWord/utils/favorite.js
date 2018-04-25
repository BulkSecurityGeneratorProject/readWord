const app = getApp();

module.exports.addOrCancel = (key, value) => {
    return new Promise((resolve) => {
        let favorite = wx.getStorageSync(app.config.favorite) || {};
        let data = favorite[key] || [];
        let hasValue = false;
        let index = 0;
        if (data.length > 0) {
            for (let obj of data) {
                if (obj === value) {
                    hasValue = true;
                    break;
                }
                index++;
            }

        }
        if (!hasValue) {
            data.push(value);
            wx.showToast({
                title: '收藏成功',
                icon: 'success',
                duration: 2000
            })
        } else {
            data.splice(index, 1);
            wx.showToast({
                title: '取消成功',
                icon: 'success',
                duration: 2000
            })
        }
        favorite[key] = data;
        wx.setStorageSync(app.config.favorite, favorite);
        resolve(!hasValue);

    })
};
module.exports.isFavorite = (key, value) => {
    let favorite = wx.getStorageSync(app.config.favorite) || {};
    let data = favorite[key] || [];
    let hasValue = false;
    let index = 0;
    if (data.length > 0) {
        for (let obj of data) {
            if (obj === value.id) {
                hasValue = true;
                break;
            }
            index++;
        }
    }
    return hasValue;
};