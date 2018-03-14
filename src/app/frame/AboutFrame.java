package app.frame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AboutFrame extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JTextPane jTextPane;

    public AboutFrame() {
        setContentPane(contentPane);
        setModal(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(new Dimension(450, 400));
        setLocationRelativeTo(null);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(e -> onOK());
    }

    private void onOK() {
        // add your code here
        dispose();
    }

}
