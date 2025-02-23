# Tucil Strategi Algoritma 1

## Deskripsi Program
Program ini merupakan aplikasi pencari solusi dari permainan [**IQ Puzzler Pro**](https://www.smartgamesusa.com) dengan menggunakan algoritma _**Brute Force**_, atau menampilkan bahwa solusi tidak ditemukan jika tidak ada solusi yang mungkin dari puzzle.

![clip](https://github.com/user-attachments/assets/16104990-f757-4390-8ebb-bcaa2989b713)


## Cara Menjalankan GUI
1. Pastikan Java Development Kit (JDK) telah terinstall.
2. Lakukan *clone* repository atau klik link berikut untuk melakukan proses [download](https://github.com/albertchriss/Tucil1_13523077/raw/refs/heads/main/iq-puzzler-solver.jar) file executable `iq-puzzler-solver.jar`.
3. _Double click_ file executable `iq-puzzler-solver.jar` untuk menjalankan GUI program.

## Alternatif Cara Menjalankan GUI
### Setup
GUI proyek ini di-*build* menggunakan Maven (sebuah *build tool* untuk JavaFX). Berikut adalah langkah-langkah untuk melakukan *setup* Maven.
1. Pastikan Java Development Kit (JDK) sudah terinstall.
2. Download zip binary Maven (version 3.9.9 saat proyek ini dibuat) untuk windows dari tautan https://maven.apache.org/download.cgi. 
3. Lakukan ekstraksi file .zip tersebut dan simpan pada *directory* yang mudah diakses seperti `C:\Program Files\Apache\Maven`. Seharusnya Anda memperoleh hasil seperti `C:\Program Files\Apache\Maven\apache-maven-3.x.x`.
4. Buat system variable baru dengan *name* `MAVEN_HOME` dan value `C:\Program Files\Apache\Maven`.
5. Masukkan path `bin` pada direktori `apache-maven-3.x.x` ke path environment variables. 
### GUI
Setelah melakukan setup Maven, cara menjalankan GUI adalah:
1. Buka Maven explorer (Biasanya ada pada pojok kanan bawah jika menggunakan VSCode).
2. Tekan gui -> Plugins -> javafx -> javafx:run

## Cara menjalankan Program (tanpa GUI)
1. Compile to `bin` directory

    ```
    javac -d bin src/main/java/puzzle/*.java 
    ```

2. Run 

    ```
    java -cp bin puzzle/Main
    ```
