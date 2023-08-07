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
        //
        if(code == KeyEvent.VK_1){
            myPanel.setCostOnNodes();
        }
        // A*
        if(code == KeyEvent.VK_2){
            myPanel.mAStarSearch();
        }
        if(code == KeyEvent.VK_3){
            myPanel.autoAStarSearch();
        }

        // BSF
        if (code == KeyEvent.VK_4){
            myPanel.mBFS();
        }
        if (code == KeyEvent.VK_5){
            myPanel.autoBFS();
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

}
