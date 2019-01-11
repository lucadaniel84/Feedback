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

public class VersionList extends ArrayAdapter<Version> {

    private Activity context;
    private List<Version> versionList;

    public VersionList(Activity context, List<Version> versionList){
        super(context, R.layout.layout_version_list, versionList);
        this.context = context;
        this.versionList = versionList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.layout_version_list, null, true);

        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewName);
        TextView textViewPlatform = (TextView) listViewItem.findViewById(R.id.textViewPlatform);

        Version version = versionList.get(position);

        textViewName.setText(version.getVersionName());
        textViewPlatform.setText(version.getVersionPlatform());

        return listViewItem;
    }
}
