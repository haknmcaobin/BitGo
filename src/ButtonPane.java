import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedReader;  
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.python.util.PythonInterpreter;


public class ButtonPane extends JPanel {
	private static int count;
	private static Map<Integer, Integer> oldMaps = null;
	private int oldCount;
	private static boolean bNew = true;
	private Integer[] states;
	private Integer[] oldStates;
	
	public ButtonPane() {
		this.init();
	}
	
	public void init() {
		this.setLayout(new GridLayout(1, 5));
		// create components
		JButton btBack = new JButton("Withdraw");
		JButton btForward = new JButton("Forward");
		JButton btShow = new JButton("Playing For Real!");
		//JButton btResearch = new JButton("Record");
		JButton btQuit = new JButton("Quit");
		
		// add events for each component
		btBack.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				
				if(bNew == true) {
					oldCount = count = QiPang.qiziCount;
					oldStates = QiPang.currentQiPang.getPointState();
					oldMaps = (HashMap)((HashMap)QiPang.mapStation).clone();
					bNew = false;
				}
				if(count > 0 ) {
					int target = QiPang.mapStation.get(count);
					QiPang.pointState[target] = QiPang.EMPTY;
					QiPang.mapStation.remove(count);
					QiPang.currentQiPang.repaint();
					count--;
					return;
				}
				
			}
		});
		
		btForward.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if(bNew == true) {
					return;
				}
				if(count != oldMaps.size()) {
					count++;
				}
				if(count <= oldMaps.size()) {
					int target = oldMaps.get(count);
					if(count%2 == 1) {
						QiPang.pointState[target] = QiPang.BLACK;
					} else {
						QiPang.pointState[target] = QiPang.WHITE;
					}
					
					QiPang.mapStation.put(count, target);
					QiPang.currentQiPang.repaint();
				}
			}
		});
		
		btShow.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if(QiPang.realgame == false) {
					QiPang.realgame = true;
					btShow.setText("Practice Mode");
				} else {
					QiPang.realgame = false;
					btShow.setText("Playing For Real!");
				}
				QiPang.currentQiPang.repaint();
			}
		});
		
//		btResearch.addMouseListener(new MouseAdapter() {
//			public void mouseClicked(MouseEvent e) {
//			}
//		});
		
		btQuit.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				try {
					Process pr = Runtime.getRuntime().exec("python ~/BTNotice.py");
					pr.waitFor();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				System.exit(0);
			}
		});
		
		// add components
		this.add(btBack);
		this.add(btForward);
		this.add(btShow);
		//this.add(btResearch);
		this.add(btQuit);
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
}
