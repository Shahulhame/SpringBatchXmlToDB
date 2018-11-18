package com.JCG.config;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.xstream.XStreamMarshaller;

import com.JCG.model.Users;
import com.JCG.model.UsersLog;
import com.JCG.model.UsersRepository;
import com.JCG.writer.UserWriter;

@Configuration
@EnableBatchProcessing
public class UserBatchConfiguration {
	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	@Autowired
	EntityManagerFactory emf;

	@Autowired
	UsersRepository usersRepository;
	
	@Autowired
	private UserWriter userWriter;

	private static final Logger log = LoggerFactory.getLogger(UserBatchConfiguration.class);

	@Bean
	public StaxEventItemReader<Users> userItemReader() {

		XStreamMarshaller unmarshaller = new XStreamMarshaller();

		Map<String, Class> aliases = new HashMap<>();
		aliases.put("user", Users.class);
		aliases.put("userlog", UsersLog.class);
		//aliases.put("usercontact", UsersContact.class);

		unmarshaller.setAliases(aliases);

		StaxEventItemReader<Users> reader = new StaxEventItemReader<>();

		reader.setResource(new ClassPathResource("/data/users.xml"));
		String[] fragmentNames= {"user","userslog","userlog"};
		//String[] fragmentNames= {"user","userlog","userscontact"};
		reader.setFragmentRootElementNames(fragmentNames);
		reader.setUnmarshaller(unmarshaller);

		return reader;

	}

	/*@Bean
	public JpaItemWriter<Users> writer() {
		JpaItemWriter<Users> writer = new JpaItemWriter();
		writer.setEntityManagerFactory(emf);
		return writer;
	}*/
	
	/*@Bean
	public UserWriter userWriter() {
		UserWriter writer = new UserWriter();
		return writer;
	}
*/
	@Bean
	public ItemProcessor<Users, Users> processor() {
		return (item) -> {
			System.out.println(item); 
			return item;
		};
	}

	@Bean
	public Job importUserJob(JobExecutionListener listener) {
		return jobBuilderFactory.get("importUserJob1").incrementer(new RunIdIncrementer()).listener(listener)
				.flow(step2()).end().build();
	}

	@Bean
	public Step step2() {
		return stepBuilderFactory.get("step2").<Users, Users>chunk(1).reader(userItemReader())
				.processor(processor()).writer(userWriter).build();
	}

	@Bean
	public JobExecutionListener listener() {
		return new JobExecutionListener() {

			@Override
			public void beforeJob(JobExecution jobExecution) {
				/**
				 * As of now empty but can add some before job conditions
				 */
				log.info("Before job...");
			}

			@Override
			public void afterJob(JobExecution jobExecution) {
				if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
					log.info("!!! JOB FINISHED! Time to verify the results");
					usersRepository.findAll()
							.forEach(user -> log.info("Found <" + user + "> in the database."));
				}
			}
		};
	}
}
