package diggate.xpertise.com.diggateapk;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Build;
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
        //ll0001.setBackgroundColor(Color.parseColor(item.colorForm));

        RoundRectShape roundRectShape = new RoundRectShape(new float[]{
                0, 0, 360, 360,
                360, 360, 0, 0}, null, null);
        ShapeDrawable shapeDrawable = new ShapeDrawable(roundRectShape);
        shapeDrawable.getPaint().setColor(Color.parseColor(item.colorForm));
        shapeDrawable.setPadding(10, 10, 10, 10);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            ll0001.setBackground(shapeDrawable);
        } else {
            ll0001.setBackgroundColor(Color.parseColor(item.colorForm));
        }

        TextView id = vi.findViewById(R.id.tvIdEvent);
        id.setText("" + item.getId());

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

        /*ShapeDrawable sd = new ShapeDrawable(new OvalShape());
        sd.setIntrinsicHeight(100);
        sd.setIntrinsicWidth(100);
        sd.getPaint().setColor(Color.parseColor("#00ff00"));
        ivImage.setPadding(50, 50, 50, 50);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            ivImage.setBackground(sd);
            ivImage.setImageBitmap(decodedImage);
        } else {
            ivImage.setImageBitmap(decodedImage);
        }*/

        ShapeDrawable biggerCircle = new ShapeDrawable(new OvalShape());
        biggerCircle.setIntrinsicHeight(60);
        biggerCircle.setIntrinsicWidth(60);
        biggerCircle.setBounds(new Rect(0, 0, 60, 60));
        biggerCircle.getPaint().setColor(Color.parseColor(item.colorForm));
        biggerCircle.setPadding(15, 15, 15, 15);

        ShapeDrawable smallerCircle = new ShapeDrawable(new OvalShape());
        smallerCircle.setIntrinsicHeight(10);
        smallerCircle.setIntrinsicWidth(10);
        smallerCircle.setBounds(new Rect(0, 0, 10, 10));
        smallerCircle.getPaint().setColor(Color.WHITE);

        smallerCircle.setPadding(5, 5, 5, 5);
        Drawable[] d = {smallerCircle, biggerCircle};
        LayerDrawable composite1 = new LayerDrawable(d);
        //ivImage.setPadding(100, 100, 100, 100);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            ivImage.setBackground(composite1);
            ivImage.setImageBitmap(decodedImage);
        } else {
            ivImage.setImageBitmap(decodedImage);
        }

        return vi;
    }
}
