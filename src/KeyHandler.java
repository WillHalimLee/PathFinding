import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    Panel myPanel;

    public KeyHandler(Panel thePanel){
        myPanel = thePanel;
    }
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if(code == KeyEvent.VK_1){

        }
        if(code == KeyEvent.VK_2){
            myPanel.setCostOnNodes();
            myPanel.autoAStarSearch();
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
