/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package frame;
import db.Koneksi;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.*;
import model.Album;
        
public class AlbumFrame extends JFrame {
    JLabel jLabel1 = new JLabel("Cari");
    JTextField eCari = new JTextField();
    JButton bCari = new JButton("Cari");
    
    String header[] = {"Kode Album", "Nama Album", "Nama Grup", "Harga"};
    DefaultTableModel tableModel = new DefaultTableModel(header, 0);
    JTable tAlbum = new JTable(tableModel);
    JScrollPane jScrollPane = new JScrollPane(tAlbum);
    
    JButton bTambah = new JButton("Tambah");
    JButton bUbah = new JButton("Ubah");
    JButton bHapus = new JButton("Hapus");
    JButton bBatal = new JButton("Batal");
    JButton bTutup = new JButton("Tutup");
    Album album;
    
    public AlbumFrame() {
        setTitle("Kelola Data Album");
        setSize(650, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        
        JPanel pAtas = new JPanel(new BorderLayout(5, 5));
        pAtas.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        pAtas.add(jLabel1, BorderLayout.WEST);
        pAtas.add(eCari, BorderLayout.CENTER);
        pAtas.add(bCari, BorderLayout.EAST);
        add(pAtas, BorderLayout.NORTH);
        
        jScrollPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        add(jScrollPane, BorderLayout.CENTER);
        
        JPanel pBawah = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        pBawah.add(bTambah); pBawah.add(bUbah); pBawah.add(bHapus); pBawah.add(bBatal); pBawah.add(bTutup);
        add(pBawah, BorderLayout.SOUTH);
        loadData("");
        
        eCari.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                loadData(eCari.getText());
            }
        });
        bCari.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            loadData(eCari.getText());
        }
        });
        
        bTambah.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AlbumTambahFrame tambah = new AlbumTambahFrame(AlbumFrame.this);
                tambah.setVisible(true);
            }
        });
        
       bUbah.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               int barisTerpilih = tAlbum.getSelectedRow();
               if (barisTerpilih == -1){
                   JOptionPane.showMessageDialog(AlbumFrame.this, "Isi data terlebih dahulu!");
                   return;
               }
               album = new Album();
               album.setKodeAlbum(tableModel.getValueAt(barisTerpilih, 0).toString());
               album.setNamaAlbum(tableModel.getValueAt(barisTerpilih, 1).toString());
               album.setNamaGrup(tableModel.getValueAt(barisTerpilih, 2).toString());
               album.setHarga(Integer.parseInt(tableModel.getValueAt(barisTerpilih, 3).toString()));
               new AlbumTambahFrame(AlbumFrame.this, album).setVisible(true);
           }
       });
       
       bHapus.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               int barisTerpilih = tAlbum.getSelectedRow();
               if (barisTerpilih == -1) {
                   JOptionPane.showMessageDialog(AlbumFrame.this, "Pilih data yang ingin dihapus!");
                   return;
               }
               String kode = tableModel.getValueAt(barisTerpilih, 0).toString();
               int konfirmasi = JOptionPane.showConfirmDialog(AlbumFrame.this, "Hapus kode " + kode + "?", "Hapus", JOptionPane.YES_NO_OPTION);
               if (konfirmasi == JOptionPane.YES_OPTION) {
                    hapusData(kode);
               }    
           }
       });
       
       bBatal.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               eCari.setText("");
               loadData("");
           }
       });
       
       bTutup.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               dispose();
           }
       });
    }
    
    public void loadData(String keyword) {
        tableModel.setRowCount(0);
        String sql = "SELECT * FROM album WHERE kode_album LIKE ? OR nama_album LIKE ? OR nama_grup LIKE ?";
        try (Connection con = Koneksi.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");
            ps.setString(3, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                tableModel.addRow(new Object[]{rs.getString("kode_album"), rs.getString("nama_album"), rs.getString("nama_grup"), rs.getInt("harga")});
            }
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }
    
    private void hapusData(String kode){
        try (Connection con = Koneksi.getConnection(); PreparedStatement ps = con.prepareStatement("DELETE FROM album WHERE kode_album = ?")) {
            ps.setString(1, kode);
            ps.executeUpdate();
            loadData("");
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }
}