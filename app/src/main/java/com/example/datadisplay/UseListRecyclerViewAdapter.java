package com.example.datadisplay;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;

import java.util.List;

public class UseListRecyclerViewAdapter extends RecyclerView.Adapter<UseListRecyclerViewAdapter.MyViewHolder> {
    private List<EmployeeDTO> employeeDTOList;
    private Context mContext;
    private UseListRecyclerViewAdapter.OnClickListener mCallback;

    UseListRecyclerViewAdapter(Context context, List<EmployeeDTO> employeeDTOS) {
        mContext = context;
        employeeDTOList = employeeDTOS;
    }


    void setOnClickListener(UseListRecyclerViewAdapter.OnClickListener activity) {
        mCallback = activity;
    }


    @Override
    public UseListRecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_employee_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(UseListRecyclerViewAdapter.MyViewHolder holder, int position) {
        EmployeeDTO employeeDTO = employeeDTOList.get(position);
        String name = employeeDTO.getName();
        String[] names = name.trim().split(" ");
        StringBuilder namesLetter = new StringBuilder();

        for (int i = 0; i < names.length; i++) {
            if (i > 1) break;

            if (!TextUtils.isEmpty(names[i])) {
                namesLetter.append(names[i].charAt(0));
            }
        }

        TextDrawable drawable = TextDrawable.builder().beginConfig()
                .textColor(Color.WHITE)
                .bold()
                .toUpperCase()
                .endConfig()
                .buildRound(namesLetter.toString(), mContext.getResources().getColor(R.color.secondaryColorOrange1));
        holder.tvRoundName.setImageDrawable(drawable);
        holder.tvUserName.setText(name);
        holder.ivRightArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onRightArrowClick(employeeDTO);
            }
        });
    }

    @Override
    public int getItemCount() {
        return employeeDTOList.size();
    }

    public interface OnClickListener {
        void onRightArrowClick(EmployeeDTO employeeDTO);
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvUserName;
        ImageView ivRightArrow, tvRoundName;

        MyViewHolder(View v) {
            super(v);
            tvRoundName = v.findViewById(R.id.tv_rounded_name);
            tvUserName = v.findViewById(R.id.tvUserName);
            ivRightArrow = v.findViewById(R.id.ivRightArrow);
        }
    }
}
