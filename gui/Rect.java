package gui;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JComponent;

public class Rect extends JComponent{
  int x;
  int y;
  float ang;
  int t = 0;
  int height;
  int width;
  Color color;
  public Rect(int x, int y, int width, int height, Color color) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    this.color = color;
  }
 
  public void draw(Graphics g) {
    g.setColor(color);
    g.fillRect(x, y, width, height);
  }
 
}