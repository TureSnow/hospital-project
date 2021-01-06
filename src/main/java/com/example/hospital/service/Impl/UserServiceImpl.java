package com.example.hospital.service.Impl;


import com.example.hospital.dao.NotifyMapper;
import com.example.hospital.dao.UserMapper;
import com.example.hospital.model.Notify;
import com.example.hospital.model.NotifyExample;
import com.example.hospital.model.User;
import com.example.hospital.model.UserExample;
import com.example.hospital.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ftang
 */

@Service
public class UserServiceImpl implements UserService {
    private UserMapper userMapper;
    private NotifyMapper notifyMapper;
    private final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
    int getNowUserId(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = getUserByName(username);
        return user.getId();
    }
    public UserServiceImpl(UserMapper userMapper, NotifyMapper notifyMapper){
        this.userMapper = userMapper;
        this.notifyMapper = notifyMapper;
    }
    public User getUserById(int id){
       return userMapper.selectByPrimaryKey(id);
    }
    public User getUserByName(String name){
        UserExample example =  new UserExample();
        example.or().andNameEqualTo(name);
        List<User> users = userMapper.selectByExample(example);
        if (users.size() == 0)
            return null;
        else return users.get(0);
    }
    public int createNewUser(User user){
        String username = user.getName();
        userMapper.insert(user);
        UserExample example = new UserExample();
        example.or().andNameEqualTo(username);
        int id = userMapper.selectByExample(example).get(0).getId();
        return id;
    }
    public List<Notify> getUnreadNotify(){
        int id=getNowUserId();
        NotifyExample notifyExample = new NotifyExample();
        notifyExample.or().andUserIdEqualTo(id).andIsReadEqualTo("0");
        List<Notify> notifies = notifyMapper.selectByExample(notifyExample);
        //change the unread -> read
        if (notifies.size()>0){
            for (Notify notify:notifies){
                notify.setIsRead("1");
                notifyMapper.updateByPrimaryKey(notify);
            }
        }
        return notifies;
    }
    @Override
    public List<Notify> getReadNotify() {
        int id=getNowUserId();
        NotifyExample notifyExample = new NotifyExample();
        notifyExample.or().andUserIdEqualTo(id).andIsReadEqualTo("1");
        List<Notify> notifies = notifyMapper.selectByExample(notifyExample);
        return notifies;
    }

}
