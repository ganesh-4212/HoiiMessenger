package com.kempasolutions.app.hoiimessanger;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class RecentChatsActivity extends AppCompatActivity {
    private List<RecentChat> recentChatList = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecentChatsAdapter mAdapter;
    PropertyReader propertyReader;
    Properties properties;
    String phone;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.recent_chat_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    protected void onStart() {
        // TODO Auto-generated method stub
        propertyReader = new PropertyReader(getApplicationContext());
        properties = propertyReader.getMyProperties();
        phone=properties.getProperty("phone");
        if(phone==null || phone.trim().length()!=10){
            Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivity(intent);
        }
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_chats);
        recyclerView = (RecyclerView) findViewById(R.id.recent_chat_list);
        new FetchRecentChats().execute();


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //Snackbar.make(recyclerView, "item position is : " + (position + 1), Snackbar.LENGTH_SHORT).show();
                Intent intent=new Intent(getApplicationContext(),ChatActivity.class);
                startActivity(intent);
            }
        }));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.share:
                Snackbar.make(recyclerView, item.getTitle() + " clicked.", Snackbar.LENGTH_SHORT).show();
                break;
            case R.id.contacts:
                Intent intent = new Intent(getApplicationContext(), ContactsActivity.class);

                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    public class FetchRecentChats extends AsyncTask<String,RecentChat,Void>{

        @Override
        protected Void doInBackground(String... params) {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/hoii/profile/";

            publishProgress(new RecentChat("Archana Shetty", "gud morning", "8:30AM",path+"1.jpg"),
                    new RecentChat("Harish", "feeling bad", "8:30PM",path+"2.jpg"),
                    new RecentChat("Nagesh", "Chilling", "8:30PM", path+"3.jpg"),
                    new RecentChat("Rajesh", "what? ", "8:30PM",path+"4.jpg"));
            publishProgress(new RecentChat("Damu", "meet u soon", "8:30PM",path+"5.jpg" ),
                    new RecentChat("Santhu", "ryt", "8:30PM", path+"6.jpg"),
                    new RecentChat("Pooja", "puttakka", "8:30PM",path+"7.jpg"));
            return null;
        }

        @Override
        protected void onProgressUpdate(RecentChat... values) {
            super.onProgressUpdate(values);
            for(RecentChat recentChat:values)
            recentChatList.add(recentChat);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mAdapter = new RecentChatsAdapter(recentChatList);
            recyclerView.setAdapter(mAdapter);
            RecentChatsActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAdapter.notifyDataSetChanged();
                }
            });
        }
    }
}
