package com.nf.vi.needfoodshipper.database;

/**
 * Created by Minh Nhat on 3/31/2017.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import com.nf.vi.needfoodshipper.Constructor.ListLangContructor;
import com.nf.vi.needfoodshipper.Constructor.ListUserContructor;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Vi on 10/14/2016.
 */
public class DBHandle extends SQLiteOpenHelper {
    public static final String TAG = DBHandle.class.getSimpleName();
    public static final String DB_NAME = "mytest.db";
    public static final int DB_VERSION = 2;

    public static final String USER_TABLE = "user";
    public static final String USER_TABLE1 = "lang";
    public static final String BBNAME = "fullName";
    public static final String EMAIL = "email";
    public static final String PHONE = "phone";
    public static final String ADDRESS = "address";
    public static final String PASS = "pass";
    public static final String ID = "id";
    public static final String BIRTHDAY = "birthday";
    public static final String CODE = "code";
    public static final String SKYPE = "skype";
    public static final String FACEBOOK = "facebook";
    public static final String DESCRIPTION = "description";
    public static final String LANGUE = "langue";


    public static final String ACCESSTOKEN = "accessToken";


    public DBHandle(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_USER =
                "CREATE TABLE " + USER_TABLE + "(" +
                        ID + " TEXT PRIMARY KEY NOT NULL," +
                        BBNAME + " TEXT NOT NULL," +
                        PASS + " TEXT NOT NULL," +
                        EMAIL + " TEXT NOT NULL," +
                        BIRTHDAY + " TEXT," +
                        ADDRESS + " TEXT NOT NULL," +
                        PHONE + " TEXT NOT NULL," +
                        CODE + " TEXT NOT NULL," +
                        SKYPE + " TEXT NOT NULL," +
                        FACEBOOK + " TEXT NOT NULL," +
                        DESCRIPTION + " TEXT NOT NULL," +

                        ACCESSTOKEN + " TEXT" + " );";

        String CREATE_TABLE_LANG =
                "CREATE TABLE " + USER_TABLE1 + "(" +
                        ID + " TEXT PRIMARY KEY NOT NULL," +

                        LANGUE + " TEXT NOT NULL" + ");";


        db.execSQL(CREATE_TABLE_LANG);
        db.execSQL(CREATE_TABLE_USER);
        Log.d("TAG", TAG);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            Log.w("MyAppTag", "Updating database from version " + oldVersion + " to "
                    + newVersion + " .Existing data will be lost.");

            db.execSQL("DROP TABLE IF EXISTS" + USER_TABLE);
            db.execSQL("DROP TABLE IF EXISTS" + USER_TABLE1);
            onCreate(db);
        }
    }

    public void addUser(ListUserContructor listu) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ID, listu.getId());
        values.put(BBNAME, listu.getFullName());// Contact Name
        values.put(PASS, listu.getPass());
        values.put(EMAIL, listu.getEmail());
        values.put(BIRTHDAY, listu.getBirthday());
        values.put(ADDRESS, listu.getAddress());
        values.put(PHONE, listu.getFone());
        values.put(CODE, listu.getCode());
        values.put(SKYPE, listu.getSkype());
        values.put(FACEBOOK, listu.getFacebook());
        values.put(DESCRIPTION, listu.getDescription());

        values.put(ACCESSTOKEN, listu.getAccessToken());


        db.insert(USER_TABLE, null, values);
        db.close(); // Closing database connection
    }

    public void addLang(ListLangContructor listl) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ID, listl.getId());

        values.put(LANGUE, listl.getLang());


        db.insert(USER_TABLE1, null, values);
        db.close(); // Closing database connection
    }

    //
    public boolean updateinfo(String id, String name, String email, String diachi, String ngaysinh, String skype, String facebook, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ID, id);
        values.put(BBNAME, name);
        values.put(EMAIL, email);
        values.put(ADDRESS, diachi);
        values.put(BIRTHDAY, ngaysinh);
        values.put(SKYPE, skype);
        values.put(FACEBOOK, facebook);
        values.put(DESCRIPTION, description);

        db.update(USER_TABLE, values, "ID = ?", new String[]{id});
        return true;
    }

    public boolean updateinfo1(String id, String langue) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ID, id);
        values.put(LANGUE, langue);


        db.update(USER_TABLE1, values, "ID = ?", new String[]{id});
        return true;
    }


    public List<ListUserContructor> getAllUser() {
        List<ListUserContructor> contactList = new ArrayList<ListUserContructor>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + USER_TABLE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ListUserContructor contact = new ListUserContructor();
                contact.setId(cursor.getString(0));
                contact.setFullName(cursor.getString(1));
                contact.setPass(cursor.getString(2));
                contact.setEmail(cursor.getString(3));
                contact.setBirthday(cursor.getString(4));
                contact.setAddress(cursor.getString(5));
                contact.setFone(cursor.getString(6));
                contact.setCode(cursor.getString(7));
                contact.setSkype(cursor.getString(8));
                contact.setFacebook(cursor.getString(9));
                contact.setDescription(cursor.getString(10));

                contact.setAccessToken(cursor.getString(11));


                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }

    public List<ListLangContructor> getAllLang() {
        List<ListLangContructor> contactList2 = new ArrayList<ListLangContructor>();
        // Select All Query
        String selectQuery1 = "SELECT  * FROM " + USER_TABLE1;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor1 = db.rawQuery(selectQuery1, null);

        // looping through all rows and adding to list
        if (cursor1.moveToFirst()) {
            do {
                ListLangContructor contact2 = new ListLangContructor();
                contact2.setId(cursor1.getString(0));
                contact2.setLang(cursor1.getString(1));


                // Adding contact to list
                contactList2.add(contact2);
            } while (cursor1.moveToNext());
        }

        // return contact list
        return contactList2;
    }

    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(USER_TABLE, null, null);
        db.close();
    }


}