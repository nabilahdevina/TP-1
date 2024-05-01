
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Scanner;

public class OrderGenerator {
    private static final Scanner input = new Scanner(System.in);

    // Menampilkan header program
    public static void showHeader() {
        System.out.println(">>=======================================<<");
        System.out.println("|| ___         ___       _ ||");
        System.out.println("||| . \\ ___ ___ ___ | __>___ ___ _| |||");
        System.out.println("||| | |/ ._>| . \\/ ._>| _>/ . \\/ . \\/ . |||");
        System.out.println("|||___/\\___.| _/\\___.|_| \\___/\\___/\\___|||");
        System.out.println("||     |_|             ||");
        System.out.println(">>=======================================<<");
        System.out.println();
    }

    // Menampilkan menu program
    public static void showMenu() {
        System.out.println("Pilih menu:");
        System.out.println("1. Generate Order ID");
        System.out.println("2. Generate Bill");
        System.out.println("3. Keluar");
    }

    // Method untuk meng-generate Order ID
    public static String generateOrderID(String namaRestoran, String tanggalOrder, String noTelepon) {
        // Validasi nama restoran
        if (namaRestoran.length() < 4) {
            return "Tidak Valid";
        }

        // Ambil 4 karakter pertama dari nama restoran
        String restoranCode = namaRestoran.substring(0, 4).toUpperCase();

        // Ambil 8 karakter dari tanggal pemesanan
        String[] tanggalParts = tanggalOrder.split("/");
        String tanggalCode = "";
        for (String part : tanggalParts) {
            tanggalCode += String.format("%02d", Integer.parseInt(part));
        }

        // Ambil 2 karakter dari nomor telepon
        int sumDigits = 0;
        for (char c : noTelepon.toCharArray()) {
            if (Character.isDigit(c)) {
                sumDigits += Character.getNumericValue(c);
            }
        }
        int teleponCode = sumDigits % 100;
        String teleponCodeStr = String.format("%02d", teleponCode);

        // Hitung checksum
        String combinedStr = restoranCode + tanggalCode + teleponCodeStr;
        int sumEven = 0;
        int sumOdd = 0;
        for (int i = 0; i < combinedStr.length(); i++) {
            int value = getCodeValue(combinedStr.charAt(i));
            if (i % 2 == 0) {
                sumEven += value;
            } else {
                sumOdd += value;
            }
        }
        int checksum1 = sumEven % 36;
        int checksum2 = sumOdd % 36;
        char checksum1Char = getCharFromCode(checksum1);
        char checksum2Char = getCharFromCode(checksum2);

        return restoranCode + tanggalCode + teleponCodeStr + checksum1Char + checksum2Char;
    }

    // Method untuk mendapatkan nilai numerik dari karakter sesuai dengan Code 39 Character Set
    private static int getCodeValue(char c) {
        if (Character.isDigit(c)) {
            return Character.getNumericValue(c);
        } else {
            return c - 'A' + 10;
        }
    }

    // Method untuk mendapatkan karakter berdasarkan nilai numerik sesuai dengan Code 39 Character Set
    private static char getCharFromCode(int code) {
        if (code < 10) {
            return (char) (code + '0');
        } else {
            return (char) (code - 10 + 'A');
        }
    }

    // Method untuk meng-generate bill
    public static String generateBill(String orderID, String lokasi) {
        int ongkosKirim;
        String tanggalPemesanan = orderID.substring(4, 6) + "/" + orderID.substring(6, 8) + "/" + orderID.substring(8, 12);
        switch (lokasi.toUpperCase()) {
            case "P":
                ongkosKirim = 10000;
                break;
            case "U":
                ongkosKirim = 20000;
                break;
            case "T":
                ongkosKirim = 35000;
                break;
            case "S":
                ongkosKirim = 40000;
                break;
            case "B":
                ongkosKirim = 60000;
                break;
            default:
                return "Harap masukkan lokasi pengiriman yang ada pada jangkauan!";
        }

        // Format biaya ongkos kirim menggunakan mata uang rupiah dan titik sebagai separator ribuan
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(new Locale("in", "ID"));
        String biayaOngkosKirim = formatRupiah.format((double) ongkosKirim).replace(",00", "").replace("Rp", "Rp ");

        return "Bill:\n" +
                "Order ID: " + orderID + "\n" +
                "Tanggal Pemesanan: " + tanggalPemesanan + "\n" +
                "Lokasi Pengiriman: " + lokasi.toUpperCase() + "\n" +
                "Biaya Ongkos Kirim: " + biayaOngkosKirim + "\n";
    }

    public static void main(String[] args) {
        showHeader();
        while (true) {
            showMenu();
            System.out.print("Pilih menu (1/2/3): ");
            int menuChoice = input.nextInt();
            input.nextLine(); // Consume newline character

            if (menuChoice == 1) {
                String namaRestoran;
                while (true) {
                    System.out.print("Nama Restoran: ");
                    namaRestoran = input.nextLine();
                    if (namaRestoran.length() < 4) {
                        System.out.println("Nama Restoran tidak valid!");
                    } else {
                        break;
                    }
                }

                String tanggalOrder;
                while (true) {
                    System.out.print("Tanggal Pemesanan (DD/MM/YYYY): ");
                    tanggalOrder = input.nextLine();
                    if (!tanggalOrder.matches("\\d{2}/\\d{2}/\\d{4}")) {
                        System.out.println("Tanggal Pemesanan dalam format DD/MM/YYYY !");
                    } else {
                        break;
                    }
                }

                String noTelepon;
                while (true) {
                    System.out.print("Nomor Telepon: ");
                    noTelepon = input.nextLine();
                    if (!noTelepon.matches("\\d+")) {
                        System.out.println("Harap masukkan nomor telepon dalam bentuk bilangan bulat positif.");
                    } else {
                        break;
                    }
                }

                String orderID = generateOrderID(namaRestoran, tanggalOrder, noTelepon);
                System.out.println("Order ID: " + orderID);
            } else if (menuChoice == 2) {
                System.out.print("Masukkan Order ID: ");
                String orderID = input.nextLine();
                System.out.print("Masukkan Lokasi Pengiriman: ");
                String lokasi = input.nextLine();

                String bill = generateBill(orderID, lokasi);
                System.out.println(bill);
            } else if (menuChoice == 3) {
                System.out.println("Terima kasih telah menggunakan layanan kami.");
                break;
            } else {
                System.out.println("Menu tidak valid. Silakan pilih kembali.");
            }
        }
    }

}