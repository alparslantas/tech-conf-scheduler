package com.tasalparslan.techconfscheduler.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tasalparslan.techconfscheduler.model.Talk;
import com.tasalparslan.techconfscheduler.repository.TalkRepository;

@Service
public class TalkService {

	private static final Logger logger = LoggerFactory.getLogger(TalkService.class);

	@Autowired
	TalkRepository repository;

	private final int perDayMinTime = 360;

	public void registerNewTalk(Talk talk) {
		System.out.println("registered talk title : " + talk.getTitle());
		// System.out.println("registered talk duration : " + talk.getDuration());
		logger.info("registered talk title : " + talk.getTitle());
		logger.info("registered talk duration : " + talk.getDuration());
		repository.save(talk);
	}

	public List<List<Talk>> getTracks() {
		List<List<Talk>> tracks = new ArrayList<List<Talk>>();

		logger.info("Getting All Talks...");
		tracks = getScheduleConferenceTrack(repository.findAll());

		return tracks;
	}

	private int getTotalTalksTime(List<Talk> talkList) {
		if (talkList == null || talkList.isEmpty()) {
			return 0;
		}
		int totalTime = 0;
		for (Talk talk : talkList) {
			totalTime += talk.getDuration();
		}
		return totalTime;
	}

	private List<List<Talk>> getScheduleConferenceTrack(List<Talk> talks) {

		// int perDayMinTime = 6 * 60;
		int totalTalkTime = getTotalTalksTime(talks);
		int totalPossibleDays = totalTalkTime / perDayMinTime;

		// Sort the talkList.
		List<Talk> talksListForOperation = new ArrayList<Talk>();
		talksListForOperation.addAll(talks);
		Collections.sort(talksListForOperation);

		// morning session
		List<List<Talk>> combForMornSessions = possibleSessionCombination(talksListForOperation, totalPossibleDays, true);

		// Remove all the scheduled talks for morning session
		for (List<Talk> talkList : combForMornSessions) {
			talksListForOperation.removeAll(talkList);
		}

		// Possible combinations for the evening session.
		List<List<Talk>> combForEveSessions = possibleSessionCombination(talksListForOperation, totalPossibleDays, false);

		// Remove all the scheduled talks for evening session
		for (List<Talk> talkList : combForEveSessions) {
			talksListForOperation.removeAll(talkList);
		}

		// check if the operation list is not empty, then try to fill all the remaining talks in evening session.
		int maxSessionTimeLimit = 240;
		if (!talksListForOperation.isEmpty()) {
			List<Talk> scheduledTalkList = new ArrayList<Talk>();
			for (List<Talk> talkList : combForEveSessions) {
				int totalTime = getTotalTalksTime(talkList);

				for (Talk talk : talksListForOperation) {
					int talkTime = talk.getDuration();

					if (talkTime + totalTime <= maxSessionTimeLimit) {
						talkList.add(talk);
						talk.setScheduled(true);
						scheduledTalkList.add(talk);
						totalTime = totalTime + talkTime;
					}
				}

				talksListForOperation.removeAll(scheduledTalkList);
				if (talksListForOperation.isEmpty())
					break;
			}
		}

		// If operation list is still not empty, its mean the conference can not be scheduled with the provided data.
		if (!talksListForOperation.isEmpty()) {
			logger.info("UNAVAILABLE FOR SCHEDULE. Will schedule when available talks registered..");
		}

		// Schedule the day event from morning session and evening session.
		return getScheduledTalkList(combForMornSessions, combForEveSessions);
	}

	private List<List<Talk>> getScheduledTalkList(List<List<Talk>> combForMornSessions, List<List<Talk>> combForEveSessions) {
		List<List<Talk>> scheduledTalksList = new ArrayList<List<Talk>>();
		int totalPossibleDays = combForMornSessions.size();

		// schedule event for all days.
		for (int dayCount = 0; dayCount < totalPossibleDays; dayCount++) {
			List<Talk> talkList = new ArrayList<Talk>();

			// initialize start time 09:00 AM.
			SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mma ");
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.HOUR, 9);
			calendar.set(Calendar.MINUTE, 0);
			Date date = calendar.getTime();

			int trackCount = dayCount + 1;
			String scheduledTime = dateFormat.format(date);

			System.out.println("Track " + trackCount + ":");

			// Morning Session - set the scheduled time in the talk and get the next time using time duration of current talk.
			List<Talk> mornSessionTalkList = combForMornSessions.get(dayCount);

			for (Talk pres : mornSessionTalkList) {
				pres.setScheduledTime(scheduledTime);
				System.out.println(scheduledTime + pres.getTitle());
				scheduledTime = getNextScheduledTime(date, pres.getDuration());
				talkList.add(pres);
			}

			// Scheduled Lunch Time for 60 mins.
			int lunchTimeDuration = 60;
			Talk lunchTalk = new Talk("Lunch", "Lunch", 60);
			lunchTalk.setScheduledTime(scheduledTime);
			talkList.add(lunchTalk);
			System.out.println(scheduledTime + "Lunch");

			// Evening Session - set the scheduled time in the talk and get the next time using time duration of current talk.
			scheduledTime = getNextScheduledTime(date, lunchTimeDuration);
			List<Talk> eveSessionTalkList = combForEveSessions.get(dayCount);
			if (!eveSessionTalkList.isEmpty()) {
				for (Talk talk : eveSessionTalkList) {
					talk.setScheduledTime(scheduledTime);
					talkList.add(talk);
					System.out.println(scheduledTime + talk.getTitle());
					scheduledTime = getNextScheduledTime(date, talk.getDuration());
				}
			}

			// Scheduled Networking Event at the end of session, Time duration is just to initialize the Talk object.
			Talk networkingTalk = new Talk("Networking Event", "Networking Event", 60);
			networkingTalk.setScheduledTime(scheduledTime);
			talkList.add(networkingTalk);
			System.out.println(scheduledTime + "Networking Event\n");
			scheduledTalksList.add(talkList);
		}

		return scheduledTalksList;
	}

	private String getNextScheduledTime(Date date, int timeDuration) {
		long timeInLong = date.getTime();
		SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mma ");

		long timeDurationInLong = timeDuration * 60 * 1000;
		long newTimeInLong = timeInLong + timeDurationInLong;

		date.setTime(newTimeInLong);
		String str = dateFormat.format(date);
		return str;
	}

	private List<List<Talk>> possibleSessionCombination(List<Talk> preList, int totalPosibleDays, boolean morningSession) {
		int minSessionTimeLimit = 180;
		int maxSessionTimeLimit = 240;
		int talkListCount = preList.size();

		if (morningSession) {
			maxSessionTimeLimit = minSessionTimeLimit;
		}

		List<List<Talk>> possibleCombinationsOfTalks = new ArrayList<List<Talk>>();
		int possibleCombinationCount = 0;

		for (int count = 0; count < talkListCount; count++) {
			int startPoint = count;
			int totalTime = 0;
			List<Talk> possibleCombList = new ArrayList<>();

			while (startPoint != talkListCount) {
				int currentCount = startPoint;
				startPoint++;
				Talk currentTalk = preList.get(currentCount);
				if (currentTalk.isScheduled()) {
					continue;
				}
				int talkTime = currentTalk.getDuration();
				if (talkTime > maxSessionTimeLimit || talkTime + totalTime > maxSessionTimeLimit) {
					continue;
				}
				possibleCombList.add(currentTalk);
				totalTime += talkTime;

				if (morningSession) {
					if (totalTime == maxSessionTimeLimit)
						break;
				}
				else if (totalTime >= minSessionTimeLimit) {
					break;
				}
			}

			boolean validSession = false;
			if (morningSession) {
				validSession = (totalTime == maxSessionTimeLimit);
			}
			else {
				validSession = (totalTime >= minSessionTimeLimit && totalTime <= maxSessionTimeLimit);
			}

			if (validSession) {
				possibleCombinationsOfTalks.add(possibleCombList);
				for (Talk talk : possibleCombList) {
					talk.setScheduled(true);
				}
				possibleCombinationCount++;
				if (possibleCombinationCount == totalPosibleDays)
					break;
			}

		}

		return possibleCombinationsOfTalks;
	}

}
