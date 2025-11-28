import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class MainApp {
    public static Controller controller = Controller.getInstance();
    public static ConnectDB konektor = ConnectDB.getInstance();
    public static boolean logOut = false;
    

    
    /*
     * Home page bagi pengguna yang belum pernah membuat channel
     * Pengguna dapat membuat channel, menonton video, melihat subscriber, dan log out
     * Jika pengguna membuat channel, akan langsung dialihkan ke home page yang sudah memiliki channel
     */
    public static void homePageNoChannel(Scanner sc) throws SQLException{
        System.out.println();
        System.out.printf("=== WELCOME TO YOUTUBE %s ===\n", controller.getUser().getNamaPengguna());
        boolean check = false;

        while (true) {
            printCommand(new String[] { "Create Channel", "Video Page", "Subscription", "Log Out" });
            System.out.print("Pick your Action : ");
            int action = sc.nextInt();
            System.out.println();

            switch (action) {
                case 1:
                    check = makeChannelPage(sc);
                    break;
                case 2:
                    videoPage(sc);
                    break;
                case 3:
                    subscribePage(sc);
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Command is not valid!");
                    break;
            }

            if (check) {
                homePageHaveChannel(sc);
                break;
            }
        }
    }

    /*
     * Page bagi pengguna yang ingin membuat channel
     * Pengguna perlu memasukkan nama, deskripsi, dan tipe channel yang akan dibuat
     */
    
    /*
     * Home page bagi pengguna yang sudah pernah membuat channel
     * Fitur sama seperti home page yang belum punya channel
     * Bedanya sekarang tombol buat channel menjadi my channel untuk melihat detail channel
     */
    public static void homePageHaveChannel(Scanner sc) throws SQLException{
        System.out.println();
        System.out.printf("=== WELCOME TO YOUTUBE %s ===\n", controller.getUser().getNamaPengguna());
        while (true) {
            printCommand(new String[] { "My Channel", "Video Page", "Subscription", "Log Out" });
            System.out.print("Pick your Action : ");
            int action = sc.nextInt();
            System.out.println();

            switch (action) {
                case 1:
                    if (controller.getChannel() instanceof ChannelGrup) {
                        myChannelGroupPage(sc);
                    } else {
                        myChannelPage(sc);
                    }
                    break;
                case 2:
                    videoPage(sc);
                    break;
                case 3:
                    subscribePage(sc);
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Command is not valid!");
                    break;
            }
        }
    }
    
    public static void homePageContentCreatorNoChannel(Scanner sc) throws SQLException{
        System.out.println();
        System.out.printf("=== WELCOME TO YOUTUBE %s ===\n", controller.getUser().getNamaPengguna());
        boolean check = false;
        while (true) {
            printCommand(new String[] { "Make Channel", "Group Channel", "Video Page", "Subscription", "Log Out" });
            System.out.print("Pick your Action : ");
            int action = sc.nextInt();
            System.out.println();

            switch (action) {
                case 1:
                    check = makeChannelPage(sc);
                    break;
                case 2:
                    contentCreatorChannelPage(sc);
                    break;
                case 3:
                    videoPage(sc);
                    break;
                case 4:
                    subscribePage(sc);
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Command is not valid!");
                    break;
            }
            if (check) {
                homePageContentCreatorHaveChannel(sc);
                break;
            }
        }
    }
    
    public static void homePageContentCreatorHaveChannel(Scanner sc) throws SQLException{
        System.out.println();
        System.out.printf("=== WELCOME TO YOUTUBE %s ===\n", controller.getUser().getNamaPengguna());
        while (true) {
            printCommand(new String[] { "My Channel", "Group Channel", "Video Page", "Subscription", "Log Out" });
            System.out.print("Pick your Action : ");
            int action = sc.nextInt();
            System.out.println();

            switch (action) {
                case 1:
                    if (controller.getChannel() instanceof ChannelGrup) {
                        myChannelGroupPage(sc);
                    } else {
                        myChannelPage(sc);
                    }
                    break;
                case 2:
                    contentCreatorChannelPage(sc);
                    break;
                case 3:
                    videoPage(sc);
                    break;
                case 4:
                    subscribePage(sc);
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Command is not valid!");
                    break;
            }
        }
    }
    
    
    public static void videoPage(Scanner sc) throws SQLException {
        System.out.println();
        int currentPage = 0;
        ResultSet rs = konektor.getTable("SELECT COUNT(idVideo) FROM Video");
        rs.next();
        int totalPage = (int) Math.ceil(rs.getInt(1) / 5.0);

        while (true) {
            controller.showCurrentPage(currentPage);
            printCommand(new String[] {"Next Page", "Previous Page", "Watch Video (by no.)", "Detail Video", 
                            "Exit to Home"});
            System.out.print("Pick your Action: ");
            int action = sc.nextInt();
            sc.nextLine();
            System.out.println();

            switch (action) {
                case 1:
                    if (currentPage + 1 == totalPage) {
                        System.out.println("=== Already on the last page! ===");
                        break;
                    }
                    currentPage++;
                    break;
                case 2:
                    if (currentPage == 0) {
                        System.out.println("=== Already on the first page! ===");
                        break;
                    }
                    currentPage--;
                    break;
                case 3:
                    System.out.print("Enter video index to watch: ");
                    int idx = sc.nextInt();
                    sc.nextLine();
                    controller.selectAndPlay(idx - currentPage * 5, sc);
                    break;
                case 4:
                    System.out.print("Enter index to see video detail: ");
                    int detailIdx = sc.nextInt();
                    sc.nextLine();
                    controller.showDetails(detailIdx - currentPage * 5, sc);
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid command.");
            }
        }
    }

    /*
     * Page untuk melihat detail channel individu
     * Pengguna dapat mengupload, melihat upload, menghapus video, edit video, melihat 
     * detail channel, dan melihat laporan
     */

    public static void subscribePage(Scanner sc) throws SQLException {
        User user = controller.getUser();
        System.out.printf("=== %s SUBSCRIPTION ===\n", user.namaPengguna);
        int channelIndex;

        while (true) {
            printCommand(new String[] { "Subscriber List", "Unsubscribe", "Channel Detail", "Back" });
            System.out.print("Pick your Action : ");
            int action = sc.nextInt();
            System.out.println();
            switch (action) {
                case 1:
                    System.out.printf("%-5s %-20s %-30s %-10s\n", "NO", "NAMA CHANNEL", "DESKRIPSI", "BANYAK SUBSCRIBER");
                    for (int i = 0; i < user.getSubscribedChannels().size(); i++) {
                        System.out.printf("%-5d %s\n", i + 1, user.getSubscribedChannels().get(i));
                    }
                    break;
                case 2:
                    System.out.print("Which Channel : ");
                    channelIndex = sc.nextInt() - 1;
                    sc.nextLine();
                    if (channelIndex < 0 || channelIndex >= user.getSubscribedChannels().size()) {
                        System.out.println("Channel index out of range!\n");
                        break;
                    }
                    user.unsubscribe(channelIndex, user.getSubscribedChannels().get(channelIndex));
                    break;
                case 3:
                    System.out.print("Which Channel: ");
                    channelIndex = sc.nextInt() - 1;
                    sc.nextLine();
                    if (channelIndex < 0 || channelIndex >= user.getSubscribedChannels().size()) {
                        System.out.println("Channel index out of range!\n");
                        break;
                    }
                    user.getSubscribedChannels().get(channelIndex).getChannelDetail(sc);
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Command is not valid!\n");
                    break;
            }
            System.out.println();
        }
    }
}
