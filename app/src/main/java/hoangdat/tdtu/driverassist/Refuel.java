package hoangdat.tdtu.driverassist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Refuel extends AppCompatActivity {
    TextView tvVehName,tvRefuelDate;
    EditText etRefuelPrice,etRefuelCapacity,etRefuelKM,etRefuelAddress,etRefuelNote;
    Button btnRefuel;
    String finalDate;

    private int lastSelectedMonth;
    private int lastSelectedYear;
    private int lastSelectedDayOfMonth;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refuel);
        mapping();
        Bundle vehRefuelBundle = getIntent().getExtras();
        if (vehRefuelBundle == null)
            return;
        Vehicle vhc = (Vehicle) vehRefuelBundle.get("vhc_refuel");
        tvVehName.setText(vhc.getVehName());

        tvRefuelDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDate();
            }
        });

        final Calendar c = Calendar.getInstance();
        lastSelectedYear = c.get(Calendar.YEAR);
        lastSelectedMonth = c.get(Calendar.MONTH);
        lastSelectedDayOfMonth = c.get(Calendar.DAY_OF_MONTH);

        btnRefuel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etRefuelPrice.getText().toString().equals("") || etRefuelCapacity.getText().toString().equals("") || etRefuelKM.getText().toString().isEmpty() ||
                etRefuelAddress.getText().toString().equals("") || finalDate.equals(""))
                {
                    Toast.makeText(Refuel.this,"Vui lòng nhập đầy đủ các thông tin cần thiết!",Toast.LENGTH_LONG).show();
                } else if (Integer.parseInt(etRefuelKM.getText().toString()) < vhc.getVehTankCapacity())
                {
                    Toast.makeText(Refuel.this,"Số KM đã đi không thể bé hơn số KM thực hiện đổ xăng!",Toast.LENGTH_LONG).show();
                }
                else {
                    String refuelPriceString = etRefuelPrice.getText().toString();
                    String refuelCapacityString = etRefuelCapacity.getText().toString();
                    String refuelKMString = etRefuelKM.getText().toString();

                    Integer refuelPrice = Integer.parseInt(refuelPriceString);
                    Integer refuelCapacity = Integer.parseInt(refuelCapacityString);
                    Integer refuelKM = Integer.parseInt(refuelKMString);
                    String refuelAddress =etRefuelAddress.getText().toString();
                    String refuelNote = etRefuelNote.getText().toString();

                    DatabaseReference vehRef = database.getReference("Vehicle");
                    vehRef.child(mAuth.getCurrentUser().getUid()).child(vhc.getVehID()).child("vehTankCapacity").setValue(refuelKM);
                    //Send data to database
                    DatabaseReference myRef = database.getReference("Refuel History");
                    String userID = mAuth.getCurrentUser().getUid();
                    String refuelKey = myRef.push().getKey();

                    RefuelObject refuelObject = new RefuelObject(refuelKey,vhc.getVehID(), vhc.getVehName(),userID,refuelPrice,refuelCapacity,refuelKM,refuelAddress,refuelNote,finalDate);
                    myRef.child(userID).child(refuelKey).setValue(refuelObject).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            setResult(100);
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Refuel.this,"Cập nhật lịch sử đổ xăng thất bại !",Toast.LENGTH_LONG).show();
                            finish();
                        }
                    });
                }
            }
        });

    }

    private void selectDate() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                tvRefuelDate.setText(dayOfMonth + "-" + (month+1) + "-" + year);
                lastSelectedYear = year;
                lastSelectedMonth = month;
                lastSelectedDayOfMonth = dayOfMonth;
                finalDate = dayOfMonth + "/" + (month + 1) + "/" + year;
            }
        };
        DatePickerDialog datePickerDialog= new DatePickerDialog(this,dateSetListener,lastSelectedYear,lastSelectedMonth,lastSelectedDayOfMonth);
        datePickerDialog.show();
    }

    private void mapping() {
        tvVehName = findViewById(R.id.tvVehicleName_Refuel);
        etRefuelPrice = findViewById(R.id.etRefuelPrice);
        etRefuelCapacity = findViewById(R.id.etRefuelCapacity);
        etRefuelKM = findViewById(R.id.etRefuelKm);
        tvRefuelDate = findViewById(R.id.etRefuelCalendar);
        etRefuelAddress = findViewById(R.id.etRefuelAddress);
        etRefuelNote = findViewById(R.id.etRefuelNote);
        btnRefuel = findViewById(R.id.btnRefuelSave);
    }
}