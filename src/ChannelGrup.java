import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class ChannelGrup extends Channel {
    protected Map<Integer, Set<Integer>> listAkses = new HashMap<>();
    private String roles[] = { "Owner", "Manager", "Editor", "Editor Limited", "Subtitle Editor", "Viewer" };
    // 1 = Pemilik
    // 2 = Manajer
    // 3 = Editor
    // 4 = Editor Limited
    // 5 = Subtitle editor
    // 6 = viewer

    public ChannelGrup(int idKanal, String channelName, String channelDescription, Date tanggalPembuatanKanal) {
        super(idKanal, channelName, channelDescription, tanggalPembuatanKanal);
        initAkses();
    }

    public ChannelGrup(String channelName, String channelDescription) {
        super(channelName, channelDescription);
        initAkses();
    }

    public void initAkses() {
        for (int i = 1; i <= 6; i++) {
            listAkses.put(i, new HashSet<>());
        }
    }

    public int getJumlahMember() {
        int jmlh = 0;
        for (int i = 1; i <= 6; i++) {
            jmlh += listAkses.get(i).size();
        }
        return jmlh;
    }

    @Override
    public void exportChannel() throws SQLException {
        String expQuery = """
                    INSERT INTO KanalGrup (idKanal, jumlahMember)
                    VALUES (?, ?)
                """;
        PreparedStatement ps = MainApp.konektor.getConnection().prepareStatement(expQuery);
        ps.setInt(1, getIdKanal());
        ps.setInt(2, getJumlahMember());
        MainApp.konektor.updateTable(ps);
    }

    // private String[] roles = { "Owner", "Manager", "Editor", "Editor Limited",
    // "Subtitle Editor", "Viewer" };

    public void printMember() throws SQLException {
        importGroupAkses();
        System.out.println("==== CHANNEL GROUP MEMBER ====");
        System.out.println("Member Size : " + getJumlahMember());
        System.out.println();

        for (int i = 2; i <= 6; i++) {
            Set<Integer> listID = listAkses.get(i);
            String roleLabel = String.format("%-15s", roles[i - 1]);
            System.out.printf("%s (%d) :\n", roleLabel, (listID != null) ? listID.size() : 0);

            if (listID != null && !listID.isEmpty()) {
                int idx = 1;
                for (int idPeng : listID) {
                    System.out.printf("%d. %s\n", idx++, User.getEmailbyID(idPeng));
                }
            }

            System.out.println();
        }
    }

   
    public int addMember(User user, String email, int role) throws SQLException {
        if (getTingkatAkses(user.getIdPengguna()) > 1) {
            System.out.println("You don't have access");
            return 1;
        }

        // Cek apakah email sudah ada
        for (Set<Integer> members : listAkses.values()) {
            if (members.contains(User.getIdbyEmail(email)))
                return 2;
        }
        String query = """
                    INSERT INTO Diundang (idKanal, idPengguna, tipeAkses)
                    VALUES (?, ?, ?)
                """;
        PreparedStatement ps = MainApp.konektor.getConnection().prepareStatement(query);
        ps.setInt(1, getIdKanal());
        ps.setInt(2, User.getIdbyEmail(email));
        ps.setInt(3, role);
        MainApp.konektor.updateTable(ps);

        listAkses.get(role).add(User.getIdbyEmail(email));
        System.out.printf("%s invited as %s\n", email, roles[role-1]);
        changeTipePengguna(User.getIdbyEmail(email),3);
        return 0;
    }

    // Channel
    @Override
    public void changeName(Scanner sc, User user) throws SQLException {
        if (getTingkatAkses(user.getIdPengguna()) > 3) {
            System.out.println("You dont have access");
            return;
        }
        super.changeName(sc, user);
    }

    @Override
    public void changeDescription(Scanner sc, User user) throws SQLException {
        if (getTingkatAkses(user.getIdPengguna()) > 3) {
            System.out.println("You dont have access");
            return;
        }
        super.changeDescription(sc, user);
    }

    // Video
    @Override
    public void changeVideoSubtitle(Scanner sc, Video video, User user) throws SQLException {
        if (getTingkatAkses(user.getIdPengguna()) > 5) {
            System.out.println("You dont have access");
            return;
        }
        if (video.getIdPengguna() != user.getIdPengguna() && getTingkatAkses(user.getIdPengguna()) == 4) {
            // jika yg upload beda dengan yg mau hapus && yang hapus adalah editor Limited
            System.out.println("You dont have access");
            return;
        }
        super.changeVideoSubtitle(sc, video, user);
    }

    @Override
    public void changeVideoName(Scanner sc, Video video, User user) throws SQLException {
        if (getTingkatAkses(user.getIdPengguna()) > 4) {
            System.out.println("You dont have access");
            return;
        }
        if (video.getIdPengguna() != user.getIdPengguna() && getTingkatAkses(user.getIdPengguna()) == 4) {
            // jika yg upload beda dengan yg mau hapus && yang hapus adalah editor Limited
            System.out.println("You dont have access");
            return;
        }
        super.changeVideoName(sc, video, user);
    }

    // @Override
    // public void changeVideoLength(Scanner sc, Video video, User user) throws
    // SQLException {
    // if (getTingkatAkses(user.getIdPengguna()) > 4) {
    // System.out.println("You dont have access");
    // return;
    // }
    // if (video.getIdPengguna() != user.getIdPengguna() &&
    // getTingkatAkses(user.getIdPengguna()) == 4) {
    // // jika yg upload beda dengan yg mau hapus && yang hapus adalah editor
    // Limited
    // System.out.println("You dont have access");
    // return;
    // }
    // super.changeVideoLength(sc, video, user);
    // }

    @Override
    public void changeVideoDescription(Scanner sc, Video video, User user) throws SQLException {
        if (getTingkatAkses(user.getIdPengguna()) > 4) {
            System.out.println("You dont have access");
            return;
        }
        if (video.getIdPengguna() != user.getIdPengguna() && getTingkatAkses(user.getIdPengguna()) == 4) {
            // jika yg upload beda dengan yg mau hapus && yang hapus adalah editor Limited
            System.out.println("You dont have access");
            return;
        }
        super.changeVideoDescription(sc, video, user);
    }
}
