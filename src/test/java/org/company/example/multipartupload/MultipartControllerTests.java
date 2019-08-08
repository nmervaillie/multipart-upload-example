package org.company.example.multipartupload;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.company.example.multipartupload.controller.MultipartController;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class MultipartControllerTests {

	private static final byte[] FILE_CONTENTS_AS_BYTES = "foo bar baz".getBytes();
	private static final String FILENAME = "sample.pdf";

	@Rule
	public WireMockRule wireMockRule = new WireMockRule(28459);

	@Autowired
	MultipartController multipartController;

    @Test
    public void shouldUploadFile() throws Exception {

    	// given
		stubFor(post(urlEqualTo("/upload/research"))
				.withHeader("Accept", equalTo("application/json"))
//				.withHeader("Content-Type", containing(" multipart/form-data"))
				.withMultipartRequestBody(
						aMultipart()
								.withHeader("Content-Disposition", equalTo("form-data; name=\"pdf\"; filename=\"" + FILENAME + "\""))
								.withHeader("Content-Type", equalTo("application/pdf"))
								.withBody(binaryEqualTo(FILE_CONTENTS_AS_BYTES))
				)
				.withMultipartRequestBody(
						aMultipart()
								.withName("upload")
								.withBody(equalTo("false"))
				)
				.willReturn(aResponse()
						.withStatus(200)
						.withHeader("Content-Type", "application/json")
						.withBody("{\"response\":\"got it\"}")));

		MockMultipartFile filePart = new MockMultipartFile("file", FILENAME,
				"application/pdf", FILE_CONTENTS_AS_BYTES);

		// when
		standaloneSetup(multipartController).build()
				.perform(multipart("/multipartfile").file(filePart))

		// then
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.response", is("got it")));

		verify(postRequestedFor(urlEqualTo("/upload/research")));
	}

}
