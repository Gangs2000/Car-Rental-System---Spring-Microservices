package com.carrentalsystem.restapi.Repository;


import com.carrentalsystem.restapi.Model.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, String> {

}
