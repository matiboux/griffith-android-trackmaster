package com.matiboux.griffith.trackmaster;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class ListResultsAdapter extends ArrayAdapter<String> implements View.OnClickListener {

    Context context;
    private List<String> objects;

    public ListResultsAdapter(@NonNull Context context, @NonNull List<String> objects) {
        super(context, R.layout.adapter_list_results, objects);
        this.context = context;
        this.objects = objects;
    }

    @Override
    public void onClick(View view) {
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Get the data item for this position
        String filename = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        if (convertView != null) viewHolder = (ViewHolder) convertView.getTag();
        else {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.adapter_list_results, parent, false);

            viewHolder.txv_filename = convertView.findViewById(R.id.txv_filename);

            convertView.setTag(viewHolder);
        }

        if (!TextUtils.isEmpty(filename)) {
            // Set filename
            viewHolder.txv_filename.setText(filename);
        }

        // Return the completed view to render on screen
        return convertView;
    }

    // View lookup cache
    private static class ViewHolder {
        TextView txv_filename;
    }
}