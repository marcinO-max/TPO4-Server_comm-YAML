/**
 *
 *  @author Ossowski Marcin S16425
 *
 */

package zad1;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.sun.corba.se.impl.oa.poa.ActiveObjectMap.Key;
import com.sun.xml.internal.ws.handler.ClientLogicalHandlerTube;
import com.sun.xml.internal.ws.handler.ServerLogicalHandlerTube;


public class Server {

	private String host;
	private int port;
	private ServerSocketChannel serverSocketChannel = null;
	private Thread thread;
	private Selector selector= null;
	StringBuilder serverLog;
	ByteBuffer bbuf;
	Charset charset = Charset.forName("utf-8");
	Map<SocketChannel, ClientLog> clientLog;
	
	
	
	public Server(String host, int port) {
		// TODO Auto-generated constructor stub
		clientLog = new HashMap<>();
		serverLog = new StringBuilder();
		this.host=host;
		this.port=port;
		bbuf = ByteBuffer.allocate(1024);
		try {
			serverSocketChannel = ServerSocketChannel.open();
			serverSocketChannel.socket().bind(new InetSocketAddress(host,port));
			serverSocketChannel.configureBlocking(false);
			selector = Selector.open();
			serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT, null);
				
		}catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		
	}

	public void startServer() {
		
	
		thread = new Thread(()->{
			
			while(!thread.isInterrupted()) {
				try {
					selector.select();
					Set keys = selector.selectedKeys();
					Iterator iterator = keys.iterator();
					while(iterator.hasNext()) {
						SelectionKey key = (SelectionKey) iterator.next();
						iterator.remove();
						
						if(key.isAcceptable()) {
							SocketChannel cc = serverSocketChannel.accept();
							cc.configureBlocking(false);
							cc.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
							
						}
						
						if (key.isReadable()) {  
				            SocketChannel cc = (SocketChannel) key.channel();
				            ByteBuffer buffer = ByteBuffer.allocate(256);
				            cc.read(buffer);
				            String clientRequest = new String(buffer.array()).trim();
				            String result = serviceRequest(cc,clientRequest);  
				            CharBuffer charBuffer = CharBuffer.wrap(result);
				            ByteBuffer byteBuffer = StandardCharsets.UTF_8.encode(charBuffer);
				            cc.write(byteBuffer);
				          }
						
						
						
					}
				
					
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}
			}
				
		
			
		});
		thread.start();
		
		
		
	}

	private String serviceRequest(SocketChannel clientSocket, String req) {
	    String response = "";
	    if (req.matches("login .+")) {
	        clientLog.put(clientSocket, new ClientLog(req.substring(6)));
	        clientLog.get(clientSocket).fullClientLog += ("=== " + clientLog.get(clientSocket).id + " log start ===\n" + "logged in" + "\n");
	        
	        serverLog.append(clientLog.get(clientSocket).id).append(" logged in at ").append(LocalTime.now()).append("\n");
	        response = "logged in";
	    } else if (req.matches(".*\\d{4}-\\d{2}-\\d{2}.*")) {
	        String[] parts = req.split(" ");
	        clientLog.get(clientSocket).fullClientLog += ("Request: " + req + "\n" + "Result:\n" + Time.passed(parts[0], parts[1]) + "\n");
	        serverLog.append(clientLog.get(clientSocket).id).append(" request at ").append(LocalTime.now())
	                .append(": \"").append(req).append("\"").append("\n");
	        response = Time.passed(parts[0], parts[1]);
	    } else if (req.contains("bye")) {
	        clientLog.get(clientSocket).fullClientLog += ("logged out\n" + "=== " + clientLog.get(clientSocket).id +
	                " log end ===\n");
	        serverLog.append(clientLog.get(clientSocket).id).append(" logged out at ").append(LocalTime.now())
	                .append("\n");
	        if (req.equals("bye and log transfer")) {
	            response = clientLog.get(clientSocket).fullClientLog;
	        } else {
	            response= "logged out";
	        }
	        clientLog.remove(clientSocket);
	        
	    }
	    return response;
	}

	public void stopServer() {
		// TODO Auto-generated method stub
		
		try {
			thread.interrupt();
			Thread.sleep(500);
			selector.close();
			serverSocketChannel.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}

	public String getServerLog() {
		// TODO Auto-generated method stub
		String tab = serverLog.toString();
		return tab;
	}
}
