package com.firstarr.net.io;

public class Package {

    public final static short FUNC1 = 10;

    public final static short FUNC2 = 11;

    public final static short DISTINGUISH = 12;

    public final static short DISTINGUISH1 = 13;


    public static void main(String[] args) {
        String s = "2102290604HGL1000145";
        System.out.println(ConvertUtils.BinaryToHexString(s.getBytes()));
    }
}
