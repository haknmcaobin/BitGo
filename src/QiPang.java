import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class QiPang extends JPanel {

	public static final int SPACE = 26;
	public static final int QIPANGSPACE = 20;
	public static final int QIPANGWIDTH = SPACE * 18 + QIPANGSPACE * 2;
	public static final int QIPANGHEIGHT = SPACE * 18 + QIPANGSPACE * 2;
	public static final int MOUSESPACE = 10;
	public static final int POINTCOUNT = 19 * 19;

	// judge state of every point
	public static final int EMPTY = 0;
	public static final int WHITE = 1;
	public static final int BLACK = 2;
	public static Map<Integer, Integer> mapStation = new HashMap<Integer, Integer>(); 
	public static QiPang currentQiPang = null;
	public static int qiziCount = 0;
	public static boolean realgame = false;
	public static Integer[] pointState = null;
	//monitor the number of black and white
	public static int setwqizi = 0;
	public static int setbqizi = 0;
	public static int ewqizi = 0;
	public static int ebqizi = 0;

	private List<Integer> xPoints = null;
	private List<Integer> yPoints = null;
	
	

	// parameters of xun huan jin zhuo dian
	private int iForbit = -1;

	public QiPang() {
		super();
		this.initPoints();
		this.setMinimumSize(new Dimension(QIPANGWIDTH, QIPANGHEIGHT));
		this.setMaximumSize(new Dimension(QIPANGWIDTH, QIPANGHEIGHT));
		this.mouseClick();
		this.setVisible(true);
		repaint();
	}

	// draw board
	public void drawBoard(Graphics g) {
		g.setColor(new Color(254, 215, 124));
		g.fillRect(0, 0, QIPANGWIDTH, QIPANGHEIGHT);
	}

	// draw board line
	public void drawLine(Graphics g) {
		g.setColor(Color.black);
		for (int i = 0; i < 19; i++) {
			g.drawLine(i * SPACE + QIPANGSPACE, QIPANGSPACE, i * SPACE
					+ QIPANGSPACE, SPACE * 18 + QIPANGSPACE);
			g.drawLine(QIPANGSPACE, i * SPACE + QIPANGSPACE, SPACE * 18
					+ QIPANGSPACE, i * SPACE + QIPANGSPACE);
		}
	}
	
	// draw letters and numbers of coordinate
	public void drawXY(Graphics g) {
		g.setColor(Color.black);
		for(int i=0; i<19; i++) {
			String str1 = ""+(i+1);
			String str2 = ""+(char)(('A')+i);
			g.drawString(str2, i*SPACE + QIPANGSPACE  - 3, QIPANGSPACE - 3);
			g.drawString(str2, i*SPACE + QIPANGSPACE  - 3, 19*SPACE + 10);
			g.drawString(str1, QIPANGSPACE/2 - 3*str1.length(), i*SPACE + QIPANGSPACE + 5);
			g.drawString(str1, 19*SPACE - 2, i*SPACE + QIPANGSPACE + 5);
		}
	}

	// draw xingwei
	public void drawPoint(Graphics g, int x, int y) {
		int ovalspace = 8;
		g.setColor(Color.black);
		g.fillOval(x - ovalspace / 2, y - ovalspace / 2, ovalspace, ovalspace);
	}

	// draw all xingwei
	public void drawAllPoint(Graphics g) {
		g.setColor(Color.black);
		for (int i = 0; i < 19; i++) {
			for (int j = 0; j < 19; j++) {
				if (i == 3 || i == 9 || i == 15) {
					if (j == 3 || j == 9 || j == 15) {
						drawPoint(g, i * SPACE + QIPANGSPACE, j * SPACE
								+ QIPANGSPACE);
					}
				}
			}
		}
	}

	// draw all existed Bitones when redrawing
	public void drawExistQiZi(Graphics g) throws IOException {
		boolean isBlack = true;
		for (int i = 0; i < pointState.length; i++) {
			if (pointState[i] == BLACK) {
				isBlack = true;
				new QiZi(false).drawQiZi(g, i, isBlack);
				//ºÚ£«1
				//ebqizi++;
				//System.out.println(ebqizi);
			} else if (pointState[i] == WHITE) {
				isBlack = false;
				new QiZi(false).drawQiZi(g, i, isBlack);
				//°×£«1
				//ewqizi++;
				//System.out.println(ewqizi);
			}
		}
		//System.out.println("redraw existed black:"+ebqizi);
	}

	// monitoring mouse click
	public void mouseClick() {
		this.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				int x = e.getX();
				int y = e.getY();
				drawMouseClickQiZi(x, y);
			}
		});
	}

	// display Bitone at clicked point
	public void drawMouseClickQiZi (int x, int y) {
		Graphics g = this.getGraphics();
		int xi = 0, yi = 0, xa = 0, ya = 0;
		for (int i = 0; i < 19; i++) {
			xa = Math.abs(x - xPoints.get(i));
			if (xa < MOUSESPACE) {
				xi = i;
				break;
			}
		}
		for (int i = 0; i < 19; i++) {
			ya = Math.abs(y - yPoints.get(i));
			if (ya < MOUSESPACE) {
				yi = i;
				break;
			}
		}

		int address = yi * 19 + xi;
		// danti xun huan jin zhuo dian_2
		if (iForbit == address) {
			return;
		} else {
			iForbit = -1;
		}
		boolean isBlack = false;
		if ((xa + ya < MOUSESPACE * 2) && pointState[address] == EMPTY) {
			QiZi qizi = new QiZi(true);
			qiziCount++;
			// set the state of this point: white or black
			if (qiziCount % 2 == 0) {
				pointState[address] = WHITE;			
				
			} else {
				pointState[address] = BLACK;
				isBlack = true;
				//setbqizi++;
				//System.out.println("set black:"+setbqizi);
			}

			// judge whether exist dead bitone;
			boolean bDead = false;
			int deadNum = 0;
			Set<Integer> differs = qizi.differQiZiArray(pointState, address);
			for (Iterator it = differs.iterator(); it.hasNext();) {
				int target = (Integer) it.next();
				int size = 0;
				if (pointState[target] != EMPTY
						&& qizi.isDeadQiZi(pointState, target)) {
					size = qizi.relationQiZi(pointState, target).length;
					pointState = qizi.handleDead(pointState, target);

					// danti xun huan jin zhuo dian_1
					if (size == 1) {
						if(pointState[address] == BLACK) {
							pointState[target] = WHITE;
						} else {
							pointState[target] = BLACK;
						}
						if(qizi.isDeadQiZi(pointState, address) && qizi.relationQiZi(pointState, address).length == 1) {
							iForbit = target;
						}				
						pointState[target] = EMPTY;
					}
					bDead = true;
					deadNum = deadNum + size;
				}
			}
			
			// zi ti jin zhuo dian
			if (bDead == false) {
				if (qizi.isDeadQiZi(pointState, address)) {
					qizi.clear();
					pointState[address] = EMPTY;
					qiziCount--;
					
					return;
				}
			}
			repaint();

			mapStation.put(qiziCount, address);
			
		}
		
		
	}

	// initial the positions of all points in the board
	public void initPoints() {
		xPoints = new ArrayList<Integer>();
		yPoints = new ArrayList<Integer>();
		pointState = new Integer[POINTCOUNT];
		for (int i = 0; i < pointState.length; i++) {
			pointState[i] = EMPTY;
		}
		for (int i = 0; i < 19; i++) {
			xPoints.add(i * SPACE + QIPANGSPACE);
			yPoints.add(i * SPACE + QIPANGSPACE);
		}
	}
	
	// display numbers(shou shu)
	public void playforeal(Graphics g) {
		//ebqizi = 0;
		for(int i=0; i<pointState.length; i++) {
			if(pointState[i] != EMPTY) {
				for(int j=mapStation.size(); j>0; j--) {
					if(mapStation.get(j) == i) {
						if(j%2 == 0) {
							g.setColor(Color.black);
							//ebqizi++;
						} else {
							g.setColor(Color.white);
						}
						String str = ""+j;
						//display count for game
						//g.drawString(str, qiziPoint(i).x - str.length()*3, qiziPoint(i).y + 5);
						//System.out.println("qizi existed:"+j);
						break;
					}
				}
			}
		}
	}
	
	public Point qiziPoint(int target) {
		Point point = new Point();
		point.x = target%19*SPACE + QIPANGSPACE;
		point.y = target/19*SPACE + QIPANGSPACE;
		return point;
	}
	
	public void paint(Graphics g) {
		drawBoard(g);
		drawLine(g);
		drawXY(g);
		drawAllPoint(g);
		
		
		//after modification
		try {
			this.drawExistQiZi(g);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		if(realgame) {
			this.playforeal(g);
		}
		g.setColor(Color.black);
		if (qiziCount % 2 == 0) {
			g.drawString("black's turn-Player A", 10, 10);
		} else {
			g.drawString("white's turn-Player B", 10, 10);
		}
	}

	public static QiPang getCurrentQiPang() {
		return currentQiPang;
	}

	public void setCurrentQiPang() {
		QiPang.currentQiPang = this;
	}

	public Integer[] getPointState() {
		return pointState;
	}

	public void setPointState(Integer[] pointState) {
		this.pointState = pointState;
	}
	public static void main(String[] args) {
		JFrame f = new JFrame();
		QiPang qipang = new QiPang();
		f.getContentPane().add(qipang);
		f.setBounds(new Rectangle(550, 550));
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
