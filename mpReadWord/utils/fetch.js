const app = getApp();
const login = require('login');
module.exports.fetch = (url, data, method = 'GET', header = {}) => {
    wx.showLoading({title: 'Loading...'});
    return new Promise((resolve, reject) => {
        wx.request({
            url: app.config.apiBase + url,
            data,
            header,
            method,
            dataType: 'json',
            success: function (data, statusCode, header) {
                console.log(data);
                console.log(statusCode);
                console.log(header);
                resolve(data)
            },
            fail: reject,
            complete: wx.hideLoading
        })
    })
};
module.exports.fetchAvailable = (url, data = {}, method = 'GET', header = {}) => {
    Object.assign(data, {'lifeStatus.equals': 'AVAILABLE'});
    return module.exports.loginAndFetch(url, data, method, header);
};
module.exports.loginAndFetch = (url, data, method = 'GET', header = {}) => {
    wx.showLoading({title: 'Loading...'});
    return login().then(res => {
        Object.assign(header, res);
        console.log("RequestHeader:" + res.Authorization);
        console.log("Fetch->Promise.......1");
        return new Promise((resolve, reject) => {
            console.log("Fetch->Promise.......2");
            wx.request({
                url: app.config.apiBaseApi + url,
                data,
                header,
                method,
                dataType: 'json',
                success: function (data) {
                    console.log(data);
                    if (data.data.status === 401) {
                        reject(401);
                    } else {
                        resolve(data)
                    }
                },
                fail: reject,
                complete: wx.hideLoading
            })
        })
    }).catch((vaule) => {
            console.log("Fetch->Promise.......3");
            if (vaule === 401) {
                wx.navigateTo({
                    url: '/pages/index/index?act=REMOVE_LOGIN'
                })
            }
        }
    )
        ;

};

module.exports.fromShare = (options) => {
    let sharedUserId = options.sharedUserId;
    return new Promise((resolve, reject) => {
        if (sharedUserId) {
            console.log("sharedUserId=", sharedUserId);
            return login(sharedUserId);
        } else {
            console.log("no share");
            return resolve({});
        }
    });
};


module.exports.onShare = (options, url, sharedUserId) => {
    if (options.from === 'button') {
        // 来自页面内转发按钮
        console.log(res.target)
    }
    return {
        title: '新新看图识字',
        path: url + '?sharedUserId=' + sharedUserId,
        success: function (res) {
        },
        fail: function (res) {
            // 转发失败
        }
    }
};
