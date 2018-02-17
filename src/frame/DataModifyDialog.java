package frame;

import app.data.DataModifyInterface;
import app.data.StudyData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by Vladimir on 16/02/18.
 **/
public class DataModifyDialog extends JDialog {
    private JButton addButton;
    private JButton editButton;
    private JButton removeButton;
    private JButton cancelButton;
    private JList<StudyData> jList;
    private DefaultListModel<StudyData> listModel = new DefaultListModel<>();
    private JPanel ContentPane;
    private JButton saveButton;
    private DataModifyInterface anInterface;

    private DataModifyDialog(StudyData[] arr, DataModifyInterface anInterface) {
        this.anInterface = anInterface;

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(new Dimension(400, 300));
        setLocation((int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth() - getWidth()) / 2,
                (int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight() - getHeight()) / 2);
        setContentPane(ContentPane);
        setModal(true);

        for (StudyData t : arr) {
            listModel.addElement(t);
        }
        jList.setModel(listModel);
        DefaultListCellRenderer renderer = new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                label.setText(((StudyData) (value)).getName());
                return label;
            }
        };
        jList.setCellRenderer(renderer);

        addButton.addActionListener(this::addButtonClick);
        editButton.addActionListener(this::editButtonClick);
        removeButton.addActionListener(this::removeButtonClick);
        saveButton.addActionListener(this::saveButtonClick);
        cancelButton.addActionListener(this::cancelButtonClick);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                StudyData[] arr = new StudyData[listModel.getSize()];
                for (int i = 0; i < listModel.getSize(); i++) {
                    arr[i] = listModel.get(i);
                }
                anInterface.exit(arr);
                super.windowClosing(e);
            }
        });
    }

    public static StudyData[] getInstance(StudyData[] studyData, DataModifyInterface anInterface) {
        DataModifyDialog dialog = new DataModifyDialog(studyData, anInterface);
        dialog.setVisible(true);
        StudyData[] arr = new StudyData[dialog.listModel.getSize()];
        for (int i = 0; i < dialog.listModel.getSize(); i++) {
            arr[i] = dialog.listModel.get(i);
        }
        return arr;
    }


    private void cancelButtonClick(ActionEvent actionEvent) {
        StudyData[] arr = new StudyData[listModel.getSize()];
        for (int i = 0; i < listModel.getSize(); i++) {
            arr[i] = listModel.get(i);
        }
        anInterface.exit(arr);
        dispose();
    }

    private void saveButtonClick(ActionEvent event) {
        StudyData[] arr = new StudyData[listModel.getSize()];
        for (int i = 0; i < listModel.getSize(); i++) {
            arr[i] = listModel.get(i);
        }
        anInterface.exit(arr);
        dispose();
    }

    private void removeButtonClick(ActionEvent event) {
        if (jList.getSelectedIndex() < 0) return;
        if (anInterface.remove(listModel.get(jList.getSelectedIndex()))) {
            listModel.remove(jList.getSelectedIndex());
        }
    }

    private void editButtonClick(ActionEvent event) {
        if (jList.getSelectedIndex() < 0) return;
        StudyData t = listModel.get(jList.getSelectedIndex());
        listModel.set(jList.getSelectedIndex(), anInterface.edit(t));
    }

    private void addButtonClick(ActionEvent event) {
        StudyData t = anInterface.add();
        if (t != null) listModel.addElement(t);
    }
}
