package com.example.recyclerviewpro.commonrv;

import java.util.ArrayList;

public abstract class UpLoadMoreAdapter<H extends BasicHolder<D>, D extends BasicHolder.Item> extends BasicAdapter<H, D> {

    public UpLoadMoreAdapter(ArrayList<D> items) {
        super(items);
    }

    public UpLoadMoreAdapter(ArrayList<D> items, BasicAdapter.ItemClickListener<D> itemClickListener) {
        super(items, itemClickListener);
    }

    @Override
    int getDataItemPosition(int sourcePosition) {
        return sourcePosition - headerCount - loadMoreCount;
    }

    @Override
    int getLoadMoreItemPosition() { //loadMore item in last
        return (0);
    }

    @Override
    int getFooterItemPosition() {
        return (getActualItemsSize() - 1); //footer item in last
    }

    @Override
    int getHeaderItemPosition() { //header item in first
        return (headerCount + 1);
    }




}
