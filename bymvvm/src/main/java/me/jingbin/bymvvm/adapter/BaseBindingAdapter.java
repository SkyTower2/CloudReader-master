package me.jingbin.bymvvm.adapter;


import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;

import java.util.List;

import me.jingbin.library.adapter.BaseByRecyclerViewAdapter;

/**
 * 【databinding】单一 item 类型 adapter
 * https://github.com/youlookwhat/ByRecyclerView【第三方库】
 */
public abstract class BaseBindingAdapter<T, B extends ViewDataBinding> extends BaseByRecyclerViewAdapter<T, BaseBindingHolder<T, B>> {

    private int mLayoutId;

    public BaseBindingAdapter(@LayoutRes int layoutId) {
        mLayoutId = layoutId;
    }

    public BaseBindingAdapter(@LayoutRes int layoutId, List<T> data) {
        super(data);
        mLayoutId = layoutId;
    }

    @NonNull
    @Override
    public BaseBindingHolder<T, B> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(parent, mLayoutId);
    }

    /**
     * ViewHolder 内部类
     */
    private class ViewHolder extends BaseBindingHolder<T, B> {
        ViewHolder(ViewGroup viewGroup, int layoutId) {
            super(viewGroup, layoutId);
        }

        /**
         * 用于绑定数据到视图
         *
         * @param holder
         * @param bean
         * @param position
         */
        @Override
        protected void onBindingView(BaseBindingHolder holder, T bean, int position) {
            if (holder != null && bean != null && binding != null) {
                bindView(holder, bean, binding, position);
            }
        }

        /**
         * 用于处理局部更新
         *
         * @param holder
         * @param bean
         * @param position
         * @param payloads
         */
        @Override
        protected void onBindingViewPayloads(BaseBindingHolder holder, T bean, int position, @NonNull List<Object> payloads) {
            if (holder != null && bean != null && binding != null) {
                bindViewPayloads(holder, bean, binding, position, payloads);
            }
        }
    }

    /**
     * 这是一个抽象方法，要求子类实现具体的视图绑定逻辑。传入的参数包括 holder、数据模型 bean、绑定对象 binding 和位置 position
     *
     * @param holder
     * @param bean
     * @param binding
     * @param position
     */
    protected abstract void bindView(BaseBindingHolder holder, T bean, B binding, int position);

    /**
     * 局部刷新，非必须
     */
    protected void bindViewPayloads(@NonNull BaseBindingHolder holder, @NonNull T bean, @NonNull B binding, int position, @NonNull List<Object> payloads) {
        /*
         * fallback to bindViewPayloads(holder, bean,position) if app does not override this method.
         * 如果不覆盖 bindViewPayloads() 方法，就走 bindView()
         */
        bindView(holder, bean, binding, position);
    }
}

