package com.crazysunj.crazydaily.ui.adapter;

import com.crazysunj.crazydaily.R;
import com.crazysunj.crazydaily.base.BaseAdapter;
import com.crazysunj.crazydaily.base.BaseViewHolder;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.ImageViewState;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * author: sunjian
 * created on: 2018/10/23 下午5:09
 * description: https://github.com/crazysunj/CrazyDaily
 */
public class ImagePreViewAdapter extends BaseAdapter<ImagePreViewAdapter.ImagePreViewEntity, BaseViewHolder> {

    public static class ImagePreViewEntity {
        private String original;
        private String path;
        private float maxScale;
        private ImageSource source;
        private ImageViewState state;

        public ImagePreViewEntity(String original, String path, float maxScale, ImageSource source, ImageViewState state) {
            this.original = original;
            this.path = path;
            this.maxScale = maxScale;
            this.source = source;
            this.state = state;
        }
    }

    public ImagePreViewAdapter(List<ImagePreViewAdapter.ImagePreViewEntity> data) {
        super(data, R.layout.layout_item_image_preview);
    }

    @Override
    protected void convert(BaseViewHolder holder, ImagePreViewAdapter.ImagePreViewEntity data) {
        SubsamplingScaleImageView imageView = holder.getView(R.id.item_image_preview, SubsamplingScaleImageView.class);
        imageView.setMaxScale(data.maxScale);
        imageView.setImage(data.source, data.state);
    }

    public ArrayList<String> getData() {
        ArrayList<String> data = new ArrayList<>();
        for (ImagePreViewEntity entity : mData) {
            data.add(entity.original);
        }
        return data;
    }

    public void removeImage(int position) {
        mData.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mData.size() - position);
    }

    public void deleteTemFile() {
        for (ImagePreViewEntity entity : mData) {
            File file = new File(entity.path);
            if (file.exists()) {
                //noinspection ResultOfMethodCallIgnored
                file.delete();
            }
        }
    }
}
