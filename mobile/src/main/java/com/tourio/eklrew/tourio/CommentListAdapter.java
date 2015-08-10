package com.tourio.eklrew.tourio;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Prud on 7/31/2015.
 */
public class CommentListAdapter extends BaseAdapter {
    private Context context;
    private List<Comment> comments;
    private static LayoutInflater inflater = null;

    public CommentListAdapter(Context context,List<Comment> comments) {
        this.context = context;
        this.comments = comments;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return comments.size();
    }

    @Override
    public Object getItem(int position) {
        return comments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position,View convertView,ViewGroup parent) {
        final Comment comment = comments.get(position);
        User commenter = comment.getCommenter();

        View commentView = convertView;
        if (commentView == null)
            commentView = inflater.inflate(R.layout.comment_list_item, null);

        ImageView commenterImageView = (ImageView)commentView.findViewById(R.id.commenter_image);
        TextView commenterNameView = (TextView)commentView.findViewById(R.id.commenter_name);
        final TextView commentTextView = (TextView)commentView.findViewById(R.id.comment_text);
        FrameLayout ratingFrame = (FrameLayout) commentView.findViewById(R.id.rating_frame);
        LinearLayout parentLayout = (LinearLayout) commentView.findViewById(R.id.comment_parent_layout);

        if (comment.getImage() != null) {
            commenterImageView.setImageBitmap(comment.getImage());
        }
        else {
            comment.loadImage(commenterImageView);
        }

        commenterNameView.setText(commenter.getName());
        commentTextView.setText(comment.getText());

        TourioHelper.LayoutHelper.setRatingImage(inflater, ratingFrame, comment.getRating());

        parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (comment.isExpanded()) {
                    Log.v(">CommentListAdapter<","expanded comment was clicked");
                    commentTextView.setMaxLines(2);
                    commentTextView.setEllipsize(TextUtils.TruncateAt.END);
                    comment.setExpanded(false);
                } else {
                    Log.v(">CommentListAdapter<","compressed comment was clicked");
                    commentTextView.setMaxLines(Integer.MAX_VALUE);
                    commentTextView.setEllipsize(null);
                    comment.setExpanded(true);
                }
            }
        });

        return commentView;
    }
}