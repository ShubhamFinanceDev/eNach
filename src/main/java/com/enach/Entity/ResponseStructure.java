package com.enach.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "mandate_resp_doc")
@Data
public class ResponseStructure {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "respid")
    private Long respId;

    @Column(name = "checksum")
    private  String checkSumVal;

    @Column(name = "msgId")
    private String msgId;

    @Column(name = "status")
    private String status;

    @Column(name = "error_code")
    private String errorCode;

    @Column(name = "error_message")
    private String errorMessage;

    @Column(name = "refid")
    private String refId;

    @Column(name = "filler1")
    private String filler1;

    @Column(name = "filler2")
    private String filler2;

    @Column(name = "filler3")
    private String filler3;

    @Column(name = "filler4")
    private String filler4;

    @Column(name = "filler5")
    private String filler5;

    @Column(name = "filler6")
    private String filler6;

    @Column(name = "filler7")
    private String filler7;

    @Column(name = "filler8")
    private String filler8;

    @Column(name = "filler9")
    private String filler9;

    @Column(name = "filler10")
    private String filler10;



}


