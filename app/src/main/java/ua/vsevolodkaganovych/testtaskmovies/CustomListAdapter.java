package ua.vsevolodkaganovych.testtaskmovies;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CustomListAdapter extends ArrayAdapter<Result> {

    public CustomListAdapter(Context context, ArrayList<Result> items) {
        super(context, 0, items);
    }

    private static class ViewHolder {
        private TextView mTitle;
        private TextView mDate;
        private TextView mRating;
        private ImageView mImageView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Result item = getItem(position);

        ViewHolder viewHolder;
        if(convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);
            viewHolder.mTitle = (TextView)convertView.findViewById(R.id.title);
            viewHolder.mDate = (TextView)convertView.findViewById(R.id.date);
            viewHolder.mRating = (TextView)convertView.findViewById(R.id.rating);
            viewHolder.mImageView = (ImageView)convertView.findViewById(R.id.imageView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        viewHolder.mTitle.setText(item.title);
        viewHolder.mDate.setText("Release date: " + item.releaseDate);
        viewHolder.mRating.setText("Rating: " + Double.toString(item.voteAverage));
        Picasso.with(getContext()).load("http://image.tmdb.org/t/p/original" + item.posterPath)
                .resize(200, 200).centerCrop().into(viewHolder.mImageView);

        return convertView;
    }
}
