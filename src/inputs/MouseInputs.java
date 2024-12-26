package inputs;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import cardTreatment.Baraja;
import gameGraphics.GamePanel;

public class MouseInputs implements MouseListener, MouseMotionListener {
	
	private GamePanel gamePanel;
	
	public MouseInputs(GamePanel gp) {
		gamePanel = gp;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		gamePanel.setRectPos(e.getX() - 39, e.getY() - 60);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
//		System.out.println(e.getX()+ " | " + e.getY());
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
