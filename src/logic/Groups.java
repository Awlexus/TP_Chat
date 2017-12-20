package logic;

import logic.storage.DataIOs;
import logic.storage.DataRepository;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class Groups {

    private ArrayList<Group> groupList = new ArrayList<>();

    private final AtomicInteger ai;
    private CallbackListener parent;
    private String repositoryPath;

    public Groups(AtomicInteger ai, String repositoryPath) {
        this.ai = ai;
        this.repositoryPath = repositoryPath;
    }

    public void setParent(CallbackListener parent) {
        this.parent = parent;
    }

    public void printGroups() {
        try {
            DataIOs.print(repositoryPath+"\\Groups.ser", groupList);
        } catch (DataRepository.DataException e) {
            System.out.println("Printing failed");
        }
    }

    synchronized public void readGroups() {
        try {
            ArrayList newContactList = ((ArrayList) DataIOs.read(repositoryPath+"\\Groups.ser"));
            groupList.clear();

            for (Object o : newContactList) {
                if (!parent.isUsedID(((Group) o).getId()))
                    groupList.add(((Group) o));
            }
        } catch (DataRepository.DataException e) {
            System.out.println("Reading failed");
        } catch (ClassCastException e) {
            System.out.println("Reading failed. Given path is no ArrayList<Group>");
        }
    }

    synchronized Group createGroup(int protocolID, ArrayList<Contact> members) {
        int id = ai.incrementAndGet();
        while (parent.isUsedID(id))
            id = ai.incrementAndGet();

        Group newGroup = new Group(protocolID, id, members);
        this.groupList.add(newGroup);
        return newGroup;

    }

    Group getByID(int id) {
        for (Object o : this.groupList) {
            if (((Group) o).getId() == id)
                return ((Group) o);
        }
        return null;
    }

    Group getByProtocolID(int protocolID) {
        for (Object o : this.groupList) {
            if (((Group) o).getProtocolID() == protocolID)
                return ((Group) o);
        }
        return null;
    }
}
