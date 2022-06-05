package hoangdat.tdtu.driverassist;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HistoryRefuel extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();

    RecyclerView rcvRefuel;
    List<RefuelObject> lstRefuel;
    RefuelAdapter adapter;
    List<String> mKeys = new ArrayList<>();
    Vehicle vhc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_refuel);


        getData();
        rcvRefuel = findViewById(R.id.rcvRefuel);
        rcvRefuel.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RefuelAdapter(lstRefuel,this);
        rcvRefuel.setAdapter(adapter);

    }

    private void getData() {
        lstRefuel = new ArrayList<>();
        myRef = database.getReference("Refuel History").child(Objects.requireNonNull(mAuth.getCurrentUser().getUid()));

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                RefuelObject rfo = snapshot.getValue(RefuelObject.class);
                if (rfo != null) {
                    lstRefuel.add(rfo);
                    String key = snapshot.getKey();
                    mKeys.add(key);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                RefuelObject rfo = snapshot.getValue(RefuelObject.class);
                if (rfo == null || lstRefuel == null || lstRefuel.isEmpty())
                    return;
                int index = mKeys.indexOf(snapshot.getKey());
                lstRefuel.set(index,rfo);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                RefuelObject rfo = snapshot.getValue(RefuelObject.class);
                if (rfo == null || lstRefuel == null || lstRefuel.isEmpty())
                    return;
                int index = mKeys.indexOf(snapshot.getKey());
                if (index != -1)
                {
                    lstRefuel.remove(index);
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