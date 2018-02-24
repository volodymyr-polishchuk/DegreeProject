package frame;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Vladimir on 22/02/18.
 **/
public class HelloPanel2 extends JPanel {
    private JPanel contentPane;
    private JButton створитиButton;
    private JButton переглянутиButton1;
    private JButton створитиButton1;
    private JButton переглянутиButton;
    private JButton додатиButton;
    private JButton додатиButton1;
    private JButton додатиButton2;
    private JButton додатиButton3;
    private JButton перейтиButton8;
    private JButton перейтиButton9;

    public HelloPanel2 (String title) {
        setLayout(new GridLayout());
        setName(title);
        add(contentPane);
    }
}
