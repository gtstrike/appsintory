package model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import view.PesanDialog;

/**
 *
 * @author Guci
 */
public class Pekerjaan {

    private String kodePekerjaan, namaPekerjaan;
    private int jumlahTugas;
    private String pesan;
    private Object[][] list;
    private final Koneksi koneksi = new Koneksi();
    private final PesanDialog pesanDialog = new PesanDialog();

    public String getKodePekerjaan() {
        return kodePekerjaan;
    }

    public void setKodePekerjaan(String kodePekerjaan) {
        this.kodePekerjaan = kodePekerjaan;
    }

    public String getNamaPekerjaan() {
        return namaPekerjaan;
    }

    public void setNamaPekerjaan(String namaPekerjaan) {
        this.namaPekerjaan = namaPekerjaan;
    }

    public int getJumlahTugas() {
        return jumlahTugas;
    }

    public void setJumlahTugas(int jumlahTugas) {
        this.jumlahTugas = jumlahTugas;
    }

    public String getPesan() {
        return pesan;
    }

    public Object[][] getList() {
        return list;
    }

    public void setList(Object[][] list) {
        this.list = list;
    }

    public boolean simpan() {
        boolean adaKesalahan = false;
        Connection connection;

        if ((connection = koneksi.getConnection()) != null) {
            int jumlahSimpan = 0;
            boolean simpan = false;
            Statement sta;
            ResultSet rset;

            try {
                String SQLStatemen = "select * from tbpekerjaan where kodepekerjaan='" + kodePekerjaan + "'";
                sta = connection.createStatement();
                rset = sta.executeQuery(SQLStatemen);
                rset.next();

                if (rset.getRow() > 0) {
                    if (pesanDialog.tampilkanPilihan("Kode pekerjaan sudah ada\nApakah data diperbaharui?", "Konfirmasi", new Object[]{"Ya", "Tidak"}) == 0) {
                        simpan = true;
                        SQLStatemen = "update tbpekerjaan set namapekerjaan='" + namaPekerjaan + "', jumlahtugas='" + jumlahTugas + "' where kodepekerjaan='" + kodePekerjaan + "'";
                        sta = connection.createStatement();
                        jumlahSimpan = sta.executeUpdate(SQLStatemen);
                    }
                } else {
                    simpan = true;
                    SQLStatemen = "insert into tbpekerjaan values ('" + kodePekerjaan + "','" + namaPekerjaan + "','" + jumlahTugas + "')";
                    sta = connection.createStatement();
                    jumlahSimpan = sta.executeUpdate(SQLStatemen);
                }

                if (simpan) {
                    if (jumlahSimpan < 1) {
                        adaKesalahan = true;
                        pesan = "Gagal menyimpan data pekerjaan";
                    }
                }

                sta.close();
                rset.close();
                connection.close();
            } catch (SQLException ex) {
                adaKesalahan = true;
                pesan = "Tidak dapat membuka tabel tbpekerjaan\n" + ex;
            }
        } else {
            adaKesalahan = true;
            pesan = "Tidak dapat melakukan koneksi ke server\n" + koneksi.getPesanKesalahan();
        }
        return !adaKesalahan;
    }

    public boolean baca(String kodePekerjaan) {
        boolean adaKesalahan = false;
        Connection connection;

        if ((connection = koneksi.getConnection()) != null) {
            Statement sta;
            ResultSet rset;

            try {
                String SQLStatemen = "select * from tbpekerjaan where kodepekerjaan='" + kodePekerjaan + "'";
                sta = connection.createStatement();
                rset = sta.executeQuery(SQLStatemen);
                rset.next();

                if (rset.getRow() > 0) {
                    this.kodePekerjaan = rset.getString("kodepekerjaan");
                    this.namaPekerjaan = rset.getString("namapekerjaan");
                    this.jumlahTugas = rset.getInt("jumlahtugas");
                } else {
                    adaKesalahan = true;
                    pesan = "Kode pekerjaan \"" + kodePekerjaan + "\" tidak ditemukan";
                }

                sta.close();
                rset.close();
                connection.close();
            } catch (SQLException ex) {
                adaKesalahan = true;
                pesan = "Tidak dapat membuka tabel tbpekerjaan\n" + ex;
            }
        } else {
            adaKesalahan = true;
            pesan = "Tidak dapat melakukan koneksi ke server\n" + koneksi.getPesanKesalahan();
        }
        return !adaKesalahan;
    }

    public boolean bacaData() {
    boolean adaKesalahan = false;
    Connection connection;

    list = new Object[0][0];

    if ((connection = koneksi.getConnection()) != null) {

        String SQLStatemen;
        Statement sta = null;
        ResultSet rset = null;

        try {
            SQLStatemen = "select kodepekerjaan, namapekerjaan from tbpekerjaan";

            sta = connection.createStatement();
            rset = sta.executeQuery(SQLStatemen);

            java.util.ArrayList<Object[]> data =
                    new java.util.ArrayList<>();

            while (rset.next()) {
                data.add(new Object[]{
                    rset.getString("kodepekerjaan"),
                    rset.getString("namapekerjaan")
                });
            }

            list = data.toArray(new Object[0][2]);

            rset.close();
            sta.close();
            connection.close();

        } catch (SQLException ex) {
            adaKesalahan = true;
            pesan = "Tidak dapat membuka tabel tbpekerjaan\n" + ex;
        }

    } else {
        adaKesalahan = true;
        pesan = "Tidak dapat melakukan koneksi ke server\n"
                + koneksi.getPesanKesalahan();
    }

    return !adaKesalahan;
}

    public boolean hapus(String kodePekerjaan) {
        boolean adaKesalahan = false;
        Connection connection;

        if ((connection = koneksi.getConnection()) != null) {
            int jumlahHapus;
            Statement sta;

            try {
                String SQLStatemen = "delete from tbpekerjaan where kodepekerjaan='" + kodePekerjaan + "'";
                sta = connection.createStatement();
                jumlahHapus = sta.executeUpdate(SQLStatemen);

                if (jumlahHapus < 1) {
                    pesan = "Kode pekerjaan tidak ditemukan";
                    adaKesalahan = true;
                }

                sta.close();
                connection.close();
            } catch (SQLException ex) {
                adaKesalahan = true;
                pesan = "Tidak dapat membuka tabel tbpekerjaan\n" + ex;
            }
        } else {
            adaKesalahan = true;
            pesan = "Tidak dapat melakukan koneksi ke server\n" + koneksi.getPesanKesalahan();
        }
        return !adaKesalahan;
    }
}
