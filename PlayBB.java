import javax.swing.JFrame;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.LinkedList;


class MyFrame extends JFrame implements ActionListener{
	JFrame f = new JFrame();
	JMenuBar menuBar = new JMenuBar();
	JMenu menu1 = new JMenu("Edit");
	JMenuItem menuItem1 = new JMenuItem("New Game");
	JMenuItem subMenu = new JMenu("ChangeSize");
	JMenuItem subMenuItem1 = new JMenuItem("5×5 3Atoms");
	JMenuItem subMenuItem2 = new JMenuItem("6×6 3Atoms");
	JMenuItem subMenuItem3 = new JMenuItem("8×8 3-5Atoms");
	JMenuItem subMenuItem4 = new JMenuItem("10×10 4-8Atoms");
	MyPanel p = new MyPanel();

	
	MyFrame(){
		menuItem1.addActionListener(this);
		subMenuItem1.addActionListener(this);
		subMenuItem2.addActionListener(this);
		subMenuItem3.addActionListener(this);
		subMenuItem4.addActionListener(this);
	}
	void start(){
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(600,600);
		f.setTitle("Black Box");
		
		f.setJMenuBar(menuBar);
		menuBar.add(menu1);
		
		menu1.add(menuItem1);	menu1.add(subMenu);
		
		subMenu.add(subMenuItem1);	subMenu.add(subMenuItem2);
		subMenu.add(subMenuItem3);	subMenu.add(subMenuItem4);
		
		Container contentPane = f.getContentPane();
		contentPane.add(p);
		f.setVisible(true);
		p.start();
	}
	
	public void actionPerformed(ActionEvent e){
		//どのメニューが押されたかを調べる
		JMenuItem item = (JMenuItem) e.getSource();
		
		//メニュー文字列を取得
		String text = item.getText();
		//メニューの先頭の文字を取得
		char menuEv = text.charAt(0);	//Char型でn番目の文字を抜き出すcharAt()
		switch(menuEv){
			case '1' : p.reCreateBoard(10);	break;
			case '5' : p.reCreateBoard(5);	break;
			case '6' : p.reCreateBoard(6);	break;
			case '8' : p.reCreateBoard(8);	break;
			case 'N' : p.reCreateBoard();		break;
		}
	}
}

//ここでプレイ用のボードを作る（データだけ）
class MyPanel extends JPanel{
	PlayBB pbb;
	CreateBoard cb = new CreateBoard(5);
	int randMax;
	int random;
	
	MyPanel(){
		randMax = cb.ans.size();
	}
	
	void start(){
		Random rnd = new Random();
		random = rnd.nextInt(randMax);
		int random2 = rnd.nextInt(cb.atomsNum);
		int mirror;
		if(rnd.nextInt(100) > 35){
			mirror = 1;
		}else{
			mirror = 0;
		}

		
		cb.resetBoard();
		cb.setAtoms(random);
		cb.setMirror(random2,mirror);
		pbb = new PlayBB(this, cb);
		repaint();
	}
	
	void reCreateBoard(){
		Random rnd = new Random();
		random = rnd.nextInt(randMax);
		int random2 = rnd.nextInt(cb.atomsNum);
		int mirror;
		if(rnd.nextInt(100) > 35){
			mirror = 1;
		}else{
			mirror = 0;
		}
		
		cb.resetBoard();
		cb.setAtoms(random);
		cb.setMirror(random2,mirror);
		removeAllButton();
		pbb = new PlayBB(this, cb);
		repaint();
	}
	void reCreateBoard(int size){
		int atoms = 3;
		Random rnd = new Random();
		int rndAtoms;

		switch(size){
			case 5 :
			case 6 : cb = new CreateBoard(size,atoms);	break;
			case 7 : 
			case 8 : rndAtoms = rnd.nextInt(3)+3;	//3-5を生成。
				cb = new CreateBoard(size,rndAtoms);	break;
			case 9 :
			case 10 : rndAtoms = rnd.nextInt(5)+4;	//4-8を生成。
				cb = new CreateBoard(size,rndAtoms);	break;
		}
		
		int random = rnd.nextInt(randMax);
		int random2 = rnd.nextInt(cb.atomsNum);
		int mirror;
		if(rnd.nextInt(100) > 35){
			mirror = 1;
		}else{
			mirror = 0;
		}
		
		cb.resetBoard();
		cb.setAtoms(random);
		cb.setMirror(random2,mirror);
		removeAllButton();
		pbb = new PlayBB(this, cb);
		repaint();
	}
	
	void removeAllButton(){
		for(int i = pbb.bl.list.size()-1; i >= 0; i--){
			remove(pbb.bl.list.get(i));
		}
		remove(pbb.result);
	}
}

//プレイするときに触る部分
class PlayBB implements ActionListener{
	MyPanel myPanel;
	CreateBoard cb;
	btnList bl = new btnList();
	//int setNum;
	MyButton result = new MyButton("CHECK ANSWER");
	final int btnSize = 45;
	final int margin = 2;
	int beamCnt = 0;
	int bx=10, by=10;				//ボタンを配置するウィンドウ座標
	
	PlayBB(MyPanel myPanel, CreateBoard createBoard){
		myPanel = myPanel;
		cb = createBoard;
		//this.setNum = setNum;
		myPanel.setLayout(null);
		result.setBounds(100,10,180,30);
		result.setBackground(Color.GRAY);
		result.addActionListener(this);
		myPanel.add(result);
		
		for(int j = 0; j < cb.brdSize; j++){
			by += btnSize;
			for(int i = 0; i < cb.brdSize; i++){
				bx += btnSize;
				bl.add(new MyButton());
				if(i == 0 && (j == 0 || j == cb.brdSize-1) ){
				}else if(i == cb.brdSize-1 && (j == 0 || j == cb.brdSize-1) ){
				}else	if(i == 0 || j == 0 || i == cb.brdSize-1 || j == cb.brdSize-1){
					bl.setBounds(bl.size()-1,bx,by,btnSize,btnSize);
					bl.get(bl.size()-1).setBackground(Color.WHITE);
					bl.get(bl.size()-1).addActionListener(this);
					myPanel.add(bl.get(bl.size()-1));
				}else{
					bl.setBounds(bl.size()-1,bx,by,btnSize,btnSize);
					bl.get(bl.size()-1).setBackground(Color.BLACK);
					bl.get(bl.size()-1).addActionListener(this);
					myPanel.add(bl.get(bl.size()-1));
				}
			}
			bx = 10;
		}
	}
	
	public void actionPerformed(ActionEvent e){
		MyButton b = (MyButton) e.getSource();
		for(int i = 0; i < bl.size(); i++){
			if(bl.get(i) == b){
				//1～size,brdSizeの倍数と、-1したもの、brdSize*(size+1)～brdSize^2-1が、外枠
				if( (i > 0 && i < cb.brdSize-1) || (i % cb.brdSize == 0) || 
				(i % cb.brdSize == cb.brdSize-1) || (i > cb.brdSize*(cb.brdSize-1) && i < cb.brdSize*cb.brdSize) ){
					int x = i % cb.brdSize;
					int y = i / cb.brdSize;
					if(b.getText() == ""){
						fireBeam(x,y,i);
					}
				}else{
					if(bl.get(i).getBackground() == Color.BLACK){
						bl.get(i).setBackground(Color.RED);
						bl.get(i).check = true;
					}else if(bl.get(i).getBackground() == Color.RED){
						bl.get(i).setBackground(Color.YELLOW);
						bl.get(i).check = true;
					}else if(bl.get(i).getBackground() == Color.YELLOW){
						bl.get(i).setBackground(Color.BLACK);
						bl.get(i).check = false;
					}
				}
			}
		}
		int count = 0;
		for(int c = 0; c < bl.size(); c++){
			if(bl.get(c).check)	count++;
		}
		if(count == cb.atomsNum){
			result.setBackground(null);
		}else{
			result.setBackground(Color.GRAY);
		}
		if(b == result){		//答え合わせ
			for(int i = 0; i < bl.size(); i++){
				if(bl.get(i).check && (bl.get(i).getBackground() == Color.RED) ){
					//for(int j = 0; j < cb.atomsNum; j++){
						//if(cb.ans.get(setNum).get(j) == ( (i%(cb.brdSize)-1)*(cb.brdSize-2) + (i/(cb.brdSize)-1)*(cb.brdSize-2) ) ){}
					if(cb.board[i%cb.brdSize][i/cb.brdSize] == Info.Atom){
						bl.get(i).setBackground(Color.GREEN);
					}
					//}
				}else if(bl.get(i).check && (bl.get(i).getBackground() == Color.YELLOW) ){
					if(cb.board[i%cb.brdSize][i/cb.brdSize] == Info.Mirr){
						bl.get(i).setBackground(Color.GREEN);
					}
				}
			}
		}
	}
	
	void fireBeam(int x, int y,int i){
		int sx = x, sy = y;			//スタートした座標
		int dx = 0, dy = 0;			//光線の進む向き
		
		if(sx == 0 || sx == cb.brdSize - 1){
			if(sx == 0) dx = 1;
			else dx = -1;
		}else if(sy == 0 || sy == cb.brdSize - 1){
			if(sy == 0) dy = 1;
			else dy = -1;
		}
		HitOrRefAndXY ss = cb.stepBeam(sx+dx, sy+dy, dx, dy);
		switch(ss.r){
			case Hit :
				bl.get(i).setBackground(Color.RED);
				bl.get(i).setText("H");
				//board[sx][sy] = Info.Hit;
				break;
			case Ref :
				bl.get(i).setBackground(Color.YELLOW);
				bl.get(i).setText("R");
				//board[sx][sy] = Info.Ref;
				break;
			case None :
				if(ss.x == sx && ss.y == sy){
					bl.get(i).setBackground(Color.YELLOW);
					bl.get(i).setText("R");
					//board[sx][sy] = Info.Ref;
				}else{
					bl.get(i).setForeground(Color.BLUE);
					bl.get(i).setText(""+(++beamCnt));
					int j = ss.x + ss.y * cb.brdSize;
					bl.get(j).setForeground(Color.BLUE);
					bl.get(j).setText(""+beamCnt);
				}
				break;
		}
	}
	void check(){
		
	}
}

class MyButton extends JButton{//JButtonにcheckを追加したかっただけ
	boolean check = false;
	MyButton(){
		super();
	}
	MyButton(String str){
		super(str);
	}
}

class btnList{
	LinkedList<MyButton> list;
	
	btnList(){
		list = new LinkedList<MyButton>();
	}
	
	void add(MyButton b){
		list.add(b);
	}
	int size(){
		return list.size();
	}
	MyButton get(int i){
		return list.get(i);
	}
	void setBounds(int i,int x,int y,int height,int width){
		list.get(i).setBounds(x,y,height,width);
	}
}
	