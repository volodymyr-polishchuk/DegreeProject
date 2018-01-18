package frame;

import app.Group;
import app.GroupChoiceListener;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class GroupChoiceDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JList<Group> listLeft;
    private DefaultListModel<Group> listLeftModel;
    private JList<Group> listRight;
    private DefaultListModel<Group> listRightModel;
    private JButton buttonGoRight;
    private JButton buttonAllLeft;
    private JButton buttonGoLeft;
    private JButton buttonAllRight;
    private GroupChoiceListener listener;

    public GroupChoiceDialog(List<Group> list, int [] choice, GroupChoiceListener listener) {
        this.listener = listener;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        pack();
        setResizable(false);

        InitialList(list, choice);

        buttonOK.addActionListener(e -> onOK());
        buttonCancel.addActionListener(e -> onCancel());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        buttonGoRight.addActionListener(e -> listRightModel.addElement(listLeftModel.remove(listLeft.getSelectedIndex())));
        buttonGoLeft.addActionListener(e -> listLeftModel.addElement(listRightModel.remove(listRight.getSelectedIndex())));
        buttonAllRight.addActionListener(e -> {
            for (int i = listLeftModel.size() - 1; i >= 0; i--) {
                listRightModel.addElement(listLeftModel.remove(i));
            }
        });
        buttonAllLeft.addActionListener(e -> {
            for (int i = listRightModel.size() - 1; i >= 0; i--) {
                listLeftModel.addElement(listRightModel.remove(i));
            }
        });
        setVisible(true);
    }

    private void InitialList(List<Group> list, int [] choice) {
        listLeftModel = new DefaultListModel<>();
        listLeftModel.clear();
        for (int i = 0; i < list.size(); i++) {
            boolean b = false;
            for (int j : choice) {if (j == i) b = true;}
            if (!b) listLeftModel.addElement(list.get(i));
            b = false;
        }
        listLeft.setModel(listLeftModel);

        listRightModel = new DefaultListModel<>();
        listRightModel.clear();
        for (int i : choice) {listRightModel.addElement(list.get(i));}
        listRight.setModel(listRightModel);
    }

    private void onOK() {
        // add your code here
        ArrayList<Group> tList = new ArrayList<>();
        for (int i = 0; i < listRightModel.size(); i++) {
            tList.add(listRightModel.get(i));
        }
        listener.GroupChoiceListener(tList);
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        List<Group> list = new ArrayList<>();
        list.add(new Group("Програмування", "ПС-16"));
        list.add(new Group("Програмування", "ПС-26"));
        list.add(new Group("Програмування", "ПС-36"));
        list.add(new Group("Програмування", "ПС-46"));
        list.add(new Group("Програмування", "ПС-47"));
        list.add(new Group("Програмування", "ПС-48"));
        GroupChoiceDialog dialog = new GroupChoiceDialog(list, new int[]{1, 2}, System.out::println);

        System.exit(0);
    }
}

