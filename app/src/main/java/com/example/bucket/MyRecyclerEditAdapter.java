package com.example.bucket;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;



public class MyRecyclerEditAdapter extends RecyclerView.Adapter<MyRecyclerEditAdapter.ViewHolder> {

    Calendar calendar = Calendar.getInstance();
    int y = calendar.get(Calendar.YEAR);
    int m = calendar.get(Calendar.MONTH);
    int d = calendar.get(Calendar.DAY_OF_MONTH);

    private ArrayList<DetailEditItem> detailItem;
    private List<Map<String, Object>> details = new ArrayList<Map<String, Object>>();


    @NonNull
    @Override
    public MyRecyclerEditAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.writebucket_datepage_add, parent, false);
        MyRecyclerEditAdapter.ViewHolder viewHolder = new MyRecyclerEditAdapter.ViewHolder(view, context);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyRecyclerEditAdapter.ViewHolder holder, int position) {

        /*
        DetailEditItem contact = detailItem.get(position);



        TextView textView = holder.number;
        textView.setText(String.valueOf(contact.getSet_Num()));


        Button btn = holder.date;
        btn.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(holder.context, (datePicker, year, month, dayOfMonth) -> {
                btn.setText(String.format("%d년 %d월 %d일", year, month + 1, dayOfMonth));  // 설정 날짜 보여주기
                contact.setSet_Date(year + "-" + (month+1) + "-" + dayOfMonth);

                System.out.println("contact modified date: " + contact.getSet_Date());
            }, y, m, d);
            datePickerDialog.show();
        });
        btn.setEnabled(true);



        EditText editText = holder.content;
        editText.setSelection(editText.getText().length());
        editText.addTextChangedListener(new TextWatcher() {
            private String lastValue = "";
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String newValue = editText.getText().toString();
                if(!lastValue.equals(newValue)) {
                    lastValue = newValue;
                    editText.setText(newValue);
                    contact.setContent(newValue);
                }
            }
        });
        editText.setText(contact.getContent());


        Map<String, Object> map = new HashMap<String, Object>();
        map.put("orderNumb", String.valueOf(contact.getSet_Num()));
        map.put("content", String.valueOf(contact.getContent()));
        map.put("planDate", String.valueOf(contact.getSet_Date()));
        details.add(map);

        holder.onBind(detailItem.get(position));

        Log.d("message", "detailPlans : " + detailItem);
        String jsonListD = new Gson().toJson(details);
        SharedPrefManager.setPreference(holder.context, "details", jsonListD);
         */
    }


    public MyRecyclerEditAdapter(ArrayList<DetailEditItem> list) {
        detailItem = list;
    }

    @Override
    public int getItemCount() {
        return detailItem.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        TextView number;
        Button date;
        EditText content;
        Context context;

        public ViewHolder(@NonNull View itemView , Context context) {
            super(itemView);
            this.context = context;

            number = (TextView) itemView.findViewById(R.id.set_number);
            date = (Button) itemView.findViewById(R.id.pick_date);
            content = (EditText) itemView.findViewById(R.id.write_content);
        }

        void onBind(DetailEditItem item) {
            System.out.println("DetailEditItem: " + item);
            number.setText(String.valueOf(item.getSet_Num()));
            date.setText(item.getSet_Format_Date() == null ? "날짜 선택하기": item.getSet_Format_Date());
            content.setText(item.getContent());
        }

    }
}

