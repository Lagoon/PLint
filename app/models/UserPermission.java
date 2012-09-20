package models;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;

import play.db.jpa.Model;

@Entity
public class UserPermission extends Model {

	public String action;

	public String actionPoint;

	@Override
	public String toString() {
		return action + "." + actionPoint;
	}

	@ManyToMany(mappedBy="userAccessPermission")
	public Set<UserLagoon> users = new HashSet<UserLagoon>(0);

	//	~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public UserPermission(String action, String actionPoint) {
		this.action = action;
		this.actionPoint = actionPoint;
	}

	public static UserPermission findByActionAndActionPoint(String action, String actionPoint) {
		return find("byActionAndActionPoint", action, actionPoint).first();
	}
}
