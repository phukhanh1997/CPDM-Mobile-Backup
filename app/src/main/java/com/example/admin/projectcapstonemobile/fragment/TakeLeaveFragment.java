package com.example.admin.projectcapstonemobile.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.projectcapstonemobile.R;
import com.example.admin.projectcapstonemobile.activity.FragmentActivity;
import com.example.admin.projectcapstonemobile.model.LeaveRequest;
import com.example.admin.projectcapstonemobile.model.Notification;
import com.example.admin.projectcapstonemobile.model.User;
import com.example.admin.projectcapstonemobile.model.YearSummary;
import com.example.admin.projectcapstonemobile.remote.ApiUtils;
import com.example.admin.projectcapstonemobile.remote.LeaveService;
import com.example.admin.projectcapstonemobile.remote.NotificationService;
import com.example.admin.projectcapstonemobile.remote.UserService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TakeLeaveFragment extends Fragment implements com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener {
    private LeaveService leaveService;
    private UserService userService;
    private NotificationService notificationService;
    private List<String> listNotAllowed;
    private List<String> listStringWorking;
    private List<Calendar> listCalendar = new ArrayList<>();
    private List<Calendar> listCalendarWorking = new ArrayList<>();
    private Calendar[] listDisable;
    private Calendar[] listWorking;
    private final String userInformationSharedPreferences = "informationSharedPreferences";
    private Button btn_dialog_take_leave_confirm;
    private Button btn_dialog_take_leave_cancel;
    private int from_year, from_month, from_day,to_year, to_month, to_day;
    private String userToken;
    private String userRole;
    private int FLAG_PICKER = 0;
    private Integer userId;
    private Calendar myCalendar1, myCalendar2, calendarDate, calendarWorking;
    private EditText edt_take_leave_from;
    private EditText edt_take_leave_to;
    private EditText edt_take_leave_content;
    private String dateFromTakeLeave;
    private String dateToTakeLeave;
    private YearSummary summary;
    private TextView totalDay;
    private TextView usedDay;
    private TextView availableDay;
    private User self;
    private DatabaseReference databaseReference;
    public TakeLeaveFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_take_leave, container, false);
        initialView();
        initialData();

        leaveService = ApiUtils.getLeaveService();
        userService = ApiUtils.getUserService();
        notificationService = ApiUtils.getNotificationService();
        final SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(userInformationSharedPreferences, Context.MODE_PRIVATE);
        userToken = sharedPreferences.getString("userToken", "");
        userRole = sharedPreferences.getString("userRole", "");
        userId = getUserId(userToken);

        self = getUserInformation(userToken);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        myCalendar1  = Calendar.getInstance();
        myCalendar2  = Calendar.getInstance();
        calendarDate = Calendar.getInstance();
        calendarWorking = Calendar.getInstance();

        edt_take_leave_from = (EditText) view.findViewById(R.id.editText_take_leave_date_from);
        edt_take_leave_to = (EditText) view.findViewById(R.id.editText_take_leave_date_to);
        edt_take_leave_content = (EditText) view.findViewById(R.id.editText_take_leave_content);
        btn_dialog_take_leave_confirm = view.findViewById(R.id.btn_dialog_take_leave_confirm);
        btn_dialog_take_leave_cancel = view.findViewById(R.id.btn_dialog_take_leave_cancel);

        totalDay = (TextView) view.findViewById(R.id.textView_take_leave_totalDay);
        usedDay = (TextView) view.findViewById(R.id.textView_take_leave_usedDay);
        availableDay = (TextView) view.findViewById(R.id.textView_take_leave_availableDay);

        from_year = myCalendar1.get(Calendar.YEAR);
        from_month = myCalendar1.get(Calendar.MONTH);
        from_day = myCalendar1.get(Calendar.DAY_OF_MONTH);
        to_year = myCalendar2.get(Calendar.YEAR);
        to_month = myCalendar2.get(Calendar.MONTH);
        to_day = myCalendar2.get(Calendar.DAY_OF_MONTH);

        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
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

        int year = now.get(Calendar.YEAR);
        summary = getSummary(year);
        usedDay.setText("Số ngày nghỉ đã sử dụng: " + summary.getDayOffApproved());
        availableDay.setText("Số ngày nghỉ còn lại: " + summary.getDayOffRemain());

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
                        TakeLeaveFragment.this,
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
                        TakeLeaveFragment.this,
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
                            TakeLeaveFragment fragment = new TakeLeaveFragment();
                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.fragment_container, fragment, "abc")
                                    .addToBackStack(null)
                                    .commit();
                            User user = new User(userId);
                            Notification notification = new Notification("Đơn xin nghỉ phép từ " + self.getDisplayName(), content, "/approverLeaveRequests", userApprove);
                            Call<Notification> callNotification = notificationService.sendNotification("Bearer " + userToken, notification);
                            callNotification.enqueue(new Callback<Notification>() {
                                @Override
                                public void onResponse(Call<Notification> call, Response<Notification> response) {
                                    if(response.isSuccessful()){
                                        Integer idRes = response.body().getId();
                                        databaseReference.child("users/").child(userApprove.getId().toString()).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                List<String> abc = (List<String>) dataSnapshot.getValue();
                                                String title = notification.getTitle();
                                                String detail = notification.getDetail();
                                                System.out.println("Day la title " + title);
                                                System.out.println("Day la detail " + detail);
                                                Notification noti = new Notification(title, detail, abc);
                                                noti.setUrl("/approverLeaveRequests");
                                                noti.setId(idRes);
                                                Call<Void> callPush = notificationService.pushNotification("Bearer " + userToken, noti);
                                                callPush.enqueue(new Callback<Void>() {
                                                    @Override
                                                    public void onResponse(Call<Void> call, Response<Void> response) {
                                                        if(response.isSuccessful()){
                                                            //Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                                                        }
                                                        if(!response.isSuccessful()){
                                                            //Toast.makeText(getActivity(), "Not success", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(Call<Void> call, Throwable t) {
                                                        //Toast.makeText(getActivity(), "Fail", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onFailure(Call<Notification> call, Throwable t) {

                                }
                            });
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

        btn_dialog_take_leave_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_take_leave_from.setText("Bắt đầu: ");
                edt_take_leave_to.setText("Kết thúc: ");
                edt_take_leave_content.setText("");
            }
        });


        return view;
    }

    private void initialView() {

    }


    private void initialData() {

    }

    private User getApprover(String userToken){
        String roleName = "ROLE_MANAGER";
        if(userRole.equals("ROLE_MANAGER")){
            roleName = "ROLE_ADMIN";
        }
        List<User> approver = new ArrayList<>();
        Call<List<User>> call = userService.getApprover("Bearer " + userToken, roleName);
        try {
            approver = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return approver.get(0);
    }

    private Calendar dateToCalendar(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
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

    private YearSummary getSummary(Integer year){
        YearSummary summary = new YearSummary();
        Call<YearSummary> call = leaveService.getSummary("Bearer " + userToken, year);

        try {
            summary = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return summary;
    }


    private User getUserInformation(String userToken){
        User newUser = new User();
        Call<User> call = userService.getUserInformation(userToken);
        try {
            newUser = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return newUser;
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        if(FLAG_PICKER==0){
            edt_take_leave_from.setText("Ngày bắt đầu: " + dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
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
            edt_take_leave_to.setText("Ngày kết thúc: " + dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
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
}
