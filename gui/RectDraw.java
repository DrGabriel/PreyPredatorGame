package gui;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class RectDraw extends JComponent {
	  private static final int ANIMATION_DELAY = 10;
	  private List<Rect> rectList = new ArrayList<Rect>();
	  private int deltaY = 2;
	   
	  public RectDraw() {
	  }
	   
	  public void animate() {
	    new Timer(ANIMATION_DELAY, new ActionListener() {
	      public void actionPerformed(ActionEvent arg0) {
	        for (Rect rect : rectList) {
	        }
	      }
	    }).start();
	  }
	 
	  public void addRect(Rect rect) {
	    rectList.add(rect);
		repaint();
	  }
	  public void cleanRects(){
		  rectList.clear();
		  repaint();
	  }
	 
	  @Override
	  public void paintComponent(Graphics g) {
	    super.paintComponent(g);
	    for (Rect rect : rectList) {
	      rect.draw(g);
	    }
	  }
	}
