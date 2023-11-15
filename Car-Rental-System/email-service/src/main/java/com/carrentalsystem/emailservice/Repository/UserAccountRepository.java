package com.carrentalsystem.emailservice.Repository;


import com.carrentalsystem.emailservice.Model.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, String> {

}
