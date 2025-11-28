import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class MainApp {
    public static Controller controller = Controller.getInstance();
    public static ConnectDB konektor = ConnectDB.getInstance();
    public static boolean logOut = false;
    
    public static void contentCreatorChannelPage(Scanner sc) throws SQLException {
        System.out.println();
        while (true) {
            System.out.println("=== BRAND CHANNEL PAGE ===");
            printCommand(new String[] {"Upload Video", "See Uploaded Videos", "Remove Video", "Edit Video", "Channel Detail", 
                            "View Report", "Edit Member","Edit Channel", "Back"});
            System.out.print("Pick your action: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Video Title: ");
                    String title = sc.nextLine();

                    System.out.print("Video Description: ");
                    String desc = sc.nextLine();

                    System.out.print("Video Duration (in seconds): ");
                    int duration = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Video File URL (https://www.youtube.com/watch?v=Bwgnw0muVlY): ");
                    String videoPath = sc.nextLine();

                    System.out.print("Subtitle Text File Path (C:\\Users\\vandy\\Downloads\\subtitle.txt): ");
                    String subPath = sc.nextLine();
                    Video newVid = new Video(title, duration,  desc, subPath, videoPath, controller.getUser().getIdPengguna(),controller.getBrandChannel().getIdKanal());
                    controller.uploadVideoBrand(newVid);
                    break;

                case 2:
                    controller.printAllVideosBrand();
                    break;

                case 3:
                    controller.printAllVideosBrand();
                    System.out.print("Enter video index to remove: ");
                    int removeIdx = sc.nextInt() - 1;
                    sc.nextLine();
                    controller.removeVideoBrand(removeIdx);
                    break;

                case 4:
                    controller.printAllVideosBrand();
                    System.out.print("Enter video index to edit: ");
                    int editIdx = sc.nextInt() - 1;
                    sc.nextLine();
                    editVideoMenu(sc, editIdx, controller.getBrandChannel());
                    break;

                case 5:
                    controller.getBrandChannelDetail(sc);
                    break;

                case 6:
                    reportPage(sc, controller.getBrandChannel());
                    break;

                case 7:
                    editMemberMenu(sc, (ChannelGrup) controller.getBrandChannel());
                    break;
                case 8:
                    EditChannelPage(sc, controller.getBrandChannel());
                    break;
                case 9:
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    /*
     * Page untuk melihat detail channel grup
     * Pengguna dapat mengupload, melihat upload, menghapus video, edit video, melihat 
     * detail channel, melihat laporan, dan mengatur member yang masuk ke dalam grup
     */
    public static void myChannelGroupPage(Scanner sc) throws SQLException {
        System.out.println();
        while (true) {
            System.out.println("=== MY CHANNEL PAGE ===");
            printCommand(new String[] {"Upload Video", "See Uploaded Videos", "Remove Video", "Edit Video", "Channel Detail", 
                            "View Report", "Edit Member","Edit Channel", "Back"});
            System.out.print("Pick your action: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Video Title: ");
                    String title = sc.nextLine();

                    System.out.print("Video Description: ");
                    String desc = sc.nextLine();

                    System.out.print("Video Duration (in seconds): ");
                    int duration = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Video File URL (https://www.youtube.com/watch?v=Bwgnw0muVlY): ");
                    String videoPath = sc.nextLine();

                    System.out.print("Subtitle Text File Path (C:\\Users\\vandy\\Downloads\\subtitle.txt): ");
                    String subPath = sc.nextLine();
                    Video newVid = new Video(title, duration,  desc, subPath, videoPath, controller.getUser().getIdPengguna(),controller.getChannel().getIdKanal());
                    controller.uploadVideo(newVid);
                    break;

                case 2:
                    controller.printAllVideos();
                    break;

                case 3:
                    controller.printAllVideos();
                    System.out.print("Enter video index to remove: ");
                    int removeIdx = sc.nextInt() - 1;
                    sc.nextLine();
                    controller.removeVideo(removeIdx);
                    break;

                case 4:
                    controller.printAllVideos();
                    System.out.print("Enter video index to edit: ");
                    int editIdx = sc.nextInt() - 1;
                    sc.nextLine();
                    editVideoMenu(sc, editIdx, controller.getChannel());
                    break;

                case 5:
                    controller.getChannelDetail(sc);
                    break;

                case 6:
                    reportPage(sc, controller.getChannel());
                    break;

                case 7:
                    editMemberMenu(sc, (ChannelGrup) controller.getChannel());
                    break;
                case 8:
                    EditChannelPage(sc, controller.getChannel());
                    break;
                case 9:
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    /*
     * Page bagi pengguna yang ingin membuat channel
     * Pengguna perlu memasukkan nama, deskripsi, dan tipe channel yang akan dibuat
     */
    public static boolean makeChannelPage(Scanner sc) throws SQLException {
        System.out.println();
        String nama, deskripsi;
        int tipeChannel;

        System.out.print("Channel Name : ");
        sc.nextLine(); // tambahan biar kebaca
        nama = sc.nextLine();

        System.out.print("Channel Description : ");
        deskripsi = sc.nextLine();

        System.out.println("==== CHANNEL TYPE (Individual = 1 | Group = 2) ====");
        System.out.println("1. Individual");
        System.out.println("2. Group");
        System.out.print("Channel type (1 / 2): ");
        tipeChannel = sc.nextInt();

        return controller.makeChannel(nama, deskripsi, tipeChannel);
    }

    /*
     * Page untuk melihat detail channel individu
     * Pengguna dapat mengupload, melihat upload, menghapus video, edit video, melihat 
     * detail channel, dan melihat laporan
     */
    public static void myChannelPage(Scanner sc) throws SQLException {
        User user = controller.getUser();
        Channel myChannel = controller.getChannel();
        while (true) {
            System.out.println("==== MY CHANNEL PAGE ====");
            printCommand(new String[] {"Upload Video", "See Uploaded Videos", "Remove Video", "Edit Video", "Channel Detail", 
                            "View Report","Edit Channel", "Back"});
            System.out.print("Pick your action: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Video Title: ");
                    String title = sc.nextLine();

                    System.out.print("Video Description: ");
                    String desc = sc.nextLine();

                    System.out.print("Video Duration (in seconds): ");
                    int duration = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Video File URL (https://www.youtube.com/watch?v=Bwgnw0muVlY): ");
                    String videoPath = sc.nextLine();

                    System.out.print("Subtitle Text File Path (C:\\Users\\vandy\\Downloads\\subtitle.txt): ");
                    String subPath = sc.nextLine();

                    Video newVid = new Video(title, duration, desc, subPath, videoPath, user.getIdPengguna(),myChannel.getIdKanal());
                    controller.uploadVideo(newVid);
                    break;

                case 2:
                    controller.printAllVideos();
                    break;

                case 3:
                    controller.printAllVideos();
                    System.out.print("Enter video index to remove: ");
                    int removeIdx = sc.nextInt() - 1;
                    sc.nextLine();
                    controller.removeVideo(removeIdx);
                    break;

                case 4:
                    controller.printAllVideos();
                    System.out.print("Enter video index to edit: ");
                    int editIdx = sc.nextInt() - 1;
                    sc.nextLine();
                    editVideoMenu(sc, editIdx, controller.getChannel());
                    break;

                case 5:
                    controller.getChannelDetail(sc);
                    break;

                case 6:
                    reportPage(sc, controller.getChannel());
                    break;
                case 7:
                    EditChannelPage(sc, controller.getChannel());
                    break;
                case 8:
                    return; // Back to HomePage
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    /*
     * Page untuk mengedit video
     * Pengguna dapat melihat detail video
     */
    public static void editVideoMenu(Scanner sc, int videoIndex, Channel myChannel) throws SQLException {
        System.out.println();
        if (videoIndex < 0 || videoIndex >= myChannel.uploaded.size()) {
            System.out.println("Video not found.\n");
            return;
        }
        Video video = myChannel.uploaded.getVideo(videoIndex);
        User user = controller.getUser();
        while (true) {
            System.out.println("=== EDIT VIDEO: " + video.getVideoNama() + " ===");
            printCommand(new String[] {"See Video Detail", "Edit title", "Edit Description", "Edit Subtitle", 
            "Play Video", "Back"}); 
            System.out.print("Pick your action: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    video.showDetails(sc);
                    break;
                case 2:
                    // System.out.print("New Title (blank = no change): ");
                    // String newTitle = sc.nextLine();
                    // if (!newTitle.isEmpty())
                    //     video.setVideoNama(newTitle);
                    // System.out.println("Title updated.");
                    if (myChannel instanceof ChannelIndividu) {
                        myChannel.changeVideoName(sc,video,user);
                    }else{
                        ChannelGrup temp = (ChannelGrup) myChannel;
                        temp.changeVideoName(sc,video,user);
                    }
                    break;
                case 3:
                    // System.out.print("New Description (blank = no change): ");
                    // String newDesc = sc.nextLine();
                    // if (!newDesc.isEmpty())
                    //     video.setVideoDeskripsi(newDesc);
                    // System.out.println("Description updated.");

                    if (myChannel instanceof ChannelIndividu) {
                        myChannel.changeVideoDescription(sc, video, user);
                    }else{
                        ChannelGrup temp = (ChannelGrup) myChannel;
                        temp.changeVideoDescription(sc, video, user);
                    }

                    break;
                case 4:
                    // System.out.print("Choose new subtitle file ((C:\\Users\\vandy\\Downloads\\subtitle.txt)): ");
                    // String subtitle = sc.next();
                    // video.setvideoSubtitle(subtitle);
                    // System.out.println("Subtitle added.");
                    if (myChannel instanceof ChannelIndividu) {
                        myChannel.changeVideoSubtitle(sc, video, user);
                    }else{
                        ChannelGrup temp = (ChannelGrup) myChannel;
                        temp.changeVideoSubtitle(sc, video, user);
                    }
                    break;
                case 5:
                    controller.play(sc, video);
                    break; 
                case 6:
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    public static void editMemberMenu(Scanner sc, ChannelGrup tempGrup) throws SQLException {
        System.out.println();
        String roles[] = {"Manager", "Editor", "Editor Limited", "Subtitle Editor", "Viewer" };

        while (true) {
            tempGrup.printMember();
            System.out.println("\n==== EDIT MEMBER: " + tempGrup.namaKanal + " ====");
            System.out.println("1. Edit Member Role");
            System.out.println("2. Add Member");
            System.out.println("3. Remove Member");
            System.out.println("4. Back");

            System.out.print("Choose an option: ");
            int choice = sc.nextInt();
            System.out.println();
            sc.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter member email to change role: ");
                    String changeEmail = sc.nextLine();
                    for (int i = 0; i < roles.length; i++) {
                        System.out.println((i+1) + ". " + roles[i]);
                    }
                    System.out.print("Enter role number: ");
                    int newRole = sc.nextInt();
                    sc.nextLine();

                    if (newRole < 1 || newRole > 5) {
                        System.out.println("Invalid role index.");
                        break;
                    }
                    
                    int check = tempGrup.changeRole(controller.getUser(), changeEmail, newRole+1);
                    if (check == 0) {
                        System.out.println("Role updated.");
                    } else if (check == 2) {
                        System.out.println("Member not found.");
                    }
                    break;

                case 2:
                    System.out.print("Add Member (email): ");
                    String email = sc.nextLine();
                    System.out.println("Choose role:");
                    for (int i = 0; i < roles.length; i++) {
                        System.out.println((i + 1) + ". " + roles[i]);
                    }
                    System.out.print("Enter role number: ");
                    int role = sc.nextInt();
                    sc.nextLine();

                    if (role < 1 || role > 5) {
                        System.out.println("Invalid role index.");
                        break;
                    }

                    int added = tempGrup.addMember(controller.getUser(), email, role + 1);
                    if (added == 0) {
                        System.out.println("Member added.");
                    } else if (added == 2) {
                        System.out.println("Member already exists.");
                    }
                    break;

                case 3:
                    System.out.print("Enter member email to remove: ");
                    String removeEmail = sc.nextLine();
                    int removed = tempGrup.removeMember(controller.getUser(), removeEmail);
                    if (removed == 0) {
                        System.out.println(removeEmail +" removed.");
                    } else if (removed == 2) {
                        System.out.println("Member not found.");
                    }
                    break;

                case 4:
                    return; // back to MyChannel

                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    public static void EditChannelPage(Scanner sc, Channel myChannel) throws SQLException{
        User user = controller.getUser();
        //bisa 1 edit namaKanal
        //bisa 2 edit deskripsi kanal Page
        while (true) {
            printCommand(new String[] { "Edit Channel Name", "Edit Channel Description", "Back" });
            System.out.print("Pick your Action : ");
            int action = sc.nextInt();
            System.out.println();
            sc.nextLine();
             switch (action) {
                case 1:
                    if (myChannel instanceof ChannelIndividu) {
                        myChannel.changeName(sc, user);
                    }else{
                        ChannelGrup temp = (ChannelGrup) myChannel;
                        temp.changeName(sc, user);
                    }
                    
                    break;
                case 2:
                    if(myChannel instanceof ChannelIndividu){
                       myChannel.changeDescription(sc, user);
                    }else{
                        ChannelGrup temp = (ChannelGrup) myChannel;
                        temp.changeDescription(sc, user);
                    }
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Command is not valid!\n");
                    break;
            }
            System.out.println();
            
        }
    }

    public static void reportPage(Scanner sc, Channel myChannel) throws SQLException{
        User user = controller.getUser();
        //bisa 1 edit namaKanal
        //bisa 2 edit deskripsi kanal Page
        while (true) {
            printCommand(new String[] { "Channel Report", "Video Edit Report", "Video Delete Report", "Video Upload Report", "Channel edit report", "Back" });
            System.out.print("Pick your Action : ");
            int action = sc.nextInt();
            sc.nextLine();
            System.out.println();
             switch (action) {
                case 1:
                    myChannel.viewChannelReports(sc);
                    break;
                case 2:
                    myChannel.viewEditReport();
                    break;
                case 3:
                    myChannel.viewHapusReport();
                    break;
                case 4:
                    myChannel.viewUnggahReport();
                    break;
                case 5:
                    myChannel.viewKelolaReport();
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Command is not valid!\n");
                    break;
            }
            System.out.println();
            
        }
        
    }
}
