import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;


public class ReloadTable {
    ArrayList<String> al = new ArrayList<String>();
    TModel mod;
    public void gui() {
        al.add("stat,1,89898");
        al.add("stat,2,89898");

        String[] colHeads = {"A","B","C" };

        mod = new TModel(al);
        JFrame frame = new JFrame("Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        JTable tbl = new JTable(mod);
        JButton btn1 = new JButton("add");
        btn1.addActionListener(new AddRow());
        frame.add("Center", tbl);
        frame.add("East", btn1);

        frame.setSize(600,400);
        frame.setVisible(true);

    }

    public static void main(String[] args) {
        new ReloadTable().gui();
    }

    public class TModel extends AbstractTableModel {
        List<List<String>> content = new ArrayList<List<String>>();

        public TModel(List<String> a1) {
            for(int i=0; i < al.size(); i++) {
                content.add(new ArrayList<String>(Arrays.asList(al.get(i).split(","))));
            }
        }

        public void addString(String s) {
            content.add(new ArrayList<String>(Arrays.asList(s.split(","))));
            fireTableDataChanged();
        }

        public int getRowCount() {
            return al.size();
        }


        public int getColumnCount() {
            return 3;
        }


        public Object getValueAt(int rowIndex, int columnIndex) {
            return content.get(rowIndex).get(columnIndex);
        }

    }

    public class AddRow implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            al.add("new,3,89898");
            mod.addString(al.get(al.size() - 1));
        }
    }
}