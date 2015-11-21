package me.shib.java.lib.omdb.service;

import me.shib.java.lib.omdb.models.OMDbContent;
import me.shib.java.lib.omdb.models.OMDbServiceModel;
import me.shib.java.lib.omdb.models.SearchResult;
import me.shib.java.lib.omdb.models.Season;
import me.shib.java.lib.rest.client.JsonLib;

public class OMDbService implements OMDbServiceModel {
	
	private JsonLib jsonLib;
	private RemoteOMDbServices remoteServices;
	private LocalCacheOMDbServices localServices;
	
	public OMDbService(long localCacheRenewalIntervalInMinutes, String localCacheDirectoryName) {
		initOMDBService(localCacheRenewalIntervalInMinutes, localCacheDirectoryName);
	}
	
	public OMDbService(long localCacheRenewalIntervalInMinutes) {
		initOMDBService(localCacheRenewalIntervalInMinutes, null);
	}
	
	public OMDbService() {
		initOMDBService(0, null);
	}
	
	private void initOMDBService(long localCacheRenewalIntervalInMinutes, String localCacheDirectoryName) {
		jsonLib = new JsonLib();
		remoteServices = new RemoteOMDbServices(jsonLib);
		if(localCacheRenewalIntervalInMinutes > 0) {
			LocalCacheManager localCache = new LocalCacheManager(localCacheRenewalIntervalInMinutes, localCacheDirectoryName);
			localServices = new LocalCacheOMDbServices(localCache, jsonLib);
		}
		else {
			localServices = null;
		}
	}

	public OMDbContent getContentByID(String imdbID) {
		OMDbContent returnableContent = null;
		if(localServices != null) {
			returnableContent = localServices.getContentByID(imdbID);
			if(returnableContent != null) {
				return returnableContent;
			}
		}
		returnableContent = remoteServices.getContentByID(imdbID);
		if(localServices != null) {
			localServices.setContentByID(returnableContent);
		}
		return returnableContent;
	}

	public OMDbContent getContentByTitle(String title) {
		OMDbContent returnableContent = null;
		if(localServices != null) {
			returnableContent = localServices.getContentByTitle(title);
			if(returnableContent != null) {
				return returnableContent;
			}
		}
		returnableContent = remoteServices.getContentByTitle(title);
		if(localServices != null) {
			localServices.setContentByTitle(returnableContent);
		}
		return returnableContent;
	}

	public SearchResult[] searchContentByTitle(String title) {
		return remoteServices.searchContentByTitle(title);
	}

	public Season getSeasonByID(String imdbID, int seasonNumber) {
		return getSeasonByID(imdbID, seasonNumber + "");
	}

	public Season getSeasonByID(String imdbID, String seasonNumber) {
		Season returnableSeason = null;
		if(localServices != null) {
			returnableSeason = localServices.getSeasonByID(imdbID, seasonNumber);
			if(returnableSeason != null) {
				return returnableSeason;
			}
		}
		returnableSeason = remoteServices.getSeasonByID(imdbID, seasonNumber);
		if(localServices != null) {
			localServices.setSeasonByID(imdbID, returnableSeason);
		}
		return returnableSeason;
	}

	public Season getSeasonByTitle(String title, int seasonNumber) {
		return getSeasonByTitle(title, seasonNumber + "");
	}

	public Season getSeasonByTitle(String title, String seasonNumber) {
		Season returnableSeason = null;
		if(localServices != null) {
			returnableSeason = localServices.getSeasonByTitle(title, seasonNumber);
			if(returnableSeason != null) {
				return returnableSeason;
			}
		}
		returnableSeason = remoteServices.getSeasonByTitle(title, seasonNumber);
		if(localServices != null) {
			localServices.setSeasonByTitle(title, returnableSeason);
		}
		return returnableSeason;
	}
	
}