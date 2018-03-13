package app.frame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AboutFrame extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JTextPane jTextPane;

    public AboutFrame() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(e -> onOK());
    }

    private void onOK() {
        // add your code here
        dispose();
    }

    public static void main(String[] args) {
        AboutFrame dialog = new AboutFrame();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
