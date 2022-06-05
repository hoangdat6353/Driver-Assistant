package hoangdat.tdtu.driverassist;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class AddRemind extends AppCompatActivity {
    TextView tvDay,tvTime;
    EditText etNote;
    Spinner spnService,spnVehName;
    Button btnSaveRemind;
    String timeTonotify;

    List<String> vhcList = new ArrayList<String>();

    List<Vehicle> lstVehicle;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_remind);

        mapping();
        spinnerServiceShowed();
        getData();

        tvDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDay();
            }
        });
        tvTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectTime();
            }
        });
        btnSaveRemind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitRemind();
            }
        });
    }

    public void mapping()
    {
        tvDay = findViewById(R.id.tvDay_Remind);
        tvTime = findViewById(R.id.tvTime_Remind);
        etNote = findViewById(R.id.etNote_Remind);
        spnService = findViewById(R.id.spnService_Remind);
        spnVehName = findViewById(R.id.spnChooseVeh_Remind);
        btnSaveRemind = findViewById(R.id.btnSaveRemind);
    }

    private void spinnerServiceShowed()
    {
        ArrayAdapter<String> spnTypeAdapter = new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item, getResources().getStringArray(R.array.strSpinnerService));
        spnTypeAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spnService.setAdapter(spnTypeAdapter);
    }

    public void selectDay()
    {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                tvDay.setText(day + "-" + (month + 1) + "-" + year);
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    public void selectTime()
    {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                timeTonotify = i + ":" + i1;
                tvTime.setText(FormatTime(i, i1));
            }
        }, hour, minute, false);
        timePickerDialog.show();
    }

    public String FormatTime(int hour, int minute) {
        String time;
        time = "";
        String formattedMinute;

        if (minute / 10 == 0) {
            formattedMinute = "0" + minute;
        } else {
            formattedMinute = "" + minute;
        }
        if (hour == 0) {
            time = "12" + ":" + formattedMinute + " AM";
        } else if (hour < 12) {
            time = hour + ":" + formattedMinute + " AM";
        } else if (hour == 12) {
            time = "12" + ":" + formattedMinute + " PM";
        } else {
            int temp = hour - 12;
            time = temp + ":" + formattedMinute + " PM";
        }
        return time;
    }

    public void submitRemind()
    {
        String serviceSelected = spnService.getSelectedItem().toString();
        String remindNote = etNote.getText().toString();
        String vehNameSelected = spnVehName.getSelectedItem().toString();

        if (serviceSelected.isEmpty() || remindNote.isEmpty() || vehNameSelected.isEmpty() )
        {
            Toast.makeText(this,"Vui lòng nhập đủ các thông tin cần thiết !",Toast.LENGTH_LONG).show();
        } else if (tvDay.getText().toString().equals("Ngày") || (tvTime.getText().toString().equals("Thời gian")))
        {
            Toast.makeText(this,"Vui lòng chọn ngày và thời gian đặt lời nhắc !",Toast.LENGTH_LONG).show();
        } else {
                String userID = mAuth.getCurrentUser().getUid();
                myRef = database.getReference("Remind").child(userID);
                String remindKey = myRef.push().getKey();

                Reminder reminder = new Reminder(remindKey,userID,vehNameSelected,serviceSelected,remindNote,tvDay.getText().toString(),tvTime.getText().toString());

                myRef.child(remindKey).setValue(reminder).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(AddRemind.this,"Thông tin lời nhắc đã được cập nhật thành công lên hệ thống !",Toast.LENGTH_LONG).show();
                        setAlarm(remindNote,serviceSelected,vehNameSelected,tvDay.getText().toString(),tvTime.getText().toString(),remindKey);
                    }
                });
        }
    }

    public void getData()
    {
        lstVehicle = new ArrayList<>();
        myRef = database.getReference("Vehicle").child(Objects.requireNonNull(mAuth.getCurrentUser().getUid()));

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Vehicle vhc = snapshot.getValue(Vehicle.class);
                if (vhc != null) {
                    vhcList.add(vhc.getVehName());
                }
               showSpnVehName();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void showSpnVehName()
    {
        ArrayAdapter<String> spnVehNameAdapter = new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item, vhcList);
        spnVehNameAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spnVehName.setAdapter(spnVehNameAdapter);
    }

    private void setAlarm(String Note,String serviceName,String vehName, String date, String time,String remindKey) {
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(getApplicationContext(), AlarmBroadcast.class);
        intent.putExtra("note", Note);
        intent.putExtra("time", date);
        intent.putExtra("date", time);
        intent.putExtra("service",serviceName);
        intent.putExtra("vehName",vehName);
        intent.putExtra("key",remindKey);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
        String dateandtime = date + " " + timeTonotify;
        DateFormat formatter = new SimpleDateFormat("d-M-yyyy hh:mm");
        try {
            Date date1 = formatter.parse(dateandtime);
            am.set(AlarmManager.RTC_WAKEUP, date1.getTime(), pendingIntent);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        finish();

    }
}