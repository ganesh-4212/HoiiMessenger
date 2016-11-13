package com.kempasolutions.app.hoiimessanger;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ContactsActivity extends AppCompatActivity {
    private List<Contacts> contactList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ContactsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        setTitle(R.string.contacts_title);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        recyclerView = (RecyclerView) findViewById(R.id.contact_list);

        mAdapter = new ContactsAdapter(contactList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(mAdapter);
        prepareChatListData();
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getApplicationContext(), ContactDetailsActivity.class);
                ContactsAdapter cAdapter = (ContactsAdapter) recyclerView.getAdapter();
                Contacts contacts = cAdapter.getItem(position);
                intent.putExtra(getString(R.string.intent_contact_name), contacts.getName());
                intent.putExtra(getString(R.string.intent_contact_status), contacts.getStatus());
                intent.putExtra(getString(R.string.intent_contact_phone), contacts.getPhone());
                startActivity(intent);
            }
        }));
    }

    private void prepareChatListData() {
        Drawable drawable = getResources().getDrawable(R.drawable.p9008595229);
        Bitmap prflpic = ((BitmapDrawable) drawable).getBitmap();
        contactList.add(new Contacts("Archana Shetty", "feeling gud...","9008595229", prflpic));
        drawable = getResources().getDrawable(R.drawable.prflpic);
        prflpic = ((BitmapDrawable) drawable).getBitmap();
        contactList.add(new Contacts("Shwetha Kulal", "feeling cold","8861410331", prflpic));

        contactList.add(new Contacts("Pooja Puthran","Yarigadru meen beka...","8762452412", prflpic));

        contactList.add(new Contacts("Rajesh", "OpenUp India","9535134526", prflpic));

        contactList.add(new Contacts("Damu", "PG loan sanctioned","9980557138", prflpic));

        contactList.add(new Contacts("Santhu", "Gaddi nettig batrya...","8197008525", prflpic));

        contactList.add(new Contacts("Shashank", "my attitude depends on how u treat me","9742690206",prflpic));
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
