package hoangdat.tdtu.driverassist;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Answer extends AppCompatActivity {
    TextView tvAskQuestion,tvAskName;
    EditText etAnswer;
    Button btnResetAnswer, btnSendAnswer;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();;
    DatabaseReference myRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);
        mapping();

        Bundle reqBundle = getIntent().getExtras();
        if (reqBundle == null)
            return;

        Request req = (Request) reqBundle.get("request_object");
        tvAskQuestion.setText(req.getRequestAsk());

        tvAskName.setText(req.getRequestOwner());
        btnResetAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etAnswer.setText("");
            }
        });
        btnSendAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendAnswer(req);
            }
        });
    }

    public void mapping()
    {
        tvAskQuestion = findViewById(R.id.tvRequestAsk_Answer);
        tvAskName = findViewById(R.id.tvAskName_Answer);
        etAnswer = findViewById(R.id.etEmergency_Answer);
        btnResetAnswer = findViewById(R.id.btnResetAnswer);
        btnSendAnswer = findViewById(R.id.btnSendAnswer);
    }

    public void sendAnswer(Request req)
    {
        new AlertDialog.Builder(this)
                .setTitle("H??? tr??? t??i x??? kh??c")
                .setMessage("Thao t??c n??y s??? g???i ph???n h???i t???i t??i x??? y??u c???u tr??? gi??p. B???n ch???c ch???n v???i c??u tr??? l???i c???a m??nh ch????")
                .setPositiveButton("C??", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        myRef = database.getReference("Request");
                        String requestId = req.getRequestId().toString();

                        Request reqUpdate = new Request(requestId,req.getRequestOwner(),req.getRequestAsk(),etAnswer.getText().toString(),true,mAuth.getCurrentUser().getDisplayName().toString());
                        myRef.child(requestId).setValue(reqUpdate).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(Answer.this,"Ph???n h???i ???? ???????c g???i ??i! C???m ??n b???n v?? ???? h??? tr??? !",Toast.LENGTH_LONG).show();
//                                Intent moveBackHome = new Intent(Answer.this, EmergencyControl.class);
//                                Answer.this.startActivity(moveBackHome);
                                finish();
                            }
                        });
                    }
                })
                .setNegativeButton("Kh??ng",null)
                .create()
                .show();
    }
}