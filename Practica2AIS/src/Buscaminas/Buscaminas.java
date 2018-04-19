package Buscaminas;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.TimerTask;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.*;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
public class Buscaminas extends JFrame implements ActionListener, MouseListener{
   
    int nomines;
    int newmines;
    int perm[][];
    String tmp;
    boolean found = false;
   String nivel;
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
   
    int deltax[] = {-1, 0, 1, -1, 1, -1, 0, 1};
    int deltay[] = {-1, -1, -1, 0, 0, 1, 1, 1};
    double starttime;
    double endtime;
   JFrame frame;
   JMenuBar menumb;
   JMenu menu1,records;
   JMenuItem reiniciar,nuevoJuego,recordp,recordi,recorde;
    JLabel minas,tiempo;

    public Buscaminas(int n1, int m1, int num, String lvl){
        this.n = n1;
        this.m = m1;
        this.nomines = num;
        this.nivel=lvl;
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
      records= new JMenu("Mostrar Tiempos");
      recordp= new JMenuItem("Records Principiante");
      recordi=new JMenuItem("Records Intermedio");
      recorde=new JMenuItem("Records Experto");
      
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
            new Buscaminas(n,m,nomines,nivel);
    }
        });
     recordp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    frame.dispose();
                    JFrame f= new JFrame("TIEMPOS PRINCIPIANTE");
                    int count=1;
                    f.setSize(450, 250);
                    f.setLayout(new GridLayout(11,1));
                    File file= new File("principiante.txt");
                    file.createNewFile();
                    String textLine;
                    BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file.getAbsoluteFile()), "ISO-8859-1"));
                    while ((textLine = reader.readLine())!= null){
                        JLabel linea= new JLabel(count+"-"+textLine);
                        f.add(linea);
                        count++;
                    }
                    JButton volver= new JButton("Volver Inicio");
                    f.add(volver);
                    volver.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent ae) {
                            f.dispose();
                            Opciones();
                        }
                    });
                    f.setVisible(true);
                    //f.pack();
                } catch (IOException ex) {
                    Logger.getLogger(Buscaminas.class.getName()).log(Level.SEVERE, null, ex);
                }
               
            }
        });
     nuevoJuego.addActionListener(new ActionListener() {
 	        public void actionPerformed(ActionEvent ev) {
 	            frame.dispose();
 	            Opciones();
 	    }
 	   });
     
      menumb.add(menu1);
      menu1.add(records);
      records.add(recordp);
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
        frame.setLocationRelativeTo(null);
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
 	   
 	   JFrame principal = new JFrame("BUSCAMINAS");
           principal.setLocationRelativeTo(null);
 	   principal.setDefaultCloseOperation(EXIT_ON_CLOSE);
 	  principal.setSize(350, 250);
 	  
 	   JMenuBar menumb = new JMenuBar();
 	   JMenu menu1= new JMenu("Opciones");
 	   JMenuItem reiniciar= new JMenuItem("Reiniciar");
 	   JMenuItem nuevoJuego = new JMenuItem("Nuevo Juego");
           JMenu record = new JMenu("Records");
           JMenuItem recordp= new JMenuItem("Tiempos Principiante");
           menu1.add(record);
           record.add(recordp); 	   
 	   menumb.add(menu1);
 	   menu1.add(reiniciar);
 	   principal.setJMenuBar(menumb);
 	      menu1.add(nuevoJuego);
 	   nuevoJuego.addActionListener(new ActionListener() {
 	        public void actionPerformed(ActionEvent ev) {
 	            principal.dispose();
 	            Opciones();
 	    }
 	   });
 	   recordp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    principal.dispose();
                    JFrame f= new JFrame("TIEMPOS PRINCIPIANTE");
                    f.setSize(450, 250);
                    int count=1;
                     f.setLocationRelativeTo(null);
                    f.setDefaultCloseOperation(EXIT_ON_CLOSE);
                    f.setLayout(new GridLayout(11,1));
                    File file= new File("principiante.txt");
                    file.createNewFile();
                    String textLine;
                    BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file.getAbsoluteFile()), "ISO-8859-1"));
                    while ((textLine = reader.readLine())!= null){
                        JLabel linea= new JLabel(count+"-"+textLine);
                        
                        f.add(linea);
                        count++;
                    }
                    JButton volver = new JButton("Volver Inicio");
                    f.add(volver);
                    volver.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent ae) {
                            f.dispose();
                            Opciones();
                        }
                    });
                    f.setVisible(true);
                    
                } catch (IOException ex) {
                    Logger.getLogger(Buscaminas.class.getName()).log(Level.SEVERE, null, ex);
                }
               
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
                               
                                
                             Buscaminas buscaminas = new Buscaminas(n,m,nomines,"principiante");
                             
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
 				Buscaminas buscaminas = new Buscaminas(n,m,nomines,"intermedio");
                            
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
 				Buscaminas buscaminas = new Buscaminas(n,m,nomines,"experto");
                            
 			}
 	   });
 	   personalizado.addActionListener(new ActionListener() {

 			public void actionPerformed(ActionEvent e) {
 				intermedio.setSelected(false);
 				principiante.setSelected(false);
 				experto.setSelected(false);
                                principal.dispose();
                                 
 				JFrame marco = new JFrame("Personalizado");
                                
                                marco.setDefaultCloseOperation(EXIT_ON_CLOSE);
                                 marco.setSize(350, 250);
 				marco.setLayout(new GridLayout(8,4));
                                 marco.setLocationRelativeTo(null);
 				
 				
 				   
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
 				//boton.setSize(500, 50);

 				marco.add(boton);
                                
                                marco.setVisible(true);
                                //marco.pack();
 				boton.addActionListener(new ActionListener() {

 		 			public void actionPerformed(ActionEvent e) {
 		 				int	n = Integer.parseInt(inN.getText());
 		 				int	m = Integer.parseInt(inM.getText());
 		 				int	nomines = Integer.parseInt(inMin.getText());
 		 				marco.dispose();
 		 				
 		 				new Buscaminas(m,n,nomines,"personalizado");
 		 				
 		 			}
 		 	   });
 				
 			}
 			});
 	   
    
 	  
 	   principal.setLayout(new GridLayout(4,1));
 	   principal.add(principiante);
 	   principal.add(intermedio);
 	   principal.add(experto);
 	   principal.add(personalizado);
 	   
 	   

        //principal.pack();
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
            try {
                checkifend();
            } catch (IOException ex) {
                Logger.getLogger(Buscaminas.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (perm[row][column] == 0){
                scan(row, column);
                try {
                    checkifend();
                } catch (IOException ex) {
                    Logger.getLogger(Buscaminas.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
 
    public void checkifend() throws IOException{
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
            if(!nivel.equals("personalizado")){
            if (checkFichero()){

              String name =JOptionPane.showInputDialog(temporaryLostComponent, "Congratulations you won!!! It took you "+(int)((endtime-starttime)/1000000000)+" seconds!\n You got a new record in this level. Insert a player name if you want to save your time, press cancel if you dont want to save it.\n");
              if(name!=null){
                while (name.contains(" ")){
                    name=JOptionPane.showInputDialog(temporaryLostComponent, "Congratulations you won!!! It took you "+(int)((endtime-starttime)/1000000000)+" seconds!\n You got a new record in this level. Insert a player name if you want to save your time, press cancel if you dont want to save it.\n(Whitespace are forbbiden)");
                    if(name==null)
                        break;
                }
                if(name!=null)
                addPlayerTime(name);
              }
            }
            else{
                JOptionPane.showMessageDialog(temporaryLostComponent, "Congratulations you won!!! It took you "+(int)((endtime-starttime)/1000000000)+" seconds!");
            }
            }else{
                 JOptionPane.showMessageDialog(temporaryLostComponent, "Congratulations you won!!! It took you "+(int)((endtime-starttime)/1000000000)+" seconds!");
            }
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

    private boolean checkFichero() throws IOException {
         File f;
        
        ArrayList<String> tiempos = new ArrayList<>();
        String textLine;
        if(nivel.equals("principiante")){
        f= new File("principiante.txt");
        f.createNewFile();
        }else if(nivel.equals("intermedio")){
            f= new File("intermedio.txt");
            f.createNewFile();
        }else{
            f= new File("experto.txt");
            f.createNewFile();
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f.getAbsoluteFile()), "ISO-8859-1"));
        while ((textLine = reader.readLine())!= null){
        tiempos.add(textLine);
        }
        if(tiempos.size()<10){
            return true;
        }else {
            String tiempo = tiempos.get(9);
            String[] split = tiempo.split(" ");
            int n = Integer.parseInt(split[1]);
            if(mostrartiempo<n)
                return true;
        }
        return false;
    }

    private void addPlayerTime(String name) throws IOException {
        ArrayList<String> tiempos = new ArrayList<>();
        String textLine;
        PrintWriter writer;
       File f;
         if(nivel.equals("principiante")){
             f= new File("principiante.txt");
            
        }else if(nivel.equals("intermedio")){
            f= new File("intermedio.txt");
            
        }else{
            f= new File("experto.txt");
            
        }
        
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f.getAbsoluteFile()), "ISO-8859-1"));
        while ((textLine = reader.readLine())!= null){
          
        tiempos.add(textLine);
        }
        
        for (int i=0; i<=tiempos.size();i++){
            if(i==10)
                break;
            if(i==tiempos.size()){
                tiempos.add(name+" "+mostrartiempo+" segundos");
                break;
            }
             String tiempo = tiempos.get(i);
             String[] split = tiempo.split(" ");
             int n = Integer.parseInt(split[1]);
                if(mostrartiempo<n){
                    tiempos.add(i,name+" "+mostrartiempo+" segundos");
                    if(tiempos.size()==11)
                        tiempos.remove(10);
                    break;
                 }
        }
         if(nivel.equals("principiante")){
            writer =new PrintWriter("principiante.txt", "UTF-8");
            
        }else if(nivel.equals("intermedio")){
             writer =new PrintWriter("intermedio.txt", "UTF-8");
            
        }else{
            writer =new PrintWriter("experto.txt", "UTF-8");
            
        }
         
        for(String s:tiempos){
        writer.println(s);
       
        }
        writer.close();
    }
}//end class
