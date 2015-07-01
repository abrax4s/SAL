/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;


import java.io.*;
import static java.lang.ProcessBuilder.Redirect;
import static java.lang.System.out;
import java.net.*;
import java.nio.charset.Charset;
import java.util.*;

/**
 * 
 * @author Sammy Guergachi <sguergachi at gmail.com>
 */
public class Monitor extends TimerTask{
    
    String ID_Usr,nameEquipo;
    Control control = new Control();
    InetAddress local;
    
    public Monitor(String usr){
        try{
            this.local = InetAddress.getLocalHost();
            this.nameEquipo = local.getHostName();
        }catch(UnknownHostException e){
            System.out.println(e.toString());
        }
        this.ID_Usr = usr;
        launcher();
    }
    
    public void launcher(){
        System.out.println("Se activa launcher");
        Timer timer= new Timer();
        timer.schedule(new Monitor(ID_Usr), 0,500);
        //System.out.println("Se activa timer");
        run();
        
    }
    private ProcessBuilder pb = new ProcessBuilder("tasklist","/FO","CSV","/FI","SESSIONNAME ne Services","/NH");
    private Set<String> base = execute(pb);
    @Override
    public void run() {
        out.println("monitor está entrando");
        Set<String> result  = execute(pb);
        Set<String> started = diff(base, result);
        print("started", started);
        base.addAll(started);
        /*Set<String> stopped = diff(result, base);
        print("stopped", stopped);
        base.removeAll(stopped);*/
    }
    
    private final Set<String> execute( ProcessBuilder pb ) {
        try {
            // execute.. 
            Process p      = pb.start();
            InputStream in = p.getInputStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            // copy output
            byte[] buffer = new byte[2048];
            long count = 0;
            int n = 0;
            while ( ( n = in.read(buffer)) != -1 ) {
                baos.write(buffer, 0, n );
                count += n;
            }
            p.destroy();
            // create a list from that output.
            return processesFrom( baos.toString("UTF-8") );
        } catch ( IOException ioe ) {/*in real world you don't just ignore exceptions.. but meh.*/}
        return null;// nor return null's
    }
    
    private final Set<String> processesFrom(String p) {
        Set<String> set = new TreeSet<String>();
        for( String line : p.split("\n") ) {
            set.add( line.split(",")[0] );
        }
        return set;
    }
    
     private Set<String> diff( Set<String> base, Set<String> delta) {
        TreeSet<String> newSet = new TreeSet<>(delta);
        newSet.removeAll( base );
        return newSet;
    }
    // Prints the given set ie: "gvim.exe started at xxxx"
    private void print(String label, Set<String> set ) {
        for(String e : set ) {
            out.printf("%s, %s at: %s%n", e, label, new Date());
            //out.println(label +":"+ e + "| user: "+ID_Usr+"| equipo: "+nameEquipo);
        }
    }    
}
