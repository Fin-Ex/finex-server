package ru.finex.core.cluster;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author m0nster.mind
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServerRole {

    public static final String ATTRIBUTE_NAME = "role";

    private String role;

}
