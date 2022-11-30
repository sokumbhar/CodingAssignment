package com.github.sokumbhar.MyApplication;

import com.github.sokumbhar.MyApplication.persistence.model.Alert;
import com.github.sokumbhar.MyApplication.repository.AlertRepository;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
class LogAnalyzerApplicationTests {

	@Autowired
	private AlertRepository alertRepository;

	@Test
	public void testFindById() {
		Alert alert = new Alert();
		alert.setId("alert-100");
		alert.setDuration(5);
		alert.setHost("xyz");
		alert.setEventType("APPLICATION_LOG");
		alertRepository.save(alert);
		assertThat(alertRepository.findById("alert-100")).isInstanceOf(Optional.class);
	}

	@Test
	public void testFindAll() {
		Alert alert = new Alert();
		alert.setId("alert-1000");
		alert.setDuration(3);
		alert.setHost("127.0.0.1
		alert.setEventType("APPLICATION_LOG");
		alert.setFlagAlert(Boolean.FALSE);
		alertRepository.save(alert);

		alert = new Alert();
		alert.setId("alert-2000");
		alert.setDuration(7);
		alert.setHost("XYZ");
		alert.setEventType(null);
		alert.setFlagAlert(Boolean.TRUE);
		alertRepository.save(alert);

		alert = new Alert();
		alert.setId("alert-3000");
		alert.setDuration(1);
		alert.setHost("localhost");
		alert.setEventType("APPLICATION_LOG");
		alert.setFlagAlert(Boolean.FALSE);
		alertRepository.save(alert);

		assertThat(alertRepository.findAll()).isInstanceOf(List.class);
	}
}
