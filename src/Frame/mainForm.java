package Frame;

import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.awt.*;

/**
 * Created by Vladimir on 09/01/18.
 **/
public class mainForm extends JFrame {
    private JPanel panel1;
    private JTable table1;

    public mainForm() {
        setContentPane(panel1);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(new Dimension(400, 400));
        setVisible(true);
        String[][] data = {{"Some text", "SomeStr"}, {"Somete", "Other"}};
        String[] col = {"Srint", "Strong"};
        table1.setModel(new JTable(data, col).getModel());
        System.out.println(table1.getValueAt(0, 0).toString());
    }

    public static void main(String[] args) {
        mainForm mf = new mainForm();
    }
}

class MyTableObj {
    @Override

    public String toString() {
        return super.toString();
    }
}
