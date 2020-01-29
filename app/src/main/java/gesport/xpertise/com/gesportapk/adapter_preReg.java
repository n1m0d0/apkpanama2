package gesport.xpertise.com.gesportapk;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
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

public class adapter_preReg extends BaseAdapter {

    protected Activity activity;
    protected ArrayList<obj_preReg> items;

    public adapter_preReg(Activity activity, ArrayList<obj_preReg> items) {
        this.activity = activity;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return items.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View vi = view;
        if (view == null) {

            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            vi = inflater.inflate(R.layout.lista_form, null);

        }

        obj_preReg item = items.get(i);

        ImageView ivImage = vi.findViewById(R.id.ivIcon);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] imageBytes = baos.toByteArray();
        imageBytes = Base64.decode(item.getIcon(), Base64.DEFAULT);
        Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        //ivImage.setBackgroundColor(Color.parseColor(item.colorForm));

        RoundRectShape roundRectShape = new RoundRectShape(new float[]{
                30, 30, 30, 30,
                30, 30, 30, 30}, null, null);
        ShapeDrawable shapeDrawable = new ShapeDrawable(roundRectShape);
        shapeDrawable.getPaint().setColor(Color.parseColor(item.colorBackground));
        shapeDrawable.setPadding(20, 20, 20, 20);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            ivImage.setBackground(shapeDrawable);
        } else {
            ivImage.setBackgroundColor(Color.parseColor(item.colorBackground));
        }
        ivImage.setImageBitmap(decodedImage);

        LinearLayout llContainer = vi.findViewById(R.id.llContainer);

        RoundRectShape roundRectShape2 = new RoundRectShape(new float[]{
                0, 0, 30, 30,
                30, 30, 0, 0}, null, null);
        ShapeDrawable shapeDrawable2 = new ShapeDrawable(roundRectShape2);
        shapeDrawable2.getPaint().setColor(Color.parseColor("#eeeeee"));
        shapeDrawable2.setPadding(10, 10, 10, 10);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            llContainer.setBackground(shapeDrawable2);
        } else {
            llContainer.setBackgroundColor(Color.parseColor("#CFD8DC"));
        }

        TextView tvDescription = vi.findViewById(R.id.tvTitle);
        tvDescription.setText(item.getDescription().toUpperCase());

        return vi;
    }
}
