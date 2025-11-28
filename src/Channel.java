import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public abstract class Channel {
    private int idKanal;
    private int idPengguna;
    private Date tanggalPembuatanKanal;
    protected String namaKanal;
    protected String deskripsiKanal;
    protected VideoList uploaded = new VideoList();
    protected List<User> subscribers = new ArrayList<>();

    public Channel(String namaKanal, String deskripsiKanal) {
        this.namaKanal = namaKanal;
        this.deskripsiKanal = deskripsiKanal;
        this.tanggalPembuatanKanal = java.sql.Date.valueOf(LocalDate.now());
    }

    public Channel(int idKanal, String namaKanal, String deskripsiKanal, Date tanggal) {
        this.namaKanal = namaKanal;
        this.deskripsiKanal = deskripsiKanal;
        this.idKanal = idKanal;
        this.tanggalPembuatanKanal = tanggal;
    }

    public Date getTanggalPembuatanKanal() {
        return tanggalPembuatanKanal;
    }

    public void setNamaKanal(String namaKanal) throws SQLException {
        this.namaKanal = namaKanal;
        // Export to database
        String query = """
                    UPDATE Kanal
                    SET namaKanal = ?
                    WHERE idKanal = ?
                """;
        PreparedStatement ps = MainApp.konektor.getConnection().prepareStatement(query);
        ps.setString(1, getNamaKanal());
        ps.setInt(2, getIdKanal());
        MainApp.konektor.updateTable(ps);
    }

    public String getDeskripsiKanal() {
        return deskripsiKanal;
    }

    public int getIdKanal() {
        return idKanal;
    }

    public int getIdPengguna() {
        return idPengguna;
    }

    public void setDeskripsiKanal(String deskripsiKanal) throws SQLException {
        this.deskripsiKanal = deskripsiKanal;
        String query = """
                    UPDATE Kanal
                    SET deskripsiKanal = ?
                    WHERE idKanal = ?
                """;
        PreparedStatement ps = MainApp.konektor.getConnection().prepareStatement(query);
        ps.setString(1, getDeskripsiKanal());
        ps.setInt(2, getIdKanal());
        MainApp.konektor.updateTable(ps);
    }

    public VideoList getUploaded() {
        return uploaded;
    }

    public void setUploaded(VideoList uploaded) {
        this.uploaded = uploaded;
    }

    public void setSubscribers(List<User> subscribers) {
        this.subscribers = subscribers;
    }

    public void setIdKanal(int idKanal) {
        this.idKanal = idKanal;
    }

    public void setTanggalPembuatanKanal(Date tanggalPembuatanKanal) {
        this.tanggalPembuatanKanal = tanggalPembuatanKanal;
    }

    public static Channel makeChannel(int idPengguna, String nama, String deskripsi, int tipeChannel)
            throws SQLException {
        String query = """
                INSERT INTO Kanal (idPengguna, namaKanal, deskripsiKanal, tanggalPembuatanKanal)
                VALUES(?, ?, ?, ?)
                """;
        PreparedStatement ps = MainApp.konektor.getConnection().prepareStatement(query);
        ps.setInt(1, idPengguna);
        ps.setString(2, nama);
        ps.setString(3, deskripsi);
        ps.setDate(4, new java.sql.Date(System.currentTimeMillis()));
        MainApp.konektor.updateTable(ps);

        // ResultSet rs = ps.getGeneratedKeys();
        // int idChannel = -1;
        // if (rs.next()) {
        // idChannel = rs.getInt(1);
        // }

        Channel channel;
        if (tipeChannel == 1) {
            channel = new ChannelIndividu(nama, deskripsi);
            updateTipeChannel(1, idPengguna);
        } else {
            channel = new ChannelGrup(nama, deskripsi);
            updateTipeChannel(2, idPengguna);
        }

        return channel;
    }


    public abstract void exportChannel() throws SQLException;

    @Override
    public String toString() {
        try {
            subscribers = importSubscriber();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String channel = String.format("%-20s %-30s %-10d", namaKanal, deskripsiKanal, subscribers.size());
        return channel;
    }

    public String getNamaKanal() {
        return namaKanal;
    }

    public void getChannelDetail(Scanner sc) throws SQLException {
        importSubscriber();
        importVideo();
        String action = "";
        while (true) {
            System.out.println("==== CHANNEL DETAIL ====");
            System.out.println("Channel Name : " + namaKanal);
            System.out.println("Description : " + deskripsiKanal);
            System.out.println("Created Date : " + tanggalPembuatanKanal);
            System.out.println("Subscribers Count : " + subscribers.size());
            System.out.println("Total Videos : " + uploaded.size());
            System.out.println("========================");
            System.out.println("Done ? (Y/y)");
            System.out.print("Answer : ");
            action = sc.next();
            if (action.toUpperCase().equals("Y")) {
                return;
            }
        }
    }
}