const app = getApp();

module.exports = () => {
    let header = {};
    let jhipsterHeader = wx.getStorageSync(app.config.jhpsterHeader);
    if (jhipsterHeader) {
        if (jhipsterHeader.header.Authorization)
            header.Authorization = jhipsterHeader.header.Authorization;
        return new Promise(resolve => {
            console.log("Promise.......1");
            resolve(header);
        })
    } else {
        return new Promise((resolve, reject) => {
            console.log("Promise.......2");
            wx.login({success: resolve, fail: reject})
        }).then(res => {
            return new Promise((resolve, reject) => {
                console.log("Promise.......3");
                wx.request({
                    url: app.config.apiLogin,
                    data: {
                        code: res.code
                    }, success: function (jhipsterHeader) {
                        resolve(jhipsterHeader);
                    }, fail: function (error) {
                        console.log(error);
                    }, complete: function () {
                        console.log("complete")
                    }
                })
            }).then(jhipsterHeader => {
                wx.setStorageSync(app.config.jhpsterHeader, jhipsterHeader);
                return new Promise(resolve => {
                    console.log("Promise.......4");
                    resolve({Authorization: jhipsterHeader.header.Authorization})
                })
            })
                .catch(error => {
                    console.log("Login Last catch.....");
                    console.log(error);
                    wx.hideLoading();
                    return new Promise(resolve => {
                        wx.showToast({
                            title: app.message.unAuth,
                            icon: "none",
                            duration: 5000
                        });
                        throw new Error(error);
                    })
                })

        });

    }


};



