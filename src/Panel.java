import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Panel extends JPanel {

    private int maxCol =150;
    private int maxRow =75;
    private int nodeSize = 10;
    final int screenWidth = nodeSize * maxCol;
    final int screenHeight = nodeSize * maxRow;
    int step = 0;
    Node[][] node = new Node[maxRow][maxCol];
    Node startNode, goalNode, currentNode;
    ArrayList<Node> openList = new ArrayList<>();
    ArrayList<Node> checkedList = new ArrayList<>();
    boolean goalReached = false;

    Panel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setLayout(new GridLayout(maxRow,maxCol));
        this.addKeyListener(new KeyHandler(this));
        this.setFocusable(true);


        int row = 0;
        int col = 0;

        while (row < maxRow && col < maxCol) {
            node[row][col] = new Node(row,col, this);
            add(node[row][col]);
            col++;
            if(col == maxCol) {
                col = 0;
                row++;
            }
        }
        setStartNode(45,20);
        setGoalNode(3,145);
        setGraphTestC();

    }


    public void setGraphTestLine() {
        for (int i = 3; i < 72; i++){
            setSolidNode(i, 75);
        }
    }

    public void setGraphTestC(){

        for (int i = 0; i < 10 ; i++) {
            setSolidNode(45-i, 30);
            setSolidNode(45+i, 30);
        }
        for (int i = 0; i < 20 ; i++) {
            setSolidNode(35, 30-i);
            setSolidNode(55, 30-i);
        }
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

    public void setCostOnNodes() {
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
        node.gCost = (int) (Math.sqrt(xDistance + yDistance) * 10);

        xDistance = node.col - goalNode.col;
        yDistance = node.row - goalNode.row;
        node.hCost = (int) (Math.sqrt(xDistance*xDistance + yDistance*yDistance) * 10);

        node.fCost = node.gCost + node.hCost;

//        if(node != startNode && node != goalNode){
//            node.setText("<html>F:" + node.fCost + "<br>G:" + node.gCost + "<br>H: " + node.hCost + "</html>");
//        }
    }

    // The first implementation. This calculation searches too many nodes.
    // might be Dijkstra
    private void getCostSlow(Node node){
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

    public void autoAStarSearch(){
        long startTime = System.currentTimeMillis();
        while (!goalReached){
            int col = currentNode.col;
            int row = currentNode.row;
            step++;
            currentNode.setAsChecked();
            checkedList.add(currentNode);
            openList.remove(currentNode);

            // Top left
            if(row - 1 >= 0 && col - 1 > 0){
                openNode(node[row-1][col-1]);
            }
            //Top mid
            if(row - 1 >= 0){
                openNode(node[row-1][col]);
            }
            // Top right
            if(row - 1 >= 0 && col + 1 < maxCol){
                openNode(node[row-1][col+1]);
            }
            // left
            if(col - 1 >= 0){
                openNode(node[row][col-1]);
            }
            // right
            if(col + 1 <= maxCol){
                openNode(node[row][col+1]);
            }
            // Bottom left
            if(row + 1 <= maxRow && col - 1 > 0){
                openNode(node[row+1][col-1]);
            }
            // Bottom mid
            if(row + 1 <= maxRow){
                openNode(node[row+1][col]);
            }
            // bottom right
            if(row + 1 <= maxRow && col + 1 < maxCol){
                openNode(node[row+1][col+1]);
            }
            int bestNodeIndex = 0;
            int bestNodeFCost = Integer.MAX_VALUE;


            for (int i = 0; i < openList.size(); i++) {
                // Check if this node's F cost is better.
                if (openList.get(i).fCost < bestNodeFCost){
                    bestNodeIndex = i;
                    bestNodeFCost = openList.get(i).fCost;
                }
                // Check if this node's G cost is better.
                else if (openList.get(i).fCost == bestNodeFCost){
                   if (openList.get(i).gCost < openList.get(bestNodeIndex).gCost){
                       bestNodeIndex = i;
                   }
                }
            }
            // Get the next best node for out next step.
            currentNode = openList.get(bestNodeIndex);
            if (currentNode == goalNode) {
                goalReached = true;
                trackThePath();
            }
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Time: " + (endTime - startTime));
        System.out.println("Nodes visited : " + step);
    }

    private void openNode(Node node) {
        if (!node.open && !node.checked && !node.solid){
            node.setAsOpen();
            node.parent = currentNode;
            openList.add(node);
        }
    }

    private void trackThePath(){
        Node current = goalNode;
        while(current != startNode){
            current = current.parent;
            if(current != startNode) {
                current.setAsPath();
            }
        }
    }

    public void mAStarSearch(){
        if (!goalReached){
            step++;
            int col = currentNode.col;
            int row = currentNode.row;

            currentNode.setAsChecked();
            checkedList.add(currentNode);
            openList.remove(currentNode);

            // Top left
            if(row - 1 >= 0 && col - 1 > 0){
                openNode(node[row-1][col-1]);
            }
            //Top mid
            if(row - 1 >= 0){
                openNode(node[row-1][col]);
            }
            // Top right
            if(row - 1 >= 0 && col + 1 < maxCol){
                openNode(node[row-1][col+1]);
            }
            // left
            if(col - 1 >= 0){
                openNode(node[row][col-1]);
            }
            // right
            if(col + 1 <= maxCol){
                openNode(node[row][col+1]);
            }
            // Bottom left
            if(row + 1 <= maxRow && col - 1 > 0){
                openNode(node[row+1][col-1]);
            }
            // Bottom mid
            if(row + 1 <= maxRow){
                openNode(node[row+1][col]);
            }
            // bottom right
            if(row + 1 <= maxRow && col + 1 < maxCol){
                openNode(node[row+1][col+1]);
            }
            int bestNodeIndex = 0;
            int bestNodeFCost = Integer.MAX_VALUE;


            for (int i = 0; i < openList.size(); i++) {
                // Check if this node's F cost is better.
                if (openList.get(i).fCost < bestNodeFCost){
                    bestNodeIndex = i;
                    bestNodeFCost = openList.get(i).fCost;
                }
                // Check if this node's G cost is better.
                else if (openList.get(i).fCost == bestNodeFCost){
                    if (openList.get(i).gCost < openList.get(bestNodeIndex).gCost){
                        bestNodeIndex = i;
                    }
                }
            }
            // Get the next best node for out next step.
            currentNode = openList.get(bestNodeIndex);
            if (currentNode == goalNode) {
                goalReached = true;
                trackThePath();
            }
        }

    }
}
