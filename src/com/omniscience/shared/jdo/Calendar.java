package com.omniscience.shared.jdo;



import java.io.Serializable;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;


/**
 * 
 * @author bharath
 * A storage class that represents a Google Calendar that is registered with us.
 */
@PersistenceCapable
public class Calendar implements Serializable {
	@PrimaryKey
	@Persistent 
	private String url;
		
	@Persistent
	private String name;
	
	@Persistent
	String description;
	
	@Persistent
	private String latitude;
	
	@Persistent
	private String longitude;
	
	@Persistent
	private String altitude;
	
	
	@Persistent
	private String owner;
	
	public Calendar() {}
	
	public Calendar(String owner, String name, String latitude, String longitude, 
			String altitude, String description, String url) {
			this.name = name;
		this.longitude = longitude;
		this.latitude = latitude;
		this.altitude = altitude;
		this.url = url;		
		this.description = description;
		this.owner = owner;
	}
	
			
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	
	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getAltitude() {
		return altitude;
	}

	public void setAltitude(String altitude) {
		this.altitude = altitude;
	}		
}
