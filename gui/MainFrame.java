package gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.CardLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.OverlayLayout;

import util.Resolution;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.util.List;
import javax.swing.JLabel;
import java.awt.Color;
import javax.swing.JList;
import javax.swing.JOptionPane;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.event.ListSelectionListener;

import gameEngine.Game;

import javax.swing.event.ListSelectionEvent;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.Font;

public class MainFrame {

	private JFrame frame;
	private JPanel mainPane;
	private JPanel gamingPane;
	private JPanel configPane;
	private int agents = 21;
	private int mapSize = 64;
	private Resolution resolutions;
	private List<int[]> supportedResolutions;
	private RectDraw rect;
	private int[] cellSize;
	
	private Game game;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame window = new MainFrame();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainFrame() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		cellSize = new int[2];
		rect= new RectDraw();
		resolutions = new Resolution();
		final LinesComponent comp = new LinesComponent();
		//game = new game(mapSize,agents);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int[] standardResolution = new int[]{(int) screenSize.getWidth(), (int) screenSize.getHeight()};
		supportedResolutions = resolutions.supportedResolutions(standardResolution);
		frame = new JFrame();
		frame.setTitle("Presa x Predador");
		frame.setBounds(0, 0, standardResolution[0],standardResolution[1]);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.getContentPane().setLayout(new CardLayout(0, 0));
		
		/////// main pane //////
		mainPane = new JPanel();
		frame.getContentPane().add(mainPane, "name_3939467628732");
		
		JButton btnNovogame = new JButton("new game");
		btnNovogame.setFont(new Font("Dialog", Font.BOLD, 21));
		btnNovogame.setBackground(Color.ORANGE);
		btnNovogame.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnNovogame.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnNovogame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				game = new Game(mapSize,agents);
				game.playing = true;
				mainPane.setVisible(false);
				frame.setContentPane(gamingPane);
				cellSize[0] =frame.getContentPane().getWidth()/64;
				cellSize[1]= frame.getContentPane().getHeight()/64;
				frame.getContentPane().setSize(cellSize[0] * 64, cellSize[1]*64);
				game.update(frame, cellSize,rect);
				comp.drawLines(cellSize, frame);
				
				gamingPane.setVisible(true);
				JOptionPane.showMessageDialog(null, "Press ->(right arrow) for play", "how to play ", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		mainPane.setLayout(new GridLayout(0, 2, 0, 0));
		btnNovogame.setBounds(frame.getWidth()/2, frame.getHeight()/2,frame.getWidth()/10, frame.getHeight()/15);
		mainPane.add(btnNovogame);
		
		JButton btnConfiguracoes = new JButton("config");
		btnConfiguracoes.setFont(new Font("Dialog", Font.BOLD, 21));
		btnConfiguracoes.setBackground(Color.ORANGE);
		btnConfiguracoes.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				mainPane.setVisible(false);
				frame.setContentPane(configPane);
				configPane.setVisible(true);
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				btnConfiguracoes.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnConfiguracoes.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
		});
		btnConfiguracoes.setBounds(frame.getWidth()/4, frame.getHeight()/2,frame.getWidth()/10, frame.getHeight()/15);
		mainPane.add(btnConfiguracoes);
		mainPane.setVisible(true);
		
		/////// gaming pane //////
		gamingPane = new JPanel();
		gamingPane.setFocusable(false);
		frame.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				int keyCode = e.getKeyCode();
				if(keyCode == KeyEvent.VK_RIGHT && game.playing){
					if((game.sharksCounter == 0) || (game.fishesCounter == 0)){
						if (game.totalAgents() == 0){
							int option =  JOptionPane.showConfirmDialog(null,
									"No one won everyone died!\n"+
							        "Play again?",
								    "Play again",
								    JOptionPane.YES_NO_OPTION);
							if(option == JOptionPane.YES_OPTION){
								game = new Game(mapSize,agents);
								game.playing = true;
							}else{
								frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
							}

						}
						
						else if(game.fishesCounter == 0 ){
							int option =  JOptionPane.showConfirmDialog(null,
									"Sharks won! All fish died!\n"+
							        "Play again?",
								    "play again",
								    JOptionPane.YES_NO_OPTION);
							if(option == JOptionPane.YES_OPTION){
								game = new Game(mapSize,agents);
								game.playing = true;
							}else{
								frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
							}
							
						}else if(game.sharksCounter == 0){
							int option =  JOptionPane.showConfirmDialog(null,
									"Fishes won! All shark died!\n"+
							        "Play again?",
								    "play again",
								    JOptionPane.YES_NO_OPTION);
							if(option == JOptionPane.YES_OPTION){
								game = new Game(mapSize,agents);
								game.playing = true;
							}else{
								frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
							}
							
						}
						rect.cleanRects();
					}else{
						game.update(frame, cellSize, rect);
					}

				}
				if(keyCode == KeyEvent.VK_ESCAPE){
					game.playing = false;
					frame.getContentPane().setVisible(false);
					frame.setContentPane(mainPane);
					mainPane.setVisible(true);
				}
			}
		});

		frame.getContentPane().add(gamingPane, "name_3965257079370");
		gamingPane.setLayout(new OverlayLayout(gamingPane));
		gamingPane.setVisible(false);
		comp.setSize(frame.getWidth(), frame.getHeight());
		gamingPane.add(comp);
		////// config pane //////
		configPane = new JPanel();
		frame.getContentPane().add(configPane, "name_3991800129003");
		

		DefaultListModel model = new DefaultListModel();
		for(int[] resolution : supportedResolutions){
			model.addElement(resolution[0] + "x"+resolution[1]);
		}
		JList list = new JList(model);
		list.setFocusable(false);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		 ListSelectionListener listSelectionListener = new ListSelectionListener() {
		      public void valueChanged(ListSelectionEvent listSelectionEvent) {
		        boolean adjust = listSelectionEvent.getValueIsAdjusting();
		        if (!adjust) {

					 int[] resolution = supportedResolutions.get(list.getSelectedIndex());
					 frame.setSize(resolution[0], resolution[1]);
					 cellSize[0] =frame.getContentPane().getWidth()/64;
					 cellSize[1]= frame.getContentPane().getHeight()/64;
					 gamingPane.setSize(cellSize[0] * 64, cellSize[1]*64);
					 frame.setLocationRelativeTo(null);
		        }
		      }
		    };
		list.addListSelectionListener(listSelectionListener);
		configPane.setLayout(new BoxLayout(configPane, BoxLayout.X_AXIS));
		list.setBounds(frame.getWidth()/2, frame.getHeight()/2, frame.getWidth()/6,frame.getHeight()/6);
		JScrollPane pane = new JScrollPane(list);
		configPane.add(pane);
		
		JLabel lblResolucoes = new JLabel("Resolucoes");
		lblResolucoes.setFont(new Font("Dialog", Font.BOLD, 21));
		pane.setColumnHeaderView(lblResolucoes);
		lblResolucoes.setBackground(Color.GRAY);
		
		JButton btnNewButton = new JButton("Voltar");
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				frame.getContentPane().setVisible(false);
				frame.setContentPane(mainPane);
				mainPane.setVisible(true);
			}
			public void mouseEntered(MouseEvent e) {
				btnNewButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnNewButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
		});
		btnNewButton.setFont(new Font("Dialog", Font.BOLD, 21));
		btnNewButton.setBackground(Color.ORANGE);
		pane.setRowHeaderView(btnNewButton);
		configPane.setVisible(false);
		frame.setFocusable(true);
		
	}
}

	 
	