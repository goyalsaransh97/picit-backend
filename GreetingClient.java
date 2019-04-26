import java.net.*;
import java.io.*;
// import org.json.simple.JSONObject;
import java.util.Base64;
import java.util.*;
import org.json.simple.*;
import org.json.simple.parser.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
public class GreetingClient {

	private static final String serverName = "3.84.167.114";
	// private static final String serverName = "localhost";
	private static final int port = 8500;	

   public static void main(String [] args) {
   		
      // try{
      // File file = new File("images/ted_cruz.jpg");
      // FileInputStream imageInFile = new FileInputStream(file);
      // byte imageData[] = new byte[(int) file.length()];
      // imageInFile.read(imageData);
      // imageInFile.close();

      // byte[] ret_imageData = applyFilter(imageData,"moustache");
      
      // System.out.println(ret_imageData);
      // FileOutputStream imageOutFile = new FileOutputStream("images/test.jpg");
      //  imageOutFile.write(ret_imageData);
      //  imageOutFile.close();
      // }
      // catch(Exception e)
      // {
      //    e.printStackTrace();
      // }


      // int user1 = createUser("kpmg1@go.com","username1");
      // int user2 = createUser("kpmg2@go.com","username2");
      // int user3 = createUser("kpmg3@go.com","username3");
      // int user4 = createUser("kpmg4@go.com","username4");
      
      // Vector<String> vecOfEmailIds = new Vector<String>();
      // vecOfEmailIds.add("kpmg1@go.com");
      // vecOfEmailIds.add("kpmg2@go.com");
      // vecOfEmailIds.add("kpmg3@go.com");
      // Vector<Integer> vecOfUserIds = getUseridsFromEmailids(vecOfEmailIds);

      // int groupid1 = createGroup(vecOfUserIds,user1,"groupA");
      // int groupid2 = createGroup(vecOfUserIds,user1,"groupB");

      // boolean bool1 = addUserToGroup(5,1,false);
      // Vector<String> vecOfGroupsOfUser = getGroupsOfUser(5);

      // boolean bool2 = removeUserFromGroup(5,1);
      // Vector<String> vecOfGroupsOfUser2 = getGroupsOfUser(5);

      // boolean bool3 = setGroupActive(user1,groupid1);
      // boolean bool4 = setGroupInactive(user2,groupid1);
      // Vector<String> vecOfGroupsOfUser = getGroupsOfUser(user2);
      // // System.out.println(vec.toString());
      // int picid1 = uploadPicture(user1);
      // int picid2 = uploadPicture(user2);
      // Vector<Integer> vecOfPicId = new Vector<Integer>();
      // vecOfPicId.add(picid1);
      // vecOfPicId.add(picid2);
      // boolean bool5 = sharePictureToGroup(picid1, user1, groupid1);
      // boolean bool6 = sharePictureToGroup(picid2, user2, groupid1);
      // Vector<String> vecOfPictures = getPicturesInGroup(groupid1);   
      // int album1 = createAlbumServer("albumname1",user1);
      // int album2 = createAlbumServer("albumname2",user2);
      // boolean bool7 = addPicturesToAlbum(vecOfPicId, album1);   
      // Vector<Integer> vecOfPicsOfAlbum = getPicturesInAlbum(album1);
      // boolean bool8 = shareAlbumWithGroup(album1, groupid1);   
      // boolean bool9 = shareAlbumWithGroup(album2, groupid1);   
      // Vector<Integer> vecOfAlbums =  getAlbumsInGroup(groupid1);
}

   public static byte[] applyFilter(byte[] imageByteArray, String filterCode) {

      try {
         System.out.println("Connecting to " + serverName + " on port " + port);
         Socket client = new Socket(serverName, port);
         
         System.out.println("Just connected to " + client.getRemoteSocketAddress());
         
         // ObjectInputStream ois = new ObjectInputStream(client.getInputStream());
         
         ObjectOutputStream oos = new ObjectOutputStream(client.getOutputStream());
         oos.flush();
         System.out.println("Done making ois and oos");
         // OutputStream outToServer = client.getOutputStream();
         // ObjectOutputStream out = new ObjectOutputStream(outToServer);
         
         // ByteArrayOutputStream baos = new ByteArrayOutputStream();
         // ImageIO.write( image, "jpg", baos );
         // baos.flush();
         // byte[] imageInByte = baos.toByteArray();
         // baos.close();
         String imageDataString = Base64.getEncoder().encodeToString(imageByteArray);
         
         JSONObject obj = new JSONObject();
         obj.put("Function","applyFilter");
         obj.put("image",imageDataString);
         obj.put("filterCode",filterCode);
         
         
         String objstr = obj.toString();
         System.out.println(objstr);
         // JSONObject newobj = new JSONObject(objstr);

         
         JSONParser parser = new JSONParser();
         // JSONObject json = (JSONObject) parser.parse(objstr);
         // System.out.println(json.get("Function"));

         // String ret_imageDataString = (String)json.get("image");
         // byte[] ret_imageByteArray = Base64.getDecoder().decode(ret_imageDataString);
         // return ret_imageByteArray;
         
         // outputStream.write(objstr);
         // outputStream.flush()
         oos.flush();
         System.out.println("next line I write");
        oos.writeObject(objstr);
        
        oos.flush();
        // oos.reset();
        // oos.close();
         System.out.println("sent to server, awaiting response");
         // InputStream inFromServer = client.getInputStream();
         // ObjectInputStream in = new ObjectInputStream(inFromServer);
         
         ObjectInputStream ois = new ObjectInputStream(client.getInputStream());

         System.out.println("done talking to server");
         String objrecvd = (String)ois.readObject();
         // ois.close();
         System.out.println("Server says \n" + objrecvd);
         JSONObject jsonrecvd = (JSONObject) parser.parse(objrecvd);
         System.out.println(jsonrecvd.get("answer"));
         client.close();
         String answer = (String)jsonrecvd.get("answer");
         byte[] ret_imageByteArray = Base64.getDecoder().decode(answer);
         return ret_imageByteArray;
         
         
      } catch (Exception e) {
         e.printStackTrace();
      }
      return null;
   }

   public static int createUser(String emailId, String userName) {

      try {
         System.out.println("Connecting to " + serverName + " on port " + port);
         Socket client = new Socket(serverName, port);
         
         System.out.println("Just connected to " + client.getRemoteSocketAddress());
         

         OutputStream outToServer = client.getOutputStream();
         ObjectOutputStream out = new ObjectOutputStream(outToServer);
         

         JSONObject obj = new JSONObject();
         obj.put("Function","createUser");
         obj.put("emailId",emailId);
         obj.put("userName",userName);
         
         String objstr = obj.toString();
         System.out.println(objstr);
         // JSONObject newobj = new JSONObject(objstr);

         
         JSONParser parser = new JSONParser();
         JSONObject json = (JSONObject) parser.parse(objstr);
         System.out.println(json.get("Function"));

      
         
         out.writeObject(objstr);

         System.out.println("sent to server, awaiting response");
         InputStream inFromServer = client.getInputStream();
         ObjectInputStream in = new ObjectInputStream(inFromServer);
         System.out.println("done talking to server");
         String objrecvd = (String)in.readObject();
         System.out.println("Server says \n" + objrecvd);
         JSONObject jsonrecvd = (JSONObject) parser.parse(objrecvd);
         System.out.println(jsonrecvd.get("answer"));

         int answer = (int)(long)jsonrecvd.get("answer");

         client.close();
         return answer;
      } catch (Exception e) {
         e.printStackTrace();
      }
      return 0;
   }
   public static Vector<Integer> getUseridsFromEmailids(Vector<String> emailIds)
   {
         try {
         System.out.println("Connecting to " + serverName + " on port " + port);
         Socket client = new Socket(serverName, port);
         
         System.out.println("Just connected to " + client.getRemoteSocketAddress());
         

         OutputStream outToServer = client.getOutputStream();
         ObjectOutputStream out = new ObjectOutputStream(outToServer);
         

         JSONObject obj = new JSONObject();
         obj.put("Function","getUseridsFromEmailids");
         JSONArray jsArray = new JSONArray();
         for (int i = 0; i < emailIds.size(); i++) {
            jsArray.add(emailIds.get(i));
            }
         obj.put("emailIds",jsArray);
         
         String objstr = obj.toString();
         System.out.println(objstr);
         // JSONObject newobj = new JSONObject(objstr);

         
         JSONParser parser = new JSONParser();
         JSONObject json = (JSONObject) parser.parse(objstr);
         // JSONArray jarray = (JSONArray)json.get("userIds");
         // if(jarray!=null)System.out.println("yaaay, it is not null");
         // else System.out.println("booo! it is null"); 
         
         // int n = jarray.size();
         // System.out.println(n);

         
         // int[] usid = new int[n];
         // for(int i=0;i<n;i++)
         // {
         //    usid[i] = (int)(long)jarray.get(i);
         // }
         // System.out.println(usid[2]);
      
         
         out.writeObject(objstr);

         System.out.println("sent to server, awaiting response");
         InputStream inFromServer = client.getInputStream();
         ObjectInputStream in = new ObjectInputStream(inFromServer);
         System.out.println("done talking to server");
         String objrecvd = (String)in.readObject();
         System.out.println("Server says \n" + objrecvd);
         JSONObject jsonrecvd = (JSONObject) parser.parse(objrecvd);
         System.out.println(jsonrecvd.get("answer"));

         JSONArray jarray = (JSONArray)jsonrecvd.get("answer");
         if(jarray==null)System.out.println("its a null");
         Vector<Integer> answer = new Vector<Integer>();
         for(int i=0;i<jarray.size();i++)
         {
            answer.add(new Integer((int)(long)jarray.get(i)));
         }
         
         client.close();
         return answer;
      } catch (Exception e) {
         e.printStackTrace();
      }
      return (new Vector<Integer>());
   }

   public static int createGroup(Vector<Integer> userIds, int creatorUserId, String groupName)

   {
   		try {
         System.out.println("Connecting to " + serverName + " on port " + port);
         Socket client = new Socket(serverName, port);
         
         System.out.println("Just connected to " + client.getRemoteSocketAddress());
         

         OutputStream outToServer = client.getOutputStream();
         ObjectOutputStream out = new ObjectOutputStream(outToServer);
         

         JSONObject obj = new JSONObject();
         obj.put("Function","createGroup");
         JSONArray jsArray = new JSONArray();
         for (int i = 0; i < userIds.size(); i++) {
         	jsArray.add(userIds.get(i));
      		}
         obj.put("userIds",jsArray);
         obj.put("creatorUserId",creatorUserId);
         obj.put("groupName",groupName);
         
         String objstr = obj.toString();
         System.out.println(objstr);
         // JSONObject newobj = new JSONObject(objstr);

         
         JSONParser parser = new JSONParser();
         JSONObject json = (JSONObject) parser.parse(objstr);
         JSONArray jarray = (JSONArray)json.get("userIds");
         // if(jarray!=null)System.out.println("yaaay, it is not null");
         // else System.out.println("booo! it is null");	
         
         // int n = jarray.size();
         // System.out.println(n);

         
         // int[] usid = new int[n];
         // for(int i=0;i<n;i++)
         // {
         // 	usid[i] = (int)(long)jarray.get(i);
         // }
         // System.out.println(usid[2]);
      
         
         out.writeObject(objstr);

         System.out.println("sent to server, awaiting response");
         InputStream inFromServer = client.getInputStream();
         ObjectInputStream in = new ObjectInputStream(inFromServer);
         System.out.println("done talking to server");
         String objrecvd = (String)in.readObject();
         System.out.println("Server says \n" + objrecvd);
         JSONObject jsonrecvd = (JSONObject) parser.parse(objrecvd);
         System.out.println(jsonrecvd.get("answer"));

         int answer = (int)(long)jsonrecvd.get("answer");
         client.close();
         return answer;
      } catch (Exception e) {
         e.printStackTrace();
      }
      return 0;
   }

   public static boolean addUserToGroup(int userId, int groupId, boolean isActive) {

      try {
         System.out.println("Connecting to " + serverName + " on port " + port);
         Socket client = new Socket(serverName, port);
         
         System.out.println("Just connected to " + client.getRemoteSocketAddress());
         

         OutputStream outToServer = client.getOutputStream();
         ObjectOutputStream out = new ObjectOutputStream(outToServer);
         

         JSONObject obj = new JSONObject();
         obj.put("Function","addUserToGroup");
         obj.put("userId",userId);
         obj.put("groupId",groupId);
         obj.put("isActive",isActive);
         
         String objstr = obj.toString();
         System.out.println(objstr);
         // JSONObject newobj = new JSONObject(objstr);

         
         JSONParser parser = new JSONParser();
         JSONObject json = (JSONObject) parser.parse(objstr);
         System.out.println(json.get("Function"));

      
         
         out.writeObject(objstr);

         System.out.println("sent to server, awaiting response");
         InputStream inFromServer = client.getInputStream();
         ObjectInputStream in = new ObjectInputStream(inFromServer);
         System.out.println("done talking to server");
         String objrecvd = (String)in.readObject();
         System.out.println("Server says \n" + objrecvd);
         JSONObject jsonrecvd = (JSONObject) parser.parse(objrecvd);
         System.out.println(jsonrecvd.get("answer"));

         boolean answer = (boolean)jsonrecvd.get("answer");

         client.close();
         return answer;
      } catch (Exception e) {
         e.printStackTrace();
      }
      return false;
   }

   public static boolean removeUserFromGroup(int userId, int groupId) {

      try {
         System.out.println("Connecting to " + serverName + " on port " + port);
         Socket client = new Socket(serverName, port);
         
         System.out.println("Just connected to " + client.getRemoteSocketAddress());
         

         OutputStream outToServer = client.getOutputStream();
         ObjectOutputStream out = new ObjectOutputStream(outToServer);
         

         JSONObject obj = new JSONObject();
         obj.put("Function","removeUserFromGroup");
         obj.put("userId",userId);
         obj.put("groupId",groupId);
         
         String objstr = obj.toString();
         System.out.println(objstr);
         // JSONObject newobj = new JSONObject(objstr);

         
         JSONParser parser = new JSONParser();
         JSONObject json = (JSONObject) parser.parse(objstr);
         System.out.println(json.get("Function"));

      
         
         out.writeObject(objstr);

         System.out.println("sent to server, awaiting response");
         InputStream inFromServer = client.getInputStream();
         ObjectInputStream in = new ObjectInputStream(inFromServer);
         System.out.println("done talking to server");
         String objrecvd = (String)in.readObject();
         System.out.println("Server says \n" + objrecvd);
         JSONObject jsonrecvd = (JSONObject) parser.parse(objrecvd);
         System.out.println(jsonrecvd.get("answer"));

         boolean answer = (boolean)jsonrecvd.get("answer");

         client.close();
         return answer;
      } catch (Exception e) {
         e.printStackTrace();
      }
      return false;
   }
   public static boolean setGroupActive(int userId, int groupId) {

      try {
         System.out.println("Connecting to " + serverName + " on port " + port);
         Socket client = new Socket(serverName, port);
         
         System.out.println("Just connected to " + client.getRemoteSocketAddress());
         

         OutputStream outToServer = client.getOutputStream();
         ObjectOutputStream out = new ObjectOutputStream(outToServer);
         

         JSONObject obj = new JSONObject();
         obj.put("Function","setGroupActive");
         obj.put("userId",userId);
         obj.put("groupId",groupId);
         
         String objstr = obj.toString();
         System.out.println(objstr);
         // JSONObject newobj = new JSONObject(objstr);

         
         JSONParser parser = new JSONParser();
         JSONObject json = (JSONObject) parser.parse(objstr);
         System.out.println(json.get("Function"));

      
         
         out.writeObject(objstr);

         System.out.println("sent to server, awaiting response");
         InputStream inFromServer = client.getInputStream();
         ObjectInputStream in = new ObjectInputStream(inFromServer);
         System.out.println("done talking to server");
         String objrecvd = (String)in.readObject();
         System.out.println("Server says \n" + objrecvd);
         JSONObject jsonrecvd = (JSONObject) parser.parse(objrecvd);
         System.out.println(jsonrecvd.get("answer"));

         boolean answer = (boolean)jsonrecvd.get("answer");

         client.close();
         return answer;
      } catch (Exception e) {
         e.printStackTrace();
      }
      return false;
   }

   public static boolean setGroupInactive(int userId, int groupId) {

      try {
         System.out.println("Connecting to " + serverName + " on port " + port);
         Socket client = new Socket(serverName, port);
         
         System.out.println("Just connected to " + client.getRemoteSocketAddress());
         

         OutputStream outToServer = client.getOutputStream();
         ObjectOutputStream out = new ObjectOutputStream(outToServer);
         

         JSONObject obj = new JSONObject();
         obj.put("Function","setGroupInactive");
         obj.put("userId",userId);
         obj.put("groupId",groupId);
         
         String objstr = obj.toString();
         System.out.println(objstr);
         // JSONObject newobj = new JSONObject(objstr);

         
         JSONParser parser = new JSONParser();
         JSONObject json = (JSONObject) parser.parse(objstr);
         System.out.println(json.get("Function"));

      
         
         out.writeObject(objstr);

         System.out.println("sent to server, awaiting response");
         InputStream inFromServer = client.getInputStream();
         ObjectInputStream in = new ObjectInputStream(inFromServer);
         System.out.println("done talking to server");
         String objrecvd = (String)in.readObject();
         System.out.println("Server says \n" + objrecvd);
         JSONObject jsonrecvd = (JSONObject) parser.parse(objrecvd);
         System.out.println(jsonrecvd.get("answer"));

         boolean answer = (boolean)jsonrecvd.get("answer");

         client.close();
         return answer;
      } catch (Exception e) {
         e.printStackTrace();
      }
      return false;
   }

   public static Vector<String> getGroupsOfUser(int userId) {

      try {
         System.out.println("Connecting to " + serverName + " on port " + port);
         Socket client = new Socket(serverName, port);
         
         System.out.println("Just connected to " + client.getRemoteSocketAddress());
         

         OutputStream outToServer = client.getOutputStream();
         ObjectOutputStream out = new ObjectOutputStream(outToServer);
         

         JSONObject obj = new JSONObject();
         obj.put("Function","getGroupsOfUser");
         obj.put("userId",userId);
         
         String objstr = obj.toString();
         System.out.println(objstr);
         // JSONObject newobj = new JSONObject(objstr);

         
         JSONParser parser = new JSONParser();
         JSONObject json = (JSONObject) parser.parse(objstr);
         System.out.println(json.get("Function"));

      
         
         out.writeObject(objstr);

         System.out.println("sent to server, awaiting response");
         InputStream inFromServer = client.getInputStream();
         ObjectInputStream in = new ObjectInputStream(inFromServer);
         System.out.println("done talking to server");
         String objrecvd = (String)in.readObject();
         System.out.println("Server says \n" + objrecvd);
         JSONObject jsonrecvd = (JSONObject) parser.parse(objrecvd);
         System.out.println(jsonrecvd.get("answer"));
         JSONArray jarray = (JSONArray)jsonrecvd.get("answer");
         if(jarray==null)System.out.println("its a null");
         Vector<String> answer = new Vector<String>();
         for(int i=0;i<jarray.size();i++)
         {
            answer.add((String)jarray.get(i));
         }

         // boolean answer = (boolean)jsonrecvd.get("answer");

         client.close();
         return answer;
      } catch (Exception e) {
         e.printStackTrace();
      }
      return (new Vector<String>());
   }


   public static int uploadPicture(int userId) {

      try {
         System.out.println("Connecting to " + serverName + " on port " + port);
         Socket client = new Socket(serverName, port);
         
         System.out.println("Just connected to " + client.getRemoteSocketAddress());
         

         OutputStream outToServer = client.getOutputStream();
         ObjectOutputStream out = new ObjectOutputStream(outToServer);
         

         JSONObject obj = new JSONObject();
         obj.put("Function","uploadPicture");
         obj.put("userId",userId);
                  
         String objstr = obj.toString();
         System.out.println(objstr);
         // JSONObject newobj = new JSONObject(objstr);

         
         JSONParser parser = new JSONParser();
         JSONObject json = (JSONObject) parser.parse(objstr);
         System.out.println(json.get("Function"));

      
         
         out.writeObject(objstr);

         System.out.println("sent to server, awaiting response");
         InputStream inFromServer = client.getInputStream();
         ObjectInputStream in = new ObjectInputStream(inFromServer);
         System.out.println("done talking to server");
         String objrecvd = (String)in.readObject();
         System.out.println("Server says \n" + objrecvd);
         JSONObject jsonrecvd = (JSONObject) parser.parse(objrecvd);
         System.out.println(jsonrecvd.get("answer"));

         int answer = (int)(long)jsonrecvd.get("answer");

         client.close();
         return answer;
      } catch (Exception e) {
         e.printStackTrace();
      }
      return 0;
   }

   public static boolean sharePictureToGroup(int picId, int userId, int groupId) {

      try {
         System.out.println("Connecting to " + serverName + " on port " + port);
         Socket client = new Socket(serverName, port);
         
         System.out.println("Just connected to " + client.getRemoteSocketAddress());
         

         OutputStream outToServer = client.getOutputStream();
         ObjectOutputStream out = new ObjectOutputStream(outToServer);
         

         JSONObject obj = new JSONObject();
         obj.put("Function","sharePictureToGroup");
         obj.put("picId",picId);
         obj.put("userId",userId);
         obj.put("groupId",groupId);
         
         String objstr = obj.toString();
         System.out.println(objstr);
         // JSONObject newobj = new JSONObject(objstr);

         
         JSONParser parser = new JSONParser();
         JSONObject json = (JSONObject) parser.parse(objstr);
         System.out.println(json.get("Function"));

      
         
         out.writeObject(objstr);

         System.out.println("sent to server, awaiting response");
         InputStream inFromServer = client.getInputStream();
         ObjectInputStream in = new ObjectInputStream(inFromServer);
         System.out.println("done talking to server");
         String objrecvd = (String)in.readObject();
         System.out.println("Server says \n" + objrecvd);
         JSONObject jsonrecvd = (JSONObject) parser.parse(objrecvd);
         System.out.println(jsonrecvd.get("answer"));

         boolean answer = (boolean)jsonrecvd.get("answer");

         client.close();
         return answer;
      } catch (Exception e) {
         e.printStackTrace();
      }
      return false;
   }

   public static Vector<String> getPicturesInGroup(int groupId) {

      try {
         System.out.println("Connecting to " + serverName + " on port " + port);
         Socket client = new Socket(serverName, port);
         
         System.out.println("Just connected to " + client.getRemoteSocketAddress());
         

         OutputStream outToServer = client.getOutputStream();
         ObjectOutputStream out = new ObjectOutputStream(outToServer);
         

         JSONObject obj = new JSONObject();
         obj.put("Function","getPicturesInGroup");
         obj.put("groupId",groupId);
         
         String objstr = obj.toString();
         System.out.println(objstr);
         // JSONObject newobj = new JSONObject(objstr);

         
         JSONParser parser = new JSONParser();
         JSONObject json = (JSONObject) parser.parse(objstr);
         System.out.println(json.get("Function"));

      
         
         out.writeObject(objstr);

         System.out.println("sent to server, awaiting response");
         InputStream inFromServer = client.getInputStream();
         ObjectInputStream in = new ObjectInputStream(inFromServer);
         System.out.println("done talking to server");
         String objrecvd = (String)in.readObject();
         System.out.println("Server says \n" + objrecvd);
         JSONObject jsonrecvd = (JSONObject) parser.parse(objrecvd);
         System.out.println(jsonrecvd.get("answer"));
         JSONArray jarray = (JSONArray)jsonrecvd.get("answer");
         Vector<String> answer = new Vector<String>();
         for(int i=0;i<jarray.size();i++)
         {
            answer.add((String)jarray.get(i));
         }

         // boolean answer = (boolean)jsonrecvd.get("answer");

         client.close();
         return answer;
      } catch (Exception e) {
         e.printStackTrace();
      }
      return (new Vector<String>());
   }

   public static int createAlbumServer(String albumName, int userId) {

      try {
         System.out.println("Connecting to " + serverName + " on port " + port);
         Socket client = new Socket(serverName, port);
         
         System.out.println("Just connected to " + client.getRemoteSocketAddress());
         

         OutputStream outToServer = client.getOutputStream();
         ObjectOutputStream out = new ObjectOutputStream(outToServer);
         

         JSONObject obj = new JSONObject();
         obj.put("Function","createAlbumServer");
         obj.put("albumName",albumName);
         obj.put("userId",userId);
         
         String objstr = obj.toString();
         System.out.println(objstr);
         // JSONObject newobj = new JSONObject(objstr);

         
         JSONParser parser = new JSONParser();
         JSONObject json = (JSONObject) parser.parse(objstr);
         System.out.println(json.get("Function"));

      
         
         out.writeObject(objstr);

         System.out.println("sent to server, awaiting response");
         InputStream inFromServer = client.getInputStream();
         ObjectInputStream in = new ObjectInputStream(inFromServer);
         System.out.println("done talking to server");
         String objrecvd = (String)in.readObject();
         System.out.println("Server says \n" + objrecvd);
         JSONObject jsonrecvd = (JSONObject) parser.parse(objrecvd);
         System.out.println(jsonrecvd.get("answer"));

         int answer = (int)(long)jsonrecvd.get("answer");

         client.close();
         return answer;
      } catch (Exception e) {
         e.printStackTrace();
      }
      return 0;
   }

   public static boolean addPicturesToAlbum(Vector<Integer> picIds, int albumId)
   {
         try {
         System.out.println("Connecting to " + serverName + " on port " + port);
         Socket client = new Socket(serverName, port);
         
         System.out.println("Just connected to " + client.getRemoteSocketAddress());
         

         OutputStream outToServer = client.getOutputStream();
         ObjectOutputStream out = new ObjectOutputStream(outToServer);
         

         JSONObject obj = new JSONObject();
         obj.put("Function","addPicturesToAlbum");
         JSONArray jsArray = new JSONArray();
         for (int i = 0; i < picIds.size(); i++) {
            jsArray.add(picIds.get(i));
            }
         obj.put("picIds",jsArray);
         obj.put("albumId",albumId);
         
         String objstr = obj.toString();
         System.out.println(objstr);
         // JSONObject newobj = new JSONObject(objstr);

         
         JSONParser parser = new JSONParser();
         JSONObject json = (JSONObject) parser.parse(objstr);
         JSONArray jarray = (JSONArray)json.get("userIds");
         // if(jarray!=null)System.out.println("yaaay, it is not null");
         // else System.out.println("booo! it is null"); 
         
         // int n = jarray.size();
         // System.out.println(n);

         
         // int[] usid = new int[n];
         // for(int i=0;i<n;i++)
         // {
         //    usid[i] = (int)(long)jarray.get(i);
         // }
         // System.out.println(usid[2]);
      
         
         out.writeObject(objstr);

         System.out.println("sent to server, awaiting response");
         InputStream inFromServer = client.getInputStream();
         ObjectInputStream in = new ObjectInputStream(inFromServer);
         System.out.println("done talking to server");
         String objrecvd = (String)in.readObject();
         System.out.println("Server says \n" + objrecvd);
         JSONObject jsonrecvd = (JSONObject) parser.parse(objrecvd);
         System.out.println(jsonrecvd.get("answer"));

         boolean answer = (boolean)jsonrecvd.get("answer");
         client.close();
         return answer;
      } catch (Exception e) {
         e.printStackTrace();
      }
      return false;
   }
   public static Vector<Integer> getPicturesInAlbum(int albumId)
   {
         try {
         System.out.println("Connecting to " + serverName + " on port " + port);
         Socket client = new Socket(serverName, port);
         
         System.out.println("Just connected to " + client.getRemoteSocketAddress());
         

         OutputStream outToServer = client.getOutputStream();
         ObjectOutputStream out = new ObjectOutputStream(outToServer);
         

         JSONObject obj = new JSONObject();
         obj.put("Function","getPicturesInAlbum");
        
         obj.put("albumId",albumId);
         
         String objstr = obj.toString();
         System.out.println(objstr);
         // JSONObject newobj = new JSONObject(objstr);

         
         JSONParser parser = new JSONParser();
         JSONObject json = (JSONObject) parser.parse(objstr);
         // JSONArray jarray = (JSONArray)json.get("userIds");
         // if(jarray!=null)System.out.println("yaaay, it is not null");
         // else System.out.println("booo! it is null"); 
         
         // int n = jarray.size();
         // System.out.println(n);

         
         // int[] usid = new int[n];
         // for(int i=0;i<n;i++)
         // {
         //    usid[i] = (int)(long)jarray.get(i);
         // }
         // System.out.println(usid[2]);
      
         
         out.writeObject(objstr);

         System.out.println("sent to server, awaiting response");
         InputStream inFromServer = client.getInputStream();
         ObjectInputStream in = new ObjectInputStream(inFromServer);
         System.out.println("done talking to server");
         String objrecvd = (String)in.readObject();
         System.out.println("Server says \n" + objrecvd);
         JSONObject jsonrecvd = (JSONObject) parser.parse(objrecvd);
         System.out.println(jsonrecvd.get("answer"));

         JSONArray jarray = (JSONArray)jsonrecvd.get("answer");
         if(jarray==null)System.out.println("its a null");
         Vector<Integer> answer = new Vector<Integer>();
         for(int i=0;i<jarray.size();i++)
         {
            answer.add(new Integer((int)(long)jarray.get(i)));
         }
         
         client.close();
         return answer;
      } catch (Exception e) {
         e.printStackTrace();
      }
      return (new Vector<Integer>());
   }
   public static boolean shareAlbumWithGroup(int albumId, int groupId) {

      try {
         System.out.println("Connecting to " + serverName + " on port " + port);
         Socket client = new Socket(serverName, port);
         
         System.out.println("Just connected to " + client.getRemoteSocketAddress());
         

         OutputStream outToServer = client.getOutputStream();
         ObjectOutputStream out = new ObjectOutputStream(outToServer);
         

         JSONObject obj = new JSONObject();
         obj.put("Function","shareAlbumWithGroup");
         obj.put("albumId",albumId);
         obj.put("groupId",groupId);
         
         String objstr = obj.toString();
         System.out.println(objstr);
         // JSONObject newobj = new JSONObject(objstr);

         
         JSONParser parser = new JSONParser();
         JSONObject json = (JSONObject) parser.parse(objstr);
         System.out.println(json.get("Function"));

      
         
         out.writeObject(objstr);

         System.out.println("sent to server, awaiting response");
         InputStream inFromServer = client.getInputStream();
         ObjectInputStream in = new ObjectInputStream(inFromServer);
         System.out.println("done talking to server");
         String objrecvd = (String)in.readObject();
         System.out.println("Server says \n" + objrecvd);
         JSONObject jsonrecvd = (JSONObject) parser.parse(objrecvd);
         System.out.println(jsonrecvd.get("answer"));

         boolean answer = (boolean)jsonrecvd.get("answer");

         client.close();
         return answer;
      } catch (Exception e) {
         e.printStackTrace();
      }
      return false;
   }

   public static Vector<Integer> getAlbumsInGroup(int groupId)
   {
         try {
         System.out.println("Connecting to " + serverName + " on port " + port);
         Socket client = new Socket(serverName, port);
         
         System.out.println("Just connected to " + client.getRemoteSocketAddress());
         

         OutputStream outToServer = client.getOutputStream();
         ObjectOutputStream out = new ObjectOutputStream(outToServer);
         

         JSONObject obj = new JSONObject();
         obj.put("Function","getAlbumsInGroup");
        
         obj.put("groupId",groupId);
         
         String objstr = obj.toString();
         System.out.println(objstr);
         // JSONObject newobj = new JSONObject(objstr);

         
         JSONParser parser = new JSONParser();
         JSONObject json = (JSONObject) parser.parse(objstr);
         // JSONArray jarray = (JSONArray)json.get("userIds");
         // if(jarray!=null)System.out.println("yaaay, it is not null");
         // else System.out.println("booo! it is null"); 
         
         // int n = jarray.size();
         // System.out.println(n);

         
         // int[] usid = new int[n];
         // for(int i=0;i<n;i++)
         // {
         //    usid[i] = (int)(long)jarray.get(i);
         // }
         // System.out.println(usid[2]);
      
         
         out.writeObject(objstr);

         System.out.println("sent to server, awaiting response");
         InputStream inFromServer = client.getInputStream();
         ObjectInputStream in = new ObjectInputStream(inFromServer);
         System.out.println("done talking to server");
         String objrecvd = (String)in.readObject();
         System.out.println("Server says \n" + objrecvd);
         JSONObject jsonrecvd = (JSONObject) parser.parse(objrecvd);
         System.out.println(jsonrecvd.get("answer"));

         JSONArray jarray = (JSONArray)jsonrecvd.get("answer");
         if(jarray==null)System.out.println("its a null");
         Vector<Integer> answer = new Vector<Integer>();
         for(int i=0;i<jarray.size();i++)
         {
            answer.add(new Integer((int)(long)jarray.get(i)));
         }
         
         client.close();
         return answer;
      } catch (Exception e) {
         e.printStackTrace();
      }
      return (new Vector<Integer>());
   }
}