module.exports = () => {
    wx.navigateToMiniProgram({
        appId: 'wx18a2ac992306a5a4',
        path: 'pages/apps/largess/detail?accountId=3025837',
        envVersion: 'release',
        success(res) {
            console.log(res);
        }
    })

};