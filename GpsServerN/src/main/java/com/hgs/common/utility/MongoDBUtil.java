package com.hgs.common.utility;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientOptions.Builder;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

public class MongoDBUtil {
	
	private static Logger logger = LoggerFactory.getLogger(MongoDBUtil.class);
	private static MongoClient mongoClient;
	private static MongoDatabase database;
	private static MongoCollection<Document> collection;
	
	public synchronized static void init(){
		logger.debug("=============MongoDbUtil初始化============");
		ServerAddress serverAddress = new ServerAddress("127.0.0.1",27017);
		
		/*MongoCredential credential = MongoCredential.createCredential(AttributeUtil.getMongodbAccount(), "admin", AttributeUtil.getMongodbPwd().toCharArray());
		List<MongoCredential> credehtials = new ArrayList<MongoCredential>();
		credehtials.add(credential);*/
		
		//连接到数据库
		Builder options = new MongoClientOptions.Builder();
		options.connectionsPerHost(1000);// 连接池设置为300个连接,默认为100
        options.connectTimeout(15000);// 连接超时，推荐>3000毫秒
        options.maxWaitTime(10000); //
        //options.socketTimeout(0);// 套接字超时时间，0无限制
        options.threadsAllowedToBlockForConnectionMultiplier(10);// 线程队列数，如果连接线程排满了队列就会抛出“Out of semaphores to get db”错误。
        MongoClientOptions build = options.build();
        mongoClient = new MongoClient(serverAddress, build);
        
        database = mongoClient.getDatabase("gps_history");
		collection = database.getCollection("gps_history");
	}
	
	/***
	 * 插入单个历史数据
	 */
	public static void saveOne(int id, float lat, float lng, Date time, float speed, int direction, int positionlogo, int mileage){
		if(time == null){
			time = new Date();
		}
		Document document = new Document("k01_user_id",id).
				append("latitude", lat).
				append("longitude", lng).
				append("gps_arrive_time", time).
				append("speed", speed).
				append("direction", direction).
				append("positionlogo", positionlogo).
				append("mileage", mileage);
		collection.insertOne(document);
	}
	
	public static void close() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }
}
