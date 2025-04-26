package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import mainGame.Game;
import utilz.LoadSave;

public class PointsOverlay {

	private int x, y, width, height;
	private ArrayList<Integer> pointList;
	private ArrayList<String> nameList;
	private BufferedImage img, winLoseBanner;
	
	
	public PointsOverlay() {
		loadImg();
		x = (Game.GAME_WIDTH - width) / 2;
		y = ((Game.GAME_HEIGHT - height) / 2) + 80;
		pointList = new ArrayList<>();
		nameList = new ArrayList<>();
	}

	private void loadImg() {
		img = LoadSave.GetSpriteAtlas(LoadSave.TABLEROPOINTS);
		winLoseBanner = LoadSave.GetSpriteAtlas(LoadSave.WIN_LOSE_BANNER);
		width = img.getWidth();
		height = img.getHeight();
	}
	
	public void draw (Graphics g, boolean winner) {
		g.setColor(Color.white);
		Font fuente;
		try {
			fuente = Font.createFont(Font.TRUETYPE_FONT, new File("res/fuentes/Minecraftia-Regular.ttf"));
			fuente = fuente.deriveFont(Font.PLAIN, 18);
		}
		catch (FontFormatException | IOException e) {}
		int xPos = x + 65, yPos = y + 97;
		g.drawImage(img, x, y, width, height, null);
		if (winner) g.drawImage(winLoseBanner.getSubimage(0, 0, 500, 152), x, y - 160, winLoseBanner.getWidth(), 152, null);
		else g.drawImage(winLoseBanner.getSubimage(0, 152, 500, 152), x, y - 160, winLoseBanner.getWidth(), 152, null);
		if (!nameList.isEmpty()) {
			for (int i = 0; i < nameList.size(); i++) {
				switch (i) {
				case 0:
					g.drawString(nameList.get(i), xPos, yPos + 2);
					break;
				case 1:
					g.drawString(nameList.get(i), xPos, yPos + 58);
					break;
				case 2:
					g.drawString(nameList.get(i), xPos, yPos + 114);
					break;
				case 3:
					g.drawString(nameList.get(i), xPos, yPos + 171);
					break;
				}
			}
		}
		xPos = x + 371;
		if (!pointList.isEmpty()) {
			for (int i = 0; i < pointList.size(); i++) {
				String res = pointList.get(i) + "";
				switch (i) {
				case 0:
					g.drawString(res, xPos, yPos + 1);
					break;
				case 1:
					g.drawString(res, xPos, yPos + 57);
					break;
				case 2:
					g.drawString(res, xPos, yPos + 113);
					break;
				case 3:
					g.drawString(res, xPos, yPos + 170);
					break;
				}
			}
		}	
	}
	
	public ArrayList<String> getNameList() { return nameList; }
	
	public void setNameList(ArrayList<String> aux) { nameList = aux; }
	
	public ArrayList<Integer> getPointList() { return pointList; }
	
	public void setPointList(ArrayList<Integer> aux) { pointList = aux; }
}
