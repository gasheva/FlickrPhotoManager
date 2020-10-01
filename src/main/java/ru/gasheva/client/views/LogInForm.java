package ru.gasheva.client.views;

import ru.gasheva.client.controllers.LogInControl;
import ru.gasheva.client.models.UserModel;

import javax.swing.*;
import java.awt.event.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;

public class LogInForm extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField tfLogin;
    private JPanel passwordPanel;
    private JButton btnCreate;
    private JTextField tfPassword;
    private JPasswordField pfPassword;
    private UserModel model;
    private LogInControl control;
    private JLabel lblPassword;


    public LogInForm(LogInControl control, UserModel model) {
        this.control=control;
        this.model = model;
    }

    public void createView(){
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);


        createControllers();
        setSize(400, 200);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    public void createControllers(){

        buttonOK.addActionListener(e -> onOK());

        buttonCancel.addActionListener(e -> onCancel());
        btnCreate.addActionListener(e -> btnCreateNewUserClicked());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void btnCreateNewUserClicked(){
        control.createNewUser();
    }

    public String getLogin(){return tfLogin.getText().trim();}
    public String getPassword(){return tfPassword.getText().trim();}
    public void showMessage(String msg){
        JOptionPane.showMessageDialog(this, msg);
    }

    public void Dispose(){
        dispose();
    }
    private void onOK() {
        control.Authorise();
    }
    private void onCancel() {
        control.cancel();
    }

    public String askCode(String url) {
        String code = "";
        JTextField tf = new JTextField();
        JTextField tfUrl = new JTextField();
        tfUrl.setText(url);
        tfUrl.setEditable(false);
        JComponent[] inputs = new JComponent[]{
                new JLabel("Now go and authorize ScribeJava here:"),
                tfUrl,
                new JLabel("And paste the verifier here: "),
                tf
        };
        int result = JOptionPane.showConfirmDialog(null, inputs, "Auth", JOptionPane.PLAIN_MESSAGE);
        if (result==JOptionPane.OK_OPTION){
          code = tf.getText().trim();
        }
        return code;
    }
}
