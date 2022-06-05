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

import java.util.Calendar;

public class ChangePart extends AppCompatActivity {
    TextView tvVehName,tvChangeDay;
    EditText etPrice,etGear,etQuantily,etAddress,etNote;
    Button btnChangePart;
    String finalDate;
    private int lastSelectedMonth;
    private int lastSelectedYear;
    private int lastSelectedDayOfMonth;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();;
    Vehicle vhc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_part);

        mapping();
        Bundle vehChangeOilBundle = getIntent().getExtras();
        if (vehChangeOilBundle == null)
            return;
        vhc = (Vehicle) vehChangeOilBundle.get("vhc_change_part");
        tvVehName.setText(vhc.getVehName());

        tvChangeDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDate();
            }
        });
        final Calendar c = Calendar.getInstance();
        lastSelectedYear = c.get(Calendar.YEAR);
        lastSelectedMonth = c.get(Calendar.MONTH);
        lastSelectedDayOfMonth = c.get(Calendar.DAY_OF_MONTH);

        btnChangePart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePart();
            }
        });

    }

    public void mapping() {
        tvVehName = findViewById(R.id.tvVehicleName_Part);
        tvChangeDay = findViewById(R.id.tvCalendar_Part);
        etPrice = findViewById(R.id.etPrice_Part);
        etGear = findViewById(R.id.etGear_Part);
        etQuantily = findViewById(R.id.etQuantily_Part);
        etAddress = findViewById(R.id.etAddress_Part);
        etNote = findViewById(R.id.etNote_Part);
        btnChangePart = findViewById(R.id.btnChangePart);
    }
    public void selectDate()
    {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                tvChangeDay.setText(dayOfMonth + "-" + (month+1) + "-" + year);
                lastSelectedYear = year;
                lastSelectedMonth = month;
                lastSelectedDayOfMonth = dayOfMonth;
                finalDate = dayOfMonth + "/" + (month + 1) + "/" + year;
            }
        };
        DatePickerDialog datePickerDialog= new DatePickerDialog(this,dateSetListener,lastSelectedYear,lastSelectedMonth,lastSelectedDayOfMonth);
        datePickerDialog.show();
    }

    public void changePart()
    {
        if (etPrice.getText().toString().equals("") || etGear.getText().toString().equals("") || etQuantily.getText().toString().equals("") ||
                etAddress.getText().toString().equals("") || finalDate.equals(""))
        {
            Toast.makeText(ChangePart.this,"Vui lòng nhập đầy đủ các thông tin cần thiết!",Toast.LENGTH_LONG).show();
        } else {
            String partPriceString = etPrice.getText().toString();
            String partQuantilyString = etQuantily.getText().toString();

            String partGear = etGear.getText().toString();
            Integer partPrice = Integer.parseInt(partPriceString);
            Integer partQuantily = Integer.parseInt(partQuantilyString);
            String partAddress =etAddress.getText().toString();
            String partNote = etNote.getText().toString();

            //Send data to database
            DatabaseReference myRef = database.getReference("Change Part History");
            String userID = mAuth.getCurrentUser().getUid();
            String partKey = myRef.push().getKey();

            PartObject partObject = new PartObject(partKey,vhc.getVehID(), vhc.getVehName(),userID,partPrice,partGear,partQuantily,partAddress,partNote,finalDate);
            myRef.child(userID).child(partKey).setValue(partObject).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Intent moveToPart = getIntent();
                    setResult(102);
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ChangePart.this,"Cập nhật lịch sử thay dầu nhớt thất bại !",Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}