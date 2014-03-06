package de.anneundsebp.ondemand;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import de.anneundsebp.ondemand.parser.Asset;

public class CatalogAssetArrayAdapter extends ArrayAdapter<Object> {
	
	private static LayoutInflater inflater= null;
	
//	String image = "http://fm4.orf.at/v2static/storyimages/site/fm4/20131040/fin_MG_7204_body_radioprogramm.jpg";

	public CatalogAssetArrayAdapter(Context context,
			List<Object> objects) {
		super(context, R.layout.list_layout, objects);
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.list_layout, null);
 
        TextView title = (TextView)vi.findViewById(R.id.title); // title
        title.setText(getItem(position).toString());
//        ImageView thumb_image=(ImageView)vi.findViewById(R.id.list_image);
//        thumb_image.setImageURI(null);
//        thumb_image.setImageURI(Uri.parse(image));
        
        ImageButton downloadButton = (ImageButton) vi.findViewById(R.id.downloadButton);
        ImageButton playButton = (ImageButton) vi.findViewById(R.id.playButton);
        if (getItem(position) instanceof Asset) {
	        downloadButton.setTag(Integer.valueOf(position));
	        downloadButton.setOnClickListener((OnClickListener)getContext());
	        downloadButton.setVisibility(View.VISIBLE);
	        playButton.setTag(Integer.valueOf(position));
	        playButton.setOnClickListener((OnClickListener)getContext());
	        playButton.setVisibility(View.VISIBLE);
        } else {
        	downloadButton.setVisibility(View.INVISIBLE);
        	playButton.setVisibility(View.INVISIBLE);
        }
        return vi;
	}

}
