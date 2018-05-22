package app.frame;

import app.data.Holiday;
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
import java.util.Date;
import java.util.Properties;

public class HolidayDialogModify extends JDialog {
    private JDatePickerImpl jDatePicker;
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JPanel datePickerPanel;
    private JCheckBox repeatCheckBox;
    private boolean isChange;

    private HolidayDialogModify() {
        initialDatePicker();
        datePickerPanel.setLayout(new FlowLayout());
        datePickerPanel.add(jDatePicker);
        setContentPane(contentPane);
        setModal(true);

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
        setLocationRelativeTo(null);
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
        isChange = true;
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        isChange = false;
        dispose();
    }

    public static Holiday getModify() {
        HolidayDialogModify dialogModify = new HolidayDialogModify();
        dialogModify.isChange = false;
        dialogModify.setTitle("Додавання вихідного");
        dialogModify.setVisible(true);
        //
        Calendar calendar = (Calendar) dialogModify.jDatePicker.getJFormattedTextField().getValue();
        return dialogModify.isChange ? new Holiday(calendar.getTime(), dialogModify.repeatCheckBox.isSelected()) : null;
    }

    public static Holiday getModify(final Holiday holiday) {
        HolidayDialogModify dialogModify = new HolidayDialogModify();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(holiday.getDate());
        dialogModify.jDatePicker.getJFormattedTextField().setValue(calendar);
        dialogModify.repeatCheckBox.setSelected(holiday.getRepeat());
        dialogModify.isChange = false;
        dialogModify.setTitle("Редагування вихідного");
        dialogModify.setVisible(true);
        //
        if (dialogModify.isChange) {
            Calendar calendar2 = (Calendar) dialogModify.jDatePicker.getJFormattedTextField().getValue();
            holiday.setDate(calendar2.getTime());
            holiday.setRepeat(dialogModify.repeatCheckBox.isSelected());
        }
        return holiday;
    }

    public static void main(String[] args) {
        HolidayDialogModify dialog = new HolidayDialogModify();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(System.currentTimeMillis()));
        calendar.set(2015, Calendar.SEPTEMBER, 5);

        dialog.jDatePicker.getJFormattedTextField().setValue(calendar);
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
