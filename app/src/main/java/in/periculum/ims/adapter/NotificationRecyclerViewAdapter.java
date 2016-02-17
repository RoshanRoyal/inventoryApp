package in.periculum.ims.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;
import java.util.List;

import in.periculum.ims.R;
import in.periculum.ims.application.AppController;
import in.periculum.ims.ds.NotificationDs;
import in.periculum.ims.ds.StockRcv;

/**
 * Created by ROYAL on 10/31/2015.
 */
public class NotificationRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView title;

        private final TextView subject;
        private final TextView dayelasped;

        private final NetworkImageView notiimg;

        public NetworkImageView getNotiimg() {
            return notiimg;
        }

        public TextView getTitle() {
            return title;
        }

        public TextView getSubject() {
            return subject;
        }

        public TextView getDayelasped() {
            return dayelasped;
        }

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            subject = (TextView) itemView.findViewById(R.id.bodynoti);
            dayelasped = (TextView) itemView.findViewById(R.id.dayelsped);
            notiimg = (NetworkImageView) itemView.findViewById(R.id.notiimg);

        }
    }

    List<NotificationDs> notificationDses;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private Activity activity;

    public NotificationRecyclerViewAdapter(List<NotificationDs> notificationDses, Activity activity) {
        this.notificationDses = notificationDses;
        this.activity = activity;
    }

    @Override
    public int getItemViewType(int position) {
        return notificationDses.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }


    RecyclerView.ViewHolder vh;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_card_small, parent, false);

            vh = new ViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.progress_item, parent, false);

            vh = new ProgressViewHolder(v);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {

            ((ViewHolder) holder).getTitle().setText(notificationDses.get(position).getNotificationSubject());
            ((ViewHolder) holder).getSubject().setText(notificationDses.get(position).getNotificationRemarks());

            ((ViewHolder) holder).getDayelasped().setText(notificationDses.get(position).getDatetimeEntered());

            if(!TextUtils.isEmpty( notificationDses.get(position).getImagePath()))
            ((ViewHolder) holder).getNotiimg().setImageUrl("http://www.analytics.periculum.in/" + notificationDses.get(position).getImagePath(), AppController.getInstance().getImageLoader());
        } else {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return notificationDses.size();
    }

}
