package com.kempasolutions.app.hoiimessanger;

import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.CircularImageView;

import java.util.List;

/**
 * Created by Ganesh Poojary on 7/30/2016.
 */
public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.MyViewHolder> {
    private List<Contacts> contactList;

    public ContactsAdapter(List<Contacts> contactList) {
        this.contactList = contactList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_list_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Contacts contacts = contactList.get(position);
        holder.profilePic.setImageBitmap(contacts.getPic());
        holder.contactName.setText(contacts.getName());
        holder.contactStatus.setText(contacts.getStatus());
    }
    public Contacts getItem(int position){
        return contactList.get(position);
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
        public TextView contactName, contactStatus;
        public CircularImageView profilePic;

        public MyViewHolder(View view) {
            super(view);
            profilePic = (CircularImageView) view.findViewById(R.id.profile_pic);
            contactName = (TextView) view.findViewById(R.id.contact_name);
            contactStatus = (TextView) view.findViewById(R.id.contact_status);
            view.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Select the action");
            menu.add(0, v.getId(), 0, R.string.view_prfl).setOnMenuItemClickListener(this);
            menu.add(0, v.getId(), 0, R.string.view_conv).setOnMenuItemClickListener(this);
            menu.add(0, v.getId(), 0, R.string.view_addr).setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            String viewProfile = itemView.getContext().getString(R.string.view_prfl);
            String viewConv = itemView.getContext().getString(R.string.view_conv);
            String viewAddr = itemView.getContext().getString(R.string.view_addr);
            String selectedItemTitle = item.getTitle().toString();
            if (selectedItemTitle == viewProfile) {
                //Handle code for view profile
            } else if (selectedItemTitle == viewConv) {
                //Handle code for clear chat
            } else if (selectedItemTitle == viewAddr) {
                //Handle code for delete chat
            } else {
                //Handle code for other menu item click
            }
            Snackbar snackbar = Snackbar.make(itemView, item.getTitle() + " clicked.", Snackbar.LENGTH_SHORT);
            snackbar.show();
            return false;
        }
    }
}
