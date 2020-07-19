package com.example.androidlabs;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatRoomActivity extends AppCompatActivity {

    static final String DB_NAME = "Message DB";
    private ArrayList<Message> list = new ArrayList<Message>();
    private MyListAdapter myAdapter;
    SQLiteDatabase db;
    Fragment dFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        FrameLayout frameLayout = findViewById(R.id.fragmentLocation);
        boolean isPhone = frameLayout==null;


        loadDataFromDatabase();

        ListView myList = findViewById(R.id.listView);
        myList.setAdapter(myAdapter = new MyListAdapter());


        Button sendBtn = findViewById(R.id.send);

        Button receiveBtn = findViewById(R.id.receive);

        EditText editText = findViewById(R.id.editText);


        sendBtn.setOnClickListener(click->{
            String inputText =editText.getText().toString();
            boolean isSend = true;

            ContentValues newRowValues = new ContentValues();
            newRowValues.put(MessageOpenHelper.COL_MESSAGE, inputText );
            newRowValues.put(MessageOpenHelper.COL_IS_SEND,isSend);

            long newId = db.insert(MessageOpenHelper.TABLE_NAME,null,newRowValues);

            list.add(new Message(inputText, isSend,newId));

            editText.setText("");
            myAdapter.notifyDataSetChanged();

        });


        receiveBtn.setOnClickListener(click->{
            String inputText =editText.getText().toString();
            boolean isSend = false;

            ContentValues newRowValues = new ContentValues();
            newRowValues.put(MessageOpenHelper.COL_MESSAGE, inputText );
            newRowValues.put(MessageOpenHelper.COL_IS_SEND,isSend);

            long newId = db.insert(MessageOpenHelper.TABLE_NAME,null,newRowValues);
            list.add(new Message(inputText, isSend, newId));
            editText.setText("");
            myAdapter.notifyDataSetChanged();
        });


        myList.setOnItemLongClickListener((p,b,pos,id)->{
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Do you want to delete this? The row is " + (pos+1) +", database id is " + id)

                    .setPositiveButton("Yes",(click,arg)->{
                        deleteMessage(list.get(pos));
                        list.remove(pos);
                        myAdapter.notifyDataSetChanged();
                        if(!isPhone)
                          this.getSupportFragmentManager().beginTransaction().remove(dFragment).commit();
                    })

                    .setNegativeButton("No",(click,arg)->{})


                    .create().show();

            return true;
        });

        myList.setOnItemClickListener((lists, view, position, id)->{
            Bundle dataToPass = new Bundle();
            dataToPass.putLong("ID", id );
            dataToPass.putString("message", list.get(position).getMessage());
            dataToPass.putBoolean("isSend", list.get(position).isSend());


            if(isPhone){
                Intent nextActivity = new Intent(ChatRoomActivity.this, EmptyActivity.class);
                nextActivity.putExtras(dataToPass); //send data to next activity
                startActivity(nextActivity); //make the transition

        }else //tablet
            {
                dFragment = new DetailsFragment(); //add a DetailFragment
                dFragment.setArguments( dataToPass ); //pass it a bundle for information
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentLocation, dFragment) //Add the fragment in FrameLayout
                        .commit(); //actually load the fragment. Calls onCreate() in DetailFragment
        }
        });


    }

    private void loadDataFromDatabase()
    {
        //get a database connection:
        MessageOpenHelper dbOpener = new MessageOpenHelper(this);
        db = dbOpener.getWritableDatabase(); //This calls onCreate() if you've never built the table before, or onUpgrade if the version here is newer


        // We want to get all of the columns. Look at MyOpener.java for the definitions:
        String [] columns = {MessageOpenHelper.COL_ID, MessageOpenHelper.COL_MESSAGE, MessageOpenHelper.COL_IS_SEND};
        //query all the results from the database:
        Cursor results = db.query(false, MessageOpenHelper.TABLE_NAME, columns, null, null, null, null, null, null);

        //Now the results object has rows of results that match the query.
        //find the column indices:
        int messageColumnIndex = results.getColumnIndex(MessageOpenHelper.COL_MESSAGE);
        int isSendColIndex = results.getColumnIndex(MessageOpenHelper.COL_IS_SEND);
        int idColIndex = results.getColumnIndex(MessageOpenHelper.COL_ID);
        //iterate over the results, return true if there is a next item:
        while(results.moveToNext())
        {
            String messageText = results.getString(messageColumnIndex);
            boolean isSend = !(results.getInt(isSendColIndex) == 0);
            long id = results.getLong(idColIndex);

            //add the new Contact to the array list:
            list.add(new Message(messageText, isSend, id));
        }
        results.moveToPosition(-1);
        //results = db.query(false, MessageOpenHelper.TABLE_NAME, columns, null, null, null, null, null, null);
        printCursor(results, db.getVersion());

    }

    private void printCursor(Cursor c, int version){

        Log.i(DB_NAME, "version: "+version+" Total columns: " + c.getColumnCount() );
        String[] columnNames = c.getColumnNames();
        String names ="" ;
        for(String s : columnNames){
            names = names+ "\t" + s;
        }
        Log.i(DB_NAME, names );

        Log.i(DB_NAME, "Total rows: " + c.getCount());
        while(c.moveToNext())
        {
        String rowOutput = "";

        for(int i =0; i < columnNames.length; i++){
            rowOutput = rowOutput + "\t" + c.getString(i);
        }
        Log.i(DB_NAME, rowOutput );
        }

    }

    private void deleteMessage(Message message)
    {
        db.delete(MessageOpenHelper.TABLE_NAME, MessageOpenHelper.COL_ID + "= ?", new String[] {Long.toString(message.getId())});
    }




    private class MyListAdapter extends BaseAdapter{


        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Message getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return getItem(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            Message mes = list.get(position);
            View newView = null;
            TextView tView = null;
            LayoutInflater inflater = getLayoutInflater();
            if(mes.isSend()){
                newView = inflater.inflate(R.layout.send_row_layout, parent, false);
                tView     = newView.findViewById(R.id.sendText);

            }else{
                newView = inflater.inflate(R.layout.receive_row_layout, parent, false);
                tView = newView.findViewById(R.id.receiveText);
            }

            tView.setText(mes.getMessage());
            return newView;
        }
    }


}