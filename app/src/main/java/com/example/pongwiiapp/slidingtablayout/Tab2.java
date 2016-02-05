package com.example.pongwiiapp.slidingtablayout;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pongwiiapp.Contact.Contact;
import com.example.pongwiiapp.db.QRCodeDAO;
import com.example.pongwiiapp.db.SearchHelper;

import net.glxn.qrgen.android.QRCode;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import android.app.SearchManager;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
public class Tab2 extends Fragment  implements android.widget.SearchView.OnQueryTextListener,
        android.widget.SearchView.OnCloseListener{



  final   public   static   String[] celebrities = {
            "คอมพิวเตอร์",
            "ปริ้นเตอร์",
            "โปรเจคเตอร์",
            "อื่นๆ",
    };

  final   public   static  String[]  num = { "1","2","3","4","5","6","7","8","9","10"};

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int IMAGE_WIDTH_IN_PIXEL = 500;
    private static final int IMAGE_HEIGHT_IN_PIXEL = 500;

    private  static int position = 0;
    private  static  int position2 = 0;
    //private final ArrayList<QRCodeDAO.QrItem> mQrItemList = new ArrayList<>();
    private QrCodeListAdapter mAdapter;

    public  static String   urlString_code ;

    private QRCodeDAO mDbHelper;

    private ListView qrCodeListView ;

    private MyCustomAdapter defaultAdapter;
    private ArrayList<String> nameList;

    private android.widget.SearchView searchView;
    private Cursor cursor ;

    Handler handler ;

    public Tab2() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.tab_2,container,false);




        nameList = new ArrayList<String>();
//        for (int i = 0; i < 20; i++) {
//            nameList.add("Diana" + i);
//        }



        final EditText urlEditText_code = (EditText) v.findViewById(R.id.url_edit_text_code);
        final EditText urlEditText = (EditText) v.findViewById(R.id.url_edit_text);
        final EditText urlEditText_sell = (EditText) v.findViewById(R.id.url_edit_text_sell);

        qrCodeListView = (ListView) v.findViewById(R.id.qr_code_list_view);

        searchView = (android.widget.SearchView) v.findViewById(R.id.search);

        final  Spinner spnr = (Spinner)v.findViewById(R.id.spinner);
        final  Spinner spnr_num = (Spinner)v.findViewById(R.id.spinner_num);

        //ประกาศ ArrayAdpter เพื่อเรียกใช้ simple_spinner_item  โดยเอามาจาก celebrities


        searchView.setIconifiedByDefault(false);

        searchView.setOnQueryTextListener(this);
        searchView.setOnCloseListener(this);


        mDbHelper = new QRCodeDAO(getContext());
        mDbHelper.open();



        defaultAdapter = new MyCustomAdapter(getActivity(), nameList);
        qrCodeListView.setAdapter(defaultAdapter);

        for (String name : nameList) {
             mDbHelper.createList(name);
        }
       // qrCodeListView.setAdapter(defaultAdapter);

        mAdapter = new QrCodeListAdapter(
                getContext(),
                R.layout.qr_item_layout,
                new ArrayList<QRCodeDAO.QrItem>()
        );


        qrCodeListView.setAdapter(mAdapter);
        updateListView();

        Button generateQrCodeButton = (Button) v.findViewById(R.id.serach_qrcode);
        generateQrCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("Click","Click");

                if (urlEditText_code.getText().toString().equals("")) {

                    Toast.makeText(getContext(), "กรุณากรอกรหัสการยืม ",
                            Toast.LENGTH_LONG).show();
                } else if (urlEditText.getText().toString().equals("")) {

                    Toast.makeText(getContext(), "กรุณากรอกชื่อผู้ยืม ",
                            Toast.LENGTH_LONG).show();
                } else if ((urlEditText_code.getText().toString().equals("")) && (urlEditText.getText().toString().equals("")) && (urlEditText_sell.getText().toString().equals(""))) {

                    Toast.makeText(getContext(), "กรุณากรอกวันที่ยืม",
                            Toast.LENGTH_LONG).show();
                } else {

                    String urlString = urlEditText.getText().toString();
                    urlString_code = urlEditText_code.getText().toString();
                    String urlString_code_sell = urlEditText_sell.getText().toString();
                    String sample = num[position];
                    String number = num[position2];

                    Log.e("data ", String.valueOf(urlString_code));
                    Log.e("sample ", sample);
                    Log.e("number ", number);

                    ByteArrayOutputStream qrCodeBOS = QRCode

                            //รหัสสินค้า , ปนะเภทสินค้า,ชื่อสิ้นค้า,ราคาสิ้นค้า,จำนวน
                            .from("product code" + ":" + urlString_code + "\n" +
                                    "Product Name" + ":" + urlString + "Price" + " :" + urlString_code_sell + "Category" + ":" + sample + "number" + ":" + number)

                            .withSize(IMAGE_WIDTH_IN_PIXEL, IMAGE_HEIGHT_IN_PIXEL)
                            .stream();
                    Log.e("data ", String.valueOf(qrCodeBOS));

//                    Log.e("data text code  ", String.valueOf(urlString_code));
//                    Log.e("data text   ", String.valueOf(urlString));
//                    Log.e("data text_dell  ", String.valueOf(urlString_code_sell));
//                    Log.e("data munber  ", String.valueOf(celebrities[+position]));
//                    Log.e("data simple  ", String.valueOf(num[+position]));

                    QRCodeDAO db = new QRCodeDAO(getContext());
                    if (db.insert(urlString_code, urlString, urlString_code_sell, celebrities[position], num[position], qrCodeBOS.toByteArray()) > -1) {
                        Toast.makeText(getContext(), "QR Code saved.", Toast.LENGTH_SHORT).show();
                        updateListView();

                        int duration = Toast.LENGTH_LONG;

                        Toast toast = Toast.makeText(getContext(), "รหัส" + urlString_code + "  \n " + " ประเภทสินค้า  " + celebrities[position] + "  \n " +
                                "ชื่อสินค้า" + urlString_code + "  \n " + "ราคา" + urlString_code_sell + "  \n " + "จำนวน" + num[+position] + "ชิ้น", duration);
                        toast.show();

                    }
                    urlEditText.setText("");
                    urlEditText_code.setText("");
                    urlEditText_sell.setText("");

                }
            }
        });
//bu
//        Button buttonsave = (Button)v.findViewById(R.id.generate_qr_code_button);
//        buttonsave.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//// save image
//
//            }
//        });

        Button delete_QrCodeButton = (Button) v.findViewById(R.id.delete_qrcode);

        delete_QrCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("delete");
                builder.setMessage("คุณต้องการที่จะลบข้อมูล QRCODE ทั้งหมด หรือไม่");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        QRCodeDAO.deleteAll();
                        updateListView();


//                        db.execSQL("DELETE FROM "+ QRCodeDAO.TABLE_NAME
//                                + " WHERE " + QRCodeDAO.COL_TEXT + "='" + name + "'"
//                                + " AND " + QRCodeDAO.COL_TEXT_CODE + "='" + lastname + "'"
//                                + " AND " + QRCodeDAO.COL_QR_CODE_BITMAP + "='" + school + "';");
//
//                        mCursor.requery();

                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();

                setHasOptionsMenu(true);


            }
        });


        //โชร์เมื่อ Click
        qrCodeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showQrCodeImageInDialog(
                        mAdapter.getItem(position).text,
                        mAdapter.getItem(position).textcode,
                        mAdapter.getItem(position).text_sell,
                        mAdapter.getItem(position).text_sample,
                        mAdapter.getItem(position).text_number,
                        mAdapter.getItem(position).qrCodeBitmap

                );
            }
        });


        qrCodeListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Delete ");
                builder.setMessage("คุณต้องการที่จะลบข้อมูล Qrcode บางส่วนหรือไม่");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        QRCodeDAO.delete_byID(position);
                        updateListView();

//                        db.execSQL("DELETE FROM "+ QRCodeDAO.TABLE_NAME
//                                + " WHERE " + QRCodeDAO.COL_TEXT + "='" + name + "'"
//                                + " AND " + QRCodeDAO.COL_TEXT_CODE + "='" + lastname + "'"
//                                + " AND " + QRCodeDAO.COL_QR_CODE_BITMAP + "='" + school + "';");
//
//                        mCursor.requery();


                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();

                setHasOptionsMenu(true);

                return false;
            }

        });

//        Button  serach_QrCodeButton = (Button) v.findViewById(R.id.serach_qrcode);
//
//
//        serach_QrCodeButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Contact cc = new Contact();
//
//                if (urlEditText_code.getText().toString().equals("")){
//
//                    Toast.makeText(getContext(), "กรุณากรอกรหัสผ่านสินค้า ",
//                            Toast.LENGTH_LONG).show();
//                }
//
//                else
//                {
//                    urlString_code = urlEditText_code.getText().toString();
//
//                    Log.e("test_code",urlString_code);
//
//                   // QRCodeDAO.serach(urlString_code);
//
//                   //QRCodeDAO.serach1(urlString_code);
//                    displayResults(urlString_code);
//
//
//                  //  mDbHelper.getCountry(urlString_code);
//                   // updateListView();
//                }
//            }
//        });


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getContext(), android.R.layout.simple_spinner_item, celebrities);

        spnr.setAdapter(adapter);
        spnr.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {

                        position = spnr.getSelectedItemPosition();

                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub
                        Toast.makeText(getContext()," คุณเลือกประเภท "+celebrities[+position],Toast.LENGTH_LONG).show();
                    }

                }
        );

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(
                getContext(), android.R.layout.simple_spinner_item, num);

        spnr_num.setAdapter(adapter2);
        spnr_num.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {

                        position = spnr_num.getSelectedItemPosition();

                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub
                        Toast.makeText(getContext()," คุณเลือก "+num[+position] + "ชื้น ",Toast.LENGTH_LONG).show();
                    }

                }
        );



        return v;
    }




    private void displayResults(String query) {


           cursor = mDbHelper.searchByInputText((query != null ? query : "@@@@"));


        if (cursor != null) {

            String[] from = new String[]{mDbHelper.COL_TEXT_CODE};

            // Specify the view where we want the results to go
            int[] to = new int[]{R.id.search_result_text_view};

            // Create a simple cursor adapter to keep the search data
            SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(getContext(), R.layout.qr_item_layout, cursor, from, to);
            qrCodeListView.setAdapter(cursorAdapter);

            // Click listener for the searched item that was selected
//            qrCodeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                    // Get the cursor, positioned to the corresponding row in the result set
//                   // Cursor cursor = (Cursor) qrCodeListView.getItemAtPosition(position);
//
//                    // Get the state's capital from this row in the database.
//                    String selectedName = cursor.getString(cursor.getColumnIndexOrThrow("name"));
//                    Toast.makeText(getContext(), selectedName, Toast.LENGTH_SHORT).show();
//
//
//
//                    // Set the default adapter
//                    qrCodeListView.setAdapter(defaultAdapter);
//
//                    // Find the position for the original list by the selected name from search
//                    for (int pos = 0; pos < nameList.size(); pos++) {
//                        if (nameList.get(pos).equals(selectedName)) {
//                            position = pos;
//                            break;
//                        }
//                    }
//
//                    // Create a handler. This is necessary because the adapter has just been set on the list again and
//                    // the list might not be finished setting the adapter by the time we perform setSelection.
//
//                    final int finalPosition = position;
//                     handler = new Handler() {
//                         @Override
//                         public void close() {
//                             qrCodeListView.setSelection(finalPosition);
//                         }
//
//                         @Override
//                         public void flush() {
//
//                         }
//
//                         @Override
//                         public void publish(LogRecord record) {
//
//
//                         }
//                     };
//
//
//
 //                   searchView.setQuery("", true);
//                }
//            });
//
       }
    }

    private void showQrCodeImageInDialog(String text , String textcode,String text_sell , String text_sample , String text_number , Bitmap qrCodeImageBitmap) {
        ImageView qrCodeImageView = new ImageView(getContext());
        qrCodeImageView.setImageBitmap(qrCodeImageBitmap);

        LinearLayout layout = new LinearLayout(getContext());
        layout.setGravity(Gravity.CENTER);
        layout.addView(
                qrCodeImageView,
                new LinearLayout.LayoutParams(700, 700)
        );

        new AlertDialog.Builder(getContext())
                .setTitle( "รหัส" + textcode +"  "+  " ประเภทสินค้า  "+ text_sample  +
                "ชื่อสินค้า" + text +   "ราคา" + text_sell  +  "จำนวน" + text_number + "ชิ้น")

                .setView(layout)
                .show();
    }

    private void updateListView() {
        QRCodeDAO db = new QRCodeDAO(getContext());
        ArrayList<QRCodeDAO.QrItem> itemList = db.readAll();

        mAdapter.clear();
        mAdapter.addAll(itemList);
        //mAdapter.notifyDataSetChanged();
    }


    private static final int MENU_ITEM_DELETE_ALL = 1;
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuItem deleteItem = menu.add(Menu.NONE, MENU_ITEM_DELETE_ALL, Menu.NONE, "Delete All");
//        deleteItem.setIcon(R.drawable.ic_delete_white_24dp);
//        deleteItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_ITEM_DELETE_ALL:
                deleteAllData();
                return true;
            default:
                return false;
        }
    }

    private void deleteAllData() {
        new AlertDialog.Builder(getContext())
                .setTitle("Delete All QR Code Data")
                .setMessage("Are you sure?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        QRCodeDAO db = new QRCodeDAO(getContext());
                        db.deleteAll();
                        updateListView();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    public boolean onClose() {

        qrCodeListView.setAdapter(defaultAdapter);

        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        displayResults(query + "*");
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (!newText.isEmpty()){
            displayResults(newText + "*");
        } else {
            qrCodeListView.setAdapter(defaultAdapter);
        }

        return false;
    }


    private class QrCodeListAdapter extends ArrayAdapter<QRCodeDAO.QrItem> {

        private final String TAG = QrCodeListAdapter.class.getSimpleName();

        private Context mContext;
        private int mLayoutResId;
        private ArrayList<QRCodeDAO.QrItem> mQrItemList;

        public QrCodeListAdapter(Context context, int layoutResId, ArrayList<QRCodeDAO.QrItem> qrItemList) {
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
            holder.qrCodeImageView.setImageBitmap(mQrItemList.get(position).qrCodeBitmap);
            holder.qrCodeTextView.setText(mQrItemList.get(position).text);
            holder.qrCodeTextViewcode.setText(mQrItemList.get(position).textcode);

            holder.qrCodeTextViewcode_save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mQrItemList.get(position);  //เรียกตาม Piction

                    Log.e("qrCodeTextView", mQrItemList.get(position).text);
                    Log.e("click", "click");


                    Toast.makeText(getContext(), " Create Completed... " + position,
                            Toast.LENGTH_LONG).show();

                    View v1 = qrCodeListView.findViewById(R.id.qr_code_image_view);
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