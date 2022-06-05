package hoangdat.tdtu.driverassist;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class VehicleDetails extends AppCompatActivity {
    ImageView imvVehicleEdit;
    EditText etVehNameEdit,etVehBrandEdit,etVehPlateEdit,etVehNoteEdit,tvVehCapacity;
    TextView tvVehName;
    Button btnUpdate, btnDelete;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_details);

        mapping();
        getData();
    }

    private void getData() {
        Bundle vehBundle = getIntent().getExtras();
        if (vehBundle == null)
            return;

        Vehicle vhc = (Vehicle) vehBundle.get("vehicle_object");

        if (vhc.getVehType().equals("Xe hơi"))
            imvVehicleEdit.setImageResource(R.drawable.vehicle_item);
        if (vhc.getVehType().equals("Xe máy"))
            imvVehicleEdit.setImageResource(R.drawable.motorcycle_item);
        if (vhc.getVehType().equals("Xe tải"))
            imvVehicleEdit.setImageResource(R.drawable.truck_item);

        etVehNameEdit.setText(vhc.getVehName());
        etVehBrandEdit.setText(vhc.getVehBrand());
        etVehPlateEdit.setText(vhc.getVehPlate());
        etVehNoteEdit.setText(vhc.getVehNote());
        tvVehName.setText(vhc.getVehName());
        tvVehCapacity.setText(vhc.getVehTankCapacity().toString());

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteVehicle(vhc);
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateVehicle(vhc);
            }
        });
    }
    private void deleteVehicle(Vehicle vhc){
        new AlertDialog.Builder(this)
                .setTitle("Xóa phương tiện")
                .setMessage("Thao tác này sẽ xóa phương tiện được chọn khỏi hệ thống")
                .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseReference myRef = database.getReference("Vehicle");
                        myRef.child(mAuth.getCurrentUser().getUid().toString()).child(vhc.getVehID()).removeValue();
                        Toast.makeText(VehicleDetails.this,"Phương tiện đã được xóa thành công khỏi hệ thống !",Toast.LENGTH_LONG).show();
                        finish();
                    }
                })
                .setNegativeButton("Hủy",null)
                .create()
                .show();
    }
    private void updateVehicle(Vehicle vhc)
    {
        vhc.setVehName(etVehNameEdit.getText().toString());
        vhc.setVehBrand(etVehBrandEdit.getText().toString());
        vhc.setVehPlate(etVehPlateEdit.getText().toString());
        vhc.setVehNote(etVehNoteEdit.getText().toString());

        new AlertDialog.Builder(this)
                .setTitle("Cập nhật phương tiện")
                .setMessage("Thao tác này sẽ cập nhật thông tin của phương tiện được chọn")
                .setPositiveButton("Cập nhật", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseReference myRef = database.getReference("Vehicle");
                        myRef.child(mAuth.getCurrentUser().getUid().toString()).child(vhc.getVehID()).setValue(vhc);
                        Toast.makeText(VehicleDetails.this,"Thông tin của phương tiện đã được cập nhật thành công !",Toast.LENGTH_LONG).show();
                        finish();
                    }
                })
                .setNegativeButton("Hủy",null)
                .create()
                .show();
    }

    private void mapping()
    {
        imvVehicleEdit = findViewById(R.id.vehImage_Edit);
        etVehNameEdit = findViewById(R.id.etNameVehicle_Edit);
        etVehBrandEdit = findViewById(R.id.etBrandVehicle_Edit);

        etVehPlateEdit = findViewById(R.id.etPlateVehicle_Edit);
        etVehNoteEdit = findViewById(R.id.etNoteVehicle_Edit);
        tvVehName = findViewById(R.id.vehName_Edit_Header);
        tvVehCapacity = findViewById(R.id.vehCapacity_Edit_Final);
        btnUpdate = findViewById(R.id.btnUpdateVehicle);
        btnDelete = findViewById(R.id.btnDeleteVehicle);
    }
}