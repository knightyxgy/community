package com.yxgy.wenda.util;

import com.yxgy.wenda.entity.User;
import org.springframework.stereotype.Component;

/**
 * 持有用户信息，类似代替session对象
 */
@Component
public class HostHolder {

    private ThreadLocal<User> users = new ThreadLocal<>();

    public void setUser(User user) {
        users.set(user);
    }

    public User getUser() {
        return users.get();
    }

    public void clear() {
        users.remove();
    }
}
