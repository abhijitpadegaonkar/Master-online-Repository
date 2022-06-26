package com.ap.demo.utils;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class GeneralUtility {
	
	public static synchronized String generateUsername(String name) {
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			System.out.println("Iturrupted in generateUsername(): " + e.getMessage());
		}
		return name.substring(0, 5) + (new Date().getTime() + "").substring(8);

	}

	public static String shuffleData(String data) {
		List<String> characters = Arrays.asList(data.split(""));
		Collections.shuffle(characters);
		StringBuilder shuffledData = new StringBuilder();
		for (String character : characters) {
			shuffledData.append(character);
		}
		return shuffledData.toString();
	}

	public static void main(String[] args) {

		Runnable t1 = () -> {
			for (int i = 0; i < 10; i++) {
				System.out.println(i + " t1: " + shuffleData(generateUsername("qwertyuiop")));
			}
		};

		Runnable t2 = () -> {
			for (int i = 0; i < 10; i++) {
				System.out.println(i + " t2: " + shuffleData(generateUsername("qwertyuiop")));
			}
		};

		Runnable t3 = () -> {
			for (int i = 0; i < 10; i++) {
				System.out.println(i + " t3: " + shuffleData(generateUsername("qwertyuiop")));
			}
		};

		new Thread(t1).start();
		new Thread(t2).start();
		new Thread(t3).start();
		
	}
}