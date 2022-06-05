package hoangdat.tdtu.driverassist;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RemindControl extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();

    RecyclerView rcvReminder;
    List<Reminder> lstRemind;
    List<String> mKeys = new ArrayList<>();
    ReminderAdapter adapter;
    FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remind_control);

        getData();
        floatingActionButton = findViewById(R.id.floatingAddButton);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent moveToAddRemind = new Intent(RemindControl.this, AddRemind.class);
                startActivity(moveToAddRemind);
            }
        });

        rcvReminder = findViewById(R.id.rcvReminder);
        rcvReminder.setLayoutManager(new LinearLayoutManager(RemindControl.this));
        adapter = new ReminderAdapter(lstRemind,RemindControl.this);
        rcvReminder.setAdapter(adapter);
    }

    private void getData()
    {
        lstRemind = new ArrayList<>();
        myRef = database.getReference("Remind").child(Objects.requireNonNull(mAuth.getCurrentUser().getUid()));

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Reminder rmd = snapshot.getValue(Reminder.class);
                if (rmd != null) {
                    lstRemind.add(rmd);
                    String key = snapshot.getKey();
                    mKeys.add(key);
                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Reminder rmd = snapshot.getValue(Reminder.class);
                if (rmd == null || lstRemind == null || lstRemind.isEmpty())
                    return;
                int index = mKeys.indexOf(snapshot.getKey());
                lstRemind.set(index,rmd);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Reminder rmd = snapshot.getValue(Reminder.class);
                if (rmd == null || lstRemind == null || lstRemind.isEmpty())
                    return;
                int index = mKeys.indexOf(snapshot.getKey());
                if (index != -1)
                {
                    lstRemind.remove(index);
                    mKeys.remove(index);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}