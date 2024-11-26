package com.example.jingbin.cloudreader.bean.wanandroid;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.example.jingbin.cloudreader.BR;

public class CoinUserInfoBean extends BaseObservable {
    /**
     * 继承BaseObservable，实现数据绑定
     * 表示这个类的实例是 可观察的，可以与 Data Binding 机制结合使用。
     *
     */

    /**
     * coinCount : 451
     * rank : 7
     * userId : 2
     * username : x**oyang
     */

    private int coinCount;//金币数量
    private int rank;//排名
    private int userId;//用户ID
    private String username;//用户名（包含星号）

    @Bindable
    public int getCoinCount() {
        return coinCount;
    }

    /**
     * notifyPropertyChanged 是 BaseObservable 提供的方法，用于通知 Data Binding 系统，属性值发生了变化。
     * 一旦调用此方法，绑定到 coinCount 的 UI 会自动刷新显示新的值
     *
     * @param coinCount
     */
    public void setCoinCount(int coinCount) {
        this.coinCount = coinCount;
        notifyPropertyChanged(BR.coinCount);
    }

    @Bindable
    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
        notifyPropertyChanged(BR.rank);
    }

    @Bindable
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
        notifyPropertyChanged(BR.userId);
    }

    @Bindable
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
        notifyPropertyChanged(BR.username);
    }
}
