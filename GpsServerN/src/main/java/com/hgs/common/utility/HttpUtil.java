package com.hgs.common.utility;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class HttpUtil {

	public static String httpPost(String url, Map<String, String> params) {

		URL u = null;
		HttpURLConnection con = null;
		// 构建请求参数
		StringBuffer sb = new StringBuffer();

		if (params != null) {

			for (Entry<String, String> e : params.entrySet()) {
				sb.append(e.getKey());
				sb.append("=");
				sb.append(e.getValue());
				sb.append("&");
			}
			sb.substring(0, sb.length() - 1);
		}
		//
		// System.out.println("send_url:"+url);
		// System.out.println("send_data:"+sb.toString());

		// 尝试发送请求
		try {
			u = new URL(url);
			con = (HttpURLConnection) u.openConnection();
			con.setRequestMethod("POST");
			con.setDoOutput(true);
			con.setDoInput(true);
			con.setUseCaches(false);
			// setConnectTimeout：设置连接主机超时（单位：毫秒）
			// setReadTimeout：设置从主机读取数据超时（单位：毫秒）
			con.setConnectTimeout(30000);// 默认30秒
			con.setReadTimeout(30000);// 默认30秒
			con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			OutputStreamWriter osw = new OutputStreamWriter(con.getOutputStream(), "GBK");
			osw.write(sb.toString());
			osw.flush();
			osw.close();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (con != null) {
				con.disconnect();
			}
		}

		// 读取返回内容
		StringBuffer buffer = new StringBuffer();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
			String temp;
			while ((temp = br.readLine()) != null) {
				buffer.append(temp);
				buffer.append("\n");
			}
		} catch (Exception e) {

			e.printStackTrace();
		}
		return buffer.toString();
	}
	
	public static void getRemoteId(List<String> list, List<String> nameList) throws IOException{
        String add_url = "http://202.100.171.177:8080/yjbjdjj/intercom/deviceManage.do";
            URL url = new URL(add_url);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            connection.connect();
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            JSONObject obj = new JSONObject();
             
            
            JSONArray ja = new JSONArray();
            
            for(int i=0;i<list.size();i++){
            	 JSONObject inner = new JSONObject();
            	 inner.put("areaCode", "000");
            	 inner.put("code", list.get(i));
            	 inner.put("name", nameList.get(i));
            	 ja.add(inner);
            }
            obj.put("data", ja);
            obj.put("csbh", "102-1");//厂商编号
            obj.put("operType", "1");
            
            
            System.out.println(obj.toString());
            
            out.writeBytes(obj.toString());
            out.flush();
            out.close();
             
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String lines;
            StringBuffer sbf = new StringBuffer();
             while ((lines = reader.readLine()) != null) {
                    lines = new String(lines.getBytes(), "utf-8");
                    sbf.append(lines);
                }
                System.out.println(sbf);
                reader.close();
                // 断开连接
                connection.disconnect();
    }
	
	public static void main(String[] args) {
		/*String url = "http://202.100.177.151:8081/BPHGps_Handle/wjgps/wjGpsPush.do";
		Map<String, String> map = new HashMap<String, String>();
		map.put("gpsId", "12345");
		map.put("gpsX", "87.6054493210124");
		map.put("gpsY", "43.800251846583");
		System.out.println(HttpUtil.httpPost(url, map));*/
		List<String> list = new ArrayList<String>();
		List<String> nameList = new ArrayList<String>();
		list.add("171011209");
		nameList.add("171011209");
		
		list.add("161121884");
		nameList.add("161121884");
		
		list.add("161121865");
		nameList.add("161121865");
		
		list.add("171020007");
		nameList.add("171020007");

		list.add("161121908");
		nameList.add("161121908");
		
		list.add("161121910");
		nameList.add("161121910");
		
		list.add("161121915");
		nameList.add("161121915");
		
		list.add("161121920");
		nameList.add("161121920");
		
		list.add("161121919");
		nameList.add("161121919");
		
		list.add("161121961");
		nameList.add("161121961");
		
		list.add("161121960");
		nameList.add("161121960");
		
		list.add("171010962");
		nameList.add("171010962");
		
		list.add("161121902");
		nameList.add("161121902");
		
		list.add("161121907");
		nameList.add("161121907");
		
		list.add("161121917");
		nameList.add("161121917");
		
		list.add("161121962");
		nameList.add("161121962");
		
		list.add("161121985");
		nameList.add("161121985");
		
		list.add("1710120144");
		nameList.add("1710120144");
		
		list.add("161121881");
		nameList.add("161121881");
		
		list.add("161121943");
		nameList.add("161121943");
		
		list.add("171020102");
		nameList.add("171020102");
		
		list.add("161121954");
		nameList.add("161121954");
		
		list.add("171020090");
		nameList.add("171020090");
		
		list.add("171092995");
		nameList.add("171092995");
		
		list.add("171091598");
		nameList.add("171091598");
		
		list.add("171092982");
		nameList.add("171092982");
		
		try {
			HttpUtil.getRemoteId(list, nameList);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
