package ws.wolfsoft.get_detail;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.trosales.hireusapp.R;
import com.trosales.hireusapp.classes.beans.Reviews;

import java.util.ArrayList;


public class JayBaseAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Reviews> reviewsArrayList;
    private Typeface fonts1,fonts2;

    public JayBaseAdapter(Context context, ArrayList<Reviews> reviewsArrayList) {
        this.context = context;
        this.reviewsArrayList = reviewsArrayList;
    }

    @Override
    public int getCount() {
        return reviewsArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return reviewsArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        fonts1 =  Typeface.createFromAsset(context.getAssets(),
                "fonts/Lato-Light.ttf");

        fonts2 = Typeface.createFromAsset(context.getAssets(),
                "fonts/Lato-Regular.ttf");

        ViewHolder viewHolder;

        if (convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.reviews_list,null);

            viewHolder = new ViewHolder();

            viewHolder.revieweeDisplayPicture = convertView.findViewById(R.id.image);
            viewHolder.revieweeComment = convertView.findViewById(R.id.title);
            viewHolder.revieweeRating = convertView.findViewById(R.id.description);
            viewHolder.reviewee = convertView.findViewById(R.id.date);

            viewHolder.revieweeComment.setTypeface(fonts2);
            viewHolder.revieweeRating.setTypeface(fonts1);

            viewHolder.reviewee.setTypeface(fonts2);

            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        Reviews reviews = (Reviews) getItem(position);
        viewHolder.revieweeDisplayPicture.setImageResource(reviews.getRevieweeDisplayPicture());
        viewHolder.revieweeComment.setText(reviews.getRevieweeComment());
        viewHolder.revieweeRating.setText(reviews.getRevieweeRating());
        viewHolder.reviewee.setText(reviews.getReviewee());

        return convertView;
    }

    private class ViewHolder{
        ImageView revieweeDisplayPicture;
        TextView revieweeComment;
        TextView revieweeRating;
        TextView reviewee;
    }
}




