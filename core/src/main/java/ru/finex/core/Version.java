package ru.finex.core;

/**
 * @author m0nster.mind
 */
public class Version {

    /**
     * Return manifest value of parameter 'Implementation-Version'.
     * @return implementation version
     */
    public static String getImplVersion() {
        return Version.class.getPackage().getImplementationVersion();
    }

}
