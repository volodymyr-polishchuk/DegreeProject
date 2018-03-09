package app.frame;

import app.data.Group;
import app.schedules.GroupChoiceListener;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
//  TODO Переробити щоб передався масив і на вихід був масив
    public GroupChoiceDialog(List<Group> list, int [] choice, GroupChoiceListener listener) {
        this.listener = listener;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        pack();
//        setSize(550, getHeight());
//        setResizable(false);
        setLocationRelativeTo(null);
        setTitle("Налаштування груп");

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
        ArrayList<Group> leftGroups = new ArrayList<>();
        ArrayList<Group> rightGroups = new ArrayList<>();
        boolean b = false;
        for (int i = 0; i < list.size(); i++) {
            for (int j : choice) {if (j == i) b = true;}
            if (!b) leftGroups.add(list.get(i));
            b = false;
        }
        Collections.sort(leftGroups);
        leftGroups.forEach(listLeftModel::addElement);
        listLeft.setModel(listLeftModel);

        listRightModel = new DefaultListModel<>();
        for (int i : choice) {rightGroups.add(list.get(i));}
        Collections.sort(rightGroups);
        rightGroups.forEach(listRightModel::addElement);
        listRight.setModel(listRightModel);
    }

    private void onOK() {
        ArrayList<Group> tList = new ArrayList<>();
        for (int i = 0; i < listRightModel.size(); i++) {
            tList.add(listRightModel.get(i));
        }
        listener.groupChoiceListener(tList);
        dispose();
    }

    private void onCancel() {
        dispose();
    }
}

