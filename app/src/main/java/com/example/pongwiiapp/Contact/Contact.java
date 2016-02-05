package com.example.pongwiiapp.Contact;

/**
 * Created by pongwiiApp on 16/1/2559.
 */
public class Contact {


    public static final String TABLE_NAME = "qrcode";
    public static final String COL_ID = "_id";

    public static final String COL_TEXT_CODE = "text_code";
    public static final String COL_TEXT = "text";
    public static final String COL_TEXT_SELL = "text_sell";

    public static final String COL_TEXT_SAMPLE = "text_sample";
    public static final String COL_TEXT_NUMBER = "text_number";

    public static final String COL_QR_CODE_BITMAP = "qr_code_bitmap";


    int _id;
    String _text_code;
    String _text;
    String _text_sell ;
    String _text_sample ;
    String _text_number ;
    String _qr_code_bitmap ;


    public Contact() {

    }


    public Contact(int id, String text_code, String text,String text_sell , String text_sample , String text_number ,String qr_code_bitmap ) {
        this._id = id;
        this._text_code = text_code;
        this._text = text;
        this._text_sell =text_sell ;
        this._text_sample =text_sample ;
        this._text_number = text_number ;
        this._qr_code_bitmap =qr_code_bitmap ;

    }


    public Contact(String name,String text_code, String text,String text_sell , String text_sample , String text_number ,String qr_code_bitmap) {
        this._text_code = text_code;
        this._text = text;
        this._text_sell =text_sell ;
        this._text_sample =text_sample ;
        this._text_number = text_number ;
        this._qr_code_bitmap =qr_code_bitmap ;

    }

    public int getID() {
        return this._id;
    }

    public void setID(int id) {
        this._id = id;
    }

    public String getTextCode() {
        return this._text_code;
    }

    public void setTextCode(String TextCode) {
        this._text_code = TextCode;
    }




    public String getTextSell() {
        return this._text_sell;
    }

    public void setTextSell(String textsell) {
        this._text_sell = textsell;
    }

    public String getTextSample() {
        return this._text_sample;
    }

    public void setTextSimple (String textsample) {
        this._text_sample = textsample;
    }



    public String getTextNumber() {
        return this._text_number;
    }

    public void setTextNumber(String textnumber) {
        this._text_number = textnumber;
    }

    public String get_qr_code_bitmap() {
        return this._qr_code_bitmap;
    }

    public void set_qr_code_bitmap(String qr_code_bitmap) {
        this._qr_code_bitmap = qr_code_bitmap;
    }


}
