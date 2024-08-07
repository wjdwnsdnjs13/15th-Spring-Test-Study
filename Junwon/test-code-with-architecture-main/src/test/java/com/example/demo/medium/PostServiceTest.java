package com.example.demo.medium;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import com.example.demo.post.controller.port.PostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.jdbc.SqlGroup;

import com.example.demo.post.domain.Post;
import com.example.demo.post.domain.PostCreate;
import com.example.demo.post.domain.PostUpdate;

@SpringBootTest
@TestPropertySource("classpath:test-application.properties")
@SqlGroup({
	@Sql(value = "/sql/post-service-test-data.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD),
	@Sql(value = "/sql/delete-all-data.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
})
public class PostServiceTest {

	@Autowired
	private PostService postService;

	@MockBean
	private JavaMailSender javaMailSender;

	@Test
	void getById_는_게시물을_가져온다(){
		// given
		// when
		Post result = postService.getById(1);

		// then
		assertThat(result.getContent()).isEqualTo("helloworld");
		assertThat(result.getWriter().getEmail()).isEqualTo("kok202@naver.com");
	}

	@Test
	void postCreateDto_를_이용하여_게시물을_생성할_수_있다(){
		// given
		PostCreate postCreate = PostCreate.builder()
				.writerId(1)
				.content("foobar")
				.build();

		// when
		Post result = postService.create(postCreate);

		// then
		assertThat(result.getId()).isNotNull();
		assertThat(result.getContent()).isEqualTo("foobar");
		assertThat(result.getCreatedAt()).isGreaterThan(0);
	}

	@Test
	void postUpdateDto_를_이용하여_게시물을_수정할_수_있다(){
		// given
		PostUpdate postUpdate = PostUpdate.builder()
			.content("hello world :)")
			.build();

		// when
		postService.update(1, postUpdate);

		// then
		Post post = postService.getById(1);
		assertThat(post.getContent()).isEqualTo("hello world :)");
		assertThat(post.getModifiedAt()).isGreaterThan(0);
	}

}
