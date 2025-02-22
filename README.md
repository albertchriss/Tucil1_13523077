# Tucil Strategi Algoritma 1

## Cara Menjalankan GUI
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
