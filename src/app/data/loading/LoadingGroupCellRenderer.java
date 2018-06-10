package app.data.loading;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Vladimir on 24/05/18.
 **/
public class LoadingGroupCellRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        GroupLoad groupLoad = (GroupLoad) value;
        label.setText(groupLoad.getGroup().getName());
        label.setToolTipText(groupLoad.getGroup().getComments());
        return label;
    }
}
