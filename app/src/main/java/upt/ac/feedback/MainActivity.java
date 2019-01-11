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
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String VERSION_NAME = "versionname";
    public static final String VERSION_ID = "versionid";

    EditText editTextName;
    Button buttonAdd;
    Spinner spinnerPlatforms;

    DatabaseReference databaseVersions;

    ListView listViewVersions;

    List<Version> versions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseVersions = FirebaseDatabase.getInstance().getReference("versions");

        editTextName = (EditText) findViewById(R.id.editTextName);
        buttonAdd = (Button) findViewById(R.id.buttonAddVersion);
        spinnerPlatforms = (Spinner) findViewById(R.id.spinnerPlatforms);

        listViewVersions = (ListView) findViewById(R.id.listViewVersions);
        versions = new ArrayList<>();

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addVersion();
            }
        });

        listViewVersions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Version version = versions.get(position);
                Intent intent = new Intent(getApplicationContext(), AddCommentActivity.class);
                intent.putExtra(VERSION_ID, version.getVersionId());
                intent.putExtra(VERSION_NAME, version.getVersionName());
                startActivity(intent);
            }
        });

        listViewVersions.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Version version = versions.get(position);
                showUpdateDialog(version.getVersionId(), version.getVersionName());
                return false;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        // attaching value event listener
        databaseVersions.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                versions.clear();

                // iterating through all the nodes
                for(DataSnapshot versionSnapshot : dataSnapshot.getChildren()){
                    // getting version
                    Version version = versionSnapshot.getValue(Version.class);
                    // adding version to the list
                    versions.add(version);
                }

                // create an adapter
                VersionList adapter = new VersionList(MainActivity.this, versions);

                // attach adapter to listView
                listViewVersions.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showUpdateDialog(final String versionId, String versionName){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_dialog,null);
        dialogBuilder.setView(dialogView);

        final EditText editTextName = (EditText) dialogView.findViewById(R.id.editTextName);
        final Spinner spinnerPlatforms = (Spinner) dialogView.findViewById(R.id.spinnerPlatforms);
        final Button buttonUpdate = (Button) findViewById(R.id.buttonUpdate);
        final Button buttonDelete = (Button) dialogView.findViewById(R.id.buttonDelete);

        dialogBuilder.setTitle("Delete Version " + versionName + " ?");
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

// ======================================================================
//        buttonUpdate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String name = editTextName.getText().toString().trim();
//                String platform = spinnerPlatforms.getSelectedItem().toString();
//
//                if(TextUtils.isEmpty(name)){
//                    editTextName.setError("Version required");
//                    return;
//                }
//
//                updateVersion(versionId, name, platform);
//                alertDialog.dismiss();
//            }
//        });
// ======================================================================

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteVersion(versionId);
            }
        });

    }

    private void deleteVersion(String versionId) {
        DatabaseReference drVersion = FirebaseDatabase.getInstance().getReference("versions").child(versionId);
        DatabaseReference drComments = FirebaseDatabase.getInstance().getReference("comments").child(versionId);

        drVersion.removeValue();
        drComments.removeValue();

        Toast.makeText(this, "Version is deleted", Toast.LENGTH_LONG).show();
    }

// ======================================================================
//    private void updateVersion(String id, String name, String platform){
//        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("versions").child(id);
//        Version version = new Version(id, name, platform);
//        databaseReference.setValue(version);
//        Toast.makeText(this, "Version Updated Successfully", Toast.LENGTH_LONG).show();
////        return true;
//    }
// ======================================================================


    // this method is saving a new version to the Firebase Realtime Database
    private void addVersion(){
        // getting the values to save
        String name = editTextName.getText().toString().trim();
        String platform = spinnerPlatforms.getSelectedItem().toString().trim();

        // checking if the value is provided
        if(!TextUtils.isEmpty(name)){

            // getting a unique id using .push().getKey() method
            // it will create a unique id and we will use it as the primary key for our versions
            String id = databaseVersions.push().getKey();

            // create an Version object
            Version version = new Version(id, name, platform);

            // saving the versions
            databaseVersions.child(id).setValue(version);

            // setting edittext to blank again
            editTextName.setText("");

            // display a success toast
            Toast.makeText(this, "Version added", Toast.LENGTH_LONG).show();
        } else {
            // if the value is not given display a toast
            Toast.makeText(this, "Version should not be empty", Toast.LENGTH_LONG).show();
        }
    }
}
