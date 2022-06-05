package hoangdat.tdtu.driverassist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChangeOilDetails extends AppCompatActivity {
    TextView tvAddress,tvPrice,tvDay,tvVehName,tvCapacity,tvKM,tvNote;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    OilObject oilTemp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_oil_details);

        mapping();
        getData();
    }

    private void getData() {
        Bundle oilBundle = getIntent().getExtras();
        if (oilBundle == null)
            return;
        OilObject ref = (OilObject) oilBundle.get("change_oil_object");
        oilTemp = ref;

        tvAddress.setText(ref.getAddress());
        tvPrice.setText(new StringBuilder().append(String.valueOf(ref.getPrice())).append(" VNĐ ").toString());
        tvDay.setText(ref.getDate());
        tvVehName.setText(ref.getVehName());
        tvCapacity.setText(new StringBuilder().append(String.valueOf(ref.getCapacity())).append(" (cSt)").toString());
        tvKM.setText(new StringBuilder().append(String.valueOf(ref.getKM())).append(" (km) ").toString());

        if (ref.getNote().equals(""))
        {
            tvNote.setText("Lần thay dầu nhớt này không có ghi chú");
        } else
        {
            tvNote.setText(ref.getNote());
        }
    }

    private void mapping() {
        tvAddress = findViewById(R.id.tvChangeOilAddress_Details);
        tvPrice = findViewById(R.id.tvChangeOilPrice_Details);
        tvDay = findViewById(R.id.tvChangeOilDay_Details);
        tvVehName = findViewById(R.id.tvVehicleName_ChangeOilDetails);
        tvCapacity = findViewById(R.id.tvChangeOilCapacity_Details);
        tvKM = findViewById(R.id.tvChangeOilKm_Details);
        tvNote = findViewById(R.id.tvChangeOilNote_Details);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.remove_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        AlertDialog.Builder alert = new AlertDialog.Builder(ChangeOilDetails.this);
        alert.setTitle("Xóa lịch sử thay dầu nhớt");
        alert.setIcon(R.drawable.ic_baseline_delete_24);
        alert.setMessage("Thao tác này sẽ xóa lịch sử của lần thay dầu nhớt này. Bạn chắc chứ ?");
        alert.setNegativeButton("Hủy", null);
        alert.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                myRef = database.getReference("Change Oil History");
                myRef.child(mAuth.getCurrentUser().getUid()).child(oilTemp.getChangeOilID()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(ChangeOilDetails.this,"Xóa lịch sử thay dầu nhớt thành công !",Toast.LENGTH_LONG).show();
                        finish();
                    }
                });
            }
        });
        alert.show();
        return super.onOptionsItemSelected(item);
    }
}