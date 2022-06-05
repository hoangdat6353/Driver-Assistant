package hoangdat.tdtu.driverassist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;

public class AllRequestAdapter extends RecyclerView.Adapter<AllRequestAdapter.ViewHolder> {
    private List<Request> lstAllRequest;
    private Context mContext;

    public AllRequestAdapter(List<Request> lstAllRequest, Context mContext) {
        this.lstAllRequest = lstAllRequest;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public AllRequestAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_request_item,parent,false);
        AllRequestAdapter.ViewHolder holder = new AllRequestAdapter.ViewHolder(v);
        return holder;    }

    @Override
    public void onBindViewHolder(@NonNull AllRequestAdapter.ViewHolder holder, int position) {
        Request request = lstAllRequest.get(position);
        if (request == null)
            return;
        holder.tvRequestOwner.setText(request.getRequestOwner());
        holder.tvRequestText.setText(request.getRequestAsk());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goAnswer = new Intent(mContext,Answer.class);
                Bundle reqInfo = new Bundle();
                reqInfo.putSerializable("request_object", request);
                goAnswer.putExtras(reqInfo);
                mContext.startActivity(goAnswer);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lstAllRequest.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvRequestText, tvRequestOwner;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRequestOwner = itemView.findViewById(R.id.tvAllRequestOwner_Item);
            tvRequestText = itemView.findViewById(R.id.tvAllRequestText_Item);
        }
    }
}
