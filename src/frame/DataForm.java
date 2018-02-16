package frame;

import app.data.StudyData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

/**
 * Created by Vladimir on 16/02/18.
 **/
public abstract class DataForm<T extends StudyData> extends JFrame {
    private JButton addButton;
    private JButton editButton;
    private JButton removeButton;
    private JButton cancelButton;
    private JList<T> jList;
    private DefaultListModel<T> listModel = new DefaultListModel<>();
    private DefaultListCellRenderer renderer = new DefaultListCellRenderer() {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel)super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            label.setText(((T)(value)).getName());
            return label;
        }
    };

    private JPanel ContentPane;
    private JButton saveButton;

    public DataForm(T[] arr) {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(new Dimension(400, 300));
        setLocation((int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth() - getWidth()) / 2,
                (int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight() - getHeight()) / 2);
        setContentPane(ContentPane);
        for (T t : arr) {
            listModel.addElement(t);
        }
        jList.setModel(listModel);
        jList.setCellRenderer(renderer);

        addButton.addActionListener(this::addButtonClick);
        editButton.addActionListener(this::editButtonClick);
        removeButton.addActionListener(this::removeButtonClick);
        saveButton.addActionListener(this::saveButtonClick);
        cancelButton.addActionListener(e -> dispose());
    }

    DefaultListModel getListModel() {
        return listModel;
    }

    abstract public void saveButtonClick(ActionEvent event);

    abstract public void removeButtonClick(ActionEvent event);

    abstract public void editButtonClick(ActionEvent event);

    abstract public void addButtonClick(ActionEvent event);

}
