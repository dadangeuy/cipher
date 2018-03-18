//package com.rizaldi.cipher;
//
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Scanner;
//
//public class FeistelCipher implements Cipherable {
//    private List<String> keys = new LinkedList<>();
//    private String left;
//    private String right;
//
//    @Override
//    public String encrypt(String key, String text) {
//        configure(key, text);
//        for (int i = 0; i < keys.size(); i++) {
//            String tmpLeft = left;
//            left = right;
//            right = xor(tmpLeft, vigenereEncrypt(right, keys.get(i)));
//        }
//        return left + right;
//    }
//
//    @Override
//    public String decrypt(String key, String text) {
//        configure(key, text);
//        for (int i = keys.size() - 1; i >= 0; i--) {
//            String tmpRight = right;
//            right = left;
//            left = xor(tmpRight, vigenereEncrypt(left, keys.get(i)));
//        }
//        return left + right;
//    }
//
//    private void configure(String key, String text) {
//        keys.clear();
//        Scanner in = new Scanner(key);
//        while (in.hasNext()) keys.add(in.next());
//        left = text.substring(0, text.length() / 2);
//        right = text.substring(text.length() / 2, text.length());
//    }
//
//    private String xor(String s, String key) {
//        StringBuilder res = new StringBuilder();
//        for (int i = 0; i < s.length(); i++)
//            res.append((char) (s.charAt(i) ^ key.charAt(i % key.length())));
//        return res.toString();
//    }
//
//    private String vigenereEncrypt(String s, String key) {
//        StringBuilder res = new StringBuilder();
//        for (int i = 0; i < s.length(); i++)
//            res.append((char) (s.charAt(i) + key.charAt(i % key.length())));
//        return res.toString();
//    }
//}
