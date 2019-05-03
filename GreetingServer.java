//compile::::	javac -cp json-simple-1.1.1.jar:mysql-connector-java.jar PicitMain.java GreetingServer.java ScriptPython.java
//run::::	java -cp .:json-simple-1.1.1.jar:mysql-connector-java.jar GreetingServer
import java.io.*;
import org.json.simple.*;
import org.json.simple.parser.*; 
import java.util.*;
import java.sql.*;  
import java.nio.charset.*; 
import java.net.*;

public class GreetingServer extends Thread {
   private ServerSocket serverSocket;
   private Picit picit;
   public GreetingServer(int port) throws IOException {
      serverSocket = new ServerSocket(port);
      // serverSocket.setSoTimeout(10000);
   }

   public JSONObject process(JSONObject obj){
      String function = (String) obj.get("Function");
      JSONObject ans = new JSONObject();
      switch (function) {
         case "applyFilter":
            String filterCode = (String)obj.get("filterCode");
            String strImage = (String)obj.get("image");
            try{
               // int temp = picit.user.createUser(emailId, userName);
               byte[] imageByteArray = Base64.getDecoder().decode(strImage);
               FileOutputStream imageOutFile = new FileOutputStream("temp.jpg");
               imageOutFile.write(imageByteArray);
               imageOutFile.close();

               ScriptPython scriptPython = new ScriptPython();
               scriptPython.runScript("temp.jpg",filterCode);

               File file = new File("temp2.jpg");
               FileInputStream imageInFile = new FileInputStream(file);
               byte[] imageData = new byte[(int) file.length()];
               imageInFile.read(imageData);
               imageInFile.close();
               String imageDataString = Base64.getEncoder().encodeToString(imageData);
               // System.out.println(imageDataString);
               ans.put("answer",imageDataString);
               file.delete();
            } catch (Exception e){e.printStackTrace();ans.put("answer",-1);}
            
            break;
         case "createUser":
            String emailId = (String) obj.get("emailId");
            String userName =(String) obj.get("userName");
            // String password =(String) obj.get("password");
            try{
               int temp = picit.user.createUser(emailId, userName);
               ans.put("answer",temp);
            } catch (Exception e){e.printStackTrace();ans.put("answer",-1);}
            
            break;
         // Vector<Integer> getUseridsFromEmailids(Vector<String> emailIds)
         case "getUseridsFromEmailids":
            // int[] userIds, int creatorUserId, String groupName
            JSONArray emailIdss = (JSONArray) obj.get("emailIds");
            // Vector<Integer> userIds = new Vector<Integer>();
            Vector<String> emailIds = new Vector<String>();
            for (int i=0; i<emailIdss.size(); i++) {
               emailIds.add((String)emailIdss.get(i));
            } 
            try{
               Vector<Integer> temp = picit.user.getUseridsFromEmailids(emailIds);
               JSONArray ans1 = new JSONArray();
               for(int i=0;i<temp.size();i++){
                  ans1.add(temp.get(i));
               }
               ans.put("answer",ans1);
            } catch (Exception e){e.printStackTrace();ans.put("answer",-1);}
            break;
         case "createGroup":
            // int[] userIds, int creatorUserId, String groupName
            JSONArray uids = (JSONArray) obj.get("userIds");
            // int[] userIds = new int[uids.size()];
            Vector<Integer> userIds = new Vector<Integer>();
            for (int i=0; i<uids.size(); i++) {
               userIds.add((int)(long)uids.get(i));
            } 
            int creatorUserId = (int)(long) obj.get("creatorUserId");
            String groupName = (String) obj.get("groupName");
            
            try{
               int temp = picit.group.createGroup(userIds, creatorUserId, groupName);
               ans.put("answer",temp);
            } catch (Exception e){e.printStackTrace();ans.put("answer",-1);}
            break;
         case "addUserToGroup":
            int userId = (int)(long) obj.get("userId");
            int groupId =(int)(long) obj.get("groupId");
            boolean isActive =(boolean) obj.get("isActive");
            
            try{
               boolean temp = picit.group.addUserToGroup(userId, groupId, isActive);
               ans.put("answer",temp);
            } catch (Exception e){e.printStackTrace();ans.put("answer",false);}
            break;
         case "removeUserFromGroup":
            userId = (int)(long) obj.get("userId");
            groupId =(int)(long) obj.get("groupId");
            // boolean isActive =(boolean) obj.get("isActive");
            try{
               boolean temp = picit.group.removeUserFromGroup(userId, groupId);
               ans.put("answer",temp);
            } catch (Exception e){e.printStackTrace();ans.put("answer",false);}
            break;
         case "setGroupActive":
            userId = (int)(long) obj.get("userId");
            groupId =(int)(long) obj.get("groupId");
            // boolean isActive =(boolean) obj.get("isActive");
            try{
               boolean temp = picit.group.setGroupActive(userId, groupId);
               ans.put("answer",temp);
            } catch (Exception e){e.printStackTrace();ans.put("answer",false);}
            break;
         case "setGroupInactive":
            userId = (int)(long) obj.get("userId");
            groupId =(int)(long) obj.get("groupId");
            // boolean isActive =(boolean) obj.get("isActive");
            try{
               boolean temp = picit.group.setGroupInactive(userId, groupId);
               ans.put("answer",temp);
            } catch (Exception e){e.printStackTrace();ans.put("answer",false);}
            break;
         case "getGroupsOfUser":
            userId = (int)(long) obj.get("userId");
            try{
               Vector<String> arr = picit.group.getGroupsOfUser(userId);
               JSONArray temp = new JSONArray();
               for (int i = 0; i < arr.size(); i++) {
                  temp.add((String)arr.get(i));
              }
               ans.put("answer",temp);
            } catch (Exception e){e.printStackTrace();JSONArray arr = new JSONArray();ans.put("answer",arr);}
            break;
         case "getUsersInGroup":
            groupId =(int)(long) obj.get("groupId");
            try{
               Vector<String> arr = picit.group.getUsersInGroup(groupId);
               JSONArray temp = new JSONArray();
               for (int i = 0; i < arr.size(); i++) {
                  temp.add((String)arr.get(i));
              }
               ans.put("answer",temp);
            } catch (Exception e){e.printStackTrace();JSONArray arr = new JSONArray();ans.put("answer",arr);}
            break;
         case "uploadPicture":
            userId = (int)(long) obj.get("userId");
            // String url = (String) obj.get("url");
            try{
               int temp = picit.picture.uploadPicture(userId);
               ans.put("answer",temp);
            } catch (Exception e){e.printStackTrace();ans.put("answer",-1);}
            break;
         case "sharePictureToGroup":
            int picId = (int)(long) obj.get("picId");
            userId = (int)(long) obj.get("userId");
            groupId = (int)(long) obj.get("groupId");
            // url = (String) obj.get("url");
            try{
               boolean temp = picit.picture.sharePictureToGroup(picId,userId,groupId);
               ans.put("answer",temp);
            } catch (Exception e){e.printStackTrace();ans.put("answer",false);}
            break;
         case "getPicturesInGroup":
            groupId = (int)(long) obj.get("groupId");
            try{
               Vector<String> arr = picit.picture.getPicturesInGroup(groupId);
               JSONArray temp = new JSONArray();
               for (int i = 0; i < arr.size(); i++) {
                  temp.add(arr.get(i));
              }
               ans.put("answer",temp);
            } catch (Exception e){e.printStackTrace();JSONArray arr = new JSONArray();ans.put("answer",arr);}
            break;
         case "createAlbumServer":
            userId = (int)(long) obj.get("userId");
            String albumName = (String) obj.get("albumName");
            try{
               int temp = picit.album.createAlbumServer(albumName,userId);
               ans.put("answer",temp);
            } catch (Exception e){e.printStackTrace();ans.put("answer",-1);}
            break;
		case "getAlbumNameFromAlbumId":
            int albumId = (int)(long) obj.get("albumId");
            try{
               String temp = picit.album.getAlbumNameFromAlbumId(albumId);
               ans.put("answer",temp);
            } catch (Exception e){e.printStackTrace();ans.put("answer",-1);}
            break;
         case "addPicturesToAlbum":
            albumId = (int)(long) obj.get("albumId");
            uids = (JSONArray) obj.get("picIds");
            Vector<Integer> picIds = new Vector<Integer>();
            for (int i=0; i<uids.size(); i++) {
               picIds.add((int)(long) uids.get(i));
            }
            try{
               boolean temp = picit.album.addPicturesToAlbum(picIds,albumId);
               ans.put("answer",temp);
            } catch (Exception e){e.printStackTrace();ans.put("answer",false);}
            break;
         case "shareAlbumWithGroup":
            albumId = (int)(long) obj.get("albumId");
            groupId = (int)(long) obj.get("groupId");            
            try{
               boolean temp = picit.album.shareAlbumWithGroup(albumId,groupId);
               ans.put("answer",temp);
            } catch (Exception e){e.printStackTrace();ans.put("answer",false);}
            break;
         case "getAlbumsInGroup":
            groupId = (int)(long) obj.get("groupId");
            try{
               Vector<Integer> arr = picit.album.getAlbumsInGroup(groupId);
               JSONArray temp = new JSONArray();
               for (int i = 0; i < arr.size(); i++) {
                  temp.add(arr.get(i));
              }
               ans.put("answer",temp);
            } catch (Exception e){e.printStackTrace();JSONArray arr = new JSONArray();ans.put("answer",arr);}
            break;
         case "getPicturesInAlbum":
            albumId = (int)(long) obj.get("albumId");
            try{
               Vector<Integer> arr = picit.album.getPicturesInAlbum(albumId);
               JSONArray temp = new JSONArray();
               for (int i = 0; i < arr.size(); i++) {
                  temp.add(arr.get(i));
              }
               ans.put("answer",temp);
            } catch (Exception e){e.printStackTrace();JSONArray arr = new JSONArray();ans.put("answer",arr);}
            break;
         default:
            ans.put("answer",-1);
            // Vector<String> getPicturesInAlbum(int albumId)

      }
      return ans;
   }

   public void run() {
      // String url="jdbc:mysql://picit.cprurvpwu10u.us-east-1.rds.amazonaws.com:3306/picit";
      String url="jdbc:mysql://picit.cprurvpwu10u.us-east-1.rds.amazonaws.com:3306/picit";
      String userName="admin";
      String password="qwerty1234";

		picit = new Picit();
		
      try{  
         Class.forName("com.mysql.jdbc.Driver");  
         System.out.println("Connecting to aws rds mysql "+url);
         picit.con = DriverManager.getConnection(url, userName, password);  
         System.out.println("successfully connected to aws rds mysql "+url);


         Statement stmt = picit.con.createStatement();  
         ResultSet resultSet;

         resultSet = stmt.executeQuery("select max(userId) from Users");
         resultSet.next(); 
         picit.userIdMax = resultSet.getInt(1) + 1;//0 if no users 

         resultSet = stmt.executeQuery("select max(groupId) from Groups");  
         resultSet.next(); 
         picit.groupIdMax = resultSet.getInt(1) + 1;

         resultSet = stmt.executeQuery("select max(picId) from Pictures");  
         resultSet.next(); 
         picit.picIdMax = resultSet.getInt(1) + 1;

         resultSet = stmt.executeQuery("select max(albumId) from Albums");  
         resultSet.next(); 
         picit.albumIdMax = resultSet.getInt(1) + 1;

         System.out.println(picit.userIdMax+", "+picit.groupIdMax+", "+picit.picIdMax+", "+picit.albumIdMax);
      }catch(Exception e){
       System.out.println(e);
      } 

      while(true) {
         try {
            System.out.println("Waiting for client on port " + 
               serverSocket.getLocalPort() + "...");
            Socket server = serverSocket.accept();
            
            System.out.println("Just connected to " + server.getRemoteSocketAddress());
            
            // DataInputStream in = new DataInputStream(server.getInputStream());
            // String get_req = in.readUTF();
            // InputStream inputStream = socket.getInputStream();
            // String get_req;
            // inputStream.read(get_req);
            ObjectInputStream   ois = new ObjectInputStream(server.getInputStream());
            
            String get_req=(String)ois.readObject();
            // ois.close();
            System.out.println(get_req);
            // Object temp = JSONParser().parse(get_req);
            // JSONObject jobj = (JSONObject) temp;
            JSONParser parser = new JSONParser();
            JSONObject jobj = (JSONObject) parser.parse(get_req);
            JSONObject ans = process(jobj);
            // JSONObject jobj = new JSONObject(get_req);
            // System.out.println("request_received= " + jobj.toString());
            System.out.println("Function= " + (String) jobj.get("Function"));
            
            // DataOutputStream out = new DataOutputStream(server.getOutputStream());
            // out.writeUTF(ans.toString());
            ObjectOutputStream  oos = new ObjectOutputStream(server.getOutputStream());
            oos.flush();
            String ansString = ans.toString();
            oos.writeObject(ansString);
            System.out.println("ans= " + ansString);
            oos.flush();
            ois.close();
            oos.close();
            server.close();
            
         } catch (SocketTimeoutException s) {
            System.out.println("Socket timed out!");
            // break;
         } catch (IOException e) {
            e.printStackTrace();
            // break;
         }
         catch (Exception e) {
            e.printStackTrace();
            // break;
         }
      }
      // picit.con.close();  
   }
   
   public static void main(String [] args) {
      int port = Integer.parseInt("8500");
      try {
         Thread t = new GreetingServer(port);
         t.start();
      } catch (IOException e) {
         e.printStackTrace();
      }
   }
   
   // int createUser(String emailId, String userName){
   //    System.out.println("");
   //    return 1997;
   // }
   // int createGroup(int[] userIds, int creatorUserId, String groupName){
   //    return 1998;
   // }
   // boolean addUserToGroup(int userId, int groupId, boolean isActive){
   //    return true;
   // }
   // boolean removeUserFromGroup(int userId, int groupId){
   //    return true;
   // }
   // Vector<Integer> getGroupsOfUser(int userId){
   //    Vector<Integer> v = new Vector<Integer>();
   //    return v;
   // }
}

