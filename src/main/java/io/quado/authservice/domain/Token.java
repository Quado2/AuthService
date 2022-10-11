package io.quado.authservice.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Token {
    private TokenType tokenType;
    private String value;
    private Integer duration;
    private String subject;
}
