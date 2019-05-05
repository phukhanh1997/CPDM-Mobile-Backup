package com.example.admin.projectcapstonemobile.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.projectcapstonemobile.R;
import com.example.admin.projectcapstonemobile.adapter.ConfirmLeaveAdapter;
import com.example.admin.projectcapstonemobile.model.LeaveRequest;
import com.example.admin.projectcapstonemobile.model.Notification;
import com.example.admin.projectcapstonemobile.model.User;
import com.example.admin.projectcapstonemobile.remote.ApiUtils;
import com.example.admin.projectcapstonemobile.remote.LeaveService;
import com.example.admin.projectcapstonemobile.remote.NotificationService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ViewLeaveRequestFragment extends Fragment {
    private final String userInformationSharedPreferences = "informationSharedPreferences";
    private LeaveService leaveService;
    private View rootView;
    private Button btn_confirm_leave_accept;
    private Button btn_confirm_leave_decline;
    private Button btn_confirm_leave_cancel;
    private TextView textView_confirm_leave_displayName;
    private TextView textView_confirm_leave_fromDate;
    private TextView textView_confirm_leave_toDate;
    private TextView textView_confirm_leave_content;
    private ListView listView_confirm;
    private String userToken;
    private String userRole;
    private List<LeaveRequest> listRequest = new ArrayList<>();
    private NotificationService notificationService;
    private DatabaseReference databaseReference;
    public ViewLeaveRequestFragment() {
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
        rootView =  inflater.inflate(R.layout.fragment_view_leave_request, container, false);
        listView_confirm = (ListView) rootView.findViewById(R.id.listView_view_leave_request_request);
        leaveService = ApiUtils.getLeaveService();
        notificationService = ApiUtils.getNotificationService();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        final SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(userInformationSharedPreferences, Context.MODE_PRIVATE);
        userToken = sharedPreferences.getString("userToken", "");
        userRole = sharedPreferences.getString("userRole", "");
        //dialog confirm leave
        final Dialog confirm_leave_dialog = new Dialog(getActivity());
        confirm_leave_dialog.setTitle("Quản lý đơn xin nghỉ");
        confirm_leave_dialog.setContentView(R.layout.dialog_confirm_leave);

        btn_confirm_leave_accept = (Button) confirm_leave_dialog.findViewById(R.id.btn_confirm_leave_accept);
        btn_confirm_leave_cancel = (Button) confirm_leave_dialog.findViewById(R.id.btn_confirm_leave_cancel);
        btn_confirm_leave_decline = (Button) confirm_leave_dialog.findViewById(R.id.btn_confirm_leave_decline);
        textView_confirm_leave_displayName = (TextView) confirm_leave_dialog.findViewById(R.id.textView_confirm_leave_displayName);
        textView_confirm_leave_fromDate = (TextView) confirm_leave_dialog.findViewById(R.id.textView_confirm_leave_fromDate);
        textView_confirm_leave_toDate = (TextView) confirm_leave_dialog.findViewById(R.id.textView_confirm_leave_toDate);
        textView_confirm_leave_content = (TextView) confirm_leave_dialog.findViewById(R.id.textView_confirm_leave_content);


        if(!userRole.equals("ROLE_ADMIN")){
            listRequest = viewLeaveRequestForUser(userToken, "fromDate,desc", 0);
            if (listRequest != null) {
                listView_confirm.setAdapter(new ConfirmLeaveAdapter(listRequest, getActivity()));
            }
            listView_confirm.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Object object = listView_confirm.getItemAtPosition(position);
                    LeaveRequest leaveRequest = (LeaveRequest) object;
                    textView_confirm_leave_displayName.setText(leaveRequest.getUser().getDisplayName());
                    String fromDate = leaveRequest.getFromDate();
                    String[] fromDateArray = fromDate.split("-");
                    textView_confirm_leave_fromDate.setText(fromDateArray[2] + "/" + fromDateArray[1] + "/" + fromDateArray[0]);
                    String toDate = leaveRequest.getToDate();
                    String[] toDateArray = toDate.split("-");
                    textView_confirm_leave_toDate.setText(toDateArray[2] + "/" + toDateArray[1] + "/" + toDateArray[0]);
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
                                listView_confirm.setAdapter(new ConfirmLeaveAdapter(listRequest,getActivity()));
                            }
                            ViewLeaveRequestFragment fragment = new ViewLeaveRequestFragment();
                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.fragment_container, fragment, "abc")
                                    .addToBackStack(null)
                                    .commit();
                            User x = leaveRequest.getUser();
                            Notification newNoti = new Notification("Đơn xin phép đã bị hủy ", leaveRequest.getContent(), "/approverLeaveRequests", x);
                            Call<Notification> callNoti = notificationService.sendNotification("Bearer " + userToken, newNoti);
                            callNoti.enqueue(new Callback<Notification>() {
                                @Override
                                public void onResponse(Call<Notification> call, Response<Notification> response) {
                                    if(response.isSuccessful()){
                                        Integer idRes = response.body().getId();
                                        databaseReference.child("users/").child(x.getId().toString()).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                List<String> abc = (List<String>) dataSnapshot.getValue();
                                                String title = newNoti.getTitle();
                                                String detail = newNoti.getDetail();
                                                System.out.println("Day la title " + title);
                                                System.out.println("Day la detail " + detail);
                                                Notification noti = new Notification(title, detail, abc);
                                                noti.setUrl("/userLeaveRequest");
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
                                                        //Toast.makeText(getActivity(), "Failure", Toast.LENGTH_SHORT).show();
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

        if(userRole.equals("ROLE_ADMIN")){
            listRequest = viewLeaveRequestForApprover(userToken, "fromDate, desc", 0);
            if(listRequest!=null){
                listView_confirm.setAdapter(new ConfirmLeaveAdapter(listRequest, getActivity()));
            }
            listView_confirm.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Object object = listView_confirm.getItemAtPosition(position);
                    LeaveRequest leaveRequest = (LeaveRequest) object;
                    textView_confirm_leave_displayName.setText(leaveRequest.getUser().getDisplayName());
                    String fromDate = leaveRequest.getFromDate();
                    String[] fromDateArray = fromDate.split("-");
                    textView_confirm_leave_fromDate.setText(fromDateArray[2] + "/" + fromDateArray[1] + "/" + fromDateArray[0]);
                    String toDate = leaveRequest.getToDate();
                    String[] toDateArray = toDate.split("-");
                    textView_confirm_leave_toDate.setText(toDateArray[2] + "/" + toDateArray[1] + "/" + toDateArray[0]);
                    textView_confirm_leave_content.setText(leaveRequest.getContent());
                    confirm_leave_dialog.show();

                    btn_confirm_leave_decline.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            deleteLeaveRequest(userToken, leaveRequest.getId());
                            Toast.makeText(getActivity(), "Hủy đơn xin nghỉ thành công", Toast.LENGTH_SHORT).show();
                            confirm_leave_dialog.dismiss();
                            listRequest = viewLeaveRequestForApprover(userToken, "fromDate,desc", 0);
                            if (listRequest != null) {
                                listView_confirm.setAdapter(new ConfirmLeaveAdapter(listRequest,getActivity()));
                            }
                            ViewLeaveRequestFragment fragment = new ViewLeaveRequestFragment();
                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.fragment_container, fragment, "abc")
                                    .addToBackStack(null)
                                    .commit();
                            User x = leaveRequest.getUser();
                            Notification newNoti = new Notification("Đơn xin phép đã bị hủy ", leaveRequest.getContent(), "/userLeaveRequests", x);
                            Call<Notification> callNoti = notificationService.sendNotification("Bearer " + userToken, newNoti);
                            callNoti.enqueue(new Callback<Notification>() {
                                @Override
                                public void onResponse(Call<Notification> call, Response<Notification> response) {
                                    if(response.isSuccessful()){
                                        Integer idRes = response.body().getId();
                                        databaseReference.child("users/").child(x.getId().toString()).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                List<String> abc = (List<String>) dataSnapshot.getValue();
                                                String title = newNoti.getTitle();
                                                String detail = newNoti.getDetail();
                                                System.out.println("Day la title " + title);
                                                System.out.println("Day la detail " + detail);
                                                Notification noti = new Notification(title, detail, abc);
                                                noti.setUrl("/userLeaveRequest");
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
                                                        //Toast.makeText(getActivity(), "Failure", Toast.LENGTH_SHORT).show();
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
                                listView_confirm.setAdapter(new ConfirmLeaveAdapter(listRequest, getActivity()));
                            }
                            ViewLeaveRequestFragment fragment = new ViewLeaveRequestFragment();
                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.fragment_container, fragment, "abc")
                                    .addToBackStack(null)
                                    .commit();
                            User x = leaveRequest.getUser();
                            Notification newNoti = new Notification("Đơn xin phép đã được duyệt ", leaveRequest.getContent(), "/userLeaveRequests", x);
                            Call<Notification> callNoti = notificationService.sendNotification("Bearer " + userToken, newNoti);
                            callNoti.enqueue(new Callback<Notification>() {
                                @Override
                                public void onResponse(Call<Notification> call, Response<Notification> response) {
                                    if(response.isSuccessful()){
                                        Integer idRes = response.body().getId();
                                        databaseReference.child("users/").child(x.getId().toString()).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                List<String> abc = (List<String>) dataSnapshot.getValue();
                                                String title = newNoti.getTitle();
                                                String detail = newNoti.getDetail();
                                                System.out.println("Day la title " + title);
                                                System.out.println("Day la detail " + detail);
                                                Notification noti = new Notification(title, detail, abc);
                                                noti.setUrl("/userLeaveRequests");
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
                                                        //Toast.makeText(getActivity(), "Failure", Toast.LENGTH_SHORT).show();
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

        return rootView;
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
}
