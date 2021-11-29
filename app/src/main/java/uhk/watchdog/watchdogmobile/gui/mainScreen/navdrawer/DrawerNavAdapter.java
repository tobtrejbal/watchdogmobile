package uhk.watchdog.watchdogmobile.gui.mainScreen.navdrawer;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import uhk.watchdog.watchdogmobile.app.AppCore;
import uhk.watchdog.watchdogmobile.R;

/**
 * Created by Tobous on 5. 2. 2015.
 */
public class DrawerNavAdapter extends RecyclerView.Adapter<DrawerNavAdapter.MainViewHolder> {

    /**
     *
     */
    private AppCore mAppCore;

    /**
     *
     */
    private List<DrawerItem> mDataSet;

    /**
     *
     */
    public static class MainViewHolder extends RecyclerView.ViewHolder {
        public MainViewHolder(View itemView) {
            super(itemView);
        }
    }

    /**
     *
     */
    public static class ViewHolderHeader extends MainViewHolder {
        // each SampleDAO item is just a string in this case
        public TextView titleView;
        public ViewHolderHeader(View itemView) {
            super(itemView);
            titleView = (TextView) itemView.findViewById(R.id.main_nav_drawer_list_view_item_header_title);
        }
    }

    /**
     *
     */
    public static class ViewHolderItem extends MainViewHolder {
        ImageView imgView;
        TextView titleView;
        public ViewHolderItem(View itemView) {
            super(itemView);
            imgView = (ImageView) itemView.findViewById(R.id.main_nav_drawer_list_view_item_img);
            titleView = (TextView) itemView.findViewById(R.id.main_nav_drawer_list_view_item_txt_title);
            itemView.setClickable(true);
        }
    }

    /**
     *
     * @param dataSet
     */
    public DrawerNavAdapter(List<DrawerItem> dataSet) {
        this.mDataSet = dataSet;
        this.mAppCore = AppCore.getInstance();
    }

    @Override
    public DrawerNavAdapter.MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0:
                return new ViewHolderHeader(LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_main_nav_list_header, parent, false));
            case 1:
                return new ViewHolderItem(LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_main_nav_list_item, parent, false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, int position) {
        int type = getItemViewType(position);

        switch(type) {
            case 0:
                ViewHolderHeader holderHeader = (ViewHolderHeader) holder;
                holderHeader.titleView.setText(mDataSet.get(position).getTitle());
                break;
            case 1:
                ViewHolderItem holderItem = (ViewHolderItem) holder;
                holderItem.titleView.setText(mDataSet.get(position).getTitle());
                holderItem.imgView.setImageResource(mDataSet.get(position).getIcon());
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    @Override
    public int getItemViewType(int position) {
        return  mDataSet.get(position).getType();
    }
}