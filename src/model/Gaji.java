package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Guci
 */
public class Gaji {

    private String ktp;
    private String pesan;
    private Object[][] listGaji;
    private final Koneksi koneksi = new Koneksi();

    public String getKtp() {
        return ktp;
    }

    public void setKtp(String ktp) {
        this.ktp = ktp;
    }

    public String getPesan() {
        return pesan;
    }

    public void setPesan(String pesan) {
        this.pesan = pesan;
    }

    public Object[][] getListGaji() {
        return listGaji;
    }

    public void setListGaji(Object[][] listGaji) {
        this.listGaji = listGaji;
    }

    public boolean simpan() {
        boolean adaKesalahan = false;
        Connection connection;

        if ((connection = koneksi.getConnection()) != null) {
            int jumlahSimpan = 0;
            String SQLStatemen;
            PreparedStatement preparedStatement;

            // Hapus dulu data lama milik karyawan ini
            try {
                SQLStatemen = "delete from tbgaji where ktp=?";
                preparedStatement = connection.prepareStatement(SQLStatemen);
                preparedStatement.setString(1, ktp);
                preparedStatement.executeUpdate();
                preparedStatement.close();
            } catch (SQLException ex) {
            }

            // Insert semua baris dari tabel
            for (Object[] recGaji : listGaji) {
                try {
                    // recGaji: [0]=kodepekerjaan, [1]=gajibersih, [2]=gajikotor, [3]=tunjangan
                    SQLStatemen = "insert into tbgaji(ktp, kodepekerjaan, gajibersih, gajikotor, tunjangan) values (?,?,?,?,?)";
                    preparedStatement = connection.prepareStatement(SQLStatemen);
                    preparedStatement.setString(1, ktp);
                    for (int i = 0; i < 4; i++) {
                        preparedStatement.setString(2 + i, recGaji[i] != null ? recGaji[i].toString() : "");
                    }
                    jumlahSimpan += preparedStatement.executeUpdate();
                    preparedStatement.close();
                } catch (SQLException ex) {
                }
            }

            try { connection.close(); } catch (SQLException ex) {}

            if (jumlahSimpan > 0) {
                adaKesalahan = false;
            }
        } else {
            adaKesalahan = true;
            pesan = "Tidak dapat melakukan koneksi ke server\n" + koneksi.getPesanKesalahan();
        }
        return !adaKesalahan;
    }

    public boolean baca(String ktp) {
        boolean adaKesalahan = false;
        Connection connection;
        this.ktp = ktp;
        listGaji = null;

        if ((connection = koneksi.getConnection()) != null) {
            String SQLStatemen;
            PreparedStatement preparedStatement;
            ResultSet rset;

            try {
                SQLStatemen = "select * from tbgaji where ktp=?";
                preparedStatement = connection.prepareStatement(SQLStatemen);
                preparedStatement.setString(1, ktp);
                rset = preparedStatement.executeQuery();
                rset.next();
                rset.last();
                listGaji = new Object[rset.getRow()][4];
                rset.first();
                int i = 0;

                do {
                    if (!rset.getString("kodepekerjaan").equals("")) {
                        // [0]=kodepekerjaan, [1]=gajibersih, [2]=gajikotor, [3]=tunjangan
                        listGaji[i] = new Object[]{
                            rset.getString("kodepekerjaan"),
                            rset.getObject("gajibersih"),
                            rset.getObject("gajikotor"),
                            rset.getObject("tunjangan")};
                    }
                    i++;
                } while (rset.next());

                if (listGaji.length > 0) {
                    adaKesalahan = false;
                }

                preparedStatement.close();
                rset.close();
                connection.close();
            } catch (SQLException ex) {
                adaKesalahan = true;
                pesan = "Tidak dapat membaca data gaji karyawan\n" + ex.getMessage();
            }
        } else {
            adaKesalahan = true;
            pesan = "Tidak dapat melakukan koneksi ke server\n" + koneksi.getPesanKesalahan();
        }
        return !adaKesalahan;
    }

    /**
     * Mencetak laporan gaji.
     * Membutuhkan library JasperReports — tambahkan lewat NetBeans:
     * Project Properties → Libraries → Add Library → JasperReports.
     *
     * @param ruang nomor ruang (0 = semua ruang)
     * @return true jika berhasil
     */
    public boolean cetakLaporan(int ruang) {
        pesan = "Fitur cetak laporan belum tersedia.\n"
              + "Tambahkan library JasperReports terlebih dahulu\n"
              + "melalui Project Properties \u2192 Libraries.";
        return false;
    }
}
