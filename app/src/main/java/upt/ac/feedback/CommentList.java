package upt.ac.feedback;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class CommentList extends ArrayAdapter<Comment> {
    private Activity context;
    private List<Comment> comments;

    public CommentList(Activity context, List<Comment> comments){
        super(context, R.layout.layout_version_list, comments);
        this.context = context;
        this.comments = comments;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.layout_comment_list, null, true);

        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewName);
        TextView textViewRating = (TextView) listViewItem.findViewById(R.id.textViewRating);

        Comment comment = comments.get(position);

        textViewName.setText((comment.getCommentString()));
        textViewRating.setText(String.valueOf(comment.getCommentRating()));

        return listViewItem;
    }
}
