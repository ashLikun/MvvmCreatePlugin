import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;

public class MvvmDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField nameField;
    private JTextField descField;
    private JTextField activityNameField;
    private JTextField viewModelNameField;
    private JTextField layoutNameField;
    private JTextField packageField;
    private JLabel nameLabel;
    private JLabel descLabel;
    private JLabel activityNameLabel;
    private JLabel layoutNameLabel;
    private JLabel viewModelNameLabel;
    private JLabel packageLabel;
    private JRadioButton activityRadioButton;
    private JRadioButton fragmentRadioButton;
    private String modeName;

    public MvvmDialog(String packageName, String modeName, DialogCallBack dialogCallBack) {
        this.modeName = modeName;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        initView(packageName);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                InputContentData data = new InputContentData();
                data.isActivity = activityRadioButton.isSelected();
                data.name = nameField.getText().trim();
                data.desc = descField.getText().trim();
                data.view = activityNameField.getText().trim();
                data.viewMode = viewModelNameField.getText().trim();
                data.layout = layoutNameField.getText().trim();
                data.packge = packageField.getText().trim();
                dialogCallBack.ok(data);
                dispose();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void initView(String packageName) {


        packageField.setText(packageName);
        //定义按钮组
        ButtonGroup group = new ButtonGroup();
        group.add(activityRadioButton);
        group.add(fragmentRadioButton);
        activityRadioButton.addChangeListener(changeEvent -> {
            onNameChange();
        });
        fragmentRadioButton.addChangeListener(changeEvent -> {
            onNameChange();
        });

        //焦点改变
        nameField.addFocusListener(new MyFocusListener());
        descField.addFocusListener(new MyFocusListener());
        activityNameField.addFocusListener(new MyFocusListener());
        viewModelNameField.addFocusListener(new MyFocusListener());
        layoutNameField.addFocusListener(new MyFocusListener());
        packageField.addFocusListener(new MyFocusListener());

        nameField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent documentEvent) {
                onNameChange();
            }

            @Override
            public void removeUpdate(DocumentEvent documentEvent) {
                onNameChange();
            }

            @Override
            public void changedUpdate(DocumentEvent documentEvent) {
                onNameChange();
            }
        });
    }

    public void onNameChange() {
        String modeNameSux = modeName.isEmpty() ? "" : modeName + "_";
        String nameText = nameField.getText().trim();
        if (!nameText.isEmpty()) {
            if (nameText.toLowerCase().contains("activity")) {
                nameText = nameText.toLowerCase().replace("activity", "");
            }
            if (nameText.toLowerCase().contains("viewmodel")) {
                nameText = nameText.toLowerCase().replace("viewmodel", "");
            }
            viewModelNameField.setText(nameText + "ViewModel");


            if (activityRadioButton.isSelected()) {
                activityNameField.setText(nameText + "Activity");

                layoutNameField.setText(modeNameSux + "activity" + Utils.activityToLayout(nameText));
            } else {
                activityNameField.setText(nameText + "Fragment");
                layoutNameField.setText(modeNameSux + "fragment" + Utils.activityToLayout(nameText));
            }
        } else {
            activityNameField.setText("");
            viewModelNameField.setText("");
            layoutNameField.setText("");
        }
    }

    private class MyFocusListener implements FocusListener {
        public void select(FocusEvent focusEvent, boolean isSelect) {
            nameLabel.setForeground(Color.decode("0x999999"));
            descLabel.setForeground(Color.decode("0x999999"));
            activityNameLabel.setForeground(Color.decode("0x999999"));
            layoutNameLabel.setForeground(Color.decode("0x999999"));
            viewModelNameLabel.setForeground(Color.decode("0x999999"));
            packageLabel.setForeground(Color.decode("0x999999"));
            if (focusEvent.getComponent() == nameField) {
                nameLabel.setForeground(isSelect ? Color.decode("0xff0000") : Color.decode("0x999999"));
            }
            if (focusEvent.getComponent() == descField) {
                descLabel.setForeground(isSelect ? Color.decode("0xff0000") : Color.decode("0x999999"));
            }
            if (focusEvent.getComponent() == activityNameField) {
                activityNameLabel.setForeground(isSelect ? Color.decode("0xff0000") : Color.decode("0x999999"));
            }
            if (focusEvent.getComponent() == layoutNameField) {
                layoutNameLabel.setForeground(isSelect ? Color.decode("0xff0000") : Color.decode("0x999999"));
            }
            if (focusEvent.getComponent() == viewModelNameField) {
                viewModelNameLabel.setForeground(isSelect ? Color.decode("0xff0000") : Color.decode("0x999999"));
            }
            if (focusEvent.getComponent() == packageField) {
                packageLabel.setForeground(isSelect ? Color.decode("0xff0000") : Color.decode("0x999999"));
            }
        }

        @Override
        public void focusGained(FocusEvent focusEvent) {
            select(focusEvent, true);
        }

        @Override
        public void focusLost(FocusEvent focusEvent) {
            select(focusEvent, false);
        }
    }

    public interface DialogCallBack {
        void ok(InputContentData data);
    }


}
