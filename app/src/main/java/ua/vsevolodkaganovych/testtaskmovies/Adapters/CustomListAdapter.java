package ua.vsevolodkaganovych.testtaskmovies.Adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import ua.vsevolodkaganovych.testtaskmovies.Models.Movie;
import ua.vsevolodkaganovych.testtaskmovies.R;

public class CustomListAdapter extends ArrayAdapter<Movie> {

    private String mDefaultPoster = "https://d3a8mw37cqal2z.cloudfront.net/assets/f996aa2014d2ffddfda8463c479898a3/images/no-poster-w185.jpg";
    public CustomListAdapter(Context context, ArrayList<Movie> items) {
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

        Movie item = getItem(position);

        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);
            viewHolder.mTitle = (TextView) convertView.findViewById(R.id.title);
            viewHolder.mDate = (TextView) convertView.findViewById(R.id.date);
            viewHolder.mRating = (TextView) convertView.findViewById(R.id.rating);
            viewHolder.mImageView = (ImageView) convertView.findViewById(R.id.imageView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.mTitle.setText(item.title);
        viewHolder.mDate.setText("Release date: " + item.releaseDate);
        viewHolder.mRating.setText("Rating: " + Double.toString(item.voteAverage));
        if (item.posterPath == null) {
            Picasso.with(getContext()).load(mDefaultPoster).resize(185, 277)
                    .placeholder(R.drawable.posterholder)
                    .into(viewHolder.mImageView);
        } else {
            Picasso.with(getContext()).load("http://image.tmdb.org/t/p/w185" + item.posterPath)
                    .placeholder(R.drawable.posterholder)
                    .into(viewHolder.mImageView);
        }

        return convertView;
    }
}
