package com.example.pongwiiapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.pongwiiapp.Contact.Contact;
import com.example.pongwiiapp.slidingtablayout.MainActivity;
import com.example.pongwiiapp.slidingtablayout.R;
import com.example.pongwiiapp.slidingtablayout.Tab1;
import com.example.pongwiiapp.slidingtablayout.Tab2;

import net.glxn.qrgen.android.QRCode;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;


public class QRCodeDAO {

    private static final String TAG = QRCodeDAO.class.getSimpleName();

    private static final String DATABASE_NAME = "qrcode.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "qrcode";
    public static final String COL_ID = "_id";
    public static final String COL_TEXT_CODE = "text_code";
    public static final String COL_TEXT = "text";
    public static final String COL_TEXT_SELL = "text_sell";
    public static final String COL_TEXT_SAMPLE = "text_sample";
    public static final String COL_TEXT_NUMBER = "text_number";
    public static final String COL_QR_CODE_BITMAP = "qr_code_bitmap";

    public static final String TABLE_NAME_2 = "product";
    public static final String COL_TEXT_PRODUCT_CODE = "product_code";
    public static final String COL_TEXT_PRODUCT_NAME = "product_name";
    public static final String COL_TEXT_PRODUCT_DATAIL = "product_detail";


    public static Cursor cursor1;

    private static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
            + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_TEXT + " text, "
            + COL_TEXT_CODE + " text_code, "
            + COL_TEXT_SELL + " text_sell, "
            + COL_TEXT_SAMPLE + " text_sample, "
            + COL_TEXT_NUMBER + " text_number, "
            + COL_QR_CODE_BITMAP + " BLOB"
            + ")";



    private static final String SQL_CREATE_TABLE_2 = "CREATE TABLE " + TABLE_NAME_2 + "("
            + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_TEXT_PRODUCT_CODE + " product_code, "
            + COL_TEXT_PRODUCT_NAME + " product_name, "
            + COL_TEXT_PRODUCT_DATAIL + " product_detail, "

            + ")";



    private static String  query;
    private Context mContext;
    private static DatabaseHelper mDbHelper;
    private static SQLiteDatabase mDatabase;

    public QRCodeDAO(Context context) {
        context = context.getApplicationContext();
        mContext = context;

        if (mDbHelper == null) {
            mDbHelper = new DatabaseHelper(context);
        }
        mDatabase = mDbHelper.getWritableDatabase();
    }


    // urlString_code,num[+position2],urlString, urlString_code_sell ,num[+position2],qrCodeBOS.toByteArray())

    // if (db.insert( urlString_code,num[+position],urlString, urlString_code_sell ,num[+position2],qrCodeBOS.toByteArray()) > -1) {




    public long insert(String text_code ,String text ,String text_sell ,String position  ,String position2,  byte[] qrCodeImageByteArray) {
        ContentValues cv = new ContentValues();

        cv.put(COL_TEXT_CODE, text_code);
        cv.put(COL_TEXT, text);
        cv.put(COL_TEXT_SELL, text_sell);
        cv.put(COL_TEXT_SAMPLE, position);
        cv.put(COL_TEXT_NUMBER, position2);

        cv.put(COL_QR_CODE_BITMAP, qrCodeImageByteArray);
        long insertResult = mDatabase.insert(TABLE_NAME, null, cv);


        Log.e("insert", String.valueOf(cv));


        Log.e("insert code ", String.valueOf(text_code));
        Log.e("insert text ", String.valueOf(text));
        Log.e("insert text_sell ", String.valueOf(text_sell));
        Log.e("insert simple  ", String.valueOf(position));
        Log.e("insert number ", String.valueOf(position2));
        //  Log.e("insert code ", String.valueOf(text));
        return insertResult;
    }



    public long insert(String product_code ,String product_name ,String product_detail ) {
        ContentValues cv = new ContentValues();

        cv.put(COL_TEXT_PRODUCT_CODE, product_code);
        cv.put(COL_TEXT_PRODUCT_NAME, product_name);
        cv.put(COL_TEXT_PRODUCT_DATAIL, product_detail);

        long insertResult = mDatabase.insert(TABLE_NAME_2, null, cv);


        Log.e("insert", String.valueOf(cv));


        Log.e("insert product_code ", String.valueOf(product_code));
        Log.e("insert product_name ", String.valueOf(product_name));
        Log.e("insert product_detail ", String.valueOf(product_detail));

        //  Log.e("insert code ", String.valueOf(text));
        return insertResult;
    }


    public long insert(String text  ,String text_code, String text_sell, String text_sample , String text_number, Bitmap qrCodeImageBitmap) {
        return insert(text,text_code ,text_sell,text_sample,text_number, convertBitmapToByteArray(qrCodeImageBitmap));
    }

//    public long insert(String product_code  ,String product_name, String product_detail) {
//        return insert(product_code,product_name ,product_detail);
//    }


    public long insert(QrItem qrItem ) {
        return insert(qrItem.text ,qrItem.textcode  , qrItem.text_sell , qrItem.text_sample , qrItem.text_number ,   qrItem.qrCodeBitmap);
    }

    public ArrayList<QrItem> readAll() {
        ArrayList<QrItem> qrList = new ArrayList<>();

        Cursor cursor = mDatabase.query(TABLE_NAME, null, null, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String text_code = cursor.getString(cursor.getColumnIndex(COL_TEXT_CODE));
                String text = cursor.getString(cursor.getColumnIndex(COL_TEXT));

                String text_sell = cursor.getString(cursor.getColumnIndex(COL_TEXT_SELL));
                String text_semple = cursor.getString(cursor.getColumnIndex(COL_TEXT_SAMPLE));
                String text_numbr = cursor.getString(cursor.getColumnIndex(COL_TEXT_NUMBER));
                byte[] bytes = cursor.getBlob(cursor.getColumnIndex(COL_QR_CODE_BITMAP));

                QrItem item = new QrItem(text_code,text, text_sell,text_semple,text_numbr, convertByteArrayToBitmap(bytes));
                qrList.add(item);
            }
            cursor.close();
        }
        return qrList;
    }



    public ArrayList<QrItem_Product> readAll_product() {
        ArrayList<QrItem_Product> qrList_product = new ArrayList<>();

        Cursor cursor = mDatabase.query(TABLE_NAME, null, null, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String text_code = cursor.getString(cursor.getColumnIndex(COL_TEXT_PRODUCT_CODE));
                String text = cursor.getString(cursor.getColumnIndex(COL_TEXT_PRODUCT_NAME));

                String text_sell = cursor.getString(cursor.getColumnIndex(COL_TEXT_PRODUCT_DATAIL));


                QrItem_Product item = new QrItem_Product(text_code,text, text_sell);
                qrList_product.add(item);
            }
            cursor.close();
        }
        return qrList_product;
    }


    public long createList(String name) {

        ContentValues initialValues = new ContentValues();


        initialValues.put(COL_TEXT, name);

        return mDatabase.insert(TABLE_NAME, null, initialValues);

    }


    public  static void deleteAll() {
        mDatabase.delete(TABLE_NAME, null, null);
    }



    public boolean deleteAllNames() {

        int doneDelete = mDatabase.delete(TABLE_NAME, null , null);
        return doneDelete > 0;

    }


    public  static void delete_byID(int position){

        cursor1 = mDatabase.query(TABLE_NAME, null, null, null, null, null, null);
        cursor1.moveToPosition(position);
        String rowId = cursor1.getString(0);
        //  db.delete(MyDbHelper.TABLE_NAME, "_id = ?", new String[] { rowId });
        mDatabase.delete(QRCodeDAO.TABLE_NAME, "_id = ?",  new String[] {rowId} );
        // mDatabase.delete(TABLE_NAME,COL_ID +"="+ id, null);


        // cursor1.requery();
//        adapter.notifyDataSetChanged();

        Log.e("click del", String.valueOf(COL_ID));
    }



    public Cursor getCountry(String id){

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        queryBuilder.setTables(TABLE_NAME);

        Cursor c = queryBuilder.query(mDbHelper.getReadableDatabase(),
                new String[] { "_id", "text_code", "text", "text_sell","text_sample","text_number","qr_code_bitmap" } ,
                "_id = ?", new String[] { id } , null, null, null ,"1"
        );

        return c;
    }



    public static Cursor searchByInputText(String inputText) throws SQLException {

     //   cursor1 = mDatabase.query(TABLE_NAME, null, null, null, null, null, null);


        String query = "SELECT docid as _id," +
                COL_TEXT_CODE +  " from " + TABLE_NAME +
                " where " + COL_TEXT_CODE + " MATCH '" + inputText + "';";

        Cursor mCursor = mDatabase.rawQuery(query,null);

//        Cursor c = mDatabase.query (TABLE_NAME,
//                new String[] {  COL_ID, COL_TEXT , COL_TEXT_CODE , COL_TEXT_SELL , COL_TEXT_SAMPLE ,COL_TEXT_NUMBER,COL_QR_CODE_BITMAP},
//                COL_TEXT_CODE + " LIKE ? " ,
//                new String[] { "'%" + inputText + "%'"},
//                null, null, null);
//        c.requery();


//         query = "SELECT docid as _id," +
//                 COL_TEXT_CODE + " from " + TABLE_NAME +
//                " where " + COL_TEXT_CODE + " MATCH '" + inputText + "';";
//        // Cursor mCursor = mDatabase.rawQuery(query, null);

        Log.e("inputText",inputText);
        Log.e("COL_ID",QRCodeDAO.COL_TEXT_CODE);
//        Log.e("query",query);
//        if (cursor1 != null) {
//            cursor1.moveToFirst();
//        }
//        return cursor1;

        if (mCursor != null) {
            mCursor.moveToFirst();
        }

        else{
            mCursor.moveToFirst();
        }
        return mCursor;
    }


    public   static  String serach1(String urlString_code) {

        cursor1 = mDatabase.query(TABLE_NAME, new String[] { COL_ID,
                        COL_TEXT_CODE, COL_TEXT,COL_TEXT_SELL, COL_TEXT_SAMPLE,COL_TEXT_NUMBER,COL_QR_CODE_BITMAP }, COL_ID + "=?",
                new String[] { String.valueOf(urlString_code) }, null, null, null, null);
        if (cursor1 != null)
            cursor1.moveToFirst();
        Log.e("selectionArgs", String.valueOf(urlString_code));
        Log.e("selectionArgs_id", cursor1.toString());
//        Contact contact = new Contact(Integer.parseInt(cursor1.getString(0)),
//                cursor1.getString(1), cursor1.getString(2),cursor1.getString(3),cursor1.getString(4),cursor1.getString(5),cursor1.getString(6));

        cursor1.requery();

        return urlString_code;
    }

    public  static void serach( String  urlString_code){
        // cursor1 = mDatabase.query(TABLE_NAME, null, null, null, null, null, null);
        //   String rowId = cursor1.getString(0);
        // String selectionArgs = { urlString_code };
        cursor1 = mDatabase.rawQuery("SELECT * FROM qrcode WHERE text_code MATCH ?", new String[]{urlString_code});
        // String sql = " SELECT * FROM qrcode WHERE text_code LIKE '%"+ urlString_code+"%'";
        Log.e("selectionArgs", String.valueOf(urlString_code));
        Log.e("sql serach", String.valueOf(mDatabase.rawQuery("SELECT * FROM qrcode WHERE text_code MATCH ?", new String[]{urlString_code})));

        cursor1.requery();

    }

    private byte[] convertBitmapToByteArray(Bitmap bitmap) {
/*
        int size = qrCodeBitmap.getRowBytes() * qrCodeBitmap.getHeight();
        ByteBuffer buffer = ByteBuffer.allocate(size);
        qrCodeBitmap.copyPixelsToBuffer(buffer);

        byte[] bytes = new byte[size];
        buffer.get(bytes, 0, bytes.length);
*/

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    private Bitmap convertByteArrayToBitmap(byte[] bytes) {
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {

        private Context mContext;
        private String urlString;

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            mContext = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_TABLE);
            db.execSQL(SQL_CREATE_TABLE_2);



        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            return;
        }
    }


    public QRCodeDAO open() throws SQLException {
        mDbHelper = new DatabaseHelper(mContext);
        mDatabase = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }

    public static class QrItem {
        public final String text;
        public final String textcode;
        public final String text_sell;
        public final String text_sample;
        public final String text_number;
        public final Bitmap qrCodeBitmap;

        public QrItem(String textcode ,String text, String text_sell, String text_sample,   String text_number, Bitmap qrCodeBitmap) {
            this.textcode = textcode;
            this.text = text;

            this.text_sell = text_sell ;
            this.text_sample = text_sample ;
            this.text_number = text_number ;
            this.qrCodeBitmap = qrCodeBitmap;
        }
    }


    public static class QrItem_Product {
        public final String product_code;
        public final String product_name;
        public final String product_detail;


        public QrItem_Product(String product_code ,String product_name, String product_detail) {
            this.product_code = product_code;
            this.product_name = product_name;

            this.product_detail = product_detail ;

        }
    }
}
