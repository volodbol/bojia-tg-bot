package com.volod.bojia.tg;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class BojiaTgBotApplicationTests {

	@Test
	void noopTest() {
		assertThat(1 + 1).isGreaterThanOrEqualTo(2);
	}

}
