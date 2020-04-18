package com.assignment.parser.model;

import com.univocity.parsers.annotations.Format;
import com.univocity.parsers.annotations.Parsed;


import java.util.Date;
import java.util.StringJoiner;

import static com.assignment.parser.config.ParserConfig.HEADER_NAME;
import static com.assignment.parser.config.ParserConfig.HEADER_POSTCODE;
import static com.assignment.parser.config.ParserConfig.HEADER_ADDRESS;
import static com.assignment.parser.config.ParserConfig.HEADER_PHONE;
import static com.assignment.parser.config.ParserConfig.HEADER_CREDIT_LIMIT;
import static com.assignment.parser.config.ParserConfig.HEADER_BIRTHDAY;

public class AccountData {
    @Parsed(field = HEADER_NAME)
    private String name;
    @Parsed(field = HEADER_POSTCODE)
    private String postCode;
    @Parsed(field = HEADER_ADDRESS)
    private String address;
    @Parsed(field = HEADER_PHONE)
    private String phone;
    @Parsed(field = HEADER_CREDIT_LIMIT)
    private float creditLimit;
    @Parsed(field = HEADER_BIRTHDAY)
    @Format(formats = {"yyyyMMdd","dd/MM/yyyy"})
    private Date birthday;

    public String getName() {
        return name;
    }

    public String getPostCode() {
        return postCode;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public float getCreditLimit() {
        return creditLimit;
    }

    public Date getBirthday() {
        return birthday;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", AccountData.class.getSimpleName() + "[", "]")
                .add("name='" + name + "'")
                .add("postCode='" + postCode + "'")
                .add("address='" + address + "'")
                .add("phone='" + phone + "'")
                .add("creditLimit=" + creditLimit)
                .add("birthday=" + birthday)
                .toString();
    }
}
