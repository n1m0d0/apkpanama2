package diggate.xpertise.com.diggateapk;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class adapter_spinner_search2 extends BaseAdapter {

    protected Activity activity;
    protected ArrayList<obj_params> items;

    public adapter_spinner_search2(Activity activity, ArrayList<obj_params> items) {
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
            vi = inflater.inflate(R.layout.personalization4, null);

        }

        obj_params item = items.get(position);

        TextView tvText = vi.findViewById(R.id.tvText);
        ImageView ivCheck = vi.findViewById(R.id.ivCheck);

        if (item != null) {
            tvText.setText(item.getDescription());
            if (item.isActive())
            {
                ivCheck.setVisibility(View.VISIBLE);
            } else {
                ivCheck.setVisibility(View.INVISIBLE);
            }
        }

        return vi;
    }
}
