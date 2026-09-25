package view;

import javax.swing.JOptionPane;

/**
 * Dialog utilitas untuk menampilkan pesan dan pilihan konfirmasi.
 *
 * @author Guci
 */
public class PesanDialog {

    /**
     * Menampilkan dialog pilihan (konfirmasi) dan mengembalikan indeks pilihan.
     * 
     * @param pesan   isi pesan yang ditampilkan
     * @param judul   judul dialog
     * @param pilihan array objek yang menjadi tombol pilihan
     * @return indeks pilihan yang diklik (0 = pilihan pertama, dst), atau -1 jika ditutup
     */
    public int tampilkanPilihan(String pesan, String judul, Object[] pilihan) {
        return JOptionPane.showOptionDialog(
            null,
            pesan,
            judul,
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            pilihan,
            pilihan[0]
        );
    }

    /**
     * Menampilkan dialog pesan informasi biasa.
     *
     * @param pesan isi pesan
     * @param judul judul dialog
     */
    public void tampilkanPesan(String pesan, String judul) {
        JOptionPane.showMessageDialog(null, pesan, judul, JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Menampilkan dialog pesan kesalahan.
     *
     * @param pesan isi pesan kesalahan
     */
    public void tampilkanKesalahan(String pesan) {
        JOptionPane.showMessageDialog(null, pesan, "Kesalahan", JOptionPane.ERROR_MESSAGE);
    }
}
