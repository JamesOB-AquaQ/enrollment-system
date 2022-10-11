package com.sega.project.enrollmentsystem.jdbc;

import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

@Component
public class GeneratedKeyHolderFactory {

        public GeneratedKeyHolder newGeneratedKeyHolder() {
            return new GeneratedKeyHolder();
        }
}
