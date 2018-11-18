package com.JCG.writer;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.JCG.model.Users;
import com.JCG.model.UsersRepository;

@Component
public class UserWriter implements ItemWriter<Users> {
	@Autowired
	private UsersRepository usersRepository;

	@Override
	public void write(List<? extends Users> items) throws Exception {
		System.out.println("Inside writer..." + items);
		items.stream().forEach(s -> usersRepository.save(s));

	}

}
