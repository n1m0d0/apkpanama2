package gesport.xpertise.com.gesportapk;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import java.util.ArrayList;


public class adapter_params extends ArrayAdapter<obj_params>{


    public adapter_params(Context context, ArrayList<obj_params> datos) {
        super(context,0, datos);



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

    private View intView(int position, View convertView, ViewGroup parent){

        if(convertView == null) {

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.personalization3, parent, false);
        }


        TextView tvText = convertView.findViewById(R.id.tvText);

        obj_params datos = getItem(position);

        if(datos != null) {

            tvText.setText(datos.getDescription());

        }

        return  convertView;

    }

}