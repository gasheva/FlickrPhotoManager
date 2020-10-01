package ru.gasheva.client.views;

import ru.gasheva.client.Photo;
import ru.gasheva.client.Tag;
import ru.gasheva.client.controllers.IPhotoInfoControl;
import ru.gasheva.client.controllers.PhotoInfoControlEdit;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.util.Set;

public class InfoPhotoForm extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField tfTitle;
    private JTable tblTags;
    private JTextArea tfDescription;
    private JSpinner spLatitude;
    private JSpinner spLongitude;
    private JSpinner spAccuracy;
    private JButton btnRemoveTag;
    private JButton btnAddTag;
    private JTextField tfUrl;
    private DefaultTableModel myModel;
    private Photo photo;
    IPhotoInfoControl control;

    public InfoPhotoForm(IPhotoInfoControl control, Photo photo) {
        this.photo = photo;
        this.control = control;
    }
    public void createView(){
        setContentPane(contentPane);
        setModal(true);

        getRootPane().setDefaultButton(buttonOK);

        createControls();
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

    }
    public void createControls(){
        tblTags.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        createEmptyTable();
        control.enableElements();
        control.setDefaultValues();

        btnAddTag.addActionListener(e->BtnAddTagClicked());
        btnRemoveTag.addActionListener(e->BtnRemoveTagClicked());

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
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    public void createTable(){
        photo.getTags().forEach(x->myModel.addRow(new Object[]{x.getId(), x.getTag()}));
        tblTags.setModel(myModel);
    }

    private void BtnRemoveTagClicked(){
        if (tblTags.getSelectedRow()!=-1){
            Tag tag = new Tag((String)myModel.getValueAt(tblTags.getSelectedRow(), 0), (String)myModel.getValueAt(tblTags.getSelectedRow(), 1));
            ((PhotoInfoControlEdit)control).removeTag(tag);
        }
        else showMessage("Select tag");

    }
    private void BtnAddTagClicked(){
        JTextField tf = new JTextField();
        JComponent[] inputs = new JComponent[]{
                new JLabel("New tags separated by space:"),
                tf
        };
        String tag="";
        int result = JOptionPane.showConfirmDialog(null, inputs, "Create Tag", JOptionPane.PLAIN_MESSAGE);
        if (result==JOptionPane.OK_OPTION){
            tag = tf.getText().trim();
            ((PhotoInfoControlEdit)control).addTag(tag);
        }

    }
    private void onOK() {
        control.ok();
    }
    private void onCancel() {
        control.cancel();
    }

    public boolean checkLocationBoundary(){
        if ((Integer)spAccuracy.getValue()<1||(Integer)spAccuracy.getValue()>16) return false;
        if ((Integer)spLongitude.getValue()<-180||(Integer)spAccuracy.getValue()>180) return false;
        if ((Integer)spLatitude.getValue()<-90||(Integer)spLatitude.getValue()>90) return false;
        return true;
    }



    private void createUIComponents() {
        spLatitude = new JSpinner(new SpinnerNumberModel(0, -90, 90, 1));
        spLongitude = new JSpinner(new SpinnerNumberModel(0, -180, 180, 1));
        spAccuracy = new JSpinner(new SpinnerNumberModel(16, 1, 16, 1));
    }

    public void getPhotoUpdate(){
        System.out.println("Val - "+spLatitude.getValue());
        photo.setLatit((Integer) spLatitude.getValue());
        photo.setLongit((Integer) spLongitude.getValue());
        photo.setAccurancy((Integer) spAccuracy.getValue());
    }
    public boolean areFieldsEmpty(){
        if (tfTitle.getText().trim().length()==0) return true;
        return false;
    }
    public void showMessage(String msg){
        JOptionPane.showMessageDialog(this, msg);
    }

    private void createEmptyTable(){
        createMyModel();
        tblTags.setModel(myModel);
        hideId();
    }
    private void hideId(){
        tblTags.getColumnModel().getColumn(0).setMinWidth(0);
        tblTags.getColumnModel().getColumn(0).setMaxWidth(0);
        tblTags.getColumnModel().getColumn(0).setWidth(0);
    }
    private void createMyModel() {
        myModel = new DefaultTableModel(new String[]{"id", "Tag"}, 0) {
            @Override
            public Class<?> getColumnClass(int column) {
                return String.class;
            }
            @Override
            public boolean isCellEditable(int arg0, int arg1) {
                return false;
            }   //TODO для редактирования разрешено

        };
    }
    public void Dispose(){
        dispose();
    }
    //region Enable controls
    public void SpLatitudeEnable(boolean enable){spLatitude.setEnabled(enable);}
    public void SpLongitudeEnable(boolean enable){spLongitude.setEnabled(enable);}
    public void SpAccuracyEnable(boolean enable){spAccuracy.setEnabled(enable);}
    public void TfTitleEnable(boolean enable){tfTitle.setEditable(enable); }
    public void TfDescriptionEnable(boolean enable){tfDescription.setEditable(enable);}
    public void TfUrlEnable(boolean enable){tfUrl.setEditable(enable);}
    public void BtnRemoveTagEnable(boolean enable){btnRemoveTag.setEnabled(enable);}
    public void BtnAddTagEnable(boolean enable){btnAddTag.setEnabled(enable);}
    //endregion contro


    //region set controls default values
    public void setSpLatitude(int val){spLatitude.setValue(val);}
    public void setSpLongitude(int val){spLongitude.setValue(val);}
    public void setSpAccuracy(int val){spAccuracy.setValue(val);}
    public void setTfTitle(String val){tfTitle.setText(val);}
    public void setTfDescription(String val){tfDescription.setText(val);}
    public void setTfUrl(String val){tfUrl.setText(val);}
    //endregion

    public void addTagsToTable(Set<Tag> tags) {
        createMyModel();
        tags.forEach(x->myModel.addRow(new Object[]{x.getId(), x.getTag()}));
        tblTags.setModel(myModel);
        hideId();
    }


    public void removeTagFromTable(Tag tag) {
        myModel.removeRow(tblTags.getSelectedRow());
        tblTags.setModel(myModel);
    }
}
