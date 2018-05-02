package app.frame;

import org.jdatepicker.impl.DateComponentFormatter;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilCalendarModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Calendar;
import java.util.Properties;

public class HolidayDialogModify extends JDialog {
    private JDatePickerImpl jDatePicker;
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JPanel datePickerPanel;
    private JButton додатиButton;
    private JList list1;
    private JButton видалитиButton;

    public HolidayDialogModify() {
        initialDatePicker();
        datePickerPanel.setLayout(new FlowLayout());
        datePickerPanel.add(jDatePicker);
        setContentPane(contentPane);
        setModal(true);
        setLocationRelativeTo(null);

        getRootPane().setDefaultButton(buttonOK);

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
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        pack();
        setVisible(true);
    }

    private void initialDatePicker() {
        Properties properties = new Properties();
        properties.setProperty("text.today", "Сьогодні");
        properties.setProperty("text.mouth", "Місяць");
        properties.setProperty("text.year", "Рік");
        properties.setProperty("text.clear", "Очистити");

        jDatePicker = new JDatePickerImpl(
                new JDatePanelImpl(new UtilCalendarModel(Calendar.getInstance()), properties), new DateComponentFormatter());
    }

    private void onOK() {
        // add your code here
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        HolidayDialogModify dialog = new HolidayDialogModify();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
