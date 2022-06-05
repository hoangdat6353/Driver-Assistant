package hoangdat.tdtu.driverassist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Notification_Pressed extends AppCompatActivity {
    TextView notiName, notiNote, notiDay;
    Button btnDeleteNoti;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_pressed);

        mapping();
        Bundle bundle = getIntent().getExtras();
        notiName.setText(bundle.getString("message"));
        notiNote.setText(bundle.getString("note"));
        notiDay.setText(bundle.getString("date"));
        String remindKey = bundle.getString("remindKey");

        btnDeleteNoti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteNotification(remindKey);
            }
        });
    }

    private void mapping()
    {
        notiName = findViewById(R.id.tvNotiName_Press);
        notiNote = findViewById(R.id.tvNotiNote_Press);
        notiDay = findViewById(R.id.tvNotiDay_Press);
        btnDeleteNoti = findViewById(R.id.btnDeleteNoti);
    }

    private void deleteNotification(String remindKey)
    {
        myRef = database.getReference("Remind");
        myRef.child(mAuth.getUid()).child(remindKey).removeValue();
        Toast.makeText(Notification_Pressed.this,"Lời nhắc đã được xóa thành công khỏi hệ thống !",Toast.LENGTH_LONG).show();
        Intent i = new Intent(Notification_Pressed.this, MainActivity.class);
        this.startActivity(i);
    }

}