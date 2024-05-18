package com.enach.Entity;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "branch_detail")
@Data
public class BranchDetails {

    @Column(name = "Branch_Name")
    private String branchName;
    @Id
    @Column(name = "Email_Id")
    private  String emailId;

}
