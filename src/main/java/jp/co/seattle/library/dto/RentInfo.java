package jp.co.seattle.library.dto;

import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * ユーザー情報格納DTO
 *
 */
@Configuration
@Data
public class RentInfo {

    private String rentDate;

    private String returnDate;

    public RentInfo() {

    }

    public RentInfo(String rentDate, String returnDate) {
        this.rentDate = rentDate;
        this.rentDate = returnDate;
    }

}