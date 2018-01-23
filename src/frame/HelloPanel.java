package frame;

import javax.swing.*;

/**
 * Created by Vladimir on 23/01/18.
 **/
public class HelloPanel extends JPanel {
    private JPanel contentPane;

    public HelloPanel(String name) {
        setName(name);
        add(contentPane);
    }
}
