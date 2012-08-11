package ws.raidrush.alex2.jabberbot.commands;

// import java.util.Arrays;

import java.io.BufferedReader;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
// import org.jivesoftware.smack.util.Base64;
import org.jivesoftware.smackx.muc.MultiUserChat;

import com.tecnick.htmlutils.htmlentities.HTMLEntities;

public class Rrsearch extends ACommand
{
  public Rrsearch(MultiUserChat jabberRoom, Message message, String[] parameters) 
  {
    super(jabberRoom, message, parameters);
    // TODO Auto-generated constructor stub
  }

  @Override
  protected void doActions() 
  {
    addMetaInformation("Autor", "murdoc");
    addMetaInformation("Name", getClass().getSimpleName());
    addMetaInformation("Version", "1.0");
    addMetaInformation("Description", "RaidRush Board Search - Search for public stuff - No UG Supprot!");
    
    Logger.getRootLogger().info("RRSearch called");
    
    String from = getMessage().getFrom();
    String nick = from.substring(from.lastIndexOf("/") + 1);
    
    String[] params = getParameters(); //Get the parameters which the user might have supplied

    if (params == null || params.length == 0) { 
      //If the user gave us no parameters
      this.sendToRoom("@" + nick + ": missing search-term");
      return;
    }
    
    this.startSearch(params[0]);
  }
  
  protected void sendToRoom(String msg)
  {
    try {
      getJabberRoom().sendMessage(msg);
    } catch(XMPPException a) {
      System.err.println("Error while sending message :-/"); //Fuck
    }
  }
  
  protected void startSearch(String term)
  {
    //this.sendToRoom("when raidrush is in ashes you have my permission to search for \"" + term + "\"");
        
    try {
      URL                 url  = new URL("http://board.raidrush.ws/search.php?do=process");
      URLConnection       conn = url.openConnection();
      conn.setDoOutput(true);
      
      OutputStreamWriter  wr   = new OutputStreamWriter(conn.getOutputStream());
      
      String data = "s=1&do=process&&sortby=lastpost&order=descending&query=" + URLEncoder.encode(term, "UTF-8"); 
      
      wr.write(data);
      wr.flush();
      
      BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
      
      String line;
      StringBuilder build = new StringBuilder();
      
      while ((line = rd.readLine()) != null)
        build.append(line);
      
      String body = build.toString();
      int start = body.indexOf(" id=\"thread_title_");
      
      if (start == -1) {
        this.sendToRoom("nothing found");
        return;
      }
      
      String res = body.substring(start + 18);
      res = res.substring(0, res.indexOf("\""));
      
      String title = body.substring(start + 18);
      title = title.substring(0, title.indexOf("<"));
      title = title.substring(title.indexOf(">") + 1);
      title = HTMLEntities.unhtmlentities(title);
      
      this.sendToRoom(title + " -> http://board.raidrush.ws/showthread.php?t=" + res);
      
      Logger.getRootLogger().info(res);
      Logger.getRootLogger().info(title);
      
    } catch (Exception e) {
      Logger.getRootLogger().info(e.getMessage());
    }
    
  }
}
