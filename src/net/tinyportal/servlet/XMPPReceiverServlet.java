package net.tinyportal.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.xmpp.JID;
import com.google.appengine.api.xmpp.Message;
import com.google.appengine.api.xmpp.MessageBuilder;
import com.google.appengine.api.xmpp.SendResponse;
import com.google.appengine.api.xmpp.XMPPService;
import com.google.appengine.api.xmpp.XMPPServiceFactory;


public class XMPPReceiverServlet extends HttpServlet  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7967229702403773331L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JID jid = new JID("seb0uil@gmail.com");
        String msgBody = "Someone has sent you a gift on Example.com. To view: http://example.com/gifts/";
        Message msg = new MessageBuilder()
            .withRecipientJids(jid)
            .withBody(msgBody)
            .build();
                
        boolean messageSent = false;
        XMPPService xmpp = XMPPServiceFactory.getXMPPService();
        if (xmpp.getPresence(jid).isAvailable()) {
            SendResponse status = xmpp.sendMessage(msg);
            messageSent = (status.getStatusMap().get(jid) == SendResponse.Status.SUCCESS);
        }

        if (!messageSent) {
            // Send an email message instead...
        }
	}
}
