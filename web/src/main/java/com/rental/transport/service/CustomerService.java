package com.rental.transport.service;

import com.rental.transport.utils.exceptions.ObjectNotFoundException;
import java.util.Arrays;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class CustomerService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws ObjectNotFoundException {

        Boolean exist = false;

        try {
            exist = NetworkService
                    .getInstance(username)
                    .getRegistrationApi()
                    .doGetCustomerExist(username)
                    .execute()
                    .body();
        }
        catch (Exception e) {

        }

        if (exist == false) {
            throw new ObjectNotFoundException("Account", username);
        }

        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_TRANSPORT");
        User user = new User( username, "", Arrays.asList(authority) );
        return user;
    }
}
