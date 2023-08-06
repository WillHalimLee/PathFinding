import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Node extends JButton implements ActionListener {

    Node parent;
    int col;
    int row;

    // gCost: Distance between current node and the start node
    // hCost: Distance between current node and goal node
    // fCost: The total cost of (G + H).

    int gCost, hCost, fCost;
    boolean start, goal, solid, open, checked, clicked;

    public Node(int theRow, int theCol) {
        col = theCol;
        row = theRow;
        setBackground(Color.white);
        setForeground(Color.BLACK);
        addActionListener(this);
        clicked = false;
    }

    public void setAsStart() {
        setBackground(Color.blue);
        setForeground(Color.white);
        setText("Start");
        start = true;
    }
    public void setAsGoal() {
        setBackground(Color.YELLOW);
        setForeground(Color.black);
        setText("Goal");
        goal = true;
    }
    public void setAsSolid(){
        setBackground(Color.black);
        setForeground(Color.black);
        solid = true;
    }
    public void setAsOpen() {
        open =true;
    }
    public void setAsChecked(){
        if(!start && !goal){
            setBackground(Color.orange);
            setForeground(Color.black);
        }
        checked = true;
    }
    public void setAsPath(){
        setBackground(Color.green);
        setForeground(Color.black);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(clicked){
            setBackground(Color.white);
            clicked = false;
        }else {
            setBackground(Color.ORANGE);
            clicked = true;
        }

    }

}
