package controller;

import model.Role.Extra;
import model.Role.Lead;
import model.Role.Role;
import model.Room.Stage;
import view.MainView;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by gildnej on 3/7/19.
 */
public class StageController implements DeadwoodController.DayListener{

    private String stageName;
    private JButton moveHere;
    private ArrayList<JButton> extras;
    private ArrayList<JButton> stars;
    private DeadwoodController deadwood;

    private Stage stage;
    private MainView boardView;

    public StageController(Stage stage, MainView boardView, DeadwoodController deadwood){
        extras = new ArrayList<JButton>();
        stars = new ArrayList<>();
        stageName = stage.getName();
        this.deadwood = deadwood;
        this.stage = stage;
        this.boardView = boardView;

        for(Role extraRole : stage.getRoles()){
            if(extraRole instanceof Extra)
                extras.add(createButton(extraRole, extraRole.getArea(), boardView));
        }

        moveHere = createButton(stage, boardView);

        updateStars();
    }

    public JButton createButton(Role thisRole, int[] area, MainView boardView){
        JButton thisButton = new JButton();

        thisButton.setBounds(area[0],area[1],area[2],area[3]);
        thisButton.setOpaque(false);
        thisButton.setContentAreaFilled(false);
        thisButton.setBorderPainted(false);
        thisButton.setVisible(true);

        thisButton.addActionListener(deadwood);
        thisButton.setActionCommand("work "+thisRole.getName());

        boardView.add(thisButton, new Integer(2));

        return thisButton;
    }

    public JButton createButton(Stage thisStage, MainView boardView){
        JButton thisButton = new JButton();
        int[] area = thisStage.getArea();

        thisButton.setBounds(area[0]+area[3]/4,area[1] +115,area[3]/2,40);
        thisButton.setOpaque(false);
        thisButton.setContentAreaFilled(false);
        thisButton.setBorderPainted(false);
        thisButton.setVisible(true);

        thisButton.addActionListener(deadwood);
        thisButton.setActionCommand("move "+thisStage.getName());

        boardView.add(thisButton, new Integer(2));

        return thisButton;
    }

    public void updateStars(){
        for(JButton starButton : stars){
            boardView.remove(starButton);
        }

        stars = new ArrayList<>();

        int[] sceneArea = stage.getArea();

        for(Role starRole : stage.getRoles()){
            int[] actualArea = Arrays.copyOf(starRole.getArea(), starRole.getArea().length);

            actualArea[0] += sceneArea[0];
            actualArea[1] += sceneArea[1];

            if(starRole instanceof Lead)
                stars.add(createButton(starRole, actualArea, boardView));
        }

    }

    public void changed(){
        updateStars();
    }
}
