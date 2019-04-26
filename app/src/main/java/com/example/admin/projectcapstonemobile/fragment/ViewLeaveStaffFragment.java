package com.example.admin.projectcapstonemobile.fragment;


import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import retrofit2.Call;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.example.admin.projectcapstonemobile.R;
import com.example.admin.projectcapstonemobile.adapter.LeaveStaffAdapter;
import com.example.admin.projectcapstonemobile.model.LeaveRequest;
import com.example.admin.projectcapstonemobile.model.User;
import com.example.admin.projectcapstonemobile.model.YearSummary;
import com.example.admin.projectcapstonemobile.remote.ApiUtils;
import com.example.admin.projectcapstonemobile.remote.LeaveService;
import com.example.admin.projectcapstonemobile.remote.UserService;
import com.google.android.gms.common.api.Api;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewLeaveStaffFragment extends Fragment{
    private final String userInformationSharedPreferences = "informationSharedPreferences";
    private View rootView;
    private LeaveService leaveService;
    private UserService userService;
    private String userToken;
    private String userRole;
    private List<User> listStaff;
    private ExpandableListView listViewStaff;
    private EditText edt_view_leave_from_year;
    private Button btn_view_leave_staff_view;
    private int searchYear;
    private YearSummary yearSummary;
    private int year = Calendar.getInstance().get(Calendar.YEAR);
    public ViewLeaveStaffFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView =  inflater.inflate(R.layout.fragment_view_leave_staff, container, false);
        leaveService = ApiUtils.getLeaveService();
        userService = ApiUtils.getUserService();
        final SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(userInformationSharedPreferences, Context.MODE_PRIVATE);
        userToken = sharedPreferences.getString("userToken", "");
        userRole = sharedPreferences.getString("userRole", "");

        listStaff = getAllStaff(userToken);
        btn_view_leave_staff_view = (Button) rootView.findViewById(R.id.btn_view_leave_staff_view);
        edt_view_leave_from_year = (EditText) rootView.findViewById(R.id.editText_view_leave_from_year);
        listViewStaff = (ExpandableListView) rootView.findViewById(R.id.expandList_leave_staff);

        edt_view_leave_from_year.setText("Năm: "+year);


        edt_view_leave_from_year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showYearDialog();
            }
        });

        btn_view_leave_staff_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> listHeader = new ArrayList<>();
                HashMap<String, List<LeaveRequest>> listDataChild = new HashMap<>();

                for(int i=0; i< listStaff.size(); i++){
                    String header = listStaff.get(i).getDisplayName() + "-" + listStaff.get(i).getDepartment().getName();
                    listHeader.add(header);
                    System.out.println("Day la header " + header + " id " + listStaff.get(i).getId());
                }

                for (User x:
                     listStaff) {
                    String header = x.getDisplayName() + "-" + x.getDepartment().getName();
                    List<LeaveRequest> leaveRequests = new ArrayList<>();
                    yearSummary = viewLeaveRequestForAdmin(userToken, x.getId(), year);
                    leaveRequests = yearSummary.getLeaveRequestSummaries();
                    if(leaveRequests!=null){
                        listDataChild.put(header, leaveRequests);
                    }
                }



                if(listStaff!=null){
                    listViewStaff.setAdapter(new LeaveStaffAdapter(listHeader, listDataChild, getActivity()));
                }
            }
        });

        return rootView;
    }

    public void showYearDialog()
    {

        final Dialog d = new Dialog(getActivity());
        d.setTitle("Chọn năm");
        d.setContentView(R.layout.year_dialog);
        Button set = (Button) d.findViewById(R.id.button1);
        Button cancel = (Button) d.findViewById(R.id.button2);
        final NumberPicker nopicker = (NumberPicker) d.findViewById(R.id.numberPicker1);

        nopicker.setMaxValue(year+50);
        nopicker.setMinValue(year-50);
        nopicker.setWrapSelectorWheel(false);
        nopicker.setValue(year);
        nopicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        set.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                edt_view_leave_from_year.setText(String.valueOf("Năm: " + nopicker.getValue()));
                year = nopicker.getValue();
                d.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
        d.show();

    }

    private List<User> getAllStaff(String userToken){
        List<User> listStaff = new ArrayList<>();
        Call<List<User>> call = userService.getAllStaff("Bearer " + userToken);
        try {
            listStaff = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listStaff;
    }
    private YearSummary viewLeaveRequestForAdmin(String userToken, Integer userId, Integer year){
        YearSummary summary = new YearSummary();
        Call<YearSummary> call =
                leaveService.findLeaveRequestOfUser("Bearer " + userToken, userId, year);
        try {
            summary = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return summary;
    }

}
