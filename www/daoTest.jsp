<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import = "com.DAO.*,
				   com.Entity.*,
				   com.common.*,
				   com.elminster.easydao.db.manager.*" %>	
<%
	DAOSupportSession.getSession().setConnection(JDBCTemplate.getConnection());
	MadeleineDAO dao = (MadeleineDAO)DAOSupportManager.getInstance().getDAO(MadeleineDAO.class);
	System.out.print(dao.select(370));
%>