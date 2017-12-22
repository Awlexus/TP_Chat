package logic;

import logic.storage.DataIOs;
import logic.storage.DataRepository;
import protocol.ProtocolUtilsKt;

import java.awt.*;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

class Contacts {

    private ArrayList<Contact> contactList = new ArrayList<>();

    private final AtomicInteger ai;
    private CallbackListener parent;
    private String repositoryPath;

    public Contacts(AtomicInteger ai, String repositoryPath) {
        this.ai = ai;
        this.repositoryPath = repositoryPath;
    }

    public void setParent(CallbackListener parent) {
        this.parent = parent;
    }

    synchronized public void printContacts() {
        try {
            DataIOs.print(repositoryPath+"\\Contacts.ser", contactList);
        } catch (DataRepository.DataException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            System.out.println("Printing failed");
        }
    }

    synchronized public void readContacts() {
        try {
            ArrayList newContactList = ((ArrayList) DataIOs.read(repositoryPath+"\\Contacts.ser"));
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

    synchronized Contact getOrCreateContact(String macaddress, InetAddress ip, String username, Color color) {
        if (getByMacAddress(macaddress) != null) {
            Contact contact = getByMacAddress(macaddress);
            contact.setIp(ip);
            contact.setUsername(username);
            return contact;
        }

        int id = ai.incrementAndGet();
        while (parent.isUsedID(id))
            id = ai.incrementAndGet();

        Contact newContact = new Contact(macaddress, id, ip, username, color, new ArrayList<>());
        this.contactList.add(newContact);

        System.out.println(newContact);
        return newContact;

    }

    Contact getByID(int id) {
        for (Object o : this.contactList) {
            if (((Contact) o).getId() == id)
                return ((Contact) o);
        }
        return null;
    }

    Contact getByMacAddress(String macaddress) {
        for (Object o : this.contactList) {
            if (((Contact) o).getMacaddr().equals(macaddress))
                return ((Contact) o);
        }
        return null;
    }

    Contact getByMacAddress(DatagramPacket packet) {
        return getByMacAddress(ProtocolUtilsKt.getMacAddress(packet));
    }
}
