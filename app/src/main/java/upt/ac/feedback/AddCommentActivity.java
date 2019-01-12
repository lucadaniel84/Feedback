  package upt.ac.feedback;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

  public class AddCommentActivity extends AppCompatActivity {

    TextView textViewVersionName;
    EditText editTextComment;
    SeekBar seekBarRating;
    ListView listViewComments;
    Button buttonAddComment;

    String id;

    private void setId(String id){
        this.id = id;
    }

    DatabaseReference databaseComments;

    List<Comment> comments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_comment);

        textViewVersionName = (TextView) findViewById(R.id.textViewVersionName);
        editTextComment = (EditText) findViewById(R.id.editTextAddComment);
        seekBarRating = (SeekBar) findViewById(R.id.seekBarRating);
        buttonAddComment = (Button) findViewById(R.id.buttonAddComment);

        listViewComments = (ListView) findViewById(R.id.listViewComments);

        Intent intent = getIntent();

        comments = new ArrayList<>();

        String id = intent.getStringExtra(MainActivity.VERSION_ID);
        String name = intent.getStringExtra(MainActivity.VERSION_NAME);

        setId(id);

        textViewVersionName.setText(name);

        databaseComments = FirebaseDatabase.getInstance().getReference("comments").child(id);

        buttonAddComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveComment();
            }
        });


//==================================== Developer Only ====================================
        listViewComments.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Comment comment = comments.get(position);
                showDeleteDialog(comment.getCommentId()); // does not do anything
                return false; // true
            }
        });
//==================================== Developer Only ====================================


    }

      @Override
      protected void onStart() {
          super.onStart();

          databaseComments.addValueEventListener(new ValueEventListener() {
              @Override
              public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                  comments.clear();

                  for(DataSnapshot commentSnapshot : dataSnapshot.getChildren()){
                      Comment comment = commentSnapshot.getValue(Comment.class);
                      comments.add(comment);
                  }

                  CommentList commentListAdapter = new CommentList(AddCommentActivity.this, comments);
                  listViewComments.setAdapter(commentListAdapter);
              }

              @Override
              public void onCancelled(@NonNull DatabaseError databaseError) {

              }
          });
      }


//==================================== Developer Only ====================================
      private void showDeleteDialog(final String commentId){
          AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
          LayoutInflater inflater = getLayoutInflater();
          final View dialogView = inflater.inflate(R.layout.delete_dialog,null);
          dialogBuilder.setView(dialogView);
          final Button buttonDelete = (Button) dialogView.findViewById(R.id.buttonDelete);
          dialogBuilder.setTitle("Delete comment ?");
          final AlertDialog alertDialog = dialogBuilder.create();
          alertDialog.show();

          buttonDelete.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  deleteComment(commentId);
                  alertDialog.dismiss();
              }
          });
      }

      private void deleteComment(String commentId){
        DatabaseReference drComment = FirebaseDatabase.getInstance().getReference("comments").child(id).child(commentId);
        drComment.removeValue(); // remove comment
        Toast.makeText(this, "Comment deleted !", Toast.LENGTH_LONG).show();
      }
//==================================== Developer Only ====================================


      private void saveComment(){
        String commentString = editTextComment.getText().toString().trim();
        int rating = seekBarRating.getProgress();

        if(!TextUtils.isEmpty(commentString)){// save the comment
            // generate a comment id and get the unique key
            String id = databaseComments.push().getKey();

            Comment comment = new Comment(id, commentString, rating);

            databaseComments.child(id).setValue(comment);

            // clear textview after adding the comment
            editTextComment.setText("");

            Toast.makeText(this, "Comment saved successfully", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Comment should not be empty", Toast.LENGTH_LONG).show();
        }
    }
}
