//pages/operation/operation.js
Page({

    /**
     * 页面的初始数据
     */
    data: {
        index: -1,
        content: ""
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function (options) {
        const that = this;
        //页面初始化 options 为页面跳转带来的参数
        this.setData({
            stuId: options.id,
        });
        if (options.id == undefined) {

        }
        // wx.request({
        //   url: "http://127.0.0.1:8080/demo/superadmin/getStudentById",
        //   data: { "stuId": options.id },
        //   method:'GET',
        //   success:function(res){
        //     var student = res.data.student;
        //     if(student == undefined){
        //       var toastText = '获取数据失败' +res.data.errMsg;
        //       wx.showToast({
        //         title: toastText,
        //         icon:'',
        //         duration:2000
        //       });
        //     }else{
        //       //index正好可以对应数组中每一个选项（我们角色中是从1开始的），因为数组是从0开始的，所以减一
        //       var index = student.roleId-1;
        //       var item = "items[" + index + "].checked";
        //       that.setData({
        //         stuName: student.name,
        //         [item]:true
        //       })
        //     }
        //   }
        // })
    },

    /**
     * 生命周期函数--监听页面初次渲染完成
     */
    onReady: function () {

    },

    /**
     * 生命周期函数--监听页面显示
     */
    onShow: function () {

    },

    /**
     * 生命周期函数--监听页面隐藏
     */
    onHide: function () {

    },

    /**
     * 生命周期函数--监听页面卸载
     */
    onUnload: function () {

    },

    /**
     * 页面相关事件处理函数--监听用户下拉动作
     */
    onPullDownRefresh: function () {

    },

    /**
     * 页面上拉触底事件的处理函数
     */
    onReachBottom: function () {

    },

    /**
     * 用户点击右上角分享
     */
    onShareAppMessage: function () {

    },
    formSubmit: function (e) {
        let that = this;
        const formData = e.detail.value;
        if (formData['index'] < 0) {
            try {
                let value = wx.getStorageSync('wordGroupKey');
                if (!value) {
                    value = [];
                }
                value.push({content: formData['content']});
                wx.setStorageSync('wordGroupKey', value);
            } catch (e) {
                console.log(2);
            }
        }

        wx.redirectTo({
            url: '../localList/localList'
        })

    }
});
