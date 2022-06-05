package hoangdat.tdtu.driverassist;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReminderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReminderFragment extends Fragment {
    List<Vehicle> lstVehicle;
    List<String> mKeys = new ArrayList<>();
    RecyclerView rvVehicle;
    VehicleHomeAdapter adapter;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ReminderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReminderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReminderFragment newInstance(String param1, String param2) {
        ReminderFragment fragment = new ReminderFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_reminder, container, false);

        rvVehicle = rootView.findViewById(R.id.rcvVehicleHome);
        getData();
        rvVehicle.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new VehicleHomeAdapter(lstVehicle,getActivity());
        rvVehicle.setAdapter(adapter);
        registerForContextMenu(rvVehicle);
        return rootView;

    }

    public void getData()
    {
        lstVehicle = new ArrayList<>();
        myRef = database.getReference("Vehicle").child(Objects.requireNonNull(mAuth.getCurrentUser().getUid()));

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Vehicle vhc = snapshot.getValue(Vehicle.class);
                if (vhc != null) {
                    lstVehicle.add(vhc);
                    String key = snapshot.getKey();
                    mKeys.add(key);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Vehicle vhc = snapshot.getValue(Vehicle.class);
                if (vhc == null || lstVehicle == null || lstVehicle.isEmpty())
                    return;
                int index = mKeys.indexOf(snapshot.getKey());
                lstVehicle.set(index,vhc);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Vehicle vhc = snapshot.getValue(Vehicle.class);
                if (vhc == null || lstVehicle == null || lstVehicle.isEmpty())
                    return;
                int index = mKeys.indexOf(snapshot.getKey());
                if (index != -1)
                {
                    lstVehicle.remove(index);
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

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int position = -1;
        try {
            position = ((VehicleHomeAdapter)rvVehicle.getAdapter()).getPosition();
        } catch (Exception e) {
            Log.d("ContextMenu", e.getLocalizedMessage(), e);
            return super.onContextItemSelected(item);
        }
        Intent moveToHistory = new Intent(getActivity(), VehicleHistory.class);
        Vehicle vhc = lstVehicle.get(position);
        Bundle vehHistory = new Bundle();
        vehHistory.putSerializable("vehicle_object_history", vhc);
        moveToHistory.putExtras(vehHistory);
        startActivity(moveToHistory);

        return super.onContextItemSelected(item);
    }
}