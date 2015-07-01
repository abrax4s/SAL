package Controller;

import Model.ConexionDB;
import View.ConsolaAdmin;
import View.GUI_Usr;
import View.Usr_Form;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.sql.*;
import javax.swing.JOptionPane;

public class Control {
    //--------------------------------- DECLARACION DE VARIABLES -----------------------------------
    ConexionDB DB = new ConexionDB();
   // Usr_Form Session = new Usr_Form();
    String SQL="";
    Statement stmt=null;
    ResultSet rs = null;
    CallableStatement cstmt=null;
    String retorna="";
    
    //------------------------------------MÉTODOS PARA REGISTRO DE USUARIO---------------------------
    
    public String SignInCheck(String matricula, String name,String ap_P,String ap_M,String pass,String checkpass){
        //Este método se asegura que el usuario NO exista para poderlo registrar
        DB.abrirCon();
        if(pass.equals(checkpass)){
            SQL="select Matricula from Usuario";
            try{
                stmt = DB.con.createStatement();
                rs=stmt.executeQuery(SQL);
                
                rs.next();
                System.out.println("Según esta es la matrícula: " +rs.getString("Matricula"));
                String checkMat=FirstDecrypt(rs.getString("Matricula"));
                System.out.println("según esta es la matrícula encriptada: "+checkMat);
                if(matricula.equals(checkMat)) {
                    return("Error, el usuario ya está registrado");
                }else{
                    retorna=RegistrarUsr(matricula,name,ap_P,ap_M,pass);
                    return retorna;
                }
                
            }catch(SQLException e){
                e.printStackTrace();
                return("Error en check: " +e.toString());
                
            }
            
        }else{
            return retorna="es este.";
        }
    }// fin SignInCheck
    
    public String RegistrarUsr(String Matricula, String Nombre, String A_Paterno, String A_Materno, String Pass){
        //este método se encarga de registrar un nuevoo usuario
        DB.abrirCon();
        String Tipo = FirstCrypt("2");
        Matricula = FirstCrypt(Matricula);
        Nombre = FirstCrypt(Nombre);
        A_Paterno = FirstCrypt(A_Paterno);
        A_Materno = FirstCrypt(A_Materno);
        Pass = FirstCrypt(Pass);
        SQL="EXEC Regis_Usr '"+ Matricula+"', '"+Nombre+"', '"+A_Paterno+"', '"+A_Materno+"', '"+Pass+"','"+Tipo+"'";
        
      
            try{
                cstmt = DB.con.prepareCall(SQL);
                cstmt.execute();
                
                return("Operación exitosa");
                
            }catch(SQLException e){
              return("SQL Exception en Registrar: "+ e.toString());
                
            }
        
    }// fin RegistrarUsr

    //----------------------------------------- FIN DE REGISTRO DE USUARIO
    //----------------------------------------------------------- MÉTODOS PARA MANTENIMIENTO
    
    public void MantainanceCleanTemp(){
        //este método llama a CLEANMGR.exe para limpiar archivos basura del equipo
        String[] PROMPT = {"cmd.exe", "/K", "CLEANMGR," ,"/C"};
        
        try {
            // Execute a command with an argument
            
            Process child = Runtime.getRuntime().exec(PROMPT);
            JOptionPane.showMessageDialog(new ConsolaAdmin(),"Operación exitosa");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(new ConsolaAdmin(),"Error: "+e.toString());          
        }
        
    }//fin MantainanceCleanTemp

    //-------------------------------------------------FIN DE MATENIMIENTO
    //--------------------------------------------MÉTODOS PARA ADMINISTRAR LABORATORIOS
    
    public void RegistrarCentro(String name, String place){
        //este método registra un nuevo centro de cómputo
        DB.abrirCon();
        
        SQL="EXEC Regis_CC '"+name +"', '" +place+"'";
        
            try{
                cstmt = DB.con.prepareCall(SQL);
                cstmt.execute();
                JOptionPane.showMessageDialog(new ConsolaAdmin(),"Operación exitosa");
            }catch(SQLException e){
                JOptionPane.showMessageDialog(new ConsolaAdmin(),"SQL Exception: "+ e.toString());
            }
    }//fin RegistrarCentro
    
    public String[] ComboFill1(String Columna,String Tabla){
        //este método llena algunos comboBox de la vista de administración de laboratorios
        String[] array;
        int lenght=0;
        ResultSet rs = null;
        Statement stmt = null;
        int i =1;
        DB.abrirCon();
        SQL="Select count("+Columna+") as 'numero' from "+Tabla;
        try{
            stmt = DB.con.createStatement();
            rs=stmt.executeQuery(SQL);
            rs.next();
            lenght = Integer.parseInt(rs.getString("numero"));
            lenght++;
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null,"SQL Exception: "+e.toString());
        }
        SQL="Select "+Columna+" from "+Tabla;
        array= new String[lenght];
        try{
            rs=stmt.executeQuery(SQL);
            //rs.next();
            array[0]=null;
            while(i<=array.length&&rs.next()){
                array[i]=rs.getString(Columna);
                //rs.next();
                i++;
            }
            
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null,"SQL Exception: "+ e.toString());
            array=new String[0];
        }
        return array;
        
    }//fin ComboFill1
    
    public String[] ComboFill2(String Columna1, String Columna2, String value, String Tabla){
        //este método llena algunas ComboBox de la vista de adminlabs
        String[] array;
        int lenght=0;
        ResultSet rs = null;
        Statement stmt = null;
        int i =1;
        DB.abrirCon();
        SQL="Select count("+Columna1+") as 'numero' from "+Tabla;
        try{
            stmt = DB.con.createStatement();
            rs=stmt.executeQuery(SQL);
            rs.next();
            lenght = Integer.parseInt(rs.getString("numero"));
            lenght++;
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null,"SQL Exception: "+e.toString());
        }
        SQL="Select "+Columna1+" from "+Tabla+" where "+Columna2+" = "+"'"+value+"'";
        array= new String[lenght];
        try{
            rs=stmt.executeQuery(SQL);
            //rs.next();
            array[0]=null;
            while(i<=array.length&&rs.next()){
                array[i]=rs.getString(Columna1);
                //rs.next();
                i++;
            }
            
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null,"SQL Exception: "+ e.toString());
            array=new String[0];
        }
        return array;
    }//fin ComboFill2
    
    public String[] StatusComboFill(){
        String [] array;
        int lenght = 3;
        array = new String[lenght];
        array[0]=null;
        array[1]="En servicio";
        array[2]="En  reparación";
        
        return array;
    }//fin StatusComboFill
    
    //------------------------------------------------ FIN DE ADMINLABS
    //--------------------------------------------------MÉTODOS PARA ADMINISTRAR USUARIOS
    
     public void UsrCheck(String matricula, String name,String ap_P,String ap_M,String pass,String checkpass){
        //Este metodo garantiza que el usuario exista para poderlo actualizar
        DB.abrirCon();
        if(pass.equals(checkpass)){
            SQL="select Matricula from Usuario where Matricula ="+ "'"+matricula+"'";
            try{
                stmt = DB.con.createStatement();
                rs=stmt.executeQuery(SQL);
                
                rs.next();

                if(matricula.equals(rs.getString("Matricula"))) {
                    updateUsr(matricula,name,ap_P,ap_M,pass);
                    
                }else{
                    JOptionPane.showMessageDialog(new ConsolaAdmin(),"Error, el usuario no está registrado");
                }
                
            }catch(SQLException e){
                JOptionPane.showMessageDialog(new ConsolaAdmin(),"Error en check: " +  e.toString());
            }
            
        }
    }//fin UsrCheck
     
    public void updateUsr(String matricula, String nombre, String A_Pat, String A_Mat, String pass){
        //este método actualiza datos del usuario
        DB.abrirCon();
        SQL="EXEC Update_User '"+matricula+"','"+nombre+"','"+A_Pat+"','"+A_Mat+"','"+pass+"'";
        try{
            cstmt = DB.con.prepareCall(SQL);
            cstmt.execute();
            JOptionPane.showMessageDialog(new ConsolaAdmin(),"Operación exitosa");
        }catch(SQLException e){
            JOptionPane.showMessageDialog(new ConsolaAdmin(),"SQL Exception: "+e.toString());
        }
    }// fin updateUsr
    
    //--------------------------------------------------FIN ADMINUSR
    //-------------------------------------------------MÉTODOS PARA ADMIN EQUIPOS
    
    public void RegistrarEquipo(String name,String Lab, String status){
        //este método registra nuevos equipos
        DB.abrirCon();
        SQL="EXEC Regis_Equipo '"+name+"','"+Lab+"','"+status+"'";
        try{
            cstmt = DB.con.prepareCall(SQL);
            cstmt.execute();
            JOptionPane.showMessageDialog(new ConsolaAdmin(),"Operación exitosa");

        }catch(SQLException e){
            JOptionPane.showMessageDialog(new ConsolaAdmin(),"SQL Exception: "+ e.toString());
        }

    }//finRegistrarEquipos
    
    public boolean EliminarEquipo(String name){
        //este método elimina el equió indicado por el Administrador
        DB.abrirCon();
        SQL = "EXEC Drop_Equipo '"+name+"'";
        try{
            cstmt = DB.con.prepareCall(SQL);
            cstmt.execute();
            JOptionPane.showMessageDialog(new ConsolaAdmin(),"Operación exitosa");
            return true;
        }catch(SQLException e){
            JOptionPane.showMessageDialog(new ConsolaAdmin(),"SQL Exception: "+ e.toString());
            return false;
        }
    }//fin EliminarEquipo
    
    //-------------------------------------------------FIN ADMINEQUIPOS
    //-------------------------------------------------MÉTODOS PARA INICIAR SESIÓN
    
    public String IniciarSesion(String usr, String pass){
        //este método realiza el inicio de sesión del usuario
        String matricula ="",contrasena="";
        DB.abrirCon();
        //pass=Encriptar.encrypt(pass);
        usr = FirstCrypt(usr);
        pass = FirstCrypt(pass);
        SQL="Select * from Usuario where Matricula = '"+usr+"'";
            try{
                stmt = DB.con.createStatement();
                rs=stmt.executeQuery(SQL);
                rs.next();
                matricula = rs.getString("Matricula");
                contrasena = rs.getString("Contraseña");
                
                if(matricula.equals(usr)&&contrasena.equals(pass)){
                    return rs.getString("Tipo");
                }else{
                    return null;
                }
            }catch(SQLException e){
                System.out.println(e.toString());
                return null;
                //return e.toString();
            }
        
    }//fin IniciarSesion
    
    public void CerrarSesionUsuario(){
        new Usr_Form().dispose();
        new GUI_Usr().lanza();
    }
    
    //---------------------------------------------------------------FIN INICIAR SESION
    //--------------------------------------------------------------MÉTODOS DE ENCRIPTACIÓN Y DESENCRIPTACIÓN DE ARCHIVOS
    
    public String FirstCrypt(String cadena){
        //Primer nivel de encriptación, octal
        String cadenaEnc="";
        int valor=0;
        try{
                for(int i=0;i<cadena.length();i++){
                        valor=cadena.codePointAt(i);
                        if(valor<100){
                                cadenaEnc= cadenaEnc+"0"+String.valueOf(valor);
                        }else{
                        cadenaEnc+=String.valueOf(valor);
                        }
                }
                cadenaEnc = SecondCrypt(cadenaEnc);
        }catch(Exception e){
                cadenaEnc = "ERROR! por favor ingrese un valor";
        }

        return cadenaEnc;
    }//fin FirstCrypt()
    
    public String SecondCrypt(String cadena){
        //Segundo nivel de encriptación, hexadecimal
        String hexEnc="", hex="";
        int indexA=0,indexB=0,sub=0;
        try{
                for(int i=0;i<cadena.length();){
                        indexB=indexA+2;
                        sub=Integer.parseInt(cadena.substring(indexA,indexB+1));
                        hex=Integer.toHexString(sub);
                        hexEnc+=hex;
                        indexA=indexB+1;
                        if(indexB==cadena.length()-1){
                                break;
                        }else{
                                i++;
                        }
                }
        }catch(NumberFormatException e){
                hexEnc="ERROR! por favor, ingrese valor";
        }
        return hexEnc;
    }//fin SecondCrypt
    
    public String FirstDecrypt(String cadena){
        //primer nivel de decriptación, hexadecimal para compaginar con el segundo nivel de encriptación
        String hexDec="";
        int num=0,indexA=0,indexB=0;
        try{
                for(int i=0;i<cadena.length();){
                        indexB=indexA+1;
                        num= (int) Long.parseLong(String.valueOf(cadena.substring(indexA, indexB+1)),16);
                        if(num<100){
                                hexDec=hexDec+"0"+String.valueOf(num);
                        }else{
                                hexDec+=String.valueOf(num);
                        }
                        indexA=indexB+1;
                        if(indexB==cadena.length()-1){
                                break;
                        }else{
                                i++;
                        }
                }
                hexDec=SecondDecrypt(hexDec);
        }catch(NumberFormatException e){
                hexDec="ERROR! por favor, proporcione un valor válido";
        }

        return hexDec;
    }//finFirstDecrypt
    
    public String SecondDecrypt(String cadena){
        //segundo nivel de decriptación, octal para empatarse con el primer nivel de encriptación
        String cadenaDec="";
        int sub=0;
        int indiceA=0, indiceB=0;
        try{
                for(int i=0;i<cadena.length();){
                        indiceB=indiceA+2;
                        sub=Integer.parseInt(cadena.substring(indiceA,indiceB+1));
                        cadenaDec+=(char)(sub);
                        indiceA=indiceB+1;
                        if(indiceB==cadena.length()-1){
                                break;
                        }else{
                                i++;
                        }
                }
        }catch(NumberFormatException e){
                cadenaDec="ERROR! Por favor, ingrese un valor válido";
        }
        return cadenaDec;
    }// fin SecondDecrypt
    

    //------------------------------------------------------------ FIN DE ENCRIPTACIÓN
    
 


//------------------------------------actionListeners para ConsolaAdmin()-------------------------------
    public void TPLActionPerformed(ActionEvent e){
        
    }
}
