package diggate.xpertise.com.diggateapk;

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
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;


public class adapter_paramsForm extends ArrayAdapter<obj_form> {

    protected ArrayList<obj_form> datos;

    public adapter_paramsForm(Context context, ArrayList<obj_form> datos) {
        super(context, 0, datos);

        this.datos = datos;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return intView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return intView(position, convertView, parent);
    }

    private View intView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.spinner_form, parent, false);
        }

        ImageView ivIcon = convertView.findViewById(R.id.ivIcon);
        TextView tvText = convertView.findViewById(R.id.tvText);

        obj_form datos = getItem(position);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] imageBytes = baos.toByteArray();
        imageBytes = Base64.decode(datos.getIdIconForm(), Base64.DEFAULT);
        Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        //ivImage.setBackgroundColor(Color.parseColor(item.colorForm));

        RoundRectShape roundRectShape = new RoundRectShape(new float[]{
                30, 30, 30, 30,
                30, 30, 30, 30}, null, null);
        ShapeDrawable shapeDrawable = new ShapeDrawable(roundRectShape);
        shapeDrawable.getPaint().setColor(Color.parseColor(datos.colorForm));
        shapeDrawable.setPadding(20, 20, 20, 20);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            ivIcon.setBackground(shapeDrawable);
        } else {
            ivIcon.setBackgroundColor(Color.parseColor(datos.colorForm));
        }
        ivIcon.setImageBitmap(decodedImage);

        if (datos != null) {

            tvText.setText(datos.getDescriptionForm());

        }

        return convertView;

    }

}