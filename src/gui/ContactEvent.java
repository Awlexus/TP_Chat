package gui;

/**
 * @author Matteo Cosi
 * @since 01.12.2017
 */
public class ContactEvent {
    private String name;
    private int sourceId;

    public ContactEvent(String name, int sourceId) {
        this.name = name;
        this.sourceId = sourceId;
    }

    public String getName() {
        return name;
    }

    public int getSourceId() {
        return sourceId;
    }
}
