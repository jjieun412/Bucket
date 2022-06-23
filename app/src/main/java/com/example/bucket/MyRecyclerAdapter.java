package com.example.bucket;


import android.app.DatePickerDialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder> {

    Calendar calendar = Calendar.getInstance();
    int y = calendar.get(Calendar.YEAR);
    int m = calendar.get(Calendar.MONTH);
    int d = calendar.get(Calendar.DAY_OF_MONTH);

    private ArrayList<DatePlanItem> dateplan;
    private List<Map<String, Object>> details = new ArrayList<Map<String, Object>>();


    @NonNull
    @Override
    public MyRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.writebucket_datepage_add, parent, false);
        ViewHolder viewHolder = new ViewHolder(view, context);
        return viewHolder;
        //return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyRecyclerAdapter.ViewHolder holder, int position) {
        DatePlanItem contact = dateplan.get(position);


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

        holder.onBind(dateplan.get(position));

        Log.d("message", "detailPlans : " + dateplan);
        String jsonListD = new Gson().toJson(details);
        SharedPrefManager.setPreference(holder.context, "details", jsonListD);
    }


    public MyRecyclerAdapter(ArrayList<DatePlanItem> list) {
        dateplan = list;
    }

    @Override
    public int getItemCount() {
        return dateplan.size();
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

        void onBind(DatePlanItem item) {
            System.out.println("DatePlan: " + item);
            number.setText(String.valueOf(item.getSet_Num()));
            date.setText(item.getSet_Format_Date() == null ? "날짜 선택하기": item.getSet_Format_Date());
            content.setText(item.getContent());
        }

    }
}
