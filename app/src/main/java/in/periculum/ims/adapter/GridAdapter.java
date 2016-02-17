package in.periculum.ims.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.provider.MediaStore.Video.Thumbnails;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import java.io.File;
import java.util.List;

import in.periculum.ims.R;

public class GridAdapter extends BaseAdapter {

    private Activity activity;
    private final List<Bitmap> imgpaths;
    LayoutInflater inflater;

    public GridAdapter(Activity activity, List<Bitmap> imgpaths) {
        this.activity = activity;
        this.inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.imgpaths = imgpaths;

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return imgpaths.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    DisplayMetrics dm;

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    private int getViewHeight(int weight) {
        int percentage = (weight * 100) / 1200;
        int height = (dm.heightPixels * percentage) / 100;
        return height;
    }

    private int getViewWidth(int weight) {
        int percentage = (weight * 100) / 760;
        int width = (dm.widthPixels * percentage) / 100;
        return width;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        dm = activity.getResources().getDisplayMetrics();
        Holder holder = new Holder();
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.galleryitem, null);

            convertView.setLayoutParams(new ListView.LayoutParams(
                    getViewWidth(200), getViewHeight(200)));
            holder.video = (ImageView) convertView.findViewById(R.id.thumbImage);
            holder.close = (ImageView) convertView.findViewById(R.id.close);

            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        final Bitmap f = imgpaths.get(position);
        holder.video.setScaleType(ImageView.ScaleType.FIT_XY);
        holder.video.setImageBitmap(f);
        holder.close.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("info !!!");

                builder.setMessage("Are you sure, you want to delete Image");
                builder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int arg1) {

                                imgpaths.remove(f);
                                notifyDataSetChanged();
                                dialog.cancel();
                            }
                        });
                builder.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // TODO Auto-generated method stub
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.setCanceledOnTouchOutside(true);
                alert.show();

            }
        });
        return convertView;
    }


    private class Holder {
        ImageView video;
        ImageView close;

    }
}