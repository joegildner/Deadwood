package view;

import controller.DeadwoodController;
import model.Player;
import model.Role.Lead;
import model.Role.Role;
import model.Room.Room;

import javax.swing.*;

/**
 * Created by gildnej on 3/11/19.
 */
public class PlayerView extends JLayeredPane implements DeadwoodController.Listener{

    private Player p;
    private JLabel pView;
    private int eminence;

    public PlayerView(Player p, int n, MainView boardView){
        this.p = p;
        pView = new JLabel();
        boardView.add(pView,new Integer(n));
        eminence = n;
        changed(p);
    }

    public void changed(Player p){
        if(this.p.getName().equals(p.getName())) {
            if (p.getCurRole() != null) {
                paintRole();
            } else {
                paintLocation();
            }
        }
    }

    public void paintRole(){
        Role curRole = p.getCurRole();
        pView.setIcon(new ImageIcon(p.getImgFile()));

        int[] roleArea = curRole.getArea();
        if(curRole instanceof Lead){
            int[] roomArea = p.getCurRoom().getArea();
            pView.setBounds(roleArea[0]+roomArea[0], roleArea[1]+roomArea[1], roleArea[2], roleArea[3]);
        }else
            pView.setBounds(roleArea[0],roleArea[1],roleArea[2],roleArea[3]);

        pView.setVisible(true);
    }

    public void paintLocation(){
        int area[] = p.getCurRoom().getPlayerPos();
        pView.setIcon(new ImageIcon(p.getImgFile()));

        area[0] += eminence*10;
        pView.setBounds(area[0],area[1],46,46);

        pView.setVisible(true);

    }
}
