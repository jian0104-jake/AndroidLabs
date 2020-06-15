package com.example.androidlabs;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ChatRoomActivity extends AppCompatActivity {

    private ArrayList<Message> list = new ArrayList<Message>();
    private MyListAdapter myAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);


        ListView myList = findViewById(R.id.listView);
        myList.setAdapter(myAdapter = new MyListAdapter());


        Button sendBtn = findViewById(R.id.send);

        Button receiveBtn = findViewById(R.id.receive);

        EditText editText = findViewById(R.id.editText);


        sendBtn.setOnClickListener(click->{
            String inputText =editText.getText().toString();
            list.add(new Message(inputText, true));
            editText.setText("");
            myAdapter.notifyDataSetChanged();

        });

        receiveBtn.setOnClickListener(click->{
            String inputText =editText.getText().toString();
            list.add(new Message(inputText, false));
            editText.setText("");
            myAdapter.notifyDataSetChanged();
        });
        myList.setOnItemLongClickListener((p,b,pos,id)->{
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Do you want to delete this? The row is " + (pos+1) +", database id is " + id)

                    .setPositiveButton("Yes",(click,arg)->{
                        list.remove(pos);
                        myAdapter.notifyDataSetChanged();
                    })

                    .setNegativeButton("No",(click,arg)->{})


                    .create().show();

            return true;
        });

    }


    private class MyListAdapter extends BaseAdapter{


        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
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

            tView.setText(mes.getMessgage());
            return newView;


        }
    }


}