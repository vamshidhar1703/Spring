package com.project.journalApp.Scheduler;

import com.project.journalApp.Cache.AppCache;
import com.project.journalApp.Entity.JournalEntry;
import com.project.journalApp.Entity.User;
import com.project.journalApp.Enums.Sentiment;
import com.project.journalApp.Repository.UserRepositoryImpl;
import com.project.journalApp.Service.EmailService;
import com.project.journalApp.Service.SentimentAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Component
public class UserScheduler {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepositoryImpl userRepository;

    @Autowired
    private AppCache appCache;

    @Autowired
    private SentimentAnalysisService sentimentAnalysisService;

    @Scheduled(cron = "0 0 9 * * SUN", zone = "UTC")
    public void fetchUsersAndSendSaMail(){
        userRepository.getUsersForSA();

        List<User> users = userRepository.getUsersForSA();
        for (User user : users) {
            List<JournalEntry> journalEntries = user.getJournalEntries();
            List<Sentiment> sentiments = journalEntries.stream().filter(x -> x.getDate().isAfter(LocalDateTime.now().minus(7, ChronoUnit.DAYS)))
                    .map(x -> x.getSentiment()).collect(Collectors.toList());

            Map<Sentiment,Integer> sentimentCounts=new HashMap<>();

            for(Sentiment sentiment:sentiments){
                if(sentiment!=null){
                    sentimentCounts.put(sentiment,sentimentCounts.getOrDefault(sentiment,0)+1);
                }
            }

            Sentiment mostFreuentSentiment=null;
            int maxCount=0;

            for(Map.Entry<Sentiment,Integer> entry:sentimentCounts.entrySet()){
                if(entry.getValue()>maxCount){
                    maxCount= entry.getValue();
                    mostFreuentSentiment=entry.getKey();
                }
            }

            if(mostFreuentSentiment!=null){
                emailService.sendEmail(user.getEmail(), "last 7 days senti",mostFreuentSentiment.toString());
            }

        }}

    @Scheduled(cron = "0 0/10 * ? * *")
    public void clearAppCache() {
        appCache.init();
    }
}
