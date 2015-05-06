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
	
	public RiotAPI(String API_KEY, String summonerName, String inputServer) throws IOException
	{
		this.apiKey = API_KEY;
		this.summonerName = summonerName;
		this.inputServer = inputServer;
		
		initializeServer();
		
		this.summonerID = getSummonerID();
	}
	
	public String currentGame() throws IOException
	{
		String response = getResponse("https://{SERVER}/observer-mode/rest/consumer/getSpectatorGameInfo/{PLATFORM_ID}/{SUMMONER_ID}?api_key={API_KEY}");
		System.out.println(response);
		return null;
	}
	
	private String getChampionName(String response)
	{
		return null;
	}
	
	public void getCurrentGame() throws IOException
	{
		String ret = currentGame();
	}
	
	private long getSummonerID() throws IOException
	{
		String response = getResponse("https://{SERVER}/api/lol/{SERVER_1}/v1.4/summoner/by-name/{SUMMONER_NAME}?api_key={API_KEY}");		
		String finalID = getSummonerIDJSON(response);
		return Long.parseLong(finalID);
	}
	
	private String getSummonerIDJSON(String response)
	{
		String finishedResponse = "";
		boolean IDFound = false;
		for (int i = 0; i < response.length(); i++)
		{
			//Fail safe to quit in case we don't find ID.
			if (i + 5 >= response.length())
			{
				break;
			}

			//If we find ID field
			if (response.charAt(i) == '"' 
				&& response.charAt(i+1) == 'i' 
				&& response.charAt(i+2) == 'd' 
				&& response.charAt(i+3) == '"'
				&& response.charAt(i+4) == ':'
				)
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
	
		return finishedResponse;
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
	private String getResponse(String url) throws IOException
	{
		url = url.replace("{SERVER}", SERVERS.get(inputServer));
		url = url.replace("{SERVER_1}", inputServer);
		url = url.replace("{PLATFORM_ID}", getPlatformID());
		url = url.replace("{SUMMONER_NAME}", summonerName);
		url = url.replace("{API_KEY}", apiKey);
		url = url.replace("{SUMMONER_ID}", getSummonerIDString());
		
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
}
