package com.example.jingbin.cloudreader.viewmodel.wan;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;

import com.example.jingbin.cloudreader.bean.wanandroid.BaseResultBean;
import com.example.jingbin.cloudreader.bean.wanandroid.CoinUserInfoBean;
import com.example.jingbin.cloudreader.data.UserUtil;
import com.example.jingbin.cloudreader.data.OnUserInfoListener;
import com.example.jingbin.cloudreader.http.HttpClient;
import com.example.jingbin.cloudreader.utils.DataUtil;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import me.jingbin.bymvvm.base.BaseViewModel;
import me.jingbin.bymvvm.room.User;

/**
 * @author jingbin
 * @data 2019/9/24
 * @Description 首页ViewModel
 */
public class MainViewModel extends BaseViewModel {

    /**
     * ObservableField与MutableLiveData使用
     * ObservableField：
     * 线程不安全，不建议在多线程中直接使用。
     * 适用于简单的数据绑定场景。
     * 通知机制：当调用 set() 方法改变值时，通知绑定的 UI 组件更新。
     * <p>
     * MutableLiveData：
     * 线程安全的，可以跨线程使用。
     * 适用于更复杂的场景，特别是需要在多个组件之间共享数据或处理复杂业务逻辑的情况。
     * 通知机制：通过 postValue()（子线程）或 setValue()（主线程）方法更新数据，并通知所有观察者。
     */

    // 问题反馈是否已读
    public ObservableField<Boolean> isReadOk = new ObservableField<>();
    // 深色模式是否已读
    public ObservableField<Boolean> isReadOkNight = new ObservableField<>();
    // 赞赏入口是否开放
    public ObservableField<Boolean> isShowAdmire = new ObservableField<>();
    public MutableLiveData<CoinUserInfoBean> coin = new MutableLiveData<>();

    public MainViewModel(@NonNull Application application) {
        super(application);
        isShowAdmire.set(DataUtil.isShowAdmire());
    }

    public void getUserInfo() {
        UserUtil.getUserInfo(new OnUserInfoListener() {
            @Override
            public void onSuccess(User user) {
                if (user != null) {
                    execute(HttpClient.Builder.getWanAndroidServer().getCoinUserInfo(),
                            new Observer<BaseResultBean<CoinUserInfoBean>>() {
                                @Override
                                public void onSubscribe(Disposable d) {
                                    addDisposable(d);
                                }

                                @Override
                                public void onNext(BaseResultBean<CoinUserInfoBean> bean) {
                                    if (bean != null && bean.getData() != null) {
                                        CoinUserInfoBean infoBean = bean.getData();
                                        infoBean.setUsername(user.getUsername());
                                        coin.setValue(infoBean);

                                        UserUtil.getUserInfo(new OnUserInfoListener() {
                                            @Override
                                            public void onSuccess(User user) {
                                                if (user != null) {
                                                    user.setCoinCount(infoBean.getCoinCount());
                                                    user.setRank(infoBean.getRank());
                                                    UserUtil.setUserInfo(user);
                                                }
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onError(Throwable e) {
                                    coin.setValue(null);
                                }

                                @Override
                                public void onComplete() {

                                }
                            });
                } else {
                    coin.setValue(null);
                }
            }
        });
    }

    public MutableLiveData<CoinUserInfoBean> getCoin() {
        return coin;
    }
}
