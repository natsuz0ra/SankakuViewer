package com.natsuzora.sankakuviewer;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ImageViewAdapter2 extends BaseQuickAdapter<itemBean, BaseViewHolder> {
    public ImageViewAdapter2(int layoutResId, @Nullable List<itemBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, itemBean item) {
        Picasso.get().load(item.url).into((ImageView)helper.getView(R.id.imageview));
    }
}
