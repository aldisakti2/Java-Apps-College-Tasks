import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;

import static java.awt.EventQueue.invokeLater;
import javax.swing.JFileChooser;


public class Peternakan extends JFrame{
    private JTextField namaField;
    private JTextField nikField;
    private JComboBox jnsTernak;
    private JSpinner jmlTernak;
    private JTextField cariField;
    private JButton cariBtn;
    private JButton addButton;
    private JButton deleteButton;
    private JButton updateButton;
    private JButton clearAllButton;
    private JTable tblData;
    private JButton exitButton;
    private JButton exportButton;
    private JPanel AllContent;
    private JTextField alamatField;
    private JTextField noTelpon;
    String jenisTernak, sql;
    private TableRowSorter sorter;

    //Initiate Connection to SQL through db.java
    java.sql.Connection conn = db.configDB();

    @SuppressWarnings("unchecked")
    public Peternakan() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setContentPane(AllContent);
        this.pack();

        String[] jTernak = {null,"Ayam","Bebek","Burung Puyuh"};
        jnsTernak.setModel(new DefaultComboBoxModel(jTernak));
        jnsTernak.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                jenisTernak = (String) jnsTernak.getSelectedItem();
            }
        });

        jmlTernak.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
            }
        });

        //Button Section
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    if ((int)jmlTernak.getValue() > 0 && null != jnsTernak.getSelectedItem()) {
                        sql = "INSERT INTO dternak (Nama, NIK, Jenis_Ternak, Jumlah_Ternak, Alamat, No_Telp) VALUES (?,?,?,?,?,?);";
                        java.sql.PreparedStatement pst = conn.prepareStatement(sql);
                        pst.setString(1,namaField.getText());
                        pst.setBigDecimal(2, new BigDecimal(nikField.getText()));
                        pst.setString(3, (String) jnsTernak.getSelectedItem());
                        pst.setInt(4, (Integer) jmlTernak.getValue());
                        pst.setString(5, alamatField.getText());
                        pst.setString(6, noTelpon.getText());
                        pst.execute();
                        JOptionPane.showMessageDialog(null, "Penyimpanan Data Berhasil");
                    } else if(null == jnsTernak.getSelectedItem()){
                        JOptionPane.showMessageDialog(null, "Mohon Masukan Jenis Ternak Anda");
                    }
                    else {
                        JOptionPane.showMessageDialog(null, "Mohon Masukan Jumlah Ternak Anda");
                    }
                } catch (Exception f) {
                    JOptionPane.showMessageDialog(null, "Penyimpanan Data GAGAL", "Gagal" ,JOptionPane.ERROR_MESSAGE);
                }
                load_table();
                empty();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String tmp;
                    sql = "DELETE FROM dternak WHERE NIK=?;";

                    //Tambahan Mouse Click
                    if (nikField.getText().equals("")) {
                        tmp = (String) tblData.getValueAt(tblData.getSelectedRow(), 2);
                    } else {
                        tmp = nikField.getText();
                    }

                    java.sql.PreparedStatement pst=conn.prepareStatement(sql);
                    pst.setBigDecimal(1, new BigDecimal(tmp));
                    pst.execute();
                    JOptionPane.showMessageDialog(null, "Penghapusan Data "+ tmp +" Berhasil");
                } catch (Exception f) {
                    if (nikField.getText().equals("") || tblData.getValueAt(tblData.getSelectedRow(), 2).equals("")){
                        JOptionPane.showMessageDialog(null, "Mohon Klik Cell Data atau Masukan NIK dari Data yang Ingin Di Hapus");
                    }
                    JOptionPane.showMessageDialog(null, "Penghapusan Data GAGAL", "Gagal" ,JOptionPane.ERROR_MESSAGE);
                }
                load_table();
                empty();
            }
        });

        updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    sql = "UPDATE dternak SET Nama=?,Jenis_Ternak=?,Jumlah_Ternak=?,Alamat=?,No_Telp=? WHERE NIK=?;";
                    java.sql.PreparedStatement pst=conn.prepareStatement(sql);
                    pst.setString(1,namaField.getText());
                    pst.setString(2, (String) jnsTernak.getSelectedItem());
                    pst.setInt(3, (Integer) jmlTernak.getValue());
                    pst.setString(4, alamatField.getText());
                    pst.setString(5, noTelpon.getText());
                    pst.setInt(6, Integer.parseInt(nikField.getText()));
                    pst.execute();
                    JOptionPane.showMessageDialog(null, "Pembaharuan Data Berhasil");
                } catch (Exception f) {
                    if (nikField.getText().equals("")){
                        JOptionPane.showMessageDialog(null, "Mohon Masukan NIK dari Data yang Ingin Di Ubah");
                    }
                    JOptionPane.showMessageDialog(null, "Pembaharuan Data GAGAL", "Gagal" ,JOptionPane.ERROR_MESSAGE);
                }
                load_table();
                empty();
            }
        });

        clearAllButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int confirm = JOptionPane.showConfirmDialog(null,"Apakah Anda Benar - Benar ingin MENGHAPUS semua data?", "Konfirmasi Hapus Data",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (confirm == JOptionPane.YES_OPTION) {
                        sql = "TRUNCATE dternak;";
                        java.sql.PreparedStatement pst = conn.prepareStatement(sql);
                        pst.execute();
                        JOptionPane.showMessageDialog(null, "Penghapusan SELURUH Data BERHASIL");
                    } else if (confirm == JOptionPane.NO_OPTION){
                        JOptionPane.showMessageDialog(null, "Penghapusan SELURUH Data DIBATALKAN");
                    }
                } catch (Exception f) {
                    JOptionPane.showMessageDialog(null, "Penghapusan SELURUH Data GAGAL", "Gagal" ,JOptionPane.ERROR_MESSAGE);
                }
                load_table();
                empty();
            }
        });

        cariBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                search(cariField.getText());
            }
        });

        exportButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try{
                    JFileChooser fileChooser = new JFileChooser();
                    int retval = fileChooser.showSaveDialog(exportButton);

                    if (retval == JFileChooser.APPROVE_OPTION) {
                        File file = fileChooser.getSelectedFile();
                        if (file != null) {
                            if (!file.getName().toLowerCase().endsWith(".pdf")) {
                                file = new File(file.getParentFile(), file.getName() + ".pdf");
                            }
                            try {
                                ExportToPDF exp = new ExportToPDF();
                                exp.writeToPDF(tblData, file.toPath());

                                //Jika Ingin Export ke Excel
                                //ExportTable exp = new ExportTable();
                                //exp.writeToExcell(tblData, file.toPath());
                                if (file.exists()) {
                                    JOptionPane.showMessageDialog(null, "Proses Export Berhasil");
                                    Desktop.getDesktop().open(file);
                                }
                            } catch (FileNotFoundException d) {
                                d.printStackTrace();
                                System.out.println("not found");
                            }
                        }
                    }
                } catch (Exception f){
                    f.printStackTrace();
                }
            }
        });

        //Load Section
        load_table();
        empty();

        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int konfirmasi = JOptionPane.showOptionDialog(null, "Ingin Keluar?",
                        "Keluar",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,
                        null,null,null);

                if (konfirmasi == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });

    }


    private void load_table() {
        // membuat tampilan model tabel
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("No");
        model.addColumn("Nama");
        model.addColumn("NIK");
        model.addColumn("Alamat");
        model.addColumn("Nomor Telp.");
        model.addColumn("Jenis Ternak");
        model.addColumn("Jumlah Ternak");

        //menampilkan data database kedalam tabel
        try {
            sql = "select * from dternak";
            model.setRowCount(0);
            java.sql.Statement stm=conn.createStatement();
            java.sql.ResultSet res=stm.executeQuery(sql);
            while(res.next()){
                model.addRow(new Object[]{
                        model.getRowCount() + 1,
                        res.getString(1),
                        res.getString(2),
                        res.getString(5),
                        res.getString(6),
                        res.getString(3),
                        res.getString(4)
                });
            }
            tblData.setModel(model);
            tblData.getTableHeader().setReorderingAllowed(false);
            sorter = new TableRowSorter<>(model);
            tblData.setRowSorter(sorter);


            //Tambahan Click Mouse
            tblData.addMouseListener(new TableMouseListener(tblData));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Database Belum Terkoneksi !!!", "Koneksi Database Gagal" ,JOptionPane.ERROR_MESSAGE);
            JOptionPane.showMessageDialog(null, "Mohon Cek Kembali Koneksi Database.", "Info Koneksi Database", JOptionPane.INFORMATION_MESSAGE);
            e.printStackTrace();
        }
    }

    public void empty(){
        namaField.setText(null);
        nikField.setText(null);
        alamatField.setText(null);
        noTelpon.setText(null);
        jnsTernak.setSelectedItem(null);
        jmlTernak.setValue(0);
    }

    public void search(String str){
        if (str.length() == 0 ){
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)"+str));
        }
    }

    public static void main(String[] args){
        invokeLater(() -> new Peternakan().setVisible(true));
    }
}
