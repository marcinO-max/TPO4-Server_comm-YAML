/**
 *
 *  @author Ossowski Marcin S16425
 *
 */

package zad1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;





public class Client {
	
		String id;
		SocketChannel channel;
		PrintWriter out = null;
		BufferedReader in = null;
		String host;
		int port;
		private static Matcher matchCode =
                Pattern.compile("(\\n250 ok)|(552 no match)").matcher("");
	

	public Client(String host, int port, String id) {
		this.id=id;
		this.port=port;
		this.host=host;	
		connect();
	}
	
	

	public String getId() {
		return id;
	}



	public void setId(String id) {
		this.id = id;
	}



	public String getHost() {
		return host;
	}



	public void setHost(String host) {
		this.host = host;
	}



	public int getPort() {
		return port;
	}



	public void setPort(int port) {
		this.port = port;
	}



	public void connect(){
		try {
	        channel = SocketChannel.open();
	        channel.configureBlocking(false);
	        channel.connect(new InetSocketAddress(host, port));
	       while(!channel.finishConnect()) {
	    	   Thread.sleep(1000);
	       }
	     } catch(UnknownHostException exc) {
	         System.err.println("Uknown host " + host);
	     } catch(Exception exc) {
	         exc.printStackTrace();
	   
	     }
		
		
		
	}

//	public String send(String request) {
//		 StringBuilder response = new StringBuilder();
//		try {
//		ByteBuffer inBuf = StandardCharsets.UTF_8.encode(request);
//		channel.write(inBuf);
//		inBuf.clear();
//		  while (true) {      
//
//		      inBuf.clear();                                        
//		      
//		      int readBytes = channel.read(inBuf);     
//
//		      if (readBytes == 0) {     
//		    	  Thread.sleep(1000);
//		    	  readBytes = channel.read(inBuf);
//		         continue;
//		      }
//		      else if (readBytes == -1) {  
//		    	  channel.close();
//		      }
//		      else {                                                   
//		          inBuf.flip();
//		          CharBuffer charBuffer = StandardCharsets.UTF_8.decode(inBuf);
//		          response.append(charBuffer);
//		          inBuf.clear();
//		          readBytes = channel.read(inBuf);
//		         
//		      }		     
//		   }		 
//		}catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		}
//		return response.toString();
//	}
	public String send(String request) {
	StringBuilder response = new StringBuilder();
	try {
	    ByteBuffer buffer = StandardCharsets.UTF_8.encode(request);
	    channel.write(buffer);
	    buffer.clear();
	    int bytesRead = channel.read(buffer);
	    while (bytesRead == 0) {
	        Thread.sleep(10);
	        bytesRead = channel.read(buffer);
	    }
	    while (bytesRead != 0) {
	        buffer.flip();
	        CharBuffer charBuffer = StandardCharsets.UTF_8.decode(buffer);
	        response.append(charBuffer);
	        buffer.clear();
	        bytesRead = channel.read(buffer);
	    }
	} catch (IOException | InterruptedException e) {
	    e.printStackTrace();
	}
	return response.toString();

}
}
