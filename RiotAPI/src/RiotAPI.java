import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

public class RiotAPI
{	
	private HashMap<String, String> SERVERS;
	private HashMap<String, String> PLATFORM_ID;
	private String apiKey;
	private String summonerName;
	private String inputServer;
	public long summonerID;
	private long sleep;
	
	public RiotAPI(String API_KEY, String inputServer, long sleep)
	{
		this.apiKey = API_KEY;
		this.inputServer = inputServer;
		this.sleep = sleep;
		
		initializeServer();
	}
	
	public RiotAPI(String API_KEY, String inputServer)
	{
		this.apiKey = API_KEY;
		this.inputServer = inputServer;
		this.sleep = 1000;
		
		initializeServer();
	}
	
	public RiotAPI(String API_KEY)
	{
		this.apiKey = API_KEY;
		this.inputServer = "NA";
		this.sleep = 1000;
		
		initializeServer();
	}
	
	public void getCurrentGame(long summonerID)
	{
		this.summonerID = summonerID;
		String response = getResponse("https://{SERVER}/observer-mode/rest/consumer/getSpectatorGameInfo/{PLATFORM_ID}/{SUMMONER_ID}?api_key={API_KEY}");
		String[] playersName = getSummonerNameFromCurrentGame(response);
		long[] playersID = new long[playersName.length];
		
		for (int i = 0; i < playersName.length; i++)
		{
			System.out.print("Player number " + (i + 1) + "  " + playersName[i].replaceAll("\\s+","") + " ");
			playersID[i] = this.getSummonerID(playersName[i].replaceAll("\\s+",""));
			System.out.println("Summoner ID: " + playersID[i]);
		}

	}
	
	//TODO
	private String getChampionName(String response)
	{
		return null;
	}
	
	public long getSummonerID(String summonerName)
	{
		this.summonerName = summonerName;
		String response = getResponse("https://{SERVER}/api/lol/{SERVER_1}/v1.4/summoner/by-name/{SUMMONER_NAME}?api_key={API_KEY}");		
		String finalID = getSummonerIDJSON(response);
		return Long.parseLong(finalID);
	}
	
	private String getSummonerIDJSON(String response)
	{
		String finishedResponse = "";
		boolean IDFound = false;
		//System.out.println(response);
		for (int i = 0; i < response.length(); i++)
		{
			//Fail safe to quit in case we don't find ID.
			if (i + 5 >= response.length())
			{
				break;
			}

			//If we find ID field
			if (response.charAt(i) == '"' && response.charAt(i+1) == 'i' && response.charAt(i+2) == 'd' && response.charAt(i+3) == '"' && response.charAt(i+4) == ':')
			{
				IDFound = true;
			}
			
			if (IDFound)
			{
				if(response.charAt(i) == '0'
				|| response.charAt(i) == '1'
				|| response.charAt(i) == '2'
				|| response.charAt(i) == '3'
				|| response.charAt(i) == '4'
				|| response.charAt(i) == '5'
				|| response.charAt(i) == '6'
				|| response.charAt(i) == '7'
				|| response.charAt(i) == '8'
				|| response.charAt(i) == '9'
				|| response.charAt(i) == ' ')
				{
					finishedResponse += response.charAt(i);
				}
				else if (response.charAt(i) == ',')
				{
					IDFound = false;
				}
			}
		}
		
		try 
		{
			Thread.sleep(this.sleep);
		} catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
		return finishedResponse;
	}
	
	private String[] getSummonerNameFromCurrentGame(String response)
	{
		String foo = "";
		String players[] = new String[10];
		int counter = 0;
		
		for (int i = 0; i < response.length(); i++)
		{
			//If we find summonerName
			//I'm sorry
			if (response.charAt(i) == '"' && response.charAt(i+1) == 's' && response.charAt(i+2) == 'u' && response.charAt(i+3) == 'm' && response.charAt(i+4) == 'm' && response.charAt(i+5) == 'o' && response.charAt(i+6) == 'n' && response.charAt(i+7) == 'e' && response.charAt(i+8) == 'r' && response.charAt(i+9) == 'N' && response.charAt(i+10) == 'a' && response.charAt(i+11) == 'm' && response.charAt(i+12) == 'e' && response.charAt(i+13) == '"' && response.charAt(i+14) == ':' && response.charAt(i+15) == '"')
			{
				i += 16;
				
				for (; response.charAt(i) != '"'; i++)
				{
					foo += response.charAt(i);
				}
				
				players[counter] = foo;
				
				counter++;
				
				foo = "";
			}
		}	
		return players;
	}
	
	
	private void initializeServer()
	{
		//Initialize server
		SERVERS = new HashMap<String, String>();
		this.SERVERS.put("BR", "br.api.pvp.net");
		this.SERVERS.put("EUNE", "eune.api.pvp.net");
		this.SERVERS.put("EUW", "euw.api.pvp.net");
		this.SERVERS.put("KR", "kr.api.pvp.net");
		this.SERVERS.put("LAN", "lan.api.pvp.net");
		this.SERVERS.put("LAS", "las.api.pvp.net");
		this.SERVERS.put("NA", "na.api.pvp.net");
		this.SERVERS.put("OCE", "oce.api.pvp.net");
		this.SERVERS.put("TR", "tr.api.pvp.net");
		this.SERVERS.put("RU", "ru.api.pvp.net");
		this.SERVERS.put("PBE", "pbe.api.pvp.net");
		this.SERVERS.put("GLOBAL", "global.api.pvp.net");
		
		//Initialize Platform id
		PLATFORM_ID = new HashMap<String, String>();
		this.PLATFORM_ID.put("BR", "BR1");
		this.PLATFORM_ID.put("EUNE", "EUN1");
		this.PLATFORM_ID.put("EUW", "EUW1");
		this.PLATFORM_ID.put("KR", "KR1");
		this.PLATFORM_ID.put("LAN", "LA1");
		this.PLATFORM_ID.put("LAS", "LA2");
		this.PLATFORM_ID.put("NA", "NA1");
		this.PLATFORM_ID.put("OCE", "OC1");
		this.PLATFORM_ID.put("TR", "TR1");
		this.PLATFORM_ID.put("RU", "RU");
		this.PLATFORM_ID.put("PBE", "PBE1");
	}
	
	private String getPlatformID()
	{
		return PLATFORM_ID.get(inputServer);
	}
	
	private String getSummonerIDString(){ return new Long(summonerID).toString(); }
	 
	/**
	 * Gets the HTML from Riot server
	 * @param url
	 * @return
	 * @throws IOException
	 */
	private String getResponse(String url)
	{
		try 
		{
			url = url.replace("{SERVER}", SERVERS.get(inputServer));
			url = url.replace("{SERVER_1}", inputServer);
			url = url.replace("{PLATFORM_ID}", getPlatformID());
			url = url.replace("{SUMMONER_NAME}", summonerName);
			url = url.replace("{API_KEY}", apiKey);
			url = url.replace("{SUMMONER_ID}", getSummonerIDString());
			
			//System.out.println(url);
			URL website = new URL(url);
	        URLConnection connection = website.openConnection();
	        BufferedReader in = new BufferedReader(
	                            new InputStreamReader(
	                            connection.getInputStream()));
	
	        StringBuilder response = new StringBuilder();
	        String inputLine;
	
	        while ((inputLine = in.readLine()) != null) 
	        {
	            response.append(inputLine);
	        }
	        
	        in.close();
	
	        return response.toString();
		}
        catch (IOException e)
		{
			return null;
		}
	}
}
