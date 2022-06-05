package hoangdat.tdtu.driverassist;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class RefuelAdapter extends RecyclerView.Adapter<RefuelAdapter.ViewHolder>
{
    private List<RefuelObject> lstRefuel;
    private Context mContext;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();;

    Bitmap bmp,scalebmp,bmp2,scalebmp2;

    public RefuelAdapter(List<RefuelObject> lstRefuel, Context mContext) {
        this.lstRefuel = lstRefuel;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public RefuelAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.refuel_item,parent,false);
        RefuelAdapter.ViewHolder holder = new RefuelAdapter.ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RefuelAdapter.ViewHolder holder, int position) {
            RefuelObject refuelObject = lstRefuel.get(position);
            if (refuelObject == null)
                 return;
            holder.tvRefuelAddress.setText(refuelObject.getRefuelAddress());
            holder.tvRefuelPrice.setText(new StringBuilder().append(String.valueOf(refuelObject.getRefuelPrice())).append(" VNĐ ").toString());
            holder.tvRefuelDate.setText(refuelObject.getRefuelDate());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickDetails(refuelObject);
                }
            });

            holder.imvPrintPdf.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    askPrint(refuelObject);
                }
            });
    }

    private void onClickDetails(RefuelObject refuelObject) {
        Intent sendRefuelInfo = new Intent(mContext, RefuelDetails.class);
        Bundle refInfo = new Bundle();
        refInfo.putSerializable("refuel_object",refuelObject);
        sendRefuelInfo.putExtras(refInfo);
        mContext.startActivity(sendRefuelInfo);
    }

    private void askPrint(RefuelObject refuelObject)
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
        alert.setTitle("Xuất lịch sử ra file PDF");
        alert.setIcon(R.drawable.ic_baseline_print_24);
        alert.setMessage("Thao tác này sẽ xuất lịch sử của lần đổ xăng này ra file PDF. Bạn chắc chứ ?");
        alert.setPositiveButton("Xuất lịch sử đổ xăng", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               exportToPdf(refuelObject);
            }
        });
        alert.setNegativeButton("Hủy", null);
        alert.show();


    }

    public void exportToPdf(RefuelObject refuelObject)
    {
        PdfDocument myPdfDocument = new PdfDocument();
        Paint myPaint = new Paint();
        PdfDocument.PageInfo myPageInfo = new PdfDocument.PageInfo.Builder(250,400,1).create();
        PdfDocument.Page myPage = myPdfDocument.startPage(myPageInfo);
        Canvas canvas = myPage.getCanvas();

        bmp = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.pdfbanner);
        scalebmp = Bitmap.createScaledBitmap(bmp,250,80,false);
        bmp2 = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.mintgreen_background);
        scalebmp2 = Bitmap.createScaledBitmap(bmp2,250,400,false);
        canvas.drawBitmap(scalebmp2,0,0,myPaint);
        canvas.drawBitmap(scalebmp,0,300,myPaint);

        myPaint.setTextAlign(Paint.Align.CENTER);
        myPaint.setTextSize(16.0f);
        myPaint.setColor(Color.RED);
        canvas.drawText("LỊCH SỬ ĐỔ XĂNG",myPageInfo.getPageWidth()/2, 30,myPaint);
        myPaint.setTextSize(6.0f);
        myPaint.setColor(Color.rgb(122,119,119));
        canvas.drawText("Driver Assist designed by TAD",myPageInfo.getPageWidth()/2,40,myPaint);

        myPaint.setTextAlign(Paint.Align.LEFT);
        myPaint.setTextSize(9.0f);
        canvas.drawText("Thông tin cụ thể: ",10,70, myPaint);

        myPaint.setTextAlign(Paint.Align.LEFT);
        myPaint.setTextSize(8.0f);
        myPaint.setColor(Color.BLACK);

        String[] listInformation = new String[]{"Tên xe: " + refuelObject.getVehName() ,"Chủ xe: " + mAuth.getCurrentUser().getDisplayName().toString(),"Địa chỉ đổ xăng: " + refuelObject.getRefuelAddress(), "Gíá cả: " +refuelObject.getRefuelPrice().toString(), "Ngày thực hiện: " + refuelObject.getRefuelDate(),
                "Dung tích đổ: " + refuelObject.getRefuelCapacity().toString(), "Ghi chú: " + refuelObject.getRefuelNote() };

        String[] dataField = new String[]{"Tên xe: ", "Chủ xe: ", "Địa chỉ đổ xăng: ","Giá cả: ", "Ngày thực hiện: ", "Dung tích đổ: ", "Ghi chú: "};
        String[] listData = new String[]{refuelObject.getVehName(), mAuth.getCurrentUser().getDisplayName(), refuelObject.getRefuelAddress(), refuelObject.getRefuelPrice().toString(),
                refuelObject.getRefuelDate(), refuelObject.getRefuelCapacity().toString(), refuelObject.getRefuelNote()};

        int startXPosition = 10;
        int startXaPosition = myPageInfo.getPageWidth() - 10;
        int endXPosition = myPageInfo.getPageWidth() - 10;
        int startYPosition = 100;

        for (int i = 0; i <= 6; i++)
        {
            myPaint.setTextSize(10.0f);
            canvas.drawText(dataField[i], startXPosition, startYPosition,myPaint);
            myPaint.setTextAlign(Paint.Align.RIGHT);
            canvas.drawText(listData[i],startXaPosition,startYPosition,myPaint);
            myPaint.setTextAlign(Paint.Align.LEFT);
            canvas.drawLine(startXPosition,startYPosition +3 ,endXPosition,startYPosition + 3,myPaint);
            startYPosition += 30;
        }

        myPdfDocument.finishPage(myPage);

          String fileName = "LichSuDoXang_" + refuelObject.getRefuelID() + ".pdf";
        File file = new File(Environment.getExternalStorageDirectory(), fileName);
        try {
            myPdfDocument.writeTo(new FileOutputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        myPdfDocument.close();


        String stringFile = Environment.getExternalStorageDirectory().getPath() + File.separator + fileName;

        AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
        alert.setTitle("Chia sẻ file PDF");
        alert.setIcon(R.drawable.ic_baseline_share_24);
        alert.setMessage("Tệp tin đã được tạo thành công ! Bạn có muốn chia sẻ tệp lịch sử đổ xăng .pdf vừa xuất ra không?");
        alert.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                File fileShare = new File(stringFile);
                if (!fileShare.exists())
                {
                    Toast.makeText(mContext,"File không tồn tại",Toast.LENGTH_LONG).show();
                    return;
                }
                Intent intentShare = new Intent(Intent.ACTION_SEND);
                intentShare.setType("application/pdf");
                Uri fileUri = FileProvider.getUriForFile(mContext, "hoangdat.tdtu.driverassist.provider",
                        fileShare);
                intentShare.putExtra(Intent.EXTRA_STREAM, fileUri);
                mContext.startActivity(Intent.createChooser(intentShare,"Chia sẻ tệp tin này..."));
            }
        });
        alert.setNegativeButton("Không", null);
        alert.show();
    }

    @Override
    public int getItemCount() {
        return lstRefuel.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvRefuelAddress,tvRefuelPrice,tvRefuelDate;
        ImageView imvPrintPdf;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRefuelAddress = itemView.findViewById(R.id.tvRefuelAddress_Item);
            tvRefuelPrice = itemView.findViewById(R.id.tvRefuelPrice_Item);
            tvRefuelDate = itemView.findViewById(R.id.tvRefuelDay_Item);
            imvPrintPdf = itemView.findViewById(R.id.imvPrintPdfRefuel);
        }
    }
}
