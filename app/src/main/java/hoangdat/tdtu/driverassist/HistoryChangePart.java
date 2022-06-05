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

public class HistoryChangePart extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();

    RecyclerView rcvChangePart;
    List<PartObject> lstChangePart;
    ChangePartAdapter adapter;
    List<String> mKeys = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_change_part);

        getData();
        rcvChangePart = findViewById(R.id.rcvChangePart);
        rcvChangePart.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ChangePartAdapter(lstChangePart,this);
        rcvChangePart.setAdapter(adapter);
    }

    public void getData()
    {
        lstChangePart = new ArrayList<>();
        myRef = database.getReference("Change Part History").child(Objects.requireNonNull(mAuth.getCurrentUser().getUid()));

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                PartObject poj = snapshot.getValue(PartObject.class);
                if (poj != null) {
                    lstChangePart.add(poj);
                    String key = snapshot.getKey();
                    mKeys.add(key);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                PartObject poj = snapshot.getValue(PartObject.class);
                if (poj == null || lstChangePart == null || lstChangePart.isEmpty())
                    return;
                int index = mKeys.indexOf(snapshot.getKey());
                lstChangePart.set(index,poj);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                PartObject poj = snapshot.getValue(PartObject.class);
                if (poj == null || lstChangePart == null || lstChangePart.isEmpty())
                    return;
                int index = mKeys.indexOf(snapshot.getKey());
                if (index != -1)
                {
                    lstChangePart.remove(index);
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