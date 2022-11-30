package com.github.sokumbhar.MyApplication.config;

import com.interview.anandankush0908.loganalyzer.persistence.model.Alert;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlertRepository extends CrudRepository<Alert, String> {
}
