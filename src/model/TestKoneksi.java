package model;

import java.sql.Connection;

public class TestKoneksi {

    public static void main(String[] args) {

        Koneksi koneksi = new Koneksi();

        Connection conn = koneksi.getConnection();

        if (conn != null) {

            System.out.println("KONEKSI BERHASIL!");

        } else {

            System.out.println("KONEKSI GAGAL!");
            System.out.println(koneksi.getPesanKesalahan());
        }
    }
}