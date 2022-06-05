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

public class ChangePartDetails extends AppCompatActivity {
    TextView tvAddress,tvPrice,tvDay,tvVehName,tvGear,tvQuantity,tvNote;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    PartObject partTemp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_part_details);

        mapping();
        getData();
    }

    public void mapping()
    {
        tvAddress = findViewById(R.id.tvChangePartAddress_Details);
        tvPrice = findViewById(R.id.tvChangePartPrice_Details);
        tvDay = findViewById(R.id.tvChangePartDay_Details);
        tvVehName = findViewById(R.id.tvVehicleName_ChangePartDetails);
        tvGear = findViewById(R.id.tvChangePartGear_Details);
        tvQuantity = findViewById(R.id.tvChangePartQuantity_Details);
        tvNote = findViewById(R.id.tvChangePartNote_Details);
    }

    public void getData()
    {
        Bundle partBundle = getIntent().getExtras();
        if (partBundle == null)
            return;
        PartObject ref = (PartObject) partBundle.get("change_part_object");
        partTemp = ref;

        tvAddress.setText(ref.getAddress());
        tvPrice.setText(new StringBuilder().append(String.valueOf(ref.getPrice())).append(" VNĐ ").toString());
        tvDay.setText(ref.getDate());
        tvVehName.setText(ref.getVehName());
        tvGear.setText(ref.getGear());
        tvQuantity.setText(new StringBuilder().append(String.valueOf(ref.getQuantily())).append("").toString());

        if (ref.getNote().equals(""))
        {
            tvNote.setText("Lần thay thế linh kiện này không có ghi chú");
        } else
        {
            tvNote.setText(ref.getNote());
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.remove_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        AlertDialog.Builder alert = new AlertDialog.Builder(ChangePartDetails.this);
        alert.setTitle("Xóa lịch sử thay thế linh kiện");
        alert.setIcon(R.drawable.ic_baseline_delete_24);
        alert.setMessage("Thao tác này sẽ xóa lịch sử của lần thay thế linh kiện này. Bạn chắc chứ ?");
        alert.setNegativeButton("Hủy", null);
        alert.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                myRef = database.getReference("Change Part History");
                myRef.child(mAuth.getCurrentUser().getUid()).child(partTemp.getChangeOilID()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(ChangePartDetails.this,"Xóa lịch sử thay thế linh kiện thành công !",Toast.LENGTH_LONG).show();
                        finish();
                    }
                });
            }
        });
        alert.show();
        return super.onOptionsItemSelected(item);
    }
}