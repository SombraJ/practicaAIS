package Buscaminas;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.TimerTask;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.*;
import static javax.swing.JFrame.EXIT_ON_CLOSE;

public class Buscaminas extends JFrame implements ActionListener, MouseListener, Serializable{ //Clase BUSCAMINAS
   //Creacion de atributos
        //Atributos del código original
    int nomines; //Numero de minas total
    int perm[][]; //Matriz que contendra el perimetro del tablero tambien
    String tmp; //variable temporal
    boolean found = false;
    int row; //variable para fila
    int column;//variable para columna
    int guesses[][]; //matriz de adivinados
    JButton b[][]; //Matriz de botones que se mostrará en el frame del juego
    int[][] mines; // matriz de minas que guarda las posiciones de las minas
    boolean allmines; // booleano que chequea si estan todas las minas
    int n, m; //atributos de numero de fila y numero de columna
    //Variables para el movimiento a partir de una casilla y cubrir las 8 posiciones de alrededor
    int deltax[] = {-1, 0, 1, -1, 1, -1, 0, 1}; 
    int deltay[] = {-1, -1, -1, 0, 0, 1, 1, 1};
    //Atributos añadidos
    int mostrartiempo;//Variable que mostrará el tiempo en la pantalla de juego(Contará segundo a segundo).
    int newmines;//Variable que mostrará el número de minas restantes.
    String nivel; //Variable String que indica que nivel de dificultad se ha elegido.
    Timer timer;//Timer que controla la variable mostrartiempo
    TimerTask ttask;//TimerTaks que indicará como funcionará el timer.
    JFrame frame;//Pantalla
    JMenuBar menumb;//Barra del menú
    JMenu menu1;//Menú dsplegable 
    JMenuItem reiniciar,nuevoJuego,guardar,cargar; //Opciones del menú
    JLabel minas,tiempo; // Etiquetas en las que se mostrarán las variables mostrartiempo y newmines.

    public Buscaminas(int n1, int m1, int num, String lvl){ //Primer constructor básico del buscaminas, es el que se llama al crear un juego nuevo.
       //Inicialización de los atributos.
        this.n = n1;
        this.m = m1;
        this.nomines = num; 
        this.nivel=lvl; // Se guarda en el String el nivel que se ha seleccionado
        mostrartiempo=-1;//El contador empieza en -1 para que se ajuste bien al Timer y a la muestra por pantalla en tiempo real 
        this.setDefaultCloseOperation(EXIT_ON_CLOSE); // Cierre del buscaminas cuando se presiona la "X" de la esquina del frame
        perm = new int[n][m]; // Se crea un perm del tamaño del tablero de juego
        boolean allmines = false; 
        //guesses y mines tienen un perimetro adicional al tablero, que lo rodea para añadir mas informacion.
        guesses = new int [n+2][m+2];
        mines = new int[n+2][m+2];
        b = new JButton [n][m]; // Creacion de la matriz de botones segun la n y m que se pasan al constructor
         newmines=nomines;//En un juego nuevo la variable que muestra las minas empieza con el numero de minas total.
        //Creación de la pantalla
      frame= new JFrame("BUSCAMINAS"); //Frame general y nombre del titulo que saldrá arriba
      frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
      menumb = new JMenuBar(); // Barra del menú
      menu1= new JMenu("Opciones"); // Menú desplegable de opciones
      //Opciones del menú
      reiniciar= new JMenuItem("Reiniciar");
      nuevoJuego = new JMenuItem("Nuevo Juego");
      guardar = new JMenuItem("Guardar Partida");
     cargar = new JMenuItem("Cargar Partida");
      
     //creacion de las etiquetas que muestran minas y tiempo a tiempo real.
     // Utilizando las variables que hemos creado para ello, saldran en la barra del menú encima del tablero.
      minas=new JLabel("Minas:"+newmines+" "); 
      tiempo=new JLabel("Tiempo:"+mostrartiempo);
      //Creacion del timer y su timertask que ira sumando de uno en uno cada segundo y que servirá de cronómetro.
      timer=new Timer();
      ttask = new TimerTask(){
            @Override
            public void run() {
                mostrartiempo++;//se suma uno
                tiempo.setText("Tiempo:"+mostrartiempo);//cada vez que se suma se actualiza la etiqueta
            }
          
      };
     
       timer.schedule(ttask, 0,1000);//Con la funcion schedule se asinga al timer la funcion ttask y se pone qe se realice cada segundo(1000milisegundos).
       
      
     
     //ActionListener de la opcion reiniciar del menu:Elimina el frame y vuelve a llamar a un nuevo buscaminas con las mismas opciones básicas.
     reiniciar.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent ev) {
            frame.dispose();
            new Buscaminas(n,m,nomines,nivel);
            
        }
     });
     //ActionListener de la opcion cargar del menu:Coge el fichero de la ultima partida guardada y lo carga en un nuevo objeto Buscaminas que utiliza otro constructor para juegos ya empezados.
     cargar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                //Nuevas variables
                int n,m,nomines,newmines,row,column,mostrartiempo;
                int[][] perm,guesses,mines;
                String nivel,tmp;
                JButton [][] b;
                boolean found;
                   ObjectInputStream oos = null;
                try {
                    //Apertura de fichero
                   oos = new ObjectInputStream(new FileInputStream("partida.txt"));
                    
                    //Lectura en orden de los elementos segun se han guardado
                    n= oos.readInt();
                    m=oos.readInt();
                    nomines=oos.readInt();
                    nivel = (String)oos.readObject();
                    newmines=oos.readInt();
                    perm=(int[][]) oos.readObject();
                    tmp= (String) oos.readObject();
                    found= oos.readBoolean();
                    row= oos.readInt();
                    column= oos.readInt();
                    guesses= (int [][]) oos.readObject();
                    mines=(int[][]) oos.readObject();
                    mostrartiempo=oos.readInt();

                    //recogida de los valores de los botones y creacion de la nueva variable "b" que se crea nueva con los valores de los string metidos en guardar
                    b = new JButton[n][m];

                        for(int i=0;i<n;i++){
                            for(int j=0;j<m;j++){
                             b[i][j] =(new JButton((String) oos.readObject()));//Para cada String guardado se crea un nuevo JButton y se añade a la matriz de botones que se pasa al Constructor
                           
                            }
                        }
                    //Eliminación del frame actual y llamada a nuevo Buscaminas con el constructor 2.
                    frame.dispose();
                    new Buscaminas(n,m,nomines,nivel,newmines,perm,tmp,found,row,column,guesses,b,mines,mostrartiempo);
                    
                   
                    //Posibles excepciones que se lanzan.
                    } catch (FileNotFoundException ex) {
                    Logger.getLogger(Buscaminas.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(Buscaminas.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(Buscaminas.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
        });
     //Opcion Nuevo juego del menú: carga de nuevo el Menú Opciones que muestra el menu principal donde se eligen los niveles.
     nuevoJuego.addActionListener(new ActionListener() {
 	        public void actionPerformed(ActionEvent ev) {
 	            frame.dispose();
 	            Opciones();
 	    }
    });
     //Opcion guardar del menú, guarda la partida actual en un fichero de texto.
     guardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {

                ObjectOutputStream oos = null;
                try {
                    //apertura del fichero
                   oos = new ObjectOutputStream(new FileOutputStream("partida.txt"));
                    //escritura de los elementos de la partida actual necesarios para el futuro en un orden concreto para luego leerlos por el mismo orden.
                   oos.writeInt(n);
                    oos.writeInt(m);
                   oos.writeInt(nomines);
                    oos.writeObject(nivel);
                    oos.writeInt(newmines);
                    oos.writeObject(perm);
                    oos.writeObject(tmp);
                    oos.writeBoolean(found);
                   oos.writeInt(row);
                    oos.writeInt(column);
                    oos.writeObject(guesses);
                  
                    oos.writeObject(mines);
                    oos.writeInt(mostrartiempo);
                    //tenia un fallo al pasar los botones al fichero, he pasado manualmente los valores en uvarios string que luego se recogen en la lectura por el mismo orden
                    for (JButton[] boton : b){
                        for (JButton boton2 : boton){
                            oos.writeObject(boton2.getText());//Escritura del String que contenía el JButton
                            
                           
                           
                        }
                    }
                   
                //Excepciones
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(Buscaminas.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(Buscaminas.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    try {
                        oos.close(); // Cierre de fichero
                    } catch (IOException ex) {
                        Logger.getLogger(Buscaminas.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            
             
        });
     //Creación del menú donde se añaden las opciones del menú al desplegable
      menumb.add(menu1);
      menu1.add(reiniciar);
      menu1.add(nuevoJuego);
      frame.setJMenuBar(menumb);
      menumb.add(minas);
       menumb.add(tiempo);
       menu1.add(guardar);
       menu1.add(cargar);
       
       //Creación del fondo de la pantalla y de las variables originales
       
        frame.setLayout(new GridLayout(n,m));
        
        for (int y = 0;y<m+2;y++){ // se rellenan las columnas de los perimetros extras que se habian puesto de mines y guesses con 3 para marcar el final del tablero
            mines[0][y] = 3;
            mines[n+1][y] = 3;
            guesses[0][y] = 3;
            guesses[n+1][y] = 3;
        }
        for (int x = 0;x<n+2;x++){ //  se rellenan las filas de los perimetros extras que se habian puesto de mines y guesses con 3 para marcar el final del tablero
            mines[x][0] = 3;
            mines[x][m+1] = 3;
            guesses[x][0] = 3;
            guesses[x][m+1] = 3;
        }
        do { // "do" que inserta las minas y rellena las matrices 
            int check = 0;
            for (int y = 1;y<m+1;y++){ // El resto de las matrices se inicializa a 0 dejando los perimetros con 3
                for (int x = 1;x<n+1;x++){
                    mines[x][y] = 0;
                    guesses[x][y] = 0;
                }
            }
            for (int x = 0;x<nomines;x++){ // Se añaden tantas minas como diga la variable nomines(Nº de minas que se pasa al constructor ) de forma aleatoria en las columnas y filas
                mines [(int) (Math.random()*(n)+1)][(int) (Math.random()*(m)+1)] = 1;
            }
            for (int x = 0;x<n;x++){
                for (int y = 0;y<m;y++){
                if (mines[x+1][y+1] == 1){ // Se recorre la matriz mines contando donde se ha puesto un 1, es decir mina insertada y en ese caso se suma uno a check.
                        check++;
                    }
                }
            }
            if (check == nomines){ // si check es igual al numero de minas, se han introducido todas las minas de forma correcta
                allmines = true; // entonces allmines vale true, todas las minas insertadas
            }
        }while (allmines == false); // repetir el do hasta qe se inserten todas las minas
        for (int y = 0;y<m;y++){
            for (int x = 0;x<n;x++){
                if ((mines[x+1][y+1] == 0) || (mines[x+1][y+1] == 1)){//Donde haya un 0 o un 1 en la matriz de mines equivalente ( Recordar que tenia perimetro extra por eso el +1 ).
                    perm[x][y] = perimcheck(x,y); // se llama a la funcion perimcheck para cada posicion y se rellena la matriz de perm.
                }
                b[x][y] = new JButton("?"); // para esa posicion de x,y se crea un boton con interrogación 
               //Se le activan los action y mouse listener con this, esto les asignaria alas funciones creadas al final de este codigo.
                b[x][y].addActionListener(this);
                b[x][y].addMouseListener(this);
                
               frame.add(b[x][y]);// se añade ese boton al frame
               
                b[x][y].setEnabled(true);// se marca como accesible para pinchar con el raton
                
            }//end inner for
        }//end for
        //activacion del frame
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);
        
        for (int y = 0;y<m+2;y++){ // se escriben por la consola la matriz de mines con las minas
            for (int x = 0;x<n+2;x++){
                System.out.print(mines[x][y]);
            }
           
        System.out.println("");}
      
    }//end constructor Mine()
    //Constructor 2 de la clase Buscaminas que carga un Buscaminas a mitad de partida.
 public Buscaminas(int n1, int m1, int num, String lvl,int newmines, int[][] perm,String tmp, boolean found, int row, int column, int guesses[][],JButton b[][],int[][] mines,int mt){
       //Inicializacion de variables del constructor
        this.n = n1;
        this.m = m1;
        this.nomines = num;
        this.nivel=lvl;
        this.newmines=newmines;
        this.perm=perm;
        this.tmp=tmp;
        this.mostrartiempo=mt-1;// se resta uno para que encaje el tiempo 
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        boolean allmines = false;
        this.guesses = guesses;
        this.mines = mines;
        this.b=b;
        //El resto de atributos de la clase se crean como en el constructor básico(1).
        //Creacion del frame como en el constructor 1.
      frame= new JFrame("BUSCAMINAS");//Creacion del frame con titulo "BUSCAMINAS".
      frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
      menumb = new JMenuBar();//Creacioó de la barra de menú donde van las opciones y el número de minas y el tiempo
      menu1= new JMenu("Opciones"); //Menú desplegable
      //Opciones del menú
      reiniciar= new JMenuItem("Reiniciar");
      nuevoJuego = new JMenuItem("Nuevo Juego");
      guardar = new JMenuItem("Guardar Partida");
      cargar = new JMenuItem("Cargar Partida");
      
      //Creación de las Jlabel como en el constructor 1.
      minas=new JLabel("Minas:"+newmines+" ");
      tiempo=new JLabel("Tiempo:"+mostrartiempo);
      //Se usa el mismo timer y timertask que en el constructor 1. Funciona exactamente igual.
      timer=new Timer();
      ttask = new TimerTask(){
            @Override
            public void run() {
               mostrartiempo++;
                tiempo.setText("Tiempo:"+mostrartiempo);
            }
          
      };
     
       timer.schedule(ttask, 0,1000);
       
      
     
     //Opcion reiniciar hace lo mismo en ambos constructores
     reiniciar.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent ev) {
            frame.dispose();
            new Buscaminas(n,m,nomines,nivel);
            
    }
        });
     //Opcion cargar hace lo mismo en ambos constructores
     cargar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                int n,m,nomines,newmines,row,column,mostrartiempo;
                int[][] perm,guesses,mines;
                String nivel,tmp;
                JButton [][] b;
                boolean found;
                   ObjectInputStream oos = null;
                try {
                    //Apertura de fichero
                   oos = new ObjectInputStream(new FileInputStream("partida.txt"));
                    
                    //Lectura por orden de los atributos que llamaran al constructor
                    n= oos.readInt();
                    m=oos.readInt();
                    nomines=oos.readInt();
                    nivel = (String)oos.readObject();
                    newmines=oos.readInt();
                    perm=(int[][]) oos.readObject();
                    tmp= (String) oos.readObject();
                    found= oos.readBoolean();
                    row= oos.readInt();
                    column= oos.readInt();
                    guesses= (int [][]) oos.readObject();
                    mines=(int[][]) oos.readObject();
                    mostrartiempo=oos.readInt();
                   
                    
                    b = new JButton[n][m];
                     //Se lee los String de los valores de los botones uno a uno y se crea de nuevo la matriz
                    for(int i=0;i<n;i++){
                        for(int j=0;j<m;j++){
                            b[i][j] =(new JButton((String) oos.readObject()));
                           
                        }
                    }
                    frame.dispose();
                    new Buscaminas(n,m,nomines,nivel,newmines,perm,tmp,found,row,column,guesses,b,mines,mostrartiempo);//Llamada al nuevo constructor con los atributos leídos.
                    
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(Buscaminas.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(Buscaminas.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(Buscaminas.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
        });
     //Opcion nuevo juego funciona igual que en el constructor 1
     nuevoJuego.addActionListener(new ActionListener() {
 	        public void actionPerformed(ActionEvent ev) {
 	            frame.dispose();
 	            Opciones();
 	    }
 	   });
     //Opcion guardar funcion igual que en el constructor 1
     guardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {

                ObjectOutputStream oos = null;
                try {
                    //Apertura de fichero
                   oos = new ObjectOutputStream(new FileOutputStream("partida.txt"));
                    //Escritura en el fichero por el mismo orden
                   oos.writeInt(n);
                    oos.writeInt(m);
                   oos.writeInt(nomines);
                    oos.writeObject(nivel);
                    oos.writeInt(newmines);
                    oos.writeObject(perm);
                    oos.writeObject(tmp);
                    oos.writeBoolean(found);
                   oos.writeInt(row);
                    oos.writeInt(column);
                    oos.writeObject(guesses);
                  
                    oos.writeObject(mines);
                    oos.writeInt(mostrartiempo);
                    //se pasan los valores de los JButton uno a uno al fichero ( daba excepcion si se pasa la matriz original)
                    for (JButton[] boton : b){
                        for (JButton boton2 : boton){
                            oos.writeObject(boton2.getText());//Se escriben los String de la matriz de botones
                            
                           
                           
                        }
                    }
                   
               
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(Buscaminas.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(Buscaminas.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    try {
                        oos.close();
                    } catch (IOException ex) {
                        Logger.getLogger(Buscaminas.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            
             
        });
     //Creacion del menú como en el otro constructor
      menumb.add(menu1);
      menu1.add(reiniciar);
      menu1.add(nuevoJuego);
      frame.setJMenuBar(menumb);
      menumb.add(minas);
      menumb.add(tiempo);
      menu1.add(guardar);
      menu1.add(cargar);
     //Creación del contenido del frame, es distinto del constructor 1 ya que se carga una partida a medias
      frame.setLayout(new GridLayout(n,m));
        
       
        
        for (int y = 0;y<m;y++){//Para cada elemento de b(array de botones que es la pantalla de minas)
            for (int x = 0;x<n;x++){
                
                b[x][y].addActionListener(this);
                b[x][y].addMouseListener(this);
                if(b[x][y].getText().equals("x"))//Si el boton era una X, se marca la casilla con naranja 
                     b[x][y].setBackground(Color.orange);
                //Si la casilla era un numero era una casilla que el usuario habia desbloqueado y se marcan como false todas.
                if(b[x][y].getText().equals("1"))
                     b[x][y].setEnabled(false);
                if(b[x][y].getText().equals("2"))
                     b[x][y].setEnabled(false);
                 if(b[x][y].getText().equals("3"))
                     b[x][y].setEnabled(false);
                if(b[x][y].getText().equals("4"))
                     b[x][y].setEnabled(false);
                if(b[x][y].getText().equals("5"))
                     b[x][y].setEnabled(false);
                if(b[x][y].getText().equals("6"))
                     b[x][y].setEnabled(false);
                if(b[x][y].getText().equals("7"))
                     b[x][y].setEnabled(false);
                if(b[x][y].getText().equals("8"))
                     b[x][y].setEnabled(false);
               
               frame.add(b[x][y]); 
                if(b[x][y].getText().equals(" "))//si el elemento era casilla vacia tambien se marca a falso.
                    b[x][y].setEnabled(false);
                
                
            }//end inner for
        }//end for
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);
        
        for (int y = 0;y<m+2;y++){//Se escriben las minas en consola como en el constructor básico que carga una partida nueva.
            for (int x = 0;x<n+2;x++){
                System.out.print(mines[x][y]);
            }
           
        System.out.println("");}
        
    }//end constructor 2 . Carga una partida a medias
        public static void Opciones() {//Función que crea el MenúPrincipal y da opcion a elegir un nivel de dificultad y a unas opciones de menú(ver tiempos,
 	   //creacion del frame
 	   JFrame principal = new JFrame("BUSCAMINAS");// se crea y se pone como titulo "BUSCAMINAS"
           principal.setLocationRelativeTo(null);
 	   principal.setDefaultCloseOperation(EXIT_ON_CLOSE);//Cierre en la "X" de la esquina
           principal.setSize(350, 250);//Se establece un tamaño
 	  //creacion del menu
 	   JMenuBar menumb = new JMenuBar(); // Barra de menú
 	   JMenu menu1= new JMenu("Opciones"); //Desplegable con opciones
 	  //Opciones
 	   JMenuItem nuevoJuego = new JMenuItem("Nuevo Juego");
           JMenu record = new JMenu("Records");
           //En este menú se pone la opcion de mostrar los tiempos ya que es el menú principal
           JMenuItem recordp= new JMenuItem("Tiempos Principiante");
           JMenuItem recordi= new JMenuItem("Tiempos Intermedio");
           JMenuItem recorde= new JMenuItem("Tiempos Experto");
           //Se deja la opcion de cargar la partida, pero no de guardar porque aqui no se esta jugando ninguna
           JMenuItem cargarp= new JMenuItem("CargarPartida");
           menu1.add(cargarp);
           menu1.add(record);
           record.add(recordp); 
           record.add(recordi);
           record.add(recorde);
 	   menumb.add(menu1);
           //Opcion de cargar igual que en el constructor
 	   cargarp.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    //Variables que se pasan al constructor
                   int n,m,nomines,newmines,row,column,mostrartiempo;
                double starttime;
                int[][] perm,guesses,mines;
                String nivel,tmp;
                JButton [][] b;
                boolean found;
                   ObjectInputStream oos = null;
                try {
                    //Apertura de fichero
                   oos = new ObjectInputStream(new FileInputStream("partida.txt"));
                    
                    //Lectura por orden de los ficheros
                    n= oos.readInt();
                    m=oos.readInt();
                    nomines=oos.readInt();
                    nivel = (String)oos.readObject();
                    newmines=oos.readInt();
                    perm=(int[][]) oos.readObject();
                    tmp= (String) oos.readObject();
                    found= oos.readBoolean();
                    row= oos.readInt();
                    column= oos.readInt();
                    guesses= (int [][]) oos.readObject();
                    mines=(int[][]) oos.readObject();
                    mostrartiempo=oos.readInt();
                    
                    
                    b = new JButton[n][m];

                    for(int i=0;i<n;i++){
                        for(int j=0;j<m;j++){
                            b[i][j] =(new JButton((String) oos.readObject()));//Creacion de los botones uno a uno a partir de los strings del fichero
                           
                        }
                    }
                    principal.dispose();
                    new Buscaminas(n,m,nomines,nivel,newmines,perm,tmp,found,row,column,guesses,b,mines,mostrartiempo);//Llamada al constructor 2 que carga una partida a medias
                    
                   
                    //Excepciones
                    } catch (FileNotFoundException ex) {
                    Logger.getLogger(Buscaminas.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(Buscaminas.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(Buscaminas.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
           
                
            });
           //se añade el menu de la barra 
 	   principal.setJMenuBar(menumb);
 	   menu1.add(nuevoJuego);
           //Opcion de juevo nuevo vuelve a cargar la misma pantalla ya que estamos en la de Opciones que es la principal
 	   nuevoJuego.addActionListener(new ActionListener() {
 	        public void actionPerformed(ActionEvent ev) {
 	            principal.dispose();
 	            Opciones();
 	    }
 	   });
           //Muestra los tiempos de la dificultad principiante guardados en un fichero
 	   recordp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    // Se elimina el frame actual
                    principal.dispose();
                    //Se crea el frame que va a mostrar los tiempos 
                    JFrame f= new JFrame("TIEMPOS PRINCIPIANTE");
                    f.setSize(450, 250);// Se le asigna tamaño
                    int count=1;//Contador que empieza en 0 que se escribira antes de lo que leemos del fichero para que salga en que posicion está el tiempo en el ranking.
                    //se rellenan los datos del frame
                    f.setLocationRelativeTo(null);
                    f.setDefaultCloseOperation(EXIT_ON_CLOSE);
                    f.setLayout(new GridLayout(11,1));
                    //Carga el fichero de niveles principiante
                    File file= new File("principiante.txt");
                    file.createNewFile();//Si el fichero no existe se crea, si ya esta creado no hace nada
                    String textLine;
                    //se lee y se cogen todos los tiempos y se copian enel frame con la ayuda de un JLabel
                    BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file.getAbsoluteFile()), "ISO-8859-1"));
                    while ((textLine = reader.readLine())!= null){
                        JLabel linea= new JLabel(count+"-"+textLine);//Cada linea del fichero se copia en un JLabel nuevo en el frame dando lugar como a un ranking colocados por orden.
                        
                        f.add(linea);//Cada JLabel por orden se coloca en el frame.
                        count++;//Se suma uno para la siguiente vuelta sea el número siguiente.
                    }
                    //se añade un boton de volver que vuelve al inicio, al menú principal
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
           //Se hace exactamente lo mismo que para los tiempos de principiante pero con el fichero que guarda los tiempos intermedios
           recordi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    principal.dispose();
                    JFrame f= new JFrame("TIEMPOS INTERMEDIO");
                    f.setSize(450, 250);
                    int count=1;
                     f.setLocationRelativeTo(null);
                    f.setDefaultCloseOperation(EXIT_ON_CLOSE);
                    f.setLayout(new GridLayout(11,1));
                    //apertura del fichero de tiempos intermedios.Solo cambia esta linea.
                    File file= new File("intermedio.txt");
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
           //Igual que en principiante e intermedio pero con los tiempos de experto.
           recorde.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                  try {
                     principal.dispose();
                     JFrame f= new JFrame("TIEMPOS EXPERTO");
                     f.setSize(450, 250);
                    int count=1;
                     f.setLocationRelativeTo(null);
                    f.setDefaultCloseOperation(EXIT_ON_CLOSE);
                    f.setLayout(new GridLayout(11,1));
                    //apertura de fichero de experto.Solo cambia esta linea, las demas son iguales.
                    File file= new File("experto.txt");
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
           //botones de las opciones de nivel de dificultad
 	   JButton principiante = new JButton("Principiante");
 	   JButton  intermedio = new JButton("Intermedio");
 	   JButton experto = new JButton("Experto");  
 	   JButton   personalizado = new JButton("Personalizado");
 	   // si se pulsa principiante se crea un nuevo Buscaminas con los atributos del nivel principiante
 	   principiante.addActionListener(new ActionListener() {

 			public void actionPerformed(ActionEvent e) {
                            //Se desactivan los demas botones
 				intermedio.setSelected(false);
 				experto.setSelected(false);
 				personalizado.setSelected(false);
                                //Atributos de principiante, 10x10 y 10 minas
 				int n = 10;
 				int m = 10;
 				int	nomines = 10;
 				principal.dispose();//Se quita el frame actual
                               
                                
                             Buscaminas buscaminas = new Buscaminas(n,m,nomines,"principiante");//Buscaminas que crea un nivel principiante
                             
 			}
 	   });
           // si se pulsa intermedio se crea un nuevo Buscaminas con los atributos del nivel intermedio
 	   intermedio.addActionListener(new ActionListener() {

 			public void actionPerformed(ActionEvent e) {
                            //se desactivan los demas botones
 				experto.setSelected(false);
 				principiante.setSelected(false);
 				personalizado.setSelected(false);
                                //Atributos nivel intermedio 16x16 y 40 minas
 				int	n = 16;
 				int	m = 16;
 				int	nomines = 40;
 				principal.dispose();
 				Buscaminas buscaminas = new Buscaminas(n,m,nomines,"intermedio");//Buscaminas que crea un nivel intermedio
                            
 			}
 	   });
           //// si se pulsa experto se crea un nuevo Buscaminas con los atributos del nivel experto
 	   experto.addActionListener(new ActionListener() {

 			public void actionPerformed(ActionEvent e) {
                            //Se desactivan los demas botones
 				intermedio.setSelected(false);
 				principiante.setSelected(false);
 				personalizado.setSelected(false);
                                //Atributos nivel experto 23x23 y 99 minas.
 				int	n = 23;
 				int	m = 23;
 				int	nomines = 99;
 				principal.dispose();
 				Buscaminas buscaminas = new Buscaminas(n,m,nomines,"experto");//Buscaminas que crea un nivel experto
                            
 			}
 	   });
           // si se pulsa Personalizado se crea un nuevo Buscaminas con los atributos del nivel personalizado
 	   personalizado.addActionListener(new ActionListener() {

 			public void actionPerformed(ActionEvent e) {
                            //Se desactivan los demas botones y se quita el frame actual
 				intermedio.setSelected(false);
 				principiante.setSelected(false);
 				experto.setSelected(false);
                                principal.dispose();
                                 //Se crea uno nuevo para ingresar los parametros
 				JFrame marco = new JFrame("Personalizado");
                                //Se establece el frame
                                marco.setDefaultCloseOperation(EXIT_ON_CLOSE);
                                 marco.setSize(450, 250);
 				marco.setLayout(new GridLayout(8,4));
                                 marco.setLocationRelativeTo(null);
 				
 				
 				   //al pulsar personalizado primero se crea otro Frame en el que se permiten ingresar las medidas y las minas
 				JLabel titulo = new JLabel("Inserta las medidas(Solo se permiten filas y columnas iguales): ");
 				marco.add(titulo);
 				
 				//Label para ingresar parametro n y m.
 				JLabel etN = new JLabel("columnas/filas:");
 				etN.setSize(10,10);
 				marco.add(etN);
 				JTextField inN = new JTextField();
 				inN.setBounds(50,135,50,25);
 				marco.add(inN);
 				//Label para ingresar parametro minas.
 				JLabel etMi = new JLabel("minas:");
 				etMi.setSize(10,10);
 				marco.add(etMi);
 				JTextField inMin = new JTextField();
 				inMin.setBounds(200,135,50,25);
 				marco.add(inMin);
 				//Boton de aceptar
 				JButton boton = new JButton("Aceptar");
 				//boton.setSize(500, 50);

 				marco.add(boton);
                                
                                marco.setVisible(true);
                                //marco.pack();
 				boton.addActionListener(new ActionListener() {//Cuando se pulsa aceptar se cogen los numeros y se elimina el frame y se crea el buscaminas con el nivel personalizado.

 		 			public void actionPerformed(ActionEvent e) {
 		 				int	n = Integer.parseInt(inN.getText());
 		 				int	m = Integer.parseInt(inN.getText());
 		 				int	nomines = Integer.parseInt(inMin.getText());
 		 				marco.dispose();
 		 				
 		 				new Buscaminas(m,n,nomines,"personalizado");//Al darle a aceptar se crea un nuevo Buscaminas con los parámetros introducidos.
 		 				
 		 			}
 		 	   });
 				
 			}
 			});
 	   
    
 	  //se crea el fondo del frame y se añaden los botones de los niveles
 	   principal.setLayout(new GridLayout(4,1));
 	   principal.add(principiante);
 	   principal.add(intermedio);
 	   principal.add(experto);
 	   principal.add(personalizado);
 	   
 	   

        //principal.pack();
        principal.setVisible(true);
    }
        //Codigo original de la funcion actionPerformed
    public void actionPerformed(ActionEvent e){
        found =  false;
        JButton current = (JButton)e.getSource();//Cuando se pulsa un boton 
        for (int y = 0;y<m;y++){
            for (int x = 0;x<n;x++){
                JButton t = b[x][y]; // se busca en la matriz b de los botones
                if(t == current){
                    row=x;column=y; found =true; // y se guardan su columna fila y se pone found a true;
                }
            }//end inner for
        }//end for
        if(!found) {
            System.out.println("didn't find the button, there was an error "); System.exit(-1);// Si no se encuentra donde se ha pinchado se muestra un mensaje de error
        }
        Component temporaryLostComponent = null;
        if (b[row][column].getBackground() == Color.orange){
            
            return;// si se pincha en una marca de mina no se hace nada
        }else if (mines[row+1][column+1] == 1){// si se pincha en donde deberia haber una mina segun la matriz de mines
                timer.cancel();//se para el timer 
                JOptionPane.showMessageDialog(temporaryLostComponent, "You set off a Mine!!!!.");// se muestra un mensaje de que has colisionado con una mina
                System.exit(0);//Se cierra el juego
        } else { //Si no se ha dado a una mina
            tmp = Integer.toString(perm[row][column]);//se coge el numero que hay en esa posicion con la matriz perm
            if (perm[row][column] == 0){//Si es un cero se pone " " que significa casilla en blanco, no hay minas alrededor
                    tmp = " ";
            }
            b[row][column].setText(tmp);//En la matriz de los botones se rellena el boton con el String que tenia tmp que es el qe tenia la matriz perm
            b[row][column].setEnabled(false);// se desactiva el boton
            try {
                checkifend();
            } catch (IOException ex) {
                Logger.getLogger(Buscaminas.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (perm[row][column] == 0){//Si es un cero significa que es un boton sin minas alrededor 
                scan(row, column);//Se hace scan de esa posicion para desbloquear mas botones libres
                try {
                    checkifend();//Cuando se abre una casilla se llama a la funcion checkifend que comprueba si se ha acababado el buscaminas.
                } catch (IOException ex) {
                    Logger.getLogger(Buscaminas.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
 //Codigo de la funcion checkifend original con alguna modificación
    public void checkifend() throws IOException{
        int check= 0;
        for (int y = 0; y<m;y++){
            for (int x = 0;x<n;x++){
        if (b[x][y].isEnabled()){
            check++;//se cuentan los botones que se pueden pulsar, los unicos que quedan sin abrir
        }
            }}
        if (check == nomines){// Si es igual al total de minas, se gana la partida y se muestran los mensajes de victoria
            timer.cancel();
           
            Component temporaryLostComponent = null;
            //Al acabar, si el usuario no esta en personalizado se comprueba si ha superado un record, si lo supera se le da la opción de poder guardar el tiempo con un nombre o no.
            if(!nivel.equals("personalizado")){
            if (checkFichero()){ // esta funcion comprueba si un tiempo es nuevo record
                //Mensaje que permite al usuario guardar su tiempo si está entre los mejores
              String name =JOptionPane.showInputDialog(temporaryLostComponent, "Congratulations you won!!! It took you "+mostrartiempo+" seconds!\n You got a new record in this level. Insert a player name if you want to save your time, press cancel if you dont want to save it.\n");
              if(name!=null){
                while (name.contains(" ")){ // si se ingresa un nombre con espacios se deniega y se le pide de nuevo al usuario indicandole qe no puede meter espacios en blanco en el nombre
                    name=JOptionPane.showInputDialog(temporaryLostComponent, "Congratulations you won!!! It took you "+mostrartiempo+" seconds!\n You got a new record in this level. Insert a player name if you want to save your time, press cancel if you dont want to save it.\n(Whitespace are forbbiden)");
                    if(name==null)
                        break;
                }
                if(name!=null)
                addPlayerTime(name);//una vez introducido el nombre se guarda en los ficheros con esta funcion
              }
            }
            //Estos elses son por si se juega en personalizado que no se puede ingresar nombre o por si no se supera record de tiempo.
            else{
                
                JOptionPane.showMessageDialog(temporaryLostComponent, "Congratulations you won!!! It took you "+mostrartiempo+" seconds!");
            }
            }else{
                 JOptionPane.showMessageDialog(temporaryLostComponent, "Congratulations you won!!! It took you "+mostrartiempo+" seconds!");
            }
        }
    }
 //Funcion scan original que desbloquea las celdas proximas libres sin minas.
    public void scan(int x, int y){
        for (int a = 0;a<8;a++){ // para cada posicion alrededor se usa los deltay y deltax
            if (mines[x+1+deltax[a]][y+1+deltay[a]] == 3){//Si es un 3 es un perimetro por lo tanto no se hace nada
 
            } else if ((perm[x+deltax[a]][y+deltay[a]] == 0) && (mines[x+1+deltax[a]][y+1+deltay[a]] == 0) && (guesses[x+deltax[a]+1][y+deltay[a]+1] == 0)){//Si es un cero es decir esta vacio se desbloquea y se llama a scan de esa posicion de forma recursivas
                if (b[x+deltax[a]][y+deltay[a]].isEnabled()){
                    b[x+deltax[a]][y+deltay[a]].setText(" ");
                    b[x+deltax[a]][y+deltay[a]].setEnabled(false);
                    scan(x+deltax[a], y+deltay[a]);
                }
            } else if ((perm[x+deltax[a]][y+deltay[a]] != 0) && (mines[x+1+deltax[a]][y+1+deltay[a]] == 0)  && (guesses[x+deltax[a]+1][y+deltay[a]+1] == 0)){//Si es un numero significa que limita con una mina, por lo tanto se desbloquea y se marca la casilla con el numero de minas que tiene y no se llama mas a scan.
                tmp = new Integer(perm[x+deltax[a]][y+deltay[a]]).toString();
                b[x+deltax[a]][y+deltay[a]].setText(Integer.toString(perm[x+deltax[a]][y+deltay[a]]));
                b[x+deltax[a]][y+deltay[a]].setEnabled(false);
            }
        }
    }
 //Funcion perimcheck original cuenta las minas alrededor de una casilla
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
 //Funcion mousePressed original con algunas modificaciones 
    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3) { //Lee un click
            found =  false;
            Object current = e.getSource();
            for (int y = 0;y<m;y++){
                    for (int x = 0;x<n;x++){
                            JButton t = b[x][y]; //detecta que casilla se ha clickado
                            if(t == current){
                                    row=x;column=y; found =true; //Guarda su columna y fila
                            }
                    }//end inner for
            }//end for
            if(!found) {
                System.out.println("didn't find the button, there was an error "); System.exit(-1); // si found es false se devuelve un error de click
            }
            if ((guesses[row+1][column+1] == 0) && (b[row][column].isEnabled())){//Si la posicion esta enabled y es un 0 en guesses se marca con una X de mina marcada,se colorea de naranja y se pone un 1 en guesses de mina marcada.
                b[row][column].setText("x");
                guesses[row+1][column+1] = 1;
                b[row][column].setBackground(Color.orange);
                //Al marcar una mina se actualiza la etiqueta que muestra las minas , quitando una.
                newmines--;
                minas.setText("Minas:"+newmines+" ");
                
            } else if (guesses[row+1][column+1] == 1){//Si se pincha en una mina marcada se quita la X y se pone de nuevo una interrogacion devolviendo un 0 a la matriz guesses
                b[row][column].setText("?");
                guesses[row+1][column+1] = 0;
                b[row][column].setBackground(null);
                //Al quitar la marca de una mina, se actualiza la etiqueta que muestra el número restando sumando una.
                newmines++;
                minas.setText("Minas:"+newmines+" ");
            }
        }
    }
 
    @Override
    public void mouseReleased(MouseEvent arg0) {
        // TODO Auto-generated method stub
    }
    //Funcion checkFichero que comprueba que el tiempo que se acaba de lograr es record o no.
    private boolean checkFichero() throws IOException {
         File f;
        
        ArrayList<String> tiempos = new ArrayList<>();
        String textLine;
        //Según la varable nivel se sabe que nivel se está jugando
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
        //lectura de todos los tiempos
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f.getAbsoluteFile()), "ISO-8859-1"));
        while ((textLine = reader.readLine())!= null){
        tiempos.add(textLine);// se añaden a un array para comprobar si se supera un record
        }
        // se realizan las comprobaciones de tiempo según todos los casos posibles
        if(tiempos.size()<10){//Si hay menos de 10 elementos se devuelve true
            return true;
        }else {
            String tiempo = tiempos.get(9); // si hay 10 se coge el ultimo elemento
            String[] split = tiempo.split(" "); // se separa el string por espacios
            int n = Integer.parseInt(split[1]);//se coge el elemento 1 que representa el tiempo ya que no se permiten meter espacios
            if(mostrartiempo<n)//Si el tiempo es menor del que esta metido, se devuelve true
                return true;
        }
        return false;//Si se llega aqui se devuelve false
    }

    private void addPlayerTime(String name) throws IOException { // En caso de que se supere un record y el usuario inserte un nombre se añade
        
        ArrayList<String> tiempos = new ArrayList<>();
        String textLine;
        PrintWriter writer; // se abre printwriter para poder escribir un nuevo tiempo
       File f;
       //se abre el fichero correspondiente segun la variable nivel
         if(nivel.equals("principiante")){
             f= new File("principiante.txt");
            
        }else if(nivel.equals("intermedio")){
            f= new File("intermedio.txt");
            
        }else{
            f= new File("experto.txt");
            
        }
        
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f.getAbsoluteFile()), "ISO-8859-1"));
        while ((textLine = reader.readLine())!= null){
          
        tiempos.add(textLine);//se cogen los tiempos
        }
        //se comprueban uno a uno hasta averiguar la posicion en la que va el nuevo tiempo
        for (int i=0; i<=tiempos.size();i++){
            if(i==10) //Si se llega a la ultima posicion se hace break 
                break;
            if(i==tiempos.size()){
                tiempos.add(name+" "+mostrartiempo+" segundos"); //Si no hay 10 elementos y se llega al final se añade al final .
                break;
            }
            //Comprobacion uno a uno del tiempo que estaba insertado.
             String tiempo = tiempos.get(i); //se coge el tiempo
             String[] split = tiempo.split(" ");// se separa por espacios
             int n = Integer.parseInt(split[1]); //se coge el elemento 1 que corresponde al tiempo 
                if(mostrartiempo<n){ //si el tiempo es menor significa que esta es su posicion
                    tiempos.add(i,name+" "+mostrartiempo+" segundos"); // se añade en esta posicion
                    if(tiempos.size()==11) // si al añadir se supera el maximo de 10 elementos 
                        tiempos.remove(10);//  se borra el elemento que se queda como ultimo
                    break;
                 }
        }
        //Se vuelven a escribir los tiempos por orden y solo los 10 resultantes o los que haya si hay menos
         if(nivel.equals("principiante")){
            writer =new PrintWriter("principiante.txt", "UTF-8");
            
        }else if(nivel.equals("intermedio")){
             writer =new PrintWriter("intermedio.txt", "UTF-8");
            
        }else{
            writer =new PrintWriter("experto.txt", "UTF-8");
            
        }
         
        for(String s:tiempos){
        writer.println(s);//se escriben línea a linea.
       
        }
        writer.close();
    }
}//end class
