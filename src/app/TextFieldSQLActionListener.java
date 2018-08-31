package app;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Created by Vladimir on 31/08/18.
 **/
public class TextFieldSQLActionListener extends KeyAdapter {

    public static final TextFieldSQLActionListener instance;
    static {
        instance = new TextFieldSQLActionListener();
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (e.getKeyChar() == '\'') {
            e.setKeyChar('`');
        }
    }
}
