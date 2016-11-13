package com.kempasolutions.app.hoiimessanger;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.kempasolutions.app.hoiimessanger.hoiidb.DatabaseHelper;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.filetransfer.FileTransfer;
import org.jivesoftware.smackx.filetransfer.FileTransferListener;
import org.jivesoftware.smackx.filetransfer.FileTransferManager;
import org.jivesoftware.smackx.filetransfer.FileTransferRequest;
import org.jivesoftware.smackx.filetransfer.IncomingFileTransfer;
import org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer;
import org.jivesoftware.smackx.receipts.DeliveryReceiptRequest;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import github.ankushsachdeva.emojicon.EmojiconEditText;

public class ChatActivity extends AppCompatActivity {
    XmppManager xmppManager;
    final String recip = "8904370036@kempa";
    final String username = "9980557138";

    static int totalSelectedMessages = 0;
    int RESULT_LOAD_IMAGE = 1;
    DatabaseHelper databaseHelper;
    EmojiconEditText txtMessage;
    private RecyclerView recyclerView;
    private ChatMessageAdapter mAdapter;
    private List<ChatMessage> messageList = new ArrayList<>();
    private MenuItem deleteMenu, shareMenu;

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_chat_activity, menu);
        shareMenu = menu.getItem(0);
        deleteMenu = menu.getItem(1);
        refreshMenuItem();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.chat_list);
        txtMessage = (EmojiconEditText) findViewById(R.id.emojicon_edit_text);
        setTitle("Ganesh");

        databaseHelper = new DatabaseHelper(getApplicationContext());

        new ConnectSevrer().execute(xmppManager);
        // databaseHelper.insertRoaster("8866443312", "Last seen", "45213", "mypic.jpg");
      /*  File myFile = new File("/sdcard/mysdfile.txt");
        try {
            myFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }*/


        mAdapter = new ChatMessageAdapter(messageList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {
                ChatMessage chatMessage = mAdapter.getMessage(position);
                if (chatMessage.isSelected()) {
                    view.setBackgroundColor(Color.TRANSPARENT);
                    chatMessage.setSelected(false);
                    totalSelectedMessages--;
                } else {
                    view.setBackgroundColor(Color.rgb(167, 255, 235));
                    chatMessage.setSelected(true);
                    totalSelectedMessages++;
                }
                refreshMenuItem();
            }
        }));
    }

    public void refreshMenuItem() {
        if (totalSelectedMessages > 0) {
            deleteMenu.setVisible(true);
            shareMenu.setVisible(true);
        } else {
            deleteMenu.setVisible(false);
            shareMenu.setVisible(false);
        }
    }

    public void getImageAndSend() {
        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    public void sendImage(final String picturePath) {
        Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
        final ImageWithDesc imageWithDesc = new ImageWithDesc(bitmap);
        final Dialog dialog = new Dialog(this);

        //setting custom layout to dialog
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.input_image_description);

        //adding text dynamically
        final EmojiconEditText desc = (EmojiconEditText) dialog.findViewById(R.id.image_upload_desc_desc);
        ImageView image = (ImageView) dialog.findViewById(R.id.image_upload_desc_img);
        image.setImageBitmap(imageWithDesc.getImage());

        //adding button click event
        Button dismissButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
        dismissButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageWithDesc.setDesc(desc.getText().toString());
                Calendar calendar = Calendar.getInstance();
                String messageTime = String.valueOf(calendar.get(Calendar.HOUR)) + ":" + String.valueOf(calendar.get(Calendar.MINUTE));
                ChatMessage newMessage = new ChatMessage("Anonyms", "ME", MessageType.IMAGE, imageWithDesc, messageTime, MessageStatus.SENT);


                messageList.add(newMessage);
                mAdapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
                new sendImageTask().execute(picturePath, imageWithDesc.getDesc());
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            sendImage(picturePath);
        }
    }

    public void sendMessage(View view) {
        ChatMessage newMessage;
        Calendar calendar = Calendar.getInstance();
        String messageText = txtMessage.getText().toString().trim();
        txtMessage.setText("");
        String messageTime = String.valueOf(calendar.get(Calendar.HOUR)) + ":" + String.valueOf(calendar.get(Calendar.MINUTE));
        newMessage = new ChatMessage("Anonyms", "ME", MessageType.TEXT, messageText, messageTime, MessageStatus.SENT);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        String timestamp=dateFormat.format(cal.getTime());
        databaseHelper.insertMessage("abcds",1,"8904370036@kempa",0,messageText,timestamp);
        messageList.add(newMessage);
        mAdapter.notifyDataSetChanged();
        recyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
        try {
            Message message1 = new Message(recip, messageText);

            String deliveryReceiptId = DeliveryReceiptRequest.addTo(message1);
            Chat chat = ChatManager.getInstanceFor(xmppManager.connection).createChat(recip);
            chat.sendMessage(message1);
            System.out.println("sendMessage: deliveryReceiptId for this message is: " + deliveryReceiptId);

        } catch (SmackException.NotConnectedException e) {
            System.out.println("Smack exception : 122:: ");
            e.printStackTrace();
        }

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
            case R.id.delete_selected_chat:
                deleteSelectedMessages();
                totalSelectedMessages = 0;
                refreshMenuItem();
                break;
            case R.id.upload_image:
                getImageAndSend();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void deleteSelectedMessages() {
        for (int i = 0; i < mAdapter.getItemCount(); i++) {
            ChatMessage chatMessage = mAdapter.getMessage(i);
            if (chatMessage.isSelected()) {
                mAdapter.deleteMessage(i--);
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    class ConnectSevrer extends AsyncTask<XmppManager, Integer, Integer> {
        @Override
        protected Integer doInBackground(XmppManager... xmppManagers) {
            String password = new StringBuffer(username).reverse().toString();
            xmppManager = new XmppManager("192.168.43.81", 5222);
            try {
                xmppManager.init();
                System.out.println("Initialization");
                xmppManager.performLogin(username, password);
                System.out.println("login");
                ChatManager.getInstanceFor(xmppManager.connection).addChatListener(new ChatManagerListener() {
                    @Override
                    public void chatCreated(Chat chat, boolean bln) {
                        chat.addMessageListener(new ChatMessageListener() {
                            @Override
                            public void processMessage(Chat chat, Message msg) {
                                if (msg != null && msg.getBody() != null) {
                                    // System.out.println("Received message: message from "+msg.getFrom());
                                    Calendar calendar = Calendar.getInstance();
                                    String messageTime = String.valueOf(calendar.get(Calendar.HOUR)) + ":" + String.valueOf(calendar.get(Calendar.MINUTE));
                                    ChatMessage newIncomingMessage = new ChatMessage("ME", "Anonymous", MessageType.TEXT, msg.getBody(), messageTime, MessageStatus.SENT);
                                    messageList.add(newIncomingMessage);
                                    ChatActivity.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            mAdapter.notifyDataSetChanged();
                                            recyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
                                        }
                                    });
                                }
                            }
                        });
                    }
                });

                FileTransferManager manager = FileTransferManager.getInstanceFor(xmppManager.connection);
                manager.addFileTransferListener(new FileTransferListener() {
                    @Override
                    public void fileTransferRequest(final FileTransferRequest request) {
                        System.out.println("file request " + request.getFileName());
                        System.out.println("file from " + request.getRequestor());
                        Thread tr = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                IncomingFileTransfer transfer = request.accept();
                                File mf = Environment.getExternalStorageDirectory();
                                File directories = new File(mf.getAbsolutePath() + "/hoii/images/");
                                if (!directories.exists()) {
                                    directories.mkdirs();
                                }
                                File file = new File(directories.getAbsoluteFile() + "/" + transfer.getFileName());
                                System.out.println(file.getAbsolutePath());
                                try {
                                    transfer.recieveFile(file);
                                    while (!transfer.isDone()) {
                                        System.out.println(transfer.getStatus());
                                        Thread.sleep(1000L);
                                    }

                                    if (transfer.getStatus() == FileTransfer.Status.complete) {
                                        System.out.println("download completed");
                                        Calendar calendar = Calendar.getInstance();
                                        String messageTime = String.valueOf(calendar.get(Calendar.HOUR)) + ":" + String.valueOf(calendar.get(Calendar.MINUTE));
                                        ImageWithDesc imageWithDesc = new ImageWithDesc(BitmapFactory.decodeFile(file.getAbsolutePath()), "");
                                        ChatMessage newMessage = new ChatMessage("ME", "Anonyms", MessageType.IMAGE, imageWithDesc, messageTime, MessageStatus.SENT);
                                        messageList.add(newMessage);
                                        ChatActivity.this.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                mAdapter.notifyDataSetChanged();
                                                recyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
                                            }
                                        });
                                    }
                                } catch (SmackException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        });
                        tr.start();
                    }
                });
                System.out.println("Server coonected 11212");
            } catch (XMPPException e) {
                System.out.println("XMPP exception : 111:: ");
                e.printStackTrace();
            } catch (IOException e) {
                System.out.println("IO exception : 112:: ");
                e.printStackTrace();
            } catch (SmackException e) {
                System.out.println("Smack exception : 113:: ");
                e.printStackTrace();
            }
            return null;
        }
    }

    public class sendImageTask extends AsyncTask<String, Integer, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            FileTransferManager manager = FileTransferManager.getInstanceFor(xmppManager.connection);
            final OutgoingFileTransfer transfer = manager.createOutgoingFileTransfer(recip+"/Smack");
            File file = new File(params[0]);
            try {
                transfer.sendFile(file, params[1]);
            } catch (SmackException e) {
                e.printStackTrace();
            }
            while (!transfer.isDone()) {
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (transfer.getStatus() == FileTransfer.Status.complete) {
                ChatActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), transfer.getFileName() + " Sent.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            return null;
        }
    }

}



