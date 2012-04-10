package com.omniscience.shared.jdo;

import com.google.gwt.user.client.rpc.IsSerializable;


/**
 * 
 * @author bharath
 * A class that represents a Calendar but made serializable to transfer 
 * through a GWT rpc
 */
public class SerializableCalendar implements IsSerializable {
	private String url;		
	private String name;	
	private String description;
	private String latitude;
	private String longitude;
	private String altitude;
	private String owner;
	
	public SerializableCalendar() {}
	
	public SerializableCalendar(Calendar cal) {
			this.name = cal.getName();
		this.longitude = cal.getLongitude();
		this.latitude = cal.getLatitude();
		this.altitude = cal.getLongitude();
		this.url = cal.getUrl();		
		this.description = cal.getDescription();		
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
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof SerializableCalendar)) {  
		  return false;
		}
		
		SerializableCalendar other = (SerializableCalendar) o;
		
		if (other.altitude == this.altitude &&
				other.latitude == this.latitude &&
				other.longitude == this.longitude &&
				other.url == this.url &&
				other.owner == this.owner &&
				other.name == this.name) {
			return true;
		}
		
		return false;
	}
}
