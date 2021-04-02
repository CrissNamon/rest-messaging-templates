package ru.rassokhindanila.restmessagingtemplates.service.Impl;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.rassokhindanila.restmessagingtemplates.service.LoggerService;

@Service
@Data
public class LoggerServiceImpl implements LoggerService {

    private Logger logger;

    @Value("${logger.enabled}")
    private boolean enabled;

    public LoggerServiceImpl()
    {
        logger = LoggerFactory.getLogger(LoggerServiceImpl.class);
    }

    @Override
    public void error(String message) {
        if(enabled) logger.error(message);
    }

    @Override
    public void info(String message) {
        if(enabled) logger.info(message);
    }
}
