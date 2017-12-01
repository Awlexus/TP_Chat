package gui;

/**
 * @author Matteo Cosi
 * @since 01.12.2017
 */
public class ContactEvent {
    private String name;
    private int id;
    private Contacts.Contact source;

    public ContactEvent(String name, int id, Contacts.Contact source) {
        this.name = name;
        this.id = id;
        this.source=source;
    }

    public Contacts.Contact getSource() {
        return source;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }
}
