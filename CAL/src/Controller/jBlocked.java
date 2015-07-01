/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Controller;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
/**
 * @web http://www.jc-mouse.net/
 * @author Mouse
 */
public class jBlocked {
    private JFrame jframe=null;
    ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    /**
 * Constructor de clase
     * @param f
 */
    public jBlocked( JFrame f )
    {
        this.jframe = f;
    }
    /**
 * ejecuta una tarea cada "n" tiempo
 * Para evitar que el usuario utilice las teclas (WINDOWS + D)(TAB) y asi perder el foco
 * de la aplicación, cada 50 milisegundos se envia el JFrame al frente y se cambia su propiedad a maximizado
 */
    public void block()
    {
        
        scheduler.scheduleAtFixedRate( 
            new Runnable() 
            {
                @Override
                public void run() {                   
                    front(); 
                }
              }, 500, 50 , TimeUnit.MILLISECONDS ); //comienza dentro de 1/2 segundo y luego se repite cada N segundos
    }
    public void front()
    {
        jframe.setExtendedState( JFrame.MAXIMIZED_BOTH );//maximizado
        jframe.toFront();
    }
}//--> fin