package com.wildmind.fanwave.tutroial;


import java.util.HashMap;

import com.wildmind.fanwave.activity.R;
import com.wildmind.fanwave.app.ApplicationManager;


public class TutorialManager {

	public static String NotificationActivity 					= "com.wildmind.fanwave.activity.NotificationActivity";
	
	public static String WizardActivity 						= "com.wildmind.fanwave.activity.WizardActivity";
	
	public static String SplashProgramActivity					= "com.wildmind.fanwave.activity.SplashProgramActivity";
	public static String SplashPrivateActivity 					= "com.wildmind.fanwave.activity.SplashPrivateActivity";
	public static String SplashSearchActivity 					= "com.wildmind.fanwave.activity.SplashSearchActivity";
	public static String SplashActivity 						= "com.wildmind.fanwave.activity.SplashActivity";
	
	public static String WebViewActivity 						= "com.wildmind.fanwave.activity.WebViewActivity";
	
	public static String ImagePresentActivity 					= "com.wildmind.fanwave.activity.ImagePresentActivity";
	
	public static String GuideEpgforPgbyChActivity  			= "com.wildmind.fanwave.activity.GuideEpgforPgbyChActivity";
	public static String GuideSettingsReorderActivity 			= "com.wildmind.fanwave.activity.GuideSettingsReorderActivity";
	public static String GuideSettingsHidderActivity 			= "com.wildmind.fanwave.activity.GuideSettingsHidderActivity";
	public static String GuideSettingsProviderActivity 			= "com.wildmind.fanwave.activity.GuideSettingsProviderActivity";
	public static String GuideSettingsActivity 					= "com.wildmind.fanwave.activity.GuideSettingsActivity";
	public static String GuideActivity 							= "com.wildmind.fanwave.activity.GuideActivity";
	
	public static String ProgramSofaActivity 					= "com.wildmind.fanwave.activity.ProgramSofaActivity";
	
	public static String TopfanActivity 						= "com.wildmind.fanwave.activity.TopfanActivity";
	
	public static String ProgramExtraInfoActivity 				= "com.wildmind.fanwave.activity.ProgramExtraInfoActivity";
	
	public static String BadgePlayerActivity 					= "com.wildmind.fanwave.activity.BadgePlayerActivity";
	public static String BadgeProgramActivity 					= "com.wildmind.fanwave.activity.BadgeProgramActivity";
	public static String BadgeTypeActivity 						= "com.wildmind.fanwave.activity.BadgeTypeActivity";
	
	public static String HotActivity 							= "com.wildmind.fanwave.activity.HotActivity";
	
	
	public static String FriendsActivity					  	= "com.wildmind.fanwave.activity.FriendsActivity";
	
	public static String CommentActivity 					 	= "com.wildmind.fanwave.activity.CommentActivity";
	
	public static String WavesActivity 						 	= "com.wildmind.fanwave.activity.WavesActivity";
	
	public static String AccountSettingsSoundActivity 		 	= "com.wildmind.fanwave.activity.AccountSettingsSoundActivity";
	public static String AccountSettingsSocialNetworkActivity 	= "com.wildmind.fanwave.activity.AccountSettingsSocialNetworkActivity";
	public static String AccountSettingsPrivacyActivity 	  	= "com.wildmind.fanwave.activity.AccountSettingsPrivacyActivity";
	public static String AccountSettingsPasswordActivity 	  	= "com.wildmind.fanwave.activity.AccountSettingsPasswordActivity";
	public static String AccountSettingsActivity 			  	= "com.wildmind.fanwave.activity.AccountSettingsActivity";
	
	
	public static String SignUpActivity 					  	= "com.wildmind.fanwave.activity.SignUpActivity";
	
	public static String LoginFanwaveActivity 				  	= "com.wildmind.fanwave.activity.LoginFanwaveActivity";
	public static String LoginActivity 						  	= "com.wildmind.fanwave.activity.LoginActivity";
	public static String LaunchingActivity 					  	= "com.wildmind.fanwave.activity.LaunchingActivity";
	
	
	
	
	
	public static String PersonalActivity 		 = "com.wildmind.fanwave.activity.PersonalActivity";
	public static int[] PersonalActivityArray 	 = { R.drawable.fanwavetutorial_personal1 ,
												  	 R.drawable.fanwavetutorial_personal2 };
	
	public static String ProgramActivity 		 = "com.wildmind.fanwave.activity.ProgramActivity";	
	public static int[] ProgramActivityArray 	 = { R.drawable.fanwavetutorial_program1 ,
													 R.drawable.fanwavetutorial_program2 ,
													 R.drawable.fanwavetutorial_program4 ,
													 R.drawable.fanwavetutorial_program5 ,
													 R.drawable.fanwavetutorial_program6 ,
													 R.drawable.fanwavetutorial_program7 };
	
	public static String MainPageActivity 		 = "com.wildmind.fanwave.activity.MainPageActivity";
	
	public static int[] MainPageActivityArray 	 = { R.drawable.fanwavetutorial_start_welcome ,
													 R.drawable.fanwavetutorial_start1 ,
													 R.drawable.fanwavetutorial_start2 ,
													 R.drawable.fanwavetutorial_start3 ,
													 R.drawable.fanwavetutorial_start4 ,
													 R.drawable.fanwavetutorial_start5 ,
													 R.drawable.fanwavetutorial_start6 ,
													 R.drawable.fanwavetutorial_start7 ,
													 R.drawable.fanwavetutorial_start8 };
	
	public static String[] TutorialList = {PersonalActivity, ProgramActivity, MainPageActivity};

	private static HashMap<String, int[]> TutorialMap = getTutorialMap();
	
	private static HashMap<String, int[]> getTutorialMap(){
		HashMap<String, int[]> TutorialMap = new HashMap<String, int[]>();
		TutorialMap.put(PersonalActivity , PersonalActivityArray);
		TutorialMap.put(ProgramActivity  , ProgramActivityArray);
		TutorialMap.put(MainPageActivity , MainPageActivityArray);
		return TutorialMap;
	}
	
	public static boolean everShowTutorial(String className){
		return ApplicationManager.getAppContext().getSharedPreferences("Tutorial", 0).getBoolean(className, false);
	}
	
	public static int[] getTutorialArray(String className){
			return TutorialMap.get(className);
	}
	
	public static boolean needToShowTutorial(String className){
		if(className.equals(PersonalActivity)||
		   className.equals(MainPageActivity))
			return true;
		else
			return false;
	} 

}
