/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package frame;
import db.Koneksi;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import model.Album;

public class AlbumTambahFrame extends JFrame {
    JLabel lKode = new JLabel(" Kode Album:");
    JLabel lNama = new JLabel(" Nama Album:");
    JLabel lGrup = new JLabel(" Nama Grup:");
    JLabel lHarga = new JLabel(" Harga(Rp):");
    
    JTextField eKode = new JTextField();
    JTextField eNama = new JTextField();
    JTextField eGrup = new JTextField();
    JTextField eHarga = new JTextField();
    
    JButton bSimpan = new JButton("Simpan");
    JButton bBatal = new JButton("Batal");
    
    private AlbumFrame parent;
    private Album albumLama = null;
    
    public AlbumTambahFrame(AlbumFrame parent) {
        this.parent = parent;
        initUI("Tambah Katalog Album Baru");
    }
    
    public AlbumTambahFrame(AlbumFrame parent, Album album) {
        this.parent = parent;
        this.albumLama = album;
        initUI("Ubah Data Kalatog Album");
        
        eKode.setText(album.getKodeAlbum());
        eKode.setEditable(false);
        eNama.setText(album.getNamaAlbum());
        eGrup.setText(album.getNamaGrup());
        eHarga.setText(String.valueOf(album.getHarga()));
    }
    
    private void initUI(String title) {
        setTitle(title);
        setSize(380, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(5, 2, 5, 10));
        
        add(lKode); add(eKode);
        add(lNama); add(eNama);
        add(lGrup); add(eGrup);
        add(lHarga); add(eHarga);
        add(bBatal); add(bSimpan);
        
        bSimpan.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                prosesSimpan();
            }
        });
        
        bBatal.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                dispose();
            }
        });
    }
        
        private void prosesSimpan(){
            if (eKode.getText().isEmpty() || eNama.getText().isEmpty() || eGrup.getText().isEmpty() || eHarga.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Semua data wajib diisi!");
                return;
            }
            
            String sql;
            if (albumLama == null) {
                sql = "INSERT INTO album (kode_album, nama_album, nama_grup, harga) VALUES (?, ?, ?, ?)";
        } else {
            sql = "UPDATE album SET nama_album=?, nama_grup=?, harga=? WHERE kode_album=?";
        }
            try (Connection con = Koneksi.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
                if (albumLama == null) {
                 ps.setString(1, eKode.getText());
                 ps.setString(2, eNama.getText());
                 ps.setString(3, eGrup.getText());
                 ps.setInt(4, Integer.parseInt(eHarga.getText()));   
                } else {
                    ps.setString(1, eNama.getText());
                    ps.setString(2, eGrup.getText());
                    ps.setInt(3, Integer.parseInt(eHarga.getText()));
                    ps.setString(4, eKode.getText());
                }
                
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Data Katalog Album Berhasil Disimpan!");
                
                parent.loadData("");
                dispose();
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(this, "Harga harus berupa angka!");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Gagal menyimpan ke database: " + ex.getMessage());
            }
        }   
}