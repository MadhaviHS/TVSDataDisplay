package com.example.datadisplay;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserListActivity extends AppCompatActivity implements UseListRecyclerViewAdapter.OnClickListener {
    @BindView(R.id.recycler_view_users)
    RecyclerView recyclerView;
    @BindView(R.id.UserSearchText)
    AutoCompleteTextView userSearchEditText;
    List<EmployeeDTO> employeeDTOS;

    private UseListRecyclerViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        ButterKnife.bind(this);
        employeeDTOS = getIntent().getParcelableArrayListExtra("employeeList");
        if (employeeDTOS != null && employeeDTOS.size() > 0) {
            setUpRecyclerView(employeeDTOS);
            loadNameToSearch(employeeDTOS);
        }
    }

    private void loadNameToSearch(List<EmployeeDTO> employeeDTOS) {
        List<String> names = new ArrayList<>();
        for (int i = 0; i < employeeDTOS.size(); i++)
            names.add(employeeDTOS.get(i).getName());
        ArrayAdapter<String> nameArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, names);
        userSearchEditText.setAdapter(nameArrayAdapter);
       /* userSearchEditText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });*/
    }

    /* @OnItemClick(R.id.UserSearchText)
     public void onClick(int position)
     {
        String name= userSearchEditText.
     }*/
    @OnClick(R.id.searchButton)
    public void onSearchClick() {
        String name = userSearchEditText.getEditableText().toString();
        if (!TextUtils.isEmpty(name)) {
            String userName = name.trim();
            for (int i = 0; i < employeeDTOS.size(); i++) {
                if (employeeDTOS.get(i).getName().trim().equalsIgnoreCase(userName)) {
                    List<EmployeeDTO> searchEmployee = new ArrayList<>();
                    searchEmployee.add(employeeDTOS.get(i));
                    setUpRecyclerView(searchEmployee);
                    return;
                }
            }

        } else
            setUpRecyclerView(employeeDTOS);
    }

    private void setUpRecyclerView(List<EmployeeDTO> employeeDTOS) {
        mAdapter = new UseListRecyclerViewAdapter(UserListActivity.this, employeeDTOS);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(UserListActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnClickListener(UserListActivity.this);

    }

    @OnClick(R.id.buttonChart)
    public void onChartClick() {
        if (employeeDTOS != null && employeeDTOS.size() >= 10) {
            ArrayList<String> pieChartList = new ArrayList<>();
            ArrayList<String> nameList = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                nameList.add(employeeDTOS.get(i).getName());
                pieChartList.add(employeeDTOS.get(i).getSalary().replace("$", ""));
            }
            Intent intent = new Intent(UserListActivity.this, PieChartActivity.class);
            intent.putExtra("pieChartList", pieChartList);
            intent.putExtra("nameList", nameList);
            startActivity(intent);

        }

    }

    @Override
    public void onRightArrowClick(EmployeeDTO employeeDTO) {
        Intent intent = new Intent(UserListActivity.this, UserDetailsActivity.class);
        intent.putExtra("Employee", employeeDTO);
        startActivity(intent);
    }
}
