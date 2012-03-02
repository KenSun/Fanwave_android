package com.wildmind.fanwave.guide;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.content.Context;
import android.util.TypedValue;
import android.widget.TableRow;

import com.wildmind.fanwave.activity.GuideActivity;
import com.wildmind.fanwave.device.DeviceManager;
import com.wildmind.fanwave.network.NetworkManager;
import com.wildmind.fanwave.program.TVProgram;
import com.wildmind.fanwave.util.StringGenerator;
import com.wildmind.fanwave.vendor.VendorManager;


public class EpgManager  {
	
	private static CopyOnWriteArrayList<ProgramListAdapterListener> pgladapter_Listener = new CopyOnWriteArrayList<ProgramListAdapterListener>();
	private static CopyOnWriteArrayList<ChangeAdapterListener> changeapter_Listener = new CopyOnWriteArrayList<ChangeAdapterListener>();
	private static ConcurrentHashMap<String, ArrayList<TVProgram>> ListTVProgram_list = new ConcurrentHashMap<String, ArrayList<TVProgram>>();
	private static ArrayList<Double>  listtvprogramkey = new ArrayList<Double>();

	private static final int LOADER_THREAD_COUNT = 3;
	private static final int DB_THREAD_COUNT = 1;

	private static ArrayList<DownloadProgramLoader> downloadpg_loader_threads = new ArrayList<DownloadProgramLoader>();
	private static ArrayList<WritableDbLoader> writableDb_loader_threads = new ArrayList<WritableDbLoader>();
	private static Stack<DownloadProgramRequest> downloadpg_requests = new Stack<DownloadProgramRequest>();
	private static Stack<WritableDbRequest> writableDb_requests = new Stack<WritableDbRequest>();
	private static ConcurrentHashMap<String, String> downloadpg_request_list = new ConcurrentHashMap<String, String>();
	private static ConcurrentHashMap<String, String> writabledb_request_list = new ConcurrentHashMap<String, String>();
	private static ArrayList<TVChannel> listTVChannels = null;
	private static String userselectTime = null;


	
	public static void setlistTVChannels(ArrayList<TVChannel> listTVC){
		listTVChannels = listTVC;
	}
	public static ArrayList<TVChannel> getlistTVChannels(){
		return listTVChannels;
	}
	
	public static void setUserSelectTime(String ustime){
		userselectTime = ustime;
	}
	public static String getUserSelectTime(){
		return userselectTime;
	}
	
	public static ConcurrentHashMap<String, ArrayList<TVProgram>> getListTVProgramlist(){
		return ListTVProgram_list;
	}
	
	static {
		for (int i = 0; i < LOADER_THREAD_COUNT; i++) {
			DownloadProgramLoader downloadpg = new DownloadProgramLoader();
			downloadpg.setPriority(Thread.NORM_PRIORITY-1);
			downloadpg_loader_threads.add(downloadpg);
			downloadpg.start();
		}
	}
	
	static {
		for (int i = 0; i < DB_THREAD_COUNT; i++) {
			WritableDbLoader writabledbl = new WritableDbLoader();
			writabledbl.setPriority(Thread.NORM_PRIORITY-1);
			writableDb_loader_threads.add(writabledbl);
			writabledbl.start();
		}
	}	
	
	/**
	 * Get PglistUrl .
	 * 
	 * header
	 * @param country
	 * @param timezone
	 * @param username
	 * @param jid
	 * 
	 * body
	 * @param start
	 * @param end
	 * @param postcode
	 * @return ArrayList<TVProgram>
	 * 
	 */
	 public static ArrayList<TVProgram> download_PglistUrl(String Start, String End){
		ArrayList<TVProgram> listTVProgram = new ArrayList<TVProgram>();
		Start = StringGenerator.timeStringShiftWithHours(Start, -Integer.valueOf(DeviceManager.getTimezone())); 
		End   = StringGenerator.timeStringShiftWithHours(End  , -Integer.valueOf(DeviceManager.getTimezone())); 
			
		 
		JSONObject obj;
		HashMap<String, String> response = null;
		HashMap<String, String> bodyMap = new HashMap<String, String>();
		bodyMap.put("start", Start);
		bodyMap.put("end", End);
		bodyMap.put("msoid", VendorManager.getVendorId());
		response = NetworkManager.connectByPost(NetworkManager.getPglistUrl(), null, bodyMap);
		if(response.get("code").equals("200")){
			try {
				obj = new JSONObject(response.get("data"));
				JSONArray Programs = obj.getJSONArray("programs");
				for (int i = 0; i < Programs.length(); i++) {
					JSONObject objj = Programs.getJSONObject(i);
					TVProgram tvpg = new TVProgram(objj, VendorManager.getCountry());
					if(listTVProgram!=null)
						listTVProgram.add(tvpg);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return null;
				}
			}
		return listTVProgram;
	}	
	 
	/**
	 * Add New to Db .
	 * 
	 * @param Context
	 * @param ArrayList<TVProgram>
	 * 
	 */
	public static void AddNewtoDb(ArrayList<TVProgram> listTVProgram)
	{
		programDb Db = new programDb();
		for (int i = 0; i < listTVProgram.size(); i++) {
			TVProgram tvpg = listTVProgram.get(i);
			Db.AddNew(tvpg , VendorManager.getCountry());
		}
		Db.close();		
	}
	
	/**
	 * Add New to Db .
	 * 
	 * @param Context
	 * @param ArrayList<TVProgram>
	 * @return code
	 * 
	 */
	public static ArrayList<TVProgram> AddNewtoDb(JSONObject obj, ArrayList<TVProgram> listTVProgram)
	{
		programDb Db = new programDb();
		for (int i = 0; i < listTVProgram.size(); i++) {
			TVProgram tvpg = listTVProgram.get(i);
			Db.AddNew(tvpg , VendorManager.getCountry());
		}
		Db.close();		
		return listTVProgram;
	}	 
	
	
	
	
	
	/**
	 *  program
	 *  
	 * @param ArrayList<TVChannel>
	 * @param ArrayList<TVProgram>
	 * @param end
	 * @return boolean true = is ok
	 * 				   false = need download new
	 */
	public static boolean Verification(ArrayList<TVProgram> LTVpg, String end){
		 int Vsum = 0;
		 if(listTVChannels!=null){
				for(int i = 0; i < listTVChannels.size(); i++){
					TVChannel TVC = listTVChannels.get(i);
					for(int i2 = 0; i2 < LTVpg.size(); i2++){
						TVProgram TVpg = LTVpg.get(i2);
						if(TVpg.getChannelCode().equals(TVC.getChcode())){
							if(Double.valueOf(TVpg.getEndTime()) >= Double.valueOf(end)){
								Vsum++;
							}

						}
					}
				}
				if((double)Vsum >= (double)listTVChannels.size()*0.8){
					return true;
				}else
					return false;

		 }
		return false;
	 }
	
	
	/**
	 * get city and supplier TVprogram for Db
	 */
	public static ArrayList<TVChannel> getListChannels() {
		ArrayList<TVChannel> channellist = ChannelManager.getSortChannelListByOrder();
		if(listTVChannels!=null)
		listTVChannels.clear();
		
		listTVChannels = channellist;
		return channellist;

	}
	
	public static TableRow.LayoutParams getParams(int width, int height) {
		TableRow.LayoutParams params = new TableRow.LayoutParams(width, height);
		return params;
	}
	
	public static int changedip(int px, Context c){
		return  (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px, c.getResources().getDisplayMetrics());
	}
	
	
	/**
	 * get List TVProgram. If failed, enqueue the request.
	 * @param Time
	 * @return
	 */
	public static boolean getListTVProgramforTime(GuideActivity _context, ProgramListAdapter adapter, String Time) {
		if(listTVChannels == null)
			return false;
		
		String Starttime = Time.substring(0,10)+"00";
		String EndTime = StringGenerator.timeStringShiftWithHours(Starttime, 1);
		programDb Db = new programDb();
		ArrayList<TVProgram> ListTVP = Db.getData(Starttime, EndTime);
		Db.close();
		boolean success = getListProgramsforTime(_context, adapter, ListTVP, EndTime);
		if (!success)
			queueRequest(Starttime);
		return success;
	}

	 public static boolean getListProgramsforTime(GuideActivity _context, final ProgramListAdapter adapter, final ArrayList<TVProgram> LTVpg, String end){
		boolean success = false;

		if(adapter == null)
			return success;
		
		if (Verification(LTVpg, end)) {
			_context.runOnUiThread( new Runnable () {
				public void run () {	
					adapter.refreshData(listTVChannels, LTVpg);
				}
			});
			success = true;
		}
		return success;

	}	
	 
		
		
		
///////////////////////////////////////////////////////////////////////////////////////////////
//									Download Program								         //	
//////////////////////////////////////////////////////////////////////////////////////////////
		
	/**
	 * Enqueue Download Program a  request.
	 * @param Time
	 */
	private static void queueRequest (String Time) {
		if (downloadpg_request_list.containsKey(Time))
			return;

		DownloadProgramRequest dpgr = new DownloadProgramRequest(Time);
		
        synchronized(downloadpg_requests){
        	downloadpg_request_list.put(Time, "");
        	downloadpg_requests.push(dpgr);
        	downloadpg_requests.notify();
        }
	}
	
	/**
	 * Handle Download Program a request.
	 * @param pgbdr
	 */
	private static void handleRequest (DownloadProgramRequest pgbdr) {
		String key = pgbdr.getTime();
		String StartTime = key;
		String EndTime = StringGenerator.timeStringShiftWithHours(key, 1);
		programDb Db = new programDb();
		ArrayList<TVProgram> ListTVP = Db.getData(StartTime, EndTime);
		Db.close();
		if(!EpgManager.Verification(ListTVP, EndTime)){
			ListTVP = null;
			ListTVP = download_PglistUrl(StartTime, EndTime);
			if(ListTVP!= null){
				String Maxtime = StringGenerator.timeStringShiftWithHours(userselectTime, 6);
				String Minaxtime = StringGenerator.timeStringShiftWithHours(userselectTime, -6);
				if(Double.valueOf(userselectTime) < Double.valueOf(Maxtime)||Double.valueOf(userselectTime) > Double.valueOf(Minaxtime))
				{
					ListTVProgram_list.put(key, ListTVP);
					listtvprogramkey.add(Double.valueOf(key));
				}
				if(ListTVProgram_list.size() >= 6){
					for(Double keyS :listtvprogramkey){
						if(keyS > Double.valueOf(Maxtime)||keyS < Double.valueOf(Minaxtime))
							ListTVProgram_list.remove(String.valueOf(keyS));
					}
				}
				
				queueWritableDbRequest(key, ListTVP);
				getViewProgramList(key);
			}
		}
		downloadpg_request_list.remove(key);
	}
	
	/**
	 * Download Program Request Class.
	 * @author Eli
	 *
	 */
	static class DownloadProgramRequest {
		
		private String Time;
		public  DownloadProgramRequest (String Time) {
			this.Time = Time;
		}
		public String getTime () {
			return Time;
		}
	}
	
	/**
	 * Download Program Loader Class.
	 * @author Eli
	 *
	 */
	static class DownloadProgramLoader extends Thread {
		public void run() {
            try {
                while(true)
                {	
                    if(downloadpg_requests.size() == 0) {
                        synchronized(downloadpg_requests) {
                        	downloadpg_requests.wait();
                        }
                    }
                    
                    if(downloadpg_requests.size() > 0) {
                    	DownloadProgramRequest dpgr;
                        synchronized(downloadpg_requests){
                        	dpgr = downloadpg_requests.pop();
                        }
                        handleRequest(dpgr);
                    }
                    if(Thread.interrupted())
                        break;
                }
            } catch (InterruptedException e) {
                //allow thread to exit
            }
        }
	}

		
		
////////////////////////////////////////////////////////////////////
//		Writable     Db 								         //	
///////////////////////////////////////////////////////////////////	
		

	/**
	 * Enqueue Writable Db a  request.
	 * @param Time
	 */
	private static void queueWritableDbRequest (String Time, ArrayList<TVProgram> listTVProgram) {
		if (downloadpg_request_list.containsKey(Time))
			return;

		WritableDbRequest dpgr = new WritableDbRequest(Time, listTVProgram);
		
        synchronized(downloadpg_requests){
        	writabledb_request_list.put(Time, "");
        	writableDb_requests.push(dpgr);
        	writableDb_requests.notify();
        }
	}
	
	/**
	 * Handle Download Program a request.
	 * @param pgbdr
	 */
	private static void handleWritableDbRequest (WritableDbRequest pgbdr) {
		String key = pgbdr.getTime();

		ArrayList<TVProgram> ListTVP = pgbdr.getlistTVProgram();
		programDb Db = new programDb();
		for(TVProgram tvpg : ListTVP){
			Db.AddNew(tvpg, VendorManager.getCountry());
		}
		Db.close();
		writabledb_request_list.remove(key);
	}
	
	/**
	 * WritableDb Request Class.
	 * @author Eli
	 *
	 */
	static class WritableDbRequest {
		private ArrayList<TVProgram> listTVProgram;
		private String Time;
		public  WritableDbRequest (String Time, ArrayList<TVProgram> listTVProgram) {
			this.Time = Time;
			this.listTVProgram = listTVProgram;
		}
		public String getTime () {
			return Time;
		}
		public ArrayList<TVProgram> getlistTVProgram () {
			return listTVProgram;
		}
	}
	
	/**
	 * WritableDb Loader Class.
	 * @author Eli
	 *
	 */
	static class WritableDbLoader extends Thread {
		public void run() {
            try {
                while(true)
                {	
                    if(writableDb_requests.size() == 0) {
                        synchronized(writableDb_requests) {
                        	writableDb_requests.wait();
                        }
                    }
                    
                    if(writableDb_requests.size() > 0) {
                    	WritableDbRequest wdbr;
                        synchronized(writableDb_requests){
                        	wdbr = writableDb_requests.pop();
                        }
                        handleWritableDbRequest(wdbr);
                    }
                    if(Thread.interrupted())
                        break;
                }
            } catch (InterruptedException e) {
                //allow thread to exit
            }
        }
	}	
	
	/**
	 * Trigger retrieve event.
	 * @param username
	 * @param bmp
	 */
	private static void getViewProgramList(String Time) {
		for (ProgramListAdapterListener listener:pgladapter_Listener)
			listener.getViewProgramList(Time);
	}
	
	/**
	 * Add an  listener.
	 * @param listener
	 */
	public static void addListener (ProgramListAdapterListener listener) {
		pgladapter_Listener.add(listener);
	}
	
	/**
	 * Remove an  listener.
	 * @param listener
	 */
	public static void removeListener (ProgramListAdapterListener listener) {
		pgladapter_Listener.remove(listener);
	}
	
	
	public interface ProgramListAdapterListener {

		public void getViewProgramList(String Time);
		
	}

	
	/**
	 * Trigger retrieve event.
	 * @param username
	 * @param bmp
	 */
	public static void getViewProgramList() {
		for (ChangeAdapterListener listener:changeapter_Listener)
			listener.isChange();
	}
	
	/**
	 * Add an Change  listener.
	 * @param listener
	 */
	public static void addChangeListener (ChangeAdapterListener listener) {
		changeapter_Listener.add(listener);
	}
	
	/**
	 * Remove an Change listener.
	 * @param listener
	 */
	public static void removeChangeListener (ChangeAdapterListener listener) {
		changeapter_Listener.remove(listener);
	}
	
	
	public interface ChangeAdapterListener {

		public void isChange();
		
	}
}
	
