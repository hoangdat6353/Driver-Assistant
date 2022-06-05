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

import org.w3c.dom.Text;

import java.util.Calendar;

public class ChangeOil extends AppCompatActivity {
    TextView tvVehName,tvChangeDay;
    EditText etPrice,etCapacity,etKM,etAddress,etNote;
    Button btnChangeOil;
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
        setContentView(R.layout.activity_change_oil);

        mapping();
        Bundle vehChangeOilBundle = getIntent().getExtras();
        if (vehChangeOilBundle == null)
            return;
        vhc = (Vehicle) vehChangeOilBundle.get("vhc_change_oil");
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

        btnChangeOil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeOil();
            }
        });
    }

    private void changeOil() {
        if (etPrice.getText().toString().equals("") || etCapacity.getText().toString().equals("") || etKM.getText().toString().equals("") ||
                etAddress.getText().toString().equals("") || finalDate.equals(""))
        {
            Toast.makeText(ChangeOil.this,"Vui lòng nhập đầy đủ các thông tin cần thiết!",Toast.LENGTH_LONG).show();
        }else if (Integer.parseInt(etKM.getText().toString()) < vhc.getVehTankCapacity())
        {
            Toast.makeText(ChangeOil.this,"Số KM đã đi không thể bé hơn số KM thực hiện thay dầu!",Toast.LENGTH_LONG).show();
        } else {
            String oilPriceString = etPrice.getText().toString();
            String oilCapacityString = etCapacity.getText().toString();
            String oilKMString = etKM.getText().toString();

            Integer oilPrice = Integer.parseInt(oilPriceString);
            Integer oilCapacity = Integer.parseInt(oilCapacityString);
            Integer oilKM = Integer.parseInt(oilKMString);
            String oilAddress =etAddress.getText().toString();
            String oilNote = etNote.getText().toString();

            DatabaseReference vehRef = database.getReference("Vehicle");
            vehRef.child(mAuth.getCurrentUser().getUid()).child(vhc.getVehID()).child("vehTankCapacity").setValue(oilKM);
            //Send data to database
            DatabaseReference myRef = database.getReference("Change Oil History");
            String userID = mAuth.getCurrentUser().getUid();
            String oilKey = myRef.push().getKey();

            OilObject oilObject = new OilObject(oilKey,vhc.getVehID(), vhc.getVehName(),userID,oilPrice,oilCapacity,oilKM,oilAddress,oilNote,finalDate);
            myRef.child(userID).child(oilKey).setValue(oilObject).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Intent moveToOil = getIntent();
                    setResult(101);
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ChangeOil.this,"Cập nhật lịch sử thay dầu nhớt thất bại !",Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public void mapping()
    {
        tvVehName = findViewById(R.id.tvVehicleName_Oil);
        tvChangeDay = findViewById(R.id.tvCalendar_Oil);
        etPrice = findViewById(R.id.etPrice_Oil);
        etCapacity = findViewById(R.id.etCapacity_Oil);
        etKM = findViewById(R.id.etKm_Oil);
        etAddress = findViewById(R.id.etAddress_Oil);
        etNote = findViewById(R.id.etNote_Oil);
        btnChangeOil = findViewById(R.id.btnChangeOil);
    }

    private void selectDate() {
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
}