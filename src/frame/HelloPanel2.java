package frame;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Vladimir on 22/02/18.
 **/
public class HelloPanel2 extends JPanel {
    private JPanel contentPane;
    private JButton перейтиButton;
    private JButton перейтиButton3;
    private JButton перейтиButton2;
    private JButton перейтиButton1;
    private JButton перейтиButton4;
    private JButton перейтиButton5;
    private JButton перейтиButton6;
    private JButton перейтиButton7;
    private JButton перейтиButton8;
    private JButton перейтиButton9;

    public HelloPanel2 (String title) {
        setLayout(new GridLayout());
        setName(title);
        add(contentPane);
    }
}
