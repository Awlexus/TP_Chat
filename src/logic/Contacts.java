package logic;

import logic.storage.DataRepository;

import java.awt.*;
import java.net.InetAddress;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

class Contacts extends ArrayList<Contact> {

    private final AtomicInteger ai;
    private final DataRepository dataRepository;

    public Contacts(AtomicInteger ai, DataRepository dataRepository) {
        this.ai = ai;
        this.dataRepository = dataRepository;
    }

    public void print() {
        try {
            // TODO: 15.12.2017 Might throw an exception when File doesn't exist
            dataRepository.deleteFile("contacts.ser");
            dataRepository.print("contacts.ser", this);
        } catch (DataRepository.DataException e) {
            System.out.println("Printing failed");
        }
    }

    Contact createContact(InetAddress ip, String username, Color color) {
        if (getByIP(ip) != null)
            return getByIP(ip);
        if (getByUsername(username) != null)
            return getByUsername(username);
        return new Contact(ai.incrementAndGet(), ip, username, color);

    }

    Contact getByID(int id) {
        for (Object o : this) {
            if (((Contact) o).getId() == id)
                return ((Contact) o);
        }
        return null;
    }

    Contact getByUsername(String username) {
        for (Object o : this) {
            if (((Contact) o).getUsername().equals(username))
                return ((Contact) o);
        }
        return null;
    }

    Contact getByIP(InetAddress ip) {
        for (Object o : this) {
            if (((Contact) o).getIp() == ip)
                return ((Contact) o);
        }
        return null;
    }
}
