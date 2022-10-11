package diggate.xpertise.com.diggateapk;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import java.util.ArrayList;

public class adapter_spinner_search extends BaseAdapter {

    protected Activity activity;
    protected ArrayList<obj_params> items;

    public adapter_spinner_search(Activity activity, ArrayList<obj_params> items) {
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
            vi = inflater.inflate(R.layout.personalization3, null);

        }

        obj_params item = items.get(position);

        TextView tvText = vi.findViewById(R.id.tvText);

        if (item != null) {

            tvText.setText(item.getDescription());

        }

        return vi;
    }
}
