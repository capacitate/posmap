package com.example.junhong.posmap;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Junhong on 2015-09-26.
 */
public class Prompt implements Runnable {
    private String input;
    private Context mContext;
    private GoogleMap mMap;
    private LatLng mLng;

    //DB variables
    private SQLiteDatabase db;
    private DBhandle helper;

    public Prompt(Context pContext){
        mContext = pContext;

        helper = new DBhandle(mContext, "posmap.db", null, 1);
    }

    public Prompt(Context pContext, GoogleMap pMap){
        mContext = pContext;
        mMap = pMap;

        helper = new DBhandle(mContext, "posmap.db", null, 1);
        select_and_show();
    }

    String getValue(){
        return this.input;
    }

    public void setMlng(LatLng pLng){
        mLng = pLng;
    }

    @Override
    public void run() {
        final Dialog dialog = new Dialog(mContext);

        dialog.setContentView(R.layout.custom_dialog);
        dialog.setTitle("Write down within 140 characters");

        //Setting max characters && in order to live character counting text watcher is required
        final EditText inputText = (EditText)dialog.findViewById(R.id.input_msg);
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(140);
        inputText.setFilters(FilterArray);
        final TextView livechar = (TextView)dialog.findViewById(R.id.charaterCount);
        final TextWatcher mLiveWatcher = new TextWatcher(){

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                livechar.setText(String.valueOf(s.length()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        inputText.addTextChangedListener(mLiveWatcher);

        //When push the ok button, app stores on its DB
        Button ok = (Button)dialog.findViewById(R.id.okBtn);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                input = inputText.getText().toString();
                mMap.addMarker(new MarkerOptions().position(mLng).title(input));
                insert(input, mLng);
                dialog.dismiss();
                return;
            }
        });

        //No action for cancel button
        Button cancel = (Button)dialog.findViewById(R.id.cacelBtn);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(inputText, InputMethodManager.SHOW_IMPLICIT);
                //imm.showSoftInputFromInputMethod(inputText, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        dialog.show();
    }

    public void insert(String insert_msg, LatLng insert_position){
        db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("msg", insert_msg);
        values.put("latitude", insert_position.latitude);
        values.put("longitude", insert_position.longitude);

        Log.i("데이터베이스insert", "msg: " + insert_msg + "\tlatitude: " + insert_position.latitude
                + "\tlongitude: " + insert_position.longitude);
        db.insert("posmap", null, values);
    }

    public void update(String updated_msg, LatLng position){
        db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("msg", updated_msg);
        //db.update("posmap", values, "latitude=?", position.latitude);
        //TODO use "and" statement in where condition
    }

    public void delete(LatLng position){
        db = helper.getWritableDatabase();
        //db.delete()
    }

    public void select_and_show(){
        db = helper.getWritableDatabase();
        Cursor c = db.query("posmap", null, null, null, null, null, null);

        //위 결과가 select * from student가 된다. Cursor는 DB 결과를 저장한다.
        //public Cursor query (String table, String[] columns, String selection,
        // String[] selectionArgs, String groupBy, String having, String orderBy)

        while(c.moveToNext()){
            int _id = c.getInt(c.getColumnIndex("_id"));
            String msg_text = c.getString(c.getColumnIndex("msg"));
            double latitude = c.getDouble(c.getColumnIndex("latitude"));
            double longitude = c.getDouble(c.getColumnIndex("longitude"));
            Log.i("데이터베이스 select", "id: " + _id + "\tmsg: " + msg_text +
                    "\tlatitude: " + latitude + "\tlongitude: " + longitude);

            mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title(msg_text));
        }
    }
}
