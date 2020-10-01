package ru.gasheva.client.views;

import ru.gasheva.client.PhotoObserver;
import ru.gasheva.client.controllers.MainControl;
import ru.gasheva.client.models.PhotoModel;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.io.File;

public class MainForm extends JFrame implements PhotoObserver {
    private JPanel mainPanel;
    private JTable tblPhoto;
    private JButton btnGet;
    private JButton btnEdit;
    private JButton btnDelete;
    private JButton btnAdd;
    private JButton btnLogIn;
    private JButton btnGetInfo;
    private JLabel lblUser;
    private DefaultTableModel myModel;
    private MainControl control;
    PhotoModel photoModel;

    public MainForm(MainControl control, PhotoModel photoModel) {
        this.control = control;
        this.photoModel = photoModel;
        photoModel.registerPhotoObserver(this);
    }
    public void createView(){
        this.setTitle("FlickrPost");
        setContentPane(mainPanel);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        setVisible(true);

    }
    public void createControllers(){
        tblPhoto.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        createEmptyTable();

        btnAdd.addActionListener(e -> btnAddClicked());
        btnGet.addActionListener(e -> btnGetClicked());
        btnEdit.addActionListener(e -> btnEditClicked());
        btnDelete.addActionListener(e -> btnDeleteClicked());
        btnLogIn.addActionListener(e -> btnLogInClicked());
        btnGetInfo.addActionListener(e -> btnGetInfoClicked());

    }
    private void btnAddClicked(){
        control.addPhoto();
    }
    private void btnLogInClicked(){
        control.logIn();
    }
    private void btnGetInfoClicked(){
        control.getInfo();
    }
    public String getSelectedPhotoId(){
        return (String) tblPhoto.getValueAt(tblPhoto.getSelectedRow(), 0);
    }
    private void btnEditClicked(){
        control.editPhoto();
    }

    public int indexRowSelect(){
        return tblPhoto.getSelectedRow();
    }
    private void btnDeleteClicked(){
        control.deletePhoto();
    }
    private void btnGetClicked(){
        control.getPhotos();
    }

    public File choosePhoto(){
        JFileChooser c = new JFileChooser();
        FileFilter imageFilter = new FileNameExtensionFilter(
                "Image files", ImageIO.getReaderFileSuffixes());
        c.removeChoosableFileFilter(c.getFileFilter());
        c.addChoosableFileFilter(imageFilter);
        int rVal = c.showOpenDialog(this);
        if (rVal!=JFileChooser.APPROVE_OPTION) return null;

        return c.getSelectedFile();

    }

    //region Enable buttons
    public void btnEditEnable(boolean isEnable){
        btnEdit.setEnabled(isEnable);
    }
    public void btnAddEnable(boolean isEnable){
        btnAdd.setEnabled(isEnable);
    }public void btnGetEnable(boolean isEnable){
        btnGet.setEnabled(isEnable);
    }
    public void btnGetInfoEnable(boolean isEnable){
        btnGetInfo.setEnabled(isEnable);
    }
    public void btnDeleteEnable(boolean isEnable){
        btnDelete.setEnabled(isEnable);
    }
    public void btnLogInEnable(boolean isEnable){
        btnLogIn.setEnabled(isEnable);
    }
    //endregion
    public void setLblUser(String name){
        lblUser.setText(name);
    }

    public void createEmptyTable(){
        createMyModel();
        tblPhoto.setModel(myModel);
        hideId();
    }
    private void hideId(){
        tblPhoto.getColumnModel().getColumn(0).setMinWidth(0);
        tblPhoto.getColumnModel().getColumn(0).setMaxWidth(0);
        tblPhoto.getColumnModel().getColumn(0).setWidth(0);
    }
    private void createMyModel() {
        myModel = new DefaultTableModel(new String[]{"id", "Photo", "URL"}, 0) {
            @Override
            public Class<?> getColumnClass(int column) {
                return String.class;
            }
            @Override
            public boolean isCellEditable(int arg0, int arg1) {
                return false;
            }

        };
    }
    public void showMessage(String msg){
        JOptionPane.showMessageDialog(this, msg);
    }
    public void Dispose(){
        photoModel.removePhotoObserver(this);
        dispose();
    }
    @Override
    public void update() {
        createMyModel();
        for(int i=0; i<photoModel.size(); i++){
            myModel.addRow(new Object[]{photoModel.get(i).getId(), photoModel.get(i).getTitle(), ""});
        }
        tblPhoto.setModel(myModel);
        hideId();

    }

    public void addPhotoInTable(String id, String name) {
        myModel.addRow(new Object[]{id, name});
        tblPhoto.setModel(myModel);
    }

    public void deletePhotoInTable() {
        myModel.removeRow(tblPhoto.getSelectedRow());
        tblPhoto.setModel(myModel);
    }
}
