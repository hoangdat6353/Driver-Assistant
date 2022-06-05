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
                .setTitle("Hỗ trợ tài xế khác")
                .setMessage("Thao tác này sẽ gửi phản hồi tới tài xế yêu cầu trợ giúp. Bạn chắc chắn với câu trả lời của mình chứ?")
                .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        myRef = database.getReference("Request");
                        String requestId = req.getRequestId().toString();

                        Request reqUpdate = new Request(requestId,req.getRequestOwner(),req.getRequestAsk(),etAnswer.getText().toString(),true,mAuth.getCurrentUser().getDisplayName().toString());
                        myRef.child(requestId).setValue(reqUpdate).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(Answer.this,"Phản hồi đã được gửi đi! Cảm ơn bạn vì đã hỗ trợ !",Toast.LENGTH_LONG).show();
//                                Intent moveBackHome = new Intent(Answer.this, EmergencyControl.class);
//                                Answer.this.startActivity(moveBackHome);
                                finish();
                            }
                        });
                    }
                })
                .setNegativeButton("Không",null)
                .create()
                .show();
    }
}