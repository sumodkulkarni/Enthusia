package org.enthusia.app;

import android.app.Activity;
import android.content.Context;

import org.enthusia.app.model.PushMessage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

/**
 * Created by rahuliyer on 3/6/14.
 * Use this class to access methods to make Toast and Shared Prefs
 */

public class Utils {

    public static final String SHARED_PREFS = "org.enthusia.app";

    /**
     * Preferences
     * Add your preferences here
     */

    public static final String PREF_USER_NAME = "pref_username";
    public static final String PREF_EMAIL = "pref_email";
    public static final String PREF_REGISTRATION_DONE = "pref_registration_done";


    /**
     * Make Toast Message in BLUE color shown at top of screen
     * @param activity the activity in which message has to be displayed
     * @param id the id of the string resource to be displayed
     */

    public static final void showInfo (Activity activity, int id) {
        showInfo(activity, activity.getString(id));
    }

    /**
     * Make Toast Message in BLUE color shown at top of screen
     * @param activity the activity in which message has to be displayed
     * @param message the string message to be displayed
     */

    public static final void showInfo (Activity activity, String message) {
        crouton(activity, message, Style.INFO);
    }

    /**
     * Make Toast Message in GREEN color shown at top of screen
     * @param activity the activity in which message has to be displayed
     * @param id the the id of the string resource to be displayed
     */

    public static final void showConfirm (Activity activity, int id) {
        showConfirm(activity, activity.getString(id));
    }

    /**
     * Make Toast Message in GREEN color shown at top of screen
     * @param activity the activity in which message has to be displayed
     * @param message the string message to be displayed
     */

    public static final void showConfirm (Activity activity, String message) {
        crouton(activity, message, Style.CONFIRM);
    }

    /**
     * Make Toast Message in RED color shown at top of screen
     * @param activity the activity in which message has to be displayed
     * @param id the id of the string resource to be displayed
     */

    public static final void showAlert (Activity activity, int id) {
        showAlert(activity, activity.getString(id));
    }

    /**
     * Make Toast Message in RED color shown at top of screen
     * @param activity the activity in which message has to be displayed
     * @param message the string message to be displayed
     */

    public static final void showAlert (Activity activity, String message) {
        crouton(activity, message, Style.ALERT);
    }

    private static final void crouton (Activity activity, String message, Style style) {
        Crouton.makeText(activity, message, style).show();
    }

    /**
     * Xml parsing
     */

    public final static String ENTHUSIA = "enthusia.xml";
    public final static String PRATIBIMB = "pratibimb.xml";
    public final static String TECHNOVANZA = "technovanza.xml";

    /**
     * Use this method to get all the push notifications received so far
     * @param context Context of the android application running (getApplicationContext()) will help
     * @param file Use the file for your particular festival e.g: Utils.ENTHUSIA, Utils.PRATIBIMB, Utils.TECHNOVANZA
     * @return a list of PushMessage objects containing respective data
     * @throws java.io.IOException Throws the IOException occurred in the read process
     */

    public final static ArrayList<PushMessage> getPushMessages (Context context, String file) throws  IOException {
        if (new File(context.getCacheDir(), file).exists()) {
            return getPushMessage(new File(context.getCacheDir(), file));
        }
        return null;
    }

    private final static ArrayList<PushMessage> getPushMessage (File file) throws IOException {
        ArrayList<PushMessage> messages = new ArrayList<PushMessage>();
        try {
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);
            NodeList elements = document.getElementsByTagName("data");
            for (int i=0; i < elements.getLength(); i++) {
                if (elements.item(i).getNodeType() == Node.ELEMENT_NODE)  {
                    messages.add( new PushMessage(
                            ((Element) elements.item(i)).getElementsByTagName("message").item(0).getTextContent(),
                            Boolean.valueOf(((Element) elements.item(i)).getElementsByTagName("read").item(0).getTextContent())
                    ));
                }
            }
            document = null;
            elements = null;
        } catch (ParserConfigurationException ex) {
            ex.printStackTrace();
        } catch (SAXException ex) {
            ex.printStackTrace();
        }
        return messages;
    }

    /**
     * Use this method to store the push messages to file
     * @param context Context of the android application running (getApplicationContext()) will help
     * @param file Use the file for your particular festival e.g: Utils.ENTHUSIA, Utils.PRATIBIMB, Utils.TECHNOVANZA
     * @param messages The List of message you have
     * @throws java.io.IOException Throws the IOException that occurred during the write process
     */

    public static final void editPushMessages(Context context, String file, ArrayList<PushMessage> messages) throws  IOException {
        if (messages == null || messages.size() == 0)
            return;
        editPushMessages(new File(context.getCacheDir(), file), messages);
    }

    private static final void editPushMessages(File file, ArrayList<PushMessage> messages) throws  IOException {

        try {
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();

            Element root = document.createElement("notification");
            document.appendChild(root);

            for (PushMessage message : messages) {

                Element data, content, read;

                data = document.createElement("data");
                content = document.createElement("message");
                read = document.createElement("read");

                content.appendChild(document.createTextNode(message.getMessage()));
                read.appendChild(document.createTextNode(String.valueOf(message.isRead()).toLowerCase()));

                data.appendChild(content);
                data.appendChild(read);

                root.appendChild(data);
            }

            TransformerFactory.newInstance().newTransformer().transform(new DOMSource(document), new StreamResult(file));

        } catch (ParserConfigurationException ex) {
            ex.printStackTrace();;
        } catch (TransformerConfigurationException ex) {
            ex.printStackTrace();
        } catch (TransformerException ex) {
            ex.printStackTrace();
        }


    }

}
