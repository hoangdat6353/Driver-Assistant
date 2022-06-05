package hoangdat.tdtu.driverassist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyRequestAdapter extends RecyclerView.Adapter<MyRequestAdapter.ViewHolder>{
    private List<Request> lstRequest;
    private Context mContext;

    public MyRequestAdapter(List<Request> lstRequest, Context mContext) {
        this.lstRequest = lstRequest;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public MyRequestAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.request_item,parent,false);
        MyRequestAdapter.ViewHolder holder = new MyRequestAdapter.ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyRequestAdapter.ViewHolder holder, int position) {
        Request request = lstRequest.get(position);
        if (request == null)
            return;
        if (request.getRequestFlag())
        {
            holder.tvRequestText.setText(request.getRequestAnswer());
        } else {
            holder.tvRequestText.setText(request.getRequestAsk());
        }
        holder.tvRequestOwner.setText(request.getRequestOwner());
        if (request.getRequestFlag())
        {
            holder.imvRequestFlag.setImageResource(R.drawable.check);
            holder.tvRequestSupporter.setText(request.getRequestAnswerBy());
        }
        else
            holder.imvRequestFlag.setImageResource(R.drawable.uncheck);

    }

    @Override
    public int getItemCount() {
        return lstRequest.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvRequestText, tvRequestOwner,tvRequestSupporter;
        ImageView imvRequestFlag;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRequestOwner = itemView.findViewById(R.id.tvRequestOwner_Item);
            tvRequestText = itemView.findViewById(R.id.tvRequestText_Item);
            imvRequestFlag = itemView.findViewById(R.id.imvRequestFlag_Item);
            tvRequestSupporter = itemView.findViewById(R.id.tvSupporterName);
        }
    }
}
