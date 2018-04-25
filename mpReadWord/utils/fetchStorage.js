const app = getApp();

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
        return true;
    } else {
        return childData;
    }
};





