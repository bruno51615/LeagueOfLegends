
public abstract class ChampionStatic 
{
	/**
	 * 
	 * @param id because Riot API is translated to String
	 * @return
	 * @throws Exception 
	 */
	public String getChampionByID(String id) throws Exception
	{
		throw new Exception("COULD NOT FIND CHAMPION BY ID " + id);
	}
}
