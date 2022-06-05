package hoangdat.tdtu.driverassist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ViewHolder>{
    private List<Reminder> lstReminder;
    private Context mContext;

    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;

    public ReminderAdapter(List<Reminder> lstReminder, Context mContext) {
        this.lstReminder = lstReminder;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ReminderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.remind_item,parent,false);
        ReminderAdapter.ViewHolder holder = new ReminderAdapter.ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ReminderAdapter.ViewHolder holder, int position) {
        Reminder reminder = lstReminder.get(position);
        if (reminder == null)
            return;
        holder.tvServiceName.setText(reminder.getServiceName());
        holder.tvDay.setText(reminder.getServiceDay());
        holder.tvNote.setText(reminder.getVehName());
        holder.imvDeleteReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteNoti(reminder);
            }
        });
    }

    public void deleteNoti(Reminder reminder)
    {
        DatabaseReference myRef = database.getReference("Remind");
        myRef.child(mAuth.getUid()).child(reminder.getReminderId()).removeValue();
        Toast.makeText(mContext,"Lời nhắc đã được xóa thành công khỏi hệ thống !",Toast.LENGTH_LONG).show();
    }


    @Override
    public int getItemCount() {
        return lstReminder.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvServiceName,tvDay,tvNote;
        ImageView imvDeleteReminder;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvServiceName = itemView.findViewById(R.id.tvReminderService_Item);
            tvDay = itemView.findViewById(R.id.tvReminderDay_Item);
            tvNote = itemView.findViewById(R.id.tvReminderNote_Item);
            imvDeleteReminder = itemView.findViewById(R.id.imvDeleteReminder_Item);
        }
    }
}
