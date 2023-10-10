package com.awesome.park.util;

public class ValidationUtils {

    public static boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber.matches("^(\\+7|8)\\d{10}$");
    }

    public static boolean isValidNameAndSurname(String name, String surname) {
        // Проверка, что и имя, и фамилия не пусты и содержат хотя бы 2 символа
        if (name.length() < 2 || surname.length() < 2) {
            return false;
        }
        // Проверка, что первый символ в имени и фамилии - заглавная буква
        if (!Character.isUpperCase(name.charAt(0)) || !Character.isUpperCase(surname.charAt(0))) {
            return false;
        }
        // Проверка, что остальные символы - буквы кириллицы
        for (int i = 1; i < name.length(); i++) {
            if (!Character.isLetter(name.charAt(i)) || !Character.UnicodeBlock.of(name.charAt(i)).equals(Character.UnicodeBlock.CYRILLIC)) {
                return false;
            }
        }
        for (int i = 1; i < surname.length(); i++) {
            if (!Character.isLetter(surname.charAt(i)) || !Character.UnicodeBlock.of(surname.charAt(i)).equals(Character.UnicodeBlock.CYRILLIC)) {
                return false;
            }
        }
        return true;
    }
}

