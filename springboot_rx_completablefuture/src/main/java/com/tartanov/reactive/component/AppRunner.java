package com.tartanov.reactive.component;

import com.tartanov.reactive.dto.User;
import com.tartanov.reactive.service.GitHubLookupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import rx.Observable;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

/**
 * Created by tartanov.mikhail on 14.03.2017.
 */


@Component
public class AppRunner implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(AppRunner.class);

    private final GitHubLookupService gitHubLookupService;

    public AppRunner(GitHubLookupService gitHubLookupService) {
        this.gitHubLookupService = gitHubLookupService;
    }


    @Override
    public void run(String... strings) throws Exception {
        long start = System.currentTimeMillis();

        CompletableFuture<User> page1 = gitHubLookupService.findUser("PivotalSoftware");
        CompletableFuture<User> page2 = gitHubLookupService.findUser("CloudFoundry");
        CompletableFuture<User> page3 = gitHubLookupService.findUser("Spring-Projects");


        Observable<User> page1Obs = Observable.from(page1);
        Observable<User> page2Obs = Observable.from(page2);
        Observable<User> page3Obs = Observable.from(page3);

        Observable
                .zip(page1Obs, page2Obs, page3Obs, (user, user2, user3) -> Arrays.asList(user, user2, user3))
                .subscribe(array -> {
                    logger.info("Entered subscriber with list {}", array);
                    array.forEach(System.out::println);
                });







    }
}
