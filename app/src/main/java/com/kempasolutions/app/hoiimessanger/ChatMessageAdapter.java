package com.kempasolutions.app.hoiimessanger;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import github.ankushsachdeva.emojicon.EmojiconTextView;

/**
 * Created by Ganesh Poojary on 7/31/2016.
 */
public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.MyViewHolder> {
    private List<ChatMessage> messageList;

    class MyViewHolder extends RecyclerView.ViewHolder {
        private int viewType;
        TextView timeText;
        EmojiconTextView messageText;
        ImageView MsgImage;

        public MyViewHolder(View view, int viewType) {
            super(view);
            this.viewType = viewType;
            if ((viewType == 1) || (viewType == 2)) {
                timeText = (TextView) view.findViewById(R.id.time_text);
                messageText = (EmojiconTextView) view.findViewById(R.id.message_text);
            }
            if ((viewType == 3) || (viewType == 4)) {
                timeText = (TextView) view.findViewById(R.id.time_text);
                messageText = (EmojiconTextView) view.findViewById(R.id.message_text);
                MsgImage = (ImageView) view.findViewById(R.id.chat_image);
            }
        }

        public int getViewType() {
            return viewType;
        }

        public void setViewType(int viewType) {
            this.viewType = viewType;
        }
    }

    public ChatMessageAdapter(List<ChatMessage> messageList) {
        this.messageList = messageList;
    }


    @Override
    public int getItemViewType(int position) {
        int viewType = 0;
        ChatMessage chatMessage = messageList.get(position);
        if (chatMessage.getFrom().equalsIgnoreCase("ME") && chatMessage.getType() == MessageType.TEXT) {
            viewType = 1;
        } else if (chatMessage.getType() == MessageType.TEXT) {
            viewType = 2;
        } else if (chatMessage.getFrom().equalsIgnoreCase("ME") && chatMessage.getType() == MessageType.IMAGE) {
            viewType = 3;
        } else if (chatMessage.getType() == MessageType.IMAGE) {
            viewType = 4;
        }
        return viewType;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        //Log.d("exeee1", String.valueOf(isItemSelected.size()));
        if (viewType == 1) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_user2_item, parent, false);
            return new MyViewHolder(itemView, viewType);
        } else if (viewType == 2) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_user1_item, parent, false);
            return new MyViewHolder(itemView, viewType);
        } else if (viewType == 3) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.image_chat_user2, parent, false);
            return new MyViewHolder(itemView, viewType);
        } else if (viewType == 4) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.image_chat_user1, parent, false);
            return new MyViewHolder(itemView, viewType);
        }
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_user2_item, parent, false);

        return new MyViewHolder(itemView, viewType);
    }

    public ChatMessage getMessage(int position) {
        return messageList.get(position);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ChatMessage chatMessage = messageList.get(position);
        if ((holder.getViewType() == 1) || (holder.getViewType() == 2)) {
            holder.timeText.setText(chatMessage.getTime());
            holder.messageText.setText(chatMessage.getBody().toString());
        }
        if ((holder.getViewType() == 3) || (holder.getViewType() == 4)) {
            holder.timeText.setText(chatMessage.getTime());
            ImageWithDesc imageWithDesc = (ImageWithDesc) chatMessage.getBody();
            holder.MsgImage.setImageBitmap(imageWithDesc.getImage());
            holder.messageText.setText(imageWithDesc.getDesc());

        }
        try {
            if (chatMessage.isSelected()) {
                holder.itemView.setBackgroundColor(Color.rgb(167, 255, 235));

            } else {
                holder.itemView.setBackgroundColor(Color.TRANSPARENT);
            }
        } catch (Exception e) {
            Log.d("exeee", String.valueOf(position));
        }
    }

    public void deleteMessage(int position) {
        if (position < messageList.size()) {
            messageList.remove(position);
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

}
