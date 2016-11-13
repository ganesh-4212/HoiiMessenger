package com.kempasolutions.app.hoiimessanger;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.kempasolutions.app.hoiimessanger.hoiidb.DatabaseHelper;

import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Archana on 8/10/2016.
 */

public class ChatService extends Service {
    static int i = 0;
    XmppManager xmppManager;
    MyReceiver myReceiver;
    String phoneNumber;
    DatabaseHelper databaseHelper;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        myReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BroadcastActions.INVOKE_CHAT_SERVICE);
        registerReceiver(myReceiver, intentFilter);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public class RegisterUser extends AsyncTask<String, Void, Void> {
        String username, password;
        XmppManager xmppManager;

        @Override
        protected Void doInBackground(String... phone) {
            try {
                username = phoneNumber;
                password = new StringBuffer(phoneNumber).reverse().toString();
                xmppManager = new XmppManager("192.168.43.81", 5222);
                xmppManager.init();
                xmppManager.performLogin(username, password);

            } catch (XMPPException ex) {
                if (ex.getMessage().contains("SASLError using SCRAM-SHA-1: not-authorized")) {
                    try {
                        xmppManager.createAccount(username, password);
                        xmppManager.performLogin(username, password);
                        Toast.makeText(getApplicationContext(), "Registered/logged in", Toast.LENGTH_SHORT).show();


                    } catch (XMPPException ex1) {
                        ex1.printStackTrace();
                    } catch (Exception ex1) {
                        ex1.printStackTrace();
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            Intent intent = new Intent();
            intent.setAction(BroadcastActions.REGISTRATION_ACTION);
            intent.putExtra(BroadcastActions.REGISTRATION_ACTION, BroadcastActions.REGISTRATION_ACTION_RESULT);
            sendBroadcast(intent);
            ChatManager.getInstanceFor(xmppManager.connection).addChatListener(new ChatManagerListener() {
                @Override
                public void chatCreated(Chat chat, boolean bln) {
                    chat.addMessageListener(new ChatMessageListener() {
                        @Override
                        public void processMessage(Chat chat, Message msg) {
                            if (msg != null && msg.getBody() != null) {
                                System.out.println("Received message: message from "+msg.getFrom());
                                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                Calendar cal = Calendar.getInstance();
                                String timestamp=dateFormat.format(cal.getTime());
                                //databaseHelper = new DatabaseHelper(getApplicationContext());
                                //databaseHelper.insertMessage(msg.getStanzaId(),1,msg.getFrom(),0,msg.getBody(),timestamp);
                                //databaseHelper.getMessages();
                            }
                        }
                    });
                }
            });
                return null;
            }
        }

        private class MyReceiver extends BroadcastReceiver {

            @Override
            public void onReceive(Context arg0, Intent arg1) {
                // TODO Auto-generated method stub

                if (arg1.hasExtra(BroadcastActions.INVOKE_CHAT_SERVICE)) {
                    switch (arg1.getStringExtra(BroadcastActions.INVOKE_CHAT_SERVICE)) {
                        case BroadcastActions.REGISTER_LOGIN:
                            phoneNumber = arg1.getStringExtra(BroadcastActions.REGISTER_LOGIN_PHONE);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    new RegisterUser().execute();
                                }
                            }).start();
                            break;
                    }
                }
            }

        }

    }
