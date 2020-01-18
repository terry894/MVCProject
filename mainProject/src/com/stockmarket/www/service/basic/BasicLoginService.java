package com.stockmarket.www.service.basic;

import com.stockmarket.www.dao.jdbc.JdbcMemberDao;
import com.stockmarket.www.entity.Member;
import com.stockmarket.www.service.LoginService;

public class BasicLoginService implements LoginService{

	JdbcMemberDao memberDao;
	
	public BasicLoginService() {
		memberDao = new JdbcMemberDao();
	}
	
	@Override
	public boolean isValidMember(String email, String pwd) {
		Member member = memberDao.getMemberByEmail(email);
		if(member != null) { 
			if(member.getPassword().equals(pwd))
				return true;
			else
				return false;
		} else
			return false;
	}

	@Override
	public int signUpMember(Member member) {
		int result = memberDao.insertMember(member);
		
		return result;
	}

	@Override
	public int deleteMember() {
		return 0;
	}

	public Member getMember(String email) {
		Member member = memberDao.getMemberByEmail(email);
		if(member != null)
			return member; 
		
		return null;
	}

}
