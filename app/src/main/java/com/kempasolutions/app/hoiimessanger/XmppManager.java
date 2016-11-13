package com.kempasolutions.app.hoiimessanger;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Type;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.iqregister.AccountManager;

import java.io.IOException;
import java.util.Collection;

public class XmppManager {

	private static final int packetReplyTimeout = 500; // millis

	private String server;
	private int port;

	private XMPPTCPConnectionConfiguration config;
	public AbstractXMPPConnection connection;

	public ChatManager chatManager;
	private MyMessageListener messageListener;

	public XmppManager(String server, int port) {
		this.server = server;
		this.port = port;
	}

	public void init() throws XMPPException, IOException, SmackException {



		config =XMPPTCPConnectionConfiguration.builder()
				.setServiceName("kempa")
				.setCompressionEnabled(false)
				.setHost(server)
				.setPort(port)
				.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
                /* .setSecurityMode(SecurityMode.required) keep this commented */
				.setSendPresence(true)
				.build();

        connection = new XMPPTCPConnection(config);
		connection.connect();

		System.out.println("Connected: " + connection.isConnected());


		messageListener = new MyMessageListener();


	}

	public void performLogin(String username, String password) throws XMPPException, IOException, SmackException {
		if (connection!=null && connection.isConnected()) {
			connection.login(username, password);
			System.out.println("Logged in");
		}
	}

	public void setStatus(boolean available, String status) throws SmackException.NotConnectedException {

		Type type = available? Type.available: Type.unavailable;
		Presence presence = new Presence(type);
		presence.setStatus(status);
		connection.sendPacket(presence);

	}

	public void destroy() {
		if (connection!=null && connection.isConnected()) {
			connection.disconnect();
		}
	}

	public void printRoster() throws Exception {
		Roster roster = Roster.getInstanceFor(connection);
		Collection<RosterEntry> entries = roster.getEntries();
		for (RosterEntry entry : entries) {
		    System.out.println(String.format("Buddy:%1$s - Status:%2$s",
		    		entry.getName(), entry.getStatus()));
		}
	}
	public void createAccount(String username, String password) throws XMPPException, SmackException.NotConnectedException, SmackException.NoResponseException {
		if (connection != null && connection.isConnected()) {
			AccountManager accountManager =AccountManager.getInstance(connection);
            accountManager.sensitiveOperationOverInsecureConnection(true);
			accountManager.createAccount(username, password);
		}
	}
	public void sendMessage(String message, String buddyJID) throws XMPPException, SmackException.NotConnectedException {
		System.out.println(String.format("Sending mesage '%1$s' to user %2$s", message, buddyJID));
		Chat chat = ChatManager.getInstanceFor(connection).createChat(buddyJID, messageListener);
		chat.sendMessage(message);

	}

	public void createEntry(String user, String name) throws Exception {
		System.out.println(String.format("Creating entry for buddy '%1$s' with name %2$s", user, name));
		Roster roster = Roster.getInstanceFor(connection);
		roster.createEntry(user, name, null);
	}

	class MyMessageListener implements ChatMessageListener {

        @Override
        public void processMessage(Chat chat, Message message) {
            String from = message.getFrom();
            String body = message.getBody();
            System.out.println(String.format("Received message '%1$s' from %2$s", body, from));
        }
    }

}
