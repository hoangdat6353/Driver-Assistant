package hoangdat.tdtu.driverassist;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class VehicleHistory extends AppCompatActivity {
    TextView vhcName,vhcType,vhcFuel,vhcOil,vhcPart,historyFuel,historyOil,historyPart;
    ImageView vhcImage;
    Vehicle vhc;
    String vehicleName, vehicleType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_history);

        mapping();
        Bundle vehHistoryBundle = getIntent().getExtras();
        if (vehHistoryBundle == null)
            return;
        vhc = (Vehicle) vehHistoryBundle.get("vehicle_object_history");
        vehicleName = vhc.getVehName();
        vehicleType = vhc.getVehType();

        vhcName.setText(vehicleName);
        vhcType.setText(vehicleType);
        if (vhc.getVehType().equals("Xe hơi"))
            vhcImage.setImageResource(R.drawable.vehicle_item);
        if (vhc.getVehType().equals("Xe máy"))
            vhcImage.setImageResource(R.drawable.motorcycle_item);
        if (vhc.getVehType().equals("Xe tải"))
            vhcImage.setImageResource(R.drawable.truck_item);

        vhcFuel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent moveToRefuel = new Intent(VehicleHistory.this,Refuel.class);
                Bundle bundleRefuel = new Bundle();
                bundleRefuel.putSerializable("vhc_refuel",vhc);
                moveToRefuel.putExtras(bundleRefuel);
                VehicleHistory.this.startActivityForResult(moveToRefuel,100);
            }
        });
        vhcOil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent moveToOil = new Intent(VehicleHistory.this,ChangeOil.class);
                Bundle bundleOil = new Bundle();
                bundleOil.putSerializable("vhc_change_oil",vhc);
                moveToOil.putExtras(bundleOil);
                VehicleHistory.this.startActivityForResult(moveToOil,101);

            }
        });
        vhcPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent moveToPart = new Intent(VehicleHistory.this,ChangePart.class);
                Bundle bundlePart = new Bundle();
                bundlePart.putSerializable("vhc_change_part",vhc);
                moveToPart.putExtras(bundlePart);
                VehicleHistory.this.startActivityForResult(moveToPart,102);
            }
        });
        historyFuel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent moveToHistoryRefuel = new Intent(VehicleHistory.this,HistoryRefuel.class);
                VehicleHistory.this.startActivity(moveToHistoryRefuel);
            }
        });
        historyOil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent moveToHistoryOil = new Intent(VehicleHistory.this,HistoryChangeOil.class);
                VehicleHistory.this.startActivity(moveToHistoryOil);
            }
        });
        historyPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent moveToHistoryPart = new Intent(VehicleHistory.this,HistoryChangePart.class);
                VehicleHistory.this.startActivity(moveToHistoryPart);
            }
        });

    }
    public void mapping()
    {
        vhcName = findViewById(R.id.textViewVehicleName_History);
        vhcType = findViewById(R.id.textViewVehicleType_History);
        vhcImage = findViewById(R.id.imageViewVehicle_History);
        vhcFuel = findViewById(R.id.imvRefuel_History);
        vhcOil = findViewById(R.id.imvChangeOil_History);
        vhcPart = findViewById(R.id.imvChangePart_History);
        historyFuel = findViewById(R.id.imvHistoryRefuel_History);
        historyOil = findViewById(R.id.imvHistoryChangeOil_History);
        historyPart = findViewById(R.id.imvHistoryChangePart_History);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == 100)
        {
            Toast.makeText(VehicleHistory.this,"Lịch sử đổ xăng đã được cập nhật thành công lên hệ thống !",Toast.LENGTH_LONG).show();
        }
        if (requestCode == 101 && resultCode == 101)
        {
            Toast.makeText(VehicleHistory.this,"Lịch sử thay dầu nhớt đã được cập nhật thành công lên hệ thống !",Toast.LENGTH_LONG).show();
        }
        if (requestCode == 102 && resultCode == 102)
        {
            Toast.makeText(VehicleHistory.this,"Lịch sử thay thế linh kiện đã được cập nhật thành công lên hệ thống !",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("VehicleName",vhc.getVehName());
        outState.putString("VehicleType",vhc.getVehType());
        if (vhc.getVehType().equals("Xe hơi"))
            outState.putInt("VehicleImage",R.drawable.vehicle_item);
        if (vhc.getVehType().equals("Xe máy"))
            outState.putInt("VehicleImage",R.drawable.motorcycle_item);
        if (vhc.getVehType().equals("Xe tải"))
            outState.putInt("VehicleImage",R.drawable.truck_item);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        vehicleName = savedInstanceState.getString("VehicleName");
        vehicleType = savedInstanceState.getString("VehicleType");
        vhcImage.setImageResource(savedInstanceState.getInt("VehicleImage"));
    }
}