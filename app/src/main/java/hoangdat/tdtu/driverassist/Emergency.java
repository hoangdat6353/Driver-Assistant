package hoangdat.tdtu.driverassist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class Emergency extends AppCompatActivity {
    EditText etEmergency;
    TextView tvDriverName;
    Button btnReset,btnSave;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);

        mapping();
        tvDriverName = findViewById(R.id.tvDriverName);
        tvDriverName.setText(mAuth.getCurrentUser().getDisplayName().toString());

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etEmergency.setText("");
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRef = database.getReference("Request");
                String requestKey = myRef.push().getKey();
                String userName = mAuth.getCurrentUser().getDisplayName().toString();
                Request req = new Request(requestKey,userName,etEmergency.getText().toString(),"",false,"");

                myRef.child(requestKey).setValue(req).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(Emergency.this,"Lời kêu gọi hỗ trợ đã được cập nhật thành công lên hệ thống ! Vui lòng chờ một tài xế nào khác phản hồi",Toast.LENGTH_LONG).show();
//                        Intent moveBack = new Intent(Emergency.this,EmergencyControl.class);
//                        Emergency.this.startActivity(moveBack);
                        finish();
                    }
                });
            }
        });
    }

    public void mapping()
    {
        etEmergency = findViewById(R.id.etEmergency);
        btnReset = findViewById(R.id.btnReset);
        btnSave = findViewById(R.id.btnSendEmergency);
    }
}