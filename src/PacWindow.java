import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.Map;
import java.util.Scanner;

public class PacWindow extends JFrame {

    public PacWindow() {
        setTitle("AKP Pacman v1.0");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.black);

        setSize(794, 884);
        setLocationRelativeTo(null);

        JLabel scoreboard = new JLabel("    Score : 0");
        scoreboard.setForeground(new Color(255, 243, 36));

        MapData map1 = getMapFromResource("resources/maps/map2_c.txt");
        adjustMap(map1);

        /*map1.getTeleports().add(new TeleportTunnel(-1,14,27,14,moveType.LEFT));
        map1.getTeleports().add(new TeleportTunnel(27,14,-1,14,moveType.RIGHT));
        map1.getGhostsData().add(new GhostData(12,17,ghostType.RED));
        map1.getGhostsData().add(new GhostData(17,14,ghostType.RED));
        map1.getGhostsData().add(new GhostData(17,10,ghostType.PINK));
        map1.getGhostsData().add(new GhostData(5,27,ghostType.CYAN));
        map1.getGhostsData().add(new GhostData(3,5,ghostType.PINK));
        map1.getGhostsData().add(new GhostData(20,5,ghostType.CYAN));
        map1.getPufoodPositions().add(new PowerUpFood(12,14,0));
        map1.getPufoodPositions().add(new PowerUpFood(25,27,3));
        map1.getPufoodPositions().add(new PowerUpFood(24,27,2));
        map1.getPufoodPositions().add(new PowerUpFood(23,27,1));
        map1.getPufoodPositions().add(new PowerUpFood(22,27,4));
        map1.getPufoodPositions().add(new PowerUpFood(21,27,0));
        map1.setGhostBasePosition(new Point(12,14));*/


        PacBoard pb = new PacBoard(scoreboard, map1, this);

        pb.setBorder(new CompoundBorder(new EmptyBorder(10, 10, 10, 10), new LineBorder(Color.BLUE)));
        addKeyListener(pb.pacman);

        this.getContentPane().add(scoreboard, BorderLayout.SOUTH);
        this.getContentPane().add(pb);
        setVisible(true);
    }

    public PacWindow(MapData md) {
        setTitle("AKP Pacman v1.0");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.black);

        setSize(794, 884);
        setLocationRelativeTo(null);

        JLabel scoreboard = new JLabel("    Score : 0");
        scoreboard.setForeground(new Color(255, 243, 36));

        //int[][] mapLoaded = loadMap(27,29,"/maps/map1.txt");
        adjustMap(md);
        PacBoard pb = new PacBoard(scoreboard, md, this);
        pb.setBorder(new CompoundBorder(new EmptyBorder(10, 10, 10, 10), new LineBorder(Color.BLUE)));
        addKeyListener(pb.pacman);

        this.getContentPane().add(scoreboard, BorderLayout.SOUTH);
        this.getContentPane().add(pb);
        setVisible(true);
    }


    public int[][] loadMap(int mx, int my, String relPath) {
        try {
            Scanner scn = new Scanner(this.getClass().getResourceAsStream(relPath));
            int[][] map;
            map = new int[mx][my];
            for (int y = 0; y < my; y++) {
                for (int x = 0; x < mx; x++) {
                    map[x][y] = scn.nextInt();
                }
            }
            return map;
        } catch (Exception e) {
            System.err.println("Error Reading Map File !");
        }
        return null;
    }

    public MapData getMapFromResource(String relPath) {
        String mapStr = "";
        try {
            Scanner scn = new Scanner(this.getClass().getResourceAsStream(relPath));
            StringBuilder sb = new StringBuilder();
            String line;
            while (scn.hasNextLine()) {
                line = scn.nextLine();
                sb.append(line).append('\n');
            }
            mapStr = sb.toString();
        } catch (Exception e) {
            System.err.println("Error Reading Map File !");
        }
        if ("".equals(mapStr)) {
            System.err.println("Map is Empty !");
        }
        return MapEditor.compileMap(mapStr);
    }

    //Dynamically Generate Map Segments
    public void adjustMap(MapData mapd) {
        int[][] map = mapd.getMap();
        int mx = mapd.getX();
        int my = mapd.getY();
        for (int y = 0; y < my; y++) {
            for (int x = 0; x < mx; x++) {
                boolean l = false;
                boolean r = false;
                boolean t = false;
                boolean b = false;
                boolean tl = false;
                boolean tr = false;
                boolean bl = false;
                boolean br = false;


                if (map[x][y] > 0 && map[x][y] < 30) {
                    int mustSet = 0;

                    l = (x > 0 && map[x - 1][y] > 0 && map[x - 1][y] < 30);
                    r = (x < mx - 1 && map[x + 1][y] > 0 && map[x + 1][y] < 30);
                    t = (y > 0 && map[x][y - 1] > 0 && map[x][y - 1] < 30);
                    b = (y < my - 1 && map[x][y + 1] > 0 && map[x][y + 1] < 30);
                    tl = (x > 0 && y > 0 && map[x - 1][y - 1] > 0 && map[x - 1][y - 1] < 30);
                    tr = (x < mx - 1 && y > 0 && map[x + 1][y - 1] > 0 && map[x + 1][y - 1] < 30);
                    bl = (x > 0 && y < my - 1 && map[x - 1][y + 1] > 0 && map[x - 1][y + 1] < 30);
                    br = (x < mx - 1 && y < my - 1 && map[x + 1][y + 1] > 0 && map[x + 1][y + 1] < 30);


                    //Decide Image to View
                    if (!r && !l && !t && !b) {
                        mustSet = 23;
                    }
                    if (r && !l && !t && !b) {
                        mustSet = 22;
                    }
                    if (!r && l && !t && !b) {
                        mustSet = 25;
                    }
                    if (!r && !l && t && !b) {
                        mustSet = 21;
                    }
                    if (!r && !l && !t && b) {
                        mustSet = 19;
                    }
                    if (r && l && !t && !b) {
                        mustSet = 24;
                    }
                    if (!r && !l && t && b) {
                        mustSet = 20;
                    }
                    if (r && !l && t && !b && !tr) {
                        mustSet = 11;
                    }
                    if (r && !l && t && !b && tr) {
                        mustSet = 2;
                    }
                    if (!r && l && t && !b && !tl) {
                        mustSet = 12;
                    }
                    if (!r && l && t && !b && tl) {
                        mustSet = 3;
                    }
                    if (r && !l && !t && b && br) {
                        mustSet = 1;
                    }
                    if (r && !l && !t && b && !br) {
                        mustSet = 10;
                    }
                    if (!r && l && !t && b && bl) {
                        mustSet = 4;
                    }
                    if (r && !l && t && b && !tr) {
                        mustSet = 15;
                    }
                    if (r && !l && t && b && tr) {
                        mustSet = 6;
                    }
                    if (!r && l && t && b && !tl) {
                        mustSet = 17;
                    }
                    if (!r && l && t && b && tl) {
                        mustSet = 8;
                    }
                    if (r && l && !t && b && !br) {
                        mustSet = 14;
                    }
                    if (r && l && !t && b && br) {
                        mustSet = 5;
                    }
                    if (r && l && t && !b && !tr) {
                        mustSet = 16;
                    }
                    if (r && l && t && !b && tr) {
                        mustSet = 7;
                    }
                    if (!r && l && !t && b && !bl) {
                        mustSet = 13;
                    }
                    if (r && l && t && b && br && tl) {
                        mustSet = 9;
                    }
                    if (r && l && t && b && !br && !tl) {
                        mustSet = 18;
                    }
                    if (r && l && t && b && !bl && tl && tr && br) { mustSet = 26; }
                    if (r && l && t && b && bl && !tl && tr && br) { mustSet = 27; }
                    if (r && l && t && b && bl && tl && !tr && br) { mustSet = 28; }
                    if (r && l && t && b && bl && tl && tr && !br) { mustSet = 29; }

                    //System.out.println("MAP SEGMENT : " + mustSet);
                    map[x][y] = mustSet;
                }
                mapd.setMap(map);
            }
        }
        System.out.println("Map Adjust OK !");
    }


}