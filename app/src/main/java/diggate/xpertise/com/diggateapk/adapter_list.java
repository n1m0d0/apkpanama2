package diggate.xpertise.com.diggateapk;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class adapter_list extends BaseAdapter {
    protected Activity activity;
    protected ArrayList<obj_params2> items;

    public adapter_list(Activity activity, ArrayList<obj_params2> items) {
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
            vi = inflater.inflate(R.layout.personalization5, null);

        }

        obj_params2 item = items.get(position);

        TextView tvTitle = vi.findViewById(R.id.tvTitle);
        TextView tvText = vi.findViewById(R.id.tvText);
        TextView tvDescription = vi.findViewById(R.id.tvDescription);

        if (item != null) {

            tvTitle.setText(item.getTitle());
            tvText.setText(item.getText());
            tvDescription.setText(item.getDescription());

        }

        return vi;
    }
}
