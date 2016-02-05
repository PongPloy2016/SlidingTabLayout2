package com.example.pongwiiapp.slidingtablayout;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pongwiiapp.db.QRCodeDAO;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by pongwiiApp on 1/2/2559.
 */
public class Tab4 extends Fragment implements SearchView.OnQueryTextListener,
        SearchView.OnCloseListener {


    private ArrayList<String> nameList;
    private  ListView  Product_listview ;

    private QRCodeDAO mDbHelper;
    public SQLiteDatabase database;


    private QrCodeListAdapter_1 mAdapter_product;

    private MyCustomAdapter_Product defaultAdapter;
    private ArrayList<String> nameList_product;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_4, container, false);

        nameList = new ArrayList<String>();

        final EditText urlEditText_code = (EditText) v.findViewById(R.id.edit_text_code_product);
        final EditText urlEditText = (EditText) v.findViewById(R.id.url_product_name);
        final EditText urlEditText_sell = (EditText) v.findViewById(R.id.url_deteal);

        Product_listview = (ListView) v.findViewById(R.id.product_add_list_view);



        final Spinner spnr = (Spinner)v.findViewById(R.id.spinner);
        final  Spinner spnr_num = (Spinner)v.findViewById(R.id.spinner_num);

        //ประกาศ ArrayAdpter เพื่อเรียกใช้ simple_spinner_item  โดยเอามาจาก celebrities


//        searchView.setIconifiedByDefault(false);
//
//        searchView.setOnQueryTextListener(this);
//        searchView.setOnCloseListener(this);


        mDbHelper = new QRCodeDAO(getContext());
        mDbHelper.open();



        defaultAdapter = new MyCustomAdapter_Product(getActivity(), nameList_product);
        Product_listview.setAdapter(defaultAdapter);

        for (String name : nameList) {
            mDbHelper.createList(name);
        }
        // qrCodeListView.setAdapter(defaultAdapter);

        mAdapter_product = new QrCodeListAdapter_1(
                getContext(),
                R.layout.qr_item_layout_product,
                new ArrayList<QRCodeDAO.QrItem_Product>()
        );


        Product_listview.setAdapter(mAdapter_product);
      //  updateListView();


        return v;
    }




    @Override
    public boolean onClose() {
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }


    private class QrCodeListAdapter_1 extends ArrayAdapter<QRCodeDAO.QrItem_Product> {

        private final String TAG = QrCodeListAdapter_1.class.getSimpleName();

        private Context mContext;
        private int mLayoutResId;
        private ArrayList<QRCodeDAO.QrItem_Product> mQrItemList;

        public QrCodeListAdapter_1 (Context context, int layoutResId, ArrayList<QRCodeDAO.QrItem_Product> qrItemList) {
            super(context, layoutResId, qrItemList);
            mContext = context;
            mLayoutResId = layoutResId;
            mQrItemList = qrItemList;
        }

        @Override
        public View getView(final int position, final View convertView, ViewGroup parent) {
            View itemLayout = convertView;
            ViewHolder holder;

            if (itemLayout == null) {
                itemLayout = View.inflate(mContext, mLayoutResId, null);

                ImageView qrCodeImageView = (ImageView) itemLayout.findViewById(R.id.qr_code_image_view);
                TextView qrCodeTextView = (TextView) itemLayout.findViewById(R.id.qr_code_text_view);

                TextView qrCodeTextViewcode = (TextView) itemLayout.findViewById(R.id.qr_code_text_view_code);

                Button qrCodeTextViewcode_save = (Button) itemLayout.findViewById(R.id.button_save);

                holder = new ViewHolder(qrCodeImageView, qrCodeTextView, qrCodeTextViewcode,qrCodeTextViewcode_save);
                itemLayout.setTag(holder);
            }

            holder = (ViewHolder) itemLayout.getTag();
         //   holder.qrCodeImageView.setImageBitmap(mQrItemList.get(position).product_code);
            holder.qrCodeTextView.setText(mQrItemList.get(position).product_name);
            holder.qrCodeTextViewcode.setText(mQrItemList.get(position).product_detail);

            holder.qrCodeTextViewcode_save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mQrItemList.get(position);  //เรียกตาม Piction

                    Log.e("qrCodeTextView", mQrItemList.get(position).toString());
                    Log.e("click", "click");


                    Toast.makeText(getContext(), " Create Completed... " + position,
                            Toast.LENGTH_LONG).show();

                    View v1 = Product_listview.findViewById(R.id.qr_code_image_view);
                    v1.setDrawingCacheEnabled(true);
                    Bitmap bmp = Bitmap.createBitmap(v1.getDrawingCache());
                    v1.setDrawingCacheEnabled(false);

                    try {
                        Date d = new Date();
                        String filename = (String) DateFormat.format("kkmmss-MMddyyyy"
                                , d.getTime());
                        File dir = new File(Environment.getExternalStorageDirectory()
                                , "/Pictures/" + filename + ".jpg");
                        FileOutputStream out = new FileOutputStream(dir);
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        bmp.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                        out.write(bos.toByteArray());
                        Toast.makeText(getContext(), "Save card!"
                                , Toast.LENGTH_SHORT).show();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
            });




            return itemLayout;
        }


        private class ViewHolder {
            public final ImageView qrCodeImageView;
            public final TextView qrCodeTextView;
            public final TextView qrCodeTextViewcode;
            public final Button qrCodeTextViewcode_save;


            public ViewHolder(ImageView qrCodeImageView, TextView qrCodeTextView, TextView qrCodeTextViewcode , Button qrCodeTextViewcode_save) {
                this.qrCodeImageView = qrCodeImageView;
                this.qrCodeTextView = qrCodeTextView;
                this.qrCodeTextViewcode = qrCodeTextViewcode;
                this.qrCodeTextViewcode_save = qrCodeTextViewcode_save;
            }
        }





    }
}
