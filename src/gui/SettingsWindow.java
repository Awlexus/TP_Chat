package gui;

import com.sun.deploy.ui.AboutDialog;

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
public class SettingsWindow extends JDialog implements ActionListener {
    ArrayList<SettingsActionListener> listeners;

    Settings settings;


    public SettingsWindow(JFrame parent,ArrayList<SettingsActionListener> listeners) {
        super(parent,true);
        setLocationRelativeTo(parent);
        setUndecorated(true);
        this.listeners=listeners;
        getContentPane().setBackground(theme.getPrimaryColorLight());

        JPanel messagePane = new JPanel();
        messagePane.add(new JLabel("test"));
        getContentPane().add(messagePane);
        JPanel buttonPane = new JPanel();
        JButton button = new JButton("OK");
        buttonPane.add(button);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                settings =new Settings(3,1, Theme.Themes.DEEP_PURPLE,"Segoe UI Emoji");
                for (int i = 0; i < listeners.size(); i++) {
                    listeners.get(i).onSettingsApplied(settings);
                }
                setVisible(false);
                dispose();
            }
        });
        getContentPane().add(buttonPane, BorderLayout.SOUTH);
        pack();
        setVisible(true);
    }
    public void actionPerformed(ActionEvent e) {
        setVisible(false);
        dispose();
    }

//TODO setting
    public JPanel getSettingsContent() {
        JPanel ret = new JPanel();
        ret.setLayout(null);

        JButton button = new JButton("OK");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                settings =new Settings(3,1, Theme.Themes.DEEP_PURPLE,"Segoe UI Emoji");
                for (int i = 0; i < listeners.size(); i++) {
                    listeners.get(i).onSettingsApplied(settings);
                }
            }
        });
        button.setSize(button.getPreferredSize());
        ret.add(button);
        return ret;
    }
}
