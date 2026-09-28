package com.example.diaop;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static com.example.diaop.DiAopApplication.TASK_1_ENV;

@SpringBootTest
@ActiveProfiles(TASK_1_ENV)
class TaskOneTests {

	@Test
	void contextLoads() {
	}

}
