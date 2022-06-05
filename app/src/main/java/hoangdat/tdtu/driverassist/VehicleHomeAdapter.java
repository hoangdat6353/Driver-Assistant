package hoangdat.tdtu.driverassist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.List;

public class VehicleHomeAdapter extends RecyclerView.Adapter<VehicleHomeAdapter.ViewHolder> {
    private List<Vehicle> lstVehicle;
    private Context mContext;
    private int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public VehicleHomeAdapter(List<Vehicle> lstVehicle, Context mContext) {
        this.lstVehicle = lstVehicle;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_vehicle_item,parent,false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Vehicle vhc = lstVehicle.get(position);
        if (vhc == null)
            return;
        holder.tvVehicleName.setText(vhc.getVehName());
        holder.tvVehicleBrand.setText(vhc.getVehBrand());
        holder.tvVehicleType.setText(vhc.getVehType());

        if (vhc.getVehType().equals("Xe máy")) {
            holder.imvVehicleImage.setImageResource(R.drawable.motorcycle_item);
        }
        if (vhc.getVehType().equals("Xe hơi")) {
            holder.imvVehicleImage.setImageResource(R.drawable.vehicle_item);
        }
        if (vhc.getVehType().equals("Xe tải")) {
            holder.imvVehicleImage.setImageResource(R.drawable.truck_item);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickDetail(vhc);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setPosition(holder.getPosition());
                return false;
            }
        });

    }

    public void onClickDetail(Vehicle vhc) {
        Intent sendVehicleInfo = new Intent(mContext, VehicleDetails.class);
        Bundle vehInfo = new Bundle();
        vehInfo.putSerializable("vehicle_object", vhc);
        sendVehicleInfo.putExtras(vehInfo);
        mContext.startActivity(sendVehicleInfo);

    }

    @Override
    public int getItemCount() {
        if (lstVehicle != null)
            return lstVehicle.size();
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements  View.OnCreateContextMenuListener {
        ImageView imvVehicleImage;
        TextView tvVehicleName,tvVehicleBrand,tvVehicleType;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imvVehicleImage = itemView.findViewById(R.id.imvVehicleImage);
            tvVehicleName = itemView.findViewById(R.id.tvVehicleNameHomeItem);
            tvVehicleBrand = itemView.findViewById(R.id.tvVehicleBrandHomeItem);
            tvVehicleType = itemView.findViewById(R.id.tvVehicleTypeHomeItem);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(0, R.id.reminder_menu_button, 0, "Đặt lịch sử");
        }
    }
}
