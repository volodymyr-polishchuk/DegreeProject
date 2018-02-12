package frame;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Vladimir on 23/01/18.
 **/
public class HelloPanel extends JPanel {
    private JPanel contentPane;
    private JList list1;
    private JButton перейтиButton;
    private JButton перейтиButton1;
    private JButton перейтиButton2;

    public HelloPanel(String name) {
        setName(name);
        setLayout(new GridLayout());
        add(contentPane);
    }
}
