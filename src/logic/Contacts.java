package logic;

import logic.storage.DataRepository;

import java.awt.*;
import java.net.InetAddress;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

class Contacts {

    private ArrayList<Contact> contactList = new ArrayList<>();

    private final AtomicInteger ai;
    private final DataRepository dataRepository;
    private CallbackListener parent;

    public Contacts(AtomicInteger ai, DataRepository dataRepository) {
        this.ai = ai;
        this.dataRepository = dataRepository;
    }

    public void setParent(CallbackListener parent) {
        this.parent = parent;
    }

    synchronized public void printContacts() {
        try {
            // TODO: 15.12.2017 Might throw an exception when File doesn't exist
            dataRepository.deleteFile("contacts.ser");
            dataRepository.print("contacts.ser", this.contactList);
        } catch (DataRepository.DataException e) {
            System.out.println("Printing failed");
        }
    }

    synchronized public void readContacts() {
        try {
            ArrayList newContactList = ((ArrayList) dataRepository.read("contacts.ser"));
            contactList.clear();

            for (Object o : newContactList)
                if (!parent.isUsedID(((Contact) o).getId()))
                    contactList.add(((Contact) o));
        } catch (DataRepository.DataException e) {
            System.out.println("Reading failed");
        } catch (ClassCastException e) {
            System.out.println("Reading failed. Given path is no ArrayList<Contact>");
        }
    }

    synchronized Contact createContact(InetAddress ip, String username, Color color) {
        if (getByIP(ip) != null)
            return getByIP(ip);
        if (getByUsername(username) != null)
            return getByUsername(username);

        int id = ai.incrementAndGet();
        while (parent.isUsedID(id))
            id = ai.incrementAndGet();

        Contact newContact = new Contact(id, ip, username, color);
        this.contactList.add(newContact);
        return newContact;

    }

    Contact getByID(int id) {
        for (Object o : this.contactList) {
            if (((Contact) o).getId() == id)
                return ((Contact) o);
        }
        return null;
    }

    Contact getByUsername(String username) {
        for (Object o : this.contactList) {
            if (((Contact) o).getUsername().equals(username))
                return ((Contact) o);
        }
        return null;
    }

    Contact getByIP(InetAddress ip) {
        for (Object o : this.contactList) {
            if (((Contact) o).getIp() == ip)
                return ((Contact) o);
        }
        return null;
    }
}
