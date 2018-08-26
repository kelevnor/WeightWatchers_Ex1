package com.kelevnor.weightwatchers_ex1.ADAPTER;

import android.app.Activity;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.kelevnor.weightwatchers_ex1.ImageLoader.ImageLoader;
import com.kelevnor.weightwatchers_ex1.MODELS.Item;
import com.kelevnor.weightwatchers_ex1.R;
import com.kelevnor.weightwatchers_ex1.UTILITY.Config;
import java.util.List;

/**
 * Created by kelevnor on 08/25/2018.
 */

public class Adapter_ListItem extends RecyclerView.Adapter<Adapter_ListItem.ViewHolder> {
    private List<Item> searchList;
    Typeface fontAwesome, openSansRegular, openSansSemiBold;
    ImageLoader imageLoader;
    onItemClickListener listener;

    Activity act;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layout;
        ImageView image;
        TextView textLine1;

        public ViewHolder(LinearLayout v) {
            super(v);
            layout = v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public Adapter_ListItem(Activity act, List<Item> searchList, onItemClickListener listener) {
        this.searchList = searchList;
        this.act = act;
        this.listener = listener;
        imageLoader = new ImageLoader(act);
        fontAwesome = Typeface.createFromAsset(act.getAssets(),"fonts/fontawesome-webfont.ttf");
        openSansRegular = Typeface.createFromAsset(act.getAssets(),"fonts/OpenSans-Regular.ttf");
        openSansSemiBold = Typeface.createFromAsset(act.getAssets(),"fonts/OpenSans-Semibold.ttf");
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        vh.layout = v.findViewById(R.id.ll_layout);
        vh.textLine1 = v.findViewById(R.id.tv_line1);
        vh.image = v.findViewById(R.id.iv_image);
        vh.textLine1.setTypeface(openSansSemiBold);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        imageLoader.DisplayImage(Config.BASE_URL+searchList.get(position).getImage(), holder.image, act);
        holder.textLine1.setText(searchList.get(position).getTitle());

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(searchList.get(position));
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return searchList.size();
    }

    public interface onItemClickListener{
        void onItemClick(Item item);
    }

    private void setBottomPaddingToView(float padding, LinearLayout view){
        float scale = act.getResources().getDisplayMetrics().density;
        int dpAsPixels = (int) (padding*scale + 0.5f);
        view.setPadding(0,0,0, dpAsPixels);
    }
}