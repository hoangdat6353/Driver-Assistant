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

public class HistoryChangeOil extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();

    RecyclerView rcvChangeOil;
    List<OilObject> lstChangeOil;
    ChangeOilAdapter adapter;
    List<String> mKeys = new ArrayList<>();
    Vehicle vhc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_change_oil);


        getData();
        rcvChangeOil = findViewById(R.id.rcvChangeOil);
        rcvChangeOil.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ChangeOilAdapter(lstChangeOil,this);
        rcvChangeOil.setAdapter(adapter);
    }
    public void getData()
    {
        lstChangeOil = new ArrayList<>();
        myRef = database.getReference("Change Oil History").child(Objects.requireNonNull(mAuth.getCurrentUser().getUid()));

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                OilObject oil = snapshot.getValue(OilObject.class);
                if (oil != null) {
                    lstChangeOil.add(oil);
                    String key = snapshot.getKey();
                    mKeys.add(key);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                OilObject oil = snapshot.getValue(OilObject.class);
                if (oil == null || lstChangeOil == null || lstChangeOil.isEmpty())
                    return;
                int index = mKeys.indexOf(snapshot.getKey());
                lstChangeOil.set(index,oil);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                OilObject oil = snapshot.getValue(OilObject.class);
                if (oil == null || lstChangeOil == null || lstChangeOil.isEmpty())
                    return;
                int index = mKeys.indexOf(snapshot.getKey());
                if (index != -1)
                {
                    lstChangeOil.remove(index);
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