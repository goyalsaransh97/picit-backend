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
         Socket client = new Socket(serverName, port);
         ObjectOutputStream oos = new ObjectOutputStream(client.getOutputStream());
         oos.flush();
         String imageDataString = Base64.getEncoder().encodeToString(imageByteArray);
         JSONObject obj = new JSONObject();
         obj.put("Function","applyFilter");
         obj.put("image",imageDataString);
         obj.put("filterCode",filterCode);
         String objstr = obj.toString();
         JSONParser parser = new JSONParser();
         oos.flush();
         oos.writeObject(objstr);
         oos.flush();
         
         ObjectInputStream ois = new ObjectInputStream(client.getInputStream());

         String objrecvd = (String)ois.readObject();
         JSONObject jsonrecvd = (JSONObject) parser.parse(objrecvd);
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
         Socket client = new Socket(serverName, port);
         OutputStream outToServer = client.getOutputStream();
         ObjectOutputStream out = new ObjectOutputStream(outToServer);
         

         JSONObject obj = new JSONObject();
         obj.put("Function","createUser");
         obj.put("emailId",emailId);
         obj.put("userName",userName);
         
         String objstr = obj.toString();
         JSONParser parser = new JSONParser();
         JSONObject json = (JSONObject) parser.parse(objstr);
         
         out.writeObject(objstr);

         InputStream inFromServer = client.getInputStream();
         ObjectInputStream in = new ObjectInputStream(inFromServer);
         String objrecvd = (String)in.readObject();
         JSONObject jsonrecvd = (JSONObject) parser.parse(objrecvd);
         
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
         
         Socket client = new Socket(serverName, port);
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
         JSONParser parser = new JSONParser();
         JSONObject json = (JSONObject) parser.parse(objstr);
         out.writeObject(objstr);

         
         InputStream inFromServer = client.getInputStream();
         ObjectInputStream in = new ObjectInputStream(inFromServer);
         
         String objrecvd = (String)in.readObject();
         
         JSONObject jsonrecvd = (JSONObject) parser.parse(objrecvd);
         

         JSONArray jarray = (JSONArray)jsonrecvd.get("answer");
         
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
         
         Socket client = new Socket(serverName, port);
         
         
         

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
         
         JSONParser parser = new JSONParser();
         JSONObject json = (JSONObject) parser.parse(objstr);
         JSONArray jarray = (JSONArray)json.get("userIds");
         out.writeObject(objstr);

         
         InputStream inFromServer = client.getInputStream();
         ObjectInputStream in = new ObjectInputStream(inFromServer);
         
         String objrecvd = (String)in.readObject();
         
         JSONObject jsonrecvd = (JSONObject) parser.parse(objrecvd);
         

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
         
         Socket client = new Socket(serverName, port);
         
         
         

         OutputStream outToServer = client.getOutputStream();
         ObjectOutputStream out = new ObjectOutputStream(outToServer);
         

         JSONObject obj = new JSONObject();
         obj.put("Function","addUserToGroup");
         obj.put("userId",userId);
         obj.put("groupId",groupId);
         obj.put("isActive",isActive);
         
         String objstr = obj.toString();
         
         JSONParser parser = new JSONParser();
         JSONObject json = (JSONObject) parser.parse(objstr);
         out.writeObject(objstr);

         InputStream inFromServer = client.getInputStream();
         ObjectInputStream in = new ObjectInputStream(inFromServer);
         
         String objrecvd = (String)in.readObject();
         
         JSONObject jsonrecvd = (JSONObject) parser.parse(objrecvd);
         

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
         
         Socket client = new Socket(serverName, port);
         OutputStream outToServer = client.getOutputStream();
         ObjectOutputStream out = new ObjectOutputStream(outToServer);
         

         JSONObject obj = new JSONObject();
         obj.put("Function","removeUserFromGroup");
         obj.put("userId",userId);
         obj.put("groupId",groupId);
         
         String objstr = obj.toString();
         
         JSONParser parser = new JSONParser();
         JSONObject json = (JSONObject) parser.parse(objstr);
         out.writeObject(objstr);

         InputStream inFromServer = client.getInputStream();
         ObjectInputStream in = new ObjectInputStream(inFromServer);
         
         String objrecvd = (String)in.readObject();
         
         JSONObject jsonrecvd = (JSONObject) parser.parse(objrecvd);
         

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
         
         Socket client = new Socket(serverName, port);
         
         OutputStream outToServer = client.getOutputStream();
         ObjectOutputStream out = new ObjectOutputStream(outToServer);
         

         JSONObject obj = new JSONObject();
         obj.put("Function","setGroupActive");
         obj.put("userId",userId);
         obj.put("groupId",groupId);
         
         String objstr = obj.toString();
         
         JSONParser parser = new JSONParser();
         JSONObject json = (JSONObject) parser.parse(objstr);
         
         out.writeObject(objstr);

         
         InputStream inFromServer = client.getInputStream();
         ObjectInputStream in = new ObjectInputStream(inFromServer);
         
         String objrecvd = (String)in.readObject();
         
         JSONObject jsonrecvd = (JSONObject) parser.parse(objrecvd);
         

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
         
         Socket client = new Socket(serverName, port);
         
         
         

         OutputStream outToServer = client.getOutputStream();
         ObjectOutputStream out = new ObjectOutputStream(outToServer);
         

         JSONObject obj = new JSONObject();
         obj.put("Function","setGroupInactive");
         obj.put("userId",userId);
         obj.put("groupId",groupId);
         
         String objstr = obj.toString();
         
         JSONParser parser = new JSONParser();
         JSONObject json = (JSONObject) parser.parse(objstr);
         
         out.writeObject(objstr);

         
         InputStream inFromServer = client.getInputStream();
         ObjectInputStream in = new ObjectInputStream(inFromServer);
         
         String objrecvd = (String)in.readObject();
         
         JSONObject jsonrecvd = (JSONObject) parser.parse(objrecvd);
         

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
         
         Socket client = new Socket(serverName, port);
         
         OutputStream outToServer = client.getOutputStream();
         ObjectOutputStream out = new ObjectOutputStream(outToServer);
         

         JSONObject obj = new JSONObject();
         obj.put("Function","getGroupsOfUser");
         obj.put("userId",userId);
         
         String objstr = obj.toString();
         
         JSONParser parser = new JSONParser();
         JSONObject json = (JSONObject) parser.parse(objstr);
         
         out.writeObject(objstr);

         
         InputStream inFromServer = client.getInputStream();
         ObjectInputStream in = new ObjectInputStream(inFromServer);
         
         String objrecvd = (String)in.readObject();
         
         JSONObject jsonrecvd = (JSONObject) parser.parse(objrecvd);
         
         JSONArray jarray = (JSONArray)jsonrecvd.get("answer");
         
         Vector<String> answer = new Vector<String>();
         for(int i=0;i<jarray.size();i++)
         {
            answer.add((String)jarray.get(i));
         }
         client.close();
         return answer;
      } catch (Exception e) {
         e.printStackTrace();
      }
      return (new Vector<String>());
   }

   public static Vector<String> getUsersInGroup(int groupId) {

      try {
         
         Socket client = new Socket(serverName, port);
         OutputStream outToServer = client.getOutputStream();
         ObjectOutputStream out = new ObjectOutputStream(outToServer);
         JSONObject obj = new JSONObject();
         obj.put("Function","getUsersInGroup");
         obj.put("groupId",groupId);
         
         String objstr = obj.toString();
         
         JSONParser parser = new JSONParser();
         JSONObject json = (JSONObject) parser.parse(objstr);
         
         out.writeObject(objstr);

         InputStream inFromServer = client.getInputStream();
         ObjectInputStream in = new ObjectInputStream(inFromServer);
         
         String objrecvd = (String)in.readObject();
         
         JSONObject jsonrecvd = (JSONObject) parser.parse(objrecvd);
         
         JSONArray jarray = (JSONArray)jsonrecvd.get("answer");
         
         Vector<String> answer = new Vector<String>();
         for(int i=0;i<jarray.size();i++)
         {
            answer.add((String)jarray.get(i));
         }

         client.close();
         return answer;
      } catch (Exception e) {
         e.printStackTrace();
      }
      return (new Vector<String>());
   }

   public static int uploadPicture(int userId) {

      try {
         
         Socket client = new Socket(serverName, port);
         
         OutputStream outToServer = client.getOutputStream();
         ObjectOutputStream out = new ObjectOutputStream(outToServer);
         

         JSONObject obj = new JSONObject();
         obj.put("Function","uploadPicture");
         obj.put("userId",userId);
                  
         String objstr = obj.toString();
         
         JSONParser parser = new JSONParser();
         JSONObject json = (JSONObject) parser.parse(objstr);
         
         out.writeObject(objstr);

         InputStream inFromServer = client.getInputStream();
         ObjectInputStream in = new ObjectInputStream(inFromServer);
         
         String objrecvd = (String)in.readObject();
         
         JSONObject jsonrecvd = (JSONObject) parser.parse(objrecvd);

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
         
         Socket client = new Socket(serverName, port);
         
         OutputStream outToServer = client.getOutputStream();
         ObjectOutputStream out = new ObjectOutputStream(outToServer);
         

         JSONObject obj = new JSONObject();
         obj.put("Function","sharePictureToGroup");
         obj.put("picId",picId);
         obj.put("userId",userId);
         obj.put("groupId",groupId);
         
         String objstr = obj.toString();
         
         
         JSONParser parser = new JSONParser();
         JSONObject json = (JSONObject) parser.parse(objstr);
         
         out.writeObject(objstr);

         
         InputStream inFromServer = client.getInputStream();
         ObjectInputStream in = new ObjectInputStream(inFromServer);
         
         String objrecvd = (String)in.readObject();
         
         JSONObject jsonrecvd = (JSONObject) parser.parse(objrecvd);
         

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
         
         Socket client = new Socket(serverName, port);
         
         OutputStream outToServer = client.getOutputStream();
         ObjectOutputStream out = new ObjectOutputStream(outToServer);
         

         JSONObject obj = new JSONObject();
         obj.put("Function","getPicturesInGroup");
         obj.put("groupId",groupId);
         
         String objstr = obj.toString();
         
         JSONParser parser = new JSONParser();
         JSONObject json = (JSONObject) parser.parse(objstr);
         
         out.writeObject(objstr);

         
         InputStream inFromServer = client.getInputStream();
         ObjectInputStream in = new ObjectInputStream(inFromServer);
         
         String objrecvd = (String)in.readObject();
         
         JSONObject jsonrecvd = (JSONObject) parser.parse(objrecvd);
         
         JSONArray jarray = (JSONArray)jsonrecvd.get("answer");
         Vector<String> answer = new Vector<String>();
         for(int i=0;i<jarray.size();i++)
         {
            answer.add((String)jarray.get(i));
         }

         client.close();
         return answer;
      } catch (Exception e) {
         e.printStackTrace();
      }
      return (new Vector<String>());
   }

   public static int createAlbumServer(String albumName, int userId) {

      try {
         
         Socket client = new Socket(serverName, port);
         
         OutputStream outToServer = client.getOutputStream();
         ObjectOutputStream out = new ObjectOutputStream(outToServer);
         

         JSONObject obj = new JSONObject();
         obj.put("Function","createAlbumServer");
         obj.put("albumName",albumName);
         obj.put("userId",userId);
         
         String objstr = obj.toString();
         
         JSONParser parser = new JSONParser();
         JSONObject json = (JSONObject) parser.parse(objstr);
         
         out.writeObject(objstr);

         InputStream inFromServer = client.getInputStream();
         ObjectInputStream in = new ObjectInputStream(inFromServer);
         
         String objrecvd = (String)in.readObject();
         
         JSONObject jsonrecvd = (JSONObject) parser.parse(objrecvd);
         

         int answer = (int)(long)jsonrecvd.get("answer");

         client.close();
         return answer;
      } catch (Exception e) {
         e.printStackTrace();
      }
      return 0;
   }

   public static String getAlbumNameFromAlbumId(int albumId) {

      try {
         
         Socket client = new Socket(serverName, port);
         OutputStream outToServer = client.getOutputStream();
         ObjectOutputStream out = new ObjectOutputStream(outToServer);

         JSONObject obj = new JSONObject();
         obj.put("Function","createAlbumServer");
         obj.put("albumId",albumId);
         
         String objstr = obj.toString();
         JSONParser parser = new JSONParser();
         JSONObject json = (JSONObject) parser.parse(objstr);
         out.writeObject(objstr);
         
         InputStream inFromServer = client.getInputStream();
         ObjectInputStream in = new ObjectInputStream(inFromServer);
         
         String objrecvd = (String)in.readObject();
         
         JSONObject jsonrecvd = (JSONObject) parser.parse(objrecvd);
         

         String answer = (String)jsonrecvd.get("answer");

         client.close();
         return answer;
      } catch (Exception e) {
         e.printStackTrace();
      }
      return null;
   }

   public static boolean addPicturesToAlbum(Vector<Integer> picIds, int albumId)
   {
         try {
         
         Socket client = new Socket(serverName, port);
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
         
         JSONParser parser = new JSONParser();
         JSONObject json = (JSONObject) parser.parse(objstr);
         JSONArray jarray = (JSONArray)json.get("userIds");
         out.writeObject(objstr);

         
         InputStream inFromServer = client.getInputStream();
         ObjectInputStream in = new ObjectInputStream(inFromServer);
         
         String objrecvd = (String)in.readObject();
         
         JSONObject jsonrecvd = (JSONObject) parser.parse(objrecvd);
         

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
         
         Socket client = new Socket(serverName, port);
         
         OutputStream outToServer = client.getOutputStream();
         ObjectOutputStream out = new ObjectOutputStream(outToServer);
         

         JSONObject obj = new JSONObject();
         obj.put("Function","getPicturesInAlbum");
        
         obj.put("albumId",albumId);
         
         String objstr = obj.toString();
         
         JSONParser parser = new JSONParser();
         JSONObject json = (JSONObject) parser.parse(objstr);
         out.writeObject(objstr);

         
         InputStream inFromServer = client.getInputStream();
         ObjectInputStream in = new ObjectInputStream(inFromServer);
         
         String objrecvd = (String)in.readObject();
         
         JSONObject jsonrecvd = (JSONObject) parser.parse(objrecvd);
         

         JSONArray jarray = (JSONArray)jsonrecvd.get("answer");
         
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
         
         Socket client = new Socket(serverName, port);
         
         OutputStream outToServer = client.getOutputStream();
         ObjectOutputStream out = new ObjectOutputStream(outToServer);
         
         JSONObject obj = new JSONObject();
         obj.put("Function","shareAlbumWithGroup");
         obj.put("albumId",albumId);
         obj.put("groupId",groupId);
         
         String objstr = obj.toString();
         
         JSONParser parser = new JSONParser();
         JSONObject json = (JSONObject) parser.parse(objstr);
         
         out.writeObject(objstr);

         
         InputStream inFromServer = client.getInputStream();
         ObjectInputStream in = new ObjectInputStream(inFromServer);
         
         String objrecvd = (String)in.readObject();
         
         JSONObject jsonrecvd = (JSONObject) parser.parse(objrecvd);
         
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
         
         Socket client = new Socket(serverName, port);
         
         OutputStream outToServer = client.getOutputStream();
         ObjectOutputStream out = new ObjectOutputStream(outToServer);
         
         JSONObject obj = new JSONObject();
         obj.put("Function","getAlbumsInGroup");
        
         obj.put("groupId",groupId);
         
         String objstr = obj.toString();
         
         JSONParser parser = new JSONParser();
         JSONObject json = (JSONObject) parser.parse(objstr);
         out.writeObject(objstr);

         
         InputStream inFromServer = client.getInputStream();
         ObjectInputStream in = new ObjectInputStream(inFromServer);
         
         String objrecvd = (String)in.readObject();
         
         JSONObject jsonrecvd = (JSONObject) parser.parse(objrecvd);
         

         JSONArray jarray = (JSONArray)jsonrecvd.get("answer");
         
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