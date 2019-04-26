import java.util.*;
import java.sql.*;  
import java.nio.charset.*; 

class RandomString { 
  
    static String getAlphaNumericString(int n) 
    { 
        // length is bounded by 256 Character 
        byte[] array = new byte[256]; 
        new Random().nextBytes(array); 
  
        String randomString 
            = new String(array, Charset.forName("UTF-8")); 
  
        // Create a StringBuffer to store the result 
        StringBuffer r = new StringBuffer(); 
  
        // remove all spacial char 
        String  AlphaNumericString 
            = randomString 
                  .replaceAll("[^A-Za-z0-9.]", ""); 
  
        // Append first 20 alphanumeric characters 
        // from the generated random String into the result 
        for (int k = 0; k < AlphaNumericString.length(); k++) { 
  
            if (Character.isLetter(AlphaNumericString.charAt(k)) 
                    && (n > 0) 
                || Character.isDigit(AlphaNumericString.charAt(k)) 
                       && (n > 0)) { 
  
                r.append(AlphaNumericString.charAt(k)); 
                n--; 
            } 
        } 
        // return the resultant string 
        return r.toString(); 
    } 
}

class TestPicit{
	User user;
	Group group;
	Picture picture;
	Album album;
	Picit picit;

	TestPicit(User user, Group group, Picture picture, Album album, Picit picit){
		this.user = user;
		this.group = group;
		this.picture = picture;
		this.album = album;
		this.picit = picit;
	}

	void testPicit(){
		deleteAllData();
		int numberOfUsers = 10;
		testCreateUser(numberOfUsers);
		int numberOfGroups = 2;
		testCreateGroup(numberOfUsers, numberOfGroups);
		int newGroups = 2;
		testAddUserToGroupRemoveUserFromGroup(numberOfUsers, numberOfGroups, newGroups);
		numberOfGroups += newGroups;
		testGetGroupsOfUser(numberOfUsers, numberOfGroups);

		int numberOfPicturesForEachUser = 2;
		testPicturesAndAlbums(numberOfUsers, numberOfPicturesForEachUser);
	}

	boolean deleteAllData(){
		Random rand = new Random();
		try{
			Statement statement = picit.con.createStatement();  
			statement.executeUpdate("delete from UserInGroups");
			statement.executeUpdate("delete from SharedPictures");
			statement.executeUpdate("delete from SharedAlbums");
			statement.executeUpdate("delete from PicturesInAlbums");
			statement.executeUpdate("delete from Albums");
			statement.executeUpdate("delete from Pictures");
			statement.executeUpdate("delete from Groups");
			statement.executeUpdate("delete from Users");
		}catch(Exception e){
			System.out.println("deleteAllData FAILED!!!\n"+e);
			return false;
		} 
		return true; 
	}
	boolean testCreateUser(int numberOfUsers){
		Vector<String> emailIds = new Vector<String>();
		for(int i=0;i<numberOfUsers;++i){
			try{ 
				String emailId = RandomString.getAlphaNumericString(20);
				String userName = RandomString.getAlphaNumericString(20);				
				int userId = user.createUser(emailId,userName);
				emailIds.add(emailId);
				user.getUseridsFromEmailids(emailIds);

				Statement statement = picit.con.createStatement();
				ResultSet rs = statement.executeQuery("select * from Users where userId = "+userId);
				while(rs.next())  
				System.out.println(rs.getString(1)+"|"+rs.getString(2)+"|"+rs.getString(3)+"|"+rs.getString(4));  

			}catch(Exception e){
				System.out.println("Test for createUser FAILED!!!");
				e.printStackTrace();
				return false;
			}
		}
		try{
			user.getUseridsFromEmailids(emailIds);
		}catch(Exception e){
			e.printStackTrace();
		}
		return true;
	}

	boolean testCreateGroup(int numberOfUsers, int numberOfGroups){
		Random rand = new Random();

		for(int i=0;i<numberOfGroups;++i){	
			try{ 
				Vector<Integer> v = new Vector<Integer>(); 
				for(int j=1;j<=numberOfUsers;++j){
					int n = rand.nextInt(2);
					if(n==1){
						v.add(j);
					}	
				}
				int users = v.size();
				int[] userIds = new int[users];
				for(int j=0;j<users;++j){
					userIds[j] = (int)v.get(j);
					// System.out.println(userIds[j]);
				}

				Integer creatorUserId = ((int)v.get(rand.nextInt(v.size())));

				String groupName = RandomString.getAlphaNumericString(20);	
				int groupId = group.createGroup(v, creatorUserId, groupName);
				for(int userId : userIds){
					group.setGroupInactive(userId,groupId);	
					group.setGroupActive(userId,groupId);
					group.setGroupInactive(userId,groupId);	
				}
				System.out.println(groupId);

				Statement statement = picit.con.createStatement();
				ResultSet rs = statement.executeQuery("select * from Groups where groupId = "+groupId);
				while(rs.next())  
				System.out.println(rs.getString(1)+"|"+rs.getString(2)+"|"+rs.getString(3));  

			}catch(Exception e){
				System.out.println("Test for createGroup FAILED!!!\n"+e);
				return false;
			}  
		}
		return true;
	}

	boolean testAddUserToGroupRemoveUserFromGroup(int numberOfUsers, int numberOfGroups, int newGroups){
		Random rand = new Random();
		try{
			for(int i=numberOfGroups+1;i<=numberOfGroups+newGroups;++i){	
				int creatorUserId = rand.nextInt(numberOfUsers)+1;
				String groupName = RandomString.getAlphaNumericString(20);
				Vector<Integer> lmao = new Vector<Integer>();
				lmao.add(creatorUserId);
				group.createGroup(lmao, creatorUserId, groupName);
				for(int userId=1;userId<=numberOfUsers;++userId){
					if(userId == creatorUserId)continue;
					group.addUserToGroup(userId,i,false);
					group.removeUserFromGroup(userId,i);
					group.addUserToGroup(userId,i,false);
				}
			}
		}catch(Exception e){
			System.out.println("Test for addUserToGroup and removeUserFromGroup FAILED!!!");
			e.printStackTrace();			
			return false;
		}  
		return true;
	}

	boolean testGetGroupsOfUser(int numberOfUsers, int numberOfGroups){
		Random rand = new Random();
		try{
			for(int i=1;i<=numberOfUsers;++i){	
				Vector<String> groups= group.getGroupsOfUser(i);
				System.out.print("Groups for user "+i+" are: ");
				for (String groupId : groups) {
					System.out.print(groupId + " ");
				}
				System.out.println();
			}
		}catch(Exception e){
			System.out.println("Test for getGroupsOfUser FAILED!!!");
			e.printStackTrace();
			return false;
		}
		return true;
	}

	boolean testPicturesAndAlbums(int numberOfUsers, int numberOfPicturesForEachUser){
		Random rand = new Random();
		try{
			Statement statement = picit.con.createStatement();
			for(int i=1;i<=numberOfUsers;++i){
				int userId = i;	
				Vector<Integer> groupIds = new Vector<Integer>();
				ResultSet rs = statement.executeQuery("select groupId from UserInGroups where userId = "+userId);
				while(rs.next()){
					groupIds.add(rs.getInt(1));
				}  
				// System.out.println(rs.getString(1)+"|"+rs.getString(2)+"|"+rs.getString(3));  

				// int [] picIds = new int [numberOfPicturesForEachUser];
				Vector<Integer> picIds = new Vector<Integer>();
				for(int j=0;j<numberOfPicturesForEachUser;++j){
					// String url = RandomString.getAlphaNumericString(100);				
					int picId = picture.uploadPicture(userId);

					for(int groupId : groupIds){
						// System.out.println(picId+" sharing to "+groupId);
						picture.sharePictureToGroup(picId, userId, groupId);
						// System.out.println(picId+" shared to "+groupId);
					}
					// picIds[j] = picId;
					picIds.add(picId);
				}
				String albumName = RandomString.getAlphaNumericString(30);				
				int albumId = album.createAlbumServer(albumName, userId);
				album.addPicturesToAlbum(picIds,albumId);
				album.getPicturesInAlbum(albumId);
				for(int groupId : groupIds){
					// System.out.println("album="+albumId+" sharing to "+groupId);
					album.shareAlbumWithGroup(albumId,groupId);
					album.getAlbumsInGroup(groupId);
					// System.out.println("album="+albumId+" shared to "+groupId);
				}
			}
		}catch(Exception e){
			System.out.println("Test for uploadPicture, createAlbum, addPicturesToAlbum, getPicturesInAlbum FAILED!!!");
			e.printStackTrace();
			return false;
		}
		return true;
	}
}

class Picit{  
	Connection con;

	int userIdMax;
	int groupIdMax;
	int picIdMax;
	int albumIdMax;

	User user;
	Group group;
	Picture picture;
	Album album;

	Picit(){
		this.user = new User(this);
		this.group = new Group(this);
		this.picture = new Picture(this);
		this.album = new Album(this);
	}
}

class PicitMain{
	// public static void main(String args[]){  
	// 	String url="jdbc:mysql://localhost:3306/picit";		//3306 is the default port
	// 	String userName="prakhar10_10";
	// 	String password="lmaolmao";
	// 	// String url="jdbc:mysql://picit.cdefe3kkdqar.ap-south-1.rds.amazonaws.com:3306/picit";
	// 	// String userName="admin";
	// 	// String password="qwerty1234";

	// 	Picit picit = new Picit();
	// 	TestPicit testPicit = new TestPicit(picit.user, picit.group, picit.picture, picit.album, picit);

	// 	try{  
	// 		Class.forName("com.mysql.jdbc.Driver");  
	// 		picit.con = DriverManager.getConnection(url, userName, password);  

	// 		/*don't do this in reality!!*/
	// 		testPicit.deleteAllData();		

	// 		Statement stmt = picit.con.createStatement();  
	// 		ResultSet rs;

	// 		rs = stmt.executeQuery("select max(userId) from Users");
	// 		rs.next(); 
	// 		picit.userIdMax = rs.getInt(1) + 1;//0 if no users 

	// 		rs = stmt.executeQuery("select max(groupId) from Groups");  
	// 		rs.next(); 
	// 		picit.groupIdMax = rs.getInt(1) + 1;

	// 		rs = stmt.executeQuery("select max(picId) from Pictures");  
	// 		rs.next(); 
	// 		picit.picIdMax = rs.getInt(1) + 1;

	// 		rs = stmt.executeQuery("select max(albumId) from Albums");  
	// 		rs.next(); 
	// 		picit.albumIdMax = rs.getInt(1) + 1;

	// 		System.out.println(picit.userIdMax+", "+picit.groupIdMax+", "+picit.picIdMax+", "+picit.albumIdMax);


	// 		testPicit.testPicit();
			
	// 		picit.con.close();  
	// 	}catch(Exception e){
	// 	 System.out.println(e);
	// 	}  
	// }
}

class User{
	Picit picit;
	User(Picit picit){
		this.picit = picit;
	}

	//returns userId 
	int createUser(String emailId, String userName) throws SQLException {
		Statement stmt = picit.con.createStatement();  
		int rowsAffected = stmt.executeUpdate("insert into Users (userId, emailId, userName) values ('"+picit.userIdMax+"','"+emailId+"','"+userName+"');");
		return picit.userIdMax++;
	}

	Vector<Integer> getUseridsFromEmailids(Vector<String> emailIds) throws SQLException {
		Statement statement = picit.con.createStatement();
		Vector<Integer> userIds = new Vector<Integer>();
		for(String emailId : emailIds){
			ResultSet rs = statement.executeQuery("select userId from Users where (emailId='"+emailId+"');");
			if(rs.next()){
				int userId = rs.getInt(1);
				userIds.add(userId);
			}	
		}
		return userIds;
	}	
}

class Group{
	Picit picit;
	Group(Picit picit){
		this.picit = picit;
	}

	//returns groupId
	//creatorUserId should be in userIds
	int createGroup(Vector<Integer> userIds, int creatorUserId, String groupName) throws SQLException {
		Statement statement = picit.con.createStatement();  
		statement.executeUpdate("insert into Groups (groupId,creatorUserId,groupName) values ('"+picit.groupIdMax+"','"+creatorUserId+"','"+groupName+"');");			
		
		for(int userId : userIds){
			statement.executeUpdate("insert into UserInGroups (userId,groupId,isActive) values ('"+userId+"','"+picit.groupIdMax+"','"+0+"');");	
		}

		return picit.groupIdMax++;
	}	

	//returns true or false
	boolean addUserToGroup(int userId, int groupId, boolean isActive) throws SQLException {
		Statement statement = picit.con.createStatement(); 
		int intisActive = isActive?1:0; 
		statement.executeUpdate("insert into UserInGroups (userId,groupId,isActive) values ('"+userId+"','"+groupId+"','"+intisActive+"');");			
		return true;
	}

	//returns true or false
	boolean removeUserFromGroup(int userId, int groupId) throws SQLException {
		Statement statement = picit.con.createStatement();  
		statement.executeUpdate("delete from UserInGroups where (userId='"+userId+"' and groupId='"+groupId+"');");			
		return true;
	}

	boolean setGroupActive(int userId, int groupId) throws SQLException {
		Statement statement = picit.con.createStatement(); 
		statement.executeUpdate("update UserInGroups set isActive='1' where (userId='"+userId+"' and groupId='"+groupId+"');");			
		return true;
	}

	boolean setGroupInactive(int userId, int groupId) throws SQLException {
		Statement statement = picit.con.createStatement(); 
		statement.executeUpdate("update UserInGroups set isActive='0' where (userId='"+userId+"' and groupId='"+groupId+"');");			
		return true;
	}

	//returns array of "groupId,groupName,0/1"
	Vector<String> getGroupsOfUser(int userId) throws SQLException {
		Vector<String> groups = new Vector<String>();
		Statement statement = picit.con.createStatement();  
		ResultSet rs = statement.executeQuery("select groupId,isActive from UserInGroups where (userId='"+userId+"');");			

		while(rs.next()){
			int groupId = rs.getInt(1);
			int isActive = rs.getInt(2);

			Statement statement2 = picit.con.createStatement();  
			ResultSet rs2 = statement2.executeQuery("select groupName from Groups where (groupId='"+groupId+"');");			
			rs2.next();
			String groupName = rs2.getString(1);
			groups.add(groupId+","+groupName+","+isActive);
		}
		return groups;
	}
}

class Picture{
	Picit picit;
	Picture(Picit picit){
		this.picit = picit;
	}

	//returns picId
	int uploadPicture(int userId) throws SQLException {
		Statement statement = picit.con.createStatement();  
		statement.executeUpdate("insert into Pictures (picId,userId) values ('"+picit.picIdMax+"','"+userId+"');");			
		return picit.picIdMax++;
	}

	//picId should have been clicked by userId
	//returns true or false 
	boolean sharePictureToGroup(int picId, int userId, int groupId) throws SQLException {
		Statement statement = picit.con.createStatement();  
		statement.executeUpdate("insert into SharedPictures (picId,userId,groupId) values ('"+picId+"','"+userId+"','"+groupId+"');");
		return true;
	}

	//returns array of "picId,userId"
	Vector<String> getPicturesInGroup(int groupId) throws SQLException {
		Vector<String> pictures = new Vector<String>();
		Statement statement = picit.con.createStatement();  
		ResultSet rs = statement.executeQuery("select picId,userId from SharedPictures where (groupId='"+groupId+"');");			
		while(rs.next()){
			int picId = rs.getInt(1);
			int userId = rs.getInt(2);
			pictures.add(picId+","+userId);
		}
		return pictures;
	}
}

class Album{
	Picit picit;
	Album(Picit picit){
		this.picit = picit;
	}

	//return albumId
	int createAlbumServer(String albumName, int userId) throws SQLException {
		Statement statement = picit.con.createStatement();  
		statement.executeUpdate("insert into Albums (albumId,albumName,userId) values ('"+picit.albumIdMax+"','"+albumName+"','"+userId+"');");
		return picit.albumIdMax++;
	}

	boolean addPicturesToAlbum(Vector<Integer> picIds, int albumId) throws SQLException {
		Statement statement = picit.con.createStatement();  
		for(int picId : picIds){
			try{
				statement.executeUpdate("insert into PicturesInAlbums (albumId,picId) values ('"+albumId+"','"+picId+"');");
			}catch(Exception e){
				System.out.println("Error in addPicturesToAlbum with picId="+picId+" and albumId="+albumId);
				e.printStackTrace();
			} 
		}
		return true;			
	}

	Vector<Integer> getPicturesInAlbum(int albumId) throws SQLException {
		Vector<Integer> pictures = new Vector<Integer>();
		Statement statement = picit.con.createStatement();  
		ResultSet rs3 = statement.executeQuery("select picId from PicturesInAlbums where (albumId='"+albumId+"');");			
		ResultSet rs2;
		while(rs3.next()){
			int picId = rs3.getInt(1);
			pictures.add(picId);
		}
		return pictures;
	}

	boolean shareAlbumWithGroup(int albumId, int groupId) throws SQLException {
		Statement statement = picit.con.createStatement();  
		// ResultSet rs = statement.executeQuery("select userId from UserInGroups where (groupId='"+groupId+"');");			
		// Vector<Integer> userIds = new Vector<Integer>();
		// while(rs.next()){
		// 	int userId = rs.getInt(1);
		// 	userIds.add(userId);
		// }
		// for(int userId : userIds){
			try{
				statement.executeUpdate("insert into SharedAlbums (albumId,groupId) values ('"+albumId+"','"+groupId+"');");
			}catch(Exception e){
				System.out.println("Error in shareAlbumWithGroup with groupId="+groupId);
				e.printStackTrace();
			} 
		// }
		return true;
	}	

	Vector<Integer> getAlbumsInGroup(int groupId) throws SQLException {
		Statement statement = picit.con.createStatement();  
		ResultSet rs = statement.executeQuery("select albumId from SharedAlbums where (groupId='"+groupId+"');");			
		Vector<Integer> albumIds = new Vector<Integer>();
		while(rs.next()){
			int albumId = rs.getInt(1);
			albumIds.add(albumId);
		}
		return albumIds;
	}
}