package uk.ac.edukapp.model;

import java.io.Serializable;
import javax.persistence.*;

import org.codehaus.jackson.annotate.JsonIgnore;

import java.util.List;

/**
 * The persistent class for the activities database table.
 * 
 */
@Entity
@Table(name = "activities")
@NamedQueries({
		@NamedQuery(name = "Activity.findByName", query = "SELECT a FROM Activity a WHERE a.activitytext = :activityname"),
		@NamedQuery(name = "Activity.popular", query = "SELECT a, SIZE(a.widgetprofiles) as freq FROM Activity a ORDER BY freq DESC") })
public class Activity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(unique = true, nullable = false)
	private int id;

	@Column(nullable = false, length = 64)
	private String activitytext;

	// bi-directional many-to-many association to Widgetprofile
	@ManyToMany(mappedBy = "activities")
	private List<Widgetprofile> widgetprofiles;

	public Activity() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getActivitytext() {
		return this.activitytext;
	}

	public void setActivitytext(String activitytext) {
		this.activitytext = activitytext;
	}

	@JsonIgnore
	public List<Widgetprofile> getWidgetprofiles() {
		return this.widgetprofiles;
	}

	public void setWidgetprofiles(List<Widgetprofile> widgetprofiles) {
		this.widgetprofiles = widgetprofiles;
	}

}