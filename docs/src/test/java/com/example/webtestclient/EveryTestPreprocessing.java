/*
 * Copyright 2014-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.webtestclient;

import org.junit.Before;
import org.junit.Rule;

import org.springframework.context.ApplicationContext;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.removeHeaders;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.documentationConfiguration;

public class EveryTestPreprocessing {

	// @formatter:off

	@Rule
	public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();

	private ApplicationContext context;

	// tag::setup[]
	private WebTestClient webTestClient;

	@Before
	public void setup() {
		this.webTestClient = WebTestClient.bindToApplicationContext(this.context)
			.configureClient()
			.filter(documentationConfiguration(this.restDocumentation)
				.operationPreprocessors()
					.withRequestDefaults(removeHeaders("Foo")) // <1>
					.withResponseDefaults(prettyPrint())) // <2>
			.build();
	}
	// end::setup[]

	public void use() throws Exception {
		// tag::use[]
		this.webTestClient.get().uri("/").exchange().expectStatus().isOk()
			.expectBody().consumeWith(document("index",
				links(linkWithRel("self").description("Canonical self link"))));
		// end::use[]
	}

}
