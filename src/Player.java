import java.awt.Image;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Properties;

import javax.swing.ImageIcon;


public class Player {
	private int qiziColor;
	private ImageIcon image = null; 
	private String name = null;
	private String bitbal = null;
	private String bitaddr = null;
	
	public Player(int qiziColor) {
		this.setQiziColor(qiziColor);
		try {
			this.init();
		} catch (IOException e) {
			e.printStackTrace();
//			System.out.println("cannot find WeiQi.properties");
		}
	}
	
	// initial players information
	public void init() throws IOException {
		File file = new File("src/WeiQi.properties");
		Reader in = new BufferedReader(new FileReader(file));
		Properties p = new Properties();
		p.load(in);
		if(this.getQiziColor() == QiPang.BLACK) {
			this.setImage(new ImageIcon(p.getProperty("bSourceImage")));
			this.setName((String)p.getProperty("bName"));
			this.setBitaddress((String)p.getProperty("bBitaddr"));
			this.setBitbalance((String)p.getProperty("bBitbal"));
		} else {
			this.setImage(new ImageIcon(p.getProperty("wSourceImage")));
			this.setName((String)p.getProperty("wName"));
			this.setBitbalance((String)p.getProperty("wBitbal"));
			this.setBitaddress((String)p.getProperty("wBitaddr"));
		}
	}
	
	public int getQiziColor() {
		return qiziColor;
	}

	public void setQiziColor(int qiziColor) {
		this.qiziColor = qiziColor;
	}

	public ImageIcon getImage() {
		return image;
	}

	public void setImage(ImageIcon image) {
		this.image = image;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getBitbalance() {
		return bitbal;
	}
	
	public void setBitbalance(String bitbal) {
	    this.bitbal = bitbal;
	}
	public String getBitaddress() {
		return bitaddr;
	}
	public void setBitaddress(String bitaddr) {
	    this.bitaddr = bitaddr;
	}
}	
