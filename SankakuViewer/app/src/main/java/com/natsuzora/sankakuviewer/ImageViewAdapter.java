package com.natsuzora.sankakuviewer;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ImageViewAdapter extends RecyclerView.Adapter<ImageViewAdapter.InnerHolder> {
    private final List<itemBean> mData;
    private OnItemClickListener mOnItemClickListener;

    public ImageViewAdapter(List<itemBean> mData) {
        this.mData = mData;
    }

    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(),R.layout.item_list_view,null);
        return new InnerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {
        holder.setData(mData.get(position),position);
    }

    @Override
    public int getItemCount() {
        if (mData != null){
            return mData.size();
        }
        return 0;
    }

    public void setOnItemClickListener(OnItemClickListener Listener) {
        this.mOnItemClickListener = Listener;
    }

    public interface OnItemClickListener{
        void onItemClick(int position,String id);
    }

    public class InnerHolder extends RecyclerView.ViewHolder {
        private ImageView mImage;
        private int position;
        private String id;

        public InnerHolder(View itemView){
            super(itemView);
            mImage = (ImageView) itemView.findViewById(R.id.imageview);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null){
                        mOnItemClickListener.onItemClick(position,id);
                    }
                }
            });
        }

        public void setData(itemBean itemBean,int position) {
            this.position = position;
            this.id = itemBean.id;
            Picasso.get().load(itemBean.url).into(mImage);
        }
    }
}
