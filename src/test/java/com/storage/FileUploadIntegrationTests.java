package com.storage;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * 
 * @author harold.murcia
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FileUploadIntegrationTests {

	/**
	 * Rest template for integration test
	 */
    @Autowired
    private TestRestTemplate restTemplate;

    /**
     * Storage service entity
     */
    @Autowired
    private StorageService storageService;
    
    /**
     * Number port for test server
     */
    @LocalServerPort
    private int port;

    /**
     * Test the file upload service the file execution and also the browser download of an specific file
     * @throws Exception if storage service can´t save file on server
     */
    @Test
    public void shouldUploadFile() throws Exception {
    	
    	ClassPathResource resource = new ClassPathResource("input.in", getClass());
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
        map.add("file", resource);
        ResponseEntity<String> response = this.restTemplate.postForEntity("/", map, String.class);

        assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.FOUND);
        assertThat(response.getHeaders().getLocation().toString()).startsWith("http://localhost:" + this.port + "/");
        
        ResponseEntity<String> response2 = this.restTemplate
                .getForEntity("/files/{filename}", String.class, "output.out");

        assertThat(response2.getStatusCodeValue()).isEqualTo(200);
        assertThat(response2.getHeaders().getFirst(HttpHeaders.CONTENT_DISPOSITION))
                .isEqualTo("attachment; filename=\"output.out\"");
        assertThat(response2.getBody()).isEqualTo("4"+System.getProperty("line.separator")+"4"+System.getProperty("line.separator")+"27"+
                System.getProperty("line.separator")+"0"+System.getProperty("line.separator")+"1"+System.getProperty("line.separator")+"1"+System.getProperty("line.separator"));

        storageService.deleteFile("output.out");
        
        
    }
  

}
