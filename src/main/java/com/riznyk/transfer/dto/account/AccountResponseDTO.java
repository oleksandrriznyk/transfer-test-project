package com.riznyk.transfer.dto.account;

import com.riznyk.transfer.entity.Account;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Schema(description = "Account details")
public class AccountResponseDTO {

    @Schema(description = "The unique ID of the account")
    private Long id;
    @Schema(description = "The name associated with the account")
    private String name;
    @Schema(description = "The name associated with the account")
    private BigDecimal balance;

    public static AccountResponseDTO fromEntity(Account account) {
        return new AccountResponseDTO(account.getId(), account.getName(), account.getBalance());
    }

}
