package logic;

import logic.storage.DataRepository;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class Groups extends ArrayList<Group> {

    private final AtomicInteger ai;
    private final DataRepository dataRepository;

    public Groups(AtomicInteger ai, DataRepository dataRepository) {
        this.ai = ai;
        this.dataRepository = dataRepository;
    }

    public void print() {
        try {
            // TODO: 15.12.2017 Might throw an exception when File doesn't exist
            dataRepository.deleteFile("Groups.ser");
            dataRepository.print("Groups.ser", this);
        } catch (DataRepository.DataException e) {
            System.out.println("Printing failed");
        }
    }

    Group createGroup(int protocolID, ArrayList<Contact> members) {
        return new Group(protocolID, ai.incrementAndGet(), members);

    }

    Group getByID(int id) {
        for (Object o : this) {
            if (((Group) o).getId() == id)
                return ((Group) o);
        }
        return null;
    }

    Group getByProtocolID(int protocolID) {
        for (Object o : this) {
            if (((Group) o).getProtocolID() == protocolID)
                return ((Group) o);
        }
        return null;
    }
}
