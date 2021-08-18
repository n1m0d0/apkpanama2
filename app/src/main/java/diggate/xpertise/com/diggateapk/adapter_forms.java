package diggate.xpertise.com.diggateapk;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Build;
import androidx.annotation.RequiresApi;
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

public class adapter_forms extends BaseAdapter {

    protected Activity activity;
    protected ArrayList<obj_form> items;

    public adapter_forms(Activity activity, ArrayList<obj_form> items) {
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

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View vi = convertView;

        /*if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            vi = inflater.inflate(R.layout.personalization2, null);

        }


        obj_form item = items.get(position);

        TextView tvDescription = vi.findViewById(R.id.tvDescription);
        tvDescription.setText(item.getDescriptionForm());

        ImageView ivImage = vi.findViewById(R.id.ivImage);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] imageBytes = baos.toByteArray();
        imageBytes = Base64.decode(item.getIdIconForm(), Base64.DEFAULT);
        Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        //ivImage.setBackgroundColor(Color.parseColor(item.colorForm));

        ShapeDrawable sd = new ShapeDrawable(new OvalShape());
        sd.setIntrinsicHeight(100);
        sd.setIntrinsicWidth(100);
        sd.getPaint().setColor(Color.parseColor(item.colorForm));
        ivImage.setBackground(sd);
        ivImage.setPadding(50, 50, 50, 50);
        ivImage.setImageBitmap(decodedImage);*/

        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            vi = inflater.inflate(R.layout.lista_form, null);

        }

        obj_form item = items.get(position);

        ImageView ivImage = vi.findViewById(R.id.ivIcon);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] imageBytes = baos.toByteArray();
        imageBytes = Base64.decode(item.getIdIconForm(), Base64.DEFAULT);
        Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        //ivImage.setBackgroundColor(Color.parseColor(item.colorForm));

        RoundRectShape roundRectShape = new RoundRectShape(new float[]{
                30, 30, 30, 30,
                30, 30, 30, 30}, null, null);
        ShapeDrawable shapeDrawable = new ShapeDrawable(roundRectShape);
        shapeDrawable.getPaint().setColor(Color.parseColor(item.colorForm));
        shapeDrawable.setPadding(20, 20, 20, 20);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            ivImage.setBackground(shapeDrawable);
        } else {
            ivImage.setBackgroundColor(Color.parseColor(item.colorForm));
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
        tvDescription.setText(item.getDescriptionForm().toUpperCase());

        return vi;

    }
}
