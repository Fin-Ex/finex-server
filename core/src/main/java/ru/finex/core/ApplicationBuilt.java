package ru.finex.core;

/**
 * Слушатель окончания инициализации ядра.
 *
 * @author m0nster.mind
 */
public interface ApplicationBuilt {

    /**
     * Вызывается, когда приложение построено/инициализировано.
     */
    void onApplicationBuilt();

}
