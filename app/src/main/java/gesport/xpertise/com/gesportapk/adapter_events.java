package gesport.xpertise.com.gesportapk;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class adapter_events extends BaseAdapter {

    protected Activity activity;
    protected ArrayList<obj_events> items;

    public adapter_events(Activity activity, ArrayList<obj_events> items) {
        this.activity = activity;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return items.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View vi = convertView;

        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            vi = inflater.inflate(R.layout.personalization, null);

        }

        obj_events item = items.get(position);

        LinearLayout ll0001 = vi.findViewById(R.id.ll0001);
        ll0001.setBackgroundColor(Color.parseColor(item.colorForm));

            TextView id = vi.findViewById(R.id.tvIdEvent);
            id.setText(""+item.getId());

            TextView values = vi.findViewById(R.id.tvKeyValue);
            values.setText(item.getVariable());

            TextView startDate = vi.findViewById(R.id.tvDateEventBegin);
            startDate.setText(item.getFecha_inicio());

            TextView endDate = vi.findViewById(R.id.tvDateEventEnd);
            endDate.setText(item.getFecha_fin());

            ImageView ivImage = vi.findViewById(R.id.iv0001);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] imageBytes = baos.toByteArray();
            imageBytes = Base64.decode(item.getIdIconForm(), Base64.DEFAULT);
            Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            ivImage.setImageBitmap(decodedImage);

        return vi;
    }
}
