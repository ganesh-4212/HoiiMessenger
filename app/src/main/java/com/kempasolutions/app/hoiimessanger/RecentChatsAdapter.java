package com.kempasolutions.app.hoiimessanger;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Movie;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.ActionMenuView.OnMenuItemClickListener;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.siyamed.shapeimageview.CircularImageView;

import java.util.List;

/**
 * Created by Ganesh Poojary on 7/29/2016.
 */
public class RecentChatsAdapter extends RecyclerView.Adapter<RecentChatsAdapter.MyViewHolder> {
    private List<RecentChat> recentChatList;

    public RecentChatsAdapter(List<RecentChat> recentChatList) {
        this.recentChatList = recentChatList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener

    {
        public TextView chatTitle, recentMsg, lastMsgTime;
        public CircularImageView profilePic;

        public MyViewHolder(View view) {
            super(view);
            profilePic = (CircularImageView) view.findViewById(R.id.profile_pic);
            chatTitle = (TextView) view.findViewById(R.id.chat_title);
            recentMsg = (TextView) view.findViewById(R.id.recent_msg);
            lastMsgTime = (TextView) view.findViewById(R.id.last_msg_time);
            view.setOnCreateContextMenuListener(this);


        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Select the action");
            menu.add(0, v.getId(), 0, R.string.view_prfl).setOnMenuItemClickListener(this);
            menu.add(0, v.getId(), 0, R.string.clear_chat).setOnMenuItemClickListener(this);
            menu.add(0, v.getId(), 0, R.string.dlt_chat).setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            String viewProfile = itemView.getContext().getString(R.string.view_prfl);
            String clearChat = itemView.getContext().getString(R.string.view_prfl);
            String deleteChat = itemView.getContext().getString(R.string.dlt_chat);
            String selectedItemTitle = item.getTitle().toString();
            if (selectedItemTitle == viewProfile) {
                //Handle code for view profile
            } else if (selectedItemTitle == clearChat) {
                //Handle code for clear chat
            } else if (selectedItemTitle == deleteChat) {
                //Handle code for delete chat
            } else {
                //Handle code for other menu item click
            }
            Snackbar snackbar = Snackbar.make(itemView, item.getTitle() + " clicked.", Snackbar.LENGTH_SHORT);
            snackbar.show();
            return false;
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_list_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final RecentChat recentChat = recentChatList.get(position);
        holder.profilePic.post(new Runnable() {
            @Override
            public void run() {
                Bitmap pic= BitmapFactory.decodeFile(recentChat.getPrflPic());
                holder.profilePic.setImageBitmap(pic);
            }
        });
        holder.chatTitle.setText(recentChat.getChatTitle());
        holder.recentMsg.setText(recentChat.getRecentMsg());
        holder.lastMsgTime.setText(recentChat.getLastMsgTime());
    }

    @Override
    public int getItemCount() {
        return recentChatList.size();
    }


}
