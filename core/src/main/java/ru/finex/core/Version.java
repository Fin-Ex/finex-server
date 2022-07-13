package ru.finex.core;

import java.util.Objects;

/**
 * @author m0nster.mind
 */
public class Version {

    /**
     * Return manifest value of parameter 'Implementation-Version'.
     * If parameter doesnt exists return 'Unknown' value.
     * @return implementation version or 'Unknown'
     */
    public static String getImplVersion() {
        return Objects.requireNonNullElse(Version.class.getPackage().getImplementationVersion(), "Unknown");
    }

}
