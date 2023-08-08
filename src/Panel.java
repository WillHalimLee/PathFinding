import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

public class Panel extends JPanel {


    private int maxRow = 15;
    private int maxCol = 30;
    private int nodeSize = 60;
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
       setTest();
    }

    public void reset (){
        this.removeAll();
        openList.clear();
        checkedList.clear();
        step = 0;
        goalReached = false;

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
        setTest();

    }

    public void setCost() {
        int row = 0;
        int col = 0;

        while (row < maxRow && col < maxCol) {
            getCost(node[row][col]);
            col++;
            if(col == maxCol) {
                col = 0;
                row++;
            }
        }
    }
    /** This is what is used to set up the map*/
    public void setTest(){
        setStartNode(11,3);
        setGoalNode(5,27);
        setGraphTestLine();

    }
    public void setGraphTestLine() {
        for (int i = 3; i < 12; i++){
            setSolidNode(i, 15);
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
    private void openNode(Node node ) {
        if (!node.open && !node.checked && !node.solid){
            node.setAsOpen();
            node.parent = currentNode;
            openList.add(node);
        }
    }

    private void trackThePath(){
        Node current = goalNode;
        int pathL = 0;
        while(current != startNode){
            current = current.parent;
            if(current != startNode) {
                current.setAsPath();
                pathL++;
            }
        }
        System.out.println("Path Length: " + pathL);
    }

    private void getCost(Node node){
        if (node.parent != null) {
            int deltaX = Math.abs(node.col - node.parent.col);
            int deltaY = Math.abs(node.row - node.parent.row);

            if (deltaX == 1 && deltaY == 1) {
                // Diagonal movement
                node.gCost = node.parent.gCost + 14;
            } else {
                // Horizontal or vertical movement
                node.gCost = node.parent.gCost + 10;
            }

        }
        int xDistance = node.col - goalNode.col;
        int yDistance = node.row - goalNode.row;
        node.hCost = (int) (Math.sqrt(xDistance*xDistance + yDistance*yDistance) * 10);

        node.fCost = node.gCost + node.hCost;

//        if(node != startNode && node != goalNode){
//            node.setText("<html>F:" + node.fCost + "<br>G:" + node.gCost + "<br>H: " + node.hCost + "</html>");
//        }
    }

    public void checkAround(int row, int col) {
        //Top mid
        if(row - 1 > 0){
            openNode(node[row-1][col]);
        }
        // Right
        if(col + 1 < maxCol){
            openNode(node[row][col+1]);
        }
        // Bottom mid
        if(row + 1 < maxRow){
            openNode(node[row+1][col]);
        }
        // Left
        if(col - 1 > 0){
            openNode(node[row][col-1]);
        }
        // Top left
        if(row - 1 > 0 && col - 1 > 0){
            openNode(node[row-1][col-1]);
        }
        // Top right
        if(row - 1 > 0 && col + 1 < maxCol){
            openNode(node[row-1][col+1]);
        }
        // bottom right
        if(row + 1 < maxRow && col + 1 < maxCol){
            openNode(node[row+1][col+1]);
        }
        // Bottom left
        if(row + 1 < maxRow && col - 1 > 0){
            openNode(node[row+1][col-1]);
        }

    }

    public void mAStar(){
        if (!goalReached){
            int col = currentNode.col;
            int row = currentNode.row;

            currentNode.setAsChecked();
            checkedList.add(currentNode);
            openList.remove(currentNode);
            if (currentNode != startNode){
                currentNode.setAsCurrent();
            }

           checkAround(row, col);
            int bestNodeIndex = 0;
            int bestNodeFCost = Integer.MAX_VALUE;

            for (int i = 0; i < openList.size(); i++) {
                // Find the cost of traveling to this node.
                getCost(openList.get(i));
                openList.get(i).setAsChecked();
                // Check if this node's F cost is better.
                if (openList.get(i).fCost < bestNodeFCost){
                    bestNodeIndex = i;
                    bestNodeFCost = openList.get(i).fCost;
                } else if (openList.get(i).fCost == bestNodeFCost){
                    if (openList.get(i).hCost < openList.get(bestNodeIndex).hCost){
                        bestNodeIndex = i;
                        bestNodeFCost = openList.get(i).fCost;
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


    public void autoAStar(){
        long startTime = System.currentTimeMillis();
        while (!goalReached){
            int col = currentNode.col;
            int row = currentNode.row;


            checkedList.add(currentNode);
            openList.remove(currentNode);

            checkAround(row, col);

            int bestNodeIndex = 0;
            int bestNodeFCost = Integer.MAX_VALUE;

            for (int i = 0; i < openList.size(); i++) {
                // Find the cost of traveling to this node.
                getCost(openList.get(i));
                if(!openList.get(i).checked){
                    step++;

                openList.get(i).setAsChecked();
                // Check if this node's F cost is better.
                if (openList.get(i).fCost < bestNodeFCost){
                    bestNodeIndex = i;
                    bestNodeFCost = openList.get(i).fCost;
                } else if (openList.get(i).fCost == bestNodeFCost){
                    if (openList.get(i).hCost < openList.get(bestNodeIndex).hCost){
                        bestNodeIndex = i;
                        bestNodeFCost = openList.get(i).fCost;
                    }
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
        System.out.println("Nodes Checked : " + step);
    }

    public void mBFS() {
        if (!goalReached){
            step++;
            int col = currentNode.col;
            int row = currentNode.row;

            checkedList.add(currentNode);
            openList.remove(currentNode);
            if (currentNode != startNode){
                currentNode.setAsCurrent();
            }
            checkAround(row, col);
            int bestNodeIndex = 0;
            int bestNodeHCost = Integer.MAX_VALUE;


            for (int i = 0; i < openList.size(); i++) {
                // Find the cost of traveling to this node.
                getCost(openList.get(i));
                if(!openList.get(i).checked){
                    step++;
                }
                openList.get(i).setAsChecked();
                // Check if this node's F cost is better.
                if (openList.get(i).hCost < bestNodeHCost){
                    bestNodeIndex = i;
                    bestNodeHCost = openList.get(i).hCost;
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
    public void autoBFS(){
        long startTime = System.currentTimeMillis();
        while (!goalReached){
            int col = currentNode.col;
            int row = currentNode.row;

            checkedList.add(currentNode);
            openList.remove(currentNode);

            checkAround(row, col);
            int bestNodeIndex = 0;
            int bestNodeHCost = Integer.MAX_VALUE;


            for (int i = 0; i < openList.size(); i++) {
                getCost(openList.get(i));
                if(!openList.get(i).checked){
                    step++;
                }
                openList.get(i).setAsChecked();

                if (openList.get(i).hCost < bestNodeHCost){
                    bestNodeIndex = i;
                    bestNodeHCost = openList.get(i).hCost;
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
        System.out.println("Nodes Checked : " + step);
    }

    public void mDijkstra(){

        if (!goalReached){
            int col = currentNode.col;
            int row = currentNode.row;

            checkedList.add(currentNode);
            openList.remove(currentNode);

            if (currentNode != startNode){
                currentNode.setAsCurrent();
            }

            checkAround(row, col);

            int bestNodeIndex = 0;
            int bestNodeGCost = Integer.MAX_VALUE;

            for (int i = 0; i < openList.size(); i++) {
                // Find the cost of traveling to this node.
                getCost(openList.get(i));
                openList.get(i).setAsChecked();
                // Check if this node's G cost is better.
                if (openList.get(i).gCost < bestNodeGCost){
                    bestNodeIndex = i;
                    bestNodeGCost = openList.get(i).gCost;
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
    public void autoDijkstra(){
        long startTime = System.currentTimeMillis();

        while (!goalReached){
            int col = currentNode.col;
            int row = currentNode.row;

            checkedList.add(currentNode);
            openList.remove(currentNode);
            checkAround(row, col);

            int bestNodeIndex = 0;
            int bestNodeGCost = Integer.MAX_VALUE;
            for (int i = 0; i < openList.size(); i++) {
                // Find the cost of traveling to this node.
                getCost(openList.get(i));
                if(!openList.get(i).checked){
                    step++;
                }
                openList.get(i).setAsChecked();
                // Check if this node's G cost is better.
                if (openList.get(i).gCost < bestNodeGCost){
                    bestNodeIndex = i;
                    bestNodeGCost = openList.get(i).gCost;

                }
            }
            // Get the next best node for out next step.
            currentNode = openList.get(bestNodeIndex);
            if (currentNode == goalNode) {
                goalReached = true;
                trackThePath();
                break;
            }
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Time: " + (endTime - startTime));
        System.out.println("Nodes Checked : " + step);
    }
}
