package hoangdat.tdtu.driverassist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddVehicle extends AppCompatActivity {
    EditText edtName,edtBrand,edtPlate,edtNote,edtTankCapacity;
    Spinner spnType,spnBrand;
    Button btnAddVehicle;
    String vehName,vehBrand,vehType,vehNote,vehPlate;
    Integer vehTankCapacity;

    //Connect to database
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vehicle);

        mapping();
        spinnerTypeShowed();
        btnAddVehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getVehicleInformation();
                DatabaseReference myRef = database.getReference("Vehicle");
                String appKey = myRef.push().getKey();
                String userID = mAuth.getCurrentUser().getUid();

                Vehicle vhc = new Vehicle(appKey,vehName,vehBrand,vehType,vehPlate,vehNote,vehTankCapacity);
//                Toast.makeText(AddVehicle.this,vhc.toString(),Toast.LENGTH_LONG).show();
                myRef.child(userID).child(vhc.getVehID()).setValue(vhc).addOnSuccessListener(AddVehicle.this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(AddVehicle.this,"Thông tin xe đã được cập nhật thành công lên hệ thống !",Toast.LENGTH_LONG).show();
                        finish();
                    }
                }).addOnFailureListener(AddVehicle.this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddVehicle.this,"Thông tin xe chưa được cập nhật ! Đã xảy ra lỗi",Toast.LENGTH_LONG).show();
                    }
                });

            }
        });
        spnType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0)
                {
                   spinnerBrandShowed("Xe máy");
                } else if (position == 1)
                {
                    spinnerBrandShowed("Xe hơi");
                } else {
                    spinnerBrandShowed("Xe tải");
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
              spinnerBrandShowed("Xe máy");
            }
        });

    }

    private void mapping() {
        edtName = (EditText) findViewById(R.id.etNameVehicle);
        //edtBrand = (EditText) findViewById(R.id.etBrandVehicle);
        edtPlate = (EditText) findViewById(R.id.etPlateVehicle);
        edtNote = (EditText) findViewById(R.id.etNoteVehicle);
        spnType = (Spinner) findViewById(R.id.spnTypeVehicle);
        spnBrand = (Spinner) findViewById(R.id.spnBrandVehicle);
        edtTankCapacity = (EditText) findViewById(R.id.etTankCapacity);
        btnAddVehicle = (Button) findViewById(R.id.btnAddVehicle);
    }
    private void spinnerTypeShowed()
    {
        ArrayAdapter<String> spnTypeAdapter = new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item, getResources().getStringArray(R.array.strSpinnerType));
        spnTypeAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spnType.setAdapter(spnTypeAdapter);
    }

    private void getVehicleInformation() {
        vehName = edtName.getText().toString();
        vehBrand = spnBrand.getSelectedItem().toString();
        vehPlate = edtPlate.getText().toString();
        vehNote = edtNote.getText().toString();
        vehType = spnType.getSelectedItem().toString();
        String TankCapacity = edtTankCapacity.getText().toString();
        vehTankCapacity = Integer.parseInt(TankCapacity);
    }

    private void spinnerBrandShowed(String stringCase)
    {
        if (stringCase.equals("Xe máy"))
        {
            ArrayAdapter<String> spnTypeAdapter = new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item, getResources().getStringArray(R.array.brandBike));
            spnTypeAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
            spnBrand.setAdapter(spnTypeAdapter);
        }
        else if (stringCase.equals("Xe hơi"))
        {
            ArrayAdapter<String> spnTypeAdapter = new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item, getResources().getStringArray(R.array.brandCar));
            spnTypeAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
            spnBrand.setAdapter(spnTypeAdapter);
        } else {
            ArrayAdapter<String> spnTypeAdapter = new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item, getResources().getStringArray(R.array.brandTruck));
            spnTypeAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
            spnBrand.setAdapter(spnTypeAdapter);
        }
    }

}