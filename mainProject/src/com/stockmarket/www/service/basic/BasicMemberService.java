package com.stockmarket.www.service.basic;

import java.util.List;

import com.stockmarket.www.dao.MemberDao;
import com.stockmarket.www.dao.jdbc.JdbcMemberDao;
import com.stockmarket.www.entity.Member;
import com.stockmarket.www.service.MemberService;

public class BasicMemberService implements MemberService {
	private MemberDao memberDao;
	
	public BasicMemberService() {
		memberDao = new JdbcMemberDao();
	}
	
	@Override
	public List<Member> getMemberList() {
		return memberDao.getRankerList();
	}

	@Override
	public Member getMember(int id) {
		return memberDao.getMember(id);
	}

	@Override
	public int updateMember(int id, int profileImg, String imgChange) {
		return memberDao.updateMember(id, profileImg, imgChange);
	}

	@Override
	public int updateMember(int id, String newPwd, String pwdChange) {
		return memberDao.updateMember(id, newPwd, pwdChange);
		}

	@Override
	public Boolean isDuplicatedId(String nickname) {
		return memberDao.isDuplicatedId(nickname);
	}
}
