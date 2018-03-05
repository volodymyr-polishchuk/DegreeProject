package test;

import javax.swing.*;

/**
 * Created by Vladimir on 05/03/18.
 **/
public class TestConfirmDialog {
    public static void main(String[] args) {
        System.out.println(JOptionPane.showConfirmDialog(null, "Some message", "Message", JOptionPane.YES_NO_CANCEL_OPTION));
    }
}
