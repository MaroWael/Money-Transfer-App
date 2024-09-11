package com.transfer.entity;

import com.transfer.dto.CustomerDTO;
import com.transfer.dto.RegisterCustomerResponse;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;



@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String country;

    @Column(nullable = false)
    private LocalDate dateOfBirth;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToOne(mappedBy = "customer")
    private Account account;


    public RegisterCustomerResponse toResponse() {
        return RegisterCustomerResponse.builder()
                .id(this.id)
                .name(this.name)
                .email(this.email)
                .country(this.country)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .build();
    }

    public CustomerDTO toDTO() {
        return CustomerDTO.builder()
                .id(this.id)
                .name(this.name)
                .email(this.email)
                .country(this.country)
                .dateOfBirth(this.dateOfBirth)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .build();
    }

}
