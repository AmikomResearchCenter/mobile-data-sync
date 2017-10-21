package adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.ariflaksito.amsossync.R;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import models.Posting;

/**
 * Created by ariflaksito on 10/20/17.
 */

public class AdapterData extends BaseAdapter {

    ArrayList<Posting> listPosting;
    int id;

    public AdapterData(int uid, List<Posting> p){
        this.id = uid;
        listPosting = new ArrayList<Posting>();
        for(Posting post : p){
            listPosting.add(post);
        }
    }

    @Override
    public int getCount() {
        return listPosting.size();
    }

    @Override
    public Object getItem(int i) {
        return listPosting.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            view = inflater.inflate(R.layout.row_data, viewGroup, false);
        }

        Posting posting = listPosting.get(i);
        final int uid = posting.getUid();

        TextView txt1 = (TextView)view.findViewById(R.id.txt_data1);
        txt1.setText(posting.getName());

        TextView txt2 = (TextView)view.findViewById(R.id.txt_data2);
        Date date = new Date();
        date.setTime(posting.getPostdate().getTime());
        txt2.setText(new SimpleDateFormat("dd MMM - HH:mm").format(date));

        TextView txt3 = (TextView)view.findViewById(R.id.txt_data3);
        txt3.setText(posting.getMsg());

        TextView txt4 = (TextView)view.findViewById(R.id.txt_data4);
        if(uid==id){
            txt4.setVisibility(View.VISIBLE);
        }else{
            txt4.setVisibility(View.GONE);
        }

//        LinearLayout button = (LinearLayout) view.findViewById(R.id.ic_data);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                android.util.Log.d("--Amsos Sync--", uid+"");
//
//            }
//        });

        return view;
    }
}
