package com.example.admin.projectcapstonemobile.fragment;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.projectcapstonemobile.R;
import com.example.admin.projectcapstonemobile.activity.FragmentActivity;
import com.example.admin.projectcapstonemobile.adapter.ConfirmLeaveAdapter;
import com.example.admin.projectcapstonemobile.model.LeaveRequest;
import com.example.admin.projectcapstonemobile.model.User;
import com.example.admin.projectcapstonemobile.remote.ApiUtils;
import com.example.admin.projectcapstonemobile.remote.LeaveService;
import com.example.admin.projectcapstonemobile.remote.UserService;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserInformationFragment extends Fragment implements com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener {
    private final String userInformationSharedPreferences = "informationSharedPreferences";
    private Button btn_take_leave;
    private Button btn_confirm_leave;
    private Button btn_show_list_leave;
    private EditText edt_take_leave_from;
    private EditText edt_take_leave_to;
    private EditText edt_take_leave_content;
    private Button btn_dialog_take_leave_confirm;
    private Button btn_dialog_take_leave_cancel;
    private Button btn_confirm_leave_accept;
    private Button btn_confirm_leave_decline;
    private Button btn_confirm_leave_cancel;
    private TextView textView_confirm_leave_displayName;
    private TextView textView_confirm_leave_fromDate;
    private TextView textView_confirm_leave_toDate;
    private TextView textView_confirm_leave_content;
    private Calendar myCalendar1, myCalendar2, calendarDate, calendarWorking;
    private String dateFromTakeLeave;
    private String dateToTakeLeave;
    private int from_year, from_month, from_day,to_year, to_month, to_day;
    private List<LeaveRequest> listRequest = new ArrayList<>();
    private ListView listView_confirm;
    private Button btn_view_leave_close;
    private String userToken;
    private String userRole;
    private int FLAG_PICKER = 0;
    private Integer userId;
    //service
    private LeaveService leaveService;
    private UserService userService;

    private List<String> listNotAllowed;
    private List<String> listStringWorking;
    private List<Calendar> listCalendar = new ArrayList<>();
    private List<Calendar> listCalendarWorking = new ArrayList<>();
    private Calendar[] listDisable;
    private Calendar[] listWorking;
    public UserInformationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_user_information, null);
        //service
        leaveService = ApiUtils.getLeaveService();
        userService = ApiUtils.getUserService();
        //userToken
        final SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(userInformationSharedPreferences, Context.MODE_PRIVATE);
        userToken = sharedPreferences.getString("userToken", "");
        userRole = sharedPreferences.getString("userRole", "");
        userId = getUserId(userToken);
        System.out.println("User id o user information fragment la`" + userId);

        //
        myCalendar1  = Calendar.getInstance();
        myCalendar2  = Calendar.getInstance();
        calendarDate = Calendar.getInstance();
        calendarWorking = Calendar.getInstance();

        btn_take_leave = (Button) rootView.findViewById(R.id.btn_take_leave);
        btn_confirm_leave = (Button) rootView.findViewById(R.id.btn_confirm_leave);
        btn_show_list_leave = (Button) rootView.findViewById(R.id.btn_show_list_leave);
        btn_dialog_take_leave_confirm = (Button) rootView.findViewById(R.id.btn_dialog_take_leave_confirm);
        btn_dialog_take_leave_cancel = (Button) rootView.findViewById(R.id.btn_dialog_comment_cancel);

        if(userRole.equals("ROLE_STAFF")){
            //btn_confirm_leave.setVisibility(View.INVISIBLE);
            btn_show_list_leave.setVisibility(View.INVISIBLE);
            btn_confirm_leave.setText("Xem danh sách xin nghỉ");
        }
        if(userRole.equals("ROLE_MANAGER")){
            btn_confirm_leave.setText("Duyệt đơn nghỉ phép");
            btn_show_list_leave.setText("Xem danh sách xin nghỉ");
        }
        //set year, month, day from calendar

        from_year = myCalendar1.get(Calendar.YEAR);
        from_month = myCalendar1.get(Calendar.MONTH);
        from_day = myCalendar1.get(Calendar.DAY_OF_MONTH);
        to_year = myCalendar2.get(Calendar.YEAR);
        to_month = myCalendar2.get(Calendar.MONTH);
        to_day = myCalendar2.get(Calendar.DAY_OF_MONTH);

        //dialog take leave
        final Dialog take_leave_dialog = new Dialog(getActivity());
        take_leave_dialog.setTitle("Xin nghỉ phép");
        take_leave_dialog.setContentView(R.layout.dialog_take_leave);

        edt_take_leave_from = (EditText) take_leave_dialog.findViewById(R.id.editText_take_leave_date_from);
        edt_take_leave_to = (EditText) take_leave_dialog.findViewById(R.id.editText_take_leave_date_to);
        edt_take_leave_content = (EditText) take_leave_dialog.findViewById(R.id.editText_take_leave_content);
        btn_dialog_take_leave_confirm = (Button) take_leave_dialog.findViewById(R.id.btn_dialog_take_leave_confirm);
        btn_dialog_take_leave_cancel = (Button) take_leave_dialog.findViewById(R.id.btn_dialog_take_leave_cancel);
        //dialog view leave
        final Dialog view_leave_dialog = new Dialog(getActivity());
        view_leave_dialog.setTitle("Xem danh sách xin nghỉ");
        view_leave_dialog.setContentView(R.layout.dialog_view_leave_request);

        btn_view_leave_close = (Button) view_leave_dialog.findViewById(R.id.btn_view_leave_request_close);
        listView_confirm = (ListView) view_leave_dialog.findViewById(R.id.listView_view_leave_request_request);

        //dialog confirm leave
        final Dialog confirm_leave_dialog = new Dialog(getActivity());
        confirm_leave_dialog.setTitle("Duyệt đơn xin nghỉ");
        confirm_leave_dialog.setContentView(R.layout.dialog_confirm_leave);

        btn_confirm_leave_accept = (Button) confirm_leave_dialog.findViewById(R.id.btn_confirm_leave_accept);
        btn_confirm_leave_cancel = (Button) confirm_leave_dialog.findViewById(R.id.btn_confirm_leave_cancel);
        btn_confirm_leave_decline = (Button) confirm_leave_dialog.findViewById(R.id.btn_confirm_leave_decline);
        textView_confirm_leave_displayName = (TextView) confirm_leave_dialog.findViewById(R.id.textView_confirm_leave_displayName);
        textView_confirm_leave_fromDate = (TextView) confirm_leave_dialog.findViewById(R.id.textView_confirm_leave_fromDate);
        textView_confirm_leave_toDate = (TextView) confirm_leave_dialog.findViewById(R.id.textView_confirm_leave_toDate);
        textView_confirm_leave_content = (TextView) confirm_leave_dialog.findViewById(R.id.textView_confirm_leave_content);


        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //on click button take leave
        btn_take_leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                take_leave_dialog.show();

                listNotAllowed = loadNotAllowedDate();
                listStringWorking = getWorkingDate();

                Calendar now = Calendar.getInstance();
                java.util.Date dateDisable = null;
                //
                if(listNotAllowed!=null){
                    for(int i=0; i<listNotAllowed.size(); i++){
                        try {
                            dateDisable = sdf.parse(listNotAllowed.get(i));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        calendarDate = dateToCalendar(dateDisable);
                        listCalendar.add(calendarDate);
                        listDisable = listCalendar.toArray(new Calendar[listCalendar.size()]);
                    }
                }


                //
                java.util.Date dateWorking = null;
                if(listStringWorking!=null){
                    for(int i=0; i<listStringWorking.size(); i++){
                        try{
                            dateWorking = sdf.parse(listStringWorking.get(i));
                        } catch(ParseException e){
                            e.printStackTrace();
                        }
                        calendarWorking = dateToCalendar(dateWorking);
                        listCalendarWorking.add(calendarWorking);
                        listWorking = listCalendarWorking.toArray(new Calendar[listCalendarWorking.size()]);
                    }
                }


                edt_take_leave_from.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FLAG_PICKER=0;
                        DatePickerDialog dialogFrom = DatePickerDialog.newInstance(
                                UserInformationFragment.this,
                                from_year,
                                from_month,
                                from_day);
                        dialogFrom.setMinDate(now);
                        if(listDisable!=null){
                            dialogFrom.setDisabledDays(listDisable);
                        }
                        if(listWorking!=null){
                            dialogFrom.setHighlightedDays(listWorking);
                        }
                        dialogFrom.show(getFragmentManager(), "DialogFrom");
                        dateFromTakeLeave= from_year + "/" + from_month + "/" + from_day;
                    }
                });
                edt_take_leave_to.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FLAG_PICKER = 1;
                        DatePickerDialog dialogTo = DatePickerDialog.newInstance(
                                UserInformationFragment.this,
                                to_year,
                                to_month,
                                to_day);
                        dialogTo.setMinDate(now);
                        if(listDisable!=null){
                            dialogTo.setDisabledDays(listDisable);
                        }
                        if(listWorking!=null){
                            dialogTo.setHighlightedDays(listWorking);
                        }
                        dialogTo.show(getFragmentManager(), "DialogTo");
                        dateToTakeLeave = to_year + "/" + to_month + "/" + to_day;
                    }
                });
                //btn confirm
                btn_dialog_take_leave_confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        User approver = getApprover(userToken);
                        Integer id = approver.getId();
                        User userApprove = new User(id);
                        String content = edt_take_leave_content.getText().toString();
                        System.out.println("DateFromTakeLeave " + dateFromTakeLeave);
                        System.out.println("DateToTakeLeave " + dateToTakeLeave);

                            LeaveRequest leaveRequest = new LeaveRequest(content, dateFromTakeLeave,dateToTakeLeave ,0 , userApprove);
                            System.out.println("Day la leave request" + leaveRequest.getApprover().getId());
                            Call<LeaveRequest> call = leaveService.createLeaveRequest(
                                    "Bearer " + userToken, leaveRequest );
                            call.enqueue(new Callback<LeaveRequest>() {
                                @Override
                                public void onResponse(Call<LeaveRequest> call, Response<LeaveRequest> response) {
                                    if(response.isSuccessful()){
                                        Toast.makeText(getActivity(), "Xin nghỉ phép thành công", Toast.LENGTH_SHORT).show();
                                        UserInformationFragment fragment = new UserInformationFragment();
                                        Intent intent = new Intent(getActivity(), FragmentActivity.class);
                                        intent.putExtra("CheckCode", "Checked");
                                        startActivity(intent);
                                    }
                                    else{
                                        Toast.makeText(getActivity(), "Ngày nghỉ không hợp lệ.", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<LeaveRequest> call, Throwable t) {
                                    Toast.makeText(getActivity(), "Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                                }
                            });
                            
                    }
                });


                //btn cancel
                btn_dialog_take_leave_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        take_leave_dialog.dismiss();
                    }
                });

            }
        });

        //on click button xin show list leave
        btn_confirm_leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userRole.equals("ROLE_MANAGER")) {
                    listRequest = viewLeaveRequestForApprover(userToken, "fromDate,desc", 0);
                    if (listRequest != null) {
                        listView_confirm.setAdapter(new ConfirmLeaveAdapter(listRequest, view_leave_dialog.getContext()));
                        for(int i=0; i<listRequest.size();i++){
                            System.out.println("Day la fromdate, desc");
                            System.out.println(listRequest.get(i).getFromDate());
                        }
                    }
                    view_leave_dialog.show();

                    listView_confirm.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Object object = listView_confirm.getItemAtPosition(position);
                            LeaveRequest leaveRequest = (LeaveRequest) object;
                            textView_confirm_leave_displayName.setText("Tên nhân viên \n" + leaveRequest.getUser().getDisplayName());
                            String fromDate = leaveRequest.getFromDate();
                            String[] fromDateArray = fromDate.split("-");
                            textView_confirm_leave_fromDate.setText("Nghỉ từ ngày " + fromDateArray[2] + "/" + fromDateArray[1] + "/" + fromDateArray[0]);
                            String toDate = leaveRequest.getToDate();
                            String[] toDateArray = toDate.split("-");
                            textView_confirm_leave_toDate.setText("Nghỉ đến ngày " + toDateArray[2] + "/" + toDateArray[1] + "/" + toDateArray[0]);
                            textView_confirm_leave_content.setText(leaveRequest.getContent());
                            confirm_leave_dialog.show();

                            btn_confirm_leave_decline.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    deleteLeaveRequest(userToken, leaveRequest.getId());
                                    Toast.makeText(getActivity(), "Xóa đơn xin nghỉ thành công", Toast.LENGTH_SHORT).show();
                                    confirm_leave_dialog.dismiss();
                                    listRequest = viewLeaveRequestForApprover(userToken, "fromDate,desc", 0);
                                    if (listRequest != null) {
                                        listView_confirm.setAdapter(new ConfirmLeaveAdapter(listRequest, view_leave_dialog.getContext()));
                                    }
                                    view_leave_dialog.show();
                                }
                            });

                            btn_confirm_leave_accept.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    leaveRequest.setStatus(1);
                                    updateLeaveRequest(userToken, leaveRequest.getId(), leaveRequest);
                                    Toast.makeText(getActivity(), "Đã duyệt đơn nghỉ phép", Toast.LENGTH_SHORT).show();
                                    confirm_leave_dialog.dismiss();
                                    listRequest = viewLeaveRequestForApprover(userToken, "fromDate,desc", 0);
                                    if (listRequest != null) {
                                        listView_confirm.setAdapter(new ConfirmLeaveAdapter(listRequest, view_leave_dialog.getContext()));
                                    }
                                    view_leave_dialog.show();
                                }
                            });

                            btn_confirm_leave_cancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    confirm_leave_dialog.dismiss();
                                }
                            });
                        }
                    });
                }
                if(userRole.equals("ROLE_STAFF")){
                    listRequest = viewLeaveRequestForUser(userToken, "fromDate,desc", 0);
                    if (listRequest != null) {
                        listView_confirm.setAdapter(new ConfirmLeaveAdapter(listRequest, view_leave_dialog.getContext()));
                    }
                    view_leave_dialog.show();
                    listView_confirm.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Object object = listView_confirm.getItemAtPosition(position);
                            LeaveRequest leaveRequest = (LeaveRequest) object;
                            textView_confirm_leave_displayName.setText("Tên nhân viên \n" + leaveRequest.getUser().getDisplayName());
                            String fromDate = leaveRequest.getFromDate();
                            String[] fromDateArray = fromDate.split("-");
                            textView_confirm_leave_fromDate.setText("Nghỉ từ ngày " + fromDateArray[2] + "/" + fromDateArray[1] + "/" + fromDateArray[0]);
                            String toDate = leaveRequest.getToDate();
                            String[] toDateArray = toDate.split("-");
                            textView_confirm_leave_toDate.setText("Nghỉ đến ngày " + toDateArray[2] + "/" + toDateArray[1] + "/" + toDateArray[0]);
                            textView_confirm_leave_content.setText(leaveRequest.getContent());
                            confirm_leave_dialog.show();

                            btn_confirm_leave_decline.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    deleteLeaveRequest(userToken, leaveRequest.getId());
                                    Toast.makeText(getActivity(), "Hủy đơn xin nghỉ thành công", Toast.LENGTH_SHORT).show();
                                    confirm_leave_dialog.dismiss();
                                    listRequest = viewLeaveRequestForUser(userToken, "fromDate,desc", 0);
                                    if (listRequest != null) {
                                        listView_confirm.setAdapter(new ConfirmLeaveAdapter(listRequest, view_leave_dialog.getContext()));
                                    }
                                    view_leave_dialog.show();
                                }
                            });

                            btn_confirm_leave_accept.setVisibility(View.INVISIBLE);

                            btn_confirm_leave_cancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    confirm_leave_dialog.dismiss();
                                }
                            });
                        }
                    });
                }

                btn_view_leave_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        view_leave_dialog.dismiss();
                    }
                });
            }
        });





        return rootView;
    }
    private User getApprover(String userToken){
        String roleName = "ROLE_MANAGER";
        List<User> approver = new ArrayList<>();
        Call<List<User>> call = userService.getApprover("Bearer " + userToken, roleName);
        try {
            approver = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return approver.get(0);
    }
    private List<String> loadNotAllowedDate(){
        List<String> listDate = new ArrayList<>();
        Call<List<String>> call = leaveService.getNotAllowedDate("Bearer " + userToken);

        try {
            listDate = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listDate;
    }
    @Override
    public void onDateSet(com.wdullaer.materialdatetimepicker.date.DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        if(FLAG_PICKER==0){
            edt_take_leave_from.setText("Nghỉ phép từ " + dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
            if(monthOfYear<9){
                if(dayOfMonth<10){
                    dateFromTakeLeave = year + "-0" + (monthOfYear+1) + "-0" + dayOfMonth;
                }
                else{
                    dateFromTakeLeave = year + "-0" + (monthOfYear+1) + "-" + dayOfMonth;
                }
            }
            else{
                if(dayOfMonth<10){
                    dateFromTakeLeave = year + "-" + (monthOfYear+1) + "-0" + dayOfMonth;
                }
                else{
                    dateFromTakeLeave = year + "-" + (monthOfYear+1) + "-" + dayOfMonth;
                }
            }
            System.out.println("Xin thong bao day la1 " + dateFromTakeLeave);
        }
        if(FLAG_PICKER==1){
            edt_take_leave_to.setText("Nghỉ phép đến " + dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
            if(monthOfYear<9){
                if(dayOfMonth<10){
                    dateToTakeLeave = year + "-0" + (monthOfYear+1) + "-0" + dayOfMonth;
                }
                else{
                    dateToTakeLeave = year + "-0" + (monthOfYear+1) + "-" + dayOfMonth;
                }
            }
            else{
                if(dayOfMonth<10){
                    dateToTakeLeave = year + "-" + (monthOfYear+1) + "-0" + dayOfMonth;
                }
                else{
                    dateToTakeLeave = year + "-" + (monthOfYear+1) + "-" + dayOfMonth;
                }
            }
            System.out.println("Xin thong bao day la2 " + dateToTakeLeave);
        }
    }
    private Calendar dateToCalendar(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }
    private List<String> getWorkingDate(){
        List<String> listDate = new ArrayList<>();
        Call<List<String>> call = leaveService.getWorkingDate("Bearer " + userToken);

        try {
            listDate = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listDate;
    }
    private Integer getUserId(String userToken){
        User user = new User();
        Call<User> call = userService.getUserInformation("Bearer " + userToken);
        try {
            user = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return user.getId();
    }
    private List<LeaveRequest> viewLeaveRequestForApprover(String userToken, String sort, Integer status){
        List<LeaveRequest> leaveRequests = new ArrayList<>();
        Call<List<LeaveRequest>> call = leaveService.findLeaveRequestByApprover(
                            "Bearer " + userToken, sort, status);

        try {
            leaveRequests = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return leaveRequests;
    }
    private void updateLeaveRequest(String userToken, Integer requestId,
                                            LeaveRequest leaveRequest){
        Call<LeaveRequest> call = leaveService.updateStatusRequest("Bearer " + userToken,
                                                                    requestId, leaveRequest);
        try {
            leaveRequest = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private List<LeaveRequest> viewLeaveRequestForUser(String userToken, String sort, Integer status){
        List<LeaveRequest> leaveRequests = new ArrayList<>();
        Call<List<LeaveRequest>> call = leaveService.findLeaveRequestByUser(
                                        "Bearer " + userToken, sort, status);
        try {
            leaveRequests = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return leaveRequests;
    }
    private void deleteLeaveRequest(String userToken, Integer requestId){
        Call<LeaveRequest> call = leaveService.deleteLeaveRequest("Bearer " + userToken, requestId);
        try {
            call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
