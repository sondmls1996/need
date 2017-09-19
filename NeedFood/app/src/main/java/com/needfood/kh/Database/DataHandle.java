package com.needfood.kh.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.needfood.kh.Adapter.ProductDetail.CheckConstructor;
import com.needfood.kh.Constructor.InfoConstructor;
import com.needfood.kh.Constructor.Language;
import com.needfood.kh.Constructor.ListMN;
import com.needfood.kh.Constructor.NotiConstructor;
import com.needfood.kh.Constructor.ShareConstructor;
import com.needfood.kh.SupportClass.DBHandle;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Vi on 6/29/2017.
 */

public class DataHandle extends SQLiteOpenHelper {
    public static final String TAG = DBHandle.class.getSimpleName();
    public static final String DB_NAME = "needfooddt.db";
    public static final int DB_VERSION = 2;

    public static final String TABLEPRD = "tableprd";
    public static final String TABLESHARE = "tbshare";
    public static final String IDS = "idshare";
    public static final String DAYSHARE = "dayshare";
    public static final String STTSHARE = "sttshare";
    public static final String IDPRD = "idprd";
    public static final String TITLEPRD = "title";
    public static final String PRICE = "price";
    public static final String QUAN = "quanlity";
    public static final String TICKKM = "tickKM";
    public static final String TICKKMPER = "tickKMper";
    public static final String TICKKMMN = "tickKMMN";
    public static final String BARCODE = "barcode";
    public static final String CODE = "code";
    public static final String TYPEMN = "typemn";
    public static final String NOTE = "note";
    public static final String TOTALMN = "totalmn";
    public static final String MONEYTB = "money";
    public static final String IDMN = "idmn";
    public static final String NAMEMN = "namemn";
    public static final String INFO = "infomation";
    public static final String FULLNAME = "fullName";
    public static final String TYPE = "type";
    public static final String EMAIL = "email";
    public static final String PHONE = "fone";
    public static final String PASS = "pass";
    public static final String ADDRESS = "address";
    public static final String IDINFO = "idin";
    public static final String ACCESSTK = "accessToken";
    public static final String COIN = "coin";
    public static final String BIRTHDAY = "birthday";
    public static final String SEX = "sex";

    public static final String CHECK_LAN = "language";
    public static final String ID_LANGUAGE = "id_language";

    public static final String TABLE_NOTI = "tbnoti";
    public static final String TIT_NOTI = "titnoti";
    public static final String IMAGE_NOTI = "imgnoti";
    public static final String TIME_NOTI = "timenoti";
    public static final String ID_NOTI = "idnoti";

    public DataHandle(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TB_SHARE = "CREATE TABLE " + TABLESHARE + "(" +
                IDS + " TEXT," +
                DAYSHARE + " TEXT," +
                STTSHARE + " TEXT" +
                ");";
        String CREATE_TABLE_CART = "CREATE TABLE " + TABLEPRD + "(" +
                QUAN + " TEXT," +
                PRICE + " TEXT," +
                TICKKM + " TEXT," +
                TICKKMPER + " TEXT," +
                TICKKMMN + " TEXT," +
                BARCODE + " TEXT," +
                CODE + " TEXT," +
                TITLEPRD + " TEXT," +
                NOTE + " TEXT," +
                IDPRD + " TEXT PRIMARY KEY," +
                TYPEMN + " TEXT" +
                ");";
        String CREATE_TABLE_USER =
                "CREATE TABLE " + MONEYTB + "(" +
                        IDMN + " TEXT NOT NULL," +
                        NAMEMN + " TEXT" +
                        ");";
        String CREATE_TABLE_INFO =
                "CREATE TABLE " + INFO + "(" +
                        FULLNAME + " TEXT NOT NULL," +
                        EMAIL + " TEXT NOT NULL," +
                        PHONE + " TEXT NOT NULL," +
                        PASS + " TEXT NOT NULL," +
                        ADDRESS + " TEXT NOT NULL," +
                        IDINFO + " TEXT NOT NULL PRIMARY KEY," +
                        ACCESSTK + " TEXT NOT NULL," +
                        COIN + " TEXT NOT NULL," +
                        TYPE + " TEXT NOT NULL," +
                        BIRTHDAY + " TEXT NOT NULL," +
                        SEX + " TEXT NOT NULL" +

                        ");";
        String CREATE_TABLE_LAN =
                "CREATE TABLE " + CHECK_LAN + "(" +
                        ID_LANGUAGE + " TEXT" +
                        ");";
        String CREATE_TABLE_NOTI =

                "CREATE TABLE " + TABLE_NOTI + "(" +
                        ID_NOTI + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        TIT_NOTI + " TEXT NOT NULL," +
                        IMAGE_NOTI + " TEXT NOT NULL," +
                        TIME_NOTI + " TEXT NOT NULL" +
                        ");";

        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_INFO);
        db.execSQL(CREATE_TABLE_LAN);
        db.execSQL(CREATE_TABLE_NOTI);
        db.execSQL(CREATE_TABLE_CART);
        Log.d("TAG", TAG);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MONEYTB);
        db.execSQL("DROP TABLE IF EXISTS " + INFO);
        db.execSQL("DROP TABLE IF EXISTS " + CHECK_LAN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTI);
        db.execSQL("DROP TABLE IF EXISTS " + TABLEPRD);
        db.execSQL("DROP TABLE IF EXISTS " + TABLESHARE);
        onCreate(db);
    }
    public void addPDR(CheckConstructor listu) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(IDPRD, listu.getId()); // Contact Name
        values.put(TITLEPRD, listu.getTitle()); // Contact Phone
        values.put(PRICE, listu.getPrice()); // Contact Phone
        values.put(QUAN, listu.getQuanli()); // Contact Phone
        values.put(TICKKM, listu.getTickkm()); // Contact Phone
        values.put(TICKKMPER, listu.getTickkm2()); // Contact Phone
        values.put(TICKKMMN, listu.getTickkm3()); // Contact Phone
        values.put(BARCODE, listu.getBarcode()); // Contact Phone
        values.put(CODE, listu.getCode()); // Contact Phone
        values.put(TYPEMN, listu.getTypeid()); // Contact Phone
        values.put(NOTE, listu.getNote()); // Contact Phone// Contact Phone


        db.insert(TABLEPRD, null, values);
        db.close(); // Closing database connection
    }
    public void addShare(ShareConstructor listu) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(IDS, listu.getIds()); // Contact Name
        values.put(DAYSHARE, listu.getDays()); // Contact Phone
        values.put(STTSHARE, listu.getSttshare()); // Contact Phone

        db.insert(TABLESHARE, null, values);
        db.close(); // Closing database connection
    }
    public boolean updatePrd(String id,String qua) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(QUAN, qua);

        db.update(TABLEPRD, values, IDPRD + " = ?", new String[]{id});
        return true;
    }
    public boolean updateShare(String id,String days,String stt) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DAYSHARE, days);
        values.put(STTSHARE, stt);

        db.update(TABLESHARE, values, IDS + " = ?", new String[]{id});
        return true;
    }

    public List<CheckConstructor> getPRDID(String id) {
        List<CheckConstructor> contactList = new ArrayList<CheckConstructor>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLEPRD + " WHERE " + IDPRD + " = " + id;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                CheckConstructor contact = new CheckConstructor();
                contact.setQuanli(cursor.getString(0));
                contact.setPrice(cursor.getString(1));
                contact.setTickkm(cursor.getString(2));
                contact.setTickkm2(cursor.getString(3));
                contact.setTickkm3(cursor.getString(4));
                contact.setBarcode(cursor.getString(5));
                contact.setCode(cursor.getString(6));
                contact.setTitle(cursor.getString(7));
                contact.setNote(cursor.getString(8));
                contact.setId(cursor.getString(9));
                contact.setTypeid(cursor.getString(10));

                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }
        cursor.close();

        // return contact list　
        return contactList;
    }

    public boolean isProductEmpty(String id) {
        boolean empty = false;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLEPRD + " WHERE "+ IDPRD +" = '" + id + "'", null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) { // This will get the integer value of the COUNT(*)
            empty=false;
        }else{
            empty=true;
        }
        return empty;
    }
    public void deletePrd(String id) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLEPRD, IDPRD + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }
    public void deleteAllPRD() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLEPRD, null, null);
        db.close();
    }
    public List<CheckConstructor> getPrd() {
        List<CheckConstructor> contactList = new ArrayList<CheckConstructor>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLEPRD;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            if (cursor.getCount() > 0) {
                do {
                    CheckConstructor contact = new CheckConstructor();
                    contact.setQuanli(cursor.getString(0));
                    contact.setPrice(cursor.getString(1));
                    contact.setTickkm(cursor.getString(2));
                    contact.setTickkm2(cursor.getString(3));
                    contact.setTickkm3(cursor.getString(4));
                    contact.setBarcode(cursor.getString(5));
                    contact.setCode(cursor.getString(6));
                    contact.setTitle(cursor.getString(7));
                    contact.setNote(cursor.getString(8));
                    contact.setId(cursor.getString(9));
                    contact.setTypeid(cursor.getString(10));
                    // Adding contact to list
                    contactList.add(contact);
                } while (cursor.moveToNext());

            }
            cursor.close();
        }
        // return contact list　
        return contactList;
    }
    public void addMN(ListMN listu) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NAMEMN, listu.getMn()); // Contact Name
        values.put(IDMN, listu.getIdmn()); // Contact Phone

        db.insert(MONEYTB, null, values);
        db.close(); // Closing database connection
    }

    public void addInfo(InfoConstructor in) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FULLNAME, in.getFullname()); // Contact Name
        values.put(EMAIL, in.getEmail()); // Contact Email
        values.put(PHONE, in.getFone()); // Contact Phone
        values.put(PASS, in.getPass()); // Contact Pass
        values.put(ADDRESS, in.getAddress()); // Contact Address
        values.put(IDINFO, in.getId()); // Contact id
        values.put(ACCESSTK, in.getAccesstoken()); // Contact accessToken
        values.put(COIN, in.getCoin());
        values.put(TYPE, in.getType());
        values.put(BIRTHDAY, in.getBirtday());
        values.put(SEX, in.getSex());
        db.insert(INFO, null, values);
        db.close(); // Closing database connection
    }

    public void addCheckLan(Language lan) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ID_LANGUAGE, lan.getId());
        db.insert(CHECK_LAN, null, values);
        db.close();
    }

    public void addNoti(String tit, String img, String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TIT_NOTI, tit);
        values.put(IMAGE_NOTI, img);
        values.put(TIME_NOTI, time);
        db.insert(TABLE_NOTI, null, values);
        db.close();
    }


    public List<NotiConstructor> getNoti() {
        List<NotiConstructor> contactList = new ArrayList<NotiConstructor>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_NOTI;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        NotiConstructor contact = new NotiConstructor();
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            if (cursor.getCount() > 0) {
                do {

                    contact.setId(cursor.getString(0));
                    contact.setTitle(cursor.getString(1));
                    contact.setImg(cursor.getString(2));
                    contact.setTime(cursor.getString(3));

                    // Adding contact to list
                    contactList.add(contact);
                } while (cursor.moveToNext());
            }
        }
        cursor.close();

        // return contact list　
        return contactList;
    }



    public List<ListMN> getMNid(String id) {
        List<ListMN> contactList = new ArrayList<ListMN>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + MONEYTB + " WHERE " + IDMN + " = " + id;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ListMN contact = new ListMN();
                contact.setIdmn(cursor.getString(0));
                contact.setMn(cursor.getString(1));

                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }
        cursor.close();

        // return contact list　
        return contactList;
    }



    public List<InfoConstructor> getAllInfor() {
        List<InfoConstructor> contactList = new ArrayList<InfoConstructor>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + INFO;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            if (cursor.getCount() > 0) {
                do {
                    InfoConstructor contact = new InfoConstructor();
                    contact.setFullname(cursor.getString(0));
                    contact.setEmail(cursor.getString(1));
                    contact.setFone(cursor.getString(2));
                    contact.setPass(cursor.getString(3));
                    contact.setAddress(cursor.getString(4));
                    contact.setId(cursor.getString(5));
                    contact.setAccesstoken(cursor.getString(6));
                    contact.setCoin(cursor.getString(7));
                    contact.setType(cursor.getString(8));
                    contact.setBirtday(cursor.getString(9));
                    contact.setSex(cursor.getString(10));
                    // Adding contact to list
                    contactList.add(contact);
                } while (cursor.moveToNext());
            }
        }
        cursor.close();

        // return contact list　
        return contactList;
    }

    public List<ListMN> getAllMN() {
        List<ListMN> contactList = new ArrayList<ListMN>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + MONEYTB;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ListMN contact = new ListMN();
                contact.setIdmn(cursor.getString(0));
                contact.setMn(cursor.getString(1));

                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }
        cursor.close();

        // return contact list　
        return contactList;
    }

    public List<Language> getLan() {
        List<Language> contactList = new ArrayList<Language>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + CHECK_LAN;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Language contact = new Language();
                contact.setId(cursor.getString(0));
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }
        cursor.close();

        // return contact list　
        return contactList;
    }

    public boolean updateinfo(String name, String email, String addr, String id, String coin,String birth) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FULLNAME, name);
        values.put(EMAIL, email);
        values.put(ADDRESS, addr);
        values.put(COIN, coin);
        values.put(BIRTHDAY, birth);
        db.update(INFO, values, IDINFO + " = ?", new String[]{id});
        return true;
    }


    public int getNotesCount() {
        String countQuery = "SELECT  * FROM " + TABLE_NOTI;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public boolean isMoneyEmpty() {
        boolean empty = false;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT (*) FROM " + MONEYTB, null);
        if (cursor != null) {
            cursor.moveToFirst();
            if (cursor.getInt(0) == 0) {
                empty = true;
            } else {
                empty = false;
            }
        }
        return empty;
    }

    public void deleteInfo() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(INFO, null, null);
        db.close();
    }

    public void deleteNote(int id) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NOTI, ID_NOTI + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }


    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLEPRD,null,null);
        db.delete(TABLE_NOTI,null,null);

        db.close();
    }


}
