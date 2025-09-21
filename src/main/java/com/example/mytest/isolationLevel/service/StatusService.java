package com.example.mytest.isolationLevel.service;

import com.example.mytest.isolationLevel.domain.StatusDomain;
import com.example.mytest.isolationLevel.repository.StatusRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import static java.lang.Thread.currentThread;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatusService {

    private final StatusRepository repository;

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void update(StatusDomain statusDomain) {
        log.info("Thread {} entered update method", currentThread().getName());

        StatusDomain current = repository.findByUuid(statusDomain.getUuid());
        log.info("Thread {} selected current status {}", currentThread().getName(), current.getStatus());

        if (statusDomain.getStatus() > current.getStatus()) {
            repository.update(statusDomain);
            log.info("Thread {} updated from current status {} to new status {}",
                    currentThread().getName(), current.getStatus(), statusDomain.getStatus());
        }

        log.info("Thread {} exited update method", currentThread().getName());
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void save(StatusDomain statusDomain) {
        log.info("Thread {} entered save method", currentThread().getName());

        StatusDomain current = repository.findByUuid(statusDomain.getUuid());
        log.info("Thread {} selected registry {}", currentThread().getName(), current);


        if (current == null) {
            Long id = repository.save(statusDomain);
            log.info("Thread {} saved statusDomain and generated id {}", currentThread().getName(), id);
        }

        log.info("Thread {} exited save method", currentThread().getName());
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public StatusDomain getUUID(String uuid) {
        log.info("Thread {} entered get method", currentThread().getName());

        StatusDomain current = repository.findByUuid(uuid);

        log.info("Thread {} selected registry {}", currentThread().getName(), current);

        log.info("Thread {} exited get method", currentThread().getName());

        return current;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void getUUID(StatusDomain statusDomain) {
        log.info("Thread {} entered get method", currentThread().getName());

        StatusDomain current = repository.findByUuid(statusDomain.getUuid());

        log.info("Thread {} selected registry {}", currentThread().getName(), current);
        log.info("Thread {} exited get method", currentThread().getName());
    }
}
