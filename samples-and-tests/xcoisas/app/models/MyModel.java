package models;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import play.db.jpa.Model;

@MappedSuperclass
public class MyModel extends Model {

	@Type(type="org.joda.time.contrib.hibernate.PersistentDateTime")
	@Column(name = "created_at")
	public DateTime created_at;

	@Type(type="org.joda.time.contrib.hibernate.PersistentDateTime")
	@Column(name = "updated_at")
	public DateTime updated_at;

	@PreUpdate
	public void onUpdate() {
		updated_at = new DateTime();
	}

	@PrePersist
	public void onCreate() {
		created_at = new DateTime();
		updated_at = created_at;
	}
}
