package com.needfood.kh.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.needfood.kh.Constructor.InfoConstructor;
import com.needfood.kh.Constructor.Language;
import com.needfood.kh.Constructor.ListMN;
import com.needfood.kh.SupportClass.DBHandle;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Vi on 6/29/2017.
 */

public class DataHandle extends SQLiteOpenHelper {
    public static final String TAG = DBHandle.class.getSimpleName();
    public static final String DB_NAME = "needfooddt.db";
    public static final int DB_VERSION = 1;


    public static final String MONEYTB = "money";
    public static final String IDMN = "idmn";
    public static final String NAMEMN = "namemn";

    public static final String INFO = "infomation";
    public static final String FULLNAME = "fullName";
    public static final String EMAIL = "email";
    public static final String PHONE = "fone";
    public static final String PASS = "pass";
    public static final String ADDRESS = "address";
    public static final String IDINFO = "idin";
    public static final String ACCESSTK = "accessToken";

    public static final String CHECK_LAN = "language";
    public static final String ID_LANGUAGE = "id_language";

    public DataHandle(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
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
                        ACCESSTK + " TEXT NOT NULL" +
                        ");";
        String CREATE_TABLE_LAN =
                "CREATE TABLE " + CHECK_LAN + "(" +
                        ID_LANGUAGE + " TEXT" +
                        ");";

        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_INFO);
        db.execSQL(CREATE_TABLE_LAN);
        Log.d("TAG", TAG);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + MONEYTB);
        db.execSQL("DROP TABLE IF EXISTS" + INFO);
        db.execSQL("DROP TABLE IF EXISTS" + CHECK_LAN);
        onCreate(db);
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
            do {
                InfoConstructor contact = new InfoConstructor();
                contact.setFullname(cursor.getString(0));
                contact.setEmail(cursor.getString(1));
                contact.setFone(cursor.getString(2));
                contact.setPass(cursor.getString(3));
                contact.setAddress(cursor.getString(4));
                contact.setId(cursor.getString(5));
                contact.setAccesstoken(cursor.getString(6));
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
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

    public boolean updateinfo(String name, String email, String fone, String pass, String addr, String id, String token) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FULLNAME, name);
        values.put(EMAIL, email);
        values.put(PHONE, fone);
        values.put(PASS, pass);
        values.put(ADDRESS, addr);
        values.put(IDINFO, id);
        values.put(ACCESSTK, token);
        db.update(INFO, values, IDINFO + " = ?", new String[]{id});
        return true;
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

    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(MONEYTB, null, null);
        db.close();
    }


}
