package com.example.recyclerviewpro.commonrv;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.example.recyclerviewpro.R;


public class UpLoadMoreHolder extends BasicHolder<UpLoadMoreHolder.Item>{


//    public UpLoadMoreHolder(Context context, ViewGroup parent) {
//        super(context, parent, R.layout.item_load_more);
//    }

    public UpLoadMoreHolder(Context context, ViewGroup parent, UpLoadMoreListener upLoadMoreListener) {
        super(context, parent, R.layout.item_load_more);
        this.upLoadMoreListener = upLoadMoreListener;
    }

    View load_more_load_fail_view;
    View load_more_loading_view;
    View load_more_load_end_view;
    @Override
    protected void bindView() {
        load_more_load_fail_view = itemView.findViewById(R.id.load_more_load_fail_view);
        load_more_loading_view = itemView.findViewById(R.id.load_more_loading_view);
        load_more_load_end_view = itemView.findViewById(R.id.load_more_load_end_view);
        load_more_load_fail_view.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
//        super.onClick(view);

        switch (view.getId()) {
            case R.id.load_more_load_fail_view:
                if (upLoadMoreListener != null) {
                    upLoadMoreListener.onClickUpReload();
                }
                break;
        }


    }

    @Override
    public void bindData(Item item, int position) {
        if (item == null) {
            return;
        }

        switch (item.status) {
            case Item.Status.NO_LOAD:
                load_more_loading_view.setVisibility(View.GONE);
                load_more_load_fail_view.setVisibility(View.GONE);
                load_more_load_end_view.setVisibility(View.GONE);
                break;
            case Item.Status.LOADING:
                load_more_loading_view.setVisibility(View.VISIBLE);
                load_more_load_fail_view.setVisibility(View.GONE);
                load_more_load_end_view.setVisibility(View.GONE);
                break;
            case Item.Status.LOAD_FAIL:
                load_more_loading_view.setVisibility(View.GONE);
                load_more_load_end_view.setVisibility(View.GONE);
                load_more_load_fail_view.setVisibility(View.VISIBLE);
                break;
            case Item.Status.LOAD_END:
                load_more_loading_view.setVisibility(View.GONE);
                load_more_load_fail_view.setVisibility(View.GONE);
                load_more_load_end_view.setVisibility(View.VISIBLE);
                break;

        }
    }

    public static class Item extends BasicHolder.Item{
        public int status;

        public static final class Status{
            public static final int NO_LOAD = 0; //no load
            public static final int LOADING = 1; //loading
            public static final int LOAD_FAIL = 2; //load fail
            public static final int LOAD_END = 3; //load all
        }
    }

    UpLoadMoreListener upLoadMoreListener;
    public interface UpLoadMoreListener{

        void onClickUpReload();
        void onUpLoadMore();

    }
}
