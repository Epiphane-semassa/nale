package com.nale.newslettermanagement.data;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.nale.newslettermanagement.model.Bulletin;
import com.nale.newslettermanagement.model.Follower;
import com.nale.newslettermanagement.model.Message;

public class FileSystemStorage {

	 private final Path rootLocation;
	 private final Path creatorLocation;
	 private final Path followersLocation;
	 private final Path bulletinsLocation;
	 private final Path messagesLocation;
	 private static final String CREATOR_CONFIG_FILE = "creator-admin.conf";
	 private static final String FOLLOWER_DATA_FILE = "followers.conf";
	 private static final String BULLETIN_DATA_FILE = "bulletins.conf";
	 private static final String MESSAGE_DATA_FILE = "messages.conf";

	 private static FileSystemStorage fileSystemStorage;

	    private FileSystemStorage() {
	        StorageProperties properties = StorageProperties.getInstance();
	        this.rootLocation = Paths.get(properties.getLocation());
	        this.creatorLocation = this.rootLocation.resolve(CREATOR_CONFIG_FILE);
	        this.followersLocation = this.rootLocation.resolve(FOLLOWER_DATA_FILE);
	        this.bulletinsLocation = this.rootLocation.resolve(BULLETIN_DATA_FILE);
	        this.messagesLocation = this.rootLocation.resolve(MESSAGE_DATA_FILE);
	    }

	    public static FileSystemStorage getInstance() {
	        if(fileSystemStorage==null)
	        	fileSystemStorage = new FileSystemStorage();

	        return fileSystemStorage;
	    }

	    public void init() {
	        try {
	        	if(!Files.exists(rootLocation)) {
	        		Files.createDirectory(rootLocation);
					createDataFiles();
	        	} else {
					createDataFiles();
					System.out.println("App Directory found!!");
	        	}

	        } catch (IOException e) {
	        	System.out.println(e.getMessage());
	            System.out.println("Could not initialize storage");
	        }
	    }

		private void createDataFiles() throws IOException {
			if(!Files.exists(creatorLocation)) Files.createFile(creatorLocation);
			if(!Files.exists(followersLocation)) Files.createFile(followersLocation);
			if(!Files.exists(bulletinsLocation)) Files.createFile(bulletinsLocation);
			if(!Files.exists(messagesLocation)) Files.createFile(messagesLocation);
		}

	    public boolean saveCreatorAccount(Admin admin) {
	        Gson gson = new GsonBuilder().setPrettyPrinting().create();
	        Path config = this.rootLocation.resolve(CREATOR_CONFIG_FILE);

	            try {
	                if(!Files.exists(config)){
	                    Files.createFile(config);
	                }
	                Files.writeString(config, gson.toJson(admin));
	                return true;
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	            return false;
	    }

	    public Admin loadCreatorAccount() {
	        Path config = this.rootLocation.resolve(CREATOR_CONFIG_FILE);
	        try {
	            String configData = Files.readString(config);
				Gson gson = new GsonBuilder().setPrettyPrinting().create();
				return gson.fromJson(configData, Admin.class);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        return null;
	    }


	    public boolean saveFollower(Follower follower) {
	        Gson gson = new GsonBuilder().setPrettyPrinting().create();
	        Path path = this.rootLocation.resolve(FOLLOWER_DATA_FILE);

	            try {
	                if(!Files.exists(path)){
	                    Files.createFile(path);
	                }
	                Set<Follower> followers = loadFollowers();
	                followers.add(follower);
	                Files.writeString(path, gson.toJson(followers));
	                return true;
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	            return false;
	    }

	    public boolean saveFollowers(Set<Follower> followers) {
	        Gson gson = new GsonBuilder().setPrettyPrinting().create();
	        Path path = this.rootLocation.resolve(FOLLOWER_DATA_FILE);

	            try {
	                if(!Files.exists(path)){
	                    Files.createFile(path);
	                }
	                Files.writeString(path, gson.toJson(followers));
	                return true;
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	            return false;
	    }

	    public Set<Follower> loadFollowers() {
	        Path path = this.rootLocation.resolve(FOLLOWER_DATA_FILE);
	        try {
	            String data = Files.readString(path);
	            if(data.isEmpty()) {
	            	return new HashSet<>();
	            }else {
	            	Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    Type typeMyType = new TypeToken<Set<Follower>>(){}.getType();
                    Set<Follower> followers = gson.fromJson(data, typeMyType);
		            return new HashSet<>(followers);
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        return new HashSet<>();
	    }

	    public boolean saveBulletin(Bulletin bulletin) {
	        Gson gson = new GsonBuilder().setPrettyPrinting().create();
	        Path path = this.rootLocation.resolve(BULLETIN_DATA_FILE);
	            try {
	                if(!Files.exists(path)){
	                    Files.createFile(path);
	                }
	                Set<Bulletin> bulletins = loadBulletins();
	                bulletins.add(bulletin);
                    String data = gson.toJson(bulletins);
	                Files.writeString(path, data);
	                return true;
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	            return false;
	    }

	    public boolean saveBulletins(Set<Bulletin> bulletins) {
	        Gson gson = new GsonBuilder().setPrettyPrinting().create();
	        Path path = this.rootLocation.resolve(BULLETIN_DATA_FILE);

	            try {
	                if(!Files.exists(path)){
	                    Files.createFile(path);
	                }
	                Files.writeString(path, gson.toJson(bulletins));
	                return true;
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	            return false;
	    }

	    public Set<Bulletin> loadBulletins() {
	        Path path = this.rootLocation.resolve(BULLETIN_DATA_FILE);
	        try {
	            String data = Files.readString(path);
	            if(data.isEmpty() || data.isBlank()) {
	            	return new HashSet<>();
	            }else {
	            	Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    Type typeMyType = new TypeToken<Set<Bulletin>>(){}.getType();
					Set<Bulletin> bulletins = gson.fromJson(data, typeMyType);
		            return new HashSet<>(bulletins);
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        return new HashSet<>();
	    }

	    public boolean saveMessage(Message message) {
	        Gson gson = new GsonBuilder().setPrettyPrinting().create();
	        Path path = this.rootLocation.resolve(MESSAGE_DATA_FILE);

	            try {
	                if(!Files.exists(path)){
	                    Files.createFile(path);
	                }
	                Set<Message> messages = loadMessages();
	                messages.add(message);
	                Files.writeString(path, gson.toJson(messages));
	                return true;
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	            return false;
	    }

	    public boolean saveMessages(Set<Message> messages) {
	        Gson gson = new GsonBuilder().setPrettyPrinting().create();
	        Path path = this.rootLocation.resolve(MESSAGE_DATA_FILE);

	            try {
	                if(!Files.exists(path)){
	                    Files.createFile(path);
	                }
	                Files.writeString(path, gson.toJson(messages));
	                return true;
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	            return false;
	    }

	    public Set<Message> loadMessages() {
	        Path path = this.rootLocation.resolve(MESSAGE_DATA_FILE);
	        try {
	            String data = Files.readString(path);
	            if(data.isEmpty() || data.isBlank()) {
	            	return new HashSet<>();
	            }else {
	            	Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    Type typeMyType = new TypeToken<Set<Message>>(){}.getType();
                    Set<Message> messages = gson.fromJson(data, typeMyType);
		            return new HashSet<>(messages);
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        return new HashSet<Message>();
	    }



}
