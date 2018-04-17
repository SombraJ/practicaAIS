package Buscaminas;

import java.awt.*;
import java.awt.event.*;
import java.util.TimerTask;
import java.util.Timer;

import javax.swing.*;
public class Buscaminas extends JFrame implements ActionListener, MouseListener{
   
    int nomines;
    int newmines;
    int perm[][];
    String tmp;
    boolean found = false;
    int row;
    int column;
    int guesses[][];
    JButton b[][];
    
    int[][] mines;
    boolean allmines;
    int n, m;
    int mostrartiempo;
   Timer timer;
    TimerTask ttask;
    //int n = 30;
    //int m = 30;
    int deltax[] = {-1, 0, 1, -1, 1, -1, 0, 1};
    int deltay[] = {-1, -1, -1, 0, 0, 1, 1, 1};
    double starttime;
    double endtime;
   JFrame frame;
   JMenuBar menumb;
   JMenu menu1;
   JMenuItem reiniciar,nuevoJuego;
    JLabel minas,tiempo;

    public Buscaminas(int n1, int m1, int num){
        this.n = n1;
        this.m = m1;
        this.nomines = num;
        mostrartiempo=-1;
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        perm = new int[n][m];
        boolean allmines = false;
        guesses = new int [n+2][m+2];
        mines = new int[n+2][m+2];
        b = new JButton [n][m];
   
      frame= new JFrame("BUSCAMINAS");
      frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
      menumb = new JMenuBar();
      menu1= new JMenu("Opciones");
      reiniciar= new JMenuItem("Reiniciar");
      nuevoJuego = new JMenuItem("Nuevo Juego");
      newmines=nomines;
      minas=new JLabel("Minas:"+newmines+" ");
      tiempo=new JLabel("Tiempo:"+mostrartiempo);
      timer=new Timer();
      ttask = new TimerTask(){
            @Override
            public void run() {
                mostrartiempo++;
                tiempo.setText("Tiempo:"+mostrartiempo);
            }
          
      };
     
       timer.schedule(ttask, 0,1000);
       
      
     
     
     reiniciar.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent ev) {
            frame.dispose();
            new Buscaminas(n,m,nomines);
    }
        });
     nuevoJuego.addActionListener(new ActionListener() {
 	        public void actionPerformed(ActionEvent ev) {
 	            frame.dispose();
 	            Opciones();
 	    }
 	   });
     
      menumb.add(menu1);
      menu1.add(reiniciar);
      menu1.add(nuevoJuego);
      frame.setJMenuBar(menumb);
      menumb.add(minas);
       menumb.add(tiempo);
        frame.setLayout(new GridLayout(n,m));
        for (int y = 0;y<m+2;y++){
            mines[0][y] = 3;
            mines[n+1][y] = 3;
            guesses[0][y] = 3;
            guesses[n+1][y] = 3;
        }
        for (int x = 0;x<n+2;x++){
            mines[x][0] = 3;
            mines[x][m+1] = 3;
            guesses[x][0] = 3;
            guesses[x][m+1] = 3;
        }
        do {
            int check = 0;
            for (int y = 1;y<m+1;y++){
                for (int x = 1;x<n+1;x++){
                    mines[x][y] = 0;
                    guesses[x][y] = 0;
                }
            }
            for (int x = 0;x<nomines;x++){
                mines [(int) (Math.random()*(n)+1)][(int) (Math.random()*(m)+1)] = 1;
            }
            for (int x = 0;x<n;x++){
                for (int y = 0;y<m;y++){
                if (mines[x+1][y+1] == 1){
                        check++; //TOTAL DE MINAS
                    }
                }
            }
            if (check == nomines){
                allmines = true;
            }
        }while (allmines == false);
        for (int y = 0;y<m;y++){
            for (int x = 0;x<n;x++){
                if ((mines[x+1][y+1] == 0) || (mines[x+1][y+1] == 1)){
                    perm[x][y] = perimcheck(x,y);
                }
                b[x][y] = new JButton("?");
               
                b[x][y].addActionListener(this);
                b[x][y].addMouseListener(this);
                
               frame.add(b[x][y]);
               
                b[x][y].setEnabled(true);
                
            }//end inner for
        }//end for
       
        frame.pack();
        frame.setVisible(true);
        for (int y = 0;y<m+2;y++){
            for (int x = 0;x<n+2;x++){
                System.out.print(mines[x][y]);
            }
           
        System.out.println("");}
        starttime = System.nanoTime(); // GUARDA TIEMPO INICIAL
    }//end constructor Mine()
 
        public static void Opciones() {
 	   
 	   JFrame principal = new JFrame("Buscaminas");

 	   principal.setDefaultCloseOperation(EXIT_ON_CLOSE);
 	   principal.setSize(30, 30);
 	   principal.setBounds(500, 100, 150, 5);
 	   
 	   JMenuBar menumb = new JMenuBar();
 	   JMenu menu1= new JMenu("Opciones");
 	   JMenuItem reiniciar= new JMenuItem("Reiniciar");
 	   JMenuItem nuevoJuego = new JMenuItem("Nuevo Juego");
 	   
 	   menumb.add(menu1);
 	   menu1.add(reiniciar);
 	   principal.setJMenuBar(menumb);
 	      
 	   nuevoJuego.addActionListener(new ActionListener() {
 	        public void actionPerformed(ActionEvent ev) {
 	            principal.dispose();
 	            Opciones();
 	    }
 	   });
 	   
 	   JButton principiante = new JButton("Principiante");
 	   
 	   JButton  intermedio = new JButton("Intermedio");
 	   JButton experto = new JButton("Experto");  
 	   JButton   personalizado = new JButton("Personalizado");
 	   
 	   principiante.addActionListener(new ActionListener() {

 			public void actionPerformed(ActionEvent e) {
 				intermedio.setSelected(false);
 				experto.setSelected(false);
 				personalizado.setSelected(false);
 				int n = 10;
 				int m = 10;
 				int	nomines = 10;
 				principal.dispose();
 				new Buscaminas(n,m,nomines);
 			}
 	   });
 	   intermedio.addActionListener(new ActionListener() {

 			public void actionPerformed(ActionEvent e) {
 				experto.setSelected(false);
 				principiante.setSelected(false);
 				personalizado.setSelected(false);
 				int	n = 16;
 				int	m = 16;
 				int	nomines = 40;
 				principal.dispose();
 				new Buscaminas(n,m,nomines);
 			}
 	   });
 	   experto.addActionListener(new ActionListener() {

 			public void actionPerformed(ActionEvent e) {
 				intermedio.setSelected(false);
 				principiante.setSelected(false);
 				personalizado.setSelected(false);
 				int	n = 16;
 				int	m = 32;
 				int	nomines = 99;
 				principal.dispose();
 				new Buscaminas(n,m,nomines);
 			}
 	   });
 	   personalizado.addActionListener(new ActionListener() {

 			public void actionPerformed(ActionEvent e) {
 				intermedio.setSelected(false);
 				principiante.setSelected(false);
 				experto.setSelected(false);
                                principal.dispose();
 				JFrame marco = new JFrame("Personalizado");
 				marco.setLayout(new GridLayout(10,10));

 				marco.setDefaultCloseOperation(EXIT_ON_CLOSE);
 				marco.setBounds(500, 100, 50, 10);
 				   
 				JLabel titulo = new JLabel("Inserta las medidas: ");
 				marco.add(titulo);
 				
 				
 				JLabel etN = new JLabel("columnas:");
 				etN.setSize(10,10);
 				marco.add(etN);
 				JTextField inN = new JTextField();
 				inN.setBounds(50,135,50,25);
 				marco.add(inN);
 				
 				JLabel etM = new JLabel("filas:");
 				etM.setSize(10,10);
 				marco.add(etM);
 				JTextField inM = new JTextField();
 				inM.setBounds(125,135,50,25);
 				marco.add(inM);
 				
 				JLabel etMi = new JLabel("minas:");
 				etMi.setSize(10,10);
 				marco.add(etMi);
 				JTextField inMin = new JTextField();
 				inMin.setBounds(200,135,50,25);
 				marco.add(inMin);
 				
 				JButton boton = new JButton("Aceptar");
 				boton.setSize(20, 20);

 				marco.add(boton);
                                marco.setVisible(true);
                                marco.pack();
 				boton.addActionListener(new ActionListener() {

 		 			public void actionPerformed(ActionEvent e) {
 		 				int	n = Integer.parseInt(inN.getText());
 		 				int	m = Integer.parseInt(inM.getText());
 		 				int	nomines = Integer.parseInt(inMin.getText());
 		 				marco.dispose();
 		 				
 		 				new Buscaminas(m,n,nomines);
 		 				
 		 			}
 		 	   });
 				
 			}
 			});
 	   
    
 	   GridLayout tam = new GridLayout(10,10);
 	   principal.setLayout(tam);
 	   principal.add(principiante);
 	   principal.add(intermedio);
 	   principal.add(experto);
 	   principal.add(personalizado);
 	   
 	   

        principal.pack();
        principal.setVisible(true);
    }
        
    public void actionPerformed(ActionEvent e){
        found =  false;
        JButton current = (JButton)e.getSource();
        for (int y = 0;y<m;y++){
            for (int x = 0;x<n;x++){
                JButton t = b[x][y];
                if(t == current){
                    row=x;column=y; found =true;
                }
            }//end inner for
        }//end for
        if(!found) {
            System.out.println("didn't find the button, there was an error "); System.exit(-1);
        }
        Component temporaryLostComponent = null;
        if (b[row][column].getBackground() == Color.orange){
            
            return;
        }else if (mines[row+1][column+1] == 1){
                timer.cancel();
                JOptionPane.showMessageDialog(temporaryLostComponent, "You set off a Mine!!!!.");
                System.exit(0);
        } else {
            tmp = Integer.toString(perm[row][column]);
            if (perm[row][column] == 0){
                    tmp = " ";
            }
            b[row][column].setText(tmp);
            b[row][column].setEnabled(false);
            checkifend();
            if (perm[row][column] == 0){
                scan(row, column);
                checkifend();
            }
        }
    }
 
    public void checkifend(){
        int check= 0;
        for (int y = 0; y<m;y++){
            for (int x = 0;x<n;x++){
        if (b[x][y].isEnabled()){
            check++;
        }
            }}
        if (check == nomines){
            timer.cancel();
            endtime = System.nanoTime();// TIEMPO FINAL
            Component temporaryLostComponent = null;
            JOptionPane.showMessageDialog(temporaryLostComponent, "Congratulations you won!!! It took you "+(int)((endtime-starttime)/1000000000)+" seconds!");
        }
    }
 
    public void scan(int x, int y){
        for (int a = 0;a<8;a++){
            if (mines[x+1+deltax[a]][y+1+deltay[a]] == 3){
 
            } else if ((perm[x+deltax[a]][y+deltay[a]] == 0) && (mines[x+1+deltax[a]][y+1+deltay[a]] == 0) && (guesses[x+deltax[a]+1][y+deltay[a]+1] == 0)){
                if (b[x+deltax[a]][y+deltay[a]].isEnabled()){
                    b[x+deltax[a]][y+deltay[a]].setText(" ");
                    b[x+deltax[a]][y+deltay[a]].setEnabled(false);
                    scan(x+deltax[a], y+deltay[a]);
                }
            } else if ((perm[x+deltax[a]][y+deltay[a]] != 0) && (mines[x+1+deltax[a]][y+1+deltay[a]] == 0)  && (guesses[x+deltax[a]+1][y+deltay[a]+1] == 0)){
                tmp = new Integer(perm[x+deltax[a]][y+deltay[a]]).toString();
                b[x+deltax[a]][y+deltay[a]].setText(Integer.toString(perm[x+deltax[a]][y+deltay[a]]));
                b[x+deltax[a]][y+deltay[a]].setEnabled(false);
            }
        }
    }
 
    public int perimcheck(int a, int y){
        int minecount = 0;
        for (int x = 0;x<8;x++){
            if (mines[a+deltax[x]+1][y+deltay[x]+1] == 1){
                minecount++;
            }
        }
        return minecount;
    }
 
    public void windowIconified(WindowEvent e){
 
    }
 
    public static void main(String[] args){
        //new Buscaminas();
        Opciones();
    }
 
    public void mouseClicked(MouseEvent e) {
 
    }
 
    @Override
    public void mouseEntered(MouseEvent arg0) {
        // TODO Auto-generated method stub
    }
 
    @Override
    public void mouseExited(MouseEvent arg0) {
        // TODO Auto-generated method stub
    }
 
    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3) {
            found =  false;
            Object current = e.getSource();
            for (int y = 0;y<m;y++){
                    for (int x = 0;x<n;x++){
                            JButton t = b[x][y];
                            if(t == current){
                                    row=x;column=y; found =true;
                            }
                    }//end inner for
            }//end for
            if(!found) {
                System.out.println("didn't find the button, there was an error "); System.exit(-1);
            }
            if ((guesses[row+1][column+1] == 0) && (b[row][column].isEnabled())){
                b[row][column].setText("x");
                guesses[row+1][column+1] = 1;
                b[row][column].setBackground(Color.orange);
                newmines--;
                minas.setText("Minas:"+newmines+" ");
                
            } else if (guesses[row+1][column+1] == 1){
                b[row][column].setText("?");
                guesses[row+1][column+1] = 0;
                b[row][column].setBackground(null);
                newmines++;
                minas.setText("Minas:"+newmines+" ");
            }
        }
    }
 
    @Override
    public void mouseReleased(MouseEvent arg0) {
        // TODO Auto-generated method stub
    }
}//end class
