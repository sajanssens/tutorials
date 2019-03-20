package com.baeldung.threadlocal;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SharedMapWithUserContext implements Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(SharedMapWithUserContext.class);

    final static Map<Integer, Context> userContextPerUserId = new ConcurrentHashMap<>();
    private final Integer userId;
    private UserRepository userRepository = new UserRepository();

    SharedMapWithUserContext(Integer userId) {
        this.userId = userId;
    }

    @Override
    public void run() {
        String userName = userRepository.getUserNameForUserId(userId);
        userContextPerUserId.put(userId, new Context(userName));
        LOG.debug("thread context for given userId: " + userId + " is: " + userContextPerUserId.get(userId));

    }
}
