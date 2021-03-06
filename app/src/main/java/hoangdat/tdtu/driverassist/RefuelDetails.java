package hoangdat.tdtu.driverassist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RefuelDetails extends AppCompatActivity {
    TextView tvAddress,tvPrice,tvDay,tvVehName,tvCapacity,tvKM,tvNote;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    RefuelObject refuelTemp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refuel_details);

        mapping();
        getData();
    }
    public void mapping()
    {
        tvAddress = findViewById(R.id.tvRefuelAddress_Details);
        tvPrice = findViewById(R.id.tvRefuelPrice_Details);
        tvDay = findViewById(R.id.tvRefuelDay_Details);
        tvVehName = findViewById(R.id.tvVehicleName_RefuelDetails);
        tvCapacity = findViewById(R.id.tvRefuelCapacity_Details);
        tvKM = findViewById(R.id.tvRefuelKm_Details);
        tvNote = findViewById(R.id.tvRefuelNote_Details);
    }
    public void getData()
    {
        Bundle refBundle = getIntent().getExtras();
        if (refBundle == null)
            return;

        RefuelObject ref = (RefuelObject) refBundle.get("refuel_object");
        refuelTemp = ref;

       tvAddress.setText(ref.getRefuelAddress());
       tvPrice.setText(String.valueOf(new StringBuilder().append(String.valueOf(ref.getRefuelPrice())).append(" VN?? ").toString()));
       tvDay.setText(ref.getRefuelDate());
       tvVehName.setText(ref.getVehName());
       tvCapacity.setText(new StringBuilder().append(String.valueOf(ref.getRefuelCapacity())).append(" (l??t)").toString());
       tvKM.setText(new StringBuilder().append(String.valueOf(ref.getRefuelKM())).append(" (km) ").toString());

       if (ref.getRefuelNote().equals(""))
       {
           tvNote.setText("L???n ????? x??ng n??y kh??ng c?? ghi ch??");
       } else
       {
           tvNote.setText(ref.getRefuelNote());
       }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.remove_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        AlertDialog.Builder alert = new AlertDialog.Builder(RefuelDetails.this);
        alert.setTitle("X??a l???ch s??? ????? x??ng");
        alert.setIcon(R.drawable.ic_baseline_delete_24);
        alert.setMessage("Thao t??c n??y s??? x??a l???ch s??? c???a l???n ????? x??ng n??y. B???n ch???c ch??? ?");
        alert.setNegativeButton("H???y", null);
        alert.setPositiveButton("X??a", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                myRef = database.getReference("Refuel History");
                myRef.child(mAuth.getCurrentUser().getUid()).child(refuelTemp.getRefuelID()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(RefuelDetails.this,"X??a l???ch s??? ????? x??ng th??nh c??ng !",Toast.LENGTH_LONG).show();
                        finish();
                    }
                });
            }
        });
        alert.show();
        return super.onOptionsItemSelected(item);
    }
}