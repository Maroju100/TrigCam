/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package server;

//import com.pi4j.io.gpio.GpioController;
//import com.pi4j.io.gpio.GpioFactory;
//import com.pi4j.io.gpio.GpioPinDigitalOutput;
//import com.pi4j.io.gpio.PinState;
//import com.pi4j.io.gpio.RaspiPin;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;
import java.io.BufferedWriter;
import java.io.FileWriter;
import  java.io.PrintWriter;
import java.util.*;

public class Server {

    private ServerSocket server;
//    private GpioPinDigitalOutput led1;
//    private GpioPinDigitalOutput led2;
//    private GpioPinDigitalOutput led3;
//    final GpioController gpio = GpioFactory.getInstance();
private int flag=1;
    public Server() {

//        led1 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00, PinState.LOW);
//        led2 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, PinState.LOW);
//        led3 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02, PinState.LOW);
    }

    public static void main(String[] args) {

        Server server = new Server();
        server.runServer();
    }

    public void runServer() {

        try {
            server = new ServerSocket(9091);
            System.out.println("Server is running");
            while (true) {
                new Controller(server.accept()).start();
                System.out.println("hello client");
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

    }

       private class Controller extends Thread {

        private Socket socket;
        private ObjectInputStream input;
        private ObjectOutputStream output;
        private String in;

        public Controller(Socket socket) throws IOException {
            this.socket = socket;
            System.out.println("New client at " + socket.getRemoteSocketAddress());
            

if (flag==1)
{
    flag=0;
    
    //  time=System.currentTimeMillis() - startTime;
//String command= "python /home/ms/q/time.py"; 
//Runtime rt = Runtime.getRuntime();     
//Process pr = rt.exec(command);
    

//Process p = Runtime.getRuntime().exec("python /home/ms/q/time.py");
//Process p = Runtime.getRuntime().exec("sh /home/pi/PiScripts/Recordvideo.sh");
Process p = Runtime.getRuntime().exec("sh /home/pi/ProximityTrig.sh");
    System.out.println("Proximity sensing  started!!!");

FileWriter fileWriter=null;
BufferedWriter bufferedWriter= null;
PrintWriter printWriter = null;

try{
fileWriter=new FileWriter("TriggerTimes",true);
bufferedWriter= new BufferedWriter(fileWriter);
printWriter=new PrintWriter(bufferedWriter);
int day,month,year;
int second,minute,hour;
GregorianCalendar date= new GregorianCalendar();
day=date.get(Calendar.DAY_OF_MONTH);
month=date.get(Calendar.MONTH);
year=date.get(Calendar.YEAR);
second=date.get(Calendar.SECOND);
minute=date.get(Calendar.MINUTE);
hour=date.get(Calendar.HOUR);
printWriter.println(""+day+"/"+(month+1)+"/"+year+"-"+hour+":"+minute+":"+second);
}
catch(IOException e)
{e.printStackTrace();
}
finally{
try{
printWriter.close();
bufferedWriter.close();
fileWriter.close();
}
catch(IOException e)
{
e.printStackTrace();
}
}


 Timer timer= new Timer();
timer.schedule(new TimerTask(){
    public void run(){
        System.out.println("flag it put to 1 again");
flag =1;        
    }
    },21000);

  }
}

        @Override
        public void run() {
            try {
                getStreams();
                output.writeObject("Hello, Welocme to Raspberry PI");
                output.flush();
                while (!(in = (String) input.readObject()).equals("close")) {
                    if (!in.equals("end")) {//end here as a marker for ending of stream
                        //System.out.println("input   " + in);
                        switch (in) {
                            case "10"://led1 off
//                                led1.low();
                                break;
                            case "11"://led1 on
//                                led1.high();
                                break;
                            case "20":
//                                led2.low();
                                break;
                            case "21":
//                                led2.high();
                                break;
                            case "30":
//                                led3.low();
                                break;
                            case "31":
//                                led3.high();
                                break;
                        }
                        output.writeObject("command"+in.toUpperCase());
                    }else{
                    
                    }
                }//end of first while
            } catch (IOException e) {//catch output object exception
                e.printStackTrace();
                System.out.println("Error handling client @ " + socket.getRemoteSocketAddress() + ": " + e.getMessage());
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            } finally {
                closeConnection();
                System.out.println("Connection with client @ " + socket.getRemoteSocketAddress() + " closed");
            }
        }//end of run

        private void getStreams() throws IOException {
            output = new ObjectOutputStream(socket.getOutputStream());
            output.flush();
            input = new ObjectInputStream(socket.getInputStream());
        }//end of getStreams

        private void closeConnection() {
            try {
                output.close();
                input.close();
                socket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }//end of closeConnection
    }
}
