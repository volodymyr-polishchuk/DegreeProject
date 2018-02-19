package frame;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Vladimir on 15/02/18.
 **/
public class SettingForm extends JDialog {
    private JTabbedPane tabbedPane1;
    private JPanel ContentPane;
    private JButton зберегтиButton;
    private JButton скасуватиButton;

    public SettingForm() {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(new Dimension(800, 600));
        setLocation((int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth() - getWidth()) / 2,
                (int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight() - getHeight()) / 2);
        setContentPane(ContentPane);
        setModal(true);
    }
}
