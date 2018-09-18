package com.tasalparslan.techconfscheduler.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Talk implements Comparable<Object> {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String title;
	private String name;
	private int duration;
	private boolean scheduled;
	private String scheduledTime;

	public Talk() {
		super();
	}

	public Talk(String title, int duration) {
		super();
		this.title = title;
		this.duration = duration;
	}

	public Talk(String title, String name, int duration) {
		super();
		this.title = title;
		this.duration = duration;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public boolean isScheduled() {
		return scheduled;
	}

	public void setScheduled(boolean scheduled) {
		this.scheduled = scheduled;
	}

	public String getScheduledTime() {
		return scheduledTime;
	}

	public void setScheduledTime(String scheduledTime) {
		this.scheduledTime = scheduledTime;
	}

	@Override
	public String toString() {
		return "Talk [id=" + id + ", title=" + title + ", name=" + name + ", duration=" + duration + ", scheduled=" + scheduled + ", scheduledTime=" + scheduledTime + "]";
	}

	@Override
	public int compareTo(Object obj) {
		Talk presentation = (Talk) obj;
		if (this.duration > presentation.duration)
			return -1;
		else if (this.duration < presentation.duration)
			return 1;
		else
			return 0;
	}

}
