package hoangdat.tdtu.driverassist;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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

public class ChangeOilAdapter extends RecyclerView.Adapter<ChangeOilAdapter.ViewHolder>
{
    private List<OilObject> lstChangeOil;
    private Context mContext;
    Bitmap bmp,scalebmp,bmp2,scalebmp2;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public ChangeOilAdapter(List<OilObject> lstChangeOil, Context mContext) {
        this.lstChangeOil = lstChangeOil;
        this.mContext = mContext;
    }


    @NonNull
    @Override
    public ChangeOilAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.change_oil_item,parent,false);
        ChangeOilAdapter.ViewHolder holder = new ChangeOilAdapter.ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChangeOilAdapter.ViewHolder holder, int position) {
        OilObject changeOilObject = lstChangeOil.get(position);
        if (changeOilObject == null)
            return;
        holder.tvChangeOilAddress.setText(changeOilObject.getAddress());
        holder.tvChangeOilPrice.setText(new StringBuilder().append(String.valueOf(changeOilObject.getPrice())).append(" VN?? ").toString());
        holder.tvChangeOilDate.setText(changeOilObject.getDate());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickDetails(changeOilObject);
            }
        });

        holder.imvPrintPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askPrint(changeOilObject);
            }
        });
    }
    public void onClickDetails(OilObject oilObject)
    {
        Intent sendOilInfo = new Intent(mContext, ChangeOilDetails.class);
        Bundle oilInfo = new Bundle();
        oilInfo.putSerializable("change_oil_object",oilObject);
        sendOilInfo.putExtras(oilInfo);
        mContext.startActivity(sendOilInfo);
    }
    private void askPrint(OilObject oilObject)
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
        alert.setTitle("Xu???t l???ch s??? ra file PDF");
        alert.setIcon(R.drawable.ic_baseline_print_24);
        alert.setMessage("Thao t??c n??y s??? xu???t l???ch s??? c???a l???n thay d???u nh???t n??y ra file PDF. B???n ch???c ch??? ?");
        alert.setPositiveButton("Xu???t l???ch s??? thay d???u nh???t", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                exportToPdf(oilObject);
            }
        });
        alert.setNegativeButton("H???y", null);
        alert.show();
    }
    public void exportToPdf(OilObject oilObject)
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
        canvas.drawText("L???CH S??? THAY D???U NH???T",myPageInfo.getPageWidth()/2, 30,myPaint);
        myPaint.setTextSize(6.0f);
        myPaint.setColor(Color.rgb(122,119,119));
        canvas.drawText("Driver Assist designed by TAD",myPageInfo.getPageWidth()/2,40,myPaint);

        myPaint.setTextAlign(Paint.Align.LEFT);
        myPaint.setTextSize(9.0f);
        canvas.drawText("Th??ng tin c??? th???: ",10,70, myPaint);

        myPaint.setTextAlign(Paint.Align.LEFT);
        myPaint.setTextSize(8.0f);
        myPaint.setColor(Color.BLACK);


        String[] dataField = new String[]{"T??n xe: ", "Ch??? xe: ", "?????a ch??? thay d???u nh???t: ","Gi?? c???: ", "Ng??y th???c hi???n: ", "Dung t??ch ?????: ", "Ghi ch??: "};
        String[] listData = new String[]{oilObject.getVehName(), mAuth.getCurrentUser().getDisplayName(), oilObject.getAddress(), oilObject.getPrice().toString(),
        oilObject.getDate(), oilObject.getCapacity().toString(), oilObject.getNote()};

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

        String fileName = "LichSuThayDauNhot_" + oilObject.getChangeOilID() + ".pdf";
        File file = new File(Environment.getExternalStorageDirectory(), fileName);
        try {
            myPdfDocument.writeTo(new FileOutputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        myPdfDocument.close();



        String stringFile = Environment.getExternalStorageDirectory().getPath() + File.separator + fileName;
        AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
        alert.setTitle("Chia s??? file PDF");
        alert.setIcon(R.drawable.ic_baseline_share_24);
        alert.setMessage("T???p tin ???? ???????c t???o th??nh c??ng ! B???n c?? mu???n chia s??? t???p l???ch s??? thay d???u nh???t .pdf v???a xu???t ra kh??ng?");
        alert.setPositiveButton("C??", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                File fileShare = new File(stringFile);
                if (!fileShare.exists())
                {
                    Toast.makeText(mContext,"File kh??ng t???n t???i",Toast.LENGTH_LONG).show();
                    return;
                }
                Intent intentShare = new Intent(Intent.ACTION_SEND);
                intentShare.setType("application/pdf");
                Uri fileUri = FileProvider.getUriForFile(mContext, "hoangdat.tdtu.driverassist.provider",
                        fileShare);
                intentShare.putExtra(Intent.EXTRA_STREAM, fileUri);
                mContext.startActivity(Intent.createChooser(intentShare,"Chia s??? t???p tin n??y..."));
            }
        });
        alert.setNegativeButton("Kh??ng", null);
        alert.show();
    }

    @Override
    public int getItemCount() {
        return lstChangeOil.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvChangeOilAddress,tvChangeOilPrice,tvChangeOilDate;
        ImageView imvPrintPdf;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvChangeOilAddress = itemView.findViewById(R.id.tvChangeOilAddress_Item);
            tvChangeOilPrice = itemView.findViewById(R.id.tvChangeOilPrice_Item);
            tvChangeOilDate = itemView.findViewById(R.id.tvChangeOilDay_Item);
            imvPrintPdf = itemView.findViewById(R.id.imvPrintPdfChangeOil);

        }
    }
}
