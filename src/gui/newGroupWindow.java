package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import static gui.MainWindow.theme;

/**
 * @author Matteo Cosi
 * @since 14.12.2017
 */
public class newGroupWindow extends JDialog implements ActionListener {

    public newGroupWindow(JFrame parent, ArrayList<MainWindowListener> listeners) {
        super(parent,true);
        setLocationRelativeTo(parent);
        setUndecorated(true);
        setSize(200,100);
        getContentPane().setBackground(theme.getPrimaryColorLight());
        setLayout(null);


        JLabel title=new JLabel("Namen Angeben:");
        title.setBounds(30,0,100,30);

        JTextField textField = new JTextField();
        textField.setBounds(10,40,180,30);

        JButton button = new JButton("OK");
        button.setBounds(20,80,160,20);
        button.addActionListener(e -> {
            for (MainWindowListener listener : listeners) {
                listener.onNewGroupCreated(new NewGroupEvent(textField.getText(),textField));
            }
            setVisible(false);
            dispose();
        });
        getContentPane().add(button);
        getContentPane().add(textField);
        getContentPane().add(title);
        setVisible(true);
    }
    public void actionPerformed(ActionEvent e) {
        setVisible(false);
        dispose();
    }
}
