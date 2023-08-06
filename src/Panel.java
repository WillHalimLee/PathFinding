import javax.swing.*;
import java.awt.*;

public class Panel extends JPanel {

    final int maxCol =15;
    final int maxRow = 10;
    final int nodeSize = 70;
    final int screenWidth = nodeSize * maxCol;
    final int screenHeight = nodeSize * maxRow;

    Node[][] node = new Node[maxRow][maxCol];
    Node startNode, goalNode, currentNode;

    Panel(){
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setLayout(new GridLayout(maxRow,maxCol));

        int row = 0;
        int col = 0;

        while (row < maxRow && col < maxCol){
            node[row][col] = new Node(row,col);
            add(node[row][col]);
            col++;
            if(col == maxCol) {
                col = 0;
                row++;
            }
        }
        setStartNode(7,2);
        setGoalNode(2,12);

        setSolidNode(2,7);
        setSolidNode(2,8);
        setSolidNode(2,9);
        setSolidNode(2,10);
        setSolidNode(2,11);
        setSolidNode(3,11);
        setSolidNode(3,12);
        setSolidNode(3,13);

        setCostOnNodes();

    }

    private void setStartNode(final int row, final int col) {
        node[row][col].setAsStart();
        startNode = node[row][col];
        currentNode = startNode;
    }
    private void setGoalNode(final int row, final int col) {
        node[row][col].setAsGoal();
        goalNode = node[row][col];
    }
    private void setSolidNode(final int row, final int col) {
        node[row][col].setAsSolid();
    }
    private void setCostOnNodes() {
        int row = 0;
        int col = 0;

        while (row < maxRow && col < maxCol){
         getCost(node[row][col]);
            col++;
            if(col == maxCol) {
                col = 0;
                row++;
            }
        }
    }
    private void getCost(Node node){
        int xDistance = Math.abs(node.col - startNode.col);
        int yDistance = Math.abs(node.row - startNode.row);
        node.gCost = xDistance + yDistance;

        xDistance = Math.abs(node.col - goalNode.col);
        yDistance = Math.abs(node.row - goalNode.row);
        node.hCost = xDistance + yDistance;

        node.fCost = node.gCost + node.hCost;

        if(node != startNode && node != goalNode){
            node.setText("<html>F:" + node.fCost + "<br>G:" + node.gCost + "</html>");
        }
    }
}
