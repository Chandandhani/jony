package com.bata.billpunch.login.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bata.billpunch.login.bean.UserRequest;
import com.bata.billpunch.login.bean.UserResponse;
import com.bata.billpunch.login.common.BillPunchConstant;
import com.bata.billpunch.login.config.JwtTokenUtil;
import com.bata.billpunch.login.exception.ValidationException;
import com.bata.billpunch.login.model.UserModel;
import com.bata.billpunch.login.repository.UserRepository;

@Component
@RestController
@CrossOrigin(origins = "*")
public class UserInfoController {

	@Autowired
    private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
    private UserRepository userInfoRepository;

    
    
    @PostMapping("/batabps/user/adduser")
    public ResponseEntity<UserResponse> create(@RequestBody UserRequest userReq, HttpServletRequest request) {
    	
        
    	String loggedinUser=getUserDetail(request);    	
    	
    	UserModel userModel=new UserModel(); 
    	
    	String username = userReq.getUsername();
        if (Boolean.TRUE.equals(userInfoRepository.existsByUsername(username))){

            throw new ValidationException("Username already existed");

        }

        String password = userReq.getPassword();
        String encodedPassword = new BCryptPasswordEncoder().encode(password);
        
        
        userModel.setFullname(userReq.getFullname());
        userModel.setUserrole(userReq.getUserrole());
        userModel.setDepartment(userReq.getDepartment());
        userModel.setDesignation(userReq.getDesignation());
        userModel.setEmailId(userReq.getEmailId());
        userModel.setEmployId(userReq.getEmployId());
        userModel.setMobNo(userReq.getMobNo());
        userModel.setPassword(encodedPassword);
        userModel.setLoginId(userReq.getUsername());
        userModel.setUsername(userReq.getUsername());
        userModel.setStatus(BillPunchConstant.ACTIVE);
        userModel.setCreatedBy(loggedinUser);
        userModel.setUserLevel(userReq.getUserLevel());
        
       userInfoRepository.save(userModel);
       
       UserResponse resp=new UserResponse();
   		resp.setFullname(userModel.getFullname());
   		resp.setUserrole(userModel.getUserrole());
   		resp.setDepartment(userModel.getDepartment());
   		resp.setDesignation(userModel.getDesignation());
   		resp.setEmailId(userModel.getEmailId());
   		resp.setUserLevel(userModel.getUserLevel());
   		resp.setMobNo(userModel.getMobNo());
        return ResponseEntity.ok(resp);
    }
    
    @PostMapping("/batabps/user/updateuser")
    public ResponseEntity<UserResponse> update(@RequestBody UserRequest userReq) {
        
    	UserModel userModel=userInfoRepository.findByUsername(userReq.getUsername());
        
        
        if (userModel!=null && userReq.getUsername()!=null){
            userModel.setFullname(userReq.getFullname());
            userModel.setUserrole(userReq.getUserrole());
            userModel.setDepartment(userReq.getDepartment());
            userModel.setDesignation(userReq.getDesignation());
            userModel.setEmailId(userReq.getEmailId());
            userModel.setEmployId(userReq.getEmployId());
            userModel.setMobNo(userReq.getMobNo());  
            userModel.setUserLevel(userReq.getUserLevel());
            userInfoRepository.save(userModel);
            
            
            UserResponse resp=new UserResponse();
       		resp.setFullname(userModel.getFullname());
       		resp.setUserrole(userModel.getUserrole());
       		resp.setDepartment(userModel.getDepartment());
       		resp.setDesignation(userModel.getDesignation());
       		resp.setEmailId(userModel.getEmailId());
       		resp.setUserLevel(userModel.getUserLevel());
       		resp.setMobNo(userModel.getMobNo());
       		resp.setUsername(userModel.getUsername());
       		return ResponseEntity.ok(resp);

        }
        else if(userReq.getUsername()==null) {
        	
        	throw new ValidationException("Not a valid Username");
        }
        else {
        	throw new ValidationException("Username already existed");
        }

        
        
    }
    
    @GetMapping("/batabps/user/allusers")
    public ResponseEntity<List<UserResponse>> allUsers() {
        
        List<UserModel> userList=userInfoRepository.findAll();
        List<UserResponse> userResponse=new ArrayList<>();
        
        userList.forEach(responseList->{
        	UserResponse resp=new UserResponse();
        	resp.setFullname(responseList.getFullname());
        	resp.setUserrole(responseList.getUserrole());
        	resp.setDepartment(responseList.getDepartment());
        	resp.setDesignation(responseList.getDesignation());
        	resp.setEmailId(responseList.getEmailId());
        	resp.setUserLevel(responseList.getUserLevel());
        	resp.setMobNo(responseList.getMobNo());
        	resp.setUsername(responseList.getUsername());
        	userResponse.add(resp);       	
        	
        });
        
        return ResponseEntity.ok(userResponse);
    }
    
    @PostMapping("/batabps/user/changepwd")
    public Boolean changepwd(@RequestBody UserRequest userReq, HttpServletRequest request) {
    	
    	String loggedinUser=getUserDetail(request);
    	 
    	String password = userReq.getPassword();
    	
    	UserModel userModel=userInfoRepository.findByUsername(loggedinUser);
    	userModel.setPassword(new BCryptPasswordEncoder().encode(password));
    	userInfoRepository.save(userModel);
    	
    	return true;
    }
    
   private String getUserDetail(HttpServletRequest request)
   {
	   final String requestTokenHeader = request.getHeader("Authorization");
       String loggedinUser = null;
       String jwtToken = null;
       
       if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
           jwtToken = requestTokenHeader.substring(7);
           try {
           	loggedinUser = jwtTokenUtil.getUsernameFromToken(jwtToken);
           } catch (Exception e) {
        	   System.out.println("Unable to get JWT Token");
           } 

       }
       return loggedinUser; 
   }
    

}
