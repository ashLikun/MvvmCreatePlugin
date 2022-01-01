import Utils.activityToLayout
import java.awt.Color
import java.awt.event.*
import javax.swing.*
import javax.swing.event.ChangeEvent
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener

class MvvmDialog(packageName: String, var modeName: String, dialogOk: (InputContentData) -> Unit) : JDialog() {
    lateinit var contentPane: JPanel
    lateinit var buttonOK: JButton
    lateinit var buttonCancel: JButton
    lateinit var nameField: JTextField
    lateinit var descField: JTextField
    lateinit var activityNameField: JTextField
    lateinit var viewModelNameField: JTextField
    lateinit var layoutNameField: JTextField
    lateinit var packageField: JTextField
    lateinit var nameLabel: JLabel
    lateinit var descLabel: JLabel
    lateinit var activityNameLabel: JLabel
    lateinit var layoutNameLabel: JLabel
    lateinit var viewModelNameLabel: JLabel
    lateinit var packageLabel: JLabel
    lateinit var activityRadioButton: JRadioButton
    lateinit var fragmentRadioButton: JRadioButton
    lateinit var isListRadioButton: JRadioButton
    lateinit var isBindingRadioButton: JRadioButton
    private fun initView(packageName: String) {
        packageField.text = packageName
        //定义按钮组
        val group = ButtonGroup()
        group.add(activityRadioButton)
        group.add(fragmentRadioButton)
        activityRadioButton.addChangeListener({ changeEvent: ChangeEvent? -> onNameChange() })
        fragmentRadioButton.addChangeListener({ changeEvent: ChangeEvent? -> onNameChange() })

        //焦点改变
        nameField.addFocusListener(MyFocusListener())
        descField.addFocusListener(MyFocusListener())
        activityNameField.addFocusListener(MyFocusListener())
        viewModelNameField.addFocusListener(MyFocusListener())
        layoutNameField.addFocusListener(MyFocusListener())
        packageField.addFocusListener(MyFocusListener())
        nameField.document.addDocumentListener(object : DocumentListener {
            override fun insertUpdate(documentEvent: DocumentEvent) {
                onNameChange()
            }

            override fun removeUpdate(documentEvent: DocumentEvent) {
                onNameChange()
            }

            override fun changedUpdate(documentEvent: DocumentEvent) {
                onNameChange()
            }
        })
    }

    fun onNameChange() {
        val modeNameSux = if (modeName.isEmpty()) "" else modeName + "_"
        var nameText = nameField.text.trim { it <= ' ' }
        if (!nameText.isEmpty()) {
            if (nameText.lowercase().contains("activity")) {
                nameText = nameText.lowercase().replace("activity", "")
            }
            if (nameText.lowercase().contains("viewmodel")) {
                nameText = nameText.lowercase().replace("viewmodel", "")
            }
            viewModelNameField.text = nameText + "ViewModel"
            if (activityRadioButton.isSelected) {
                activityNameField.text = nameText + "Activity"
                layoutNameField.text = modeNameSux + "activity" + activityToLayout(nameText)
            } else {
                activityNameField.text = nameText + "Fragment"
                layoutNameField.text = modeNameSux + "fragment" + activityToLayout(nameText)
            }
        } else {
            activityNameField.text = ""
            viewModelNameField.text = ""
            layoutNameField.text = ""
        }
    }

    private fun createUIComponents() {
        // TODO: place custom component creation code here
    }

    private inner class MyFocusListener() : FocusListener {
        fun select(focusEvent: FocusEvent, isSelect: Boolean) {
            nameLabel.foreground = Color.decode("0x999999")
            descLabel.foreground = Color.decode("0x999999")
            activityNameLabel.foreground = Color.decode("0x999999")
            layoutNameLabel.foreground = Color.decode("0x999999")
            viewModelNameLabel.foreground = Color.decode("0x999999")
            packageLabel.foreground = Color.decode("0x999999")
            if (focusEvent.component === nameField) {
                nameLabel.foreground = if (isSelect) Color.decode("0xff0000") else Color.decode("0x999999")
            }
            if (focusEvent.component === descField) {
                descLabel.foreground = if (isSelect) Color.decode("0xff0000") else Color.decode("0x999999")
            }
            if (focusEvent.component === activityNameField) {
                activityNameLabel.foreground = if (isSelect) Color.decode("0xff0000") else Color.decode("0x999999")
            }
            if (focusEvent.component === layoutNameField) {
                layoutNameLabel.foreground = if (isSelect) Color.decode("0xff0000") else Color.decode("0x999999")
            }
            if (focusEvent.component === viewModelNameField) {
                viewModelNameLabel.foreground = if (isSelect) Color.decode("0xff0000") else Color.decode("0x999999")
            }
            if (focusEvent.component === packageField) {
                packageLabel.foreground = if (isSelect) Color.decode("0xff0000") else Color.decode("0x999999")
            }
        }

        override fun focusGained(focusEvent: FocusEvent) {
            select(focusEvent, true)
        }

        override fun focusLost(focusEvent: FocusEvent) {
            select(focusEvent, false)
        }
    }

    init {
        setContentPane(contentPane)
        isModal = true
        getRootPane().defaultButton = buttonOK
        initView(packageName)
        buttonOK.addActionListener(ActionListener {
            val data = InputContentData()
            data.isActivity = activityRadioButton.isSelected
            data.isList = isListRadioButton.isSelected
            data.isViewBinding = isBindingRadioButton.isSelected
            data.name = nameField.text.trim { it <= ' ' }
            data.desc = descField.text.trim { it <= ' ' }
            data.view = activityNameField.text.trim { it <= ' ' }
            data.viewMode = viewModelNameField.text.trim { it <= ' ' }
            data.layout = layoutNameField.text.trim { it <= ' ' }
            data.packge = packageField.text.trim { it <= ' ' }
            dialogOk.invoke(data)
            dispose()
        })
        buttonCancel.addActionListener(object : ActionListener {
            override fun actionPerformed(e: ActionEvent) {
                dispose()
            }
        })

        // call onCancel() when cross is clicked
        defaultCloseOperation = DO_NOTHING_ON_CLOSE
        addWindowListener(object : WindowAdapter() {
            override fun windowClosing(e: WindowEvent) {
                dispose()
            }
        })

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(object : ActionListener {
            override fun actionPerformed(e: ActionEvent) {
                dispose()
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
    }
}