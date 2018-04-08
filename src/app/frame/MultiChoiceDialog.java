package app.frame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Vladimir on 28/03/18.
 **/
public class MultiChoiceDialog<E> extends JDialog {
    private JList<E> mainList;
    private DefaultListModel<E> listModel;
    private JPanel contentPane;
    private JButton saveButton;
    private JButton cancelButton;
    private JLabel selectedItemsLabel;
    private JButton choiceAllButton;
    private List<E> inList;
    private boolean closeFlag = false;

    public MultiChoiceDialog(final List<E> list, final List<E> selectedList) {
        inList = selectedList;
        setContentPane(contentPane);
        setModal(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(new Dimension(300, 400));
        setMinimumSize(new Dimension(300, 300));
        setLocationRelativeTo(null);
        listModel = new DefaultListModel<>();
        list.forEach(listModel::addElement);
        mainList.setModel(listModel);
        saveButton.addActionListener(e -> {
            closeFlag = true;
            dispose();
        });
        cancelButton.addActionListener(e -> {
            closeFlag = false;
            dispose();
        });
        mainList.setSelectionModel(new DefaultListSelectionModel() {
            @Override
            public void setSelectionInterval(int i, int i1) {
                if(super.isSelectedIndex(i)) super.removeSelectionInterval(i, i1);
                else super.addSelectionInterval(i, i1);
            }
        });
        selectedList.forEach(e -> mainList.addSelectionInterval(listModel.indexOf(e), listModel.indexOf(e)));
        selectedItemsLabel.setText(mainList.getSelectedIndices().length + " елементів обрано");
        mainList.addListSelectionListener(e -> {
            selectedItemsLabel.setText(mainList.getSelectedIndices().length + " елементів обрано");
            choiceAllButton.setText("Обрати все");
        });
        choiceAllButton.addActionListener(e -> {
            if (choiceAllButton.getText().equals("Обрати все")) {
                mainList.addSelectionInterval(0, mainList.getModel().getSize() - 1);
                choiceAllButton.setText("Прибрати все");
            } else {
                mainList.removeSelectionInterval(0, mainList.getModel().getSize() - 1);
                choiceAllButton.setText("Обрати все");
            }
        });
        setTitle("Оберіть потрібні елементи");
    }

    public List<E> showAndGetData() {
        this.setVisible(true);
        return closeFlag ? this.mainList.getSelectedValuesList() : this.inList;
    }

    public static void main(String[] args) {
        System.out.println(new MultiChoiceDialog<>(Arrays.asList("1", "2", "3", "4"), Arrays.asList("1", "2")).showAndGetData());
    }
}
